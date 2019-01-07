package com.study.czq.androidKaiFaYiShu.ui.activity;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;


import com.study.czq.androidKaiFaYiShu.R;
import com.study.czq.androidKaiFaYiShu.base.BaseActivity;


public class WindowActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        createWindow();
    }

    @Override
    protected int getRLayoutId() {
        return R.layout.activity_window;
    }

    /**
     * Flags参数表示Window的属性,它有很多选项,通过这些选项可以控制Window的显
     * 示特性,这里主要介绍几个比较常用的选项,剩下的请查看官方文档。
     * FLAG_NOT_FOCUSABLE
     * 表示Window不需要获取焦点,也不需要接收各种输入事件,此标记会同时启用
     * FLAG_NOT_TOUCH_MODAL,最终事件会直接传递给下层的具有焦点的Window。
     * FLAG_NOT_TOUCH_MODAL
     * 在此模式下,系统会将当前Window区域以外的单击事件传递给底层的Window,当前
     * Window区域以内的单击事件则自己处理。这个标记很重要,一般来说都需要开启此标
     * 记,否则其他Window将无法收到单击事件。FLAG_SHOW_WHEN_LOCKED
     * 开启此模式可以让Window显示在锁屏的界面上。
     */
    public void createWindow() {
        Button button = new Button(this);
        button.setText("button");
        WindowManager.LayoutParams viewLayout = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.TYPE_APPLICATION,0,
                PixelFormat.TRANSPARENT);
        viewLayout.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;

        viewLayout.gravity = Gravity.LEFT | Gravity.TOP;
        viewLayout.x = 100;
        viewLayout.y = 300;

        getWindowManager().addView(button,viewLayout);
    }
}
