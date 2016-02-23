package com.livvy.amovie.model;

import android.os.Handler;
import android.util.Log;

import com.ekuaizhi.library.data.model.DataItem;
import com.ekuaizhi.library.data.model.DataResult;
import com.livvy.amovie.R;

/**
 * 电影相关模块
 *
 * ==获取电影列表(详情)
 * Created by livvy on 2/21/16.
 */
public class MovieModel {

    public DataResult getMovieDetailList(int size,int page){

        //模拟网络环境
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
        }


        DataResult result = new DataResult();
        DataItem item;
        int startPos = (page - 1) * size;

        result.hasError = false;
        result.totalcount = 16;

//        // 模拟构造网络加载的数据
//        for (int i = 0; i < 10; i++) {
//            int jobnum = startPos + i + 1;
//
//            item = new DataItem();
//            item.setString("movieTime","AAAA" + jobnum);
//            item.setString("movieName"," 西游记之大圣归来" + i);
//            item.setString("movieEnglishName","Monkey King: Hero is Back");
//            item.setInt("moviePhoto", R.mipmap.image_home_movie_test_01);
//            item.setString("movieImport","田晓鹏导演");
//            item.setString("movieForward","1314人期待");
//            result.addItem(item);
//        }

        DataItem dataItem = new DataItem();
        dataItem.setString("movieTime","AAAAAAA");
        dataItem.setString("movieName"," 西游记之大圣归来");
        dataItem.setString("movieEnglishName","Monkey King: Hero is Back");
        dataItem.setInt("moviePhoto", R.mipmap.image_home_movie_test_01);
        dataItem.setString("movieImport","田晓鹏导演");
        dataItem.setString("movieForward","1314人期待");
        result.addItem(dataItem);

        dataItem = new DataItem();
        dataItem.setString("movieTime","AAAAAAA");
        dataItem.setString("movieName"," 火影忍者剧场版:博人传");
        dataItem.setString("movieEnglishName","Boruto: Naruto the Movie");
        dataItem.setInt("moviePhoto", R.mipmap.image_home_movie_test_02);
        dataItem.setString("movieImport","山下宏幸导演");
        dataItem.setString("movieForward","2245人期待");
        result.addItem(dataItem);

        dataItem = new DataItem();
        dataItem.setString("movieTime","AAAAAAA");
        dataItem.setString("movieName"," 澳门风云3 賭城風雲III");
        dataItem.setString("movieEnglishName","From Vegas To Macau III");
        dataItem.setInt("moviePhoto", R.mipmap.image_home_movie_test_03);
        dataItem.setString("movieImport","刘伟强/王晶导演");
        dataItem.setString("movieForward","213人期待");
        result.addItem(dataItem);

        dataItem = new DataItem();
        dataItem.setString("movieTime","AAAAAAA");
        dataItem.setString("movieName"," 星球大战7：原力觉醒");
        dataItem.setString("movieEnglishName","Star Wars: The Force Awakens");
        dataItem.setInt("moviePhoto", R.mipmap.image_home_movie_test_04);
        dataItem.setString("movieImport","J·J·艾布拉姆斯导演");
        dataItem.setString("movieForward","1114人期待");
        result.addItem(dataItem);

        dataItem = new DataItem();
        dataItem.setString("movieTime","BBBBBBBB");
        dataItem.setString("movieName"," 西游记之大圣归来");
        dataItem.setString("movieEnglishName","Monkey King: Hero is Back");
        dataItem.setInt("moviePhoto", R.mipmap.image_home_movie_test_01);
        dataItem.setString("movieImport","田晓鹏导演");
        dataItem.setString("movieForward","1314人期待");
        result.addItem(dataItem);

        dataItem = new DataItem();
        dataItem.setString("movieTime","BBBBBBB");
        dataItem.setString("movieName"," 火影忍者剧场版:博人传");
        dataItem.setString("movieEnglishName","Boruto: Naruto the Movie");
        dataItem.setInt("moviePhoto", R.mipmap.image_home_movie_test_02);
        dataItem.setString("movieImport","山下宏幸导演");
        dataItem.setString("movieForward","2245人期待");
        result.addItem(dataItem);

        dataItem = new DataItem();
        dataItem.setString("movieTime","BBBBBBBB");
        dataItem.setString("movieName"," 澳门风云3 賭城風雲III");
        dataItem.setString("movieEnglishName","From Vegas To Macau III");
        dataItem.setInt("moviePhoto", R.mipmap.image_home_movie_test_03);
        dataItem.setString("movieImport","刘伟强/王晶导演");
        dataItem.setString("movieForward","213人期待");
        result.addItem(dataItem);

