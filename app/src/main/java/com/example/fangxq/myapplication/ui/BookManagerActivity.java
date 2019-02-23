package com.example.fangxq.myapplication.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.fangxq.myapplication.Book;
import com.example.fangxq.myapplication.IBookManager;
import com.example.fangxq.myapplication.IOnNewBookArrivedListener;
import com.example.fangxq.myapplication.R;
import com.example.fangxq.myapplication.aidl.BookArrivedListenerImpl;
import com.example.fangxq.myapplication.aidl.BookManagerImpl;
import com.example.fangxq.myapplication.aidl.BookManagerService;
import com.example.fangxq.myapplication.aidl.IIBookManager;
import com.example.fangxq.myapplication.aidl.IIOnNewBookArrivedListener;

import java.util.List;

/**
 * @author huiguo
 * @date 2019/1/23
 */
public class BookManagerActivity extends Activity {

    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;
    private IBookManager mRemoteBookManager;
    //private IIBookManager mRemoteBookManager;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.e("fxq", "receive new book :" + ((Book)msg.obj).bookName);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.d("fxq", "binder died. tname:" + Thread.currentThread().getName());
            if (mRemoteBookManager == null) {
                return;
            }
            mRemoteBookManager.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mRemoteBookManager = null;
            // TODO:这里重新绑定远程Service
        }
    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            //IIBookManager bookManager = BookManagerImpl.asInterface(service);
            mRemoteBookManager = bookManager;
            try {
                mRemoteBookManager.asBinder().linkToDeath(mDeathRecipient, 0);
                bookManager.registerListener(mOnNewBookArrivedListener);
                List<Book> list = bookManager.getBookList();
                Log.e("fxq", "query book list, list type:"
                        + list.getClass().getCanonicalName());
                for (Book book : list) {
                    Log.e("fxq", "query book list:" + book.bookName);
                }
                Book newBook = new Book(3, "Android进阶");
                bookManager.addBook(newBook);
                Log.e("fxq", "add book:" + newBook.bookName);
                List<Book> newList = bookManager.getBookList();
                for (Book book : newList) {
                    Log.e("fxq", "query book newlist:" + book.bookName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteBookManager = null;
            Log.d("fxq", "onServiceDisconnected. tname:" + Thread.currentThread().getName());
        }
    };

    private IOnNewBookArrivedListener mOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {

        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook)
                    .sendToTarget();
        }
    };


//    private IIOnNewBookArrivedListener mOnNewBookArrivedListener = new BookArrivedListenerImpl() {
//        @Override
//        public void onNewBookArrived(Book newBook) throws RemoteException {
//            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook)
//                    .sendToTarget();
//        }
//    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        if (mRemoteBookManager != null
                && mRemoteBookManager.asBinder().isBinderAlive()) {
            try {
                Log.e("fxq", "unregister listener:" + mOnNewBookArrivedListener);
                mRemoteBookManager
                        .unregisterListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mServiceConnection);
        super.onDestroy();
    }

    public void onButton1Click(View view) {
        Toast.makeText(this, "click button1", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (mRemoteBookManager != null) {
                    try {
                        mRemoteBookManager.getBookList();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
