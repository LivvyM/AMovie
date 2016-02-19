package com.ekuaizhi.library.adapter;

/**
 * Created by livvym on 15-12-21.
 *
 */

import android.support.annotation.Nullable;
import android.view.View;

/**
 * 选择更容易实现自己的{@link android.widget.BaseAdapter}。扩展这个类的下面解释,然后使用{@link android.widget.AdapterView}
 * 链接到你的{@link android.widget.AdapterView }
 * <ol>
 *     <li>注释子类使用{@link com.ekuaizhi.library.adapter.annotations.LayoutId },i.e @LayoutId(R.id.item_layout)。它将链接ItemViewHolder项目的布局。</li>
 *     <li>注释视图字段{@link com.ekuaizhi.library.adapter.annotations.ViewId },i.e @ViewId(R.id.textView1)。它将寻找viewId的项目的布局和分配子类的带注释的字段的视图。</li>
 *     <li>Implement {@link #onSetValues(Object, PositionInfo)} 填充视图使用项目中的数据对象</li>
 *     <li>可选, implement {@link #onSetListeners()}  在view上添加 listeners.</li>
 * </ol>
 */
public abstract class ItemViewHolder<T> extends ViewHolder {

    private T mItem;
    private Object mListener;

    /**
     * 构造一个视图
     *
     * @param view the parent view where the held views reside.
     */
    public ItemViewHolder(View view) {
        super(view);
    }

    /**
     * Constructs an item view holder with the item view and the item data
     *
     * @param view the parent view where the held views reside.
     * @param item the data item that is used to populate the held views.
     */
    public ItemViewHolder(View view, T item) {
        super(view);
        setItem(item);
    }

    /**
     * Gets the item data
     *
     * @return the data item that is used to populate the held views.
     */
    public T getItem() {
        return mItem;
    }

    /**
     * Sets the item data
     *
     * @param item the data item that is used to populate the held views.
     */
    public void setItem(T item) {
        mItem = item;
    }

    /**
     * Must implement this method to populate the views with the data in the item object.
     *
     * @param item         the data item that is used to populate the held views.
     * @param positionInfo information about the position of the item on the list.
     */
    public abstract void onSetValues(T item, PositionInfo positionInfo);

    /**
     * Implement this method to add listeners to the views. This method is only called once when
     * the Adapter is created. Note that at this point calling {@link #getItem()} will return null, however
     * you can still call {@link #getItem()} from inside your listener object implementation, e.g from inside onClick().
     */
    public void onSetListeners() {
    }

    /**
     * Gets the listener object that was passed into the Adapter through its constructor.
     *
     * @return a generic listener object that can be casted and used as a callback
     * or null if not listener was set into the Adapter.
     */
    @Nullable
    public Object getListener() {
        return mListener;
    }

    /**
     * Gets the listener object that was passed into the Adapter through its constructor and cast
     * it to a given type.
     *
     * @param type the type of the listener
     * @return the listener casted to the given type or null if not listener was set into the Adapter.
     */
    @Nullable
    public <P> P getListener(Class<P> type) {
        if (mListener != null) {
            return type.cast(mListener);
        }
        return null;
    }

    protected void setListener(Object listener) {
        mListener = listener;
    }

}
