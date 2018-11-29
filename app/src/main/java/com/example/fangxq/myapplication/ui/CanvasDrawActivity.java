
package com.example.fangxq.myapplication.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.fangxq.myapplication.R;

public class CanvasDrawActivity extends Activity {
    private ImageView iv;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_draw);
        iv = (ImageView) findViewById(R.id.iv);
        bitmap = Bitmap.createBitmap(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels - iv.getTop(), Config.ARGB_8888);

    }

    public void paint(View v) {
        test();
    }

    private void test() {
        Canvas canvas = new Canvas(bitmap);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Style.STROKE);
        p.setColor(Color.BLUE);
        p.setStrokeWidth(5);
        PathEffect effects = new DashPathEffect(new float[]{1, 2, 4, 8}, 1);
        p.setPathEffect(effects);
        canvas.drawCircle(getResources().getDisplayMetrics().widthPixels / 2, getResources().getDisplayMetrics().widthPixels / 2,
                getResources().getDisplayMetrics().widthPixels / 3, p);
        canvas.drawLine(0, 0, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().widthPixels, p);
        iv.setImageBitmap(bitmap);
    }
}

