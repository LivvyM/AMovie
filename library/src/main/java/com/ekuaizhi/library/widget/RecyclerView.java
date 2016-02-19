package com.ekuaizhi.library.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.util.Log;

import com.ekuaizhi.library.log.Logger;

/**
 * Created by livvym on 16-1-6.
 *
 */
public class RecyclerView extends android.support.v7.widget.RecyclerView {

    private OnLackDataListener onLackDataListener;
    private OnScrollListener onScrollListener;

    public RecyclerView(Context context) {
        this(context, null);
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        super.setLayoutManager(new LinearLayoutManager(getContext()));
        super.addOnScrollListener(onScrollDefaultListener);
    }

    private OnScrollListener onScrollDefaultListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(android.support.v7.widget.RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (onScrollListener != null) {
                onScrollListener.onScrollStateChanged(recyclerView, newState);
            }
        }
        @Override
        public void onScrolled(android.support.v7.widget.RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (onLackDataListener != null) {
                try {
                    LinearLayoutManager manager = (LinearLayoutManager) getLayoutManager();
                    if (manager.findLastVisibleItemPosition() >= manager.getItemCount() - onLackDataListener.minNumber() && dy > 0 && !onLackDataListener.isLoading()) {
                        onLackDataListener.onLackData();
                    }
                }catch (Exception e) {
                    throw new IllegalStateException("this RecyclerView's LayoutManager isn't a LinearLayoutManager.");
                }
            }
            if (onScrollListener != null) {
                onScrollListener.onScrolled(recyclerView, dx, dy);
            }
        }
    };

    public void setOnLackDataListener(OnLackDataListener onLackDataListener) {
        this.onLackDataListener = onLackDataListener;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public interface OnLackDataListener{
        void onLackData();
        boolean isLoading();
        int minNumber();
    }
}

