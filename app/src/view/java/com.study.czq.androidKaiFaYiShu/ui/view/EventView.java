package com.study.czq.androidKaiFaYiShu.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;


public class EventView extends View {
    //实现弹性滑动
    Scroller mScroller = new Scroller(getContext());
    // 缓慢滚动到指定位置

    private void smoothScrollTo(int destX,int destY) {
        int scrollX = getScrollX();
        int delta = destX -scrollX;
        // 1000ms内滑向destX,效果就是慢慢滑动
        mScroller.startScroll(scrollX,0,delta,0,1000);
        invalidate();
    }
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }

    public EventView(Context context) {
        super(context);
    }

    public EventView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EventView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
