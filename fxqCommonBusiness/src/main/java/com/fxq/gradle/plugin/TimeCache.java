package com.fxq.gradle.plugin;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huiguo
 * @date 2019/3/12
 */
public class TimeCache {

    public static Map<String, Long> mStartTime = new HashMap<>();
    public static Map<String, Long> mEndTime = new HashMap<>();
    private static final String TAG = "TIME_CACHE";

    public static void setStartTime(String methodName, long time) {
        Log.e(TAG, "------start------");
        mStartTime.put(methodName, time);
    }

    public static void setEndTime(String methodName, long time) {
        mEndTime.put(methodName, time);
        getCostTime(methodName);
    }

    public static String getCostTime(String methodName) {
        long start = mStartTime.get(methodName);
        long end = mEndTime.get(methodName);
        String costTimeStr = "method: " + methodName + " cost " + Long.valueOf(end - start) + "ms ";
        Log.e(TAG, costTimeStr);
        Log.e(TAG, "------end------");
        return costTimeStr;

    }
}
