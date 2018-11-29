package fxq.android.com.commonbusiness.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Scroller;

import java.lang.reflect.Field;

import fxq.android.com.commonbusiness.SwipeFinishAction;

/**
 * Created by Fangxq on 2018/4/23.
 */

public class SwipeFinishUtils {
    private static final int INVALID_POINTER = -1;

    private boolean mIsBeingDragged;

    private int mTouchSlop;
    private int mActivePointerId;
    private int mLastMotionX;
    private int mLastMotionY;
    private int mMoveMotionX;
    private int mMoveMotionY;
    private int mMaximumVelocity;
    private int mMinimumVelocity;

    private View mView;
    private VelocityTracker mVelocityTracker;
    private Scroller mScroller;
    private SwipeFinishAction mAction;
    private int mStatusBarHeight;

    private boolean mFullScreen;
    private boolean mDownOver = false;//距离左侧1/2才生效
    private int mScreenWidth;
    private boolean mInChild;

    public SwipeFinishUtils(Context context, Scroller scroller) {
        mScroller = scroller;
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mStatusBarHeight = getStatusBarHeight(context);
    }

    public void setContentView(View view) {
        this.mView = view;
    }

    public void setSwipeToTouchAction(SwipeFinishAction action) {
        this.mAction = action;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        Log.i("TAG", "SwipeToClose----------------------onInterceptTouchEvent:" + ev.getAction() + ",mIsBeingDragged:" + mIsBeingDragged);
        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
                if (!mDownOver) {
                    break;
                }
                final int activePointerId = mActivePointerId;
                if (activePointerId == INVALID_POINTER) {
                    break;
                }

                final int pointerIndex = ev.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    break;
                }

                final int x = (int) ev.getX(pointerIndex);
                final int y = (int) ev.getY(pointerIndex);
                final int xDiff = Math.abs(x - mLastMotionX);
                final int yDiff = Math.abs(y - mLastMotionY);
                boolean hasScrollToRight = x > mLastMotionX;
                boolean hasCanScroll = !mInChild || (hasScrollToRight && mAction != null && mAction.onScrollToClose());
                if (xDiff > yDiff && xDiff > mTouchSlop && hasCanScroll) {
                    mIsBeingDragged = true;
                    mLastMotionX = x;
                    mMoveMotionX = x;
                    mLastMotionY = y;
                    mMoveMotionY = y;
                    initVelocityTrackerIfNotExists();
                    mVelocityTracker.addMovement(ev);
                    final ViewParent parent = mView.getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                final int x = (int) ev.getX();
                final int y = (int) ev.getY();
                mLastMotionX = x;
                mMoveMotionX = x;
                mLastMotionY = y;
                mMoveMotionY = y;
                mActivePointerId = ev.getPointerId(0);

                initOrResetVelocityTracker();
                mVelocityTracker.addMovement(ev);

                mIsBeingDragged = !mScroller.isFinished();
                Point point = new Point(mMoveMotionX, addYForSDK19(mLastMotionY));
                Rect rect = new Rect();
                mInChild = mAction != null && mAction.inChild(rect, point);

                if (mAction != null && !mIsBeingDragged) {
                    mAction.onTouchDown();
                }
                mDownOver = mLastMotionX <= mScreenWidth / 2;
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                recycleVelocityTracker();
                mDownOver = false;
                mInChild = false;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
        }
        return mIsBeingDragged;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionX = (int) ev.getX(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        initVelocityTrackerIfNotExists();
        MotionEvent vtev = MotionEvent.obtain(ev);
        final int actionMasked = ev.getActionMasked();

        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN: {
                if (mView instanceof ViewGroup) {
                    if (((ViewGroup) mView).getChildCount() == 0)
                        return false;
                }
                if ((mIsBeingDragged = !mScroller.isFinished())) {
                    final ViewParent parent = mView.getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }

                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                mLastMotionX = (int) ev.getX();
                mMoveMotionX = mLastMotionX;

                mLastMotionY = (int) ev.getY();
                mMoveMotionY = mLastMotionY;
                mActivePointerId = ev.getPointerId(0);
                break;
            }
            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                if (activePointerIndex == -1) {
                    break;
                }

                final int x = (int) ev.getX(activePointerIndex);
                int deltaX = mMoveMotionX - x;
                final int y = (int) ev.getY(activePointerIndex);
                int deltaY = mMoveMotionY - y;
                if (!mIsBeingDragged && Math.abs(deltaX) >= Math.abs(deltaY) && Math.abs(deltaX) > mTouchSlop) {
                    final ViewParent parent = mView.getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    mIsBeingDragged = true;
                    if (deltaX > 0) {
                        deltaX -= mTouchSlop;
                    } else {
                        deltaX += mTouchSlop;
                    }
                }
                if (mIsBeingDragged) {
                    if (mView.getScrollX() + deltaX >= 0) {
                        deltaX = -mView.getScrollX();
                    }
                    mView.scrollBy(deltaX, 0);
                }
                mMoveMotionX = x;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mIsBeingDragged) {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int initialVelocity = (int) velocityTracker.getXVelocity(mActivePointerId);
                    int scrollX = mView.getScrollX();
                    int dx = Math.abs(scrollX);
                    boolean finish = false;
                    if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
                        if (initialVelocity > 0) {
                            finish = true;
                            dx = -(mView.getWidth() - dx);
                        }
                    } else if (Math.abs(mLastMotionX - ev.getX()) > mView.getWidth() / 2) {
                        finish = true;
                        dx = -(mView.getWidth() - dx);
                    }
                    scrollTo(scrollX, dx, finish);
                    mActivePointerId = INVALID_POINTER;
                }
                mDownOver = false;
                mInChild = false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                final int index = ev.getActionIndex();
                mLastMotionX = (int) ev.getX(index);
                mMoveMotionX = mLastMotionX;
                mActivePointerId = ev.getPointerId(index);
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                mLastMotionX = (int) ev.getX(ev.findPointerIndex(mActivePointerId));
                mMoveMotionX = mLastMotionX;
                break;
        }

        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();
        if (mAction != null) {
            mAction.onScroll();
        }
        return true;
    }

    private void scrollTo(int startX, int dx, boolean finish) {
        if (mAction != null) {
            mAction.startScroll(startX, dx, finish);
        }
    }


    public void setActivityFullScreen(boolean fullScreen) {
        mFullScreen = fullScreen;
    }

    private int addYForSDK19(int y) {
        if (mFullScreen) {
            return y;
        } else {
            return y + mStatusBarHeight;
        }
    }

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }


    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    public int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
            if (context instanceof Activity) {
                Rect frame = new Rect();
                ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                statusBarHeight = frame.top;
            }
        }
        return statusBarHeight;
    }


}
