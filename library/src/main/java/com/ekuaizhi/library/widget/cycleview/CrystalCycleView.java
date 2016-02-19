package com.ekuaizhi.library.widget.cycleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.ekuaizhi.library.R;
import com.ekuaizhi.library.widget.cycleview.adapter.CrystalPageAdapter;
import com.ekuaizhi.library.widget.cycleview.adapter.CrystalViewHolderCreator;
import com.ekuaizhi.library.widget.cycleview.listener.CrystalPageChangeListener;
import com.ekuaizhi.library.widget.cycleview.listener.OnItemClickListener;
import com.ekuaizhi.library.widget.cycleview.view.CrystalLoopViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 页面翻转控件，极方便的广告栏
 * 支持无限循环，自动翻页，翻页特效
 * @author Sai 支持自动翻页
 */
public class CrystalCycleView<T> extends LinearLayout {
    private List<T> mDatas;
    private int[] page_indicatorId;
    private ArrayList<ImageView> mPointViews = new ArrayList<ImageView>();
    private CrystalPageChangeListener pageChangeListener;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private OnItemClickListener listener;
    private CrystalLoopViewPager viewPager;
    private ViewPagerScroller scroller;
    private ViewGroup loPageTurningPoint;
    private long autoTurningTime;
    private boolean turning;
    private boolean canTurn = false;//自动翻页控制
    private boolean manualPageable = true;//手动滑动控制
    private boolean canLoop = true;
    public enum PageIndicatorAlign{
        ALIGN_PARENT_LEFT,ALIGN_PARENT_RIGHT,CENTER_HORIZONTAL
    }
    public static int SCROLLDURATIONDEFAULT = 800;// 滑动速度,值越大滑动越慢，滑动太快会使3d效果不明显

    public enum Transformer {
        DefaultTransformer("DefaultTransformer"), AccordionTransformer(
                "AccordionTransformer"), BackgroundToForegroundTransformer(
                "BackgroundToForegroundTransformer"), CubeInTransformer(
                "CubeInTransformer"), CubeOutTransformer(
                "CubeOutTransformer"), DepthPageTransformer(
                "DepthPageTransformer"), FlipHorizontalTransformer(
                "FlipHorizontalTransformer"), FlipVerticalTransformer(
                "FlipVerticalTransformer"), ForegroundToBackgroundTransformer(
                "ForegroundToBackgroundTransformer"), RotateDownTransformer(
                "RotateDownTransformer"), RotateUpTransformer(
                "RotateUpTransformer"), StackTransformer(
                "StackTransformer"), TabletTransformer(
                "TabletTransformer"), ZoomInTransformer(
                "ZoomInTransformer"), ZoomOutSlideTransformer(
                "ZoomOutSlideTransformer"), ZoomOutTranformer(
                "ZoomOutTranformer");

        private final String className;

        // 构造器默认也只能是private, 从而保证构造函数只能在内部使用
        Transformer(String className) {
            this.className = className;
        }

        public String getClassName() {
            return className;
        }
    }

    private Handler timeHandler = new Handler();
    private Runnable adSwitchTask = new Runnable() {
        @Override
        public void run() {
            if (viewPager != null && turning) {
                int page = viewPager.getCurrentItem() + 1;
                if(page >= viewPager.getAdapter().getCount()){
                    page = viewPager.getFristItem();
                    viewPager.setCurrentItem(page,false);
                }
                else {
                    viewPager.setCurrentItem(page, true);
                }
                timeHandler.postDelayed(adSwitchTask, autoTurningTime);
            }
        }
    };

