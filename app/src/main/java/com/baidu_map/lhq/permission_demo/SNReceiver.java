package com.baidu_map.lhq.permission_demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SNReceiver extends BroadcastReceiver{
    private final static String TAG = "SNReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("android.intent.action.BOOT_COMPLETED")){
            Log.d(TAG, "android.intent.action.BOOT_COMPLETED");
        }else {
            Log.d(TAG, "onReceive: obtain sn");
        }
    }
}
