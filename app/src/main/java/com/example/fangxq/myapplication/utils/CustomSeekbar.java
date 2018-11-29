package com.example.fangxq.myapplication.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.example.fangxq.myapplication.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;

import static com.example.fangxq.myapplication.customview.BubbleSeekBar.TextPosition.BOTTOM;
import static com.example.fangxq.myapplication.customview.BubbleSeekBar.TextPosition.SIDES;


public class CustomSeekbar extends View {

    @IntDef({SIDES, BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextPosition {
        int SIDES = 0, BOTTOM = 1;
    }

    private int mMin; // 首值（起始值）
    private int mMax; // 尾值（结束值）
    private float mProgress; // 实时值
    private int mTrackSize; // 下层track的高度
    private int mSecondTrackSize; // 上层track的高度
    private int mThumbRadius; // thumb的半径
    private int mThumbRadiusOnDragging; // 当thumb被拖拽时的半径
    private int mSectionCount; // min到max均分的份数
    private int mThumbColor; // thumb的颜色
    private int mTrackColor; // 下层track的颜色
    private int mSecondTrackColor; // 上层track的颜色
    private boolean isShowText; // 是否显示首尾值文字
    private int mTextSize; // 首尾值文字大小
    private int mTextColor; // 首尾值文字颜色
    @TextPosition
    private int mTextPosition; // 首尾值文字位置
    private boolean isShowThumbText; // 是否显示实时值文字
    private int mThumbTextSize; // 实时值文字大小
    private int mThumbTextColor; // 实时值文字颜色
    private int mBubbleColor;// 气泡颜色
    private int mBubbleTextSize; // 气泡文字大小
    private int mBubbleTextColor; // 气泡文字颜色
    private boolean isShowSectionMark; // 是否显示份数
    private boolean isAutoAdjustSectionMark; // 是否自动滑到最近的整份数，以showSectionMark为前提
    private boolean isShowProgressInFloat; // 是否显示小数形式progress，所有小数均保留1位

    private int mDelta; // max - min
    private float mThumbCenterX; // thumb的中心X坐标
    private float mTrackLength; // track的长度
    private float mSectionOffset; // 一个section的长度
    private boolean isThumbOnDragging; // thumb是否在被拖动
    private int mTextSpace; // 文字与其他的间距
    private OnProgressChangedListener mProgressListener; // progress变化监听

    private float mLeft; // 便于理解，假设显示SectionMark，该值为首个SectionMark圆心距自己左边的距离
    private float mRight; // 同上假设，该值为最后一个SectionMark圆心距自己左边的距离
    private Paint mPaint;
    private Rect mRectText;

    private WindowManager.LayoutParams mLayoutParams;
    private int[] mPoint = new int[2];
    private long mAnimDuration = 200;
    private TextView seekbarText;

    public CustomSeekbar(Context context) {
        this(context, null);
    }

    public CustomSeekbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BubbleSeekBar, defStyleAttr, 0);
        mMin = a.getInteger(R.styleable.BubbleSeekBar_bsb_min, 0);
        mMax = a.getInteger(R.styleable.BubbleSeekBar_bsb_max, 100);
        mProgress = a.getInteger(R.styleable.BubbleSeekBar_bsb_progress, mMin);
        mTrackSize = a.getDimensionPixelSize(R.styleable.BubbleSeekBar_bsb_track_size, dp2px(2));
        mSecondTrackSize = a.getDimensionPixelSize(R.styleable.BubbleSeekBar_bsb_second_track_size,
                mTrackSize + dp2px(2));
        mThumbRadius = a.getDimensionPixelSize(R.styleable.BubbleSeekBar_bsb_thumb_radius,
                mSecondTrackSize + dp2px(2));
        mThumbRadiusOnDragging = a.getDimensionPixelSize(R.styleable.BubbleSeekBar_bsb_thumb_radius,
                mSecondTrackSize * 2);
        mSectionCount = a.getInteger(R.styleable.BubbleSeekBar_bsb_section_count, 10);
        mTrackColor = a.getColor(R.styleable.BubbleSeekBar_bsb_track_color,
                ContextCompat.getColor(context, R.color.colorPrimary));
        mSecondTrackColor = a.getColor(R.styleable.BubbleSeekBar_bsb_second_track_color,
                ContextCompat.getColor(context, R.color.colorAccent));
        mThumbColor = a.getColor(R.styleable.BubbleSeekBar_bsb_thumb_color, mSecondTrackColor);
        isShowText = a.getBoolean(R.styleable.BubbleSeekBar_bsb_show_text, false);
        mTextSize = a.getDimensionPixelSize(R.styleable.BubbleSeekBar_bsb_text_size, sp2px(14));
        mTextColor = a.getColor(R.styleable.BubbleSeekBar_bsb_text_color, mTrackColor);
        int pos = a.getInteger(R.styleable.BubbleSeekBar_bsb_text_position, 0);
        if (pos == 0) {
            mTextPosition = SIDES;
        } else if (pos == 1) {
            mTextPosition = BOTTOM;
        }
        isShowThumbText = a.getBoolean(R.styleable.BubbleSeekBar_bsb_show_thumb_text, false);
        mThumbTextSize = a.getDimensionPixelSize(R.styleable.BubbleSeekBar_bsb_thumb_text_size, sp2px(14));
        mThumbTextColor = a.getColor(R.styleable.BubbleSeekBar_bsb_thumb_text_color, mSecondTrackColor);
        mBubbleColor = a.getColor(R.styleable.BubbleSeekBar_bsb_bubble_color, mSecondTrackColor);
        mBubbleTextSize = a.getDimensionPixelSize(R.styleable.BubbleSeekBar_bsb_bubble_text_size, sp2px(14));
        mBubbleTextColor = a.getColor(R.styleable.BubbleSeekBar_bsb_bubble_text_color, Color.WHITE);
        isShowSectionMark = a.getBoolean(R.styleable.BubbleSeekBar_bsb_show_section_mark, false);
        isAutoAdjustSectionMark = a.getBoolean(R.styleable.BubbleSeekBar_bsb_auto_adjust_section_mark, false);
        isShowProgressInFloat = a.getBoolean(R.styleable.BubbleSeekBar_bsb_show_progress_in_float, false);
        int duration = a.getInteger(R.styleable.BubbleSeekBar_bsb_anim_duration, -1);
        mAnimDuration = duration < 0 ? 200 : duration;
        a.recycle();

        if (mMin > mMax) {
            int tmp = mMax;
            mMax = mMin;
            mMin = tmp;
        }
        mDelta = mMax - mMin;

        if (mProgress < mMin) {
            mProgress = mMin;
        }
        if (mProgress > mMax) {
            mProgress = mMax;
        }
        if (mSecondTrackSize < mTrackSize) {
            mSecondTrackSize = mTrackSize + dp2px(2);
        }
        if (mThumbRadius <= mSecondTrackSize) {
            mThumbRadius = mSecondTrackSize + dp2px(2);
        }
        if (mThumbRadiusOnDragging <= mSecondTrackSize) {
            mThumbRadiusOnDragging = mSecondTrackSize * 2;
        }
        if (mSectionCount <= 0) {
            mSectionCount = 10;
        }
        if (isAutoAdjustSectionMark && !isShowSectionMark) {
            isAutoAdjustSectionMark = false;
        }
        if (mSectionCount > mDelta) {
            isShowProgressInFloat = true;
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setTextAlign(Paint.Align.CENTER);

        mRectText = new Rect();

        mTextSpace = dp2px(2);

        //calculateRadiusOfBubble();
    }

    /**
     * 根据min、max计算气泡半径
     */
    private void calculateRadiusOfBubble() {
        mPaint.setTextSize(mBubbleTextSize);

        // 计算滑到两端气泡里文字需要显示的宽度，比较取最大值为气泡的半径
        String text = mMin < 0 ? "-" + mMin : "" + mMin;
        mPaint.getTextBounds(text, 0, text.length(), mRectText);
        int w1 = (mRectText.width() + mTextSpace * 2) / 2;
        if (isShowProgressInFloat) {
            text = (mMin < 0 ? "-" + mMin : mMin) + ".0";
            mPaint.getTextBounds(text, 0, text.length(), mRectText);
            w1 = (mRectText.width() + mTextSpace * 2) / 2;
        }

        text = mMax < 0 ? "-" + mMax : "" + mMax;
        mPaint.getTextBounds(text, 0, text.length(), mRectText);
        int w2 = (mRectText.width() + mTextSpace * 2) / 2;
        if (isShowProgressInFloat) {
            text = (mMax < 0 ? "-" + mMax : mMax) + ".0";
            mPaint.getTextBounds(text, 0, text.length(), mRectText);
            w2 = (mRectText.width() + mTextSpace * 2) / 2;
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = mThumbRadiusOnDragging * 2; // 默认高度为拖动时thumb圆的直径
        if (isShowThumbText) {
            mPaint.setTextSize(mThumbTextSize);
            mPaint.getTextBounds("j", 0, 1, mRectText); // “j”是字母和阿拉伯数字中最高的
            height += mRectText.height() + mTextSpace; // 如果显示实时进度，则原来基础上加上进度文字高度和间隔
        }
        if (isShowText && mTextPosition == BOTTOM) { // 如果首尾值在track之下显示，比较取较大值
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds("j", 0, 1, mRectText);
            height = Math.max(height, mThumbRadiusOnDragging * 2 + mRectText.height() + mTextSpace);
        }
        setMeasuredDimension(resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec), height);

        mLeft = getPaddingLeft() + mThumbRadiusOnDragging;
        mRight = getMeasuredWidth() - getPaddingRight() - mThumbRadiusOnDragging;

        if (isShowText) {
            mPaint.setTextSize(mTextSize);
            if (mTextPosition == SIDES) {

                String text = String.valueOf(mMin);
                mPaint.getTextBounds(text, 0, text.length(), mRectText);
                mLeft += (mRectText.width() + mTextSpace);

                text = String.valueOf(mMax);
                mPaint.getTextBounds(text, 0, text.length(), mRectText);
                mRight -= (mRectText.width() + mTextSpace);

            } else if (mTextPosition == BOTTOM) {

                String text = String.valueOf(mMin);
                mPaint.getTextBounds(text, 0, text.length(), mRectText);
                float max = Math.max(mThumbRadiusOnDragging, mRectText.width() / 2f);
                mLeft = getPaddingLeft() + max;

                text = String.valueOf(mMax);
                mPaint.getTextBounds(text, 0, text.length(), mRectText);
                max = Math.max(mThumbRadiusOnDragging, mRectText.width() / 2f);
                mRight = getMeasuredWidth() - getPaddingRight() - max;
            }
        }

        mTrackLength = mRight - mLeft;
        mSectionOffset = mTrackLength * 1f / mSectionCount;


    }

    /**
     * 气泡BubbleView实际是通过WindowManager动态添加的一个视图，因此与SeekBar唯一的位置联系就是它们在屏
     * 幕上的绝对坐标。
     * 先计算进度mProgress为零时BubbleView的中心坐标（mBubbleCenterRawSolidX，mBubbleCenterRawSolidY），
     * 然后根据进度来增量计算横坐标mBubbleCenterRawX，再动态设置LayoutParameter.x，就实现了气泡跟随滑动移动。
     * //
     */
//    private void locatePositionOnScreen() {
//        getLocationOnScreen(mPoint);
//
//        mBubbleCenterRawSolidX = mPoint[0] + mLeft - mBubbleView.getMeasuredWidth() / 2f;
//        mBubbleCenterRawX = mBubbleCenterRawSolidX + mTrackLength * (mProgress - mMin) / mDelta;
//        mBubbleCenterRawSolidY = mPoint[1] - mBubbleView.getMeasuredHeight();
//        if (!BuildUtils.isMIUI()) {
//            mBubbleCenterRawSolidY -= dp2px(24);
//        }
//    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float x1 = getPaddingLeft();
        float x2 = getWidth() - getPaddingRight();
        float y = getPaddingTop() + mThumbRadiusOnDragging;

        if (isShowText) {
            mPaint.setTextSize(mTextSize);
            mPaint.setColor(mTextColor);

            // 画首尾值文字
            if (mTextPosition == SIDES) {
                float y_ = y + mRectText.height() / 2f;

                String text = String.valueOf(mMin);
                mPaint.getTextBounds(text, 0, text.length(), mRectText);
                canvas.drawText(text, x1 + mRectText.width() / 2f, y_, mPaint);
                x1 += mRectText.width() + mTextSpace;

                text = String.valueOf(mMax);
                mPaint.getTextBounds(text, 0, text.length(), mRectText);
                canvas.drawText(text, x2 - mRectText.width() / 2f, y_, mPaint);
                x2 -= (mRectText.width() + mTextSpace);

            } else if (mTextPosition == BOTTOM) {
                float y_ = y + mThumbRadiusOnDragging + mTextSpace;

                String text = String.valueOf(mMin);
                mPaint.getTextBounds(text, 0, text.length(), mRectText);
                y_ += mRectText.height();
                x1 = mLeft;
                canvas.drawText(text, x1, y_, mPaint);

                text = String.valueOf(mMax);
                mPaint.getTextBounds(text, 0, text.length(), mRectText);
                x2 = mRight;
                canvas.drawText(text, x2, y_, mPaint);
            }
        }

        if (!isShowText || mTextPosition != BOTTOM) {
            x1 += mThumbRadiusOnDragging;
            x2 -= mThumbRadiusOnDragging;
        }

        if (!isThumbOnDragging) {
            mThumbCenterX = mTrackLength / mDelta * (mProgress - mMin) + x1;
        }

        if (isShowThumbText && !isThumbOnDragging) {
            // 排除显示小数实时值、首尾文字在Bottom时，滑到首尾的情况（不会与首尾文字重合，故索性不显示）
            if (mTextPosition == SIDES || !isShowProgressInFloat ||
                    ((int) mProgress != mMin && (int) mProgress != mMax)) {

                mPaint.setTextSize(mThumbTextSize);
                mPaint.setColor(mThumbTextColor);

                if (isShowProgressInFloat) {
                    mPaint.getTextBounds(String.valueOf(getProgressInFloat()), 0,
                            String.valueOf(getProgressInFloat()).length(), mRectText);
                    float y_ = y + mRectText.height() + mThumbRadiusOnDragging + mTextSpace;
                    canvas.drawText(String.valueOf(getProgressInFloat()), mThumbCenterX, y_, mPaint);
                } else {
                    mPaint.getTextBounds(String.valueOf(getProgress()), 0,
                            String.valueOf(getProgress()).length(), mRectText);
                    float y_ = y + mRectText.height() + mThumbRadiusOnDragging + mTextSpace;
                    canvas.drawText(String.valueOf(getProgress()), mThumbCenterX, y_, mPaint);
                }
            }
        }

        if (isShowSectionMark) {
//            // 画分段标识点
//            float r = (mThumbRadiusOnDragging - dp2px(2)) / 2f;
//            float junction = mTrackLength / mDelta * Math.abs(mProgress - mMin) + mLeft; // 交汇点
//            for (int i = 0; i <= mSectionCount; i++) {
//                if (x1 + i * mSectionOffset <= junction) {
//                    mPaint.setColor(mSecondTrackColor);
//                } else {
//                    mPaint.setColor(mTrackColor);
//                }
//                canvas.drawCircle(x1 + i * mSectionOffset, y, r, mPaint);
//            }
        }

        // 画下层track
        mPaint.setColor(mSecondTrackColor);
        mPaint.setStrokeWidth(mSecondTrackSize);
        canvas.drawLine(x1, y, mThumbCenterX, y, mPaint);

        // 画上层track
        mPaint.setColor(mTrackColor);
        mPaint.setStrokeWidth(mTrackSize);
        canvas.drawLine(mThumbCenterX, y, x2, y, mPaint);

        // 画thumb
        mPaint.setColor(mThumbColor);
        canvas.drawCircle(mThumbCenterX, y, isThumbOnDragging ? mThumbRadiusOnDragging :
                mThumbRadius, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        });
    }

    float dx;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                isThumbOnDragging = isThumbTouched(event);
                if (isThumbOnDragging) {
                    mProgressListener.onProgressChanged(getProgress());
                    invalidate();
                }
                dx = mThumbCenterX - event.getX();

                break;
            case MotionEvent.ACTION_MOVE:
                if (isThumbOnDragging) {
                    mThumbCenterX = event.getX() + dx;
                    if (mThumbCenterX < mLeft) {
                        mThumbCenterX = mLeft;
                    }
                    if (mThumbCenterX > mRight) {
                        mThumbCenterX = mRight;
                    }
                    mProgress = (mThumbCenterX - mLeft) * mDelta / mTrackLength + mMin;

                    mProgressListener.onProgressChanged(getProgress());

                    invalidate();

                    if (mProgressListener != null) {
                        mProgressListener.onProgressChanged(getProgress());
                        mProgressListener.onProgressChanged(getProgressInFloat());
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isAutoAdjustSectionMark) {
                    autoAdjustSection();
                } else if (isThumbOnDragging) {
                    mProgressListener.onProgressChanged(getProgress());

                    if (mProgressListener != null) {
                        mProgressListener.getProgressOnActionUp(getProgress());
                        mProgressListener.getProgressOnActionUp(getProgressInFloat());
                    }
                }

                break;
        }

        return isThumbOnDragging || super.onTouchEvent(event);
    }

    /**
     * 识别thumb是否被有效点击
     */
    private boolean isThumbTouched(MotionEvent event) {
        float x = mTrackLength / mDelta * (mProgress - mMin) + mLeft;
        float y = getHeight() / 2f;
        return (event.getX() - x) * (event.getX() - x) + (event.getY() - y) * (event.getY() - y)
                <= (mLeft + dp2px(8)) * (mLeft + dp2px(8));
    }

    /**
     * 显示气泡
     * 原理是利用WindowManager动态添加一个与Toast相同类型的BubbleView，消失时再移除
     */
