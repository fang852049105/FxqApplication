package com.fxq.designmethod.decotator;

/**
 * 抽象装饰者角色，职责是装饰被装饰的对象。内部一定有一个对被装饰者的引用。一般情况下也是一个抽象类
 * @author huiguo
 * @date 2019/1/16
 */
public abstract class Decorator implements Component {
    private Component component;

    public Decorator(Component component) {
        this.component = component;
    }

    @Override
    public void decorate() {
        if (null != component) {
            component.decorate();
        }
    }
}

