// IBinderPool.aidl
package com.baidu_map.lhq.permission_demo;

// Declare any non-default types here with import statements

interface IBinderPool {
    /**
    * @param binderCode,the unique token of specific Binder<br/>
    * @return specific Binder who's token is binderCode.
    */
    IBinder queryBinder(int binderCode);
}
