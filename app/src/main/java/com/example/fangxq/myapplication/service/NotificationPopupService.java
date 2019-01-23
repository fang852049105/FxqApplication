package com.example.fangxq.myapplication.service;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.fangxq.myapplication.R;
import com.example.fangxq.myapplication.ui.LineChartTestActivity;
import com.example.fangxq.myapplication.utils.ActivityProvider;

/**
 * Created by Fangxq on 2018/5/14.
 */

public class NotificationPopupService extends Service {

    private Activity mTopActivity;
    private TN mTN;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTN = new TN(this);
        this.getApplication().registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                mTopActivity = activity;
                if (ActivityProvider.getInstance().isForeground(activity)) {
                    mTN.show();
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (!ActivityProvider.getInstance().isForeground(activity)) {
                    mTN.hide();
                }

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTN != null) {
            mTN.hide();
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!ActivityProvider.getInstance().isForeground(this)) {
            mTN.hide();
            return super.onStartCommand(intent, flags, startId);
        } else {
            mTN.show();
        }
        return super.onStartCommand(intent, flags, startId);
    }


    private static class TN {
        private WindowManager windowManager;
        private Context mContext;

        private View popupView;
        private TextView tipView;
        private LayoutInflater inflater;

        private String mText;

        private float lastX;
        private float lastY;
        private boolean isclick;


        final Runnable mShow = new Runnable() {
            @Override
            public void run() {
                handleShow();
            }
        };

        final Runnable mHide = new Runnable() {
            @Override
            public void run() {
                handleHide();
            }
        };

        public void setText(String text) {
            mText = text;
        }

        private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        final Handler mHandler = new Handler();

        TN(Context context) {
            inflater = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            mContext = context;

            mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            mParams.format = PixelFormat.TRANSLUCENT;
            mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
//            if (Build.VERSION.SDK_INT >= 26) {
//                mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_MEDIA;
//            } else {
//                mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
//            }


            mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

            mParams.gravity = Gravity.TOP | Gravity.LEFT;

        }

        /**
         * schedule handleShow into the right thread
         */
        public void show() {
            mHandler.post(mShow);
        }

        /**
         * schedule handleHide into the right thread
         */
        public void hide() {
            mHandler.post(mHide);
        }

        private void handleShow() {
            if (popupView != null) {
                return;
            }
            popupView = LayoutInflater.from(mContext).inflate(R.layout.layout_notification_popupview, null, false);
            Context context = popupView.getContext().getApplicationContext();
            String packageName = popupView.getContext().getPackageName();
            if (context == null) {
                context = popupView.getContext();
            }
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            // We can resolve the Gravity here by using the Locale for getting
            // the layout direction
            mParams.packageName = packageName;
            if (popupView.getParent() != null) {
                windowManager.removeView(popupView);
            }
            try {
                windowManager.addView(popupView, mParams);
            } catch (Exception ex) {

            }
            //整个view设置点击事件，不影响整个view的Touch事件
//            popupView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(mContext, LineChartTestActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    mContext.startActivity(i);
//                }
//            });

            //子View设置点击事件，子View区域的影响整个view的Touch事件
//            popupView.findViewById(R.id.iv_icon).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(mContext, LineChartTestActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    mContext.startActivity(i);
//                }
//            });
//            popupView.findViewById(R.id.ll_content).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(mContext, LineChartTestActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    mContext.startActivity(i);
//                }
//            });
            popupView.findViewById(R.id.iv_icon).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
//                    mParams.x = (int) event.getRawX() - popupView.getMeasuredWidth()/2;
//                    //减25为状态栏的高度
//                    mParams.y = (int) event.getRawY() - popupView.getMeasuredHeight()/2 - 25;
//                    //刷新
//                    windowManager.updateViewLayout(popupView, mParams);
//                    return false;
                    int ea = event.getAction();
                    switch (ea) {
                        case MotionEvent.ACTION_DOWN:
                            isclick = false;//当按下的时候设置isclick为false，具体原因看后边的讲解
                            lastX = event.getRawX();
                            lastY = event.getRawY();//按钮初始的横纵坐标
                            break;
                        case MotionEvent.ACTION_MOVE:
                            isclick = true;//当按钮被移动的时候设置isclick为true
                            int dx = (int) (event.getRawX() - lastX);
                            int dy = (int) (event.getRawY() - lastY);//按钮被移动的距离
                            mParams.x = mParams.x + dx;
                            mParams.y = mParams.y + dy;
                            windowManager.updateViewLayout(popupView,mParams);
                            lastX = event.getRawX();
                            lastY = event.getRawY();
                            break;
                        default:
                            isclick=false;
                            break;
                    }
                    return isclick;
                }
            });

            popupView.findViewById(R.id.iv_icon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, LineChartTestActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
            });
            popupView.findViewById(R.id.ll_content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, LineChartTestActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
            });
        }


        private void handleHide() {
            if (popupView != null) {
                // note: checking parent() just to make sure the view has
                // been added...  i have seen cases where we get here when
                // the view isn't yet added, so let's try not to crash.
                if (popupView.getParent() != null) {
                    windowManager.removeView(popupView);
                }

                popupView = null;
            }
        }
    }
}
