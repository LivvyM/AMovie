package com.ekuaizhi.library.adapter.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by livvym on 15-12-21.
 *类注释用于布局Id和一个链接{@link com.lm.library.lmadapter.ItemViewHolder}
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LayoutId {
    int value();
}
