package com.example.demo.redis;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Date: 2022-12-07
 * Time: 15:43
 *
 * @author 陈子豪
 */


public class UserKey extends BasePrefix {
    public UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getAllUsers = new UserKey("users");
}
