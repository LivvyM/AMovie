package com.ekuaizhi.library.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 允许 {@link android.support.v7.widget.RecyclerView.Adapter} 的使用
 * {@link com.ekuaizhi.library.adapter.ItemViewHolder} in a {@link android.support.v7.widget.RecyclerView}.
 * 你应该扩展这个类如果你的适配器需要一个数据结构不同的列表或需要提供一些额外的功能来处理数据.
 * i.e 如果你需要一个方法来添加项列表的开头。如果不是简单地使用所提供的实现 {@link com.ekuaizhi.library.adapter.EasyRecyclerAdapter}
 *
 * @param <T> Data type for items
 */
public abstract class BaseEasyRecyclerAdapter<T> extends RecyclerView.Adapter<BaseEasyRecyclerAdapter.RecyclerViewHolder> {

    private Class mItemViewHolderClass;
    private LayoutInflater mInflater;
    private Integer mItemLayoutId;
    private Object mListener;

    /**
     * 在上下文中的构造函数 {@link com.ekuaizhi.library.adapter.ItemViewHolder}
     *
     * @param context             有效的Context
     * @param itemViewHolderClass 你的 {@link com.ekuaizhi.library.adapter.ItemViewHolder} 实现的class
     */
    public BaseEasyRecyclerAdapter(Context context, Class<? extends ItemViewHolder> itemViewHolderClass) {
        init(context, itemViewHolderClass);
    }

    /**
     * 在上下文中的构造函数 {@link com.ekuaizhi.library.adapter.ItemViewHolder} 加上一个listener
     *
     * @param context             有效的Context
     * @param itemViewHolderClass 你的 {@link com.ekuaizhi.library.adapter.ItemViewHolder} 实现的class
     * @param listener            一个通用的对象,可以通过 {@link ItemViewHolder} 调用
     *                            {@link ItemViewHolder#getListener()}, 这可以用于将一个侦听器传递给view holder,那么你可以进行一个回调。
     */
    public BaseEasyRecyclerAdapter(Context context, Class<? extends ItemViewHolder> itemViewHolderClass, Object listener) {
        init(context, itemViewHolderClass);
        mListener = listener;
    }

    private void init(Context context, Class<? extends ItemViewHolder> itemViewHolderClass) {
        mItemViewHolderClass = itemViewHolderClass;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItemLayoutId = EasyAdapterUtil.parseItemLayoutId(itemViewHolderClass);
    }

    public abstract T getItem(int position);

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = mInflater.inflate(mItemLayoutId, parent, false);
        ItemViewHolder<T> itemViewHolder = EasyAdapterUtil.createViewHolder(itemView, mItemViewHolderClass);
        itemViewHolder.setListener(mListener);
        itemViewHolder.onSetListeners();
        return new RecyclerViewHolder(itemViewHolder);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder recyclerViewHolder, int position) {
        T item = getItem(position);
        ItemViewHolder<T> itemViewHolder = recyclerViewHolder.itemViewHolder;
        PositionInfo positionInfo = new PositionInfo(position, position == 0, position == getItemCount() - 1);
        itemViewHolder.setItem(item);
        itemViewHolder.onSetValues(item, positionInfo);
    }

    // A RecyclerView.ViewHolder that wraps an ItemViewHolder
    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ItemViewHolder itemViewHolder;

        public RecyclerViewHolder(ItemViewHolder itemViewHolder) {
            super(itemViewHolder.getView());
            this.itemViewHolder = itemViewHolder;
        }
    }
}
