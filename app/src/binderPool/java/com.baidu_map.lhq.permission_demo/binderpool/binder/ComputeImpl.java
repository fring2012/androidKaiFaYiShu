package com.baidu_map.lhq.permission_demo.binderpool.binder;

import android.os.RemoteException;

import com.baidu_map.lhq.permission_demo.ICompute;

public class ComputeImpl extends ICompute.Stub{
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
