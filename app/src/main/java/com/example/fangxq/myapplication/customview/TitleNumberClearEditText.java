package com.example.fangxq.myapplication.customview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fangxq.myapplication.R;
import com.fxq.lib.utils.Utils;


/**
 * Created by Fangxq on 2017/6/29.
 */
public class TitleNumberClearEditText extends RelativeLayout {

    private Context mContext;
    private TextView mTitleText;
    private NumberClearEditText mNumberClearEditText;
    private boolean isTitleZoom = false;
    private static final int TITLE_SIZE = 15;
    private int mZoomTitleSize; // px

    public TitleNumberClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public TitleNumberClearEditText(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.view_title_number_clear_edit_text, this);
        mTitleText = (TextView) findViewById(R.id.title);
        mNumberClearEditText = (NumberClearEditText) findViewById(R.id.number_edit_layout);
        mNumberClearEditText.setOnFocusChanged(new NumberClearEditText.OnFocusChanged() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (!isTitleZoom) {
                        textAnimation();
                    }
                    isTitleZoom = true;
                }
            }
        });

    }

    private void textAnimation() {
        float mOriginalTextSize = mNumberClearEditText.getEditTextSize();
        if (mZoomTitleSize == 0) {
            mZoomTitleSize = Utils.dip2px(mContext, TITLE_SIZE);
        }
        float scale = mZoomTitleSize / mOriginalTextSize;
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator xanimator = ObjectAnimator.ofFloat(mTitleText, "scaleX", 1f, scale);
        ObjectAnimator yaniAnimator = ObjectAnimator.ofFloat(mTitleText, "scaleY", 1f, scale);
        animatorSet.playTogether(xanimator, yaniAnimator);
        animatorSet.setDuration(500);
        animatorSet.start();
    }

    public void setZoomTextSize(int textSize) {
        this.mZoomTitleSize = textSize;
    }
}
