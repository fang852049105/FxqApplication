package com.example.fangxq.myapplication.customview;

import android.content.Context;
import android.widget.TextView;

import com.example.fangxq.myapplication.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.text.DecimalFormat;


public class FinanceMarkerView extends MarkerView {

    private TextView mMarkerContentText;

    public FinanceMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        mMarkerContentText = (TextView) findViewById(R.id.markerContent);
    }

    @Override
    public int getXOffset(float xpos) {
        int offset = 0;
        setBackgroundResource(R.drawable.chart_marker_left);
        if (xpos > dip2px(getContext(), 250)) {
            offset = -getWidth();
            setBackgroundResource(R.drawable.chart_marker_right);
        } else if(xpos > dip2px(getContext(), 30)) {
            offset = -getWidth()/2;
            setBackgroundResource(R.drawable.chart_marker);
        }
        return offset;
    }

    @Override
    public int getYOffset(float ypos) {
        return -dip2px(getContext(), 3) - getHeight();
    }

    @Override
    public void refreshContent(Entry entry, Highlight highlight) {
        if (entry instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) entry;
            mMarkerContentText.setText(formatNumber(ce.getHigh(), 2) + "元");
        } else {
            mMarkerContentText.setTextColor(getResources().getColor(android.R.color.white));
            mMarkerContentText.setText(formatNumber(entry.getVal(), 2) + "元");
        }
    }

    public static String formatNumber(Number number, int digits) {
        String formatedNumber = "";
        if (number != null) {
            StringBuffer b = new StringBuffer();
            for (int i = 0; i < digits; i++) {
                if (i == 0)
                    b.append(".");
                b.append("0");
            }

            formatedNumber = format(number, "###,###,###,##0" + b.toString());
        }
        return formatedNumber;
    }

    private static String format(Number number, String format) {
        DecimalFormat mFormat = new DecimalFormat(format);
        return mFormat.format(number);
    }

    public static int dip2px(Context context, int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
