package com.example.demo.service;

import com.example.demo.dao.UserMapper;
import com.example.demo.model.Answer;
import com.example.demo.model.Problem;
import com.example.demo.model.User;
import com.example.demo.redis.ProblemsKey;
import com.example.demo.redis.RedisCacheTime;
import com.example.demo.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 陈子豪
 * Date: 2022-05-06
 * Time: 9:36
 */

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    public User selectByName(String username) {
        return userMapper.selectByName(username);
    }

    public int addUser(String username, String password) {
        return userMapper.addUser(username, password);
    }

    public int saveUserSubmitCode(int userId, int problemId, String submitCode) {
        return userMapper.saveUserSubmitCode(userId, problemId, submitCode);
    }

    public String getLastSubmitCode(Integer problemID, int userID) {
        return userMapper.getLastSubmitCode(problemID, userID);
    }

    public Integer isPass(int userId, int problemId) {
        return userMapper.isPass(userId, problemId);
    }

    public Integer setPass(Integer id, Integer problemId) {
        return userMapper.setPass(id, problemId);
    }

    public int updateUserOfProblemIsPass(String stdout, int userId, Integer problemId) {
        // 去除空格, 统计通过个数
        String isOk = stdout.replaceAll("\\s*", "");
        int passTestCount = 0;
        for (int i = 0; i < isOk.length(); i++) {
            if (isOk.charAt(i) == 'o') {
                passTestCount++;
            }
        }
        if (passTestCount == 10) {
            Integer integer = setPass(userId, problemId);
            if (integer == null || integer == 0) {
                return -1;
            }
            // 更新缓存
            List<Problem> problems = redisService.get(ProblemsKey.getProblems, userId + "", List.class);
            for (Problem curProblem : problems) {
                if (curProblem.getId() == problemId) {
                    curProblem.setIsPass(1);
                    redisService.set(ProblemsKey.getProblems, userId + "", problems, RedisCacheTime.PROBLEMS_CACHE_TIME);
                    return 0;
                }
            }
        }
        return 0;
    }
}
