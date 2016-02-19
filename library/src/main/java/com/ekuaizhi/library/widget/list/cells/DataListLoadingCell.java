package com.ekuaizhi.library.widget.list.cells;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ekuaizhi.library.util.DeviceUtil;
import com.ekuaizhi.library.widget.repeater.DataCell;


/**
 * 默认的加载中单元格
 * 如果需要写子类，请直接继承 DataCell
 * 当前类不希望被其他子类继承
 */
public final class DataListLoadingCell extends DataCell {
	private TextView mTextView = null;

	@Override
	public final View createCellView(){
		Context context = mAdapter.getContext();
		LinearLayout rootView = new LinearLayout(context);

		ListView.LayoutParams rootParams = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT);
		rootView.setLayoutParams(rootParams);
		rootView.setGravity(Gravity.CENTER);

		LinearLayout parentLayout = new LinearLayout(context);
		ViewGroup.LayoutParams parentParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		parentLayout.setLayoutParams(parentParams);
		parentLayout.setGravity(Gravity.CENTER);
		parentLayout.setOrientation(LinearLayout.HORIZONTAL);

		ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleSmall);
		ViewGroup.LayoutParams progressParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		progressBar.setLayoutParams(progressParams);

		mTextView = new TextView(context);
		ViewGroup.LayoutParams textViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		int textViewPadding = DeviceUtil.dip2px(18);
		mTextView.setLayoutParams(textViewParams);
		mTextView.setGravity(Gravity.START | Gravity.LEFT | Gravity.CENTER_VERTICAL);
		mTextView.setPadding(textViewPadding, textViewPadding, textViewPadding, textViewPadding);
		mTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#777777")));
		mTextView.setTextSize(14);

		parentLayout.addView(progressBar);
		parentLayout.addView(mTextView);
		rootView.addView(parentLayout);

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
	public final void bindData() {
		mTextView.setText("Loading...");
	}
}
