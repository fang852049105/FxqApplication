package com.fxq.designmethod.observer;

/**
 * 观察者接口
 * @author huiguo
 * @date 2019/1/16
 */
public interface Observer {
    void update(String info);
}


//使用
//观察者学生
//Observer studentA = new StudentAObserver();
//Observer studentB = new StudentAObserver();
////被观察者图书馆
//Observable library = new LibraryObservable();
////在图书馆登记
//library.addObserver(studentA);
//library.addObserver(studentB);
//
////图书馆有书了通知
//library.notifyObservers("书到了，先到先得");
