package fxq.android.com.commonbusiness.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import java.lang.reflect.Method;

import fxq.android.com.commonbusiness.SwipeFinishAction;
import fxq.android.com.commonbusiness.SwipeFinishTouchCheckAction;
import fxq.android.com.commonbusiness.utils.SwipeFinishUtils;

/**
 * Created by Fangxq on 2018/4/23.
 */

public class SwipeFinishLayout extends FrameLayout implements SwipeFinishAction {

    private boolean mEnableGesture = true;
    private Scroller mScroller;
    private SwipeFinishUtils mSwipeToClose;
    private boolean mToCloseActivity;
    private Activity mActivity;

    public SwipeFinishLayout(Context context) {
        super(context);
        init(context);
    }

    public SwipeFinishLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
        mSwipeToClose = new SwipeFinishUtils(context, mScroller);

        mSwipeToClose.setContentView(this);
        mSwipeToClose.setSwipeToTouchAction(this);
        setWillNotDraw(false);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mEnableGesture && mSwipeToClose != null && mSwipeToClose.onInterceptTouchEvent(ev)) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return (mEnableGesture && mSwipeToClose != null && mSwipeToClose.onTouchEvent(ev));
    }

    public void setAllAreaCanScroll(boolean allAreaCanScroll) {
    }

    public void setEnableGesture(boolean enableGesture) {
        this.mEnableGesture = enableGesture;
    }

    public void setActivityFullScreen(boolean fullScreen) {
        this.mSwipeToClose.setActivityFullScreen(fullScreen);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        } else {
            if (mToCloseActivity) {
                if (mSwipeToCloseLayoutAction != null) {
                    mSwipeToCloseLayoutAction.onCloseAction();
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Math.abs(getScrollX()) >= getWidth()) {
            return;
        }
        canvas.save();
        int alpha = 160 * (getWidth() - Math.abs(getScrollX())) / getWidth();
        int color = (alpha << 24 | 0x00000000);
        canvas.drawColor(color);
        canvas.restore();
    }

    @Override
    public void startScroll(int startX, int dx, boolean finish) {
        mToCloseActivity = dx < 0;
        mScroller.startScroll(startX, 0, dx, 0, 500 * Math.abs(dx) / getWidth());
        postInvalidate();
    }

    private SwipeToCloseLayoutAction mSwipeToCloseLayoutAction;

    public void setSwipeToCloseLayoutAction(SwipeToCloseLayoutAction action) {
        this.mSwipeToCloseLayoutAction = action;
    }

    public interface SwipeToCloseLayoutAction extends SwipeFinishTouchCheckAction {
        /**
         * 关闭界面
         */
        void onCloseAction();
    }

    public void attachToActivity(Activity activity) {
        mActivity = activity;
        ViewGroup viewGroup = (ViewGroup) activity.findViewById(android.R.id.content);
        View contentView = viewGroup.getChildAt(0);
        viewGroup.removeView(contentView);
        if (contentView != null) {
            if (contentView.getBackground() == null) {
                contentView.setBackgroundColor(Color.WHITE);
            }
            attachView(contentView);
        }
        viewGroup.addView(this);
        convertActivityFromTranslucent(activity);
    }

    public void attachView(View view) {
        addView(view);
    }

    public void convertActivityFromTranslucent(Activity activity) {
        try {
            Method method = Activity.class
                    .getDeclaredMethod("convertFromTranslucent");
            method.setAccessible(true);
            method.invoke(activity);
        } catch (Throwable t) {
        }
    }

    public void convertActivityToTranslucent(Activity activity) {
        if (activity == null) {
            return;
        }
        try {
            Class<?>[] t = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionListenerClazz = null;
            Class<?>[] method = t;
            int len$ = t.length;
            for (int i$ = 0; i$ < len$; ++i$) {
                Class<?> clazz = method[i$];
                if (clazz.getSimpleName().contains(
                        "TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                    break;
                }
            }
            if (Build.VERSION.SDK_INT >= 21) {
                Class<?> ActivityOptions = Class
                        .forName("android.app.ActivityOptions");
                Method var8 = Activity.class.getDeclaredMethod(
                        "convertToTranslucent",
                        translucentConversionListenerClazz, ActivityOptions);
                var8.setAccessible(true);
                var8.invoke(activity, new Object[]{null, null});
            } else {
                Method var8 = Activity.class.getDeclaredMethod(
                        "convertToTranslucent",
                        translucentConversionListenerClazz);
                var8.setAccessible(true);
                var8.invoke(activity, new Object[]{null});
            }
        } catch (Throwable e) {
        }
    }

    @Override
    public void onScroll() {
        convertActivityToTranslucent(mActivity);
    }

    @Override
    public void onTouchDown() {
        convertActivityToTranslucent(mActivity);
    }

    @Override
    public boolean inChild(Rect rect, Point point) {
        return mSwipeToCloseLayoutAction != null && mSwipeToCloseLayoutAction.inChild(rect, point);
    }

    @Override
    public boolean onScrollToClose() {
        return mSwipeToCloseLayoutAction != null && mSwipeToCloseLayoutAction.onScrollToClose();
    }

    public void onActivityDestory() {
        mSwipeToClose = null;
        mScroller = null;
        mActivity = null;
        mToCloseActivity = false;
    }

}
