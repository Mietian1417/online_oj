package com.example.demo.model;

import lombok.Data;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 陈子豪
 * Date: 2022-05-06
 * Time: 9:50
 */

@Data
public class User {
    private int id;
    private String username;
    private String password;
    // 1 为 管理用户, 0 为 普通用户
    private int isAdmin;
}
