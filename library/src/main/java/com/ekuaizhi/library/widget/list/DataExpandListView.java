package com.ekuaizhi.library.widget.list;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.ekuaizhi.library.R;
import com.ekuaizhi.library.data.model.DataItem;
import com.ekuaizhi.library.data.model.DataResult;
import com.ekuaizhi.library.widget.list.header.StickyListHeadersListView;
import com.ekuaizhi.library.widget.repeater.DataAdapter;
import com.ekuaizhi.library.widget.repeater.DataCellSelector;
import com.ekuaizhi.library.widget.repeater.DataLoader;
import com.ekuaizhi.library.widget.repeater.DataView;

/**
 * ExpandListView
 *
 * Created by livvy on 2/20/16.
 */
public class DataExpandListView extends StickyListHeadersListView implements DataView {

    protected DataExpandListAdapter mDataAdapter = null; // ListView 的数据适配器
    protected boolean mEnableAutoHeight = false; // ListView 中数据变化后是否自动改变高度
    private int mListViewMaxItemCount = 0; // ListView 中子视图总数（当ListView滚动时，这个值会被赋上）
    private boolean mAllowAutoTurnPage = false; // 是否允许 ListView 自动加载下一页数据
    private boolean mListViewEnableScroll = true; // 是否允许 ListView 滚动，一旦设置为true，listview上下左右都无法拖动
    private long mLastAutoTurnPageTime = 0;// 上次翻页时间
    protected int mListViewScrollState; // 列表滑动状态
    private AbsListView.OnScrollListener mOnScrollListener; // 滑动监听事件

    public DataExpandListView(Context context) {
        super(context, null);
        init(context);
    }

    public DataExpandListView(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.stickyListHeadersListViewStyle);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public DataExpandListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    protected void init(Context context) {
//        setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            }
//        });
//        setOnHeaderClickListener(new OnHeaderClickListener() {
//            @Override
//            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
//
//            }
//        });
//        setOnStickyHeaderChangedListener(new OnStickyHeaderChangedListener() {
//            @Override
//            public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {
//
//            }
//        });
//        setOnStickyHeaderOffsetChangedListener(new OnStickyHeaderOffsetChangedListener() {
//            @Override
//            public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {
//
//            }
//        });
//        setDrawingListUnderStickyHeader(true);
//        setAreHeadersSticky(true);
//        setStickyHeaderTopOffset(-20);

        mList.setDividerHeight(0); // 去掉默认分割线
        mList.setFooterDividersEnabled(false); // 最后一条记录默认无分割线
        mList.setCacheColorHint(Color.parseColor("#00000000"));

        DataExpandListAdapter adapter = new DataExpandListAdapter(this);
        setDataAdapter(adapter);
        initAutoTurnPageListener();
        adapter.initEditModeData();
    }

    /**
     * 当界面恢复时，把列表数据从 Bundle 中取出来
     */
    public void restoreStateFromBundle(Bundle bundle) {
        mDataAdapter.restoreStateFromBundle(bundle);
    }

    /**
     * 当界面被回收时，把列表数据存放到 Bundle 中
     */
    public void saveStateToBundle(Bundle bundle) {
        mDataAdapter.saveStateToBundle(bundle);
    }

    /**
     * 设置单元格点击事件监听器
     *
     * @param listener                  点击事件监听器
     * @param is_listview_real_listener 是否设置原始 ListView 单元格点击事件
     */
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener, boolean is_listview_real_listener) {
        if (is_listview_real_listener) {
            super.setOnItemClickListener(listener);
        } else {
            mDataAdapter.setOnItemClickListener(listener);
        }
    }

    @Override
    public void setDataAdapter(DataAdapter adapter) {
        if (adapter instanceof DataExpandListAdapter) {
            mDataAdapter = (DataExpandListAdapter) adapter;
            setAdapter(mDataAdapter);
        }
    }

    /**
     * 为listView绑上滚动事件，以便进行翻页控制
     */
    void initAutoTurnPageListener() {
        this.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView listView, int state) {
                mListViewScrollState = state;

                if (null != mOnScrollListener) {
                    mOnScrollListener.onScrollStateChanged(listView, state);
                }

                if (mListViewMaxItemCount >= getDataCount() && SCROLL_STATE_IDLE == state) {
                    tryAutoTurnPage();
                }
            }

            @Override
            public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (null != mOnScrollListener) {
                    mOnScrollListener.onScroll(listView, firstVisibleItem, visibleItemCount, totalItemCount);
                }

                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    tryAutoTurnPage();
                } else {
                    mListViewMaxItemCount = firstVisibleItem + visibleItemCount;
                }
            }
        });
    }

    /**
     * 尝试自动翻页
     */
    private void tryAutoTurnPage() {
        if (!mAllowAutoTurnPage) {
            return;
        }

        // 控制自动翻页的频率，500毫秒之内只翻一次
        long curTime = System.currentTimeMillis();
        if (curTime - mLastAutoTurnPageTime < 500) {
            return;
        }

        mLastAutoTurnPageTime = curTime;

        if (!mDataAdapter.getDataLoadControl().getDataLoadNoError()) {
            return;
        }

        if (mDataAdapter.getDataLoadControl().getDataLoadCompleted()) {
            return;
        }

        mDataAdapter.prepareToLoadData();
    }

    /**
     * 设置是否允许自动翻页
     */
    public void setAllowAutoTurnPage(boolean allow) {
        mAllowAutoTurnPage = allow;
    }


    @Override
    public DataAdapter getDataAdapter() {
        return mDataAdapter;
    }

    @Override
    public DataItem getDataItem(int position) {
        return mDataAdapter.getDataItem(position);
    }

    @Override
    public int getDataCount() {
        return mDataAdapter.getDataCount();
    }

    @Override
    public DataResult getDataList() {
        return mDataAdapter.getDataList();
    }

    @Override
    public void setDataCellClass(Class<?> cls, Object cellClassConstructorParameter) {
        mDataAdapter.setDataCellClass(cls, cellClassConstructorParameter);
    }

    @Override
    public void setDataCellClass(Class<?> cls) {
        mDataAdapter.setDataCellClass(cls);
    }

    @Override
    public void setDataCellSelector(DataCellSelector selector, Object cellClassConstructorParameter) {
        mDataAdapter.setDataCellSelector(selector, cellClassConstructorParameter);
    }

    @Override
    public void setDataCellSelector(DataCellSelector selector) {
        mDataAdapter.setDataCellSelector(selector);
    }

    @Override
    public void setupData(DataResult result) {
        mDataAdapter.setupData(result);
    }

    @Override
    public void refreshData() {
        mLastAutoTurnPageTime = 0;
        mDataAdapter.refreshData();
    }

    @Override
    public void prepareToLoadData() {
        mDataAdapter.prepareToLoadData();
    }

    @Override
    public DataLoader getDataLoader() {
        return mDataAdapter.getDataLoader();
    }

    @Override
    public void setDataLoader(DataLoader dataLoader, boolean autoStartLoadData) {
        mDataAdapter.setDataLoader(dataLoader, autoStartLoadData);
    }

    @Override
    public void notifySyncDataSet(boolean onlyStatusChanged) {
        mDataAdapter.notifyDataSetChanged();
    }

    @Override
    public void callEventsBeforeLoadData(@NonNull DataAdapter adapter) {
        setAllowAutoTurnPage(false);
    }

    @Override
    public void callEventsAfterLoadData(@NonNull DataAdapter adapter) {
        setAllowAutoTurnPage(true);
    }

    public int getSelectedItemPosition(){
        return mList.getSelectedItemPosition();
    }

}
