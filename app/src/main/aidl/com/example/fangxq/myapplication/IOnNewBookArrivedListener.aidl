// IOnNewBookArrivedListener.aidl
package com.example.fangxq.myapplication;

// Declare any non-default types here with import statements

import com.example.fangxq.myapplication.Book;

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}
