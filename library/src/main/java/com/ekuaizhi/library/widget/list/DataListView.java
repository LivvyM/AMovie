package com.ekuaizhi.library.widget.list;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ekuaizhi.library.data.model.DataItem;
import com.ekuaizhi.library.data.model.DataResult;
import com.ekuaizhi.library.util.DeviceUtil;
import com.ekuaizhi.library.widget.repeater.DataAdapter;
import com.ekuaizhi.library.widget.repeater.DataCellSelector;
import com.ekuaizhi.library.widget.repeater.DataLoader;
import com.ekuaizhi.library.widget.repeater.DataView;
import com.ekuaizhi.library.widget.repeater.OnDataLoadFinishListener;
import com.ekuaizhi.library.widget.repeater.OnDataRefreshedListener;


/**
 * DataListView 数据列表视图（可加载本地数据和网络数据）
 */
public class DataListView extends ListView implements DataView {
    protected DataListAdapter mDataAdapter = null; // ListView 的数据适配器
    protected boolean mEnableAutoHeight = false; // ListView 中数据变化后是否自动改变高度
    private int mListViewMaxItemCount = 0; // ListView 中子视图总数（当ListView滚动时，这个值会被赋上）
    private boolean mAllowAutoTurnPage = false; // 是否允许 ListView 自动加载下一页数据
    private boolean mListViewEnableScroll = true; // 是否允许 ListView 滚动，一旦设置为true，listview上下左右都无法拖动
    private long mLastAutoTurnPageTime = 0;// 上次翻页时间
    protected int mListViewScrollState; // 列表滑动状态
    private OnScrollListener mOnScrollListener; // 滑动监听事件

    public DataListView(Context context) {
        super(context);
        init(context);
    }

