package com.example.demo.service;

import com.example.demo.dao.UserMapper;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public User selectByName(String username) {
        return userMapper.selectByName(username);
    }

    public int addUser(String username, String password) {
        return userMapper.addUser(username, password);
    }

    public int saveUserSubmitCode(int userID, int problemID, String submitCode) {
        return userMapper.saveUserSubmitCode(userID, problemID, submitCode);
    }

    public String getLastSubmitCode(Integer problemID, int userID) {
        return userMapper.getLastSubmitCode(problemID, userID);
    }

}
