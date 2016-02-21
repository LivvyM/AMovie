package com.livvy.amovie.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.ekuaizhi.library.data.model.DataItem;
import com.ekuaizhi.library.data.model.DataItemArray;
import com.ekuaizhi.library.data.model.DataResult;
import com.ekuaizhi.library.widget.list.DataExpandListView;
import com.ekuaizhi.library.widget.repeater.DataExpandCell;
import com.livvy.amovie.R;
import com.livvy.amovie.activity.base.AppBaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 设置界面
 * Created by livvy on 2/20/16.
 */
public class SettingActivity extends AppBaseActivity{

    private DataExpandListView mExpandListExpand;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initData();
        mExpandListExpand = (DataExpandListView)findViewById(R.id.mExpandListExpand);
        mExpandListExpand.setDataCellClass(ExpandDemoCell.class);
        mExpandListExpand.setDataLoader((adapter, pageAt, pageSize) -> {
            return initData();
        },true);

    }

    private DataResult initData(){

        DataResult result = new DataResult();
        result.hasError = false;
        result.status = 0;
        result.message = "1111111";
        result.totalcount = 20;

        DataItem dataItem = new DataItem();
        dataItem.setString("content","Afghanistan");
        result.addItem(dataItem);
        dataItem = new DataItem();
        dataItem.setString("content","Afghanistan");
        result.addItem(dataItem);
        dataItem = new DataItem();
        dataItem.setString("content","Afghanistan");
        result.addItem(dataItem);
        dataItem = new DataItem();
        dataItem.setString("content","Afghanistan");
        result.addItem(dataItem);
        dataItem = new DataItem();
        dataItem.setString("content","Afghanistan");
        result.addItem(dataItem);
        dataItem = new DataItem();
        dataItem.setString("content","Afghanistan");
        result.addItem(dataItem);
        dataItem = new DataItem();
        dataItem.setString("content","Afghanistan");
        result.addItem(dataItem);

        dataItem = new DataItem();
        dataItem.setString("content","Bahamas");
        result.addItem(dataItem);
        dataItem = new DataItem();
        dataItem.setString("content","Bahamas");
        result.addItem(dataItem);
        dataItem = new DataItem();
        dataItem.setString("content","Bahamas");
        result.addItem(dataItem);
        dataItem = new DataItem();
        dataItem.setString("content","Bahamas");
        result.addItem(dataItem);
        dataItem = new DataItem();
        dataItem.setString("content","Bahamas");
        result.addItem(dataItem);
        dataItem = new DataItem();
        dataItem.setString("content","Bahamas");
        result.addItem(dataItem);
        dataItem = new DataItem();
        dataItem.setString("content","Bahamas");
        result.addItem(dataItem);

        dataItem = new DataItem();
        dataItem.setString("content","Crims");
        result.addItem(dataItem);
        dataItem = new DataItem();
        dataItem.setString("content","Crims");
        result.addItem(dataItem);
        dataItem = new DataItem();
        dataItem.setString("content","Crims");
        result.addItem(dataItem);
        dataItem = new DataItem();
        dataItem.setString("content","Crims");
        result.addItem(dataItem);
        dataItem = new DataItem();
        dataItem.setString("content","Crims");
        result.addItem(dataItem);
        dataItem = new DataItem();
        dataItem.setString("content","Crims");
        result.addItem(dataItem);

        return result;
    }


    public class ExpandDemoCell extends DataExpandCell{

        private TextView mTextExpand;
        private TextView mTextConvert;


        private List<DataItem> mDatas = new ArrayList<>();
        private List<Integer> mSectionIndices = new ArrayList<>();
        private List<String> mSectionLetters = new ArrayList<>();


        @Override
        public int getCellViewLayoutID() {
            return R.layout.test_list_item_layout;
        }

        @Override
        public int getExpandCellViewLayoutID() {
            return R.layout.header;
        }

        @Override
        public void bindView() {
            mTextConvert = (TextView)findViewById(getCellView(),R.id.mTextConvert);
        }

        @Override
        public void bindExpandView() {
            mTextExpand = (TextView)findViewById(getExpandView(),R.id.mTextExpand);
        }

        @Override
        public void bindExpandData() {
            // set header text as first char in name
            CharSequence headerChar = mDetail.getString("content").subSequence(0, 1);
            mTextExpand.setText(headerChar);
        }

        @Override
        public void bindData() {
            mDatas.clear();
            mSectionIndices.clear();
            mSectionLetters.clear();

            mDatas = mAdapter.getDataList().items.<DataItem>getItems();
            mSectionIndices = getSectionIndices();
            mSectionLetters = getSectionLetters();

            mTextConvert.setText(mDetail.getString("content"));
        }

        @Override
        public int getPositionForSection(int sectionIndex) {

            return mSectionIndices.get(sectionIndex);

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
            return mDetail.getString("content").subSequence(0, 1).charAt(0);
        }

        private List<Integer> getSectionIndices() {
            ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
            char lastFirstChar = mDatas.get(0).getString("content").charAt(0);
            sectionIndices.add(0);
            for (int i = 1; i < mDatas.size(); i++) {
                if (mDatas.get(i).getString("content").charAt(0) != lastFirstChar) {
                    lastFirstChar = mDatas.get(i).getString("content").charAt(0);
                    sectionIndices.add(i);
                }
            }
            return sectionIndices;
        }

        private List<String> getSectionLetters() {
            List<String> letter = new ArrayList<>();
            for (int i = 0; i < mSectionIndices.size(); i++) {
                letter.add(mDatas.get(mSectionIndices.get(i)).getString("content").substring(0,2));
            }
            return letter;
        }

    }

}