    public CrystalCycleView(Context context) {
        this(context, null);
    }
    public CrystalCycleView(Context context, boolean canLoop) {
        this(context, null);
        this.canLoop = canLoop;
    }
    public CrystalCycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.CrystalCycleView);
        canLoop = a.getBoolean(R.styleable.CrystalCycleView_canLoop,true);
        init(context);
    }

    private void init(Context context) {
        View hView = LayoutInflater.from(context).inflate(
                R.layout.include_viewpager, this, true);
        viewPager = (CrystalLoopViewPager) hView.findViewById(R.id.cbLoopViewPager);
        loPageTurningPoint = (ViewGroup) hView.findViewById(R.id.loPageTurningPoint);
        initViewPagerScroll();
    }

    public CrystalCycleView setPages(CrystalViewHolderCreator holderCreator, List<T> datas){
        this.mDatas = datas;
        viewPager.setCanLoop(canLoop);
        viewPager.setAdapter(new CrystalPageAdapter(holderCreator,mDatas));

        if (page_indicatorId != null)
            setPageIndicator(page_indicatorId);
        return this;
    }

    /**
     * 通知数据变化
     */
    public void notifyDataSetChanged(){
        viewPager.getAdapter().notifyDataSetChanged();
        if (page_indicatorId != null)
            setPageIndicator(page_indicatorId);
    }

    /**
     * 设置底部指示器是否可见
     */
    public CrystalCycleView setPointViewVisible(boolean visible) {
        loPageTurningPoint.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 底部指示器资源图片
     *
     */
    public CrystalCycleView setPageIndicator(int[] page_indicatorId) {
        loPageTurningPoint.removeAllViews();
        mPointViews.clear();
        this.page_indicatorId = page_indicatorId;
        if(mDatas==null)return this;
        for (int count = 0; count < mDatas.size(); count++) {
            // 翻页指示的点
            ImageView pointView = new ImageView(getContext());
            pointView.setPadding(5, 0, 5, 0);
            if (mPointViews.isEmpty())
                pointView.setImageResource(page_indicatorId[1]);
            else
                pointView.setImageResource(page_indicatorId[0]);
            mPointViews.add(pointView);
            loPageTurningPoint.addView(pointView);
        }
        pageChangeListener = new CrystalPageChangeListener(mPointViews,
                page_indicatorId);
        viewPager.setOnPageChangeListener(pageChangeListener);
        pageChangeListener.onPageSelected(viewPager.getRealItem());
        if(onPageChangeListener != null)pageChangeListener.setOnPageChangeListener(onPageChangeListener);

        return this;
    }

    /**
     * 指示器的方向
     * @param align  三个方向：居左 （RelativeLayout.ALIGN_PARENT_LEFT），居中 （RelativeLayout.CENTER_HORIZONTAL），居右 （RelativeLayout.ALIGN_PARENT_RIGHT）
     */
    public CrystalCycleView setPageIndicatorAlign(PageIndicatorAlign align) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) loPageTurningPoint.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, align == PageIndicatorAlign.ALIGN_PARENT_LEFT ? RelativeLayout.TRUE : 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, align == PageIndicatorAlign.ALIGN_PARENT_RIGHT ? RelativeLayout.TRUE : 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, align == PageIndicatorAlign.CENTER_HORIZONTAL ? RelativeLayout.TRUE : 0);
        loPageTurningPoint.setLayoutParams(layoutParams);
        return this;
    }

    /***
     * 是否开启了翻页
     */
    public boolean isTurning() {
        return turning;
    }

    /***
     * 开始翻页
     * @param autoTurningTime 自动翻页时间
     */
    public CrystalCycleView startTurning(long autoTurningTime) {
        //如果是正在翻页的话先停掉
        if(turning){
            stopTurning();
        }
        //设置可以翻页并开启翻页
        canTurn = true;
        this.autoTurningTime = autoTurningTime;
        turning = true;
        timeHandler.postDelayed(adSwitchTask, autoTurningTime);
        return this;
    }

    public void stopTurning() {
        turning = false;
        timeHandler.removeCallbacks(adSwitchTask);
    }

    /**
     * 自定义翻页动画效果
     *
     */
    public CrystalCycleView setPageTransformer(PageTransformer transformer) {
        viewPager.setPageTransformer(true, transformer);
        return this;
    }

    /**
     * 自定义翻页动画效果，使用已存在的效果
     *
     */
    public CrystalCycleView setPageTransformer(Transformer transformer) {
        try {
            String pkName = getClass().getPackage().getName();
            viewPager.setPageTransformer(true, (PageTransformer) Class.forName(pkName +
                    ".transforms." + transformer.getClassName()).newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 设置ViewPager的滑动速度
     * */
    private void initViewPagerScroll() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            scroller = new ViewPagerScroller(
                    viewPager.getContext());
//			scroller.setScrollDuration(1500);
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public boolean isManualPageable() {
        return viewPager.isCanScroll();
    }

    public void setManualPageable(boolean manualPageable) {
        viewPager.setCanScroll(manualPageable);
    }

    //触碰控件的时候，翻页应该停止，离开的时候如果之前是开启了翻页的话则重新启动翻页
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP||action == MotionEvent.ACTION_CANCEL||action == MotionEvent.ACTION_OUTSIDE) {
            // 开始翻页
            if (canTurn)startTurning(autoTurningTime);
        } else if (action == MotionEvent.ACTION_DOWN) {
            // 停止翻页
            if (canTurn)stopTurning();
        }
        return super.dispatchTouchEvent(ev);
    }

    //获取当前的页面index
    public int getCurrentItem(){
        if (viewPager!=null) {
            return viewPager.getRealItem();
        }
        return -1;
    }
    //设置当前的页面index
    public void setcurrentitem(int index){
        if (viewPager!=null) {
            viewPager.setCurrentItem(index);
        }
    }

    public ViewPager.OnPageChangeListener getOnPageChangeListener() {
        return onPageChangeListener;
    }

    /**
     * 设置翻页监听器
     * @param onPageChangeListener
     * @return
     */
    public CrystalCycleView setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
        //如果有默认的监听器（即是使用了默认的翻页指示器）则把用户设置的依附到默认的上面，否则就直接设置
        if(pageChangeListener != null)pageChangeListener.setOnPageChangeListener(onPageChangeListener);
        else viewPager.setOnPageChangeListener(onPageChangeListener);
        return this;
    }



    public boolean isCanLoop() {
        return viewPager.isCanLoop();
    }

    /**
     * 监听item点击
     * @param onItemClickListener
     */
    public CrystalCycleView setOnItemClickListener(OnItemClickListener onItemClickListener) {
        if (onItemClickListener == null) {
            viewPager.setOnClickListener(null);
            return this;
        }
        this.listener = onItemClickListener;
        viewPager.getAdapter().setOnItemClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(getCurrentItem());
            }
        });
        return this;
    }

    /**
     * 设置ViewPager的滚动速度
     * @param scrollDuration
     */
    public void setScrollDuration(int scrollDuration){
        scroller.setScrollDuration(scrollDuration);
    }

    public int getScrollDuration() {
        return scroller.getScrollDuration();
    }
}
