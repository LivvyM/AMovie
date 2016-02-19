package com.ekuaizhi.library.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.ekuaizhi.library.util.ToastUtil;

/**
 * Created by livvym on 15-12-25.
 */
public abstract class BaseFragment extends Fragment{

    public Activity mContext;

    public Handler mLoadHandler;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
        mLoadHandler = new Handler();
    }

    public void loadData(){
        long time = 500;
        mLoadHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoadData();
            }
        },time);
    }

    protected abstract void onLoadData();

    /**
     * @param message toast的内容
     */
    protected void toast(String message) {
        ToastUtil.showShort(getActivity(), message);
    }

    /**
     * @param resId toast的内容来自String.xml
     */
    protected void toast(int resId) {
        ToastUtil.showShort(getActivity(), resId);
    }

    public String getStrings(int resId){
        return BaseApp.getAppContext().getResources().getString(resId);
    }

}
