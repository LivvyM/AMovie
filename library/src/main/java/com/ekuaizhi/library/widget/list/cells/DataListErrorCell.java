package com.ekuaizhi.library.widget.list.cells;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;

import com.ekuaizhi.library.widget.list.DataListView;
import com.ekuaizhi.library.widget.repeater.DataView;


/**
 * 默认的出错单元格
 * 如果需要写子类，请直接继承 DataCell
 * 当前类不希望被其他子类继承
 */
public final class DataListErrorCell extends DataListDefaultCell {
	@Override
	public final View createCellView() {
		View tmpView = super.createCellView();
		mTextView.setGravity(Gravity.CENTER);
		mTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#777777")));
		return tmpView;
	}

	@Override
	public final void bindData() {
		String message = mAdapter.getDataList().message.trim();
		if (message.length() < 1) {
			message = "Load error, click retry!";
		}

        mTextView.setText(message);

        DataView dataView = mAdapter.getDataView();
        if (dataView instanceof DataListView) {
            DataListView listView = (DataListView) dataView;
            if (listView.getEnableAutoHeight()) {
                mTextView.setMaxWidth(listView.getWidth());
            }
        }
	}
}