    public DataListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init(context);
    }

    public DataListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs);
        init(context);
    }

    protected void initAttrs(Context context, AttributeSet attrs) {
    }

    protected void init(Context context) {
        setDividerHeight(0); // 去掉默认分割线
        setFooterDividersEnabled(false); // 最后一条记录默认无分割线
        setCacheColorHint(Color.parseColor("#00000000"));

        DataListAdapter adapter = new DataListAdapter(this);
        setDataAdapter(adapter);
        initAutoTurnPageListener();
        adapter.initEditModeData();
    }

    /**
     * 设置 ListView 在设置数据后，自动调整自身高度
     */
    public final void setEnableAutoHeight(boolean enable) {
        mEnableAutoHeight = enable;
    }

    /**
     * 刷新界面状态和数据，该方法可在多线程中使用
     */
    public final void statusChangedNotify() {
        mDataAdapter.getDataLoadControl().statusChangedNotify();
    }

    /**
     * 当前是否会自动调整高度
     */
    public final boolean getEnableAutoHeight() {
        return mEnableAutoHeight;
    }

    /**
     * 设置单元格点击事件监听器
     *
     * @param listener                  点击事件监听器
     * @param is_listview_real_listener 是否设置原始 ListView 单元格点击事件
     */
    public void setOnItemClickListener(OnItemClickListener listener, boolean is_listview_real_listener) {
        if (is_listview_real_listener) {
            super.setOnItemClickListener(listener);
        } else {
            mDataAdapter.setOnItemClickListener(listener);
        }
    }

    /**
     * 设置 HeaderView 点击事件
     */
    public void setOnHeaderClickListener(OnItemClickListener listener) {
        mDataAdapter.setOnHeaderClickListener(listener);
    }

    /**
     * 设置 FooterView 点击事件
     */
    public void setOnFooterClickListener(OnItemClickListener listener) {
        mDataAdapter.setOnFooterClickListener(listener);
    }

    /**
     * 设置数据单元格点击事件
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        setOnItemClickListener(listener, false);
    }

    /**
     * 设置 ListView 滚动监听事件
     */
    public void setDataListViewOnScrollListener(OnScrollListener listener) {
        mOnScrollListener = listener;
    }

    /**
     * 设置每页记录总数
     */
    public void setPageSize(int size) {
        mDataAdapter.getDataLoadControl().setPageSize(size);
    }

    /**
     * 设置数据全部加载完成时的监听事件
     */
    public void setDataLoadFinishListener(OnDataLoadFinishListener listener) {
        mDataAdapter.setOnDataLoadFinishListener(listener);
    }

    /**
     * 往当前列表中追加数据
     * 调用该方法会自动刷新界面
     */
    public final void appendData(DataResult result) {
        mDataAdapter.getDataLoadControl().appendDataAndRefreshView(result, true);
    }

    /**
     * 若数据有删除，调用此方法会计算当前真实的页数
     * 这个方法仅仅会影响加载下一页数据时的启示页数，不会刷新界面
     * 调用这个方法时，最好先设置好 DataResult 的数据唯一键名，即调用 setItemUniqueKey 设置一个key。
     */
    public final void calculateCurrentPage() {
        mDataAdapter.getDataLoadControl().calculateCurrentPage();
    }

    /**
     * 指定 数据为空 单元格的类名和参数
     *
     * @param cls                           单元格类名
     * @param cellClassConstructorParameter 单元格默认构造方法带的参数 (默认为null)
     */
    public final void setEmptyCellClass(Class<?> cls, Object cellClassConstructorParameter) {
        mDataAdapter.mEmptyOrganizer.setCellClass(cls, cellClassConstructorParameter);
    }

    public final void setEmptyCellClass(Class<?> cls) {
        setEmptyCellClass(cls, null);
    }

    /**
     * 指定 出错 单元格的类名和参数
     *
     * @param cls                           单元格类名
     * @param cellClassConstructorParameter 单元格默认构造方法带的参数 (默认为null)
     */
    public final void setErrorCellClass(Class<?> cls, Object cellClassConstructorParameter) {
        mDataAdapter.mErrorOrganizer.setCellClass(cls, cellClassConstructorParameter);
    }

    public final void setErrorCellClass(Class<?> cls) {
        setErrorCellClass(cls, null);
    }

    /**
     * 指定 下一页 单元格的类名和参数
     *
     * @param cls                           单元格类名
     * @param cellClassConstructorParameter 单元格默认构造方法带的参数 (默认为null)
     */
    public final void setMoreCellClass(Class<?> cls, Object cellClassConstructorParameter) {
        mDataAdapter.mMoreOrganizer.setCellClass(cls, cellClassConstructorParameter);
    }

    public final void setMoreCellClass(Class<?> cls) {
        setMoreCellClass(cls, null);
    }

    /**
     * 指定 载入中 单元格的类名和参数
     *
     * @param cls                           单元格类名
     * @param cellClassConstructorParameter 单元格默认构造方法带的参数 (默认为null)
     */
    public final void setLoadingCellClass(Class<?> cls, Object cellClassConstructorParameter) {
        mDataAdapter.mLoadingOrganizer.setCellClass(cls, cellClassConstructorParameter);
    }

    public final void setLoadingCellClass(Class<?> cls) {
        setLoadingCellClass(cls, null);
    }

    /**
     * 自动计算listView的高度
     */
    public final void autoSetHeight() {
        int totalHeight = 0;
        int dataCount = mDataAdapter.getCount();
        int itemWidth = this.getWidth();

        if (itemWidth < 1) {
            itemWidth = DeviceUtil.getScreenPixelsWidth();
        }

        for (int i = 0; i < dataCount; i++) {
            try {
                View listItem = mDataAdapter.getView(i, null, this);

                // 给最外层的单元格视图设置最大宽度，这样里面的元素在计算宽度的时候就有依据了
                try {
                    ViewGroup.LayoutParams params = listItem.getLayoutParams();
                    if (null == params) {
                        if (listItem instanceof LinearLayout) {
                            listItem.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, LinearLayout.LayoutParams.MATCH_PARENT));
                        } else if (listItem instanceof RelativeLayout) {
                            listItem.setLayoutParams(new RelativeLayout.LayoutParams(itemWidth, RelativeLayout.LayoutParams.MATCH_PARENT));
                        }
                    } else {
                        params.width = itemWidth;
                        listItem.setLayoutParams(params);
                    }
                } catch (Throwable e) {
                }

                // 测试量方式改成
                // measure(0, 0)，
                // 原先的
                // measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
                // 算出的高度不正确
                listItem.measure(0, 0);
                int height = listItem.getMeasuredHeight();
                totalHeight += height;
            } catch (Throwable e) {
            }
        }

        ViewGroup.LayoutParams params = this.getLayoutParams();
        params.height = totalHeight + (this.getDividerHeight() * dataCount);
        this.setLayoutParams(params);
    }

    /**
     * 设置是否允许自动翻页
     */
    public void setAllowAutoTurnPage(boolean allow) {
        mAllowAutoTurnPage = allow;
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
     * 为listView绑上滚动事件，以便进行翻页控制
     */
    void initAutoTurnPageListener() {
        this.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView listView, int state) {
                mListViewScrollState = state;

                if (null != mOnScrollListener) {
                    mOnScrollListener.onScrollStateChanged(listView, state);
                }

                if (mListViewMaxItemCount >= getDataCount() && SCROLL_STATE_IDLE == state) {
                    tryAutoTurnPage();
                }

                if (state == SCROLL_STATE_IDLE) {
                    listViewStateIdle();
                } else if (state == SCROLL_STATE_TOUCH_SCROLL) {
                    listViewStateTouchScroll();
                } else if (state == SCROLL_STATE_FLING) {
                    listViewStateFling();
                }
            }

            @Override
            public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                doScrollAction(listView, mListViewScrollState);

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
     * 在列表滚动时做隐藏用户栏作用，主要用户下拉粉丝团隐藏用户信息栏目
     */
    public void doScrollAction(AbsListView listView, int scrollState) {
    }

    /**
     * 滑动事件是否被屏蔽
     */
    public boolean getScrollEnable() {
        return mListViewEnableScroll;
    }

    /**
     * 设置刷新数据时是否保留原始数据，如果保留，刷新数据时将不会出现载入中的提示信息，而且旧的数据会保留直至加载数据成功
     */
    public DataListView setKeepDataWhenOnRefresh(boolean keepData) {
        if(null != mDataAdapter) {
            mDataAdapter.getDataLoadControl().setKeepDataWhenOnRefresh(keepData);
        }

        return this;
    }

    /**
     * 屏蔽上下和左右滑动事件
     */
    public void setScrollEnable(boolean enable) {
        mListViewEnableScroll = enable;

        if (mListViewEnableScroll) {
            this.setOnTouchListener(null);
        } else {
            this.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return event.getAction() == MotionEvent.ACTION_MOVE;
                }
            });
        }
    }

    /**
     * 设置数据刷新结束后的事件监听器
     */
    public void setOnRefreshedListener(OnDataRefreshedListener listener) {
        mDataAdapter.getDataLoadControl().setOnDataRefreshedListener(listener);
    }

    /**
     * DataListView滚动停止，留给子类继承 / 暂无用，准备删除
     */
    protected void listViewStateIdle() {
    }

    /**
     * DataListView触控滑动，留给子类继承 / 暂无用，准备删除
     */
    protected void listViewStateTouchScroll() {
    }

    /**
     * DataListView自动滚动开始，留给子类继承 / 暂无用，准备删除
     */
    protected void listViewStateFling() {
    }

    /**
     * 这个是很早以前的方法，是为了兼容键盘事件，留着也无所谓
     */
    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

        if (gainFocus && previouslyFocusedRect != null) {
            final ListAdapter listAdapter = getAdapter();
            final int adapterCount = listAdapter.getCount();

            switch (direction) {
                case FOCUS_DOWN:
                    for (int i = 0; i < adapterCount; i++) {
                        if (listAdapter.isEnabled(i)) {
                            setSelection(i);
                            break;
                        }
                    }
                    break;

                case FOCUS_UP:
                    for (int i = adapterCount - 1; i >= 0; i--) {
                        if (listAdapter.isEnabled(i)) {
                            setSelection(i);
                            break;
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void setDataAdapter(DataAdapter adapter) {
        if (adapter instanceof DataListAdapter) {
            mDataAdapter = (DataListAdapter) adapter;
            setAdapter(mDataAdapter);
        }
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

}
