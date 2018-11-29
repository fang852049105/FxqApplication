package com.fxq.lib.anrwatchdog;

import android.content.Context;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;

import gradleplugin.fxq.com.fxqlib.BuildConfig;


/**
 * @author huiguo
 * @date 2018/9/10
 */
public class ANRWatchRunnable implements Runnable {


    /**
     * 线程中断回调
     */
    private ANRWatchManager.InterruptionListener mInterruptionListener;

    /**
     * 设置判定发生了ANR的时间。
     */
    private long timeoutInterval = -1;
    private Handler watchDogHandler = new Handler(Looper.getMainLooper());
    private boolean isStop;

    /**
     * 忽视debuger状态的ANR
     */
    private boolean ignoreDebugger;

    private boolean reportAllThreadInfo;

    /**
     * 判定发生了ANR的默认时间，必须要小于5秒，否则等弹出ANR，可能就被用户立即杀死了。
     */
    public static final int DEFAULT_ANR_TIMEOUT = 4500;
    private volatile int timeTick = 0;
    private Thread mANRWatchThread;
    private Context mContext;

    public ANRWatchRunnable(Context context) {
        if (context != null) {
            mContext = context.getApplicationContext();
        }
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timeTick = (timeTick + 1) % Integer.MAX_VALUE;
        }
    };

    @Override
    public void run() {
        int lastTimeTick;
        while (!isStop) {
            lastTimeTick = timeTick;
            watchDogHandler.post(runnable);
            try {
                Thread.sleep(timeoutInterval);
            } catch (InterruptedException e) {
                if (mInterruptionListener != null) {
                    mInterruptionListener.onInterrupted(e);
                }
                e.printStackTrace();
                return;
            }
            //如果相等，说明过了timeoutInterval的时间后watchDogHandler仍没有处理消息，已经ANR了
            if (timeTick == lastTimeTick) {
                if (Debug.isDebuggerConnected() || (ignoreDebugger && BuildConfig.DEBUG)) {
                    continue;
                }
                ANRException anrException;
                if (reportAllThreadInfo) {
                    anrException = ANRException.getAllThreadException();
                } else {
                    anrException = ANRException.getMainThreadException();
                }
                ANRWatchManager.getInstance(mContext).handleANRException(anrException);
            }
        }
    }

    public void start() {
        isStop = false;
        if (mANRWatchThread == null || !mANRWatchThread.isAlive()) {
            mANRWatchThread = new Thread(this);
            mANRWatchThread.start();
        }
    }

    public void stop() {
        if (!isStop) {
            if (mANRWatchThread != null && mANRWatchThread.isAlive()) {
                mANRWatchThread.interrupt();
                mANRWatchThread = null;
            }
            isStop = true;
        }
    }

    public ANRWatchRunnable setInterruptionListener(ANRWatchManager.InterruptionListener mInterruptionListener) {
        this.mInterruptionListener = mInterruptionListener;
        return this;
    }

    public ANRWatchRunnable setTimeoutInterval(long timeoutInterval) {
        if (timeoutInterval != -1) {
            this.timeoutInterval = timeoutInterval;
        } else {
            this.timeoutInterval = DEFAULT_ANR_TIMEOUT;
        }
        return this;
    }

    public ANRWatchRunnable setIgnoreDebugger(boolean ignoreDebugger) {
        this.ignoreDebugger = ignoreDebugger;
        return this;
    }

    public ANRWatchRunnable setReportAllThreadInfo(boolean reportAllThreadInfo) {
        this.reportAllThreadInfo = reportAllThreadInfo;
        return this;
    }
}
