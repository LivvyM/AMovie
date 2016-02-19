package com.ekuaizhi.library.widget.network;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekuaizhi.library.util.DeviceUtil;
import com.ekuaizhi.library.widget.repeater.DataCell;

/**
 * Created by livvym on 16-2-1.
 */
public class DataViewNetWorkCell extends DataCell{

    protected TextView mTextView;

    @Override
    public View createCellView(){
        LinearLayout rootView = new LinearLayout(mAdapter.getContext());

        AbsListView.LayoutParams rootParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(rootParams);
        rootView.setGravity(Gravity.TOP);

        mTextView = new TextView(mAdapter.getContext());
        ViewGroup.LayoutParams textViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int textViewPadding = DeviceUtil.dip2px(18);
        mTextView.setLayoutParams(textViewParams);
        mTextView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        mTextView.setPadding(textViewPadding, textViewPadding, textViewPadding, textViewPadding);
        mTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
        mTextView.setTextSize(14);
        mTextView.setText("1111111111111111111111111111111111111111111111");

        rootView.addView(mTextView);

        return rootView;
    }

    @Override
    public int getCellViewLayoutID() {
        return 0;
    }

    @Override
    public void bindView() {

    }

    @Override
    public void bindData() {

    }
}
