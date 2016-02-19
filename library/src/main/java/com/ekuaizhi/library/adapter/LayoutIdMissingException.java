package com.ekuaizhi.library.adapter;

/**
 * Created by livvym on 15-12-21.
 * This RuntimeException is thrown when an extension of {@link com.ekuaizhi.library.adapter.ItemViewHolder}
 * has not been annotated with {@link com.ekuaizhi.library.adapter.annotations.LayoutId}
 */
public class LayoutIdMissingException extends RuntimeException {

    public LayoutIdMissingException() {
        super("ItemViewHolder children classes must be annotated with a layout id, please add @LayoutId(someLayoutId) ");
    }

}

