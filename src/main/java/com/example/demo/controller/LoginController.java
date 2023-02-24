package com.example.demo.controller;

import com.example.demo.config.VerificationCode;
import com.example.demo.model.User;
import com.example.demo.redis.UserKey;
import com.example.demo.result.ErrorCode;
import com.example.demo.result.Result;
import com.example.demo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotEmpty;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Date: 2022-12-05
 * Time: 8:59
 *
 * @author 陈子豪
 */

@RestController
@Validated
public class LoginController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private VerificationCode verificationCode;

    @RequestMapping("/login")
    public Result<String> login(@NotEmpty(message = "用户名不能为空! ") String username,
                                @NotEmpty(message = "密码不能为空! ") String password,
                                String code, HttpServletRequest request) {

        // 验证码验证
        HttpSession session = request.getSession(false);
        String verifyCode = (String) session.getAttribute("verifyCode");
        if (code == null || !verifyCode.toLowerCase().equals(code.toLowerCase())) {
            return Result.error(ErrorCode.VERIFYCODECODE_ERROR);
        }

        // 用户名密码验证(密码前端已经进行 md5 加密)
        List<User> userList = redisService.get(UserKey.getAllUsers, "", List.class);
        for (User user : userList) {
           if(user.getUsername().equals(username) && user.getPassword().equals(password)){
               // 保存用户信息
               User userSession = new User();
               userSession.setId(user.getId());
               userSession.setUsername(username);
               userSession.setPassword(password);
               userSession.setIsAdmin(user.getIsAdmin());
               session.setAttribute("user", userSession);
               return Result.success("");
           }
        }
        return Result.error(ErrorCode.USER_OR_PASSWORD_ERROR);
    }

    @RequestMapping("/login/verificationCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        BufferedImage image = verificationCode.getImage();
        String text = verificationCode.getText();
        // text 验证码字符串存 session, 方便后面校验比对
        // 这里新建一个 session, 后续的存储数据都放在这个 session 中
        HttpSession session = request.getSession(true);
        session.setAttribute("verifyCode", text);
        VerificationCode.output(image, resp.getOutputStream());
    }

    @RequestMapping("/logout")
    public void logout(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        // 删除用户信息
        HttpSession session = request.getSession(false);
        session.removeAttribute("user");
        // 重定向到登录页面
        response.sendRedirect("/online_oj_login.html");
    }
}
