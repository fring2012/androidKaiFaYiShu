package com.baidu_map.lhq.permission_demo.main;

import android.content.UriMatcher;
import android.net.Uri;

import java.util.concurrent.CountDownLatch;

public class test {
    public static void main(String[] args) {
        final CountDownLatch countDownLatch = new CountDownLatch(2){
            @Override
            public void await() throws InterruptedException {
                super.await();
                System.out.println(Thread.currentThread().getName() +  " count down is ok");
            }
        };

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                //do something
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " is done");
                countDownLatch.countDown();
                System.out.println(Thread.currentThread().getName() + " 继续执行");
            }
        }, "thread1");

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                //do something
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " is done");
                countDownLatch.countDown();
                System.out.println(Thread.currentThread().getName() + " 继续执行");
            }
        }, "thread2");


        thread1.start();
        thread2.start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
