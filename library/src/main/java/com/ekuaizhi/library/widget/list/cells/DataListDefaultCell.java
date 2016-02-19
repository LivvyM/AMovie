package com.ekuaizhi.library.widget.list.cells;

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
 * 默认的数据单元格
 * 这是一个独立可用的数据单元格，如果需要写子类，请直接继承 DataCell
 * 当前类不希望被其他子类继承
 */
public class DataListDefaultCell extends DataCell {
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

		rootView.addView(mTextView);

		return rootView;
	}

	@Override
	public final int getCellViewLayoutID() {
		return 0;
	}

	@Override
	public final void bindView() {
	}

	@Override
	public void bindData() {
		mTextView.setText(mDetail.getString("title"));
	}
}
