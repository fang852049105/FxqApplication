package com.example.fangxq.myapplication.ui;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.example.fangxq.myapplication.R;
import com.example.fangxq.myapplication.adapter.RecycleViewAdapter;
import com.example.fangxq.myapplication.manager.CustomLinearLayoutManager;
import com.fxq.apt.annotation.BindView;
import com.fxq.lib.utils.Utils;

import fxq.android.com.commonbusiness.utils.BindViewUtils;

/**
 * @author huiguo
 * @date 2018/11/26
 */
public class RecycleViewFragment extends Fragment {

    private View mContentView;
    private RecyclerView mRecyclerView;
    private RelativeLayout mContentLayout;
    private CustomLinearLayoutManager manager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_recycleview_fragment, container, false);
        initView();
        return mContentView;
    }

    private void initView() {
        mContentLayout = (RelativeLayout) mContentView.findViewById(R.id.rl_content);
        mRecyclerView = (RecyclerView) mContentView.findViewById(R.id.recycler_view);
        manager = new CustomLinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        RecycleViewAdapter recycleViewAdapter  = new RecycleViewAdapter(getActivity(), R.layout.layout_recyclew_item_view, 20);
        recycleViewAdapter.setOnItemClickLitener(new RecycleViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View v, int x, int y) {
                final View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_animation_view, mContentLayout, false);
                RelativeLayout.LayoutParams focusItemParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                focusItemParams.leftMargin = x + Utils.dip2px(getActivity(), 15);
                focusItemParams.topMargin = y - Utils.dip2px(getActivity(), 30);
                view.setLayoutParams(focusItemParams);
                mContentLayout.addView(view);
                ObjectAnimator translation = ObjectAnimator.ofFloat(view, "translationY", 0f, -60f, -80f, -80f, -60f);
                ObjectAnimator fade = ObjectAnimator.ofFloat(view, "alpha", 0f, 0.2f, 0.6f, 0.8f, 1f, 1.0f, 0.8f, 0f);

                ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1f, 1.05f, 1.1f, 1.05f, 1f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1f, 1.05f, 1.1f, 1.05f, 1f);

                AnimatorSet animSet1 = new AnimatorSet();
                animSet1.play(scaleX).with(scaleY);
                animSet1.setDuration(500);
                animSet1.start();

                AnimatorSet animSet = new AnimatorSet();
                animSet.play(translation).with(fade);
                animSet.setInterpolator(new AccelerateDecelerateInterpolator());
                animSet.setDuration(1000);

                animSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        manager.setScrollEnabled(false);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        manager.setScrollEnabled(true);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animSet.start();

            }
        });
        mRecyclerView.setAdapter(recycleViewAdapter);

    }
}
