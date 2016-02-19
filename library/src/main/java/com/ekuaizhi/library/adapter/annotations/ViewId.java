package com.ekuaizhi.library.adapter.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by livvyM on 15-12-21.
 * 字段注释用来连接一个视图与一个视图Id字段
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewId {
    int value();
}
