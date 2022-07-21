package com.example.demo.model;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 陈子豪
 * Date: 2022-05-06
 * Time: 10:10
 */

@Data
public class Problem {
    private int id;
    private String title;
    private String level;
    private String description;
    private String templateCode;
    private String testCode;
    private String referenceCode;
}
