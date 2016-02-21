package com.ekuaizhi.library.widget.repeater;

import android.support.annotation.NonNull;

import com.ekuaizhi.library.data.model.DataItem;
import com.ekuaizhi.library.data.model.DataResult;


/**
 * 数据视图
 */
public interface DataView {
    /**
     * 设置数据适配器
     */
    void setDataAdapter(DataAdapter adapter);

    /**
     * 获取数据视图对应的数据适配器
     */
    DataAdapter getDataAdapter();

    /**
     * 获取指定位置的数据记录
     */
    DataItem getDataItem(int position);

    /**
     * 获取数据容器中现有数据的总数
     */
    int getDataCount();

    /**
     * 获取数据容器
     */
    DataResult getDataList();

    /**
     * 指定 数据单元格类名 和 单元格类的初始化参数
     */
    void setDataCellClass(Class<?> cls, Object cellClassConstructorParameter);

    /**
     * 指定 数据单元格的类名
     */
    void setDataCellClass(Class<?> cls);

    /**
     * 指定 数据单元格选择器 和 单元格类的初始化参数
     */
    void setDataCellSelector(DataCellSelector selector, Object cellClassConstructorParameter);

    /**
     * 指定 数据单元格选择器
     */
    void setDataCellSelector(DataCellSelector selector);

    /**
     * 初始化视图数据
     */
    void setupData(DataResult result);

    /**
     * 刷新界面数据，设置过数据装载器才会生效
     */
    void refreshData();

    /**
     * 准备加载数据
     */
    void prepareToLoadData();

    /**
     * 获取数据装载器
     */
    DataLoader getDataLoader();

    /**
     * 设置数据装载器
     *
     * @param dataLoader 数据装载器
     * @param autoStartLoadData 设置好装载器后是否自动开始加载数据
     */
    void setDataLoader(DataLoader dataLoader, boolean autoStartLoadData);

    /**
     * 通知视图刷新数据
     *
     * @param onlyStatusChanged 是否只有状态发生了改变
     */
    void notifySyncDataSet(boolean onlyStatusChanged);

    /**
     * 数据加载前的回调，仅在 DataLoadControl 中被调用
     */
    void callEventsBeforeLoadData(@NonNull DataAdapter adapter);

    /**
     * 数据加载结束后的回调，仅在 DataLoadControl 中被调用
     */
    void callEventsAfterLoadData(@NonNull DataAdapter adapter);

}
