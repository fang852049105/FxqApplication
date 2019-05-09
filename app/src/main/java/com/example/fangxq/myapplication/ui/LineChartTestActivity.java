package com.example.fangxq.myapplication.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.fangxq.myapplication.R;
import com.example.fangxq.myapplication.customview.StaticMarkerView;
import com.example.fangxq.myapplication.customview.TestMarkerView;
import com.example.fangxq.myapplication.customview.TestPopupViewManager;
import com.example.fangxq.myapplication.utils.Reflect;
import com.fxq.lib.utils.Utils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Fangxq on 2017/2/15.
 */
public class LineChartTestActivity extends Activity implements OnChartValueSelectedListener {

    private LineChart mChart;
   // private LineChart mChart1;
    private RadioGroup radioGroup;
    private ArrayList<Entry> yPoint1 = new ArrayList<Entry>();
    private ArrayList<Entry> yPoint2 = new ArrayList<Entry>();
    private LineDataSet pointData = new LineDataSet(yPoint1, "point1 dataset");
    private LineDataSet pointData2 = new LineDataSet(yPoint2, "point2 dataset");
    private int dataXIndex = -1;
    private ScrollView mScrollView;
    private float mLastY = -1;
    private  float mNowY = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linechart_test);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        Reflect.on(mScrollView).set("mOverflingDistance", 200);
        mChart = (LineChart) findViewById(R.id.chartTest);
        //mChart1= (LineChart) findViewById(R.id.chartTest1);
        mChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mLastY = event.getY();
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    mNowY = event.getY();
                    if (Math.abs(Math.abs(mLastY) - Math.abs(mNowY)) > Utils.dip2px(LineChartTestActivity.this, 50)) {
                        mScrollView.requestDisallowInterceptTouchEvent(false);
                    } else {
                        mScrollView.requestDisallowInterceptTouchEvent(true);
                    }
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    //允许ScrollView截断点击事件，ScrollView可滑动
                    mScrollView.requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        initDataStyle(mChart);
        initDoubleLineChart(20, 30);

