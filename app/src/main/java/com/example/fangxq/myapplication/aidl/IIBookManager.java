package com.example.fangxq.myapplication.aidl;

import android.os.IInterface;
import android.os.RemoteException;


import com.example.fangxq.myapplication.Book;

import java.util.List;

/**
 * 手写Binder，声明一个aidl性质的接口
 * @author huiguo
 * @date 2019/1/23
 */
public interface IIBookManager extends IInterface {
    static final String DESCRIPTOR = "com.example.fangxq.myapplication.aidl.IIBookManager";
    static final int TRANSACTION_getBookList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_addBook = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_registerListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_unregisterListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);

    public List<Book> getBookList() throws RemoteException;

    public void addBook(Book book) throws RemoteException;

    public void registerListener(IIOnNewBookArrivedListener listener) throws RemoteException;
    public void unregisterListener(IIOnNewBookArrivedListener listener) throws RemoteException;

}