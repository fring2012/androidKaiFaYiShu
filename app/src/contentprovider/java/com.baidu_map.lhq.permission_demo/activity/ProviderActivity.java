package com.baidu_map.lhq.permission_demo.activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;


import com.baidu_map.lhq.permission_demo.R;
import com.baidu_map.lhq.permission_demo.activity.base.BaseActivity;


public class ProviderActivity extends BaseActivity {
    private static final String TAG = "ProviderActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationInfo ai = null;
        try {
            ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Bundle bundle = ai.metaData;
        String myApiKey = bundle.getString("type");
        Log.d(TAG, "onCreate: " + myApiKey);

        Uri uri = Uri.parse("content://BookProvider");
        getContentResolver().query(uri,null,null,null,null);
        getContentResolver().query(uri,null,null,null,null);
        getContentResolver().query(uri,null,null,null,null);
    }

    @Override
    protected int getRLayoutId() {
        return R.layout.activity_provider;
    }
}
