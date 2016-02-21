package com.livvy.amovie.app;

import android.content.Context;

import com.ekuaizhi.library.base.BaseApp;
import com.ekuaizhi.library.http.UnifyHttpClient;
import com.ekuaizhi.library.log.LogLevel;
import com.ekuaizhi.library.log.Logger;
import com.ekuaizhi.library.widget.list.DataListCellCenter;
import com.livvy.amovie.R;
import com.livvy.amovie.cell.DefaultCellProvider;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 *
 * Created by livvym on 16-2-19.
 */
public class AppClient extends BaseApp{

    //debug打印的log的TAG
    public static final String TAG = "EKZ-BASE";

    //是否log的总开关 默认为开启的状态  发布之前需要关闭 : LogLevel.NONE
    public static LogLevel logLevel = LogLevel.FULL;

    //网络超时时间
    private static final int CONNECT_TIMEOUT = 10000;

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.init(AppClient.TAG).logLevel(logLevel);
        UnifyHttpClient.getInstance().setHttpLogLevel(logLevel).setConnectTimeout(CONNECT_TIMEOUT);

        // 设置当前项目的默认单元格样式
        DataListCellCenter.setDefaultCellProvider(new DefaultCellProvider());

        //设置字体
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/HYQiHei_50.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        //设置302问题跳转splash页面的action
//        SplashManager.setSplashAction("com.ekuaizhi.kuaizhi.model_main.activity.SplashActivity");

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
