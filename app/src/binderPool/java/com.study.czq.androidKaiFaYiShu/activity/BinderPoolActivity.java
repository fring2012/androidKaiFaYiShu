package com.study.czq.androidKaiFaYiShu.activity;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.study.czq.androidKaiFaYiShu.ICallBack;
import com.study.czq.androidKaiFaYiShu.ICompute;
import com.study.czq.androidKaiFaYiShu.ISecurityCenter;
import com.study.czq.androidKaiFaYiShu.R;
import com.study.czq.androidKaiFaYiShu.base.BaseActivity;
import com.study.czq.androidKaiFaYiShu.binderpool.BinderPool;
import com.study.czq.androidKaiFaYiShu.binderpool.binder.ComputeImpl;
import com.study.czq.androidKaiFaYiShu.binderpool.binder.SecurityCenterImpl;
import com.study.czq.androidKaiFaYiShu.utils.Trace;

public class BinderPoolActivity extends BaseActivity {
    private ISecurityCenter mSecurityCenter;
    private ICompute mCompute;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork(new View(BinderPoolActivity.this));
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected int getRLayoutId() {
        return R.layout.activity_binderpool;
    }

    public void doWork(View view) {
        Trace.d(TAG, "doWork");
        BinderPool binderPool = BinderPool.getInstance(this);
        binderPool.resiginCallBack(new ICallBack.Stub() {
            @Override
            public void onStart() throws RemoteException {
                Trace.d("callback","in " + Thread.currentThread().getName() + " onStart");
            }

            @Override
            public void onEnd() throws RemoteException {
                Trace.d("callback","in " + Thread.currentThread().getName() + " onEnd");
            }
        });
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
        mCompute = ComputeImpl.asInterface(computeBinder);
        try {
            Log.d(TAG, "3+5=" + mCompute.add(3,5));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
