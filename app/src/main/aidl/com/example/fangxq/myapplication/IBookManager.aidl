// IBookManager.aidl
package com.example.fangxq.myapplication;
import com.example.fangxq.myapplication.Book;
import com.example.fangxq.myapplication.IOnNewBookArrivedListener;
// Declare any non-default types here with import statements

//系统生成Binder
interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book); // in表示输入型参数，out表示输出型数据、inout（输入输出型）
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}

