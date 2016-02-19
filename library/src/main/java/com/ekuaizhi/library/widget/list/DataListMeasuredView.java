package com.ekuaizhi.library.widget.list;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 数据列表视图:若listview的单元格中有多行数据，请使用该类，解决Listview显示高度的问题；
 */
public class DataListMeasuredView extends DataListView {
    public DataListMeasuredView(Context context) {
        super(context);
    }

    public DataListMeasuredView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DataListMeasuredView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST));
    }
}
