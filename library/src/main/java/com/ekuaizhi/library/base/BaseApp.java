package com.ekuaizhi.library.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 继承了Application 增加了一个可以作为缓存存放app全局变量的session
 * 一个记录Activity的List。
 *
 * Created by livvy on 15-12-24.
 */
public class BaseApp extends Application{

    public HashMap<String, Object> session;

    public List<Activity> activityManager;

    private static BaseApp instance;

    /**
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    public void init() {
        instance = this;

        session = new HashMap<>();
        activityManager = new ArrayList<>();
    }

    public static Context getAppContext() {
        return instance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
