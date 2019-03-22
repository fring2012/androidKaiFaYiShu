package com.study.czq.androidKaiFaYiShu.app;

import android.app.Application;
import android.util.Log;

import com.study.czq.androidKaiFaYiShu.utils.Trace;

public class App extends Application{
    private static final String TAG = "App";
    private static App mApp;
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        Trace.d(TAG, "onCreate: App");
    }

    public static App getApp() {
        return mApp;
    }
}
