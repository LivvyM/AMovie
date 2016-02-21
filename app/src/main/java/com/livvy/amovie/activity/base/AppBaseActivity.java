package com.livvy.amovie.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ekuaizhi.library.base.BaseActivity;
import com.ekuaizhi.library.manager.StatusBarManager;
import com.livvy.amovie.util.ColorUtil;

/**
 *
 * Created by livvy on 16-2-19.
 */
public abstract class AppBaseActivity extends BaseActivity{

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusColor(ColorUtil.COLOR_PRIMARY);
    }

    protected void setStatusColor(int color) {
        new StatusBarManager(this).setColor(color);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