//        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                mChart.clear();
//                if (checkedId == R.id.oneYear) {
//                    initDoubleLineChart(20, 100);
//                    mChart.getData().notifyDataChanged();
//                    mChart.notifyDataSetChanged();
//                    mChart.animateX(1500);
//                    mChart.invalidate();
//
//                } else if (checkedId == R.id.twoYear) {
//                    initDoubleLineChart(30, 100);
//                    mChart.getData().notifyDataChanged();
//                    mChart.notifyDataSetChanged();
//                    mChart.animateX(1500);
//                    mChart.invalidate();
//                }
//            }
//        });
        initRaidoGroup();
    }

    private void initRaidoGroup() {
        List<String> stringList = new ArrayList<>();
        stringList.add("近1月");
        stringList.add("近3月");
        stringList.add("近6月");
        stringList.add("近一年");
        for (int i = 0; i <stringList.size(); i++ ) {
            RadioButton button=new RadioButton(this);
            setRaidBtnAttribute(button, stringList.get(i), 0);

            LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) button
                    .getLayoutParams();
            layoutParams.setMargins(Utils.dip2px(this,10), 0,  Utils.dip2px(this,10), 0);//4个参数按顺序分别是左上右下
            button.setLayoutParams(layoutParams);
            if (i == 1) {
                button.setChecked(true);
            }
        }
    }

    @SuppressLint("ResourceType")
    private void setRaidBtnAttribute(final RadioButton codeBtn, String btnContent, int id ){
        if( null == codeBtn ){
            return;
        }
        codeBtn.setBackgroundResource(R.drawable.trend_button_selector);
        codeBtn.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
        codeBtn.setTextSize(12f);
        codeBtn.setText(btnContent);
        codeBtn.setTextColor(ContextCompat.getColorStateList(LineChartTestActivity.this, R.drawable.color_radiobutton));

        codeBtn.setGravity( Gravity.CENTER );
        codeBtn.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                initDoubleLineChart(30, 100);
                //mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
                mChart.animateX(1500);
                mChart.invalidate();
                Toast.makeText(LineChartTestActivity.this, codeBtn.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        //DensityUtilHelps.Dp2Px(this,40)
        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(Utils.dip2px(this, 68),  Utils.dip2px(this, 28));
        codeBtn.setLayoutParams( rlp );
        radioGroup.addView(codeBtn);
    }



    private void initDataStyle(LineChart mLineChart) {

        mLineChart.setDescription(null);
        mLineChart.setTouchEnabled(true);
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(false);
        mLineChart.setPinchZoom(false);
        mLineChart.setDrawGridBackground(false);
        mLineChart.setBackgroundResource(R.drawable.chart_bg);
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.getLegend().setEnabled(false);
        mLineChart.setOnChartValueSelectedListener(this);
        mLineChart.setViewPortOffsets(0,0,0,50);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        xAxis.setDrawAxisLine(false);

        YAxis yAxis = mLineChart.getAxisLeft();
        yAxis.setDrawAxisLine(false);
        yAxis.removeAllLimitLines();
        yAxis.setYOffset(0f);
        yAxis.setZeroLineColor(getResources().getColor(R.color.a2));
        yAxis.setDrawZeroLine(true);
        yAxis.setDrawGridLines(false);
        yAxis.setGridColor(getResources().getColor(R.color.a2));
        yAxis.setValueFormatter(new DecimalYAxisValueFormatter());
        yAxis.setDrawLimitLinesBehindData(true);
        yAxis.setSpaceBottom(0f);
        yAxis.setXOffset(10f);
        yAxis.setDrawTopYLabelEntry(false);
        yAxis.setTextColor(getResources().getColor(R.color.a2));
        mChart.animateX(1500);
        mChart.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                Log.e("fxq", "onDrag");
                return true;
            }
        });

    }

    private void initDoubleLineChart(int count, float range) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add((i) + "");
        }

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            float mult = range / 2f;
            float val = (float) (Math.random() * mult) + 50;// + (float)
            // ((mult *
            // 0.1) / 10);
            yVals1.add(new Entry(val, i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals1, "DataSet 1");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setLineWidth(1f);
        set1.setCircleSize(3f);
        set1.setDrawCircles(false);
        set1.setDrawValues(false);
        set1.setColor(getResources().getColor(R.color.b2));
        set1.setCircleColor(getResources().getColor(R.color.b2));
        set1.setHighLightColor(getResources().getColor(R.color.a2));
        set1.setDrawCircleHole(false);
        set1.setDrawHorizontalHighlightIndicator(false);

        ArrayList<Entry> yVals2 = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            float mult = range;
            float val = (float) (Math.random() * mult) - 10;// + (float)
            // ((mult *
            // 0.1) / 10);
            yVals2.add(new Entry(val, i));
        }

        // create a dataset and give it a type
        LineDataSet set2 = new LineDataSet(yVals2, "DataSet 2");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setLineWidth(1f);
        set2.setDrawCircles(false);
        set2.setCircleSize(3f);
        set2.setDrawValues(false);
        set2.setDrawCircleHole(false);

        set2.setColor(getResources().getColor(R.color.a2));
        set2.setCircleColor(getResources().getColor(R.color.a2));
        set2.setDrawHorizontalHighlightIndicator(false);
        set2.setHighLightColor(getResources().getColor(R.color.a2));

        ArrayList<Entry> yPoint = new ArrayList<Entry>();
        yPoint.add(yVals2.get(5));
        yPoint.add(yVals2.get(15));
        LineDataSet pointSet = new LineDataSet(yPoint, "pointSet");
        pointSet.setCircleSize(2f);
        pointSet.setDrawCircles(true);
        pointSet.setDrawHighlightIndicators(true);
        pointSet.setDrawCircleHole(false);
        pointSet.setValueTextSize(9f);
        pointSet.setDrawValues(false);
        pointSet.setDrawFilled(false);
        pointSet.setLineWidth(0f);
        pointSet.setColor(Color.TRANSPARENT);
        pointSet.setCircleColor(getResources().getColor(R.color.a1));
        pointSet.setHighLightColor(Color.TRANSPARENT);

        float yMax = Math.max(set1.getYMax(), set2.getYMax());
        float yMin = Math.min(set1.getYMin(), set2.getYMin());
        if (yMax != yMin) {
            float maxValue = yMax + Math.abs(yMax - yMin) * 0.3f;
            float minValue = yMin - Math.abs(yMax- yMin) * 0.3f;
            if (maxValue > 0) {
               // calculateMaxMin(mChart1, maxValue, Math.min(minValue, 0));
                calculateMaxMin(mChart, maxValue, Math.min(minValue, 0));
            } else {
                calculateMaxMin(mChart, -minValue / 5, minValue);
                //calculateMaxMin(mChart1, -minValue / 5, minValue);

            }
        } else if (xVals.size() == 1) {
            if (yMax > 0) {
                mChart.getAxisLeft().setAxisMaxValue(yMax * 1.2f);
                //mChart1.getAxisLeft().setAxisMaxValue(yMax * 1.2f);
                mChart.getAxisLeft().setAxisMinValue(0);
               // mChart1.getAxisLeft().setAxisMinValue(0);
            } else {
                mChart.getAxisLeft().setAxisMinValue(yMax * 1.2f);
                //mChart1.getAxisLeft().setAxisMinValue(yMax * 1.2f);
                mChart.getAxisLeft().setAxisMaxValue(-yMax * 1.2f);
               // mChart1.getAxisLeft().setAxisMaxValue(-yMax * 1.2f);
            }
        } else {
            if (yMax > 0) {
                calculateMaxMin(mChart, yMax * 1.2f, Math.min(yMin * 0.8f, 0));
                //calculateMaxMin(mChart1, yMax * 1.2f, Math.min(yMin * 0.8f, 0));
            } else if (yMin < 0) {
                calculateMaxMin(mChart, Math.max(yMax / 5, 0), yMin * 1.2f);
                //calculateMaxMin(mChart1, Math.max(yMax / 5, 0), yMin * 1.2f);
            } else {
                mChart.getAxisLeft().setAxisMaxValue(10f);
                //mChart1.getAxisLeft().setAxisMaxValue(10f);
                mChart.getAxisLeft().setAxisMinValue(-10f);
               // mChart1.getAxisLeft().setAxisMinValue(-10f);
            }
        }
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);
//        data.setValueTextColor(Color.RED);
//        data.setValueTextSize(9f);

        // set data
        mChart.setData(data);

        TestMarkerView tmv = new TestMarkerView(LineChartTestActivity.this, R.layout.layout_marker_content, yVals1, yVals2);
        mChart.setMarkerView(tmv);
        StaticMarkerView smv = new StaticMarkerView(this, R.layout.custom_marker_view);
        mChart.setStaticMarkerView(smv);
        ArrayList<Highlight> highlights = new ArrayList<>();
        highlights.add(new Highlight(5, 0));
        highlights.add(new Highlight(10, 0));
        highlights.add(new Highlight(15, 0));
        Highlight[] highlightArr =  new Highlight[highlights.size()];
        highlights.toArray(highlightArr);
        mChart.drawStaticMarkerValues(highlightArr);
