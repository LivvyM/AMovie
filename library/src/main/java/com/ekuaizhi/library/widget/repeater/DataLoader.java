package com.ekuaizhi.library.widget.repeater;


import com.ekuaizhi.library.data.model.DataResult;

/**
 * 数据装载器，一般从网络获取数据时用
 */
public interface DataLoader {
    /**
     * 设定数据装载器，该函数会在新开辟的线程中加载数据，加载成功后在主线程刷新界面
     */
    DataResult fetchData(DataAdapter adapter, int pageAt, int pageSize);
}
