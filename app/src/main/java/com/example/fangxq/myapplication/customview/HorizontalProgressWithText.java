package com.example.fangxq.myapplication.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.example.fangxq.myapplication.R;

/**
 * Created by Fangxq on 2017/10/16.
 */
public class HorizontalProgressWithText extends ProgressBar {

    private String text;
    private Paint mPaint;
    private Context context;
    private int mTextSize = sp2px(10);

    public HorizontalProgressWithText(Context context) {
        super(context);
        this.context = context;
        initText();
    }

    public HorizontalProgressWithText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initText();
    }


    public HorizontalProgressWithText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initText();
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
        int x = (getWidth() / 2) - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        canvas.drawText(this.text, x, y, this.mPaint);
    }

    //初始化，画笔
    private void initText(){
        this.mPaint = new Paint();
        this.mPaint.setTextSize(mTextSize);
        this.mPaint.setColor(context.getResources().getColor(R.color.a2));

    }


    public void setText(String text) {
        this.text = text;
    }

    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }

}