        dataItem = new DataItem();
        dataItem.setString("movieTime","BBBBBBBB");
        dataItem.setString("movieName"," 星球大战7：原力觉醒");
        dataItem.setString("movieEnglishName","Star Wars: The Force Awakens");
        dataItem.setInt("moviePhoto", R.mipmap.image_home_movie_test_04);
        dataItem.setString("movieImport","J·J·艾布拉姆斯导演");
        dataItem.setString("movieForward","1114人期待");
        result.addItem(dataItem);

        dataItem = new DataItem();
        dataItem.setString("movieTime","CCCCCCC");
        dataItem.setString("movieName"," 西游记之大圣归来");
        dataItem.setString("movieEnglishName","Monkey King: Hero is Back");
        dataItem.setInt("moviePhoto", R.mipmap.image_home_movie_test_01);
        dataItem.setString("movieImport","田晓鹏导演");
        dataItem.setString("movieForward","1314人期待");
        result.addItem(dataItem);

        dataItem = new DataItem();
        dataItem.setString("movieTime","CCCCCCC");
        dataItem.setString("movieName"," 火影忍者剧场版:博人传");
        dataItem.setString("movieEnglishName","Boruto: Naruto the Movie");
        dataItem.setInt("moviePhoto", R.mipmap.image_home_movie_test_02);
        dataItem.setString("movieImport","山下宏幸导演");
        dataItem.setString("movieForward","2245人期待");
        result.addItem(dataItem);

        dataItem = new DataItem();
        dataItem.setString("movieTime","CCCCCCC");
        dataItem.setString("movieName"," 澳门风云3 賭城風雲III");
        dataItem.setString("movieEnglishName","From Vegas To Macau III");
        dataItem.setInt("moviePhoto", R.mipmap.image_home_movie_test_03);
        dataItem.setString("movieImport","刘伟强/王晶导演");
        dataItem.setString("movieForward","213人期待");
        result.addItem(dataItem);

        dataItem = new DataItem();
        dataItem.setString("movieTime","CCCCCCC");
        dataItem.setString("movieName"," 星球大战7：原力觉醒");
        dataItem.setString("movieEnglishName","Star Wars: The Force Awakens");
        dataItem.setInt("moviePhoto", R.mipmap.image_home_movie_test_04);
        dataItem.setString("movieImport","J·J·艾布拉姆斯导演");
        dataItem.setString("movieForward","1114人期待");
        result.addItem(dataItem);

        dataItem = new DataItem();
        dataItem.setString("movieTime","DDDDDDD");
        dataItem.setString("movieName"," 西游记之大圣归来");
        dataItem.setString("movieEnglishName","Monkey King: Hero is Back");
        dataItem.setInt("moviePhoto", R.mipmap.image_home_movie_test_01);
        dataItem.setString("movieImport","田晓鹏导演");
        dataItem.setString("movieForward","1314人期待");
        result.addItem(dataItem);

        dataItem = new DataItem();
        dataItem.setString("movieTime","DDDDDDD");
        dataItem.setString("movieName"," 火影忍者剧场版:博人传");
        dataItem.setString("movieEnglishName","Boruto: Naruto the Movie");
        dataItem.setInt("moviePhoto", R.mipmap.image_home_movie_test_02);
        dataItem.setString("movieImport","山下宏幸导演");
        dataItem.setString("movieForward","2245人期待");
        result.addItem(dataItem);

        dataItem = new DataItem();
        dataItem.setString("movieTime","DDDDDDD");
        dataItem.setString("movieName"," 澳门风云3 賭城風雲III");
        dataItem.setString("movieEnglishName","From Vegas To Macau III");
        dataItem.setInt("moviePhoto", R.mipmap.image_home_movie_test_03);
        dataItem.setString("movieImport","刘伟强/王晶导演");
        dataItem.setString("movieForward","213人期待");
        result.addItem(dataItem);

        dataItem = new DataItem();
        dataItem.setString("movieTime","DDDDDDD");
        dataItem.setString("movieName"," 星球大战7：原力觉醒");
        dataItem.setString("movieEnglishName","Star Wars: The Force Awakens");
        dataItem.setInt("moviePhoto", R.mipmap.image_home_movie_test_04);
        dataItem.setString("movieImport","J·J·艾布拉姆斯导演");
        dataItem.setString("movieForward","1114人期待");
        result.addItem(dataItem);

        return result;
    }

}
