package com.example.fangxq.myapplication.customview;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.fangxq.myapplication.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fangxq on 2017/2/15.
 */
public class TestMarkerView extends MarkerView {

    private TextView mMarkerContentText;
    private List<Entry> yValue1 = new ArrayList<Entry>();
    private List<Entry> yValue2 = new ArrayList<Entry>();

    public TestMarkerView(Context context, int layoutResource, ArrayList<Entry> yValue1, ArrayList<Entry> yValue2) {
        super(context, layoutResource);
        this.yValue1 = yValue1;
        this.yValue2 = yValue2;
        mMarkerContentText = (TextView) findViewById(R.id.markerContent);

    }
    @Override
    public void refreshContent(Entry entry, Highlight highlight) {
        mMarkerContentText.setTextColor(getResources().getColor(android.R.color.white));
        mMarkerContentText.setText("***指数" + String.valueOf((int)yValue1.get(entry.getXIndex()).getVal()) + "\n" + "****指数" +String.valueOf((int)yValue2.get(entry.getXIndex()).getVal()));
    }

    @Override
    public int getXOffset(float xpos) {
        int offset = 0;
//        if (xpos > dip2px(getContext(), 250)) {
//            offset = -getWidth();
//            //setBackgroundResource(R.drawable.chart_marker_right);
//        } else if(xpos > dip2px(getContext(), 30)) {
//            offset = -getWidth()/2;
//            //setBackgroundResource(R.drawable.chart_marker);
//        }
        if (xpos > getDeviceDisplayInfo(getContext()).widthPixels * 0.5) {
            offset = -getWidth() -  dip2px(getContext(), 15);
        } else {
            offset = dip2px(getContext(), 15);
        }
        return offset;
    }

    @Override
    public int getYOffset(float v) {
        return -dip2px(getContext(), 3) - getHeight();
    }

    public int dip2px(Context context, int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    public DisplayMetrics getDeviceDisplayInfo(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metric);
        return metric;
    }
}
