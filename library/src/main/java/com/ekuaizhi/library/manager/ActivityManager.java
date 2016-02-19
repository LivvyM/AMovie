package com.ekuaizhi.library.manager;

import android.app.Activity;

import com.ekuaizhi.library.util.AppUtil;

import java.util.Stack;

/**
 * Created by livvym on 15-12-25.
 * 一个管理Activity的栈，用来从任何地方来关闭位于最上层的activity
 * 在application中通过 getActivityManager()来的到最上层的实例，并且使用popActivity来关闭一个activity
 * 或关闭当前最上层的activity。
 * 当前android并不提供这样的功能，只能在activity中调用finish()来关闭自己，但是不能关闭其他的activity。
 * 比如我们想实现一个功能从屏幕A—>屏幕B—>屏幕C—>屏幕D,然后在在转到屏幕D之前将屏幕B和C关闭,在屏幕 B和屏幕C界面点击会退按钮都可以回退到上一个屏幕,
 * 但是在屏幕D上点击会退按钮让其回退到A,此外在一些循环跳转的界面上如果不在合适的地方将一些不需要的屏幕关闭,那么经过多次跳转后回导致内存溢出。
 * 对此我们可以设计一个全局的Activity栈,使用这个栈来管理Activity。
 */
public class ActivityManager {
    private static String mActivityPath = "";
    private static Activity mCurActivity = null; // 当前活动 Activity
    private final static Stack<Activity> mActivityStack = new Stack<>(); // Activity 栈

    /**
     * 设置当前活动的 Activity
     *
     * @param activity 当前被激活的 Activity
     */
    public synchronized static void setCurrentActivity(Activity activity) {
        mCurActivity = activity;
    }


    /**
     * 获取当前活动的 Activity
     */
    public synchronized static Activity getCurrentActivity() {
        return mCurActivity;
    }

    /**
     * 退出栈中所有 Activity
     */
    public synchronized static void finishAllActivities() {
        while (true) {
            if (mActivityStack.size() < 1) {
                break;
            }

            Activity curActivity = mActivityStack.lastElement();

            if (curActivity == null) {
                break;
            }

            curActivity.finish();
            mActivityStack.remove(curActivity);
        }

        buildActivityPath();
    }

    /**
     * Activity前向跳转：从栈尾开始销毁指定 Activity 类型之后的所有 Activity
     *
     * @param cls 指定 Activity 类型
     */
    public synchronized static void popToActivity(Class<?> cls) {
        if (null == cls) {
            return;
        }

        while (true) {
            if (mActivityStack.size() < 1) {
                break;
            }

            Activity curActivity = mActivityStack.lastElement();

            if (curActivity == null) {
                break;
            }

            if (curActivity.getClass().equals(cls)) {
                break;
            }

            curActivity.finish();
            mActivityStack.remove(curActivity);
        }

        buildActivityPath();
    }

    /**
     * Activity前向跳转：从栈尾开始销毁指定 Activity 对象之后的所有 Activity
     *
     * @param activity 指定 Activity 对象
     */
    public synchronized static void popToActivity(Activity activity) {
        if (null == activity) {
            return;
        }

        while (true) {
            if (mActivityStack.size() < 1) {
                break;
            }

            Activity curActivity = mActivityStack.lastElement();

            if (curActivity == null) {
                break;
            }

            if (curActivity.equals(activity)) {
                break;
            }

            curActivity.finish();
            mActivityStack.remove(curActivity);
        }

        buildActivityPath();
    }

    /**
     * 获取当前 Activity 的完整路径
     */
    public synchronized static String getActivityPath() {
        return mActivityPath;
    }

    /**
     * 获取当前 Activity 的完整路径
     */
    private synchronized static void buildActivityPath() {
        if (mActivityStack.size() < 1) {
            mActivityPath = "/";
        } else {
            StringBuilder message = new StringBuilder();

            for (Activity x : mActivityStack) {
                message.append(String.format("/%s", AppUtil.getClassName(x)));
            }

            mActivityPath = message.toString();
        }
    }

    /**
     * Activity入栈
     *
     * @param activity Activity 对象
     */
    public synchronized static void pushActivity(Activity activity) {
        if (null == activity) {
            return;
        }

        if (mActivityStack.contains(activity)) {
            mActivityStack.remove(activity);
        }

        mActivityStack.add(activity);

        buildActivityPath();
    }

    /**
     * 从栈尾开始，把指定类名的 Activity 从 activityStack 中移除 遇到不是指定类名的 Activity 则终止
     *
     * @param cls 指定类名的 Activity
     */
    public synchronized static void popLastActivity(Class<?> cls) {
        if (null == cls) {
            return;
        }

        if (mActivityStack.size() < 1) {
            return;
        }

        while (true) {
            Activity curActivity = mActivityStack.lastElement();

            if (curActivity == null) {
                break;
            }

            if (!curActivity.getClass().equals(cls)) {
                break;
            }

            curActivity.finish();
            mActivityStack.remove(curActivity);
        }

        buildActivityPath();
    }

    /**
     * 把指定 Activity 从 activityStack 中移除
     *
     * @param activity 指定 Activity
     */
    public synchronized static void removeActivity(Activity activity) {
        if (mActivityStack.size() < 1 || null == activity) {
            return;
        }

        for (Activity x : mActivityStack) {
            if (x.equals(activity)) {
                mActivityStack.remove(x);
                break;
            }
        }

        buildActivityPath();
    }

    /**
     * 获取栈顶的activity
     */
    public synchronized static Activity getTopTaskActivity(){
        if(mActivityStack.size() < 1){
            return null;
        }
        return mActivityStack.lastElement();
    }

}
