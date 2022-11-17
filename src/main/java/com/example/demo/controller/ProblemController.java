package com.example.demo.controller;

import com.example.demo.model.Answer;
import com.example.demo.model.Problem;
import com.example.demo.model.User;
import com.example.demo.param.GroupSeq;
import com.example.demo.param.UserParam;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 陈子豪
 * Date: 2022-05-06
 * Time: 9:36
 */

@RequestMapping("/online_oj")
@RestController
public class ProblemController {
    @Autowired
    private ProblemService problemService;

    @Autowired
    private UserService userService;

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
        List<Problem> problemList = problemService.getProblemList();
        IsAdminAndList isAdminAndList = new IsAdminAndList();
        isAdminAndList.setIsAdmin(user.getIsAdmin());
        isAdminAndList.setProblemList(problemList);
        return Result.success(isAdminAndList);
    }

    @RequestMapping("/detail")
    public Result<IsAdminAndProblem> getDetail(@RequestParam("problemId") Integer problemId,
                                               @SessionAttribute("user") User user) {
        if (problemId == null || problemId <= 0) {
            return Result.error(ErrorCode.PROBLEM_IS_NOT_EXISTS);
        }

        Problem problem = problemService.getProblemByID(problemId);
        if (problem == null) {
            return Result.error(ErrorCode.PROBLEM_IS_NOT_EXISTS);
        }
        IsAdminAndProblem isAdminAndProblem = new IsAdminAndProblem();
        isAdminAndProblem.setIsAdmin(user.getIsAdmin());
        isAdminAndProblem.setProblem(problem);
        return Result.success(isAdminAndProblem);
    }

    @RequestMapping("/submit")
    public Result<Answer> submitCode(@RequestParam("problemId") Integer problemId,
                                     @RequestParam("code") String code,
                                     @SessionAttribute("user") User user) {
        if (problemId == null || problemId <= 0) {
            return Result.error(ErrorCode.PROBLEM_IS_NOT_EXISTS);
        }

        Problem problem = problemService.getProblemByID(problemId);
        if (problem == null) {
            return Result.error(ErrorCode.PROBLEM_IS_NOT_EXISTS);
        }

        String testCode = problem.getTestCode();
        Answer answer = problemService.submitAndSaveCode(user.getId(), problemId, testCode, code);
        return Result.success(answer);
    }

    @RequestMapping("/loadLastSubmitCode")
    public Result<String> getLastSubmitCode(@RequestParam("problemId") Integer problemId,
                                            @SessionAttribute("user") User user) {
        if (problemId == null || problemId <= 0) {
            return Result.error(ErrorCode.PROBLEM_IS_NOT_EXISTS);
        }

        Problem problem = problemService.getProblemByID(problemId);
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
    public Result<String> getReferenceAnswer(@RequestParam("problemId") Integer problemId) {
        if (problemId == null || problemId <= 0) {
            return Result.error(ErrorCode.PROBLEM_IS_NOT_EXISTS);
        }

        Problem problem = problemService.getProblemByID(problemId);
        if (problem == null) {
            return Result.error(ErrorCode.PROBLEM_IS_NOT_EXISTS);
        }

        String referenceAnswer = problemService.getReferenceAnswer(problemId);
        return Result.success(referenceAnswer);
    }

    @RequestMapping("/addProblem")
    public Result<Integer> addProblem(@RequestParam("title") String title,
                                      @RequestParam("level") String level,
                                      @RequestParam("description") String description,
                                      @RequestParam("templateCode") String templateCode,
                                      @RequestParam("testCode") String testCode,
                                      @RequestParam("referenceCode") String referenceCode) {
        if (title == null || "".equals(title) ||
                level == null || "".equals(level)
                || description == null || "".equals(description.replaceAll(" ", "").replaceAll("\n", ""))
                || templateCode == null || "".equals(templateCode.replaceAll(" ", "").replaceAll("\n", ""))
                || testCode == null || "".equals(testCode.replaceAll(" ", "").replaceAll("\n", ""))
                || referenceCode == null || "".equals(referenceCode.replaceAll(" ", "").replaceAll("\n", ""))
        ) {
            return Result.error(ErrorCode.SUBMIT_EXISTS_NULL);

        }

        Problem problem = new Problem();
        problem.setTitle(title);
        problem.setLevel(level);
        problem.setDescription(description);
        problem.setTemplateCode(templateCode);
        problem.setTestCode(testCode);
        problem.setReferenceCode(referenceCode);
        Integer isSuccess = problemService.addProblem(problem.getTitle(), problem.getLevel(),
                problem.getDescription(), problem.getTemplateCode(),
                problem.getTestCode(), problem.getReferenceCode());
        return isSuccess == 1 ? Result.success(isSuccess) : Result.error(ErrorCode.DATABASE_OPTION_FAIL);
    }

    @RequestMapping("/deleteProblem")
    public Result<Integer> deleteProblem(@RequestParam("problemId") Integer problemId) {
        if (problemId == null || problemId <= 0) {
            return Result.error(ErrorCode.PROBLEM_IS_NOT_EXISTS);
        }
        Problem problem = problemService.getProblemByID(problemId);
        if (problem == null) {
            return Result.error(ErrorCode.PROBLEM_IS_NOT_EXISTS);
        }
        Integer isSuccess = problemService.deleteProblemByID(problemId);
        return isSuccess == 1 ? Result.success(isSuccess) : Result.error(ErrorCode.DATABASE_OPTION_FAIL);
    }
}
