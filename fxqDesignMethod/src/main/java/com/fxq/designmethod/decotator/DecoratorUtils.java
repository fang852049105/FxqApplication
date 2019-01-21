package com.fxq.designmethod.decotator;

/**
 * @author huiguo
 * @date 2019/1/16
 */
public class DecoratorUtils {

    // 装饰模式的使用
    public void decorate() {
        Decorator hatDecorator = new HatDecorator(new SweaterDecorator(new PersonComponent()));
        hatDecorator.decorate();
    }
}
