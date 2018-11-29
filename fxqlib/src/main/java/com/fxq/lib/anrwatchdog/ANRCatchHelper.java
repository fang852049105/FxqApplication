package com.fxq.lib.anrwatchdog;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import java.util.Iterator;
import java.util.List;

/**
 * @author huiguo
 * @date 2018/10/15
 */
public class ANRCatchHelper {

    private static final String SPLIT_TAG = "PID:";

    public static ActivityManager.ProcessErrorStateInfo findError(Context context, long time) {
        time = time < 0L ? 0L : time;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return null;
        }
        long limit = time / 500L;
        int index = 0;
        do {
            List errorStateInfoList = activityManager.getProcessesInErrorState();
            if (errorStateInfoList != null) {
                Iterator iterator = errorStateInfoList.iterator();
                while (iterator.hasNext()) {
                    ActivityManager.ProcessErrorStateInfo errorStateInfo = (ActivityManager.ProcessErrorStateInfo) iterator.next();
                    if (errorStateInfo.condition == 2) { //ANR 异常
                        return errorStateInfo;
                    }
                }
            }
        } while ((long) (index++) < limit);
        return null;
    }

    private static long lastTimes = 0;

    public static String getFilteredANR(Context context) {
        // 参考Bugly的逻辑
        String anrMsg = "";
        long nowTime = System.currentTimeMillis();
        if (nowTime - lastTimes < 10000L) {
            return anrMsg;
        }
        lastTimes = nowTime;
        ActivityManager.ProcessErrorStateInfo errorStateInfo = findError(context, 10000L);
        if (errorStateInfo == null || TextUtils.isEmpty(errorStateInfo.longMsg)) {
            return anrMsg;
        }
        if (errorStateInfo.pid == android.os.Process.myPid()) {
            anrMsg = errorStateInfo.longMsg;
            if (errorStateInfo.longMsg.contains(SPLIT_TAG)) {
                anrMsg = errorStateInfo.longMsg.substring(0, errorStateInfo.longMsg.indexOf(SPLIT_TAG));
            }
        }
        return anrMsg;
    }
}