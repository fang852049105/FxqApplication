package com.fxq.designmethod.decotator;

/**
 * 具体的组件角色，是被装饰的具体对象，就是我们要动态添加行为的类。
 * @author huiguo
 * @date 2019/1/16
 */
public class PersonComponent implements Component {

    @Override
    public void decorate() {
        System.out.println("我穿了一件T恤，一条牛仔，一双奥康皮鞋");
    }
}