//    private void showBubble() {
//        if (mBubbleView.getParent() != null) {
//            return;
//        }
//
//        if (mLayoutParams == null) {
//            mLayoutParams = new WindowManager.LayoutParams();
//            mLayoutParams.gravity = Gravity.START | Gravity.TOP;
//            mLayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
//            mLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            mLayoutParams.format = PixelFormat.TRANSLUCENT;
//            mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
//            if (BuildUtils.isMIUI()) { // MIUI禁止了开发者使用TYPE_TOAST
//                mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
//            } else {
//                mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
//            }
//        }
//        mLayoutParams.x = (int) (mBubbleCenterRawX + 0.5f);
//        mLayoutParams.y = (int) (mBubbleCenterRawSolidY + 0.5f);
//
//        mBubbleView.setAlpha(0);
//        mBubbleView.setVisibility(VISIBLE);
//        mBubbleView.animate().alpha(1f).setDuration(mAnimDuration)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//                        mWindowManager.addView(mBubbleView, mLayoutParams);
//                    }
//                }).start();
//    }

    /**
     * 自动滚向最近的分段处
     */
    private void autoAdjustSection() {
        int i;
        float x = 0;
        for (i = 0; i <= mSectionCount; i++) {
            x = i * mSectionOffset + mLeft;
            if (x <= mThumbCenterX && mThumbCenterX - x <= mSectionOffset) {
                break;
            }
        }

        BigDecimal bigDecimal = BigDecimal.valueOf(mThumbCenterX);
        float x_ = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        boolean onSection = x_ == x; // 就在section处，不作valueAnim，优化性能

        AnimatorSet animatorSet = new AnimatorSet();

        ValueAnimator valueAnim = null;
        if (!onSection) {
            if (mThumbCenterX - x <= mSectionOffset / 2f) {
                valueAnim = ValueAnimator.ofFloat(mThumbCenterX, x);
            } else {
                valueAnim = ValueAnimator.ofFloat(mThumbCenterX, (i + 1) * mSectionOffset + mLeft);
            }
            valueAnim.setInterpolator(new LinearInterpolator());
            valueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mThumbCenterX = (float) animation.getAnimatedValue();
                    mProgress = (mThumbCenterX - mLeft) * mDelta / mTrackLength + mMin;

                    mProgressListener.onProgressChanged(getProgress());

                    invalidate();

                    if (mProgressListener != null) {
                        mProgressListener.onProgressChanged(getProgress());
                        mProgressListener.onProgressChanged(getProgressInFloat());
                    }
                }
            });
        }

        //todo
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(seekbarText, View.ALPHA, 1);

        if (onSection) {
            animatorSet.setDuration(mAnimDuration).play(alphaAnim);
        } else {
            animatorSet.setDuration(mAnimDuration).playTogether(valueAnim, alphaAnim);
        }
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                mProgress = (mThumbCenterX - mLeft) * mDelta / mTrackLength + mMin;
                isThumbOnDragging = false;
                mProgressListener.onProgressChanged(getProgress());
                invalidate();
                if (mProgressListener != null) {
                    mProgressListener.getProgressOnFinally(getProgress());
                    mProgressListener.getProgressOnFinally(getProgressInFloat());
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mProgressListener.onProgressChanged(getProgress());
                mProgress = (mThumbCenterX - mLeft) * mDelta / mTrackLength + mMin;
                isThumbOnDragging = false;
                invalidate();
            }
        });
        animatorSet.start();
    }

    /**
     * 隐藏气泡
     */
