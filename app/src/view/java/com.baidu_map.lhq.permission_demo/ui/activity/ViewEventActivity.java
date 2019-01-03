package com.baidu_map.lhq.permission_demo.ui.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Scroller;

import com.baidu_map.lhq.permission_demo.R;


public class ViewEventActivity extends Activity {
    //记录滑动速度
    VelocityTracker mVelocityTracker;
    GestureDetector mGestureListener;
    AnimationView mAnimationView;
    Button mButton;
    //利用Handler实现弹性滑动,使用延时策略
    ScrollHandler mScrollHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        mGestureListener = new GestureDetector(this,new GestureListener());
        mAnimationView = findViewById(R.id.animation_view);
        mButton = findViewById(R.id.btn_view_test);
        mScrollHandler = new ScrollHandler();

        //scrollTo 和 scrollBy是对view的内容进行滑动
        //mAnimationView.scrollTo(-500,-100); //mAnimationView的内容向右滑动到500，100坐标处

        //通过改变view的LayoutParams的leftMargin的值，让view进行滑动
//        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)mButton.getLayoutParams();
//        params.leftMargin += 100;

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                 //ObjectAnimator实现弹性滑动
//                ObjectAnimator.ofFloat(mAnimationView,"translationX",0,100)
//                        .setDuration(1000) //动画持续时间
//                        .start();
                //利用Handler延时策略实现弹性滑动,
                mScrollHandler.sendEmptyMessage(mScrollHandler.MESSAGE_SCROLL_TO);
            }
        });
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




    @SuppressLint("HandlerLeak")
    private class ScrollHandler  extends Handler {
        private static final int MESSAGE_SCROLL_TO = 1;
        private static final int FRAME_COUNT = 30;
        private static final int DELAYED_TIME = 33;
        private int mCount = 0;
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SCROLL_TO: {
                    mCount++;
                    if (mCount <= FRAME_COUNT) {
                        float fraction = mCount / (float) FRAME_COUNT;
                        int scrollX = (int) (fraction * 100);
                        mAnimationView.scrollTo(scrollX, 0);
                        this.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO,
                                DELAYED_TIME);
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

}

