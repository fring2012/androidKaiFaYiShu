package com.study.czq.androidKaiFaYiShu.app;

import android.app.Application;
import android.util.Log;

import com.study.czq.androidKaiFaYiShu.utils.Trace;

public class App extends Application{
    private static final String TAG = "App";
    @Override
    public void onCreate() {
        super.onCreate();
        Trace.d(TAG, "onCreate: App");
    }
}
