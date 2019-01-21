package com.fxq.designmethod.strategy;

/**
 * 具体策略类
 * @author huiguo
 * @date 2019/1/16
 */
public class CheckAnswer implements IStrategy {

    @Override
    public void doAction() {
        System.out.println("查看答案");
    }
}