//    private void hideBubble() {
//        mBubbleView.setVisibility(GONE); // 防闪烁
//        if (mBubbleView.getParent() != null) {
//            mWindowManager.removeViewImmediate(mBubbleView);
//        }
//    }
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("save_instance", super.onSaveInstanceState());
        bundle.putFloat("progress", mProgress);

        return bundle;
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                Resources.getSystem().getDisplayMetrics());
    }

    public int getMin() {
        return mMin;
    }

    public void setMin(int min) {
        if (mMin == min || min > mMax) {
            return;
        }

        mMin = min;
        mDelta = mMax - mMin;

        calculateRadiusOfBubble();

        postInvalidate();
    }

    public int getMax() {
        return mMax;
    }

    public void setMax(int max) {
        if (mMax == max || max < mMin) {
            return;
        }

        mMax = max;
        mDelta = mMax - mMin;

        if (mProgress > mMax) {
            mProgress = mMax;
        }
        calculateRadiusOfBubble();

        postInvalidate();
    }

    public int getProgress() {
        return Math.round(mProgress);
    }

    public float getProgressInFloat() {
        BigDecimal bigDecimal = BigDecimal.valueOf(mProgress);
        return bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public void setProgress(int progress) {
        setProgress(progress + 0.0f);
    }

    public void setProgress(float progress) {
        if (mProgress == progress || progress < mMin || progress > mMax) {
            return;
        }

        mProgress = progress;

        if (mProgressListener != null) {
            mProgressListener.onProgressChanged(getProgress());
            mProgressListener.onProgressChanged(getProgressInFloat());
            mProgressListener.getProgressOnFinally(getProgress());
            mProgressListener.getProgressOnFinally(getProgressInFloat());
        }

        postInvalidate();
    }

    public int getTrackSize() {
        return mTrackSize;
    }

    public void setTrackSize(int trackSize) {
        if (mTrackSize != trackSize) {
            mTrackSize = trackSize;
            if (mSecondTrackSize <= mTrackSize) {
                mSecondTrackSize = mTrackSize + dp2px(2);
            }
            if (mThumbRadius <= mSecondTrackSize) {
                mThumbRadius = mSecondTrackSize + dp2px(2);
            }
            if (mThumbRadiusOnDragging <= mSecondTrackSize) {
                mThumbRadiusOnDragging = mSecondTrackSize * 2;
            }

            postInvalidate();
        }
    }

    public int getSecondTrackSize() {
        return mSecondTrackSize;
    }

    public void setSecondTrackSize(int secondTrackSize) {
        if (mSecondTrackSize != secondTrackSize && secondTrackSize >= mTrackSize) {
            mSecondTrackSize = secondTrackSize;
            if (mThumbRadius <= mSecondTrackSize) {
                mThumbRadius = mSecondTrackSize + dp2px(2);
            }
            if (mThumbRadiusOnDragging <= mSecondTrackSize) {
                mThumbRadiusOnDragging = mSecondTrackSize * 2;
                requestLayout();
                return;
            }

            postInvalidate();
        }
    }

    public int getThumbRadius() {
        return mThumbRadius;
    }

    public void setThumbRadius(int thumbRadius) {
        if (mThumbRadius != thumbRadius) {
            mThumbRadius = thumbRadius;
            if (mThumbRadius <= mSecondTrackSize) {
                mThumbRadius = mSecondTrackSize + dp2px(2);
            }
            if (mThumbRadiusOnDragging <= mSecondTrackSize) {
                mThumbRadiusOnDragging = mSecondTrackSize * 2;
                requestLayout();
                return;
            }

            postInvalidate();
        }
    }

    public int getThumbRadiusOnDragging() {
        return mThumbRadiusOnDragging;
    }

    public void setThumbRadiusOnDragging(int thumbRadiusOnDragging) {
        if (mThumbRadiusOnDragging != thumbRadiusOnDragging) {
            mThumbRadiusOnDragging = thumbRadiusOnDragging;
            if (mThumbRadiusOnDragging <= mSecondTrackSize) {
                mThumbRadiusOnDragging = mSecondTrackSize * 2;
            }

            requestLayout();
        }
    }

    public int getSectionCount() {
        return mSectionCount;
    }

    public void setSectionCount(int sectionCount) {
        if (mSectionCount != sectionCount) {
            mSectionCount = sectionCount;
            if (mSectionCount <= 0 || mSectionCount > mMax - mMin) {
                mSectionCount = 10;
            }
            if (mSectionCount > mDelta) {
                isShowProgressInFloat = true;

                calculateRadiusOfBubble();
            }

            requestLayout();
        }
    }

    public int getTrackColor() {
        return mTrackColor;
    }

    public void setTrackColor(int trackColor) {
        if (mTrackColor != trackColor) {
            mTrackColor = trackColor;
            postInvalidate();
        }
    }

    public int getSecondTrackColor() {
        return mSecondTrackColor;
    }

    public void setSecondTrackColor(int secondTrackColor) {
        if (mSecondTrackColor != secondTrackColor) {
            mSecondTrackColor = secondTrackColor;
            postInvalidate();
        }
    }

    public boolean isShowText() {
        return isShowText;
    }

    public void setShowText(boolean showText) {
        if (isShowText != showText) {
            isShowText = showText;
            requestLayout();
        }
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        if (mTextSize != textSize) {
            mTextSize = textSize;
            requestLayout();
        }
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        if (mTextColor != textColor) {
            mTextColor = textColor;
            postInvalidate();
        }
    }

    public int getTextPosition() {
        return mTextPosition;
    }

    public void setTextPosition(@TextPosition int textPosition) {
        if (mTextPosition != textPosition) {
            mTextPosition = textPosition;
            requestLayout();
        }
    }

    public boolean isShowThumbText() {
        return isShowThumbText;
    }

    public void setShowThumbText(boolean showThumbText) {
        if (isShowThumbText != showThumbText) {
            isShowThumbText = showThumbText;
            requestLayout();
        }
    }

    public int getThumbTextSize() {
        return mThumbTextSize;
    }

    public void setThumbTextSize(int thumbTextSize) {
        if (mThumbTextSize != thumbTextSize) {
            mThumbTextSize = thumbTextSize;
            requestLayout();
        }
    }

    public int getThumbTextColor() {
        return mThumbTextColor;
    }

    public void setThumbTextColor(int thumbTextColor) {
        if (mThumbTextColor != thumbTextColor) {
            mThumbTextColor = thumbTextColor;
            postInvalidate();
        }
    }

    public boolean isShowProgressInFloat() {
        return isShowProgressInFloat;
    }

    public void setShowProgressInFloat(boolean showProgressInFloat) {
        if (mSectionCount > mDelta) {
            isShowProgressInFloat = true;
            return;
        }

        if (isShowProgressInFloat != showProgressInFloat) {
            isShowProgressInFloat = showProgressInFloat;

            calculateRadiusOfBubble();

            postInvalidate();
        }
    }


    public boolean isShowSectionMark() {
        return isShowSectionMark;
    }

    public void setShowSectionMark(boolean showSectionMark) {
        if (isShowSectionMark != showSectionMark) {
            isShowSectionMark = showSectionMark;

            if (isAutoAdjustSectionMark && !isShowSectionMark) {
                isAutoAdjustSectionMark = false;
            }
            postInvalidate();
        }
    }

    public boolean isAutoAdjustSectionMark() {
        return isAutoAdjustSectionMark;
    }

    public void setAutoAdjustSectionMark(boolean autoAdjustSectionMark) {
        if (isAutoAdjustSectionMark != autoAdjustSectionMark) {
            isAutoAdjustSectionMark = autoAdjustSectionMark;

            if (isAutoAdjustSectionMark && !isShowSectionMark) {
                isAutoAdjustSectionMark = false;
            }
            postInvalidate();
        }
    }

    public void setText(TextView seekbarText) {
        this.seekbarText = seekbarText;
    }

    public OnProgressChangedListener getOnProgressChangedListener() {
        return mProgressListener;
    }

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        mProgressListener = onProgressChangedListener;
    }

    /**
     * progress改变监听器
     */
    public interface OnProgressChangedListener {

        void onProgressChanged(int progress);

        void onProgressChanged(float progress);

        void getProgressOnActionUp(int progress);

        void getProgressOnActionUp(float progress);

        void getProgressOnFinally(int progress);

        void getProgressOnFinally(float progress);
    }

    /**
     * progress改变监听
     * <br/>
     * 用法同{@link AnimatorListenerAdapter}
     */
    public static class OnProgressChangedListenerAdapter implements OnProgressChangedListener {

        @Override
        public void onProgressChanged(int progress) {
        }

        @Override
        public void onProgressChanged(float progress) {
        }

        @Override
        public void getProgressOnActionUp(int progress) {
        }

        @Override
        public void getProgressOnActionUp(float progress) {
        }

        @Override
        public void getProgressOnFinally(int progress) {
        }

        @Override
        public void getProgressOnFinally(float progress) {
        }
    }


}
