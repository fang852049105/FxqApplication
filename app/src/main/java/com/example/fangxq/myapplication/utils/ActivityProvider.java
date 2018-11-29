package com.example.fangxq.myapplication.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fangxq on 2018/5/14.
 */

public class ActivityProvider {

    private static ActivityProvider activityProvider;
    private List<Activity> appTask = new ArrayList<>();
    private String topActivityStatus;

    public static final String RESUMING = "resuming";
    public static final String STOPPED = "stopped";
    public static final String PAUSED = "paused";

    private ActivityProvider() {
    }

    public synchronized static ActivityProvider getInstance() {
        if (activityProvider == null) {
            activityProvider = new ActivityProvider();
        }
        return activityProvider;
    }

    public void registerActivity(Activity activity) {
        appTask.add(activity);
    }

    public void unregisterActivity(Activity activity) {
        if (appTask.contains(activity)) {
            appTask.remove(activity);
        }
    }

    public void clearTask() {
        for (Activity activity : appTask) {
            activity.finish();
        }
    }

    public void exitApp() {
        clearTask();
        System.exit(0);
    }

    public void setStatus(String status) {
        this.topActivityStatus = status;
    }

    public boolean isForeground()
    {
        if (appTask.isEmpty() || STOPPED.equals(topActivityStatus)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public Activity getTopActivity() {
        if (appTask.isEmpty()) {
            return null;
        }

        return appTask.get(appTask.size() - 1);
    }
}
