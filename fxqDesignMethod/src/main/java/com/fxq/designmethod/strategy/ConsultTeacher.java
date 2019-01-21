package com.fxq.designmethod.strategy;

/**
 * @author huiguo
 * @date 2019/1/16
 */
public class ConsultTeacher implements IStrategy {

    @Override
    public void doAction() {
        System.out.println("请教老师");
    }
}
