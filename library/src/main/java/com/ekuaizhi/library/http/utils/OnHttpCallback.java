package com.ekuaizhi.library.http.utils;

/**
 * Created by livvym on 15-12-25.
 */
public interface OnHttpCallback<T> {

    void onSuccess(T entity);

//    void onFailure(int Status);
}
