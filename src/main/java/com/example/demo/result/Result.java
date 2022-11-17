package com.example.demo.result;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Date: 2022-11-15
 * Time: 17:49
 *
 * @author 陈子豪
 */
public class Result<T> {
    public int code;
    public String message;
    public T data;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>(0, "success", data);
    }

    public static <T> Result<T> error(ErrorCode errorCode) {
        return new Result<T>(errorCode.code, errorCode.message);
    }
}
