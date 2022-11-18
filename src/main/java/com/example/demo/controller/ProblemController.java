package com.example.demo.controller;

import com.example.demo.model.Answer;
import com.example.demo.model.Problem;
import com.example.demo.model.User;
import com.example.demo.param.GroupSeq;
import com.example.demo.param.ProblemParam;
import com.example.demo.param.UserParam;
import com.example.demo.redis.ProblemKey;
import com.example.demo.redis.ProblemsKey;
import com.example.demo.redis.RedisCacheTime;
import com.example.demo.redis.RedisService;
import com.example.demo.result.ErrorCode;
import com.example.demo.result.Result;
import com.example.demo.service.ProblemService;
import com.example.demo.service.UserService;
import com.example.demo.vo.IsAdminAndList;
import com.example.demo.vo.IsAdminAndProblem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 陈子豪
 * Date: 2022-05-06
 * Time: 9:36
 */

/**
 * @Validated 开启 controller 参数校验
 */
@RequestMapping("/online_oj")
@RestController
@Validated
public class ProblemController {
    @Autowired
    private ProblemService problemService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;


    /**
     * 初始化, 将所有的题目加载到缓存中
     */
    @PostConstruct
    public void loadProblemsToRedis() {
        List<Problem> problemList = problemService.getProblemList();
        for (Problem simpleProblem : problemList) {
            Problem problem = problemService.getProblemById(simpleProblem.getId());
            redisService.set(ProblemKey.getProblem, problem.getId() + "", problem, RedisCacheTime.DETAIL_PROBLEM_CACHE_TIME);
        }

    }

    @RequestMapping("/register")
    public Result<String> register(@Validated({GroupSeq.class}) UserParam userParam) {

        User user = userService.selectByName(userParam.getUsername());
        if (user != null) {
            return Result.error(ErrorCode.USER_EXISTS);
        }
        int ret = userService.addUser(userParam.getUsername(), userParam.getPassword());
        if (ret != 1) {
            return Result.error(ErrorCode.USER_ADD_FAIL);
        }
        return Result.success("");
    }

    @RequestMapping("/login")
    public Result<String> login(@Validated({GroupSeq.class}) UserParam userParam,
                                HttpServletRequest request) {

        User user = userService.selectByName(userParam.getUsername());
        if (user == null || !user.getPassword().equals(userParam.getPassword())) {
            return Result.error(ErrorCode.USER_OR_PASSWORD_ERROR);
        }

        HttpSession session = request.getSession(true);
        if (session != null) {
            session.setAttribute("user", user);
        }
        return Result.success("");
    }

    @RequestMapping("/logout")
    public void logout(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        session.removeAttribute("user");
        response.sendRedirect("/online_oj_login.html");
    }

    @RequestMapping("/list")
    public Result<IsAdminAndList> getProblemList(@SessionAttribute("user") User user) {
        // 查看 redis 是否存在, 存在直接返回
        IsAdminAndList redisISAdminAndList = redisService.get(ProblemsKey.getProblems, "", IsAdminAndList.class);
        if (redisISAdminAndList != null) {
            return Result.success(redisISAdminAndList);
        }

        List<Problem> problemList = problemService.getProblemList();
        IsAdminAndList isAdminAndList = new IsAdminAndList();
        isAdminAndList.setIsAdmin(user.getIsAdmin());
        isAdminAndList.setProblemList(problemList);

        // redis 不存在, 建立缓存
        redisService.set(ProblemsKey.getProblems, "", isAdminAndList, RedisCacheTime.PROBLEMS_CACHE_TIME);
        return Result.success(isAdminAndList);
    }

