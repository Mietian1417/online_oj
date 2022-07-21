package com.example.demo.controller;

import com.example.demo.config.AddProblemFailException;
import com.example.demo.config.NotFoundAnyCodeException;
import com.example.demo.config.ProblemNotFoundException;
import com.example.demo.model.Answer;
import com.example.demo.model.Problem;

import com.example.demo.model.User;
import com.example.demo.service.ProblemService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
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
    public Object register(String username, String password) {
        HashMap<String, Object> hashMap = new HashMap<>();

        if (username == null || "".equals(username) || password == null || "".equals(password)) {
            hashMap.put("message", "用户名或密码为空! ");
            hashMap.put("status", -1);
            return hashMap;
        }

        User user = userService.selectByName(username);
        if (user != null) {
            hashMap.put("status", -1);
            hashMap.put("message", "用户名已存在! ");
            return hashMap;
        }
        int ret = userService.addUser(username, password);
        if (ret != 1) {
            hashMap.put("status", -1);
            hashMap.put("message", "添加用户失败, 请稍后再试! ");
            return hashMap;
        }
        return 1;
    }

    @RequestMapping("/login")
    public Object login(String username, String password, HttpServletRequest request) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (username == null || "".equals(username) || password == null || "".equals(password)) {
            hashMap.put("message", "用户名或密码为空! ");
            hashMap.put("status", -1);
            return hashMap;
        }

        User user = userService.selectByName(username);
        if (user == null || !user.getPassword().equals(password)) {
            hashMap.put("message", "用户名或密码错误! ");
            hashMap.put("status", -1);
            return hashMap;
        }

        HttpSession session = request.getSession(true);
        if (session != null) {
            session.setAttribute("user", user);
        }

        return 1;
    }

    @RequestMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        session.removeAttribute("user");
        response.sendRedirect("/online_oj_login.html");
    }

    @RequestMapping("/list")
    public HashMap<String, Object> getProblemList(@SessionAttribute("user") User user) {
        HashMap<String, Object> hashMap = new HashMap<>();

        HashMap<String, Object> message = new HashMap<>();
        message.put("data", problemService.getProblemList());
        message.put("isAdmin", user.getIsAdmin());

        hashMap.put("status", 1);
        hashMap.put("message", message);
        return hashMap;
    }

    @RequestMapping("/detail")
    public HashMap<String, Object> getDetail(Integer problemID, @SessionAttribute("user") User user) throws ProblemNotFoundException {
        if (problemID == null || problemID <= 0) {
            throw new ProblemNotFoundException();
        }

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", 1);

        HashMap<String, Object> message = new HashMap<>();
        message.put("data", problemService.getProblemByID(problemID));
        message.put("isAdmin", user.getIsAdmin());

        hashMap.put("message", message);
        return hashMap;
    }

    @RequestMapping("/submit")
    public Answer submitCode(Integer problemID, String code, @SessionAttribute("user") User user) throws ProblemNotFoundException {
        System.out.println(user.toString());
        System.out.println(user.toString());
        System.out.println(user.toString());
        if (problemID == null || problemID <= 0) {
            throw new ProblemNotFoundException();
        }

        Problem problem = problemService.getProblemByID(problemID);
        if (problem == null) {
            throw new ProblemNotFoundException();
        }

        String testCode = problem.getTestCode();
        return problemService.submitAndSaveCode(user.getId(), problemID, testCode, code);
    }

    @RequestMapping("/loadLastSubmitCode")
    public HashMap<String, Object> getLastSubmitCode(Integer problemID, @SessionAttribute("user") User user) throws ProblemNotFoundException, NotFoundAnyCodeException {
        if (problemID == null || problemID <= 0) {
            throw new ProblemNotFoundException();
        }

        Problem problem = problemService.getProblemByID(problemID);
        if (problem == null) {
            throw new ProblemNotFoundException();
        }
        String lastSubmitCode = userService.getLastSubmitCode(problemID, user.getId());
        if (lastSubmitCode == null) {
            throw new NotFoundAnyCodeException();
        }

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", 1);
        hashMap.put("message", lastSubmitCode);
        return hashMap;
    }

    @RequestMapping("/loadReferenceAnswer")
    public HashMap<String, Object> getReferenceAnswer(Integer problemID) throws ProblemNotFoundException, NotFoundAnyCodeException {
        if (problemID == null || problemID <= 0) {
            throw new ProblemNotFoundException();
        }

        Problem problem = problemService.getProblemByID(problemID);
        if (problem == null) {
            throw new ProblemNotFoundException();
        }

        String referenceAnswer = problemService.getReferenceAnswer(problemID);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", 1);
        hashMap.put("message", referenceAnswer);
        return hashMap;
    }

    @RequestMapping("/addProblem")
    public Integer addProblem(String title, String level, String description,
                              String templateCode, String testCode, String referenceCode) throws AddProblemFailException {
        if (title == null || "".equals(title) ||
                level == null || "".equals(level) ||
                description == null || "".equals(description.replaceAll(" ","").replaceAll("\n","")) ||
                templateCode == null || "".equals(templateCode.replaceAll(" ","").replaceAll("\n","")) ||
                testCode == null || "".equals(testCode.replaceAll(" ","").replaceAll("\n","")) ||
                referenceCode == null || "".equals(referenceCode.replaceAll(" ","").replaceAll("\n",""))
        ) {
            throw new AddProblemFailException();
        }

        Problem problem = new Problem();
        problem.setTitle(title);
        problem.setLevel(level);
        problem.setDescription(description);
        problem.setTemplateCode(templateCode);
        problem.setTestCode(testCode);
        problem.setReferenceCode(referenceCode);
        return problemService.addProblem(problem.getTitle(), problem.getLevel(), problem.getDescription(),
                problem.getTemplateCode(), problem.getTestCode(), problem.getReferenceCode());
    }

    @RequestMapping("/deleteProblem")
    public Integer deleteProblem(Integer problemID) throws ProblemNotFoundException {
        if (problemID == null || problemID <= 0){
            throw new ProblemNotFoundException();
        }
        Problem problem = problemService.getProblemByID(problemID);
        if (problem == null){
            throw new ProblemNotFoundException();
        }
        return problemService.deleteProblemByID(problemID);
    }


}
