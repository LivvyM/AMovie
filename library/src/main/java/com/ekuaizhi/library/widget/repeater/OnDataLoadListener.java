package com.ekuaizhi.library.widget.repeater;


import com.ekuaizhi.library.data.model.DataResult;

/**
 * 每次从网络加载数据结束后都会调用该回调
 */
public interface OnDataLoadListener {
	void onReceived(DataAdapter adapter, int pageAt, DataResult result);
}
