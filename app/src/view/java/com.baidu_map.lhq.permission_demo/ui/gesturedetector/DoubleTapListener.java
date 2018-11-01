package com.baidu_map.lhq.permission_demo.ui.gesturedetector;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**GestureDetector这个类对外提供了两个接口和一个外部类
 * 接口：OnGestureListener，OnDoubleTapListener,OnContextClickListener
 * 内部类:SimpleOnGestureListener
 * 1.OnGestureListener，这个Listener监听一些手势，如单击、滑动、长按等操作
 * 2.OnDoubleTapListener，这个Listener监听双击和单击事件
 * 3.OnContextClickListener，很多人都不知道ContextClick是什么，我以前也不知道，直到我把平板接上了外接键盘
 **/
public class DoubleTapListener implements GestureDetector.OnDoubleTapListener {
    /**
     * 可以确认（通过单击DOWN后300ms没有下一个DOWN事件确认）
     * 这不是一个双击事件，而是一个单击事件的时候会回调。
     * @param e
     * @return
     */
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    /**
     * 可以确认这是一个双击事件的时候回调
     * @param e
     * @return
     */
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    /**
     * onDoubleTap回调之后的输入事件（DOWN、MOVE、UP）都会回调这个方法
     * （这个方法可以实现一些双击后的控制，如让View双击后变得可拖动等）。
     * @param e
     * @return
     */
    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }
}
