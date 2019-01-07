// IBookManager.aidl
package com.study.czq.androidKaiFaYiShu;
import com.study.czq.androidKaiFaYiShu.entity.Book;
// Declare any non-default types here with import statements
import com.study.czq.androidKaiFaYiShu.IOnNewBookArrivedListener;

interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
