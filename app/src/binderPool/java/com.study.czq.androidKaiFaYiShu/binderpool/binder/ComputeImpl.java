package com.study.czq.androidKaiFaYiShu.binderpool.binder;

import android.os.RemoteException;
import android.util.Log;

import com.study.czq.androidKaiFaYiShu.ICompute;


public class ComputeImpl extends ICompute.Stub{
    private static final String TAG = "ComputeImpl";
    @Override
    public int add(int a, int b) throws RemoteException {
        Log.d(TAG,"a + b = " + (a + b));
        return a + b;
    }
}
