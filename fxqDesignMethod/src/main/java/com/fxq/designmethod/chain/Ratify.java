package com.fxq.designmethod.chain;

/**
 * @author huiguo
 * @date 2018/12/21
 * 处理Request和获取请求结果Result
 * 为什么Chain定义为内部接口，而不在外部定义？考虑两者的关系，提高内聚性，起始分开
 * Chain接口是责任链模式的精髓：转发功能可动态扩展＂责任人＂
 */
public interface Ratify {
    // 处理请求
    Result deal(Chain chain);

    /**
     * 接口描述：对request和Result封装，用来转发
     */
    interface Chain {
        // 获取当前request
        Request request();

        // 转发request
        Result proceed(Request request);
    }
}
