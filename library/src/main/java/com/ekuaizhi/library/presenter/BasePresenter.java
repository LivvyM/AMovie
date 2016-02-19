package com.ekuaizhi.library.presenter;

import com.ekuaizhi.library.base.BaseApp;

/**
 * Created by livvym on 16-1-4.
 */
public class BasePresenter {

    public String getString(int id){
        return BaseApp.getAppContext().getResources().getString(id);
    }

}
