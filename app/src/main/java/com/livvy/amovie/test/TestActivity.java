package com.livvy.amovie.test;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ekuaizhi.library.data.model.DataItem;
import com.ekuaizhi.library.widget.list.DataListView;
import com.ekuaizhi.library.widget.repeater.DataCell;
import com.livvy.amovie.R;
import com.livvy.amovie.activity.base.AppBaseActivity;

/**
 *
 * Created by livvym on 16-3-1.
 */
public class TestActivity extends AppBaseActivity{

    private DataListView mListDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mListDemo = (DataListView)findViewById(R.id.mListDemo);
        mListDemo.setDataCellClass(DemoCell.class);
        mListDemo.setDataLoader((adapter, pageAt, pageSize) -> {
            //修改DemoData方法，模拟各种环境
            return  DemoData.loadDataFromNetwork(pageAt, pageSize);
        },true);
        // 设置列表视图的点击事件
        mListDemo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataItem item = mListDemo.getDataItem(position); // 获取当前 position 对应 CELL 的数据
                toast("点击了: " + item.getString("job_name"));
            }
        });
    }

    class DemoCell extends DataCell{

        private TextView mTextTitle;
        private TextView mTextContent;

        @Override
        public int getCellViewLayoutID() {
            return R.layout.cell_test;
        }

        @Override
        public void bindView() {
            mTextTitle = (TextView)findViewById(R.id.mTextTitle);
            mTextContent = (TextView)findViewById(R.id.mTextContent);
        }

        @Override
        public void bindData() {
            mTextTitle.setText(mDetail.getString("job_name"));
            mTextContent.setText(mDetail.getString("co_name"));
        }
    }
}
