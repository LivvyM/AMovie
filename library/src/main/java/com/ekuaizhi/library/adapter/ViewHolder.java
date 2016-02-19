package com.ekuaizhi.library.adapter;

import android.content.Context;
import android.view.View;

import com.ekuaizhi.library.adapter.annotations.FieldAnnotationParser;


/**
 * 扩展这个类如果你想举行一个给定的视图的子视图
 * 注释视图字段{ @link com.lm.library.lmadapter.annotations。ViewId },i.e @ViewId(R.id.textView1)。
 * 它将寻找viewId的给定的父视图,并将分配子类的带注释的字段的视图。
 */
public abstract class ViewHolder {

    private View mView;

    /**
     * @param view
     */
    public ViewHolder(View view) {
        mView = view;
        FieldAnnotationParser.setViewFields(this, view);
    }

    /**
     * @return 设置视图 {@link #ViewHolder(View)}
     */
    public View getView() {
        return mView;
    }

    /**
     * 返回上下文举行视图中运行,因此它可以用来访问资源,等等。
     *
     * @return 上下文在视图中运行的context。
     */
    public Context getContext() {
        return mView.getContext();
    }

}
