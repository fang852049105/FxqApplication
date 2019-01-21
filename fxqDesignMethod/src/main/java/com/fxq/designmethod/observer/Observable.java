package com.fxq.designmethod.observer;

/**
 * 场景：比如我们要去学校图书馆借书，但是图书馆这类书没有了，那么我们就在图书馆登记一下，然后图书馆这类书有人还了就通知这几个登记过的人说来借阅吧
 * 被观察者接口
 * @author huiguo
 * @date 2019/1/16
 */
public interface Observable {
    void addObserver(Observer observer);
    void deleteObserver(Observer observer);
    void notifyObservers(String info);
}

