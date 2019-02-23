package com.example.fangxq.myapplication.aidl;

import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.fangxq.myapplication.Book;


/**
 * @author huiguo
 * @date 2019/2/11
 */
public class BookArrivedListenerImpl extends Binder implements IIOnNewBookArrivedListener{

    public BookArrivedListenerImpl() {
        this.attachInterface(this, LIS_DESCRIPTOR);
    }

    public static IIOnNewBookArrivedListener asInterface(IBinder obj) {
        if ((obj == null)) {
            return null;
        }
        android.os.IInterface iin = obj.queryLocalInterface(LIS_DESCRIPTOR);
        if (((iin != null) && (iin instanceof IIOnNewBookArrivedListener))) {
            return ((IIOnNewBookArrivedListener) iin);
        }
        return new BookArrivedListenerImpl.Proxy(obj);
    }

    @Override
    public void onNewBookArrived(Book newBook) throws RemoteException {

    }

    //返回当前Binder对象
    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION: {
                reply.writeString(LIS_DESCRIPTOR);
                return true;
            }
            case TRANSACTION_onNewBookArrived: {
                data.enforceInterface(LIS_DESCRIPTOR);
                Book arg0;
                if ((0 != data.readInt())) {
                    arg0 = Book.CREATOR.createFromParcel(data);
                } else {
                    arg0 = null;
                }
                this.onNewBookArrived(arg0);
                reply.writeNoException();
                return true;
            }
        }
        return super.onTransact(code, data, reply, flags);
    }

    private static class Proxy implements IIOnNewBookArrivedListener {
        private IBinder mRemote;

        Proxy(IBinder remote) {
            mRemote = remote;
        }

        public String getInterfaceDescriptor()
        {
            return LIS_DESCRIPTOR;
        }

        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            Parcel date = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                date.writeInterfaceToken(LIS_DESCRIPTOR);
                if (newBook != null) {
                    date.writeInt(1);
                    newBook.writeToParcel(date, 0);
                } else {
                    date.writeInt(0);
                }
                mRemote.transact(TRANSACTION_onNewBookArrived, date, reply, 0);
                reply.readException();
            } finally {
                reply.recycle();
                date.recycle();
            }
        }

        @Override
        public IBinder asBinder() {
            return null;
        }

    }
}
