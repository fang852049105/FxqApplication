package com.example.fangxq.myapplication.aidl;

import android.os.IInterface;
import android.os.RemoteException;

import com.example.fangxq.myapplication.Book;

/**
 * @author huiguo
 * @date 2019/2/11
 */
public interface IIOnNewBookArrivedListener extends IInterface {
    static final String LIS_DESCRIPTOR = "com.example.fangxq.myapplication.aidl.IIOnNewBookArrivedListener";
    static final int TRANSACTION_onNewBookArrived = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);

    public void onNewBookArrived(Book newBook) throws RemoteException;
}
