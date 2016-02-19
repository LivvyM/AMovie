package com.ekuaizhi.library.widget.cycleview.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.ekuaizhi.library.widget.cycleview.adapter.CrystalPageAdapter;


/**
 * Created by Sai on 15/12/5.
 */
public class CrystalLoopViewPager extends ViewPager {

    OnPageChangeListener mOuterPageChangeListener;
    private CrystalPageAdapter mAdapter;

    private boolean isCanScroll = true;
    private int START_POSITION_MULTIPLE = 100;
    private boolean canLoop = true;

    @Override
    public void setAdapter(PagerAdapter adapter) {
        mAdapter = (CrystalPageAdapter) adapter;
        mAdapter.setCanLoop(canLoop);
        super.setAdapter(mAdapter);

        setCurrentItem(getFristItem(), false);
    }

    public int getFristItem(){
        return canLoop ? mAdapter.getRealCount() * START_POSITION_MULTIPLE : 0;
    }

    public boolean isCanScroll() {
        return isCanScroll;
    }

    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanScroll)
            return super.onTouchEvent(ev);
        else
            return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isCanScroll)
            return super.onInterceptTouchEvent(ev);
        else
            return false;
    }

    public CrystalPageAdapter getAdapter() {
        return mAdapter;
    }

    public int getRealItem() {
        return mAdapter != null ? mAdapter.toRealPosition(super.getCurrentItem()) : 0;
    }


    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item + mAdapter.getRealCount() * (canLoop ? START_POSITION_MULTIPLE : 1));
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mOuterPageChangeListener = listener;
    }


    public CrystalLoopViewPager(Context context) {
        super(context);
        init();
    }

    public CrystalLoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        super.setOnPageChangeListener(onPageChangeListener);
    }

    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        private float mPreviousPosition = -1;

        @Override
        public void onPageSelected(int position) {
            int realPosition = mAdapter.toRealPosition(position);
            if (mPreviousPosition != realPosition) {
                mPreviousPosition = realPosition;
                if (mOuterPageChangeListener != null) {
                    mOuterPageChangeListener.onPageSelected(realPosition);
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            int realPosition = position;

            if (mOuterPageChangeListener != null) {
                if (realPosition != mAdapter.getRealCount() - 1) {
                    mOuterPageChangeListener.onPageScrolled(realPosition,
                            positionOffset, positionOffsetPixels);
                } else {
                    if (positionOffset > .5) {
                        mOuterPageChangeListener.onPageScrolled(0, 0, 0);
                    } else {
                        mOuterPageChangeListener.onPageScrolled(realPosition,
                                0, 0);
                    }
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mOuterPageChangeListener != null) {
                mOuterPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    };

    public boolean isCanLoop() {
        return canLoop;
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
    }

}
