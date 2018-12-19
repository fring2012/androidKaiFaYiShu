package com.baidu_map.lhq.permission_demo.ui.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import com.baidu_map.lhq.permission_demo.R;
import com.baidu_map.lhq.permission_demo.base.BaseActivity;

public class NotificationActivity extends BaseActivity {
    NotificationManager mNotificationManager;

    public final static int ID = 1;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String service = Context.NOTIFICATION_SERVICE;
        mNotificationManager =(NotificationManager)getSystemService(service);





    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_notification:
                sendNotification();
                break;
            case R.id.btn_clean_notification:
                mNotificationManager.cancel(ID);
                break;
            case R.id.btn_send_custom_notification:
                customNotification();
                break;
        }
    }

    public void sendNotification() {

        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher_round); //设置图标
        builder.setTicker("显示第二个通知");
        builder.setContentTitle("通知"); //设置标题
        builder.setContentText("点击查看详细内容"); //消息内容
        builder.setWhen(System.currentTimeMillis()); //发送时间
        builder.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
        builder.setAutoCancel(true);//打开程序后图标消失
        //单体通知后会打开NotificationActivity.class
        Intent intent =new Intent (this,NotificationActivity.class);
        PendingIntent pendingIntent =PendingIntent.getActivity(NotificationActivity.this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        Notification notification1 = builder.build();
        mNotificationManager.notify(ID, notification1); // 通过通知管理器发送通知
    }

    /**
     * 定制通知
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void customNotification() {
        Intent intent = new Intent(this,NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.
                activity_notification_custom);
        remoteViews.setTextViewText(R.id.txt_remove,"custom notification");
        remoteViews.setImageViewResource(R.id.img_remove,R.mipmap.ic_launcher);
        PendingIntent openActivity2PendingIntent = PendingIntent.getActivity(this,
                0,new Intent(this,NotificationActivity.class),PendingIntent.FLAG_UPDATE_CURRENT
                );
        remoteViews.setOnClickPendingIntent(R.id.btn_open_activity,openActivity2PendingIntent);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setTicker("hello world");
        builder.setWhen(System.currentTimeMillis());
        builder.setCustomContentView(remoteViews);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();

        mNotificationManager.notify(2,notification);
    }
    @Override
    protected int getRLayoutId() {
        return R.layout.activity_notification;
    }
}
