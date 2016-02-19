package com.livvy.amovie.activity;

import android.os.Bundle;

import com.livvy.amovie.R;
import com.livvy.amovie.activity.base.AppBaseActivity;

public class MainActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
