package com.algo4chris.algo4chrisweb.advice.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface HttpRequestLog {

    /** 是否開啟 HttpRequestLog */
    boolean enable() default true;

    HttpRequestElements[] value() default HttpRequestElements.All;
}
