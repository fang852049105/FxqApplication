package com.example.fangxq.myapplication.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.example.fangxq.myapplication.R;

/**
 * @author huiguo
 * @date 2018/11/26
 */
public class FragmentTestActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fragment_test_activity);
    }
}
