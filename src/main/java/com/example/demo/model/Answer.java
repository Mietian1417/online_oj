package com.example.demo.model;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 陈子豪
 * Date: 2022-03-07
 * Time: 19:17
 */

/**
 * 这个类表示运行的结果(可能编译出错, 运行出错, 标准输出)
 */
@Data
public class Answer {
    // 用状态码来表示代码执行状况(0 表示正常, 1 表示 编译异常, 2 表示 运行异常, 3 表示其他异常)
    private int status;
    // 出现错误(编译错误或运行异常)
    private String stderr;
    // 正常输出
    private String stdout;
    // 错误原因
    private String reason;
}
