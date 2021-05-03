package com.rule.common.annotation;

import com.rule.common.entity.SelectBoxView;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Translate {

    String code();

    Class<? extends SelectBoxView> type();
}
