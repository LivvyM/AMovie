package com.ekuaizhi.library.widget.list;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

import com.ekuaizhi.library.data.model.DataItem;
import com.ekuaizhi.library.data.model.DataResult;
import com.ekuaizhi.library.widget.repeater.DataAdapter;
import com.ekuaizhi.library.widget.repeater.DataCellOrganizer;
import com.ekuaizhi.library.widget.repeater.DataCellSelector;
import com.ekuaizhi.library.widget.repeater.DataLoadControl;
import com.ekuaizhi.library.widget.repeater.DataLoader;
import com.ekuaizhi.library.widget.repeater.DataView;
import com.ekuaizhi.library.widget.repeater.OnDataLoadFinishListener;


/**
 * DataListView 数据适配器
 */
public final class DataListAdapter extends SimpleAdapter implements OnItemClickListener, DataAdapter {
    // 界面相关
    private DataListView mListView = null; // 列表视图
    private Context mContext = null; // 视图上下文控件

    // 数据加载管理器
    private DataLoadControl mDataControl = new DataLoadControl(this);

    // 单元格样式
    protected final DataCellOrganizer mErrorOrganizer = DataListCellCenter.errorOrganizer(this); // 出错单元格样式
    protected final DataCellOrganizer mLoadingOrganizer = DataListCellCenter.loadingOrganizer(this); // 载入中单元格样式
    protected final DataCellOrganizer mEmptyOrganizer = DataListCellCenter.emptyOrganizer(this); // 空单元格样式
    protected final DataCellOrganizer mMoreOrganizer = DataListCellCenter.moreOrganizer(this); // 更多/下一页单元格样式
    protected final DataCellOrganizer mDataOrganizer = DataListCellCenter.dataOrganizer(this); // 正常数据单元格

    // 事件监听器
    private OnItemClickListener mItemClickListener = null; // 正常数据单元格的点击事件
    private OnItemClickListener mHeaderClickListener = null; // 列表头附加视图的点击事件
    private OnItemClickListener mFooterClickListener = null; // 列表底部附加视图的点击事件
    private OnDataLoadFinishListener<DataListAdapter> mOnDataLoadFinishListener = null; // 数据完全加载完成后的事件监听器

    // 单元格数量
    private int mMaxDataCount = 0; // 当前子视图总数

    private boolean isTag = false;

    public DataListAdapter(DataListView listView) {
        super(listView.getContext(), null, 0, null, null);

        mListView = listView;
        mListView.setOnItemClickListener(this, true);
        mContext = mListView.getContext();
    }

    public DataListAdapter(Context context) {
        super(context, null, 0, null, null);

        mContext = context;
    }

    /**
     * 设置正常单元格点击事件
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    /**
     * 设置 headerview 点击事件
     */
    public void setOnHeaderClickListener(OnItemClickListener listener) {
        mHeaderClickListener = listener;
    }

    /**
     * 设置 listview 之 footerview 点击事件
     */
    public void setOnFooterClickListener(OnItemClickListener listener) {
        mFooterClickListener = listener;
    }

    /**
     * 当界面恢复时，把列表数据从 Bundle 中取出来
     */
    public void restoreStateFromBundle(Bundle bundle) {
        if (null == bundle) {
            return;
        }

        DataResult listData = bundle.getParcelable("listData");

        if (null != listData) {
            mDataControl.appendDataAndRefreshView(listData, true);

            int selectedItemPosition = bundle.getInt("selectedItemPosition");
            if (selectedItemPosition > 0) {
                mListView.setSelection(selectedItemPosition);
            }
        }
    }

    /**
     * 当界面被回收时，把列表数据存放到 Bundle 中\
     */
    public Bundle saveStateToBundle(Bundle bundle) {
        if (null == bundle) {
            bundle = new Bundle();
        }

        bundle.putParcelable("listData", mDataControl.getDataList());
        bundle.putInt("selectedItemPosition", mListView.getSelectedItemPosition());

        return bundle;
    }

