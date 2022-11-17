package com.example.demo.validated;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Date: 2022-11-17
 * Time: 10:34
 *
 * @author 陈子豪
 */


@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {LoginPasswordValidator.class})
public @interface PasswordValid {

    boolean required() default true;

    String message() default "密码必须包含数字, 英文, 以及其它特殊字符! ";

    // 下面这两个属性是必加的
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
