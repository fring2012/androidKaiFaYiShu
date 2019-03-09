package com.study.czq.androidKaiFaYiShu.binderpool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;


import com.study.czq.androidKaiFaYiShu.IBinderPool;
import com.study.czq.androidKaiFaYiShu.ICallBack;
import com.study.czq.androidKaiFaYiShu.binderpool.binder.ComputeImpl;
import com.study.czq.androidKaiFaYiShu.binderpool.binder.SecurityCenterImpl;
import com.study.czq.androidKaiFaYiShu.service.BinderPoolService;
import com.study.czq.androidKaiFaYiShu.utils.Trace;

import java.util.concurrent.CountDownLatch;

public class BinderPool {
    private static final String TAG = "BinderPool";
    public static final int BINDER_NONE = -1;
    public static final int BINDER_COMPUTE = 0;
    public static final int BINDER_SECURITY_CENTER = 1;
    private Context mContext;
    private IBinderPool mBinderPool;
    private static volatile BinderPool sInstance;
    private CountDownLatch mConnectBinderPoolCountDownLatch;
    private BinderPool(Context context){
        mContext = context.getApplicationContext();
        connectBinderPoolService();
    }
    public static BinderPool getInstance(Context context){
        if (sInstance == null){
            synchronized (BinderPool.class){
                if (sInstance == null){
                    sInstance = new BinderPool(context);
                }
            }
        }
        return sInstance;
    }


    private synchronized void connectBinderPoolService(){
        Trace.d(TAG, "connectBinderPoolService");
        mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent service = new Intent(mContext,BinderPoolService.class);
//        mContext.startService(service);
        mContext.bindService(service,mBinderPoolConnection,Context.BIND_AUTO_CREATE);
        try {
            mConnectBinderPoolCountDownLatch.await();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
     * query binder by binderCode from binder pool
     *
     * @param binderCode
     *
    the unique token of binder
     * @return binder who's token is binderCode<br>
     *
    return null when not found or BinderPoolService died.
     */
    public IBinder queryBinder(int binderCode){
        IBinder binder = null;
        try {
            if (mBinderPool != null) {
                binder = mBinderPool.queryBinder(binderCode);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return binder;
    }
    public boolean resiginCallBack(ICallBack callBack) {
        Trace.d(TAG,"callBack:" + callBack);
        try {
            if (mBinderPool != null) {
                return mBinderPool.resiginCallBack(callBack);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;

    }
    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            /**
             * 如果在同一进程，参数iBinder的类型为BinderPoolImpl,如果在不同进程iBinder类型为BinderPoxy
             */
            Trace.d(TAG, "onServiceConnected:" + iBinder);
            mBinderPool = IBinderPool.Stub.asInterface(iBinder);
            try {
                mBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mConnectBinderPoolCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private IBinder.DeathRecipient mBinderPoolDeathRecipient = new Binder.DeathRecipient(){

        @Override
        public void binderDied() {
            Trace.w(TAG, "binder died");
            mBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient, 0);
            mBinderPool = null;
            connectBinderPoolService();
        }
    };






    public static class BinderPoolImpl extends IBinderPool.Stub{
        private ICallBack mICallBack;
        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            if (mICallBack != null)
                mICallBack.onStart();
            IBinder binder = null;
            switch (binderCode){
                case BINDER_SECURITY_CENTER:
                    binder = new SecurityCenterImpl();
                    break;
                case BINDER_COMPUTE:
                    binder = new ComputeImpl();
                    break;
                default:
                    break;
            }
            if (mICallBack != null)
                mICallBack.onEnd();
            return binder;
        }

        @Override
        public boolean resiginCallBack(ICallBack callBack) throws RemoteException {
            mICallBack = callBack;
            return true;
        }
    }

}
