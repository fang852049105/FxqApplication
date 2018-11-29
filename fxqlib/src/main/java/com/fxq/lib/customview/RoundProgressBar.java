package com.fxq.lib.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.nineoldandroids.animation.ValueAnimator;

import gradleplugin.fxq.com.fxqlib.R;

public class RoundProgressBar extends View {
    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private int progress;
    /**
     * 是否显示中间的进度
     */
    private boolean textIsDisplayable;
    /**
     * 是否逆时针显示进度
     */
    private boolean anticlockwise;

    /**
     * 是否分区显示
     */
    private boolean variousDisplay;

    /**
     * 分区后是否显示段间空格
     */
    private boolean displayGap = true;

    /**
     * 分区显示模式（粗－细<true> or 细－粗<false>）
     */
    private boolean variousInverse;

    /**
     * 第一分区名字
     */
    private String variousPart1;

    /**
     * 第二分区名字
     */
    private String variousPart2;

    /**
     * 一二分区的粗细差别
     */
    private float variousRoundWidthDv;

    /**
     * Animator for progress
     */
    private ValueAnimator progressAnimator;

    /**
     * 圆弧一段接一段的显示
     */
    private boolean displayOneByOne;

    /**
     * 圆环进度的颜色、粗细
     */
    private int roundProgressColorFirst;
    private int roundProgressColorSecond;
    private int roundProgressColorThird;

    /**
     * 圆环进度的粗细 0-细，1-粗
     */
    private int roundProgressStyleFirst;
    private int roundProgressStyleSecond;
    private int roundProgressStyleThird;

    /**
     * 圆弧最终进度
     */
    private float roundProgressFirst = -1;
    private float roundProgressSecond = -1;


    /**
     * 进度的风格，实心或者空心
     */
    private int style;

    public static final int STROKE = 0;
    public static final int FILL = 1;

