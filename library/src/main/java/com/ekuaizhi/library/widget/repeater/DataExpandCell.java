package com.ekuaizhi.library.widget.repeater;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ekuaizhi.library.data.model.DataItem;
import com.ekuaizhi.library.util.DeviceUtil;

/**
 * Created by livvy on 2/20/16.
 */
public abstract class DataExpandCell {

    /**
     * 单元格所在 DataListView 对应的 adapter
     * 这个值在 bindData 和  bindView 方法调用时，是不可能为空的
     */
    protected DataAdapter mAdapter;

    /**
     * 单元格对应的View
     * 这个值在  bindData 和  bindView 方法调用时，是不可能为空的。
     * <p/>
     * 这个变量我设成私有的了，禁止子类直接访问；findViewById 方法可以直接用本类的
     */
    private View mCellView;

    /**
     * 单元格Expand对应的View
     *
     */
    private View mExpandView;

    /**
     * 单元格对应的位置。
     * 这个值因为单元格复用的关系，其值会变化。
     * 但是在 bindData 调用前，其值会被预先设置好，所以在 bindData 方法中它可以放心使用。
     */
    protected int mPosition;
    /**
     * 单元格对应的数据。
     * 这个值因为单元格复用的关系，其值会变化。
     * 但是在 bindData 调用前，其值会被预先设置好，所以在 bindData 方法中它可以放心使用。
     */
    protected DataItem mDetail;

    /**
     * 单元格变化的标志位
     * 判断单元格是否需要变化。
     */
    protected boolean isTag;

    /**
     * 获取单元格对应的父级视图
     */
    public DataView getDataView() {
        return mAdapter.getDataView();
    }

    /**
     * 获取单元格对应的上下文句柄
     */
    public Context getContext() {
        return mAdapter.getContext();
    }

    /**
     * 初始化单元格和 adapter 的关系，创建单元格视图
     *
     * @param adapter  单元格对应的 adapter
     * @param position 单元格初始化时的位置
     */
    public void initAdapterAndCellViewForOnce(DataAdapter adapter, int position) {
        // 初始化 adapter 和数据
        mAdapter = adapter;
        updateCellData(position);

        // 创建单元格视图
        try {
            int cellID = getCellViewLayoutID();
            if (cellID != 0) {
                mCellView = LayoutInflater.from(adapter.getContext()).inflate(cellID, null);
            } else {
                mCellView = createCellView();
            }

            int expandID = getExpandCellViewLayoutID();
            if(expandID != 0){
                mExpandView = LayoutInflater.from(adapter.getContext()).inflate(expandID, null);
            }else {
                mExpandView = createCellView();
            }
        } catch (Throwable e) {
        }

        // 创建单元格视图失败则会再创建一个默认单元格视图
        if (null == mCellView) {
            mCellView = createDefaultCellView();
        }

        // 设置单元格关系
        mCellView.setTag(this);
        mExpandView.setTag(this);

        // 绑定单元格视图
        bindView();
        bindExpandView();

        // 绑定单元格视图
        bindData();
        bindExpandData();
    }

    /**
     * 更新单元格对应的数据
     *
     * @param position 单元格的位置
     */
    public  void updateCellData(int position) {
        mPosition = position;
        mDetail = mAdapter.getDataItem(position);
    }

    /**
     * 获取当期位置
     */
    public  int getPosition(){
        return mPosition;
    }

    /**
     * 获取当期位置的数据
     */
    public  DataItem getCellData(){
        return mDetail;
    }

    /**
     * 创建默认单元格视图
     *
     * @return View 返回一个默认单元格
     */
    private View createDefaultCellView() {
        LinearLayout rootLayout = new LinearLayout(mAdapter.getContext());

        ListView.LayoutParams rootParams = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT);
        rootLayout.setLayoutParams(rootParams);
        rootLayout.setGravity(Gravity.TOP);
        rootLayout.setBackgroundColor(Color.parseColor("#E5E5E5"));

        TextView textViewLayout = new TextView(mAdapter.getContext());
        ViewGroup.LayoutParams textViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int textViewPadding = DeviceUtil.dip2px(20);
        textViewLayout.setLayoutParams(textViewParams);
        textViewLayout.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        textViewLayout.setPadding(textViewPadding, textViewPadding, textViewPadding, textViewPadding);
        textViewLayout.setTextColor(ColorStateList.valueOf(Color.parseColor("#FF0000")));
        textViewLayout.setTextSize(14);
        textViewLayout.setText("Default cell view.");

        rootLayout.addView(textViewLayout);

        return rootLayout;
    }

    /**
     * 返回单元格视图
     *
     * @return View
     */
    public  View getCellView() {
        return mCellView;
    }

    public View getExpandView(){
        return mExpandView;
    }

    /**
     * 通过控件ID来查找单元格上的控件
     *
     * @return View
     */
    public  View findViewById(View view ,int id) {
        return view.findViewById(id);
    }

    /**
     * 创建单元格视图
     * 该方法由子类实现；createCellView 和 getCellViewLayoutID 必须实现一个
     * 只有在 getCellViewLayoutID 方法返回0时，才会调用 createCellView
     */
    public View createCellView() {
        return null;
    }

    /**
     * 获取单元格对应的 layoutID
     * 该方法由子类实现；createCellView 和 getCellViewLayoutID 必须实现一个
     * getCellViewLayoutID 方法返回0时会调用 createCellView
     */
    public abstract int getCellViewLayoutID();

    /**
     * 获取单元格Expand对应的 LayoutID
     *
     */
    public abstract int getExpandCellViewLayoutID();

    /**
     * 绑定单元格视图中的控件到变量
     * 该方法由子类实现
     */
    public abstract void bindView();

    public abstract void bindExpandView();

    /**
     * 绑定单元格数据到控件
     * 该方法由子类实现
     */
    public abstract void bindData();

    public abstract void bindExpandData();


    public abstract int getPositionForSection(int sectionIndex);

    public abstract int getSectionForPosition(int position);

    public abstract Object[] getSections();

    public abstract long getHeaderId(int position);

}
