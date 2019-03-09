package com.study.czq.androidKaiFaYiShu.ui.appwidgetprovider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.study.czq.androidKaiFaYiShu.R;

/**
 * onEnable： 当该窗口小部件第一次添加到桌面时调用该方法，可添加多次但只在第一次调用。
   onUpdate ：小部件被添加时或者每次小部件更新时都会调用一次该方法，小部件的
              更新时机由updatePeriodMillis来指定，每个周期小部件都会自动更新一次。
   onDeleted： 每删除一次桌面小部件就调用一次。
   onDisabled： 当最后一个该类型的桌面小部件被删除时调用该方法，注意是最后一个。
   onReceive： 这是广播的内置方法，用于分发具体的事件给其他方法。
 */
public class MyAppWidgetProvider  extends AppWidgetProvider {
    public static final String TAG = "MyAppWidgetProvider";
    public static final String CLICK_ACTION = "com.ryg.chapter_5.action.CLICK";
    public MyAppWidgetProvider() {
        super();
    }
    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context,intent);
        Trace.i(TAG,"onReceive : action = " + intent.getAction());
// 这里判断是自己的action，做自己的事情，比如小部件被单击了要干什么，这里是做一个动画效果
        if (intent.getAction().equals(CLICK_ACTION)) {
            Toast.makeText(context,"clicked it",Toast.LENGTH_SHORT).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap srcbBitmap = BitmapFactory.decodeResource(
                            context.getResources(), R.mipmap.ic_launcher);
                    AppWidgetManager appWidgetManager = AppWidgetManager.
                            getInstance(context);
                    for (int i = 0; i < 37; i++) {
                        float degree = (i * 10) % 360;
                        RemoteViews remoteViews = new RemoteViews(context
                                .getPackageName(),R.layout.widget);
                        remoteViews.setImageViewBitmap(R.id.imageView1,
                                rotateBitmap(context,srcbBitmap,degree));
                        Intent intentClick = new Intent();
                        intentClick.setAction(CLICK_ACTION);
                        PendingIntent pendingIntent = PendingIntent
                                .getBroadcast(context,0,intentClick,0);
                        remoteViews.setOnClickPendingIntent(R.id.imageView1,
                                pendingIntent);
                        appWidgetManager.updateAppWidget(new ComponentName(
                                        context,MyAppWidgetProvider.class),
                                remoteViews);
                        SystemClock.sleep(30);
                    }
                }
            }).start();
        }
    }
    /**
     * 每次桌面小部件更新时都调用一次该方法
     */
    @Override
    public void onUpdate(Context context,AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context,appWidgetManager,appWidgetIds);
        Trace.i(TAG,"onUpdate");
        final int counter = appWidgetIds.length;
        Trace.i(TAG,"counter = " + counter);
        for (int i = 0; i < counter; i++) {
            int appWidgetId = appWidgetIds[i];
            onWidgetUpdate(context,appWidgetManager,appWidgetId);
        }
    }
    /**
     *桌面小部件更新
     *
     * @param context
     * @param appWidgeManger
     * @param appWidgetId
     */
    private void onWidgetUpdate(Context context,
                                AppWidgetManager appWidgeManger,int appWidgetId) {
        Trace.i(TAG,"appWidgetId = " + appWidgetId);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget);
// “桌面小部件”单击事件发送的Intent广播
        Intent intentClick = new Intent();
        intentClick.setAction(CLICK_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,
                intentClick,0);
        remoteViews.setOnClickPendingIntent(R.id.imageView1,pendingIntent);
        appWidgeManger.updateAppWidget(appWidgetId,remoteViews);
    }
    private Bitmap rotateBitmap(Context context,Bitmap srcbBitmap,float
            degree) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        Bitmap tmpBitmap = Bitmap.createBitmap(srcbBitmap,0,0,
                srcbBitmap.getWidth(),srcbBitmap.getHeight(),matrix,true);
        return tmpBitmap;
    }

}
