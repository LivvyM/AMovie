package com.ekuaizhi.library.widget.list;

import com.ekuaizhi.library.widget.list.cells.DataExpandListDefaultCell;
import com.ekuaizhi.library.widget.list.cells.DataExpandListEmptyCell;
import com.ekuaizhi.library.widget.list.cells.DataExpandListErrorCell;
import com.ekuaizhi.library.widget.list.cells.DataExpandListLoadingCell;
import com.ekuaizhi.library.widget.list.cells.DataExpandListMoreCell;
import com.ekuaizhi.library.widget.network.DataViewNetWorkCell;
import com.ekuaizhi.library.widget.repeater.DataAdapter;
import com.ekuaizhi.library.widget.repeater.DataExpandCellOrganizer;

/**
 * Created by livvy on 2/20/16.
 */
public class DataExpandListCellCenter {

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
    public static DataExpandCellOrganizer errorOrganizer(DataExpandListAdapter adapter) {
        Class<?> cellClass = null == mDefaultCellProvider ? null : mDefaultCellProvider.getDefaultErrorCellClass();

        if(null == cellClass){
            cellClass = DataExpandListErrorCell.class;
        }

        return new DataExpandCellOrganizer(adapter, cellClass);
    }

    /**
     * 获取数据为空单元格的配置器
     * 配置器中会被设上默认的数据为空单元格
     *
     * @return DataCellOrganizer
     */
    public static DataExpandCellOrganizer emptyOrganizer(DataExpandListAdapter adapter) {
        Class<?> cellClass = null == mDefaultCellProvider ? null : mDefaultCellProvider.getDefaultEmptyCellClass();

        if(null == cellClass){
            cellClass = DataExpandListEmptyCell.class;
        }

        return new DataExpandCellOrganizer(adapter, cellClass);
    }

    /**
     * 获取加载中单元格的配置器
     * 配置器中会被设上默认的加载中单元格
     *
     * @return DataCellOrganizer
     */
    public static DataExpandCellOrganizer loadingOrganizer(DataExpandListAdapter adapter) {
        Class<?> cellClass = null == mDefaultCellProvider ? null : mDefaultCellProvider.getDefaultLoadingCellClass();

        if(null == cellClass){
            cellClass = DataExpandListLoadingCell.class;
        }

        return new DataExpandCellOrganizer(adapter, cellClass);
    }

    /**
     * 获取下一页单元格的配置器
     * 配置器中会被设上默认的下一页单元格
     *
     * @return DataCellOrganizer
     */
    public static DataExpandCellOrganizer moreOrganizer(DataExpandListAdapter adapter) {
        Class<?> cellClass = null == mDefaultCellProvider ? null : mDefaultCellProvider.getDefaultMoreCellClass();

        if(null == cellClass){
            cellClass = DataExpandListMoreCell.class;
        }

        return new DataExpandCellOrganizer(adapter, cellClass);
    }

    /**
     * 获取数据单元格的配置器
     * 配置器中会被设上默认的数据单元格
     *
     * @return DataCellOrganizer
     */
    public static DataExpandCellOrganizer dataOrganizer(DataExpandListAdapter adapter) {
        Class<?> cellClass = null == mDefaultCellProvider ? null : mDefaultCellProvider.getDefaultDataCellClass();

        if(null == cellClass){
            cellClass = DataExpandListDefaultCell.class;
        }

        return new DataExpandCellOrganizer(adapter, cellClass);
    }

    /**
     *获取数据单元格的配置器
     * 配置器中会被设上网络问题
     *
     * @return DataCellOrganizer
     */
    public static DataExpandCellOrganizer networkOrganizer(DataAdapter adapter){
        Class<?> cellClass = null == mDefaultCellProvider ? null : mDefaultCellProvider.getDefaultNetworkCellClass();

        if(null == cellClass){
            cellClass = DataViewNetWorkCell.class;
        }

        return new DataExpandCellOrganizer(adapter, cellClass);
    }
}
