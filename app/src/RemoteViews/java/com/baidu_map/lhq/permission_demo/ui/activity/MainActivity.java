package com.baidu_map.lhq.permission_demo.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.baidu_map.lhq.permission_demo.R;
import com.baidu_map.lhq.permission_demo.base.BaseActivity;

public class MainActivity extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onJumpActivityClick(View view) {
        int id = view.getId();
        Intent intent = null;
        switch (id) {
            case R.id.btn_notification:
                intent = new Intent(this,NotificationActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_send_custom_notification:
                intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction("com.ryg.chapter_5.action.CLICK");
                sendBroadcast(intent);
            default:
                break;
        }
        startActivity(intent);
    }
    @Override
    protected int getRLayoutId() {
        return R.layout.activity_main;
    }
}
