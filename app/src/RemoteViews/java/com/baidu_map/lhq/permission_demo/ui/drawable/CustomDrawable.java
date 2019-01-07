package com.study.czq.androidKaiFaYiShu.ui.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

public class CustomDrawable extends Drawable {
    private Paint mPaint;
    public CustomDrawable(int color) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
    }
    @Override
    public void draw(@NonNull Canvas canvas) {
        final Rect r = getBounds();
        float cx = r.exactCenterX();
        float cy = r.exactCenterY();
        canvas.drawCircle(cx,cy,Math.min(cx,cy),mPaint);
    }

    /**
     * 这是一个设置透明度的方法。如果设置了透明度，那么可以传递给画笔Paint，
     * 前一篇的项目中是在Render中调用了mPaint.setAlpha(alpha);
     * 方法。0（0x00）表示完全透明，255（0xFF）表示完全不透明。
     * @param alpha
     */
    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        invalidateSelf();
    }

    /**
     * 如果一个Drawable设置了一个颜色过滤器，那么在绘制出来之前，被绘制内容的每一个像素都会被颜色过滤器改变。
     * 项目在Render中调用了mPaint.setColorFilter(cf);方法。ColorFilter是一个抽象类，
     * 他有一个比较好用的子类ColorMatrixColorFilter，我们可以通过设置颜色颜色举证来改变最终的显示效果。
     * 但是这里主要介绍Drawable，ColorFilter这里不展开讲。
     *
     * 作者：twinsnan
     * 链接：https://www.jianshu.com/p/4e5c66a73259
     * 來源：简书
     * 简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。
     * @param cf
     */
    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
        invalidateSelf();
    }
    @Override
    public int getOpacity() {
// not sure,so be safe
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
    }


    /**
     * getIntrinsicWidth和getIntrinsicHeight这两个方法需要注意一下,当自
     * 定义的Drawable有固有大小时最好重写这两个方法,因为它会影响到View的wrap_content
     * 布局,比如自定义Drawable是绘制一张图片,那么这个Drawable的内部大小就可以选用图
     * 片的大小。在上面的例子中,自定义的Drawable是由颜色填充的圆形并且没有固定的大
     * 小,因此没有重写这两个方法,这个时候它的内部大小为-1,即内部宽度和内部高度都
     * 为-1。需要注意的是,内部大小不等于Drawable的实际区域大小,Drawable的实际区域大
     * 小可以通过它的getBounds方法来得到,一般来说它和View的尺寸相同。
     * @return
     */
    @Override
    public int getIntrinsicWidth() {
        return super.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return super.getIntrinsicHeight();
    }
}
