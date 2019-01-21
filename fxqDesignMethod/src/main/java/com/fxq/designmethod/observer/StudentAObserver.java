package com.fxq.designmethod.observer;

/**
 * 观察者
 * @author huiguo
 * @date 2019/1/16
 */
public class StudentAObserver implements Observer {

    @Override
    public void update(String info) {
        System.out.println(info);
    }
}

