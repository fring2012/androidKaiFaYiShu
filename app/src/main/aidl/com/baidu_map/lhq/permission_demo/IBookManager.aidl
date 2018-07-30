// IBookManager.aidl
package com.baidu_map.lhq.permission_demo;
import com.baidu_map.lhq.permission_demo.entity.Book;
// Declare any non-default types here with import statements
import com.baidu_map.lhq.permission_demo.IOnNewBookArrivedListener;

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
