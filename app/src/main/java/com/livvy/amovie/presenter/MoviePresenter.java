package com.livvy.amovie.presenter;

import com.ekuaizhi.library.data.model.DataResult;
import com.livvy.amovie.model.MovieModel;

/**
 *
 * Created by livvy on 2/21/16.
 */
public class MoviePresenter {

    private MovieModel movieModel;

    public MoviePresenter(){
        movieModel = new MovieModel();
    }

    public DataResult getMovieDetailList(int size,int page){
        return movieModel.getMovieDetailList(size,page);
    }
}
