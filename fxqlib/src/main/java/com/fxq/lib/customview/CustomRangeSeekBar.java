package com.fxq.lib.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.fxq.lib.utils.Utils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import gradleplugin.fxq.com.fxqlib.R;

/**
 * Created by Fangxq on 2017/6/2.
 */
public class CustomRangeSeekBar extends View {

    private float mMin; // 首值（起始值）
    private float mMax; // 尾值（结束值）
    private float mProgress; // 实时值
    private int mTrackSize; // 下层track的高度
    private int mSecondTrackSize; // 上层track的高度
    private int mThumbRadius; // thumb的半径
    private int mThumbRadiusOnDragging; // 当thumb被拖拽时的半径
    private int mSectionRadius; // 节点半径
    private int mSectionCount; // min到max均分的份数
    private int mThumbColor; // thumb的颜色
    private int mTrackColor; // 下层track的颜色
    private int mSecondTrackColor; // 上层track的颜色

    private int mSectionTextSize; // 刻度值文字大小
    private int mSectionTextColor; // 刻度值文字颜色
    private boolean isShowSectionMark; // 是否显示份数节点
    private boolean isShowAllSectionValue; // 是否显示刻度值
    private boolean isAutoAdjustSectionMark; // 是否自动滑到最近的整份数，以showSectionMark为前提
    private int mSeparationSection;

    private float mDelta; // max - min
    private float mThumbCenterX; // thumb的中心X坐标
    private float mTrackLength; // track的长度
    private float mSectionOffset; // 一个section的长度
    private boolean isThumbOnDragging; // thumb是否在被拖动
    private int mTextSpace; // 文字与其他的间距

    private OnProgressChangedListener mProgressListener;
    private OnActionUpOrCancelListener onActionUpOrCancelListener;
    private float mLeft; // 便于理解，假设显示SectionMark，该值为首个SectionMark圆心距自己左边的距离
    private float mRight; // 同上假设，该值为最后一个SectionMark圆心距自己左边的距离
    private Paint mPaint;
    private Rect mRectText;
    private long mAnimDuration = 100;
    private int mRectTextHeight = -1;
    private List<String> mSectionList = new ArrayList<String>();
    private CharSequence[] mSections;
    private String mStartMark;
    private String mEndMark;
    private boolean showText;
    private boolean enabled;

    public CustomRangeSeekBar(Context context) {
        this(context, null);
    }

