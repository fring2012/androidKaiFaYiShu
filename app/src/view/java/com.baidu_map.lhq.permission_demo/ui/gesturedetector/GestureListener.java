package com.baidu_map.lhq.permission_demo.ui.gesturedetector;

import android.util.Log;
import android.view.MotionEvent;

import static android.view.GestureDetector.*;

/**GestureDetector这个类对外提供了两个接口和一个外部类
 * 接口：OnGestureListener，OnDoubleTapListener,OnContextClickListener
 * 内部类:SimpleOnGestureListener
 * 1.OnGestureListener，这个Listener监听一些手势，如单击、滑动、长按等操作
 * 2.OnDoubleTapListener，这个Listener监听双击和单击事件
 * 3.OnContextClickListener，很多人都不知道ContextClick是什么，我以前也不知道，直到我把平板接上了外接键盘
 **/
public class GestureListener implements OnGestureListener{
    private static String TAG = "GestureListener";
    /**
     * 用户按下屏幕就会触发
     * @param e
     * @return
     */
    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(TAG,"onDown(MotionEvent e)");
        return false;
    }

    /**
     * 用户按下按键后100ms（根据Android7.0源码）还没有松开或者移动就会回调，
     * 官方在源码的解释是说一般用于告诉用户已经识别按下事件的回调
     * （我暂时想不出有什么用途，因为这个回调触发之后还会触发其他的，不像长按）。
     * @param e
     */
    @Override
    public void onShowPress(MotionEvent e) {
        Log.d(TAG,"onShowPress(MotionEvent e)");
    }

    /**
     * 用户手指松开（UP事件）的时候如果没有执行onScroll()和onLongPress()
     * 这两个回调的话，就会回调这个，说明这是一个点击抬起事件，
     * 但是不能区分是否双击事件的抬起。
     * @param e
     * @return
     */
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(TAG,"onSingleTapUp(MotionEvent e)");
        return false;
    }

    /**
     * 手指滑动的时候执行的回调（接收到MOVE事件，且位移大于一定距离），
     * e1,e2分别是之前DOWN事件和当前的MOVE事件，
     * distanceX和distanceY就是当前MOVE事件和上一个MOVE事件的位移量。
     * @param e1
     * @param e2
     * @param distanceX
     * @param distanceY
     * @return
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(TAG,"onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)");
        return false;
    }

    /**
     * 用户长按后（好像不同手机的时间不同，
     * 源码里默认是100ms+500ms）触发，触发之后不会触发其他回调,直至松开（UP事件）
     * @param e
     */
    @Override
    public void onLongPress(MotionEvent e) {
        Log.d(TAG,"onLongPress(MotionEvent e)");

    }

    /**
     * 用户执行抛操作之后的回调，MOVE事件之后手松开（UP事件）
     * 那一瞬间的x或者y方向速度，如果达到一定数值（源码默认是每秒50px），
     * 就是抛操作（也就是快速滑动的时候松手会有这个回调，
     * 因此基本上有onFling必然有onScroll）。
     * @param e1
     * @param e2
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(TAG,"onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)");
        return false;
    }
}
