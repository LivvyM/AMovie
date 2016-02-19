package com.ekuaizhi.library.widget.list;


import com.ekuaizhi.library.widget.list.cells.DataListDefaultCell;
import com.ekuaizhi.library.widget.list.cells.DataListEmptyCell;
import com.ekuaizhi.library.widget.list.cells.DataListErrorCell;
import com.ekuaizhi.library.widget.list.cells.DataListLoadingCell;
import com.ekuaizhi.library.widget.list.cells.DataListMoreCell;
import com.ekuaizhi.library.widget.network.DataViewNetWorkCell;
import com.ekuaizhi.library.widget.repeater.DataAdapter;
import com.ekuaizhi.library.widget.repeater.DataCellOrganizer;

/**
 * 单元格管理中心
 * <p/>
 * 负责初始化默认单元格
 */
public final class DataListCellCenter {
    /**
     * 获取默认单元格的配置器
     */
    private static DataListDefaultCellProvider mDefaultCellProvider = null;

    /**
     * 设置默认单元格的配置器
     */
    public static void setDefaultCellProvider(DataListDefaultCellProvider provider){
        mDefaultCellProvider = provider;
    }

    /**
     * 获取出错单元格的配置器
     * 配置器中会被设上默认的出错单元格
     *
     * @return DataCellOrganizer
     */
    public static DataCellOrganizer errorOrganizer(DataListAdapter adapter) {
        Class<?> cellClass = null == mDefaultCellProvider ? null : mDefaultCellProvider.getDefaultErrorCellClass();

        if(null == cellClass){
            cellClass = DataListErrorCell.class;
        }

        return new DataCellOrganizer(adapter, cellClass);
    }

    /**
     * 获取数据为空单元格的配置器
     * 配置器中会被设上默认的数据为空单元格
     *
     * @return DataCellOrganizer
     */
    public static DataCellOrganizer emptyOrganizer(DataListAdapter adapter) {
        Class<?> cellClass = null == mDefaultCellProvider ? null : mDefaultCellProvider.getDefaultEmptyCellClass();

        if(null == cellClass){
            cellClass = DataListEmptyCell.class;
        }

        return new DataCellOrganizer(adapter, cellClass);
    }

    /**
     * 获取加载中单元格的配置器
     * 配置器中会被设上默认的加载中单元格
     *
     * @return DataCellOrganizer
     */
    public static DataCellOrganizer loadingOrganizer(DataListAdapter adapter) {
        Class<?> cellClass = null == mDefaultCellProvider ? null : mDefaultCellProvider.getDefaultLoadingCellClass();

        if(null == cellClass){
            cellClass = DataListLoadingCell.class;
        }

        return new DataCellOrganizer(adapter, cellClass);
    }

    /**
     * 获取下一页单元格的配置器
     * 配置器中会被设上默认的下一页单元格
     *
     * @return DataCellOrganizer
     */
    public static DataCellOrganizer moreOrganizer(DataListAdapter adapter) {
        Class<?> cellClass = null == mDefaultCellProvider ? null : mDefaultCellProvider.getDefaultMoreCellClass();

        if(null == cellClass){
            cellClass = DataListMoreCell.class;
        }

        return new DataCellOrganizer(adapter, cellClass);
    }

    /**
     * 获取数据单元格的配置器
     * 配置器中会被设上默认的数据单元格
     *
     * @return DataCellOrganizer
     */
    public static DataCellOrganizer dataOrganizer(DataListAdapter adapter) {
        Class<?> cellClass = null == mDefaultCellProvider ? null : mDefaultCellProvider.getDefaultDataCellClass();

        if(null == cellClass){
            cellClass = DataListDefaultCell.class;
        }

        return new DataCellOrganizer(adapter, cellClass);
    }

    /**
     *获取数据单元格的配置器
     * 配置器中会被设上网络问题
     *
     * @return DataCellOrganizer
     */
    public static DataCellOrganizer networkOrganizer(DataAdapter adapter){
        Class<?> cellClass = null == mDefaultCellProvider ? null : mDefaultCellProvider.getDefaultNetworkCellClass();

        if(null == cellClass){
            cellClass = DataViewNetWorkCell.class;
        }

        return new DataCellOrganizer(adapter, cellClass);
    }
}
