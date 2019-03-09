package com.study.czq.androidKaiFaYiShu.ui.activity;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.study.czq.androidKaiFaYiShu.R;
import com.study.czq.androidKaiFaYiShu.base.BaseActivity;
import com.study.czq.androidKaiFaYiShu.ui.animation.Rotate3dAnimation;

public class AnimationActivity extends BaseActivity {
    Button button;

    Button button2;

    Button button3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        button = findViewById(R.id.btn_rotate3d);
        button2 = findViewById(R.id.btn_button2);
        button3 = findViewById(R.id.btn_button3);

        setTranslation(button2);
    }

    @Override
    protected int getRLayoutId() {
        return R.layout.activity_main;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_rotate3d:
                Trace.d("animation",view.getId() + "");
                view.startAnimation(new Rotate3dAnimation(
                        100,200,400,500,100,true
                ));
                break;
            case R.id.btn_button2:
                changeBackgroundColor(button2);
                setTranslation(button2);
                break;
            case R.id.btn_button3:
                performAnimate();
                break;
            default:
                    break;
        }
    }

    /**
     * 改变一个对象的背景色属性,典型的情形是改变View的背景色,下面的动画可
     * 以让背景色在3秒内实现从0xFFFF8080到0xFF8080FF的渐变,动画会无限循环而且会有反
     * 转的效果。
     * @param view
     */
    public void changeBackgroundColor(View view) {
        ValueAnimator colorAnim = ObjectAnimator.ofInt(view,"backgroundColor",
                /*Red*/0xFFFF8080,/*Blue*/0xFF8080FF);
        colorAnim.setDuration(3000);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();
    }

    /**
     * 动画集合,5秒内对View的旋转、平移、缩放和透明度都进行了改变。
     * @param view
     */
    public void setTranslation(View view) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view,"rotationX",0,360),
                ObjectAnimator.ofFloat(view,"rotationY",0,180),
                ObjectAnimator.ofFloat(view,"rotation",0,-90),
                ObjectAnimator.ofFloat(view,"translationX",0,90),
                ObjectAnimator.ofFloat(view,"translationY",0,90),
                ObjectAnimator.ofFloat(view,"scaleX",1,1.5f),
                ObjectAnimator.ofFloat(view,"scaleY",1,0.5f),
                ObjectAnimator.ofFloat(view,"alpha",1,0.25f,1)
        );
        set.setDuration(5 * 1000).start();
    }

    private void performAnimate() {
        ViewWrapper viewWrapper = new ViewWrapper(button3);
        ObjectAnimator.ofInt(viewWrapper,"width",500).setDuration(5000).start();
    }


    private void performAnimate(final View target,final int start,final int end) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(1,100);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            // 持有一个IntEvaluator对象,方便下面估值的时候使用
            private IntEvaluator mEvaluator = new IntEvaluator();
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                // 获得当前动画的进度值,整型,1~100之间
                int currentValue = (Integer) animator.getAnimatedValue();
                Trace.d(TAG,"current value: " + currentValue);
                // 获得当前进度占整个动画过程的比例,浮点型,0~1之间
                float fraction = animator.getAnimatedFraction();
                // 直接调用整型估值器,通过比例计算出宽度,然后再设给Button
                target.getLayoutParams().width = mEvaluator.evaluate(fraction,start,end);
                        target.requestLayout();
            }
        });
        valueAnimator.setDuration(5000).start();
    }


    /**
     * 用一个类来包装原始对象,间接为其提供get和set方法这是一个很有用的解决方法,是笔者最喜欢用的,因为用起来很方便,也很好理解,
     * 下面将通过一个具体的例子来介绍它。
     */
    private static class ViewWrapper {
        private View mTarget;
        public ViewWrapper(View target) {
            mTarget = target;
        }
        public int getWidth() {
            return mTarget.getLayoutParams().width;
        }
        public void setWidth(int width) {
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }
    }
}
