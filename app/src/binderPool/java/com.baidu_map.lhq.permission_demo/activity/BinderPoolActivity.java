package com.study.czq.androidKaiFaYiShu.ui.activity;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.study.czq.androidKaiFaYiShu.ISecurityCenter;
import com.study.czq.androidKaiFaYiShu.R;
import com.study.czq.androidKaiFaYiShu.base.BaseActivity;
import com.study.czq.androidKaiFaYiShu.binderpool.BinderPool;
import com.study.czq.androidKaiFaYiShu.binderpool.binder.ComputeImpl;
import com.study.czq.androidKaiFaYiShu.binderpool.binder.SecurityCenterImpl;

public class BinderPoolActivity extends BaseActivity {
    private ISecurityCenter mSecurityCenter;
    private ComputeImpl mCompute;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        }).start();
    }

    @Override
    protected int getRLayoutId() {
        return R.layout.activity_binderpool;
    }

    private void doWork() {
        Log.d(TAG, "doWork");
        BinderPool binderPool = BinderPool.getInstance(this);
        IBinder securityBinder = binderPool
                .queryBinder(BinderPool.BINDER_SECURITY_CENTER);

        mSecurityCenter = (ISecurityCenter) SecurityCenterImpl.asInterface(securityBinder);
        Log.d(TAG,"visit ISecurityCenter");
        String msg = "helloworld-安卓";
        Log.d(TAG, "content:" + msg);
        try {
            String password = mSecurityCenter.encrypt(msg);
            Log.d(TAG, "encrypt:" + password);
            Log.d(TAG, "decrypt:" + mSecurityCenter.decrypt(password));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.d(TAG,"visit ICompute");
        IBinder computeBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
        mCompute = (ComputeImpl) ComputeImpl.asInterface(computeBinder);
        try {
            Log.d(TAG, "3+5=" + mCompute.add(3,5));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
