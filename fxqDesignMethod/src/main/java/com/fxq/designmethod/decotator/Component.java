package com.fxq.designmethod.decotator;

/**
 * 装饰模式和代理模式有点相似，使用上有所不同。
 * 在我的理解，代理侧重被代理对象的功能修改，同时限制了其他类对被代理对象的访问。
 * 装饰模式更侧重被装饰对象功能的嵌套，增强且不限制其他类对被装饰对象的访问。
 *
 * 应用场景：比如你今天要去参加一个party，你挑了一件T恤，一条牛仔，一双奥康皮鞋准备出席。然后出门发现天气挺冷，所以进门加了件毛衣，然后走到路上，发现大家穿着时髦，所以想了想，赶紧去店里买了个帽子，然后出席了party
 * @author huiguo
 * @date 2019/1/16
 */

/**
 * 抽象组件角色，可以是一个接口或抽象类，是被装饰的原始对象;
 */
public interface Component {
    void decorate();
}