    public CustomRangeSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRangeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomRangeSeekBar, defStyleAttr, 0);
        mMin = a.getFloat(R.styleable.CustomRangeSeekBar_csb_min, 0);
        mMax = a.getFloat(R.styleable.CustomRangeSeekBar_csb_max, 100);
        mProgress = a.getFloat(R.styleable.CustomRangeSeekBar_csb_progress, mMin);
        mTrackSize = a.getDimensionPixelSize(R.styleable.CustomRangeSeekBar_csb_track_size, Utils.dip2px(context, 3));
        mSecondTrackSize = a.getDimensionPixelSize(R.styleable.CustomRangeSeekBar_csb_second_track_size, Utils.dip2px(context, 3));
        mThumbRadius = a.getDimensionPixelSize(R.styleable.CustomRangeSeekBar_csb_thumb_radius, mSecondTrackSize * 2);
        mThumbRadiusOnDragging = a.getDimensionPixelSize(R.styleable.CustomRangeSeekBar_csb_thumb_radius, mSecondTrackSize * 2);
        mSectionRadius = a.getDimensionPixelSize(R.styleable.CustomRangeSeekBar_csb_section_radius, mSecondTrackSize * 2);
        mSectionCount = a.getInteger(R.styleable.CustomRangeSeekBar_csb_section_count, 5);
        mTrackColor = a.getColor(R.styleable.CustomRangeSeekBar_csb_track_color, ContextCompat.getColor(context, R.color.b3));
        mSecondTrackColor = a.getColor(R.styleable.CustomRangeSeekBar_csb_second_track_color, ContextCompat.getColor(context, R.color.a1));
        mThumbColor = a.getColor(R.styleable.CustomRangeSeekBar_csb_thumb_color, mSecondTrackColor);
        mSectionTextSize = a.getDimensionPixelSize(R.styleable.CustomRangeSeekBar_csb_text_size, Utils.dip2px(context, 14));
        mSectionTextColor = a.getColor(R.styleable.CustomRangeSeekBar_csb_text_color, mTrackColor);
        isShowSectionMark = a.getBoolean(R.styleable.CustomRangeSeekBar_csb_show_section_mark, false);
        isShowAllSectionValue = a.getBoolean(R.styleable.CustomRangeSeekBar_csb_show_all_section_value, false);
        isAutoAdjustSectionMark = a.getBoolean(R.styleable.CustomRangeSeekBar_csb_auto_adjust_section_mark, false);
        mSections = a.getTextArray(R.styleable.CustomRangeSeekBar_csb_section_list);
        mSeparationSection = a.getInteger(R.styleable.CustomRangeSeekBar_csb_separation_section, -1);
        showText = a.getBoolean(R.styleable.CustomRangeSeekBar_csb_show_text, true);
        enabled = a.getBoolean(R.styleable.CustomRangeSeekBar_csb_enabled, true);
        if (mSections != null) {
            for (int i = 0; i < mSections.length; i++) {
                mSectionList.add(mSections[i].toString());
            }
        }
        a.recycle();

        if (mMin > mMax) {
            int tmp = (int) mMax;
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
            mSecondTrackSize = mTrackSize + Utils.dip2px(context, 2);
        }
        if (mThumbRadius <= mSecondTrackSize) {
            mThumbRadius = mSecondTrackSize + Utils.dip2px(context, 2);
        }
        if (mThumbRadiusOnDragging <= mSecondTrackSize) {
            mThumbRadiusOnDragging = mSecondTrackSize * 2;
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setTextAlign(Paint.Align.CENTER);

        mRectText = new Rect();
        mTextSpace = Utils.dip2px(context, 5);
        mSectionCount = mSectionList.isEmpty() ? mSectionCount : mSectionList.size() - 1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!mSectionList.isEmpty()) {
            mSectionCount = mSectionList.size() - 1;
        }
        int height = mThumbRadiusOnDragging * 2; // 默认高度为拖动时thumb圆的直径

        mPaint.setTextSize(mSectionTextSize);
        mPaint.getTextBounds("月", 0, 1, mRectText);
        height = Math.max(height, mThumbRadiusOnDragging * 2 + mRectText.height() * 2 + mTextSpace);
        setMeasuredDimension(resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec), height);


        String text = String.valueOf(mMin);
        mPaint.getTextBounds(text, 0, text.length(), mRectText);
        float max = mThumbRadiusOnDragging;
        mLeft = getPaddingLeft() + max;

        text = String.valueOf(mMax);
        mPaint.getTextBounds(text, 0, text.length(), mRectText);
        mRight = getMeasuredWidth() - getPaddingRight() - max;

        mTrackLength = mRight - mLeft;
        mSectionOffset = mTrackLength * 1f / mSectionCount;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mSectionList.isEmpty()) {
            mSectionCount = mSectionList.size() - 1;
        }
        float x1 = mLeft;
        float x2 = mRight;
        float y = getPaddingTop() + mThumbRadiusOnDragging;
        mPaint.setTextSize(mSectionTextSize);
        mPaint.setColor(mSectionTextColor);
        float y_ = y + mThumbRadiusOnDragging + mTextSpace;
        if (!mSectionList.isEmpty() && isShowAllSectionValue && showText) {
            // 画所有底部刻度值
            y_ += mRectText.height();
            for (int i = 0; i <= mSectionCount; i++) {
                String text = mSectionList.get(i).toString();
                mPaint.getTextBounds(text, 0, text.length(), mRectText);
                if (i == 0) {
                    canvas.drawText(text, x1 + i * mSectionOffset + mRectText.width() / 2, y_, mPaint);
                } else if (i == mSectionCount) {
                    canvas.drawText(text, x1 + i * mSectionOffset - mRectText.width() / 2, y_, mPaint);
                } else {
                    canvas.drawText(text, x1 + i * mSectionOffset, y_, mPaint);
                }
            }
        } else if (showText) {
            //画收尾刻度值文字
            if (StringUtils.isNotEmpty(mStartMark) && StringUtils.isNotEmpty(mEndMark)) {
                mPaint.getTextBounds(mStartMark, 0, mStartMark.length(), mRectText);
                y_ += mRectText.height();
                canvas.drawText(mStartMark, x1 + mRectText.width() / 2, y_, mPaint);

                mPaint.getTextBounds(mEndMark, 0, mEndMark.length(), mRectText);
                canvas.drawText(mEndMark, x2 - mRectText.width() / 2, y_, mPaint);

            } else if (mSectionList.size() >= 2) {
                String text = mSectionList.get(0).toString();
                mPaint.getTextBounds(text, 0, text.length(), mRectText);
                y_ += mRectText.height();
                canvas.drawText(text, x1 + mRectText.width() / 2, y_, mPaint);

                text = mSectionList.get(mSectionCount).toString();
                mPaint.getTextBounds(text, 0, text.length(), mRectText);
                canvas.drawText(text, x2 - mRectText.width() / 2, y_, mPaint);
            } else {
                String text = String.valueOf(mMin);
                mPaint.getTextBounds(text, 0, text.length(), mRectText);
                y_ += mRectText.height();
                canvas.drawText(text, x1 + mRectText.width() / 2, y_, mPaint);

                text = String.valueOf(mMax);
                mPaint.getTextBounds(text, 0, text.length(), mRectText);
                canvas.drawText(text, x2 - mRectText.width() / 2, y_, mPaint);
            }
        }


        if (!isThumbOnDragging) {
            mThumbCenterX = mTrackLength / mDelta * (mProgress - mMin) + x1;
        }
        // 画上层track
        mPaint.setColor(mSecondTrackColor);
        mPaint.setStrokeWidth(mSecondTrackSize);
        canvas.drawLine(x1, y, mThumbCenterX, y, mPaint);

        // 画下层track
        mPaint.setColor(mTrackColor);
        mPaint.setStrokeWidth(mTrackSize);
        if (isSeparationShow()) {
            canvas.drawLine(mThumbCenterX, y, x2 - (mSectionCount - mSeparationSection) * mSectionOffset, y, mPaint);
        } else {
            canvas.drawLine(mThumbCenterX, y, x2, y, mPaint);
        }

        if (isShowSectionMark) {
            // 画分段标识点
            float r = mSectionRadius;
            float junction = mTrackLength / mDelta * Math.abs(mProgress - mMin) + mLeft; // 交汇点
            for (int i = 0; i <= mSectionCount; i++) {

                if (x1 + i * mSectionOffset <= junction) {
                    mPaint.setColor(mSecondTrackColor);
                } else {
                    mPaint.setColor(mTrackColor);
                }
                canvas.drawCircle(x1 + i * mSectionOffset, y, r, mPaint);
            }
        } else {
            float r = mSectionRadius;
            float junction = mTrackLength / mDelta * Math.abs(mProgress - mMin) + mLeft;
            for (int i = 0; i <= mSectionCount; i++) {
                if (i == 0 || i == mSectionCount || (isSeparationShow() && i == mSeparationSection)) {
                    if (x1 + i * mSectionOffset <= junction) {
                        mPaint.setColor(mSecondTrackColor);
                    } else {
                        mPaint.setColor(mTrackColor);
                    }
                    canvas.drawCircle(x1 + i * mSectionOffset, y, r, mPaint);
                }
            }

        }

        if (isSeparationShow()) {
            mPaint.setColor(mTrackColor);
            mPaint.setStrokeWidth(mTrackSize);
            int startX = (int) (x2 - (mSectionCount - mSeparationSection) * mSectionOffset);
            int pathEffectWidth = Utils.dip2px(getContext(), 6);
            int dottedLineWidth = Utils.dip2px(getContext(), 2);
            for (int i = startX; i <= x2; i = i + pathEffectWidth) {
                canvas.drawLine(i, y, i + dottedLineWidth, y, mPaint);
            }
        }

        // 画thumb
        mPaint.setColor(mThumbColor);
        canvas.drawCircle(mThumbCenterX, y, isThumbOnDragging ? mThumbRadiusOnDragging :
                mThumbRadius, mPaint);
    }

    private boolean isSeparationShow() {
        if (mSeparationSection > 0 && mSeparationSection < mSectionCount) {
            return true;
        }
        return false;
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

    private float dx;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!enabled) {
            return super.onTouchEvent(event);
        } else {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    isThumbOnDragging = isThumbTouched(event);
                    if (isThumbOnDragging) {
                        if (mProgressListener != null) {
                            int position = -1;
                            if (!mSectionList.isEmpty()) {
                                position = Math.round(getProgressInFloat() * mSectionCount / 100);
                            }
                            mProgressListener.onProgressChanged(getProgressInFloat(), position, mThumbCenterX, mLeft, mRight, false, false);
                        }
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
                        if (isSeparationShow() && mThumbCenterX > mRight - (mSectionCount - mSeparationSection) * mSectionOffset) {
                            mThumbCenterX = mRight - (mSectionCount - mSeparationSection) * mSectionOffset;
                        }
                        mProgress = (mThumbCenterX - mLeft) * mDelta / mTrackLength + mMin;
                        if (mProgressListener != null) {
                            int position = -1;
                            if (!mSectionList.isEmpty()) {
                                position = Math.round(getProgressInFloat() * mSectionCount / 100);
                            }
                            mProgressListener.onProgressChanged(getProgressInFloat(), position, mThumbCenterX, mLeft, mRight, false, false);
                        }

                        invalidate();
                    }

                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (isAutoAdjustSectionMark) {
                        autoAdjustSection();
                    } else if (isThumbOnDragging) {
                        if (mProgressListener != null) {
                            int position = -1;
                            if (!mSectionList.isEmpty()) {
                                position = Math.round(getProgressInFloat() * mSectionCount / 100);
                            }
                            mProgressListener.onProgressChanged(getProgressInFloat(), position, mThumbCenterX, mLeft, mRight, false, false);
                        }
                    }
                    if (onActionUpOrCancelListener != null) {
                        onActionUpOrCancelListener.onActionUpOrCancel();
                    }
                    break;
            }

            return isThumbOnDragging || super.onTouchEvent(event);
        }
    }

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


        ValueAnimator valueAnim = null;
        if (!onSection || mSectionCount >= 2) {
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
                    if (mProgressListener != null) {
                        int position = -1;
                        if (!mSectionList.isEmpty()) {
                            position = Math.round(getProgressInFloat() * mSectionCount / 100);
                        }
                        mProgressListener.onProgressChanged(getProgressInFloat(), position, mThumbCenterX, mLeft, mRight, false, false);
                    }
                    invalidate();
                }
            });
            if (valueAnim != null) {
                valueAnim.setDuration(mAnimDuration);
                valueAnim.start();
                valueAnim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (mProgressListener != null) {
                            int position = -1;
                            if (!mSectionList.isEmpty()) {
                                position = Math.round(getProgressInFloat() * mSectionCount / 100);
                            }
                            final int finalPosition = position;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressListener.onProgressChanged(getProgressInFloat(), finalPosition, mThumbCenterX, mLeft, mRight, false, true);
                                }
                            }, 10);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
            }

        }
    }

    /**
     * 识别thumb是否被有效点击
     */
    private boolean isThumbTouched(MotionEvent event) {
        float x = mTrackLength / mDelta * (mProgress - mMin) + mLeft;
        float y = getHeight() / 2f;
        return (event.getX() - x) * (event.getX() - x) + (event.getY() - y) * (event.getY() - y)
                <= (mLeft + Utils.dip2px(getContext(), 8)) * (mLeft + Utils.dip2px(getContext(), 8));
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

    public void setProgressSectionPosition(float section, float sectionCount) {
        if (section <= sectionCount && sectionCount > 0) {
            setProgress(section / sectionCount * 100);
        } else {
            setProgress(0.0f);
        }
    }

    public void setProgress(float progress) {
        if (progress < mMin || progress > mMax) {
            return;
        }

        mProgress = progress;
        postInvalidate();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mProgressListener != null) {
                    int position = -1;
                    if (!mSectionList.isEmpty()) {
                        position = Math.round(getProgressInFloat() * mSectionCount / 100);
                    }
                    mProgressListener.onProgressChanged(getProgressInFloat(), position, mThumbCenterX, mLeft, mRight, true, false);

                }
            }
        }, 40);
    }

    public List<String> getSectionList() {
        return mSectionList;
    }

    public void setSectionList(List<String> mSectionList) {
        this.mSectionList = mSectionList;
        if (!mSectionList.isEmpty()) {
            mSectionCount = mSectionList.size() - 1;
        }
        postInvalidate();
    }

    public int getSeparationSection() {
        return mSeparationSection;
    }

    public void setSeparationSectionPosition(int mSeparationSection) {
        this.mSeparationSection = mSeparationSection;
    }

    public long getAnimDuration() {
        return mAnimDuration;
    }

    public void setAnimDuration(long mAnimDuration) {
        this.mAnimDuration = mAnimDuration;
    }

    public void setMark(String mStartMark, String mEndMark) {
        this.mStartMark = mStartMark;
        this.mEndMark = mEndMark;
        postInvalidate();
    }

    public void setMinMax(float mMin, float mMax) {
        this.mMin = mMin;
        this.mMax = mMax;
        mDelta = mMax - mMin;
        postInvalidate();
    }

    public void setmSecondTrackColor(int color) {
        this.mSecondTrackColor = color;
        postInvalidate();
    }

    public void setmThumbColor(int color) {
        this.mThumbColor = color;
        postInvalidate();
    }

    public void setSectionCount(int mSectionCount) {
        this.mSectionCount = mSectionCount;
    }


    public OnProgressChangedListener getOnProgressChangedListener() {
        return mProgressListener;
    }

    public void setOnProgressChangedListener(OnProgressChangedListener mProgressListener) {
        this.mProgressListener = mProgressListener;
    }

    public OnActionUpOrCancelListener getOnActionUpOrCancelListener() {
        return onActionUpOrCancelListener;
    }

    public void setOnActionUpOrCancelListener(OnActionUpOrCancelListener onActionUpOrCancelListener) {
        this.onActionUpOrCancelListener = onActionUpOrCancelListener;
    }

    /**
     * progress改变监听器
     */
    public interface OnProgressChangedListener {

        void onProgressChanged(float progress, int position, float mThumbCenterX, float mLeft, float mRight, boolean isFisrtSet, boolean autoAdjust);
    }

    public interface OnActionUpOrCancelListener {
        void onActionUpOrCancel();
    }
}
