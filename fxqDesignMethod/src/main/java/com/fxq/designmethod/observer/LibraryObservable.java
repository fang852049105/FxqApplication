package com.fxq.designmethod.observer;

import java.util.ArrayList;

/**
 * 被观察者
 * @author huiguo
 * @date 2019/1/16
 */
public class LibraryObservable implements Observable{
    //观察者集合
    private final ArrayList<Observer> observers;

    public LibraryObservable() {
        super();
        observers = new ArrayList<Observer>();
    }

    @Override
    public synchronized void addObserver(Observer observer) {
        if (observer == null)
            throw new NullPointerException();
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public synchronized void deleteObserver(Observer observer) {
        if (observer == null) {
            throw new NullPointerException();
        }

        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String info) {

        if (observers == null || observers.size() <= 0) {
            return;
        }

        for(Observer observer : observers){
            observer.update(info);
        }
    }
}

