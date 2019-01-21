package com.fxq.designmethod.decotator;

/**
 * 具体装饰者类，装饰者可以添加新的方法，而且能在原有的方法前面或者后面添加新的行为。
 * @author huiguo
 * @date 2019/1/16
 */
public class HatDecorator extends Decorator {

    public HatDecorator(Component component) {
        super(component);
    }

    @Override
    public void decorate() {
        super.decorate();
        System.out.println("加个帽子");
    }
}

