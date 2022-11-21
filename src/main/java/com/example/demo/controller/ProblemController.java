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
import javax.validation.constraints.Min;
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
 * @Validated 开启 controller 类下方法的参数校验
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

    /**
     * 用户注册
     *
     * @param userParam 参数校验的类
     * @return 注册状态
     * @Validated({GroupSeq.class}) 开启校验类的校验顺序(GroupSeq 为定义校验顺序的接口)
     */
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

    /**
     * 用户登录
     *
     * @param userParam 参数校验的类
     * @param request   客户端的信息
     * @return 登录状态
     * @Validated({GroupSeq.class}) 开启校验类的校验顺序(GroupSeq 为定义校验顺序的接口)
     */
    @RequestMapping("/login")
    public Result<String> login(@Validated({GroupSeq.class}) UserParam userParam,
                                HttpServletRequest request) {

        User user = userService.selectByName(userParam.getUsername());
        if (user == null || !user.getPassword().equals(userParam.getPassword())) {
            return Result.error(ErrorCode.USER_OR_PASSWORD_ERROR);
        }

        // 保存用户信息
        HttpSession session = request.getSession(true);
        if (session != null) {
            session.setAttribute("user", user);
        }
        return Result.success("");
    }

    /**
     * 用户注销
     *
     * @param request  客户端的信息
     * @param response 服务器的信息
     * @throws IOException 重定向方法抛出的异常(找不到对应的页面)
     */
    @RequestMapping("/logout")
    public void logout(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        // 删除用户信息
        HttpSession session = request.getSession(false);
        session.removeAttribute("user");
        // 重定向到登录页面
        response.sendRedirect("/online_oj_login.html");
    }

    /**
     * 展示用户列表
     *
     * @param user 用户信息
     * @return 返回 vo(题目列表 + 是否为管理用户)
     * @SessionAttribute("user") 获取指定字段的用户信息
     */
    @RequestMapping("/list")
    public Result<IsAdminAndList> getProblemList(@SessionAttribute("user") User user) {
        // 查看 redis 是否存在, 存在直接返回
        List<Problem> problems = redisService.get(ProblemsKey.getProblems, user.getId() + "", List.class);
        if (problems != null) {
            IsAdminAndList isAdminAndList = new IsAdminAndList();
            isAdminAndList.setIsAdmin(user.getIsAdmin());
            isAdminAndList.setProblemList(problems);
            return Result.success(isAdminAndList);
        }

        List<Problem> problemList = problemService.getProblemList();
        // 设置该用户的所有的题目状态(是否通过)
        for (Problem problem : problemList) {
            Integer isPass = userService.isPass(user.getId(), problem.getId());
            if (isPass == null) {
                isPass = 0;
            }
            problem.setIsPass(isPass);
        }

        IsAdminAndList isAdminAndList = new IsAdminAndList();
        isAdminAndList.setIsAdmin(user.getIsAdmin());
        isAdminAndList.setProblemList(problemList);

        // redis 不存在, 建立缓存
        redisService.set(ProblemsKey.getProblems, user.getId() + "", problemList, RedisCacheTime.PROBLEMS_CACHE_TIME);
        return Result.success(isAdminAndList);
    }

    /**
     * 展示题目详情页
     *
     * @param problemId 题目 id
     * @param user      用户信息
     * @return 返回 vo(题目详情 + 是否为管理用户)
     * @Validated 注解
     * @Min(value = 1, message = ("题目 id 不存在! "))   int 类型最小校验, value 为最小值, message 为报错信息
     * @NotNull(message = "题目 id 不存在! ")    引用字段的非空校验, message 为报错信息
     * @SessionAttribute("user") 获取指定字段的用户信息
     */
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

    /**
     * 执行用户提交的代码
     *
     * @param problemId 题目 id
     * @param code      用户提交的代码
     * @param user      用户信息
     * @return 返回执行结果
     * @Validated 注解
     * @Min(value = 1, message = ("题目 id 不存在! "))   int 类型最小校验, value 为最小值, message 为报错信息
     * @NotNull(message = "题目 id 不存在! ")    引用字段的非空校验, message 为报错信息
     * @SessionAttribute("user") 获取指定字段的用户信息
     */
    @RequestMapping("/submit")
    public Result<Answer> submitCode(@Min(value = 1, message = ("题目 id 不存在! "))
                                     @NotNull(message = "题目 id 不存在! ") Integer problemId,
                                     @RequestParam("code") String code,
                                     @SessionAttribute("user") User user) {

        Problem problem = problemService.getProblemById(problemId);
        if (problem == null) {
            return Result.error(ErrorCode.PROBLEM_IS_NOT_EXISTS);
        }

        // 获取题目的测试代码, 拼接提交代码进行测试
        String testCode = problem.getTestCode();
        Answer answer = problemService.submitAndSaveCode(user.getId(), problemId, testCode, code);
        
        // 用户提交代码通过全部用例, 标记题目为通过状态
        String stdout = answer.getStdout();
        if (stdout != null) {
            int status = userService.updateUserOfProblemIsPass(stdout, user.getId(), problemId);
            if (status == -1) {
                return Result.error(ErrorCode.SERVER_EXECUTE_CODE_FAIL);
            }
        }

        return Result.success(answer);
    }

    /**
     * 加载用户上一次执行的代码
     *
     * @param problemId 题目 id
     * @param user      用户信息
     * @return 返回上次该用户提交的代码
     * @Validated 注解
     * @Min(value = 1, message = ("题目 id 不存在! "))   int 类型最小校验, value 为最小值, message 为报错信息
     * @NotNull(message = "题目 id 不存在! ")    引用字段的非空校验, message 为报错信息
     * @SessionAttribute("user") 获取指定字段的用户信息
     */
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

    /**
     * 查看参考答案
     *
     * @param problemId 题目 id
     * @return 返回题目的参考答案
     * @Validated 注解
     * @Min(value = 1, message = ("题目 id 不存在! "))   int 类型最小校验, value 为最小值, message 为报错信息
     * @NotNull(message = "题目 id 不存在! ")    引用字段的非空校验, message 为报错信息
     */
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

        return Result.success(problem.getReferenceCode());
    }

    /**
     * 添加题目
     *
     * @param problemParam 题目 id
     * @param user         用户信息
     * @return 返回添加状态
     * @Validated({GroupSeq.class}) 开启校验类的校验顺序(GroupSeq 为定义校验顺序的接口)
     * @SessionAttribute("user") 获取指定字段的用户信息
     */
    @RequestMapping("/addProblem")
    public Result<Integer> addProblem(@Validated({GroupSeq.class}) ProblemParam problemParam,
                                      @SessionAttribute("user") User user) {

        Integer isSuccess = problemService.addProblem(problemParam.getTitle(), problemParam.getLevel(),
                problemParam.getDescription(), problemParam.getTemplateCode(),
                problemParam.getTestCode(), problemParam.getReferenceCode());
        if (isSuccess == null || isSuccess == 0) {
            return Result.error(ErrorCode.PROBLEM_ADD_FAIL);
        }

        // 更新 redis 缓存
        List<Problem> problemList = problemService.getProblemList();
        redisService.set(ProblemsKey.getProblems, user.getId() + "", problemList, RedisCacheTime.PROBLEMS_CACHE_TIME);
        return Result.success(isSuccess);
    }

    /**
     * 删除题目
     *
     * @param problemId 题目 id
     * @param user      用户信息
     * @return 返回删除状态
     * @Validated 注解
     * @Min(value = 1, message = ("题目 id 不存在! "))   int 类型最小校验, value 为最小值, message 为报错信息
     * @NotNull(message = "题目 id 不存在! ")    引用字段的非空校验, message 为报错信息
     * @SessionAttribute("user") 获取指定字段的用户信息
     */
    @RequestMapping("/deleteProblem")
    public Result<Integer> deleteProblem(@Min(value = 1, message = ("题目 id 不存在! "))
                                         @NotNull(message = "题目 id 不存在! ") Integer problemId,
                                         @SessionAttribute("user") User user) {

        Problem problem = problemService.getProblemById(problemId);
        if (problem == null) {
            return Result.error(ErrorCode.PROBLEM_IS_NOT_EXISTS);
        }

        Integer isSuccess = problemService.deleteProblemById(problemId);
        if (isSuccess == null || isSuccess == 0) {
            return Result.error(ErrorCode.PROBLEM_DELETE_FAIL);

        }

        // 更新 redis 缓存
        List<Problem> problemList = problemService.getProblemList();
        redisService.set(ProblemsKey.getProblems, user.getId() + "", problemList, RedisCacheTime.PROBLEMS_CACHE_TIME);
        return Result.success(isSuccess);
    }
}