    public static final int MIN_CIRCLE = (int) (360 * 0.16);
    private static final int MIN_WEIGHT = 4;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint = new Paint();

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);

        // 获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.GREEN);
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 15);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
        anticlockwise = mTypedArray.getBoolean(R.styleable.RoundProgressBar_anticlockwise, false);
        variousDisplay = mTypedArray.getBoolean(R.styleable.RoundProgressBar_variousDisplay, false);
        variousInverse = mTypedArray.getBoolean(R.styleable.RoundProgressBar_variousInverse, false);
        variousPart1 = mTypedArray.getString(R.styleable.RoundProgressBar_variousPart1);
        variousPart2 = mTypedArray.getString(R.styleable.RoundProgressBar_variousPart2);
        variousRoundWidthDv = mTypedArray.getDimension(R.styleable.RoundProgressBar_variousRoundWidthDv, 1.8f * 3);
        displayGap = mTypedArray.getBoolean(R.styleable.RoundProgressBar_displayGap, true);
        displayOneByOne = mTypedArray.getBoolean(R.styleable.RoundProgressBar_displayOneByOne, false);
        roundProgressColorFirst = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColorFirst, Color.RED);
        roundProgressColorSecond = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColorSecond, Color.GREEN);
        roundProgressColorThird = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColorThird, Color.GREEN);
        roundProgressStyleFirst = mTypedArray.getInteger(R.styleable.RoundProgressBar_roundProgressStyleFirst, -1);
        roundProgressStyleSecond = mTypedArray.getInteger(R.styleable.RoundProgressBar_roundProgressStyleSecond, -1);
        roundProgressStyleThird = mTypedArray.getInteger(R.styleable.RoundProgressBar_roundProgressStyleThird, -1);
        roundProgressFirst = mTypedArray.getInteger(R.styleable.RoundProgressBar_roundProgressFirst, -1);
        roundProgressSecond = mTypedArray.getInteger(R.styleable.RoundProgressBar_roundProgressSecond, -1);
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);

        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centre = getWidth() / 2; // 获取圆心的x坐标

        if (variousDisplay) {
            paint.setAntiAlias(true); // 消除锯齿

            /**
             * 画分区文字
             */
            paint.setStrokeWidth(0);
            paint.setStrokeCap(Cap.ROUND);
            paint.setTextSize(textSize);

            float textHeight = paint.getFontMetricsInt().bottom - paint.getFontMetricsInt().top; // 测量字体高度，我们需要根据字体的高度设置在圆环中间
            if (textIsDisplayable && !TextUtils.isEmpty(variousPart1) && style == STROKE) {
                paint.setColor(roundColor);
                paint.setTypeface(Typeface.DEFAULT);
                float textWidth = paint.measureText(variousPart1); // 测量字体宽度，我们需要根据字体的宽度设置在圆环中间
                canvas.drawText(variousPart1, centre - textWidth / 2, TextUtils.isEmpty(variousPart2) ? centre + textHeight / 2 - paint.getFontMetricsInt().bottom : centre - textHeight - paint.getFontMetricsInt().top, paint); // 画出第一分区名字
            }

            if (textIsDisplayable && !TextUtils.isEmpty(variousPart2) && style == STROKE) {
                paint.setColor(roundProgressColor);
                paint.setTypeface(Typeface.DEFAULT_BOLD);
                float textWidth = paint.measureText(variousPart2); // 测量字体宽度，我们需要根据字体的宽度设置在圆环中间
                canvas.drawText(variousPart2, centre - textWidth / 2, centre + textHeight - paint.getFontMetricsInt().bottom, paint); // 画出第二分区名字
            }

            if (displayOneByOne) {
                int gap = displayGap ? (int) (360 * roundWidth / (Math.PI * getWidth())) - 2 : 0;

                if (0 < progress) {
                    int progressOne = progress; //第一段圆弧进度
                    if (progress > roundProgressFirst) {
                        progressOne = (int) roundProgressFirst;
                    }
                    float calculatedSweepAngle1 = ((360 - gap) * progressOne / max - 2 * gap + Math.abs((360 - gap) * progressOne / max - 2 * gap)) / 2;

                    float roundWidth1 = roundProgressStyleFirst == 1 ? roundWidth : roundWidth - variousRoundWidthDv * 2 / 3;
                    // 设置进度是实心还是空心
                    paint.setStrokeWidth(roundWidth1); // 设置圆环的宽度
                    paint.setColor(roundProgressColorFirst); // 设置进度的颜色
                    int radius2 = (int) (centre - roundWidth1 / 2); // 圆环的半径
                    RectF oval2 = new RectF(centre - radius2 + variousRoundWidthDv * 2 / 6, centre - radius2 + variousRoundWidthDv * 2 / 6, centre + radius2 - variousRoundWidthDv * 2 / 6, centre + radius2 - variousRoundWidthDv * 2 / 6); // 用于定义的圆弧的形状和大小的界限

                    switch (style) {
                        case STROKE: {
                            paint.setStyle(Paint.Style.STROKE);
                            canvas.drawArc(oval2, -90 + gap, calculatedSweepAngle1, false, paint); // 根据进度画圆弧
                            break;
                        }
                        case FILL: {
                            paint.setStyle(Paint.Style.FILL_AND_STROKE);
                            if (progress != 0)
                                canvas.drawArc(oval2, -90 + gap, calculatedSweepAngle1, true, paint); // 根据进度画圆弧
                            break;
                        }
                    }

                }
                if (roundProgressSecond != -1) {
                    if (roundProgressFirst < progress) {
                        int progressTwo = progress; //第二段圆弧进度
                        if (progress > roundProgressSecond) {
                            progressTwo = (int) roundProgressSecond;
                        }
                        float calculatedSweepAngle2 = ((360 - gap) * (progressTwo - roundProgressFirst) / max - 2 * gap + Math.abs((360 - gap) * (progressTwo - roundProgressFirst) / max - 2 * gap)) / 2;
                        float roundWidth2 = roundProgressStyleSecond == 1 ? roundWidth : roundWidth - variousRoundWidthDv * 2 / 3;
                        // 设置进度是实心还是空心
                        paint.setStrokeWidth(roundWidth2); // 设置圆环的宽度
                        paint.setColor(roundProgressColorSecond); // 设置进度的颜色
                        int radius2 = (int) (centre - roundWidth2 / 2); // 圆环的半径
                        RectF oval2 = new RectF(centre - radius2 + variousRoundWidthDv * 2 / 6, centre - radius2 + variousRoundWidthDv * 2 / 6, centre + radius2 - variousRoundWidthDv * 2 / 6, centre + radius2 - variousRoundWidthDv * 2 / 6); // 用于定义的圆弧的形状和大小的界限

                        switch (style) {
                            case STROKE: {
                                paint.setStyle(Paint.Style.STROKE);
                                canvas.drawArc(oval2, -90 + roundProgressFirst * (360 - gap) / max + gap, calculatedSweepAngle2, false, paint); // 根据进度画圆弧
                                break;
                            }
                            case FILL: {
                                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                                if (progress != 0)
                                    canvas.drawArc(oval2, -90 + roundProgressFirst * (360 - gap) / max + gap, calculatedSweepAngle2, false, paint);
                                break;
                            }
                        }
                    }
                    if (progress > roundProgressSecond) {
                        float calculatedSweepAngle3 = ((360 - gap) * (progress - roundProgressSecond) / max - 2 * gap + Math.abs((360 - gap) * (progress - roundProgressSecond) / max - 2 * gap)) / 2;
                        float roundWidth3 = roundProgressStyleThird == 1 ? roundWidth : roundWidth - variousRoundWidthDv * 2 / 3;
                        // 设置进度是实心还是空心
                        paint.setStrokeWidth(roundWidth3); // 设置圆环的宽度
                        paint.setColor(roundProgressColorThird); // 设置进度的颜色
                        int radius2 = (int) (centre - roundWidth3 / 2); // 圆环的半径
                        RectF oval2 = new RectF(centre - radius2 + variousRoundWidthDv * 2 / 6, centre - radius2 + variousRoundWidthDv * 2 / 6, centre + radius2 - variousRoundWidthDv * 2 / 6, centre + radius2 - variousRoundWidthDv * 2 / 6); // 用于定义的圆弧的形状和大小的界限

                        switch (style) {
                            case STROKE: {
                                paint.setStyle(Paint.Style.STROKE);
                                canvas.drawArc(oval2, -90 + roundProgressSecond * (360 - gap) / max + gap, calculatedSweepAngle3, false, paint); // 根据进度画圆弧
                                break;
                            }
                            case FILL: {
                                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                                if (progress != 0)
                                    canvas.drawArc(oval2, -90 + roundProgressSecond * (360 - gap) / max + gap, calculatedSweepAngle3, false, paint);
                                break;
                            }
                        }
                    }
                } else {
                    if (progress > roundProgressFirst) {
                        float calculatedSweepAngle3 = ((360 - gap) * (progress - roundProgressFirst) / max - 2 * gap + Math.abs((360 - gap) * (progress - roundProgressFirst) / max - 2 * gap)) / 2;
                        float roundWidth2 = roundProgressStyleSecond == 1 ? roundWidth : roundWidth - variousRoundWidthDv * 2 / 3;
                        // 设置进度是实心还是空心
                        paint.setStrokeWidth(roundWidth2); // 设置圆环的宽度
                        paint.setColor(roundProgressColorSecond); // 设置进度的颜色
                        int radius2 = (int) (centre - roundWidth2 / 2); // 圆环的半径
                        RectF oval2 = new RectF(centre - radius2 + variousRoundWidthDv * 2 / 6, centre - radius2 + variousRoundWidthDv * 2 / 6, centre + radius2 - variousRoundWidthDv * 2 / 6, centre + radius2 - variousRoundWidthDv * 2 / 6); // 用于定义的圆弧的形状和大小的界限

                        switch (style) {
                            case STROKE: {
                                paint.setStyle(Paint.Style.STROKE);
                                canvas.drawArc(oval2, -90 + roundProgressFirst * (360 - gap) / max + gap, calculatedSweepAngle3, false, paint); // 根据进度画圆弧
                                break;
                            }
                            case FILL: {
                                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                                if (progress != 0)
                                    canvas.drawArc(oval2, -90 + roundProgressFirst * (360 - gap) / max + gap, calculatedSweepAngle3, false, paint);
                                break;
                            }
                        }
                    }
                }
            } else {
                /**
                 * 画圆弧 ，画第二段进度（先画第二段确保第一段显示在第二段之上）
                 */

                float roundWidth2 = variousInverse ? roundWidth - variousRoundWidthDv * 2 / 3 : roundWidth;
                // 设置进度是实心还是空心
                paint.setStrokeWidth(roundWidth2); // 设置圆环的宽度
                paint.setColor(roundProgressColor); // 设置进度的颜色
                int radius2 = (int) (centre - roundWidth2 / 2); // 圆环的半径
                RectF oval2 = new RectF(centre - radius2 + variousRoundWidthDv * 2 / 6, centre - radius2 + variousRoundWidthDv * 2 / 6, centre + radius2 - variousRoundWidthDv * 2 / 6, centre + radius2 - variousRoundWidthDv * 2 / 6); // 用于定义的圆弧的形状和大小的界限

                switch (style) {
                    case STROKE: {
                        paint.setStyle(Paint.Style.STROKE);
                        canvas.drawArc(oval2, -90 + 360 * progress / max, 360 - 360 * progress / max, false, paint); // 根据进度画圆弧
                        break;
                    }
                    case FILL: {
                        paint.setStyle(Paint.Style.FILL_AND_STROKE);
                        if (progress != 0)
                            canvas.drawArc(oval2, -90 + 360 * progress / max, 360 - 360 * progress / max, true, paint); // 根据进度画圆弧
                        break;
                    }
                }

                /**
                 * 画圆弧 ，画第一段进度
                 */

                float roundWidth1 = variousInverse ? roundWidth : roundWidth - variousRoundWidthDv * 2 / 3;
                // 设置进度是实心还是空心
                paint.setStrokeWidth(roundWidth1); // 设置圆环的宽度
                paint.setColor(roundColor); // 设置进度的颜色
                int radius1 = (int) (centre - roundWidth1 / 2); // 圆环的半径
                RectF oval1 = new RectF(centre - radius1, centre - radius1, centre + radius1, centre + radius1); // 用于定义的圆弧的形状和大小的界限

                int gap = displayGap ? (360 * progress / max < 360 ? (int) (360 * roundWidth / (Math.PI * getWidth())) + 4 : 0) : 0;
                float calculatedSweepAngle1 = (360 * progress / max - 2 * gap + Math.abs(360 * progress / max - 2 * gap)) / 2;
                switch (style) {
                    case STROKE: {
                        paint.setStyle(Paint.Style.STROKE);
                        canvas.drawArc(oval1, -90 + gap, calculatedSweepAngle1, false, paint); // 根据进度画圆弧
                        break;
                    }
                    case FILL: {
                        paint.setStyle(Paint.Style.FILL_AND_STROKE);
                        if (progress != 0)
                            canvas.drawArc(oval1, -90 + gap, calculatedSweepAngle1, true, paint); // 根据进度画圆弧
                        break;
                    }
                }
            }


            return;
        }

        // ***** not various display *****

        paint.setAntiAlias(true); // 消除锯齿
        int radius = (int) (centre - roundWidth / 2); // 圆环的半径

        /**
         * 画进度百分比
         */
        paint.setStrokeWidth(0);
        paint.setStrokeCap(Cap.ROUND);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD); // 设置字体
        int percent = (int) (((float) progress / (float) max) * 100); // 中间的进度百分比，先转换成float在进行除法运算，不然都为0
        float textWidth = paint.measureText(percent + "%"); // 测量字体宽度，我们需要根据字体的宽度设置在圆环中间

        if (textIsDisplayable && percent != 0 && style == STROKE) {
            canvas.drawText(percent + "%", centre - textWidth / 2, centre + textSize / 2, paint); // 画出进度百分比
        }

        /**
         * 画最外层的大圆环
         */
        paint.setColor(roundColor); // 设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); // 设置空心
        paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        paint.setAntiAlias(true); // 消除锯齿
        canvas.drawCircle(centre, centre, radius, paint); // 画出圆环

        Log.e("log", centre + "");

        /**
         * 画圆弧 ，画圆环的进度
         */

        // 设置进度是实心还是空心
        paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        paint.setColor(roundProgressColor); // 设置进度的颜色
        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius); // 用于定义的圆弧的形状和大小的界限

        switch (style) {
            case STROKE: {
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, -90 + (anticlockwise ? (360 - 360 * progress / max) : 0), 360 * progress / max, false, paint); // 根据进度画圆弧
                break;
            }
            case FILL: {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0)
                    canvas.drawArc(oval, -90 + (anticlockwise ? (360 - 360 * progress / max) : 0), 360 * progress / max, true, paint); // 根据进度画圆弧
                break;
            }
        }

    }

    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = (max == 0 ? 100 : max);
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置分区1名字
     *
     * @param variousPart1
     */
    public synchronized void setVariousPart1(String variousPart1) {
        this.variousPart1 = variousPart1;

    }

    /**
     * 设置分区2名字
     *
     * @param variousPart2
     */
    public synchronized void setVariousPart2(String variousPart2) {
        this.variousPart2 = variousPart2;

    }

    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }

    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public int getRoundProgressColorFirst() {
        return roundProgressColorFirst;
    }

    public void setRoundProgressColorFirst(int roundProgressColorFirst) {
        this.roundProgressColorFirst = roundProgressColorFirst;
    }

    public int getRoundProgressColorSecond() {
        return roundProgressColorSecond;
    }

    public void setRoundProgressColorSecond(int roundProgressColorSecond) {
        this.roundProgressColorSecond = roundProgressColorSecond;
    }

    public int getRoundProgressColorThird() {
        return roundProgressColorThird;
    }

    public void setRoundProgressColorThird(int roundProgressColorThird) {
        this.roundProgressColorThird = roundProgressColorThird;
    }

    public float getRoundProgressFirst() {
        return roundProgressFirst;
    }

    public void setRoundProgressFirst(float roundProgressFirst) {
        this.roundProgressFirst = roundProgressFirst;
    }

    public float getRoundProgressSecond() {
        return roundProgressSecond;
    }

    public void setRoundProgressSecond(float roundProgressSecond) {
        this.roundProgressSecond = roundProgressSecond;
    }

    public int getRoundProgressStyleThird() {
        return roundProgressStyleThird;
    }

    public void setRoundProgressStyleThird(int roundProgressStyleThird) {
        this.roundProgressStyleThird = roundProgressStyleThird;
    }

    public boolean isDisplayOneByOne() {
        return displayOneByOne;
    }

    public void setDisplayOneByOne(boolean displayOneByOne) {
        this.displayOneByOne = displayOneByOne;
    }

    public int getRoundProgressStyleFirst() {
        return roundProgressStyleFirst;
    }

    public void setRoundProgressStyleFirst(int roundProgressStyleFirst) {
        this.roundProgressStyleFirst = roundProgressStyleFirst;
    }

    public int getRoundProgressStyleSecond() {
        return roundProgressStyleSecond;
    }

    public void setRoundProgressStyleSecond(int roundProgressStyleSecond) {
        this.roundProgressStyleSecond = roundProgressStyleSecond;
    }

    public void startProcess(int current, int last, long length) {
        if (last < max && last > 0) {
            if (360 * last / max < MIN_CIRCLE && last > 0) {
                last = MIN_CIRCLE * max / 360;
            } else if (360 - 360 * last / max < MIN_CIRCLE && last < 360) {
                last = (360 - MIN_CIRCLE) * max / 360;
            }
        }
        progressAnimator = ValueAnimator.ofInt(current, last);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(this);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        progressAnimator.start();

    }

    public void startProcess(int current, int length) {
        float roundProgressFirstReal = 0;
        float roundProgressSecondReal = 0;
        float roundProgressThirdReal = 0;
        if (0 < this.roundProgressFirst && this.roundProgressFirst < MIN_WEIGHT) {
            roundProgressFirstReal = MIN_WEIGHT;
        }
        if (this.roundProgressSecond == -1) {
            if (this.roundProgressFirst > (max - MIN_WEIGHT) && this.roundProgressFirst != 100) {
                roundProgressFirstReal = max - MIN_WEIGHT;
            }
        } else {
            roundProgressSecondReal = this.roundProgressSecond - this.roundProgressFirst;
            roundProgressThirdReal = max - this.roundProgressSecond;
            if (0 < roundProgressSecondReal && roundProgressSecondReal < MIN_WEIGHT) {
                roundProgressSecondReal = MIN_WEIGHT;
            }
            if (0 < roundProgressThirdReal && roundProgressThirdReal < MIN_WEIGHT) {
                roundProgressThirdReal = MIN_WEIGHT;
            }
            if (this.roundProgressFirst > roundProgressSecondReal && this.roundProgressFirst > roundProgressThirdReal) {
                if (this.roundProgressFirst > (max - roundProgressSecondReal - roundProgressThirdReal)) {
                    roundProgressFirstReal = max - roundProgressSecondReal - roundProgressThirdReal;
                }
            } else if (roundProgressSecondReal > this.roundProgressFirst && roundProgressSecondReal > roundProgressThirdReal) {
                if (roundProgressSecondReal > (max - roundProgressFirstReal - roundProgressThirdReal)) {
                    roundProgressSecondReal = max - roundProgressFirstReal - roundProgressThirdReal;
                }
            }
        }
        if (roundProgressFirstReal > 0) {
            this.roundProgressFirst = roundProgressFirstReal;
        }
        if (this.roundProgressSecond > 0) {
            this.roundProgressSecond = this.roundProgressFirst + roundProgressSecondReal;
        }

        progressAnimator = ValueAnimator.ofInt(current, 100);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(this);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        progressAnimator.start();

    }

    public void startInverseProcess(int current, int last, int length) {
        progressAnimator = ValueAnimator.ofInt(max - current, last);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(this);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        progressAnimator.start();

    }

    public void setDisplayGap(boolean displayGap) {
        this.displayGap = displayGap;
    }
}
