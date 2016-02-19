package com.ekuaizhi.library.log;

/**
 * Created by livvym on 15-12-22.
 */
public interface LogTool {
    void d(String tag, String message);

    void e(String tag, String message);

    void w(String tag, String message);

    void i(String tag, String message);

    void v(String tag, String message);

    void wtf(String tag, String message);
}
