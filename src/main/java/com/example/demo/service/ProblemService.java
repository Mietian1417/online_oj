package com.example.demo.service;

import com.example.demo.model.Answer;
import com.example.demo.model.Question;
import com.example.demo.mapper.ProblemMapper;
import com.example.demo.model.Problem;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 陈子豪
 * Date: 2022-05-06
 * Time: 9:59
 */

@Service
public class ProblemService {
    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private UserService userService;

    public List<Problem> getProblemList() {
        return problemMapper.getProblemList();
    }

    public Problem getProblemByID(Integer problemID) {
        return problemMapper.getProblemByID(problemID);
    }

    public Answer submitAndSaveCode(int userID, int problemID, String testCode, String code) {

        String finalCode = merge(testCode, code);

        if (finalCode == null) {
            Answer answer = new Answer();
            answer.setStatus(1);
            answer.setReason("提交非法代码! ");
            return answer;
        }


        // 运行代码
        Task task = new Task();
        Question question = new Question();
        question.setCode(finalCode);
        Answer answer = task.compileAndRun(question);
        if (answer.getStatus() == 0) {
            // 当用户编译运行没有问题时, 保存(更新)用户当前提交的代码
            userService.saveUserSubmitCode(userID, problemID, code);
        }
        return answer;
    }

    private String merge(String testCode, String submitCode) {
        if (submitCode == null) {
            return null;
        }
        int pos = submitCode.lastIndexOf("}");
        if (pos == -1) {
            return null;
        }

        if (checkMethodIsExist(submitCode)) {
            String str = submitCode.substring(0, pos);
            return str + testCode + "\n}";
        }
        return null;
    }

    private boolean checkMethodIsExist(String submitCode) {
        String test = submitCode.replaceAll(" ", "");
        String test1 = test.replaceAll("\n", "");
        if (test1.charAt(test1.length() - 1) == '}' &&
                test1.charAt(test1.length() - 2) == '}') {
            return true;
        }
        return false;
    }

    public String getReferenceAnswer(Integer problemID) {
        return problemMapper.getReferenceAnswer(problemID);
    }

    public Integer addProblem(String title, String level, String description, String templateCode, String testCode, String referenceCode) {
        return problemMapper.addProblem(title, level, description, templateCode, testCode, referenceCode);
    }

    public Integer deleteProblemByID(Integer problemID) {
        return problemMapper.deleteProblemByID(problemID);
    }
}
