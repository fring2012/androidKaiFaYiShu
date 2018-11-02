package com.baidu_map.lhq.permission_demo.base;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

public abstract class BaseActivity extends Activity {
    protected static final String TAG = BaseActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getRLayoutId());
    }

    protected abstract int getRLayoutId();
}
