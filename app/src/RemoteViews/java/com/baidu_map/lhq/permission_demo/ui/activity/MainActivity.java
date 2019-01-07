package com.study.czq.androidKaiFaYiShu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.study.czq.androidKaiFaYiShu.R;
import com.study.czq.androidKaiFaYiShu.base.BaseActivity;

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
            case R.id.btn_drawable:
                intent = new Intent(this,DrawableActivity.class);
                startActivity(intent);
            default:
                break;
        }
    }
    @Override
    protected int getRLayoutId() {
        return R.layout.activity_main;
    }
}
