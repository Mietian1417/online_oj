package com.example.demo.exception;

import com.example.demo.result.ErrorCode;
import com.example.demo.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 统一异常处理
 */


/**
 * BindException 该异常是参数校验异常, 是自定义规则触发的异常
 */
@ControllerAdvice
@ResponseBody
public class CustomExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e) {

        e.printStackTrace();
        // 自定义异常
        if (e instanceof CustomException) {
            CustomException exception = (CustomException) e;
            return Result.error(exception.getErrorCode());

        // 参数校验异常
        } else if (e instanceof BindException) {
            BindException  exception = (BindException) e;
            List<ObjectError> errors = exception.getAllErrors();
            ObjectError error = errors.get(0);
            return Result.error(ErrorCode.BIND_ERROR.fillArgs(error.getDefaultMessage()));

        // 未知的异常, 返回服务器异常
        } else {
            return Result.error(ErrorCode.SERVER_ERROR);
        }
    }
}
