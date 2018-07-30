package com.baidu_map.lhq.permission_demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu_map.lhq.permission_demo.R;
import com.baidu_map.lhq.permission_demo.activity.base.PermissionActivity;

public class TwoActivity extends PermissionActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TwoActivity", "onCreate: 11111");
    }

    @Override
    protected int getRLayoutId() {
        return R.layout.activity_main;
    }


}
