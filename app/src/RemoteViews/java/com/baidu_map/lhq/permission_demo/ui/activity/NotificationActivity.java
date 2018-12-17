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
        }
    }

    public void sendNotification() {

        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher_background); //设置图标
        builder.setTicker("显示第二个通知");
        builder.setContentTitle("通知"); //设置标题
        builder.setContentText("点击查看详细内容"); //消息内容
        builder.setWhen(System.currentTimeMillis()); //发送时间
        builder.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
        builder.setAutoCancel(true);//打开程序后图标消失
        Intent intent =new Intent (NotificationActivity.this,NotificationActivity.class);
        PendingIntent pendingIntent =PendingIntent.getActivity(NotificationActivity.this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        Notification notification1 = builder.build();
        mNotificationManager.notify(ID, notification1); // 通过通知管理器发送通知
    }
    @Override
    protected int getRLayoutId() {
        return R.layout.activity_notification;
    }
}
