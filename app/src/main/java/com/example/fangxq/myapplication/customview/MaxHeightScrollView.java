package com.example.fangxq.myapplication.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.example.fangxq.myapplication.R;
import com.fxq.lib.utils.Utils;

/**
 * Created by Fangxq on 2017/11/2.
 */
public class MaxHeightScrollView extends ScrollView {

    private float maxHeight;
    private float maxHeightRatio;
    private static final float DEFAULT_MAX_RATIO = 0.6f;
    private static final float DEFAULT_MAX_HEIGHT = 0f;

    public MaxHeightScrollView(Context context) {
        this(context, null);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightScrollView, defStyleAttr, 0);
        maxHeightRatio = a.getFloat(R.styleable.MaxHeightScrollView_sv_max_height_ratio, DEFAULT_MAX_RATIO);
        maxHeight = a.getDimension(R.styleable.MaxHeightScrollView_sv_max_height, DEFAULT_MAX_HEIGHT);
        a.recycle();
        init();
    }

    private void init(){
        if (maxHeight <= 0) {
            maxHeight = maxHeightRatio * Utils.getDeviceDisplayInfo(getContext()).heightPixels;
        } else {
            maxHeight = Math.min(maxHeight, maxHeightRatio * Utils.getDeviceDisplayInfo(getContext()).heightPixels);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        heightSize = heightSize <= maxHeight ? heightSize : (int) maxHeight;
        int maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize,
                heightMode);
        super.onMeasure(widthMeasureSpec, maxHeightMeasureSpec);
    }

}
