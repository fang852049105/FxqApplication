package com.example.fangxq.myapplication.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.fangxq.myapplication.R;
import com.example.fangxq.myapplication.customview.BubbleSeekBar;
import com.fxq.lib.customview.CustomRangeSeekBar;
import com.fxq.lib.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fangxq on 2017/2/16.
 */
public class CustomSeekbarActivity extends Activity {

    private BubbleSeekBar mBubbleSeekBar;
    private TextView num_tv;
    private SeekBar seekBar;
    private double width, fDensity;
    private int numbers=0;

    private DisplayMetrics displaysMetrics;
    private List<String> mSectionList = new ArrayList<String>();
    private List<String> mSectionList2 = new ArrayList<String>();
    private TextView customSeekbarText;
    private TextView fxqCustomSeekbarText;

    private CustomRangeSeekBar customSeekbarSeparationShow;
    private CustomRangeSeekBar customSeekbar;
    private CustomRangeSeekBar mCustomSeekbarNumber;
    private int marginMin;
    private int marginMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_seekbar);
        initData();
        initView();
        marginMin = Utils.dip2px(CustomSeekbarActivity.this, 5);
    }

    private void initData () {
        mSectionList.add(getString(R.string.one_month));
        mSectionList.add(getString(R.string.six_month));
        mSectionList.add(getString(R.string.one_year));
        mSectionList.add(getString(R.string.three_year));

        for (int i = 1; i <= 11; i ++) {
            mSectionList2.add("风险等级"+ i);
        }
    }

    //获取屏幕信息，以及初始化操作
    private void initView() {
        displaysMetrics = getResources().getDisplayMetrics();
        width = displaysMetrics.widthPixels;
//        fDensity = (width - dip2px(this, 40)) / 100;
        mBubbleSeekBar = (BubbleSeekBar) findViewById(R.id.bubble_seek_bar);
        fxqCustomSeekbarText = (TextView) findViewById(R.id.custom_seek_bar_text3);
        customSeekbar = (CustomRangeSeekBar)findViewById(R.id.custom_seek_bar4);
        customSeekbar.setSectionList(mSectionList);
        customSeekbar.setAnimDuration(100);
//        customSeekbar.setOnProgressChangedListener(new CustomSeekbar.OnProgressChangedListener() {
//
//            @Override
//            public void onProgressChanged(float progress, String text, float mLeft, float mRight) {
//                LinearLayout.LayoutParams paramsStrength = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT);
//                fDensity = (mRight - mLeft) / 100.00;
//                Rect rect = new Rect();
//                Paint paint = new Paint();
//                paint.getTextBounds(text, 0, text.length(), rect);
//                paramsStrength.leftMargin = (int) (progress * fDensity +  mLeft - Utils.dip2px(CustomSeekbarActivity.this, rect.width()/2));
//                fxqCustomSeekbarText.setLayoutParams(paramsStrength);
//                fxqCustomSeekbarText.setText(text);
//
//            }
//
//        });
        //customSeekbar.setProgressSectionPostion(1, 3);

        customSeekbarSeparationShow =  (CustomRangeSeekBar)findViewById(R.id.custom_seek_bar6);
        //customSeekbarSeparationShow.setMark("低", "高");
        customSeekbarSeparationShow.setSectionList(mSectionList);
        //customSeekbarSeparationShow.setSeparationSectionPosition(8);
        customSeekbarSeparationShow.setAnimDuration(100);
        customSeekbarSeparationShow.setOnProgressChangedListener(new CustomRangeSeekBar.OnProgressChangedListener() {

            @Override
            public void onProgressChanged(float progress, int position, float mThumbCenterX, float mLeft, float mRight, boolean isFisrtSet, boolean autoAdjust) {
                LinearLayout.LayoutParams paramsStrength = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                fDensity = (mRight - mLeft) / 100.00;
                String text = mSectionList.get(position);
                Rect rect = new Rect();
                Paint paint = new Paint();
                paint.getTextBounds(text, 0, text.length(), rect);
                int marginLeft;
                if (mThumbCenterX > 0) {
                    marginLeft = (int) mThumbCenterX - Utils.dip2px(CustomSeekbarActivity.this, rect.width() / 2);
                } else {
                    marginLeft = (int) (progress * fDensity +  mLeft - Utils.dip2px(CustomSeekbarActivity.this, rect.width()/2));
                }
                if (marginLeft < marginMin) {
                    marginLeft = marginMin;
                }
                marginMax = (int)mRight - Utils.dip2px(CustomSeekbarActivity.this, rect.width());
                if (marginLeft > marginMax) {
                    marginLeft = marginMax;
                }
                paramsStrength.leftMargin = marginLeft;
                paramsStrength.rightMargin = marginMin;
                fxqCustomSeekbarText.setLayoutParams(paramsStrength);
                fxqCustomSeekbarText.setText(text);
            }


        });
        customSeekbarSeparationShow.setProgressSectionPosition(1, 3);


        mCustomSeekbarNumber =  (CustomRangeSeekBar)findViewById(R.id.custom_seek_bar7);
        mCustomSeekbarNumber.setAnimDuration(50);
        mCustomSeekbarNumber.setMinMax(1, 100);
        List<String> rangeList = new ArrayList<>();
        for (int i = 1; i <= 100; i ++) {
            rangeList.add(String.valueOf(i));
        }
        mCustomSeekbarNumber.setSectionList(rangeList);
        mCustomSeekbarNumber.setOnProgressChangedListener(new CustomRangeSeekBar.OnProgressChangedListener() {

            @Override
            public void onProgressChanged(float progress, int position, float mThumbCenterX, float mLeft, float mRight, boolean isFisrtSet, boolean autoAdjust) {
                LinearLayout.LayoutParams paramsStrength = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                fDensity = (mRight - mLeft) / (100-1);
                String text = String.valueOf(progress) + "%";
                Rect rect = new Rect();
                Paint paint = new Paint();
                paint.getTextBounds(text, 0, text.length(), rect);
                int marginLeft;
                if (mThumbCenterX > 0) {
                    marginLeft = (int) mThumbCenterX - Utils.dip2px(CustomSeekbarActivity.this, rect.width() / 2);
                } else {
                    marginLeft = (int) ((progress - 0.4) * fDensity +  mLeft - Utils.dip2px(CustomSeekbarActivity.this, rect.width()/2));
                }
                if (marginLeft < marginMin) {
                    marginLeft = marginMin;
                }
                paramsStrength.leftMargin = marginLeft;
                fxqCustomSeekbarText.setLayoutParams(paramsStrength);
                fxqCustomSeekbarText.setText(text);

            }


        });
        mCustomSeekbarNumber.setProgress(6.4f);

    }

    private SeekBar.OnSeekBarChangeListener mSeekChange = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            numbers = progress;
            LinearLayout.LayoutParams paramsStrength = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsStrength.leftMargin = (int) (progress * fDensity);
            num_tv.setLayoutParams(paramsStrength);
            num_tv.setText(numbers+"");

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
        }
    };

    /**
     * 根据手机分辨率从 px(像素) 单位 转成 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机分辨率从 dp 单位 转成 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
