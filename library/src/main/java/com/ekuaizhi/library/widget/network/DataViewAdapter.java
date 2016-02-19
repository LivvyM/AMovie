package com.ekuaizhi.library.widget.network;

import android.content.Context;
import android.view.View;

import com.ekuaizhi.library.data.model.DataItem;
import com.ekuaizhi.library.data.model.DataResult;
import com.ekuaizhi.library.widget.repeater.DataAdapter;
import com.ekuaizhi.library.widget.repeater.DataCellSelector;
import com.ekuaizhi.library.widget.repeater.DataLoadControl;
import com.ekuaizhi.library.widget.repeater.DataLoader;
import com.ekuaizhi.library.widget.repeater.DataView;

import java.util.HashMap;

/**
 * Created by livvym on 16-2-1.
 */
public class DataViewAdapter implements DataAdapter{

    private Context mContext;
    private DataItem item;

    public DataViewAdapter(Context context){
        this.mContext = context;
    }

    public DataViewAdapter(Context context,DataItem item){
        this.mContext = context;
        this.item     = item;
    }

    public void refreshData(refreshData refreshData){
        final String url = item.getString("url");
        final HashMap<String,String> params = item.getObject("item");
        refreshData.refresh(url,params);
    }

    @Override
    public DataView getDataView() {
        return null;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public DataLoadControl getDataLoadControl() {
        return null;
    }

    @Override
    public DataItem getDataItem(int position) {
        return item;
    }

    @Override
    public int getDataCount() {
        return 0;
    }

    @Override
    public DataResult getDataList() {
        return null;
    }

    @Override
    public DataLoader getDataLoader() {
        return null;
    }

    @Override
    public void setDataLoader(DataLoader dataLoader, boolean autoStartLoadData) {
    }

    @Override
    public void setDataCellClass(Class<?> cls, Object cellClassConstructorParameter) {
    }

    @Override
    public void setDataCellClass(Class<?> cls) {
    }

    @Override
    public void setDataCellSelector(DataCellSelector selector, Object cellClassConstructorParameter) {
    }

    @Override
    public void setDataCellSelector(DataCellSelector selector) {
    }

    @Override
    public void setupData(DataResult result) {
    }

    @Override
    public void refreshData() {
    }

    @Override
    public void prepareToLoadData() {
    }

    @Override
    public void notifySyncDataSet(boolean onlyStatusChanged) {
    }

    @Override
    public void initEditModeData() {
    }

    @Override
    public void notifySyncTagDataSet(boolean isTag) {
    }

    @Override
    public boolean isTag() {
        return false;
    }

    public interface refreshData{
        void refresh(String url, HashMap<String,String> params);
    }
}
