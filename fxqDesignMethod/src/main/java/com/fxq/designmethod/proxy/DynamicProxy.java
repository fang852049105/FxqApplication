package com.fxq.designmethod.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 动态代理
 * @author huiguo
 * @date 2019/1/16
 */
public class DynamicProxy implements InvocationHandler {

    private Subject subject;  // Subject 必须是接口

    public DynamicProxy(Subject subject) {
        this.subject = subject;
    }

    @Override
    public String invoke(Object arg0, Method method, Object[] arg2)
            throws Throwable {
        String proxyFare = "我要额外收中介费500块";
        String result = (String) method.invoke(subject,arg2);

        return result +", "+ proxyFare;
    }

}

