package com.ekuaizhi.library.presenter;

import com.ekuaizhi.library.view.ILoadingView;

/**
 * Created by livvym on 15-12-25.
 */
public abstract class LoadingPresenter {

    public ILoadingView loadingView;

    public LoadingPresenter(ILoadingView view){
        this.loadingView = view;
    }
}
