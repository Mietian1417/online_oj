package com.example.demo.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Date: 2022-11-18
 * Time: 12:27
 *
 * @author 陈子豪
 */

/**
 * 该类是添加题目参数校验类, 约定了参数的要求
 *      GroupSeq 接口表示注解的执行顺序
 */
@Data
public class ProblemParam {

    // 用来接收返回自增 id
    private int id;

    @NotEmpty(message = "题目标题不能为空! ", groups = {GroupSeq.First.class})
    private String title;

    @NotEmpty(message = "题目难度不能为空! ",groups = {GroupSeq.Second.class})
    private String level;

    @NotEmpty(message = "题目描述不能为空! ", groups = {GroupSeq.Third.class})
    private String description;

    @NotEmpty(message = "题目模板代码不能为空! ", groups = {GroupSeq.Fourth.class})
    private String templateCode;

    @NotEmpty(message = "题目测试代码不能为空! ", groups = {GroupSeq.Fifth.class})
    private String testCode;

    @NotEmpty(message = "题目参考答案不能为空! ", groups = {GroupSeq.Sixth.class})
    private String referenceCode;
}
