package com.ekuaizhi.library.adapter;


/**
 * This class holds information about the position of an item in the {@link com.ekuaizhi.library.adapter.EasyAdapter}
 */
public class PositionInfo {

    private int mPosition;
    private boolean mFirst;
    private boolean mLast;

    /**
     * 无参构造函数
     */
    public PositionInfo() {

    }

    /**
     * 构造一个PositionInfo: position, first and last
     *
     * @param position item的位置
     * @param first    true 则为第一个位置
     * @param last     true 则为最后一个位置
     */

    public PositionInfo(int position, boolean first, boolean last) {
        setPosition(position);
        setFirst(first);
        setLast(last);
    }

    /**
     * @return item的position {@link com.ekuaizhi.library.adapter.EasyAdapter}
     */
    public int getPosition() {
        return mPosition;
    }

    /**
     * @return true 则为第一个位置 {@link com.ekuaizhi.library.adapter.EasyAdapter}
     */
    public boolean isFirst() {
        return mFirst;
    }

    /**
     * @return true 则为最后一个位置 {@link com.ekuaizhi.library.adapter.EasyAdapter}
     */
    public boolean isLast() {
        return mLast;
    }

    /**
     * 设置item的position
     *
     * @param position item的位置
     */
    public void setPosition(int position) {
        mPosition = position;
    }

    /**
     * 设置item是否为第一个位置
     *
     * @param first true 则为第一个位置
     */
    public void setFirst(boolean first) {
        mFirst = first;
    }

    /**
     * 设置item是否为最后一个位置
     *
     * @param last true 则为最后一个位置
     */
    public void setLast(boolean last) {
        mLast = last;
    }
}
