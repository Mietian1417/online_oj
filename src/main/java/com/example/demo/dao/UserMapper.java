package com.example.demo.dao;

import com.example.demo.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 陈子豪
 * Date: 2022-05-06
 * Time: 9:37
 */

@Mapper
public interface UserMapper {
    User selectByName(String username);

    int addUser(User user);

    int saveUserSubmitCode(int userId, int problemId, String submitCode);

    Integer deleteUserSubmitCode(int userId, int problemId);

    String getLastSubmitCode(Integer problemId, int userId);

    Integer isPass(Integer userId, Integer problemId);

    int setPass(Integer userId, Integer problemId);

    List<User> getAllUsers();
}

