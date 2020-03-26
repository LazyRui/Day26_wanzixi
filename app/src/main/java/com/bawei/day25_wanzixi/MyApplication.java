package com.bawei.day25_wanzixi;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * @ClassName: MyApplication
 * @Description: Java类的作用
 * @Author: LazyRui
 * @CreateDate: 2020/3/26 18:15
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
    }
}
