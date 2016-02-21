package com.livvy.amovie.activity;

import android.os.Bundle;

import com.ekuaizhi.library.widget.list.DataExpandListView;
import com.livvy.amovie.R;
import com.livvy.amovie.activity.base.AppBaseActivity;
import com.livvy.amovie.cell.MovieDetailCell;
import com.livvy.amovie.presenter.MoviePresenter;

/**
 * 主页面,主要用于展示电影时间表(分为多种浏览方式)
 * 1.默认浏览方式
 *
 * Created by livvy on 2/19/16.
 */
public class MainActivity extends AppBaseActivity {

    protected DataExpandListView mExpandListMovies; //首页电影列表-detail

    private MoviePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MoviePresenter();

        initView();

    }

    private void initView(){
        mExpandListMovies = (DataExpandListView)findViewById(R.id.mExpandListMovies);

        mExpandListMovies.setDataCellClass(MovieDetailCell.class);
        mExpandListMovies.setDataLoader((adapter, pageAt, pageSize) -> {
            return presenter.getMovieDetailList();
        },true);
    }

}
