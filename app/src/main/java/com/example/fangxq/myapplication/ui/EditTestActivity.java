package com.example.fangxq.myapplication.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.fangxq.myapplication.R;
import com.example.fangxq.myapplication.customview.CjjShadowButton;
import com.example.fangxq.myapplication.customview.CustomEditText;
import com.example.fangxq.myapplication.customview.TitleClearShownEditText;
import com.example.fangxq.myapplication.customview.TitleNumberClearEditText;
import com.fxq.lib.utils.Utils;

/**
 * Created by Fangxq on 2017/6/29.
 */
public class EditTestActivity extends Activity {

    private TitleNumberClearEditText mTitleNumberClearEditText;
    private TitleClearShownEditText mTitleClearShownEditText;
    private CjjShadowButton mCjjShadowButton;
    private CustomEditText mCustomEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_test);
        initView();
    }

    private void initView() {
        mTitleNumberClearEditText = (TitleNumberClearEditText) findViewById(R.id.mTitleNumberClearEditText);
        mTitleNumberClearEditText.setZoomTextSize(Utils.dip2px(EditTestActivity.this, 15));
        mTitleClearShownEditText = (TitleClearShownEditText) findViewById(R.id.mTitleClearShownEditText);
        mTitleClearShownEditText.setZoomTextSize(Utils.dip2px(EditTestActivity.this, 15));
        mCjjShadowButton = (CjjShadowButton) findViewById(R.id.mCjjShadowButtonView);
        mCjjShadowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("fxq", "onClick");
                mCjjShadowButton.setButtonEnabled(false);
            }
        });
        mCustomEditText = (CustomEditText) findViewById(R.id.CustomEditText);
        mCustomEditText.addOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.e("fxq", "hasFocus 1");
                } else {
                    Log.e("fxq", "not hasFocus 1");
                }
            }
        });
        mCustomEditText.addOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.e("fxq", "hasFocus 2");
                } else {
                    Log.e("fxq", "not hasFocus 2");
                }
            }
        });
        mCustomEditText.addOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.e("fxq", "hasFocus 3");
                } else {
                    Log.e("fxq", "not hasFocus 3");
                }
            }
        });
        mCustomEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.e("fxq", "hasFocus");
                } else {
                    Log.e("fxq", "not hasFocus");
                }
            }
        });

    }
}
