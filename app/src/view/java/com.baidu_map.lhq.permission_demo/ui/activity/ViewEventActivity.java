package com.baidu_map.lhq.permission_demo.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Scroller;

import com.baidu_map.lhq.permission_demo.R;
import com.baidu_map.lhq.permission_demo.ui.gesturedetector.GestureListener;
import com.baidu_map.lhq.permission_demo.ui.view.AnimationView;


public class ViewEventActivity extends Activity {
    //记录滑动速度
    VelocityTracker mVelocityTracker;
    GestureDetector mGestureListener;
    AnimationView mAnimationView;
    Button mButton;
    Scroller mScroller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        mGestureListener = new GestureDetector(this,new GestureListener());
        mAnimationView = findViewById(R.id.animation_view);
        mButton = findViewById(R.id.btn_view_test);


        //scrollTo 和 scrollBy是对view的内容进行滑动
        //mAnimationView.scrollTo(-500,-100); //mAnimationView的内容向右滑动到500，100坐标处

        //通过改变view的LayoutParams的leftMargin的值，让view进行滑动
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)mButton.getLayoutParams();
        params.leftMargin += 100;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //手势监听
        mGestureListener.onTouchEvent(event);
        //velocityTracker(event); //记录速度
        return true;
    }

    /**
     * 记录速度
     * @param event
     */
    private void velocityTracker(MotionEvent event){
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if(mVelocityTracker == null) {
                    // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    // Reset the velocity tracker back to its initial state.
                    mVelocityTracker.clear();
                }
                // Add a user's movement to the tracker.
                mVelocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.computeCurrentVelocity(1000);
                Log.e("Velocity","x: " + mVelocityTracker.getXVelocity() + ";"
                        + "y: " + mVelocityTracker.getYVelocity());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.recycle();

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}

