package com.example.demo.exception;


import com.example.demo.result.ErrorCode;

/**
 * 定义异常类的信息
 *      有了这个类之后只要自定义自己想要的异常类, 再帮父类构造一下构造方法即可
 */
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ErrorCode message;

    public CustomException(ErrorCode message) {
        super(message.toString());
        this.message = message;
    }

    public ErrorCode getErrorCode() {
        return message;
    }

}

