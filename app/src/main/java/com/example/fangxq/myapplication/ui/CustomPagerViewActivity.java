package com.example.fangxq.myapplication.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fangxq.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import fxq.android.com.commonbusiness.adapter.ViewPagerHolder;
import fxq.android.com.commonbusiness.adapter.ViewPagerHolderCreator;
import fxq.android.com.commonbusiness.customview.CommonViewPager;

/**
 * Created by Fangxq on 2017/5/31.
 */
public class CustomPagerViewActivity extends Activity {

    private CommonViewPager mCommonViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_pagerview);
        initView();
    }

    private void initView() {
        mCommonViewPager = (CommonViewPager) findViewById(R.id.activity_common_view_pager);
        // 设置数据
        mCommonViewPager.setPages(mockData(), new ViewPagerHolderCreator<ViewImageHolder>() {
            @Override
            public ViewImageHolder createViewHolder() {
                // 返回ViewPagerHolder
                return new ViewImageHolder();
            }
        });
    }

    /**
     * 提供ViewPager展示的ViewHolder
     * <P>用于提供布局和绑定数据</P>
     */
    public static class ViewImageHolder implements ViewPagerHolder<DataEntry> {
        private ImageView mImageView;
        private TextView mTextView;
        @Override
        public View createView(Context context) {
            // 返回ViewPager 页面展示的布局
            View view = LayoutInflater.from(context).inflate(R.layout.view_pager_item,null);
            mImageView = (ImageView) view.findViewById(R.id.viewPager_item_image);
            mTextView = (TextView) view.findViewById(R.id.item_desc);
            return view;
        }

        @Override
        public void onBind(Context context, int position, DataEntry data) {
            // 数据绑定
            // 自己绑定数据，灵活度很大
            mImageView.setImageResource(data.imageResId);
            mTextView.setText(data.desc);
        }
    }

    private List<DataEntry> mockData() {
        List<DataEntry> DataEntryList = new ArrayList<>();
        for (int i = 0; i < 5; i ++) {
            DataEntry dataEntry = new DataEntry();
            dataEntry.setDesc("test" + i);
            dataEntry.setImageResId(R.drawable.ic_test);
            DataEntryList.add(dataEntry);
        }
        return DataEntryList;
    }

    public class DataEntry {
        private int imageResId;

        private String desc;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getImageResId() {
            return imageResId;
        }

        public void setImageResId(int imageResId) {
            this.imageResId = imageResId;
        }
    }

}
