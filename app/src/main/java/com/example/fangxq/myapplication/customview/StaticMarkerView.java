package com.example.fangxq.myapplication.customview;

import android.content.Context;
import android.widget.TextView;

import com.example.fangxq.myapplication.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.Utils;

/**
 * Created by Fangxq on 2017/2/15.
 */
public class StaticMarkerView extends MarkerView {

    private TextView tvContent;

    public StaticMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.tvContent);

    }
    @Override
    public void refreshContent(Entry entry, Highlight highlight) {
        if (entry.getXIndex() == 5) {
            setBackgroundResource(R.drawable.ic_sell);
        } else if (entry.getXIndex() == 10) {
            setBackgroundResource(R.drawable.ic_buy);
        } else if (entry.getXIndex() == 15) {
            setBackgroundResource(R.drawable.ic_rebalance);
        }

        if (entry instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) entry;

            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {

            tvContent.setText("" + Utils.formatNumber(entry.getVal(), 0, true));
        }
    }


    @Override
    public int getXOffset(float xpos) {
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float v) {
        return -getHeight();
    }

}
