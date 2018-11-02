package com.baidu_map.lhq.permission_demo.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;

public class AnimationView extends View {
    private static final String TAG = "AnimationView";
    private int mLastX,mLastY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x -mLastX;
                int deltaY = y -mLastY;
                Log.d(TAG,"move,deltaX:" + deltaX + " deltaY:" + deltaY);
                ///使用ViewHelper要添加nineoldandroids-2.4.0.jar包
                int translationX = (int) ViewHelper.getTranslationX(this) + deltaX;
                int translationY = (int)ViewHelper.getTranslationY(this) + deltaY;
                this.setTranslationX(translationX);
                this.setTranslationY(translationY);
                //android 3.0以下要用以下代码
//                ViewHelper.setTranslationX(this,translationX);
//                ViewHelper.setTranslationY(this,translationY);
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
            default:
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw: ");
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(50,50,50,paint);

    }

    public AnimationView(Context context) {
        super(context);
    }

    public AnimationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
