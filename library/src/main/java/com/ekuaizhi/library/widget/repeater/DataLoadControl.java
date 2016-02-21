package com.ekuaizhi.library.widget.repeater;

import android.os.Message;
import android.util.Log;
import android.view.View;

import com.ekuaizhi.library.data.model.DataItem;
import com.ekuaizhi.library.data.model.DataResult;
import com.ekuaizhi.library.util.handler.MessageHandler;


/**
 * 数据加载控制器，仅在 DataAdapter 的实现类中调用
 */
public class DataLoadControl {
    private DataAdapter mAdapter;

    // 事件监听器
    private OnDataLoadListener mOnDataLoadListener = null; // 数据装载器 数据加载事件监听
    private OnDataRefreshedListener mDataRefreshedListener = null; // 数据刷新操作发生时触发的监听器

    // 数据
    private DataLoader mDataLoader = null; // 数据装载器
    private final DataResult mDataList = new DataResult(); // 列表数据容器
    private DataResult mNetLoadData = null; // 网络加载成功后等待被替换的临时数据

    // 加载状态
    private volatile boolean mDataLoadProcessing = false; // 正在从网络加载数据
    private volatile boolean mDataLoadNoError = true; // 当前数据加载是否无错误发生
    private volatile boolean mDataLoadCalled = false; // 已调用开始加载方法
    private volatile boolean mDataHasSetup = false; // 数据是否已设置过

    // 线程相关
    private Thread mDataLoadThread = null; // 数据加载线程
    private long mMainThreadID = Thread.currentThread().getId(); // 获取当前线程的ID

    // 数据刷新相关
    private boolean mKeepDataWhenOnRefresh = true; // 刷新时保留原始数据
    private boolean mDataRefreshNow = false; // 是否正在刷新数据

    // 页码相关
    private int mPageAt = 0; // 当前页码
    private int mPageSize = 10; // 每页翻页大小
    private int mMaxPageCount = 0; // 当前数据最多页数

    public DataLoadControl(DataAdapter adapter) {
        mAdapter = adapter;
    }

    /**
     * 往数据队列中追加数据，并刷新数据视图
     *
     * @param result          要追加的数据
     * @param isDataFromLocal 是否为本地数据，如果是本地数据，追加完数据后，按实际数据条数矫正当前页码；否则页码直接加1
     */
    public void appendDataAndRefreshView(DataResult result, boolean isDataFromLocal) {
        mDataLoadProcessing = false;
        mDataHasSetup = true;
        mDataLoadNoError = mDataList.append(result);

        if (null != result) {
            mDataList.message = result.message;
        }

        if (isDataFromLocal) {
            mPageAt = (int) Math.ceil((float) mDataList.getItemsCount() / mPageSize);

            if (mPageAt < 0) {
                mPageAt = 0;
            }
        } else {
            if (mDataLoadNoError) {
                mPageAt++;
            }
        }

        if (mDataLoadNoError) {
            mMaxPageCount = (int) Math.ceil((float) mDataList.totalcount / mPageSize);
        }

        DataView dataView = mAdapter.getDataView();
        if (null != dataView) {
            dataView.notifySyncDataSet(false);
        }
    }

    /**
     * 设置刷新数据时是否保留原始数据，如果保留，刷新数据时将不会出现载入中的提示信息，而且旧的数据会保留直至加载数据成功
     */
    public DataLoadControl setKeepDataWhenOnRefresh(boolean keepData){
        mKeepDataWhenOnRefresh = keepData;
        return this;
    }

    /**
     * 获取指定位置的数据记录
     */
    public DataItem getDataItem(int position) {
        return mDataList.getItem(position);
    }

    /**
     * 获取数据容器中现有数据的总数
     */
    public int getDataCount() {
        return mDataList.getItemsCount();
    }

    /**
     * 数据容器声称最大数据记录数，一般大于等于现有数据总数
     */
    public int getTotalCount() {
        return mDataList.totalcount;
    }

    /**
     * 返回列表数据容器
     */
    public DataResult getDataList() {
        return mDataList;
    }

