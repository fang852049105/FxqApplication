package com.fxq.lib.anrwatchdog;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.fxq.lib.utils.FileUtils;
import com.fxq.lib.utils.PackageUtils;

import java.io.File;

import gradleplugin.fxq.com.fxqlib.BuildConfig;

/**
 * @author huiguo
 * @date 2018/9/10
 */
public class ANRWatchManager {

    /**
     * ANR 监听回调
     */
    private ANRListener mANRListener;
    /**
     * 线程中断回调
     */
    private InterruptionListener mInterruptionListener;
    /**
     * 忽视debuger状态的ANR
     */
    private boolean ignoreDebugger;
    /**
     * 设置判定发生了ANR的时间。
     */
    private long timeoutInterval = -1;

    /**
     * 保存ANR信息到file
     */
    private boolean saveExceptionToFile;

    /**
     * 抓取所有线程堆栈信息
     */
    private boolean reportAllThreadInfo;

    /**
     * 只运行于debug模式
     */
    private boolean onlyRunDebugMode = true;

    /**
     * 保存ANR信息的文件夹路径
     */
    private String filePath = "2dFireANRCrashs";
    /**
     * 保存ANR信息的文件名称
     */
    private String fileName = "ANR.txt";

    /**
     * 是否开启广播监听
     */
    private boolean startBRMonitor = true;

    private static ANRWatchManager mANRWatchManager;
    private ANRWatchRunnable mANRWatchRunnable;
    private Context mContext;
    private static final long SLEEP_TIME = 60 * 1000;
    private ANRBroadcastReceiver mANRBroadcastReceiver;
    public static final String ACTION_ANR = "android.intent.action.ANR";

    private ANRWatchManager(Context context) {
        if (context != null) {
            mContext = context.getApplicationContext();
        }
    }

    public synchronized static ANRWatchManager getInstance(@NonNull Context context) {
        if (mANRWatchManager == null) {
            mANRWatchManager = new ANRWatchManager(context);
        }
        return mANRWatchManager;
    }

    public ANRWatchManager setANRListener(ANRListener mANRListener) {
        this.mANRListener = mANRListener;
        return this;
    }

    public ANRWatchManager setInterruptionListener(InterruptionListener mInterruptionListener) {
        this.mInterruptionListener = mInterruptionListener;
        return this;
    }

    public ANRWatchManager setIgnoreDebugger(boolean ignoreDebugger) {
        this.ignoreDebugger = ignoreDebugger;
        return this;
    }

    public ANRWatchManager setTimeoutInterval(long timeoutInterval) {
        this.timeoutInterval = timeoutInterval;
        return this;
    }

    public ANRWatchManager setSaveExceptionToFile(boolean saveExceptionToFile) {
        this.saveExceptionToFile = saveExceptionToFile;
        return this;
    }

    public ANRWatchManager setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public ANRWatchManager setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public ANRWatchManager setReportAllThreadInfo(boolean reportAllThreadInfo) {
        this.reportAllThreadInfo = reportAllThreadInfo;
        return this;
    }

    public ANRWatchManager setOnlyRunDebugMode(boolean onlyRunDebugMode) {
        this.onlyRunDebugMode = onlyRunDebugMode;
        return this;
    }

    public ANRWatchManager setStartBRMonitor(boolean startBRMonitor) {
        this.startBRMonitor = startBRMonitor;
        return this;
    }

    /**
     * 启动监控
     */
    public synchronized void start() {
        if (Debug.isDebuggerConnected() || (onlyRunDebugMode && !BuildConfig.DEBUG)) {
            return;
        }
        appStartPostANRExceptionFile();
        //ANRWatchRunnable start
        if (mANRWatchRunnable == null) {
            mANRWatchRunnable = new ANRWatchRunnable(mContext);
        }
        mANRWatchRunnable.setInterruptionListener(mInterruptionListener)
                .setIgnoreDebugger(ignoreDebugger)
                .setTimeoutInterval(timeoutInterval)
                .setReportAllThreadInfo(reportAllThreadInfo)
                .start();
        //ANRCatch use BroadcastReceiver
        if (startBRMonitor) {
            registerANRReceiver(mContext);
        }
    }

    /**
     * 停止监控
     */
    public synchronized void stop() {
        if (mANRWatchRunnable != null) {
            mANRWatchRunnable.stop();
        }
        if (startBRMonitor) {
            unregisterANRReceiver(mContext);
        }
    }

