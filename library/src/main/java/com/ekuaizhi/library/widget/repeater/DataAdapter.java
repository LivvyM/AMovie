package com.ekuaizhi.library.widget.repeater;

import android.content.Context;

import com.ekuaizhi.library.data.model.DataItem;
import com.ekuaizhi.library.data.model.DataResult;


/**
 * 数据适配器接口
 */
public interface DataAdapter {
    /**
     * 获取数据适配器对应的数据视图
     */
    DataView getDataView();

    /**
     * 获取数据适配器对应的上下文句柄
     */
    Context getContext();

    /**
     * 获取数据下载管理器
     */
    DataLoadControl getDataLoadControl();

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
     * 通知视图刷新数据
     *
     * @param onlyStatusChanged 是否只有状态发生了改变
     */
    void notifySyncDataSet(boolean onlyStatusChanged);

    /**
     * 初始化编辑模式下的数据
     */
    void initEditModeData();


}
