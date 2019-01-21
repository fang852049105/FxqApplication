package com.fxq.designmethod.decotator;

/**
 * @author huiguo
 * @date 2019/1/16
 */
public class SweaterDecorator extends Decorator {

    public SweaterDecorator(Component component) {
        super(component);
    }

    @Override
    public void decorate() {
        super.decorate();
        System.out.println("加了件毛衣");
    }
}