    /**
     * 设置全部数据加载完成后的监听事件
     */
    public void setOnDataLoadFinishListener(OnDataLoadFinishListener listener) {
        mOnDataLoadFinishListener = listener;
    }

    /**
     * 计算并校验当前的数据对应单元格总数
     */
    private void calculateItemsCount() {
        // 当前加载的数据数量小于1, 可能是 数据为空/数据加载中/数据加载出错，都要多一个单元格
        if (mDataControl.getDataCount() < 1) {
            mMaxDataCount = 1;
            return;
        }

        // 若当前加载的数据数量大于1,而且又等于数据总数，这表示操作成功
        if (mDataControl.getTotalCount() == mDataControl.getDataCount()) {
            mMaxDataCount = mDataControl.getTotalCount();
            return;
        }

        // 当前加载数据数量小于数据总量，不是出错就是数据正在加载中，都要多一个单元格
        mMaxDataCount = mDataControl.getDataCount() + (mDataControl.getMaxPageCount() > mDataControl.getPageAt() ? 1 : 0);
    }

    /**
     * 获取对应位置的单元格组织者
     */
    private DataCellOrganizer getItemOrganizer(int position) {
        // 当前加载的数据数量小于1
        // 可能有四种情况：出错、加载中、数据为空、没到最后一页但又没有数据也不在加载中状态
        if (mDataControl.getDataCount() < 1) {
            if (mDataControl.getDataLoadProcessing() || !mDataControl.getDataHasSetup()) { // 加载中
                return mLoadingOrganizer;
            } else if (!mDataControl.getDataLoadNoError()) { // 出错
                return mErrorOrganizer;
            } else if (mDataControl.getPageSize() * mDataControl.getPageAt() < mDataControl.getTotalCount()) {
                // 没到最后一页但又没有数据也不在加载中状态
                return mMoreOrganizer;
            }
            // 数据为空
            return mEmptyOrganizer;
        }

        // 正常数据
        if (position < mDataControl.getDataCount()) {
            return mDataOrganizer;
        }

        if (mDataControl.getDataLoadProcessing()) { // 加载中
            return mLoadingOrganizer;
        } else if (!mDataControl.getDataLoadNoError()) { // 出错
            return mErrorOrganizer;
        }

        // 下一页
        return mMoreOrganizer;
    }

    /**
     * 刷新界面数据
     */
    @Override
    public void notifyDataSetChanged() {
        // 计算一下单元格数量；因为加载完毕后单元格数量会发生变化
        calculateItemsCount();

        if (null != mListView) {
            super.notifyDataSetChanged();

            // 自动计算 listView 的高度； 放在这里看上去有点不妥，但是没有收到问题反馈
            if (mListView.getEnableAutoHeight()) {
                mListView.autoSetHeight();
            }
        }

        // 备注：
        // mOnDataLoadFinishListener.onLoadFinished 事件只能在数据全部正确加载完成后才会调用
        if (null != mOnDataLoadFinishListener && mDataControl.getDataLoadCompleted()) {
            mOnDataLoadFinishListener.onLoadFinished(DataListAdapter.this);
        }
    }

    /**
     * 区分点击事件，劫持 “载入中”、“出错”、“下一页” 等单元格的事件
     */
    @Override
    public void onItemClick(AdapterView<?> adapter, View viewGroup, int position, long id) {
        if (null == mListView) {
            return;
        }

        int headerCount = mListView.getHeaderViewsCount();

        if (position < headerCount) {
            if (null != mHeaderClickListener) {
                mHeaderClickListener.onItemClick(adapter, viewGroup, position, id);
            }
            return;
        }

        if (position > headerCount + getCount()) {
            if (null != mFooterClickListener) {
                mFooterClickListener.onItemClick(adapter, viewGroup, position, id);
            }
            return;
        }

        position -= headerCount;

        if (position < mDataControl.getDataCount()) {
            if (null != mItemClickListener) {
                mItemClickListener.onItemClick(adapter, mListView, position, id);
            }
        } else {
            if (!mDataControl.getDataLoadProcessing()) {
                prepareToLoadData();
            }
        }
    }

