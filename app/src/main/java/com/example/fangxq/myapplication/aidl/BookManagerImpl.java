package com.example.fangxq.myapplication.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import com.example.fangxq.myapplication.Book;
import com.example.fangxq.myapplication.IBookManager;
import com.example.fangxq.myapplication.IOnNewBookArrivedListener;

import java.util.List;

/**
 * @author huiguo
 * @date 2019/1/23
 * 手动实现binder
 */
public abstract class BookManagerImpl extends Binder implements IIBookManager {


    public BookManagerImpl() {
        this.attachInterface(this, DESCRIPTOR);
    }

    /**
     * 将服务端的Binder对象装换成客户端所有的AIDL接口类型的对象
     * @param obj
     * @return
     */
    public static IIBookManager asInterface(IBinder obj) {
        if ((obj == null)) {
            return null;
        }
        android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
        if (((iin != null) && (iin instanceof IIBookManager))) {
            return ((IIBookManager) iin);
        }
        return new BookManagerImpl.Proxy(obj);
    }

    @Override
    public List<Book> getBookList() throws RemoteException {
        //todo
        return null;
    }

    @Override
    public void addBook(Book book) throws RemoteException {
        //todo
    }

    @Override
    public void registerListener(IIOnNewBookArrivedListener listener) throws RemoteException {
        //todo
    }

    @Override
    public void unregisterListener(IIOnNewBookArrivedListener listener) throws RemoteException {
        //todo
    }

    //返回当前Binder对象
    @Override
    public IBinder asBinder() {
        return this;
    }

    public abstract void registerListener(IOnNewBookArrivedListener listener) throws RemoteException;

    public abstract void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException;

    // 此方法运行在服务端的Binder线程，当客户端发起跨进程请求时，远程请求会通过系统底层封装后交由此方法处理。
    // 服务端通过code确定请求的目标方法，接着从data中取出目标方法所需的参数，执行目标方法。当方法执行完毕后，向reply中写入返回值。
    // 如果此方法返回false，那么客户端的请求就会失败，因此我们可以利用这个特性来做权限验证。
    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION: {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            case TRANSACTION_getBookList: {
                data.enforceInterface(DESCRIPTOR);
                List<Book> result = this.getBookList();
                reply.writeNoException();
                reply.writeTypedList(result);
                return true;
            }
            case TRANSACTION_addBook: {
                data.enforceInterface(DESCRIPTOR);
                Book arg0;
                if ((0 != data.readInt())) {
                    arg0 = Book.CREATOR.createFromParcel(data);
                } else {
                    arg0 = null;
                }
                this.addBook(arg0);
                reply.writeNoException();
                return true;
            }
            case TRANSACTION_registerListener: {
                data.enforceInterface(DESCRIPTOR);
                IIOnNewBookArrivedListener arg0;
                arg0 = BookArrivedListenerImpl.asInterface(data.readStrongBinder());
                this.registerListener(arg0);
                reply.writeNoException();
                return true;
            }
            case TRANSACTION_unregisterListener: {
                data.enforceInterface(DESCRIPTOR);
                IIOnNewBookArrivedListener arg0;
                arg0 = BookArrivedListenerImpl.asInterface(data.readStrongBinder());
                this.unregisterListener(arg0);
                reply.writeNoException();
                return true;
            }
        }
        return super.onTransact(code, data, reply, flags);
    }

    private static class Proxy implements IIBookManager {
        private IBinder mRemote;

        Proxy(IBinder remote) {
            mRemote = remote;
        }

        @Override
        public IBinder asBinder() {
            return mRemote;
        }

        public String getInterfaceDescriptor()
        {
            return DESCRIPTOR;
        }

        //此方法运行在客户端，当客户端远程调用此方法时，首先创建改方法所需要的输入型Parcel对象data,输出型对象reply和返回值对象List;
        //然后将方法的参数信息写入data中，接着调用transact方法来发起RPC（远程过程调用）请求，同时线程挂起，然后服务端的onTransact方法会被调用，
        // 直到RPC过程返回，当前线程继续执行，并从reply中取出结果。
        @Override
        public List<Book> getBookList() throws RemoteException {
            Parcel date = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            List<Book> result;
            try {
                date.writeInterfaceToken(DESCRIPTOR);
                mRemote.transact(TRANSACTION_getBookList, date, reply, 0);
                reply.readException();
                result = reply.createTypedArrayList(Book.CREATOR);
            } finally {
                reply.recycle();
                date.recycle();
            }
            return result;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Parcel date = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                date.writeInterfaceToken(DESCRIPTOR);
                // 0、1 在onTransact（）中做逻辑处理
                if (book != null) {
                    date.writeInt(1);
                    book.writeToParcel(date, 0);
                } else {
                    date.writeInt(0);
                }
                mRemote.transact(TRANSACTION_addBook, date, reply, 0);
                reply.readException();
            } finally {
                reply.recycle();
                date.recycle();
            }
        }

        @Override
        public void registerListener(IIOnNewBookArrivedListener listener) throws RemoteException {
            android.os.Parcel _data = android.os.Parcel.obtain();
            android.os.Parcel _reply = android.os.Parcel.obtain();
            try {
                _data.writeInterfaceToken(DESCRIPTOR);
                _data.writeStrongBinder((((listener != null)) ? (listener.asBinder()) : (null)));
                mRemote.transact(TRANSACTION_registerListener, _data, _reply, 0);
                _reply.readException();
            } finally {
                _reply.recycle();
                _data.recycle();
            }
        }

        @Override
        public void unregisterListener(IIOnNewBookArrivedListener listener) throws RemoteException {
            android.os.Parcel _data = android.os.Parcel.obtain();
            android.os.Parcel _reply = android.os.Parcel.obtain();
            try {
                _data.writeInterfaceToken(DESCRIPTOR);
                _data.writeStrongBinder((((listener != null)) ? (listener.asBinder()) : (null)));
                mRemote.transact(TRANSACTION_unregisterListener, _data, _reply, 0);
                _reply.readException();
            } finally {
                _reply.recycle();
                _data.recycle();
            }
        }
    }
}