    /**
     * 数据是否已被初始化过
     */
    public boolean getDataHasSetup() {
        return mDataHasSetup;
    }

    /**
     * 数据是否正在加载中
     */
    public boolean getDataLoadProcessing() {
        return mDataLoadProcessing;
    }

    /**
     * 数据加载是否无错误
     */
    public boolean getDataLoadNoError(){
        return mDataLoadNoError;
    }

    /**
     * 判断当前数据是否全部加载完毕
     */
    public boolean getDataLoadCompleted() {
        return !mDataLoadProcessing && mDataLoadNoError && (mDataList.totalcount <= mDataList.getItemsCount() || (mPageAt == mMaxPageCount));
    }

    /**
     * 设置当前每页数据条数
     */
    public void setPageSize(int size) {
        if (size < 1) {
            mPageSize = 10;
        } else {
            mPageSize = size;
        }
    }

    /**
     * 获得当前每页数据条数
     */
    public int getPageSize() {
        return mPageSize;
    }

    /**
     * 当前数据页数
     */
    public int getPageAt() {
        return mPageAt;
    }

    /**
     * 获取数据最大的页数
     */
    public int getMaxPageCount() {
        return mMaxPageCount;
    }

    /**
     * 若数据有删除，调用此方法会计算当前真实的页数
     * 这个方法仅仅会影响加载下一页数据时的启示页数，不会刷新界面
     * 调用这个方法时，最好先设置好 DataResult 的数据唯一键名，即调用 setItemUniqueKey 设置一个key。
     */
    public void calculateCurrentPage() {
        mPageAt = (int) Math.floor((float) mDataList.getItemsCount() / mPageSize);
    }

    /**
     * 设置每次数据加载结束后的监听器
     */
    public void setOnDataLoadListener(OnDataLoadListener l) {
        mOnDataLoadListener = l;
    }

    /**
     * 每次数据加载结束后的监听器
     */
    public OnDataLoadListener getOnDataLoadListener() {
        return mOnDataLoadListener;
    }

    /**
     * 设置每次数据刷新结束后的监听器
     */
    public void setOnDataRefreshedListener(OnDataRefreshedListener l) {
        mDataRefreshedListener = l;
    }

    /**
     * 每次数据刷新结束后的监听器
     */
    public OnDataRefreshedListener getOnDataRefreshedListener() {
        return mDataRefreshedListener;
    }

    /**
     * 获取当前数据加载器
     */
    public DataLoader getDataLoader() {
        return mDataLoader;
    }

    /**
     * 设置数据加载器
     */
    public synchronized void setDataLoader(DataLoader dataLoader, boolean autoStartLoadData) {
        mDataLoader = dataLoader;
        if (autoStartLoadData) {
            prepareToLoadData();
        }
    }

    /**
     * 刷新数据
     */
    public synchronized void refreshData() {
        if (null == mDataLoader || mDataLoadProcessing) {
            return;
        }

        mDataRefreshNow = true;
        mDataLoadNoError = true;

        if (!mKeepDataWhenOnRefresh) {
            mDataList.clear();
            mPageAt = 0;
            mMaxPageCount = 0;

            // 这里必须通知一次视图刷新数据，因为 prepareToLoadData 中通知视图刷新数据时会延时
            DataView dataView = mAdapter.getDataView();
            if (null != dataView) {
                dataView.notifySyncDataSet(false);
            }
        }

        prepareToLoadData();
    }

    /**
     * 准备加载数据
     */
    public synchronized void prepareToLoadData() {
        if (null == mDataLoader || mDataLoadProcessing) {
            return;
        }

        if (null != mDataLoadThread && mDataLoadThread.isAlive()) {
            return;
        }

        if (mDataLoadCalled) {
            return;
        }

        mDataLoadCalled = true;

        DataView dataView = mAdapter.getDataView();
        if (null != dataView) {
            dataView.callEventsBeforeLoadData(mAdapter);
        }

        // 如果是 UI 线程发起的数据加载请求，则直接进行加载数据
        // 如果是在其他新线程发起的数据加载请求，则向 mHandler 发消息加载
        if (Thread.currentThread().getId() == mMainThreadID) {
            performLoadData();
        } else {
            mHandler.sendEmptyMessage(HANDLER_START_LOAD_DATA);
        }
    }

