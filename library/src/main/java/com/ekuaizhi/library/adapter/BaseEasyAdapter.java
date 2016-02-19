package com.ekuaizhi.library.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 *你应该扩展这个类如果你的适配器需要一个数据结构不同的列表或需要提供一些额外的功能来处理数据.
 *  i.e 如果你需要一个方法来添加项列表的开头。如果不是简单地使用所提供的实现 {@link com.ekuaizhi.library.adapter.EasyAdapter}
 * @param <T> Data type for items
 */
public abstract class BaseEasyAdapter<T> extends BaseAdapter {

    private Class<? extends ItemViewHolder> mItemViewHolderClass;
    private LayoutInflater mInflater;
    private Integer mItemLayoutId;
    private Object mListener;

    /**
     * 在上下文中的构造函数 {@link com.ekuaizhi.library.adapter.ItemViewHolder}
     *
     * @param context              有效的Context
     * @param itemViewHolderClass  你的 {@link com.ekuaizhi.library.adapter.ItemViewHolder} 实现的class
     */
    public BaseEasyAdapter(Context context, Class<? extends ItemViewHolder> itemViewHolderClass) {
        init(context, itemViewHolderClass);
    }

    /**
     * 在上下文中的构造函数 {@link com.ekuaizhi.library.adapter.ItemViewHolder} 加上一个listener
     *
     * @param context             有效的Context
     * @param itemViewHolderClass 你的 {@link com.ekuaizhi.library.adapter.ItemViewHolder} 实现的class
     * @param listener            一个通用的对象,可以通过 {@link ItemViewHolder} 调用
     *                            {@link ItemViewHolder#getListener()}, 这可以用于将一个侦听器传递给view holder,那么你可以进行一个回调。
     *
     */
    public BaseEasyAdapter(Context context, Class<? extends ItemViewHolder> itemViewHolderClass, Object listener) {
        init(context, itemViewHolderClass);
        mListener = listener;
    }

    private void init(Context context, Class<? extends ItemViewHolder> itemViewHolderClass) {
        mItemViewHolderClass = itemViewHolderClass;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItemLayoutId = EasyAdapterUtil.parseItemLayoutId(itemViewHolderClass);
    }

    @Override
    public abstract T getItem(int position);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewHolder<T> holder;
        if (convertView == null) {
            convertView = mInflater.inflate(mItemLayoutId, parent, false);
            //Create a new view holder using reflection
            holder = EasyAdapterUtil.createViewHolder(convertView, mItemViewHolderClass);
            holder.setListener(mListener);
            holder.onSetListeners();
            if (convertView != null) convertView.setTag(holder);
        } else {
            //Reuse the view holder
            holder = (ItemViewHolder) convertView.getTag();
        }

        T item = getItem(position);
        holder.setItem(item);
        PositionInfo positionInfo = new PositionInfo(position, position == 0, position == getCount() - 1);
        holder.onSetValues(item, positionInfo);

        return convertView;
    }

}
