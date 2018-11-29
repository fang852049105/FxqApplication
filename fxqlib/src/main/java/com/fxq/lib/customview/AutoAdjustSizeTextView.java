package com.fxq.lib.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.fxq.lib.utils.Utils;


/**
 * Created by Fangxq on 2016/12/6.
 */
public class AutoAdjustSizeTextView extends TextView {

    // Attributes
    private Paint testPaint;
    private float cTextSize;
    private int DEFAULT_MIN_TEXT_SIZE = 11;
    private Context mContext;


    public AutoAdjustSizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    /**
     * Re size the font so the specified text fits in the text box * assuming
     * the text box is the specified width.
     */
    private void refitText(String text, int textWidth) {
        if (textWidth > 0) {
            testPaint = new Paint();
            testPaint.set(this.getPaint());
            //获得当前TextView的有效宽度
            int availableWidth = textWidth - this.getPaddingLeft()
                    - this.getPaddingRight();
            float[] widths = new float[text.length()];
            Rect rect = new Rect();
            testPaint.getTextBounds(text, 0, text.length(), rect);
            //所有字符串所占像素宽度
            int textWidths = rect.width();
            int textNum = 0;
            cTextSize = this.getTextSize();//这个返回的单位为px
            while (textWidths > availableWidth) {
                cTextSize = cTextSize - 1;
                if (cTextSize <= Utils.dip2px(mContext, DEFAULT_MIN_TEXT_SIZE)) {
                    cTextSize = Utils.dip2px(mContext, DEFAULT_MIN_TEXT_SIZE);
                    break;
                }
                testPaint.setTextSize(cTextSize);//这里传入的单位是px
                //textWidths = testPaint.getTextWidths(text, widths);
                textNum = testPaint.getTextWidths(text, widths);
                textWidths = 0;
                for (int i = 0; i < textNum; i++) {
                    textWidths += widths[i];
                }
            }
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, cTextSize);//这里制定传入的单位是px
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        refitText(getText().toString(), this.getWidth());
    }

    public void setMinTextSize(int size) {
        this.DEFAULT_MIN_TEXT_SIZE = size;
    }
}
