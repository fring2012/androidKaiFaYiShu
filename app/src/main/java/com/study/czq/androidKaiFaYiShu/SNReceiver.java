package com.study.czq.androidKaiFaYiShu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.study.czq.androidKaiFaYiShu.utils.Trace;

public class SNReceiver extends BroadcastReceiver{
    private final static String TAG = "SNReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("android.intent.action.BOOT_COMPLETED")){
            Trace.d(TAG, "android.intent.action.BOOT_COMPLETED");
        }else {
            Trace.d(TAG, "onReceive: obtain sn");
        }
    }
}
