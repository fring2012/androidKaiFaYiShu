package com.study.czq.androidKaiFaYiShu.app;

import android.app.Application;
import android.util.Log;

public class App extends Application{
    private static final String TAG = "App";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: App");
    }
}
