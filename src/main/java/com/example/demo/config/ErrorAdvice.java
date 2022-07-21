package com.example.demo.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 陈子豪
 * Date: 2022-05-06
 * Time: 11:47
 */

// 我们只处理需要处理的异常, 那些访问出错的异常状态, 关于代码编译运行的状态我们要明确返回给用户, 以便用户知道自己代码的出错信息.
@ControllerAdvice
@ResponseBody
public class ErrorAdvice {

    @ExceptionHandler(ProblemNotFoundException.class)
    public HashMap<String, Object> problemNotFoundException(){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", -1);
        hashMap.put("message", "未找到相关 id 的题目! ");
        return hashMap;
    }

    @ExceptionHandler(NotFoundAnyCodeException.class)
    public HashMap<String, Object> notFoundAnyCodeException(){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", -1);
        hashMap.put("message", "该用户尚未提交过任何可执行的代码! ");
        return hashMap;
    }

    @ExceptionHandler(AddProblemFailException.class)
    public HashMap<String, Object> addProblemFailException(){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", -1);
        hashMap.put("message", "添加题目不允许存在任何空的提交项! ");
        return hashMap;
    }
}
