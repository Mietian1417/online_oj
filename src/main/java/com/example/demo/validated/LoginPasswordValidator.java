package com.example.demo.validated;

import com.example.demo.util.ValidatorUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * Created with IntelliJ IDEA.
 * Description:
 * Date: 2022-11-17
 * Time: 10:40
 *
 * @author 陈子豪
 */
public class LoginPasswordValidator implements ConstraintValidator<PasswordValid, String> {
    // 是否强制校验
    private boolean required = false;

    @Override
    public void initialize(PasswordValid constraintAnnotation) {
        // 初始化, 对参数是否可以为空进行传值
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (required) {
            // ValidatorUtil 类封装了校验规则
            return ValidatorUtil.isPasswordToPass(value);
        } else {
            // 如果 required 为 false, 表示 参数可以为空,
            // 如果是空的直接返回 true, 否则进行校验
            if (value.length() == 0) {
                return true;
            } else {
                return ValidatorUtil.isPasswordToPass(value);
            }
        }
    }
}
