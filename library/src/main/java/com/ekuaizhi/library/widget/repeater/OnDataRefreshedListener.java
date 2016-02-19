package com.ekuaizhi.library.widget.repeater;

/**
 * 每次刷新数据完成后都会调用该回调
 */
public interface OnDataRefreshedListener {
    void onRefreshed(DataAdapter adapter);
}
