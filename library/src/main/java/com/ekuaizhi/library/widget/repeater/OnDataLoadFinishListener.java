package com.ekuaizhi.library.widget.repeater;


import com.ekuaizhi.library.widget.list.DataListAdapter;

/**
 * 数据完全加载完成后的监听事件
 */
public interface OnDataLoadFinishListener<T> {
	void onLoadFinished(T adapter);
}
