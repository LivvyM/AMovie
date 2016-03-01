package com.livvy.amovie.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by livvym on 16-2-19.
 */
public abstract class AppBaseParamActivity extends AppBaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent() != null && getIntent().getExtras() != null){
            onInitParams(getIntent().getExtras());
        }else{
            onInitParams(new Bundle());
        }
        // 让子类负责界面的初始化
        setupViews(savedInstanceState);
    }

    protected abstract void onInitParams(Bundle bundle);

    /**
     * 此抽象方法约束子类完成视图初始化工作，其实该方法会在onCreate方法里调用，这样写可以把参数初始化和界面初始化分开
     *
     * @param savedInstanceState onCreate的传入参数，存放界面从被回收转态恢复时的数据；若当前界面不是从回收状态恢复的，此参数的值为null
     */
    protected abstract void setupViews(Bundle savedInstanceState);


}
