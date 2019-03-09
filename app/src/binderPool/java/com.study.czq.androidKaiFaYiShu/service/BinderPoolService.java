package com.study.czq.androidKaiFaYiShu.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.study.czq.androidKaiFaYiShu.binderpool.BinderPool;
import com.study.czq.androidKaiFaYiShu.utils.Trace;


public class BinderPoolService extends Service {
    private static final String TAG = "BinderPoolService";
    private Binder mBinderPool = new BinderPool.BinderPoolImpl();

    /**
     * startService启动服务时，onCreate,onStartCommand在猪线程中执行
     * bindService绑定服务时,onCreate,onStartCommand在bindService线程中执行
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Trace.d(TAG,"in " + Thread.currentThread().getName() + " onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Trace.d(TAG,"in " + Thread.currentThread().getName() + " onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Trace.d(TAG,"in " + Thread.currentThread().getName() + " onBind()");
        return mBinderPool;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
