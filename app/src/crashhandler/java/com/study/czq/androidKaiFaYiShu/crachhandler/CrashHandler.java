package com.study.czq.androidKaiFaYiShu.crachhandler;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import com.study.czq.androidKaiFaYiShu.utils.Trace;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
  Android应用不可避免地会发生crash,也称之为崩溃,无论你的程序写得多么完美,
  总是无法完全避免crash的发生,可能是由于Android系统底层的bug,也可能是由于不充分
  的机型适配或者是糟糕的网络状况。当crash发生时,系统会kill掉正在执行的程序,现象
  就是闪退或者提示用户程序已停止运行,这对用户来说是很不友好的,也是开发者所不愿
  意看到的。更糟糕的是,当用户发生了crash,开发者却无法得知程序为何crash,即便开
 发人员想去解决这个crash,但是由于无法知道用户当时的crash信息,所以往往也无能为
  力。幸运的是,Android提供了处理这类问题的方法,请看下面Thread类中的一个方法
  setDefaultUncaughtExceptionHandler:

     * * Sets the default uncaught exception handler. This handler is invoked in
     * * case any Thread dies due to an unhandled exception.
     * *
     * * @param handler
     * *
     * The handler to set or null.
     *
    public static void setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler handler){
        Thread.defaultUncaughtHandler=handler;
    }
    从方法的字面意义来看,这个方法好像可以设置系统的默认异常处理器,其实这个方
    法就可以解决上面所提到的crash问题。当crash发生的时候,系统就会回调
    UncaughtExceptionHandler的uncaughtException方法,在uncaughtException方法中就可以获
    取到异常信息,可以选择把异常信息存储到SD卡中,然后在合适的时机通过网络将crash
    信息上传到服务器上,这样开发人员就可以分析用户crash的场景从而在后面的版本中修
    复此类crash。我们还可以在crash发生时,弹出一个对话框告诉用户程序crash了,然后再
    退出,这样做比闪退要温和一点
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;
    private static final String PATH = Environment.getExternalStorageDirectory().getPath();
    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".trace";
    private static CrashHandler sInstance = new CrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;
    private CrashHandler() {
    }
    public static CrashHandler getInstance() {
        return sInstance;
    }
    public void init(Context context) {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }
    /**
     * 这个是最关键的函数,当程序中有未被捕获的异常,系统将会自动调用#uncaught-
     Exception方法
     * thread为出现未捕获异常的线程,ex为未捕获的异常,有了这个ex,我们就可以得到异
     常信息
     */
    @Override
    public void uncaughtException(Thread thread,Throwable ex) {
        try {
            //导出异常信息到SD卡中
            dumpExceptionToSDCard(ex);
            //这里可以上传异常信息到服务器,便于开发人员分析日志从而解决bug
            uploadExceptionToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ex.printStackTrace();
        //如果系统提供了默认的异常处理器,则交给系统去结束程序,否则就由自己结束自己
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread,ex);
        } else {
            System.exit(0);
//            Process.killProcess(Process.myPid());
        }
    }
    private void dumpExceptionToSDCard(Throwable ex) throws IOException {
//如果SD卡不存在或无法使用,则无法把异常信息写入SD卡
        if (!Environment.getExternalStorageState().equals(Environment.
                MEDIA_MOUNTED))
        {
            if (DEBUG) {
                Trace.w(TAG,"sdcard unmounted,skip dump exception");
                return;
            }
        }
        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new
                Date(current));
        File file = new File(PATH + FILE_NAME + time + FILE_NAME_SUFFIX);try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new
                    FileWriter(file)));
            pw.println(time);
            dumpPhoneInfo(pw);
            pw.println();
            ex.printStackTrace(pw);
            pw.close();
        } catch (Exception e) {
            Trace.e(TAG,"dump crash info failed");
        }
    }
    private void dumpPhoneInfo(PrintWriter pw)throws PackageManager.NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),
                PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);
        //Android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);
        //手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);
        //手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);
        //CPU架构
        pw.print("CPU ABI: ");pw.println(Build.CPU_ABI);
    }
    private void uploadExceptionToServer() {
//TODO Upload Exception Message To Your Web Server
    }
}
