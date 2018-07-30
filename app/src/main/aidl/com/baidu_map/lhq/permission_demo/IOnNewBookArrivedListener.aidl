// IOnNewBookArrivedListener.aidl
package com.baidu_map.lhq.permission_demo;
import com.baidu_map.lhq.permission_demo.entity.Book;
// Declare any non-default types here with import statements

interface IOnNewBookArrivedListener {
     void onNewBookArrived(in Book newBook);
}
