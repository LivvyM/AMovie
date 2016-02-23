package com.livvy.amovie.cell;

import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ekuaizhi.library.data.model.DataItem;
import com.ekuaizhi.library.widget.repeater.DataCell;
import com.ekuaizhi.library.widget.repeater.DataExpandCell;
import com.livvy.amovie.R;
import com.livvy.amovie.app.AppClient;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by livvy on 2/20/16.
 */
public class MovieDetailCell extends DataExpandCell{

    /**
     * ExpandView
     */
    protected View mViewExpandPoint;
    protected ImageView mViewExpandLineBottom;
    protected ImageView mViewExpandLineTop;
    protected TextView mTextExpandTime;

    /**
     * BodyView
     */
    protected ImageView mImageItemPhoto;
    protected TextView mTextItemName;
    protected TextView mTextItemEnglishName;
    protected TextView mTextItemImport;
    protected TextView mTextItemForward;

    /**
     * expand数据
     */
    private List<DataItem> mDatas = new ArrayList<>();
    private List<Integer> mSectionIndices = new ArrayList<>();
    private List<String> mSectionLetters = new ArrayList<>();

    @Override
    public int getCellViewLayoutID() {
        return R.layout.item_expand_home_body;
    }

    @Override
    public int getExpandCellViewLayoutID() {
        return R.layout.item_expand_home_header;
    }


    @Override
    public void bindExpandView() {
        mViewExpandPoint      = findViewById(getExpandView(),R.id.mViewExpandPoint);
        mViewExpandLineBottom = (ImageView)findViewById(getExpandView(),R.id.mViewExpandLineBottom);
        mViewExpandLineTop    = (ImageView)findViewById(getExpandView(),R.id.mViewExpandLineTop);
        mTextExpandTime       = (TextView)findViewById(getExpandView(),R.id.mTextExpandTime);
    }

    @Override
    public void bindView() {
        mImageItemPhoto     = (ImageView)findViewById(getCellView(),R.id.mImageItemPhoto);
        mTextItemName        = (TextView)findViewById(getCellView(),R.id.mTextItemName);
        mTextItemEnglishName = (TextView)findViewById(getCellView(),R.id.mTextItemEnglishName);
        mTextItemImport      = (TextView)findViewById(getCellView(),R.id.mTextItemImport);
        mTextItemForward     = (TextView)findViewById(getCellView(),R.id.mTextItemForward);
    }

    @Override
    public void bindExpandData() {
        try{
            mTextExpandTime.setText(mDatas.get(mPosition).getString("movieTime"));
        }catch (Throwable throwable){
        }

    }

    @Override
    public void bindData() {
        mDatas.clear();
        mSectionIndices.clear();
        mSectionLetters.clear();
        mDatas = mAdapter.getDataList().items.<DataItem>getItems();
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();

//        Glide.with(AppClient.getAppContext()).load(mDetail.getString("moviePhoto"))
//                .placeholder(R.mipmap.image_home_movie_test_01)
//                .crossFade()
//                .into(mImageItemPhoto);

        mImageItemPhoto.setBackgroundResource(mDatas.get(mPosition).getInt("moviePhoto"));
        mTextItemName.setText(mDatas.get(mPosition).getString("movieName"));
        mTextItemEnglishName.setText(mDatas.get(mPosition).getString("movieEnglishName"));
        mTextItemImport.setText(mDatas.get(mPosition).getString("movieImport"));
        mTextItemForward.setText(mDatas.get(mPosition).getString("movieForward"));
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        if(mSectionIndices.size() > sectionIndex){
            return mSectionIndices.get(sectionIndex);
        }
        return mSectionIndices.get(mSectionIndices.size() - 1);
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.size(); i++) {
            if (position < mSectionIndices.get(i)) {
                return i - 1;
            }
        }
        return mSectionIndices.size() - 1;
    }

    @Override
    public Object[] getSections() {
        String[] letter = new String[mSectionLetters.size()];
        for (int i = 0;i < mSectionLetters.size();i++){
            letter[i] = mSectionLetters.get(i);
        }
        return letter;
    }

    @Override
    public long getHeaderId(int position) {
        return mDatas.get(position).getString("movieTime").charAt(0);
    }

    private List<Integer> getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        String lastFirstChar = mDatas.get(0).getString("movieTime");
        sectionIndices.add(0);
        for (int i = 1; i < mDatas.size(); i++) {
            if (!lastFirstChar.equals(mDatas.get(i).getString("movieTime"))) {
                lastFirstChar = mDatas.get(i).getString("movieTime");
                sectionIndices.add(i);
            }
        }
        return sectionIndices;
    }

    private List<String> getSectionLetters() {
        List<String> letter = new ArrayList<>();
        for (int i = 0; i < mSectionIndices.size(); i++) {
            letter.add(mDatas.get(mSectionIndices.get(i)).getString("movieTime").substring(0,1));
        }
        return letter;
    }

}
