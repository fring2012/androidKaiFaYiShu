package com.study.czq.androidKaiFaYiShu.jni;

public class myJNI {

    static {
        System.loadLibrary("JniTest");
    }

    public static native String sayHello();
}
