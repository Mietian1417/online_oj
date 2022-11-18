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

/**
 * 该类是自定义注解 @PasswordValid 的校验流程
 */
public class LoginPasswordValidator implements ConstraintValidator<PasswordValid, String> {

    // 是否强制校验
    private boolean required = false;

    /**
     * 初始化, 对该注解是否可以为空进行传值
     * @param constraintAnnotation 自定义注解类
     */
    @Override
    public void initialize(PasswordValid constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    /**
     * 匹配规则
     * @param value 登录密码
     * @param constraintValidatorContext 自定义新的错误信息,
     *                                   由于 @PasswordValid 注解已经约定好了错误信息, 所以该参数不用配置
     * @return 返回密码是否通过校验
     */
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
