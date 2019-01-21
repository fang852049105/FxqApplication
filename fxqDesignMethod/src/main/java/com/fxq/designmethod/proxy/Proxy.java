package com.fxq.designmethod.proxy;

/**
 * 代理
 * @author huiguo
 * @date 2019/1/16
 */
public class Proxy extends Subject{
    private Subject subject;

    public void setHunter(Subject subject){
        this.subject = subject;
    }

    @Override
    public String houseHunting() {
        String proxyFare = "我要额外收中介费500块";
        String infoFromHunter = subject.houseHunting();

        return infoFromHunter +", "+ proxyFare;
    }
}

//使用
//Subject hunter = new Hunter();
//Proxy houseAgency = new Proxy();
//houseAgency.setHunter(hunter);
//String info = houseAgency.houseHunting();
//System.out.println(info);

