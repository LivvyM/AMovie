package com.ekuaizhi.library.widget.list.cells;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;

import com.ekuaizhi.library.widget.list.DataListView;
import com.ekuaizhi.library.widget.repeater.DataView;

/**
 * Created by livvy on 2/20/16.
 */
public class DataExpandListMoreCell extends DataExpandListDefaultCell {
    @Override
    public final View createCellView() {
        View tmpView = super.createCellView();
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
        return tmpView;
    }

    @Override
    public final void bindData() {
        mTextView.setText("More...");

        DataView dataView = mAdapter.getDataView();
        if (dataView instanceof DataListView) {
            DataListView listView = (DataListView) dataView;
            if (listView.getEnableAutoHeight()) {
                mTextView.setMaxWidth(listView.getWidth());
            }
        }
    }
}