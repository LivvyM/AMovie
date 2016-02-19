package com.ekuaizhi.library.adapter.annotations;

import java.lang.annotation.Annotation;

public class ClassAnnotationParser {

    /**
     * Parse {@link LayoutId} annotation form a class
     *
     * @param myClass 注释类
     * @return 注释的整数值
     */
    public static Integer getLayoutId(Class myClass) {
        Annotation annotation = myClass.getAnnotation(LayoutId.class);

        if (annotation instanceof LayoutId) {
            LayoutId layoutIdAnnotation = (LayoutId) annotation;
            return layoutIdAnnotation.value();
        }

        return null;
    }
}
