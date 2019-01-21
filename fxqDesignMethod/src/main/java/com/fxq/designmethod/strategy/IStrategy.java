package com.fxq.designmethod.strategy;

/**
 * 场景：我们大学编程的时候，遇到一个编程题目解不出来，那么这时候有几种情况可以解决。一个是查看答案，一个是求教同学，一个是请教老师。
 * 策略接口
 * @author huiguo
 * @date 2019/1/16
 */
public interface IStrategy {
    void doAction();
}
