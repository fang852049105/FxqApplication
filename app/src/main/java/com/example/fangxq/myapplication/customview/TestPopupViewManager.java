package com.example.fangxq.myapplication.customview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.fangxq.myapplication.R;
import com.example.fangxq.myapplication.bean.MessageNotificationItem;
import com.example.fangxq.myapplication.ui.LineChartTestActivity;
import com.example.fangxq.myapplication.utils.ActivityProvider;
import com.fxq.lib.utils.Utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fangxq on 2017/10/26.
 */

public class TestPopupViewManager {

    private List<PopupWindow> popupWindowList = new ArrayList<>();
    private List<MessageNotificationItem> messageList = new ArrayList<>();

    private static TestPopupViewManager testPopupViewManager;
    private float mPosY;
    private float mCurPosY;


    private TestPopupViewManager() {}

    public synchronized static TestPopupViewManager getInstance() {
        if (testPopupViewManager == null) {
            testPopupViewManager = new TestPopupViewManager();
        }
        return testPopupViewManager;

    }


    public MessageNotificationItem buildTestMessageNotificationItem() {
        MessageNotificationItem messageNotificationItem = new MessageNotificationItem();
        messageNotificationItem.setContent("111111111");
        messageNotificationItem.setTitle("2222222222");
        return messageNotificationItem;
    }

    public void showPopupWindow(MessageNotificationItem messageNotificationItem) {
        messageList.add(messageNotificationItem);
        if (messageList == null || messageList.size() == 1) {
            checkToShow(messageNotificationItem);
        }
    }

    private void getMessageForShow() {
        MessageNotificationItem messageItemForShow = null;
        for (MessageNotificationItem messageNotificationItem : messageList) {
            if (!messageNotificationItem.isShowed()) {
                messageItemForShow = messageNotificationItem;
                break;
            }
        }
        checkToShow(messageItemForShow);
    }

    private void checkToShow(MessageNotificationItem messageNotificationItem) {
        Activity topActivity = ActivityProvider.getInstance().getTopActivity();
        if (messageNotificationItem != null && StringUtils.isNotEmpty(messageNotificationItem.getTitle())
                && ActivityProvider.getInstance().isForeground(topActivity) && topActivity != null ) {
            messageNotificationItem.setShowed(true);
            initPopupWindow(messageNotificationItem, topActivity);
        }
    }

    private void initPopupWindow(final MessageNotificationItem messageNotificationItem, final Activity topActivity) {
        Log.e("fxq", "initPopupWindow messageNotificationItem " + messageNotificationItem.toString());
        View viewPopupWindow = LayoutInflater.from(topActivity).inflate(R.layout.layout_notification_popupview, null, false);
        RelativeLayout mNotificationLayout = (RelativeLayout) viewPopupWindow.findViewById(R.id.rl_notification_content);
        PopupWindow mPopupWindow = null;
        mPopupWindow = new PopupWindow(viewPopupWindow, LinearLayout.LayoutParams.MATCH_PARENT, Utils.dip2px(topActivity, 80), true);
        mPopupWindow.setAnimationStyle(R.style.FilterPushInOutStyle);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setFocusable(false);
        mPopupWindow.update();
        final PopupWindow finalMPopupWindow = mPopupWindow;
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindowList.remove(finalMPopupWindow);
                Log.e("fxq", "popupWindowList size = " + popupWindowList.size());
                Log.e("fxq", "messageList size = " + messageList.size());
            }
        });
        viewPopupWindow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        mPosY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurPosY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mCurPosY - mPosY > 0
                                && (Math.abs(mCurPosY - mPosY) > 25)) {
                            //向下滑動

                        } else if (mCurPosY - mPosY < 0
                                && (Math.abs(mCurPosY - mPosY) > 25)) {
                            //向上滑动
                            finalMPopupWindow.dismiss();
                        }
                        break;
                }
                return false;
            }
        });
        viewPopupWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalMPopupWindow.dismiss();
                Intent i = new Intent(topActivity, LineChartTestActivity.class);
                topActivity.startActivity(i);
            }
        });

        int height = mNotificationLayout.getHeight();
        //Log.e("fxq", "height = " + height);
        //Log.e("fxq", "mNotificationLayout = " + mNotificationLayout.toString());
        mNotificationLayout.animate().translationY(height).setListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e("fxq", "onAnimationEnd ");
                if (messageList != null && messageList.size() > 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (popupWindowList != null && popupWindowList.size() > 1) {
                                popupWindowList.get(0).dismiss();
                            }
                            if (messageList != null && messageList.contains(messageNotificationItem)) {
                                messageList.remove(messageNotificationItem);
                            }
//                            Log.e("fxq", "popupWindowList size = " + popupWindowList.size());
//                            Log.e("fxq", "messageList size = " + messageList.size());

                            getMessageForShow();
                        }
                    }, 300);
                } else {
//                    Log.e("fxq", "else popupWindowList size = " + popupWindowList.size());
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (popupWindowList != null && popupWindowList.size() > 1) {
//                                popupWindowList.get(0).dismiss();
//                            }
//                        }
//                    }, 300);
                }
            }
        });

        mNotificationLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                finalMPopupWindow.dismiss();
            }
        }, 3000);
        if (mPopupWindow != null) {
            int statusBarHeight = Utils.getStatusBarHeight(topActivity) > 0 ? Utils.getStatusBarHeight(topActivity) : Utils.dip2px(topActivity, 24);
            mPopupWindow.showAtLocation(topActivity.getWindow().getDecorView(), Gravity.NO_GRAVITY, 0, statusBarHeight);
            popupWindowList.add(mPopupWindow);
            Log.e("fxq", "show popupWindowList size = " + popupWindowList.size());

        }
//        if (popupWindowList != null && popupWindowList.size() > 1) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    popupWindowList.get(0).dismiss();
//                }
//            }, 500);
//        }
    }

    public void dismissPopupView() {
        if (popupWindowList != null && popupWindowList.size() > 0) {
            popupWindowList.get(0).dismiss();
        }
    }

}
