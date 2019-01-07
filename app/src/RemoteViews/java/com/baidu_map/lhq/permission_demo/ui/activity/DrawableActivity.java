package com.study.czq.androidKaiFaYiShu.ui.activity;

import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.study.czq.androidKaiFaYiShu.R;
import com.study.czq.androidKaiFaYiShu.base.BaseActivity;

public class DrawableActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView txt_transition = findViewById(R.id.txt_transition);
        TransitionDrawable drawable = (TransitionDrawable) txt_transition.getBackground();
        drawable.startTransition(1000);
    }

    @Override
    protected int getRLayoutId() {
        return R.layout.activity_drawable;
    }
}
