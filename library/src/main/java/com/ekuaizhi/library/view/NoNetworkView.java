package com.ekuaizhi.library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 *
 * 当没有网络的情况家显示的View
 * 直接
 * Created by livvym on 16-1-29.
 */
public class NoNetworkView extends RelativeLayout{

    protected int mDrawable = 0;
    protected Button mButton;

    public NoNetworkView(Context context) {
        this(context, null);
    }

    public NoNetworkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        draw();
    }

    private void draw(){

        //父layout
        LinearLayout mParentLayout = new LinearLayout(getContext());
        mParentLayout.setBackgroundColor(0xFFE0E0E0);
        mParentLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams mParentLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
//        mParentLayoutParams.setMargins(0,Unit.dp2px(getContext(),48),0,0);
        mParentLayout.setLayoutParams(mParentLayoutParams);

        //添加图片
        ImageView mImageView = new ImageView(getContext());
//        mImageView.setLayoutParams(new LinearLayout.LayoutParams(Unit.dp2px(getContext(),60),Unit.dp2px(getContext(),60)));
        //显示的图片--没有网络的情况
        if(mDrawable != 0){
            mImageView.setBackgroundResource(mDrawable);
        }
        mParentLayout.addView(mImageView);
        //添加文字
        TextView mTextView = new TextView(getContext());
        mTextView.setText("暂时没有网络，请点击重新尝试");
        mParentLayout.addView(mTextView);
        //添加重试按钮
        mButton = new Button(getContext());
        mButton.setText("重试");
        mParentLayout.addView(mButton);
        //父layout居中设置
        RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(mParentLayout,mLayoutParams);
    }

    public void setRefreshCLickListener(OnClickListener listener){
        if(null == mButton){
            return;
        }
        mButton.setOnClickListener(listener);
    }
}
