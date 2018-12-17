package com.baidu_map.lhq.permission_demo.jni;

public class myJNI {

    static {
        System.loadLibrary("JniTest");
    }

    public static native String sayHello();
}