    /**
     * 数据从网络加载成功后，同步到适配器并刷新界面
     */
    private void performAppendData() {
        boolean needAppendData = true;
        boolean dataHasRefreshed = mDataRefreshNow;

        if (mDataRefreshNow) {
            mDataRefreshNow = false;

            if (mKeepDataWhenOnRefresh) {
                if (null != mNetLoadData && !mNetLoadData.hasError) {
                    mPageAt = 0;
                    mDataList.clear();
                } else {
                    if (mDataList.isValidListData()) {
                        needAppendData = false;
                    }
                }
            } else {
                mPageAt = 0;
            }
        }

        if (needAppendData) {
            if (null != mNetLoadData) {
                appendDataAndRefreshView(mNetLoadData, false);
                mNetLoadData = null;
            }
        } else {
            mDataLoadProcessing = false;
        }

        if (dataHasRefreshed && null != mDataRefreshedListener) {
            mDataRefreshedListener.onRefreshed(mAdapter);
        }

        DataView dataView = mAdapter.getDataView();
        if (null != dataView) {
            dataView.callEventsAfterLoadData(mAdapter);
        }

        mDataLoadCalled = false;
    }

    /**
     * 通知界面，可在多线程下使用
     */
    public void statusChangedNotify() {
        // 如果线程是UI线程，那么就直接调用 notifySyncDataSet
        // 否则就通过 handle 发消息调用。
        if (Thread.currentThread().getId() == mMainThreadID) {
            mAdapter.notifySyncDataSet(false);
        } else {
            mHandler.sendEmptyMessage(HANDLER_SYNC_VIEW_DATA);
        }
    }

    /**
     * 开启线程，并在线程中加载数据
     */
    private void performLoadData() {
        mDataLoadNoError = true;
        mDataLoadProcessing = true;

        // 通知视图加载状态的变化
        DataView dataView = mAdapter.getDataView();
        if (null != dataView) {
            dataView.notifySyncDataSet(true);
        }

        try {
            mDataLoadThread = new Thread(new Runnable() {
                public void run() {
                    int pageAt = mDataRefreshNow ? 1 : mPageAt + 1;

                    mNetLoadData = mDataLoader.fetchData(mAdapter, pageAt, mPageSize);
                    mHandler.sendEmptyMessage(HANDLER_APPEND_DATA);

                    if (null != mOnDataLoadListener) {
                        mOnDataLoadListener.onReceived(mAdapter, pageAt, mNetLoadData);
                    }
                }
            });

            if (null != dataView && dataView instanceof View) {
                ((View) dataView).post(new Runnable() {
                    public void run() {
                        mDataLoadThread.start();
                    }
                });
            } else {
                mDataLoadThread.start();
            }
        } catch (Throwable e) {
        }
    }

    // mHandler 支持的消息类型
    protected final int HANDLER_SYNC_VIEW_DATA = 1; // 同步数据到视图，数据发生了变动
    protected final int HANDLER_SYNC_VIEW_STATUS = 2; // 同步数据到视图，状态发生了变动
    protected final int HANDLER_APPEND_DATA = 3; // 追加数据并同步数据到视图
    protected final int HANDLER_START_LOAD_DATA = 4; // 通知界面刷新状态，并开始加载数据

    // 适配器主线程 Handler，用来在多线程中刷新UI
    protected MessageHandler mHandler = new MessageHandler() {
        @Override
        public void handleMessage(Message msg) {
            DataView dataView = mAdapter.getDataView();
            switch (msg.what) {
                case HANDLER_SYNC_VIEW_DATA:
                    if (null != dataView) {
                        dataView.notifySyncDataSet(false);
                    }
                    break;

                case HANDLER_SYNC_VIEW_STATUS:
                    if (null != dataView) {
                        dataView.notifySyncDataSet(true);
                    }
                    break;

                case HANDLER_APPEND_DATA:
                    performAppendData();
                    break;

                case HANDLER_START_LOAD_DATA:
                    performLoadData();
                    break;
            }
        }
    };
}
