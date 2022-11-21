package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 陈子豪
 * Date: 2022-05-06
 * Time: 19:53
 */

/**
 * springboot 全局配置
 */
@Configuration
public class SpringWebMvcConfigurer implements WebMvcConfigurer {

    /**
     * 拦截哪些资源和页面
     * @param registry 帮助配置映射截取程序列表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/**/*.js")
                .excludePathPatterns("/**/*.css")
                .excludePathPatterns("/**/*.jpg")
                .excludePathPatterns("/**/*.png")
                .excludePathPatterns("/**/*.jpeg")
                .excludePathPatterns("/**/online_oj_login.html")
                .excludePathPatterns("/**/online_oj_register.html")
                .excludePathPatterns("/**/login")
                .excludePathPatterns("/**/register");
    }
}