    @RequestMapping("/detail")
    public Result<IsAdminAndProblem> getDetail(@Min(value = 1, message = ("题目 id 不存在! "))
                                               @NotNull(message = "题目 id 不存在! ") Integer problemId,
                                               @SessionAttribute("user") User user) {

        // 查看 redis 是否存在, 存在直接返回
        Problem redisProblem = redisService.get(ProblemKey.getProblem, problemId + "", Problem.class);
        if (redisProblem != null) {
            IsAdminAndProblem isAdminAndProblem = new IsAdminAndProblem();
            isAdminAndProblem.setIsAdmin(user.getIsAdmin());
            isAdminAndProblem.setProblem(redisProblem);
            return Result.success(isAdminAndProblem);
        }

        Problem problem = problemService.getProblemById(problemId);
        if (problem == null) {
            return Result.error(ErrorCode.PROBLEM_IS_NOT_EXISTS);
        }

        IsAdminAndProblem isAdminAndProblem = new IsAdminAndProblem();
        isAdminAndProblem.setIsAdmin(user.getIsAdmin());
        isAdminAndProblem.setProblem(problem);
        // redis 不存在, 建立缓存
        redisService.set(ProblemKey.getProblem, problemId + "", problem, RedisCacheTime.DETAIL_PROBLEM_CACHE_TIME);
        return Result.success(isAdminAndProblem);
    }

    @RequestMapping("/submit")
    public Result<Answer> submitCode(@Min(value = 1, message = ("题目 id 不存在! "))
                                     @NotNull(message = "题目 id 不存在! ") Integer problemId,
                                     @RequestParam("code") String code,
                                     @SessionAttribute("user") User user) {

        Problem problem = problemService.getProblemById(problemId);
        if (problem == null) {
            return Result.error(ErrorCode.PROBLEM_IS_NOT_EXISTS);
        }

        String testCode = problem.getTestCode();
        Answer answer = problemService.submitAndSaveCode(user.getId(), problemId, testCode, code);
        return Result.success(answer);
    }

    @RequestMapping("/loadLastSubmitCode")
    public Result<String> getLastSubmitCode(@Min(value = 1, message = ("题目 id 不存在! "))
                                            @NotNull(message = "题目 id 不存在! ") Integer problemId,
                                            @SessionAttribute("user") User user) {

        Problem problem = problemService.getProblemById(problemId);
        if (problem == null) {
            return Result.error(ErrorCode.PROBLEM_IS_NOT_EXISTS);
        }
        String lastSubmitCode = userService.getLastSubmitCode(problemId, user.getId());
        if (lastSubmitCode == null) {
            return Result.error(ErrorCode.USER_NOT_SUBMIT_ANY_CODE);
        }
        return Result.success(lastSubmitCode);
    }

    @RequestMapping("/loadReferenceAnswer")
    public Result<String> getReferenceAnswer(@Min(value = 1, message = ("题目 id 不存在! "))
                                             @NotNull(message = "题目 id 不存在! ") Integer problemId) {

        // 查看 redis 是否存在, 存在直接返回
        Problem redisProblem = redisService.get(ProblemKey.getProblem, problemId + "", Problem.class);
        if (redisProblem != null) {
            return Result.success(redisProblem.getReferenceCode());
        }

        Problem problem = problemService.getProblemById(problemId);
        if (problem == null) {
            return Result.error(ErrorCode.PROBLEM_IS_NOT_EXISTS);
        }

        String referenceAnswer = problemService.getReferenceAnswer(problemId);
        return Result.success(referenceAnswer);
    }

    @RequestMapping("/addProblem")
    public Result<Integer> addProblem(@Validated({GroupSeq.class}) ProblemParam problemParam) {

        Integer isSuccess = problemService.addProblem(problemParam.getTitle(), problemParam.getLevel(),
                problemParam.getDescription(), problemParam.getTemplateCode(),
                problemParam.getTestCode(), problemParam.getReferenceCode());
        return isSuccess == 1 ? Result.success(isSuccess) : Result.error(ErrorCode.PROBLEM_ADD_FAIL);
    }

    @RequestMapping("/deleteProblem")
    public Result<Integer> deleteProblem(@Min(value = 1, message = ("题目 id 不存在! "))
                                         @NotNull(message = "题目 id 不存在! ") Integer problemId) {

        Problem problem = problemService.getProblemById(problemId);
        if (problem == null) {
            return Result.error(ErrorCode.PROBLEM_IS_NOT_EXISTS);
        }
        Integer isSuccess = problemService.deleteProblemById(problemId);
        return isSuccess == 1 ? Result.success(isSuccess) : Result.error(ErrorCode.PROBLEM_DELETE_FAIL);
    }
}
