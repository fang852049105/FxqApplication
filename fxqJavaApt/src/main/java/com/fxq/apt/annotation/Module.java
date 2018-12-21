package com.fxq.apt.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author huiguo
 * @date 2018/12/17
 */
@Retention(RetentionPolicy.CLASS)
public @interface Module {
    String value();
}
