package com.example.demo.param;

import com.example.demo.validated.PasswordValid;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;


/**
 * Created with IntelliJ IDEA.
 * Description:
 * Date: 2022-11-17
 * Time: 11:40
 *
 * @author 陈子豪
 */

/**
 * 该类是登录参数校验类, 约定了参数的要求
 *      GroupSeq 接口表示注解的执行顺序
 */

@Data
public class UserParam {

    @NotEmpty(message = "用户名不能为空! ", groups = {GroupSeq.First.class})
    @Length(min = 2, max = 16, message = "用户名长度必须在 2~16 个字符之间(中文算一个字符)! ", groups = {GroupSeq.Second.class})
    private String username;


    @NotEmpty(message = "密码不能为空! ", groups = {GroupSeq.Third.class})
    @Length(min = 4, max = 16, message = "密码长度必须在 4~16 个字符之间! ", groups = {GroupSeq.Fourth.class})
    @PasswordValid(groups = {GroupSeq.Fifth.class})
    private String password;
}
