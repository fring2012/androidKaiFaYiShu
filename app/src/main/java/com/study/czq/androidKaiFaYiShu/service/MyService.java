package com.study.czq.androidKaiFaYiShu.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.study.czq.androidKaiFaYiShu.IBookManager;
import com.study.czq.androidKaiFaYiShu.IOnNewBookArrivedListener;
import com.study.czq.androidKaiFaYiShu.entity.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MyService extends Service{
    private static final String TAG = "MyService";
    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);
    private List<Book> mBookList = new ArrayList<>();
    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<>();
    IBookManager.Stub mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            synchronized (mBookList) {
                return mBookList;
            }
        }
        @Override
        public void addBook(Book book) throws RemoteException {
            synchronized (mBookList) {
                if (!mBookList.contains(book)) {
                    mBookList.add(book);
                }
            }
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {

            mListenerList.register(listener);

            Log.d(TAG,"registerListener,size:" + mListenerList.beginBroadcast());
            mListenerList.finishBroadcast();
        }

        @Override
        public void unregisterListener(com.study.czq.androidKaiFaYiShu.IOnNewBookArrivedListener listener) throws RemoteException {
                mListenerList.unregister(listener);
            Log.d(TAG,"unregisterListener,current size:" + mListenerList.
                    beginBroadcast());
            mListenerList.finishBroadcast();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1,"Android","A230"));
        mBookList.add(new Book(2,"Ios","A112"));
        new Thread(new ServiceWorker()).start();


    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
        Log.d(TAG,"onNewBookArrived,notify listeners:" + mListenerList.beginBroadcast());
        mListenerList.finishBroadcast();
        final int n = mListenerList.beginBroadcast();
        for (int i = 0; i < n; i++) {
            IOnNewBookArrivedListener listener = mListenerList.getBroadcastItem(i);
            Log.d(TAG,"onNewBookArrived,notify listener:" + listener);
            listener.onNewBookArrived(book);
        }
        mListenerList.finishBroadcast();
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        super.onDestroy();
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
                Book newBook = new Book(bookId,"new book#" + bookId,"A2" + bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
