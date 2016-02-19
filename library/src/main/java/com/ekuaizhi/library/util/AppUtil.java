package com.ekuaizhi.library.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import android.util.Log;

import com.ekuaizhi.library.base.BaseApp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 与应用相关的一些实用方法
 */
public class AppUtil {

    /**
     * 获取当前客户端的整数版本号
     *
     * @return int 当前客户端的整数版本号
     */
    public static int appVersionCode() {
        try {
            return BaseApp.getAppContext().getPackageManager().getPackageInfo(BaseApp.getAppContext().getPackageName(), 0).versionCode;
        } catch (Throwable e) {
        }

        return 0;
    }

    /**
     * 获取当前客户端的名称
     */
    public static String appName() {
        String name = null;
        try {
            name = BaseApp.getAppContext().getString(BaseApp.getAppContext().getApplicationInfo().labelRes);
        } catch (Throwable e) {
        }

        if (null == name) {
            name = "";
        }

        return name;
    }

    /**
     * 获取当前应用包名
     */
    public static String packageName() {
        String name = null;
        try {
            name = BaseApp.getAppContext().getPackageName();
        } catch (Throwable e) {
        }

        if (null == name) {
            name = "";
        }
        return name;
    }

    /**
     * 获取当前应用的进程名称 (不会返回null，获取不到只会返回空字符串)
     */
    public static String getCurrentProcessName() {
        try {
            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) BaseApp.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return null == appProcess.processName ? "" : appProcess.processName;
                }
            }
        } catch (Throwable e) {
        }

        return "";
    }


    /**
     * 获取当前客户端的字符串版本号
     */
    public static String appVersionName() {
        try {
            return BaseApp.getAppContext().getPackageManager().getPackageInfo(BaseApp.getAppContext().getPackageName(), 0).versionName;
        } catch (Throwable e) {
        }

        return "";
    }

    /**
     * 获取一个对象的类名
     */
    public static String getClassName(Object x) {
        if (null != x) {
            try {
                String className;
                if (x instanceof Class<?>) {
                    className = ((Class<?>) x).getName();
                } else {
                    className = x.getClass().getName();
                }

                String[] classNames = className.split("\\.");
                return classNames[classNames.length - 1];
            } catch (Throwable e) {
            }
        }

        return "";
    }

    /**
     * 获取当前执行文件的路径
     */
    public static String getAppRunPath() {
        String path;

        try {
            path = BaseApp.getAppContext().getFilesDir().getAbsolutePath();
        } catch (Throwable e) {
            path = "";
        }

        return path;
    }

    /**
     * 获取当前应用 apk 的路径
     *
     * @return String
     */
    public static String getPackagePath() {
        String path;

        try {
            path = BaseApp.getAppContext().getPackageResourcePath();
        } catch (Throwable e) {
            path = "";
        }

        return path;
    }

    /**
     * 获取当前应用的签名
     */
    public static String appSignatures() {
        String signatures = null;

        try {
            signatures = getPackageSignatures(packageName());
        } catch (Throwable e) {
        }

        if (null == signatures) {
            signatures = "";
        }

        return signatures;
    }

    /**
     * 获取指定安装包的签名
     *
     * @return String 返回签名 / 如果指定包名的应用不存在或拿不到签名则返回null
     */
    public static String getPackageSignatures(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }

        PackageInfo packageInfo = null;

        try {
            packageInfo = BaseApp.getAppContext().getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
        } catch (Throwable e) {
        }

        if (null == packageInfo) {
            return null;
        }

        Signature[] signs = packageInfo.signatures;

        if (null == signs || signs.length == 0) {
            return null;
        }

        return signs[0].toCharsString();
    }

}
