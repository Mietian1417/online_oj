package com.example.demo.model;

import lombok.Data;

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
    private int isAdmin;
}
