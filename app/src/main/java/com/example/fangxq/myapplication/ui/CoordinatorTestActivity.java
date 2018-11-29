package com.example.fangxq.myapplication.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.fangxq.myapplication.R;
import com.example.fangxq.myapplication.adapter.MyRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import fxq.android.com.commonbusiness.ui.BaseSwipeFinishActivity;

/**
 * Created by Fangxq on 2017/3/1.
 */
public class CoordinatorTestActivity extends BaseSwipeFinishActivity {

    private RecyclerView mRecyclerView;
    private View textRelative;
    private TranslateAnimation mShowAction;
    private RelativeLayout userInfoRelative;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_coordinator);
//        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, 2.0f, Animation.RELATIVE_TO_SELF, 0.0f);
//        mShowAction.setDuration(300);
        addSwipeFinishLayout();
        initDate();
        userInfoRelative = (RelativeLayout) findViewById(R.id.userInfo) ;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        textRelative = findViewById(R.id.text_relative);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.AppBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.e("fxq", "verticalOffset = " + verticalOffset);
                int height = (userInfoRelative.getHeight() - textRelative.getHeight()) / 9*8;
                userInfoRelative.setAlpha((float) (height -Math.abs(verticalOffset))/(float) (height));
                int height2 = userInfoRelative.getHeight() - textRelative.getHeight() - height + 40;
                textRelative.setAlpha((float) (Math.abs(verticalOffset) - height + 40)/(float) (height2));
                textRelative.setTranslationY(height -Math.abs(verticalOffset) + 20);
                if (verticalOffset == 0) {
                    toolbar.setVisibility(View.GONE);
                } else if (50 < Math.abs(verticalOffset)){
                    toolbar.setVisibility(View.VISIBLE);
                }
            }
        });
        ((ImageView) findViewById(R.id.avatar0)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Log.e("fxq", "onClick");
            }
        });
        ((ImageView) findViewById(R.id.remainder)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Log.e("fxq", "onClick remainder");
            }
        });

    }
    private void initDate(){
        mRecyclerView = (RecyclerView) this.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        List<Integer> datas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            datas.add(i);
        }
        mRecyclerView.setAdapter(new MyRecyclerViewAdapter(this, datas));
    }

}
