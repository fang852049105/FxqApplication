package com.example.fangxq.myapplication.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.fangxq.myapplication.R;

/**
 * Created by Fangxq on 2017/6/29.
 */
public class CjjShadowButton extends RelativeLayout {

    private Context mContext;
    private Button mButton;

    public CjjShadowButton(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public CjjShadowButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.view_shadow_button, this);
        mButton = (Button) findViewById(R.id.button);
    }

    public void setButtonEnabled(boolean enabled) {
        mButton.setEnabled(enabled);
    }

}
