package com.example.demo.dao;

import com.example.demo.model.User;
import org.apache.ibatis.annotations.Mapper;

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

    int addUser(String username, String password);

    int saveUserSubmitCode(int userID, int problemID, String submitCode);

    String getLastSubmitCode(Integer problemID, int userID);
}
