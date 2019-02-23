package com.example.fangxq.myapplication.aidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.fangxq.myapplication.Book;
import com.example.fangxq.myapplication.IBookManager;
import com.example.fangxq.myapplication.IOnNewBookArrivedListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author huiguo
 * @date 2019/1/23
 */
public class BookManagerService extends Service {

    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>(); // 支持并发读写

    //对象不能进行跨进程直接传输，跨进程传输客户端的同一个对象会在服务端生成不同的对象，但这些对象底层的Binder对象是同一个。
    // 专门用于删除跨进程listener的接口
    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<IOnNewBookArrivedListener>();


    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            SystemClock.sleep(1000);
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListenerList.register(listener);

            final int N = mListenerList.beginBroadcast();
            mListenerList.finishBroadcast();
            Log.e("fxq", "registerListener, current size:" + N);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            boolean success = mListenerList.unregister(listener);

            if (success) {
                Log.e("fxq", "unregister success.");
            } else {
                Log.e("fxq", "not found, can not unregister.");
            }
            final int N = mListenerList.beginBroadcast();
            mListenerList.finishBroadcast();
            Log.e("fxq", "unregisterListener, current size:" + N);
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
                throws RemoteException {
            int check = checkCallingOrSelfPermission("com.fangxq.permission.ACCESS_BOOK_SERVICE");
            Log.e("fxq", "check=" + check);
            if (check == PackageManager.PERMISSION_DENIED) {
                return false;
            }

            String packageName = null;
            String[] packages = getPackageManager().getPackagesForUid(
                    getCallingUid());
            if (packages != null && packages.length > 0) {
                packageName = packages[0];
            }
            Log.e("fxq", "onTransact: " + packageName);
            if (!packageName.startsWith("com.example.fangxq")) {
                return false;
            }

            return super.onTransact(code, data, reply, flags);
        }
    };

//    private Binder mBinder = new BookManagerImpl() {
//        @Override
//        public List<Book> getBookList() throws RemoteException {
//            SystemClock.sleep(5000);
//            return mBookList;
//        }
//
//        @Override
//        public void addBook(Book book) throws RemoteException {
//            mBookList.add(book);
//        }
//
//        @Override
//        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
//            mListenerList.register(listener);
//
//            final int N = mListenerList.beginBroadcast();
//            mListenerList.finishBroadcast();
//            Log.e("fxq", "registerListener, current size:" + N);
//        }
//
//        @Override
//        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
//            boolean success = mListenerList.unregister(listener);
//
//            if (success) {
//                Log.e("fxq", "unregister success.");
//            } else {
//                Log.e("fxq", "not found, can not unregister.");
//            }
//            final int N = mListenerList.beginBroadcast();
//            mListenerList.finishBroadcast();
//            Log.e("fxq", "unregisterListener, current size:" + N);
//        }
//
//        @Override
//        public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
//                throws RemoteException {
//            int check = checkCallingOrSelfPermission("com.fangxq.permission.ACCESS_BOOK_SERVICE");
//            Log.e("fxq", "check=" + check);
//            if (check == PackageManager.PERMISSION_DENIED) {
//                return false;
//            }
//
//            String packageName = null;
//            String[] packages = getPackageManager().getPackagesForUid(
//                    getCallingUid());
//            if (packages != null && packages.length > 0) {
//                packageName = packages[0];
//            }
//            Log.e("fxq", "onTransact: " + packageName);
//            if (!packageName.startsWith("com.example.fangxq")) {
//                return false;
//            }
//
//            return super.onTransact(code, data, reply, flags);
//        }
//    };

    @Override
    public void onCreate() {
        super.onCreate();
        android.os.Debug.waitForDebugger();
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(1, "IOS"));
        new Thread(new ServiceWorker()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int check = checkCallingOrSelfPermission("com.fangxq.permission.ACCESS_BOOK_SERVICE");
        Log.e("fxq", "onbind check=" + check);
        if (check == PackageManager.PERMISSION_DENIED) {
            return null;
        }
        return mBinder;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        super.onDestroy();
    }

    private void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
        final int N = mListenerList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener l = mListenerList.getBroadcastItem(i);
            if (l != null) {
                try {
                    l.onNewBookArrived(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        mListenerList.finishBroadcast();
    }

    private class ServiceWorker implements Runnable {
        @Override
        public void run() {
            // do background processing here.....
            while (!mIsServiceDestoryed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId, "new book#" + bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
