package com.pighand.aio.common.CAPTCHA;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CAPTCHA {

    String key();

    /**
     * 验证方式
     */
    ModeEnum value() default ModeEnum.ALWAYS;
}
