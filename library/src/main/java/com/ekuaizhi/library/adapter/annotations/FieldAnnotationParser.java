package com.ekuaizhi.library.adapter.annotations;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;

public class FieldAnnotationParser {

    /**
     * 解析{ @link ViewId }注释和试图分配视图id的注释字段。
     * 它将抛出一个{ @link ClassCastException }如果给定ID的字段和视图有不同的类型。
     *
     * @param object 对象的注释。
     * @param view   父视图包含一个视图viewId给定的注释。
     */
    public static void setViewFields(final Object object, final View view) {
        setViewFields(object, new ViewFinder() {
            @Override
            public View findViewById(int viewId) {
                return view.findViewById(viewId);
            }
        });
    }

    /**
     * 解析{ @link ViewId }注释和试图分配视图id的注释字段。
     * 它将抛出一个{ @link ClassCastException }如果给定ID的字段和视图有不同的类型.
     *
     * @param object   对象的注释。
     * @param activity 其中包含一个视图中给出的viewId注释。
     */
    public static void setViewFields(final Object object, final Activity activity) {
        setViewFields(object, new ViewFinder() {
            @Override
            public View findViewById(int viewId) {
                return activity.findViewById(viewId);
            }
        });
    }

    /**
     * 解析{ @link ViewId }注释和试图分配视图id的注释字段。
     * 它将抛出一个{ @link ClassCastException }如果给定ID的字段和视图有不同的类型.
     *
     * @param object     对象的注释。
     * @param viewFinder 回调,提供了一种方法找到视图中给定的viewID注释。
     */
    private static void setViewFields(final Object object, final ViewFinder viewFinder) {
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(ViewId.class)) {
                field.setAccessible(true);
                ViewId viewIdAnnotation = field.getAnnotation(ViewId.class);
                try {
                    field.set(object, field.getType().cast(viewFinder.findViewById(viewIdAnnotation.value())));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private interface ViewFinder {
         View findViewById(int viewId);
    }
}
