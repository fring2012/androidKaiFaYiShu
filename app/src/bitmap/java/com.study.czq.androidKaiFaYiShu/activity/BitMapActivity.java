package com.study.czq.androidKaiFaYiShu.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;

import com.study.czq.androidKaiFaYiShu.R;
import com.study.czq.androidKaiFaYiShu.base.BaseActivity;

public class BitMapActivity extends BaseActivity {
    LruCache mMemoryCache;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        LruCache的实现比较简单,读者可以参考它的源码,这里仅介绍如何使用LruCache来
//        实现内存缓存。仍然拿图片缓存来举例子,下面的代码展示了LruCache的典型的初始化过
//        程:
        //在下面的代码中,只需要提供缓存的总容量大小并重写sizeOf方法即可。sizeOf方法
        //的作用是计算缓存对象的大小,这里大小的单位需要和总容量的单位一致。对于下面的示
        //例代码来说,总容量的大小为当前进程的可用内存的1/8,单位为KB,而sizeOf方法则完
        //成了Bitmap对象的大小计算。
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String,Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key,Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };

        //DiskLruCache用于实现存储设备缓存,即磁盘缓存,它通过将缓存对象写入文件系统
        //从而实现缓存的效果。DiskLruCache得到了Android官方文档的推荐
    }

    @Override
    protected int getRLayoutId() {
        return R.layout.activity_bitmap;
    }
}
