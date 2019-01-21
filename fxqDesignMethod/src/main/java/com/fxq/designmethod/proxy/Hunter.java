package com.fxq.designmethod.proxy;

/**
 * 真实主题
 * @author huiguo
 * @date 2019/1/16
 */
public class Hunter extends Subject{
    @Override
    public String houseHunting() {
        String info = "我要找一间房子,房子的价格在2000-3000之间三室一厅";
        return info;
    }
}

