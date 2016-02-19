package com.ekuaizhi.library.adapter;


import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 延伸 {@link com.ekuaizhi.library.adapter.BaseEasyAdapter} 使用列表数据结构,并提供方法来设置一个新的项目列表或将它们添加到现有列表。
 * 你仅仅需要实现 {@link com.ekuaizhi.library.adapter.ItemViewHolder} 并将其传递到这个类的构造函数。
 * @param <T> Data type for items
 */
public class EasyAdapter<T> extends BaseEasyAdapter<T> {

    private List<T> mListItems;

    /**
     * 在上下文中的构造函数 {@link com.ekuaizhi.library.adapter.ItemViewHolder}
     *
     * @param context             有效的Context
     * @param itemViewHolderClass 你的 {@link com.ekuaizhi.library.adapter.ItemViewHolder} 实现的class
     * @param listItems           项目列表加载适配器
     */
    public EasyAdapter(Context context, Class<? extends ItemViewHolder> itemViewHolderClass, List<T> listItems) {
        super(context, itemViewHolderClass);
        setItems(listItems);
    }

    /**
     * 在上下文中的构造函数 {@link com.ekuaizhi.library.adapter.ItemViewHolder}
     *
     * @param context             有效的Context
     * @param itemViewHolderClass 你的 {@link com.ekuaizhi.library.adapter.ItemViewHolder} 实现的class
     */
    public EasyAdapter(Context context, Class<? extends ItemViewHolder> itemViewHolderClass) {
        super(context, itemViewHolderClass);
        mListItems = new ArrayList<T>();
    }

    /**
     * 在上下文中的构造函数 {@link com.ekuaizhi.library.adapter.ItemViewHolder} 加上一个listener
     *
     * @param context             有效的Context
     * @param itemViewHolderClass 你的 {@link com.ekuaizhi.library.adapter.ItemViewHolder} 实现的class
     * @param listItems           项目列表加载适配器
     * @param listener            一个通用的对象,可以通过 {@link ItemViewHolder} 调用
     *                            {@link ItemViewHolder#getListener()}, 这可以用于将一个侦听器传递给view holder,那么你可以进行一个回调。
     */
    public EasyAdapter(Context context, Class<? extends ItemViewHolder> itemViewHolderClass, List<T> listItems, Object listener) {
        super(context, itemViewHolderClass, listener);
        setItems(listItems);
    }

    /**
     * 在上下文中的构造函数 {@link com.ekuaizhi.library.adapter.ItemViewHolder} 加上一个listener
     *
     * @param context             有效的Context
     * @param itemViewHolderClass 你的 {@link com.ekuaizhi.library.adapter.ItemViewHolder} 实现的class
     * @param listener            一个通用的对象,可以通过 {@link ItemViewHolder} 调用
     *                            {@link ItemViewHolder#getListener()}, 这可以用于将一个侦听器传递给view holder,那么你可以进行一个回调。
     */
    public EasyAdapter(Context context, Class<? extends ItemViewHolder> itemViewHolderClass, Object listener) {
        super(context, itemViewHolderClass, listener);
        mListItems = new ArrayList<>();
    }

    /**
     * 设置一个新的list item和刷新{@code AdapterView} 被调用
     * {@code notifyDataSetChanged()}.
     * Use {@link #setItemsWithoutNotifying(List)}()} 如果你不希望去刷新
     * the {@code AdapterView} at this time.
     *
     * @param listItems 新的list item 去作为底层数据结构
     */
    public void setItems(List<T> listItems) {
        mListItems = listItems;
        notifyDataSetChanged();
    }

    /**
     * 设置一个新的list item
     *
     * @param listItems 新的list item 去作为底层数据结构
     */
    public void setItemsWithoutNotifying(List<T> listItems) {
        mListItems = listItems;
    }

    /**
     * 检索 {@code List} 的items.改变这个 {@code List} 在数据上的显示
     * the {@code AdapterView}.
     *
     * @return 底层数据 {@code List}
     */
    public List<T> getItems() {
        return mListItems;
    }

    /**
     * 添加一个item和refresh {@code AdapterView} by calling
     * {@code notifyDataSetChanged()}.
     *
     * @param item 添加item
     */
    public void addItem(T item) {
        mListItems.add(item);
        notifyDataSetChanged();
    }

    /**
     * 移除和刷新 {@code AdapterView} by calling
     * {@code notifyDataSetChanged()}.
     *
     * @param item 添加item
     * @return true则数据修改 false 则失败.
     */
    public boolean removeItem(T item) {
        if (mListItems.remove(item)) {
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    /**
     * 添加一个item和refresh  {@code AdapterView} by calling
     * {@code notifyDataSetChanged()}.
     *
     * @param items 添加的数据集
     * @return true则数据修改 false 则失败.
     */
    public boolean addItems(Collection<? extends T> items) {
        if (mListItems.addAll(items)) {
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    /**
     * 移除和刷新 {@code AdapterView} by calling
     * {@code notifyDataSetChanged()}.
     *
     * @param items {@code Collection} 添加的数据集
     * @return  true则数据修改 false 则失败.
     */
    public boolean removeItems(Collection<? extends T> items) {
        if (mListItems.removeAll(items)) {
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public T getItem(int position) {
        return mListItems.get(position);
    }

}
