package com.example.fangxq.myapplication.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author huiguo
 * @date 2018/11/7
 */
@Retention(RetentionPolicy.RUNTIME) //注解存活的时间
@Target(ElementType.TYPE)   //注解可以出现的地方
public @interface BindContentView {
}
