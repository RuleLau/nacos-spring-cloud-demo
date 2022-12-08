package com.rule.demo;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LrHystrixCommand {

    /**
     * 默认超时时间
     *
     * @return
     */
    int timeout() default 1000;

    /**
     * 回退方法
     *
     * @return
     */
    String fallback() default "";
}
