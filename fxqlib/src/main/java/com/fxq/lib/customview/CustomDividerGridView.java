package com.fxq.lib.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.fxq.lib.utils.Utils;

import gradleplugin.fxq.com.fxqlib.R;

/**
 * Created by Fangxq on 2017/2/13.
 */

public class CustomDividerGridView extends GridView {

    private int paddingTop;
    private int paddingBottom;
    private int paddingRight;
    private int padingLeft;
    public boolean isOnMeasure;

    public CustomDividerGridView(Context context) {
        super(context);
    }

    public CustomDividerGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomDividerGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setDividerStyle(int paddingLeft, int paddingRight, int paddingTop, int paddingBottom) {
        this.padingLeft = paddingLeft;
        this.paddingRight = paddingRight;
        this.paddingTop = paddingTop;
        this.paddingBottom = paddingBottom;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        View localView1 = getChildAt(0);
        int column = getWidth() / localView1.getWidth();

//        int column = 1;
//        try {
//            //通过反射拿到列数
//            Field field = GridView.class.getDeclaredField("mNumColumns");
//            field.setAccessible(true);
//            column = field.getInt(this);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
        int childCount = getChildCount();
        Paint localPaint;
        localPaint = new Paint();
        localPaint.setStrokeWidth(Utils.dip2px(getContext(), 0.4f));
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setColor(getContext().getResources().getColor(R.color.b3));
        for (int i = 0; i < childCount; i++) {
            View cellView = getChildAt(i);
            if ((i + 1) % column == 0) {
                if (i + (childCount / column) <= childCount) {
                    canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight() - paddingRight, cellView.getBottom(), localPaint);
                }
            } else if ((i + 1) > (childCount - (childCount % column))) {
                canvas.drawLine(cellView.getRight(), cellView.getTop() + paddingTop, cellView.getRight(), cellView.getBottom() - paddingBottom, localPaint);
            } else {
                canvas.drawLine(cellView.getRight(), cellView.getTop() + paddingTop, cellView.getRight(), cellView.getBottom() - paddingBottom, localPaint);
                if (i + (childCount / column) <= childCount) {
                    if (i % column == 0) {
                        canvas.drawLine(cellView.getLeft() + padingLeft, cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
                    } else {
                        canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
                    }
                }
            }
        }
        if (childCount % column != 0) {
            for (int j = 0; j < (column - childCount % column); j++) {
                View lastView = getChildAt(childCount - 1);
                canvas.drawLine(lastView.getRight() + lastView.getWidth() * j, lastView.getTop(), lastView.getRight() + lastView.getWidth() * j,
                        lastView.getBottom(), localPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        isOnMeasure = true;
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        isOnMeasure = false;
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        return true;
    }
}

