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
    }

    protected abstract void onInitParams(Bundle bundle);


}
