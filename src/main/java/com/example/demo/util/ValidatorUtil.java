package com.example.demo.util;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Date: 2022-11-17
 * Time: 10:49
 *
 * @author 陈子豪
 */
public class ValidatorUtil {

    // 密码规则
    public static boolean isPasswordToPass(String password) {

        // 匹配数字
        boolean isHasNumbers = false;
        // 匹配英文字母
        boolean isHasAlphabet = false;
        // 匹配其他字符
        boolean isHasOtherSymbols = false;

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (ch >= '0' && ch <= '9') {
                isHasNumbers = true;
            }
            if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z') {
                isHasAlphabet = true;
            }
            if (ch == '.' || ch == '@' || ch == '#' || ch == '!' || ch == '！' || ch == '%'
                    || ch == '^' || ch == '&' || ch == '*' || ch == '(' || ch == ')'
                    || ch == '（' || ch == '）' || ch == '/' || ch == '?' || ch == '-'
                    || ch == '+' || ch == '`' || ch == '~') {
                isHasOtherSymbols = true;
            }
        }
        System.out.println("校验--------------------------------------------");
        return isHasNumbers && isHasAlphabet && isHasOtherSymbols;
    }
}
