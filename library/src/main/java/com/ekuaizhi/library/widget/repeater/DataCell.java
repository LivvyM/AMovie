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
 * 单元格复用抽象类：视图、数据和ViewHolder的合集。
 * <p/>
 * 1.继承和使用时的重要约束：
 * 这个类的子类的构造方法不允许带参数，否则将不会被调用。
 * 其子类可以是独立类、静态化类或在 DataView/DataAdapter/Activity/DataCellSelector 对象中写的内部类。
 * 其他对象中的单元格内部类在指定时需要把对象作为默认实例化参数传进去，详见 DataListView 的 setMoreCellClass/setDataCellSelector/setDataCellClass/setErrorCellClass/setEmptyCellClass 等方法的说明。
 * <p/>
 * 2.子类中需要实现的方法为：
 * (1) createCellView 或 getCellViewLayoutID (只会调用一次)
 * (2) bindView (只会调用一次)
 * (3) bindData
 * <p/>
 * 2.创建时方法的调用顺序：
 * initAdapterAndCellViewForOnce
 * ->updateCellData
 * ->createCellView/getCellViewLayoutID
 * ->bindView
 * ->bindData
 * <p/>
 * 3.复用时方法的调用顺序：
 * updateCellData
 * ->bindData
 */
public abstract class DataCell {
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
    public final void initAdapterAndCellViewForOnce(DataAdapter adapter, int position) {
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
        } catch (Throwable e) {
        }

        // 创建单元格视图失败则会再创建一个默认单元格视图
        if (null == mCellView) {
            mCellView = createDefaultCellView();
        }

        // 设置单元格关系
        mCellView.setTag(this);

        // 绑定单元格视图
        bindView();

        // 绑定单元格视图
        bindData();
    }

    /**
     * 更新单元格对应的数据
     *
     * @param position 单元格的位置
     */
    public final void updateCellData(int position) {
        mPosition = position;
        mDetail = mAdapter.getDataItem(position);
    }

    /**
     * 获取当期位置
     */
    public final int getPosition(){
        return mPosition;
    }

    /**
     * 获取当期位置的数据
     */
    public final DataItem getCellData(){
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
    public final View getCellView() {
        return mCellView;
    }

    /**
     * 通过控件ID来查找单元格上的控件
     *
     * @return View
     */
    public final View findViewById(int id) {
        return mCellView.findViewById(id);
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
     * 绑定单元格视图中的控件到变量
     * 该方法由子类实现
     */
    public abstract void bindView();

    /**
     * 绑定单元格数据到控件
     * 该方法由子类实现
     */
    public abstract void bindData();
}