    /**
     * 处理ANRWatchRunnable捕捉的异常信息
     * @param anrException
     */
    public void handleANRException(ANRException anrException) {
        if (anrException == null) {
            return;
        }
        if (mANRListener != null) {
            mANRListener.onAppNotResponding(anrException);
        } else {
            if (reportAllThreadInfo) {
                handleAllThreadInfoForANRException(anrException);
            } else {
                handleMainThreadInfoForANRException(anrException);
            }
        }
    }

    /**
     * 处理ANRBroadcastReceiver捕捉的异常信息
     * @param anrExceptionStr
     */
    public void handleANRExceptionStr(String anrExceptionStr) {
        if (TextUtils.isEmpty(anrExceptionStr)) {
            return;
        }
        if (mANRListener != null) {
            mANRListener.onAppNotResponding(anrExceptionStr);
        } else {
            if (saveExceptionToFile) {
                FileUtils.saveStringToFile(mContext, anrExceptionStr, FileUtils.getFilePath(filePath), fileName);
            } else {
                postANRException(anrExceptionStr, "");
            }
        }
    }

    /**
     * 处理主线程ANR异常信息
     * @param anrException
     */
    private void handleMainThreadInfoForANRException(ANRException anrException) {
        StringBuilder exceptionStringBuilder = new StringBuilder();
        Throwable throwable = anrException.getCause();
        StackTraceElement[] stackTraceElements = null;
        if (throwable != null) {
            stackTraceElements = throwable.getStackTrace();
        }
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            exceptionStringBuilder.append(stackTraceElement.toString()).append("\n");
        }
        if (saveExceptionToFile) {
            FileUtils.saveStringToFile(mContext, exceptionStringBuilder.toString(), FileUtils.getFilePath(filePath), fileName);
        } else {
            postANRException(exceptionStringBuilder.toString(), "");
        }
    }

    private void handleAllThreadInfoForANRException(ANRException anrException) {
        //todo
    };



    /**
     * @param exceptionStr  ANR信息
     * @param needDeleteFilePath 保存ANR信息的文件路径，上传后需删除
     */
    private void postANRException(final String exceptionStr, final String needDeleteFilePath) {
        if (TextUtils.isEmpty(exceptionStr)) {
            return;
        }
        // post to server
    }

    private void appStartPostANRExceptionFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                postANRExceptionFile();
            }
        }).start();
    }

    /**
     * 上传之前保存的未上传过的ANR信息
     */
    private void postANRExceptionFile() {
        if (!PackageUtils.hasPermission(mContext, FileUtils.READ_EXTERNAL_STORAGE)) {
            return;
        }
        String path = FileUtils.getFilePath(filePath) + File.separator + fileName;
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        try {
            String anrExctptionStr = FileUtils.readFileToString(path);
            if (TextUtils.isEmpty(anrExctptionStr)) {
                return;
            }
            postANRException(anrExctptionStr, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //BroadcastReceiver catch ANR start
    private void registerANRReceiver(Context context) {
        if (mANRBroadcastReceiver == null) {
            mANRBroadcastReceiver = new ANRBroadcastReceiver();
        }
        context.registerReceiver(mANRBroadcastReceiver, new IntentFilter(ACTION_ANR));
    }

    private void unregisterANRReceiver(Context context) {
        if (mANRBroadcastReceiver == null) {
            return;
        }
        context.unregisterReceiver(mANRBroadcastReceiver);
    }

    /**
     * 处理ANR信息
     */
    public void filterANR() {
        if (Debug.isDebuggerConnected() || (ignoreDebugger && BuildConfig.DEBUG)) {
            return;
        }
        handleANRExceptionStr(ANRCatchHelper.getFilteredANR(mContext));
    }
    //BroadcastReceiver catch ANR end

    public interface ANRListener {
        /**
         * ANRWatchRunnable捕捉的ANR信息回调
         * @param anrException
         */
        void onAppNotResponding(ANRException anrException);

        /**
         * ANRBroadcastReceiver捕捉的ANR信息回调
         * @param anrExceptionStr
         */
        void onAppNotResponding(String anrExceptionStr);
    }

    public interface InterruptionListener {
        /**
         * 线程中断回调
         *  @param exception
         */
        void onInterrupted(InterruptedException exception);
    }
}
