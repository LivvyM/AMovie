/*
 * Copyright (C) 2014 Ribot Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ekuaizhi.library.adapter;

import android.app.Activity;

import com.ekuaizhi.library.adapter.annotations.FieldAnnotationParser;


/**
 * 如果你想要拓宽activity的view
 * 注释字段的视图 {@link com.ekuaizhi.library.adapter.annotations.ViewId} i.e @ViewId(R.id.textView1).
 * 它将寻找viewId的activity的布局和分配子类的带注释的字段的视图。
 * </p>
 * 你必须 call {@link Activity#setContentView(int)} 在实例化之前的活动ActivityViewHolder的任何子类。
 */
public abstract class ActivityViewHolder {

    private Activity mActivity;

    /**
     * 构造一个ActivityViewHolder使用一个activity
     *
     * @param activity 活动
     */
    public ActivityViewHolder(Activity activity) {
        mActivity = activity;
        FieldAnnotationParser.setViewFields(this, activity);
    }

    /**
     * 被用来构造的activity {@link com.ekuaizhi.library.adapter.ActivityViewHolder}
     *
     * @return 活动的activity {@link #ActivityViewHolder(Activity)}
     */
    public Activity getActivity() {
        return mActivity;
    }

}
