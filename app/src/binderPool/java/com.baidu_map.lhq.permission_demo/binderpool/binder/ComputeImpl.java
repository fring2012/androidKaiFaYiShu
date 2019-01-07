package com.study.czq.androidKaiFaYiShu.binderpool.binder;

import android.os.RemoteException;

import com.study.czq.androidKaiFaYiShu.ICompute;

public class ComputeImpl extends ICompute.Stub{
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
