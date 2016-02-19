package com.ekuaizhi.library.manager;

/**
 * Created by livvym on 16-1-29.
 */
public class SplashManager {

    private static  String mSplashAction;

    /**
     * 切换网络情况家，会导致session失效，连接失效的问题。返回code 302问题。
     * 所以需要加载splash界面，重新登陆用户。
     * action 为需要跳转的页面的全路径
     */
    public static void setSplashAction(String action){
        mSplashAction = action;
    }

    public static String getSplashAction(){
        if(mSplashAction == null){
            mSplashAction = "";
        }
        return mSplashAction;
    }

}
