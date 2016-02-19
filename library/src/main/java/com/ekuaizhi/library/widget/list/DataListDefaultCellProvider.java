package com.ekuaizhi.library.widget.list;

/**
 * 默认单元格提供者：列表视图控件共封装了5种类型的单元格，如果使用的地方没有设置则会用默认单元格提供者提供的默认单元格类名
 * 所有返回的单元格类必须继承自DataCell，否则视为无效
 * 每个函数若返回null表示使用公共库中的默认单元格
 */
public interface DataListDefaultCellProvider {
    /**
     * 出错单元格类
     */
    Class<?> getDefaultErrorCellClass();

    /**
     * 数据为空单元格类
     */
    Class<?> getDefaultEmptyCellClass();

    /**
     * 载入中单元格类
     */
    Class<?> getDefaultLoadingCellClass();

    /**
     * 下一页单元格类
     */
    Class<?> getDefaultMoreCellClass();

    /**
     * 数据单元格类
     */
    Class<?> getDefaultDataCellClass();

    /**
     * 网络异常数据
     */
    Class<?> getDefaultNetworkCellClass();
}