//        ArrayList<Highlight> highlights = new ArrayList<>();
//        highlights.add(new Highlight(5, 0));
//        highlights.add(new Highlight(15, 0));
//        Highlight[] highlightArr =  new Highlight[highlights.size()];
//        highlights.toArray(highlightArr);
//        mChart1.highlightValues(highlightArr);
    }

    public class DecimalYAxisValueFormatter implements YAxisValueFormatter {

        private DecimalFormat mFormat;
        private static final String decimalFormatter = "###,##0.00";

        public DecimalYAxisValueFormatter() {
            mFormat = new DecimalFormat(decimalFormatter);
        }

        @Override
        public String getFormattedValue(float value, YAxis yAxis) {
            String result = "";
            if (value == 0) {
                result = "0";
            }
            return result;
        }
    }

    private void calculateMaxMin(LineChart lineChart, float maxValue, float minValue) {
//        if (maxValue - minValue < 0.05) {
//            lineChart.getAxisLeft().setAxisMaxValue((0.05f - (maxValue - minValue)) / 2f + maxValue);
//            lineChart.getAxisLeft().setAxisMinValue(minValue - (0.05f - (maxValue - minValue)) / 2f);
//        } else {
//            lineChart.getAxisLeft().setAxisMaxValue(maxValue);
//            lineChart.getAxisLeft().setAxisMinValue(minValue);
//        }
    }


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        ArrayList<ILineDataSet> sets = (ArrayList<ILineDataSet>) mChart.getData().getDataSets();

        yPoint1.clear();
        yPoint2.clear();

        yPoint1.add(sets.get(0).getEntryForXIndex(e.getXIndex()));
        yPoint2.add(sets.get(1).getEntryForXIndex(e.getXIndex()));


        pointData.setCircleRadius(2f);
        pointData.setDrawCircles(true);
        pointData.setDrawCircleHole(false);
        pointData.setValueTextSize(9f);
        pointData.setDrawFilled(true);
        pointData.setDrawValues(false);
        pointData.setColor(getResources().getColor(R.color.b2));
        pointData.setCircleColor(getResources().getColor(R.color.b2));
        pointData.setHighLightColor(Color.TRANSPARENT);

        pointData2.setCircleRadius(2f);
        pointData2.setDrawCircles(true);
        pointData2.setDrawCircleHole(false);
        pointData2.setDrawFilled(true);
        pointData2.setDrawValues(false);
        pointData2.setColor(getResources().getColor(R.color.a2));
        pointData2.setCircleColor(getResources().getColor(R.color.a2));
        pointData2.setHighLightColor(Color.TRANSPARENT);

        mChart.getData().addDataSet(pointData);
        mChart.getData().addDataSet(pointData2);
        mChart.getData().notifyDataChanged();
        mChart.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected() {
        yPoint1.clear();
        yPoint2.clear();
        mChart.getData().notifyDataChanged();
        mChart.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TestPopupViewManager.getInstance().dismissPopupView();
    }
}
