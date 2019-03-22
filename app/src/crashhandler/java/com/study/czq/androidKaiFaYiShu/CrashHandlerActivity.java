package com.study.czq.androidKaiFaYiShu;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.study.czq.androidKaiFaYiShu.base.BaseActivity;
import com.study.czq.androidKaiFaYiShu.crachhandler.CrashHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CrashHandlerActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //最好在application里设置，在这里为应用设置异常处理,然后程序才能获取未处理的异常
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);

        Class c;
        try {
            c = Class.forName("android.app.ActivityManager");

            Method m = c.getMethod("checkUidPermission", new Class[] {String.class, int.class});
            Object o = m.invoke(null, new Object[]{"android.permission.READ_CONTACTS", 10010});
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getRLayoutId() {
        return 0;
    }
}
