package com.ekuaizhi.library.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ekuaizhi.library.manager.ActivityManager;
import com.ekuaizhi.library.util.ToastUtil;

/**
 * Created by livvym on 15-12-24.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static BaseApp app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //后台恢复
        app = (BaseApp) this.getApplication();
        // 设置当前活跃的Activity
        if (null == ActivityManager.getCurrentActivity()) {
            ActivityManager.setCurrentActivity(this);
        }
        // 把当前Activity压入栈中
        ActivityManager.pushActivity(this);
    }

    @Override
    protected void onDestroy() {
        // 销毁时 Activity 从栈中移除
        ActivityManager.removeActivity(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        // 下面两行代码主要正对 TabHost 类的 Activity
        ActivityManager.setCurrentActivity(this);
        ActivityManager.pushActivity(this);
        super.onResume();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //恢复页面入口 测试方法--开发者选项-不保留活动
    }

    /**
     * @param message toast的内容
     */
    protected void toast(String message) {
        ToastUtil.showShort(this, message);
    }

    /**
     * @param resId toast的内容来自String.xml
     */
    protected void toast(int resId) {
        ToastUtil.showShort(this, resId);
    }

    public String getStrings(int resId) {
        return getResources().getString(resId);
    }
}
