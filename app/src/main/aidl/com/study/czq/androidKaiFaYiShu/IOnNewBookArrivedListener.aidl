// IOnNewBookArrivedListener.aidl
package com.study.czq.androidKaiFaYiShu;
import com.study.czq.androidKaiFaYiShu.entity.Book;
// Declare any non-default types here with import statements

interface IOnNewBookArrivedListener {
     void onNewBookArrived(in Book newBook);
}
