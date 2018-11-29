package com.fxq.lib.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.fxq.lib.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import gradleplugin.fxq.com.fxqlib.R;

/**
 * Created by Fangxq on 2017/8/7.
 */
public class AutoVerticalScrollTextview extends TextSwitcher implements ViewSwitcher.ViewFactory {

    private static final int FLAG_START_AUTO_SCROLL = 0;
    private static final int FLAG_STOP_AUTO_SCROLL = 1;

    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mPaddingBottom;
    private int mTextColor;
    private int mTextSize;
    private String mText;
    private static final int DEFAULT_TEXT_SIZE = 14;
    private Rotate3dAnimation mInUp;
    private Rotate3dAnimation mOutUp;
    private long inAnimDuration = 1000;
    private long outAnimDuration = 1000;
    private long intervalTime = 2000;
    private boolean isGravityCenter = false;


    private OnItemClickListener itemClickListener;
    private Context mContext;
    private int currentId = 0;
    private List<String> textList;

    public AutoVerticalScrollTextview(Context context) {
        this(context, null);
        mContext = context;
    }

    public AutoVerticalScrollTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        textList = new ArrayList<String>();
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray attrsArray = mContext.obtainStyledAttributes(attrs, R.styleable.AutoVerticalScrollTextview);
        mTextColor = attrsArray.getInteger(R.styleable.AutoVerticalScrollTextview_android_textColor, R.color.b1);
        mTextSize = attrsArray.getDimensionPixelSize(R.styleable.AutoVerticalScrollTextview_android_textSize, Utils.dip2px(mContext, DEFAULT_TEXT_SIZE));
        mText = attrsArray.getString(R.styleable.AutoVerticalScrollTextview_android_text);
        mPaddingLeft = (int) attrsArray.getDimension(R.styleable.AutoVerticalScrollTextview_avs_paddingLeft, 5f);
        mPaddingBottom = (int) attrsArray.getDimension(R.styleable.AutoVerticalScrollTextview_avs_paddingBottom, 5f);
        mPaddingRight = (int) attrsArray.getDimension(R.styleable.AutoVerticalScrollTextview_avs_paddingRight, 5f);
        mPaddingTop = (int) attrsArray.getDimension(R.styleable.AutoVerticalScrollTextview_avs_paddingTop, 5f);
        isGravityCenter = attrsArray.getBoolean(R.styleable.AutoVerticalScrollTextview_avs_gravity_center, false);
        attrsArray.recycle();

        setFactory(this);
    }


    public void setInAnimTime(long animDuration) {
        this.inAnimDuration = animDuration;
        mInUp = createAnim(true, true);
        setInAnimation(mInUp);
    }

    public void setOutAnimTime(long animDuration) {
        this.outAnimDuration = animDuration;
        mOutUp = createAnim(false, true);
        setOutAnimation(mOutUp);
    }

    public void setIntervalTime(long time) {
        intervalTime = time;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FLAG_START_AUTO_SCROLL:
                    if (textList.size() > 0) {
                        currentId++;
                        setText(textList.get(currentId % textList.size()));
                    }
                    handler.sendEmptyMessageDelayed(FLAG_START_AUTO_SCROLL, intervalTime);
                    break;
                case FLAG_STOP_AUTO_SCROLL:
                    handler.removeMessages(FLAG_START_AUTO_SCROLL);
                    break;
            }
        }
    };


    public void setTextList(List<String> titles) {
        textList.clear();
        textList.addAll(titles);
        if (textList.size() > 1) {
            mInUp = createAnim(true, true);
            mOutUp = createAnim(false, true);

            setInAnimation(mInUp);//当View显示时动画资源ID
            setOutAnimation(mOutUp);//当View隐藏是动画资源ID。

        }
        if (!textList.isEmpty()) {
            setText(textList.get(0));
        }
    }

    public void startAutoScroll() {
        handler.sendEmptyMessage(FLAG_START_AUTO_SCROLL);
    }

    public void stopAutoScroll() {
        handler.removeMessages(FLAG_START_AUTO_SCROLL);
    }

    @Override
    public View makeView() {
        TextView t = new TextView(mContext);
        if (isGravityCenter) {
            t.setGravity(Gravity.CENTER);
        } else {
            t.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        }
        t.setSingleLine(true);
        t.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
        t.setTextColor(mTextColor);
        t.setTextSize(Utils.px2dip(mContext, mTextSize));
        t.setEllipsize(TextUtils.TruncateAt.END);
        t.setText(mText);
        t.setClickable(true);
        t.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null && textList.size() > 0 && currentId != -1) {
                    itemClickListener.onItemClick(currentId % textList.size());
                }
            }
        });
        return t;
    }

    private Rotate3dAnimation createAnim(boolean turnIn, boolean turnUp) {

        Rotate3dAnimation rotation = new Rotate3dAnimation(turnIn, turnUp);
        if (turnIn) {
            rotation.setDuration(inAnimDuration);
        } else {
            rotation.setDuration(outAnimDuration);

        }
        rotation.setFillAfter(false);
        rotation.setInterpolator(new AccelerateInterpolator());

        return rotation;
    }

    class Rotate3dAnimation extends Animation {
        private float mCenterX;
        private float mCenterY;
        private final boolean mTurnIn;
        private final boolean mTurnUp;
        private Camera mCamera;

        public Rotate3dAnimation(boolean turnIn, boolean turnUp) {
            mTurnIn = turnIn;
            mTurnUp = turnUp;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
            mCenterY = getHeight();
            mCenterX = getWidth();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {

            final float centerX = mCenterX;
            final float centerY = mCenterY;
            final Camera camera = mCamera;
            final int derection = mTurnUp ? 1 : -1;

            final Matrix matrix = t.getMatrix();

            camera.save();
            if (mTurnIn) {
                camera.translate(0.0f, derection * mCenterY * (interpolatedTime - 1.0f), 0.0f);
            } else {
                camera.translate(0.0f, derection * mCenterY * (interpolatedTime), 0.0f);
            }
            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }


    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}

