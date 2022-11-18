package com.example.demo.result;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Date: 2022-11-15
 * Time: 18:04
 *
 * @author 陈子豪
 */
public class ErrorCode {
    public int code;
    public String message;

    public ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public static final ErrorCode USER_EXISTS = new ErrorCode(-1, "用户名已存在! ");

    public static final ErrorCode USER_ADD_FAIL = new ErrorCode(-1, "添加用户失败, 请稍后再试! ");

    public static final ErrorCode USER_OR_PASSWORD_ERROR = new ErrorCode(-1, "用户名或密码错误! ");

    public static final ErrorCode PROBLEM_IS_NOT_EXISTS = new ErrorCode(-1, "未找到相关 id 的题目! ");

    public static final ErrorCode USER_NOT_SUBMIT_ANY_CODE = new ErrorCode(-1, "该用户尚未提交过任何可执行的代码! ");


    public static final ErrorCode PROBLEM_ADD_FAIL = new ErrorCode(-1, "题目添加失败!");

    public static final ErrorCode PROBLEM_DELETE_FAIL = new ErrorCode(-1, "题目删除失败!");;



    public static final ErrorCode BIND_ERROR = new ErrorCode(500101, "参数校验异常: %s");

    public static final ErrorCode SERVER_ERROR = new ErrorCode(500100, "服务端异常");


    public ErrorCode fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.message, args);
        return new ErrorCode(code, message);
    }

    @Override
    public String toString() {
        return "ErrorCode{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

    public ErrorCode subArgs(String message) {
        int code = this.code;
        message = message.substring(message.indexOf(":") + 2);
        return new ErrorCode(code, message);
    }
}