    @Override
    public int getCount() {
        return mMaxDataCount;
    }

    @Override
    public Object getItem(int position) {
        return mDataControl.getDataItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 获取单元格的顺序，顺序的排列方式为：加载中单元格 -> 出错单元格 -> 下一页单元格 -> 空单元格 -> 数据单元格
     */
    @Override
    public int getItemViewType(int position) {
        DataCellOrganizer organizer = getItemOrganizer(position);
        int startViewType = 0;

        if (!organizer.equals(mLoadingOrganizer)) {
            startViewType += mLoadingOrganizer.getCellTypeCount();

            if (!organizer.equals(mErrorOrganizer)) {
                startViewType += mErrorOrganizer.getCellTypeCount();

                if (!organizer.equals(mMoreOrganizer)) {
                    startViewType += mMoreOrganizer.getCellTypeCount();

                    if (!organizer.equals(mEmptyOrganizer)) {
                        startViewType += mEmptyOrganizer.getCellTypeCount();
                    }
                }
            }
        }

        return startViewType + organizer.getCellType(position);
    }

    /**
     * 获取单元格类型总数
     */
    @Override
    public int getViewTypeCount() {
        return mLoadingOrganizer.getCellTypeCount() + mErrorOrganizer.getCellTypeCount() + mMoreOrganizer.getCellTypeCount() + mEmptyOrganizer.getCellTypeCount() + mDataOrganizer.getCellTypeCount();
    }

    /**
     * 获取对应位置的单元格视图
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DataCellOrganizer organizer = getItemOrganizer(position);
        return organizer.getCellView(convertView, position);
    }

    @Override
    public DataView getDataView() {
        return mListView;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public DataLoadControl getDataLoadControl() {
        return mDataControl;
    }

    @Override
    public DataItem getDataItem(int position) {
        return mDataControl.getDataItem(position);
    }

    @Override
    public int getDataCount() {
        return mDataControl.getDataCount();
    }

    @Override
    public DataResult getDataList() {
        return mDataControl.getDataList();
    }

    @Override
    public DataLoader getDataLoader() {
        return mDataControl.getDataLoader();
    }

    @Override
    public void setDataLoader(DataLoader dataLoader, boolean autoStartLoadData) {
        mDataControl.setDataLoader(dataLoader, autoStartLoadData);
    }

    @Override
    public void setDataCellClass(Class<?> cls, Object cellClassConstructorParameter) {
        mDataOrganizer.setCellClass(cls, cellClassConstructorParameter);
    }

    @Override
    public void setDataCellClass(Class<?> cls) {
        mDataOrganizer.setCellClass(cls, null);
    }

    @Override
    public void setDataCellSelector(DataCellSelector selector, Object cellClassConstructorParameter) {
        mDataOrganizer.setCellSelector(selector, cellClassConstructorParameter);
    }

    @Override
    public void setDataCellSelector(DataCellSelector selector) {
        mDataOrganizer.setCellSelector(selector, null);
    }

    @Override
    public void setupData(DataResult result) {
        mDataControl.getDataList().clear();
        mDataControl.appendDataAndRefreshView(result, true);
    }

    @Override
    public void refreshData() {
        mDataControl.refreshData();
    }

    @Override
    public void prepareToLoadData() {
        mDataControl.prepareToLoadData();
    }

    @Override
    public void notifySyncDataSet(boolean onlyStatusChanged) {
        if (null != mListView) {
            mListView.notifySyncDataSet(onlyStatusChanged);
        }
    }

    @Override
    public void initEditModeData() {
        if (null == mListView || !mListView.isInEditMode()) {
            return;
        }

        mDataControl.getDataList().clear();

        for (int i = 0; i < 4; i++) {
            DataItem item = new DataItem();
            item.setString("title", "Item " + i);
            mDataControl.getDataList().addItem(item);
        }

        notifyDataSetChanged();
    }

    @Override
    public void notifySyncTagDataSet(boolean isTag) {
        this.isTag = isTag;
        notifyDataSetChanged();
    }

    @Override
    public boolean isTag() {
        return isTag;
    }
}