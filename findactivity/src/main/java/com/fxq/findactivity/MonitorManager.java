package com.fxq.findactivity;

import android.app.Application;

/**
 * @author Fanxq
 * @date 2019-09-30
 */
public class MonitorManager {

    private ActivityMonitor mMonitor;

    private Application mApplication;
    private static MonitorManager sDove;

    public static MonitorManager getInstance(Application application){
        if (sDove == null) {
            sDove = new MonitorManager(application);
        }
        return sDove;
    }
    private MonitorManager(Application application){
        this.mApplication = application;
        if (this.mMonitor == null) {
            this.mMonitor = new ActivityMonitor(application.getApplicationContext());
        }
    }

    public ActivityMonitor getMonitor(){
        return mMonitor;
    }

    public void init(){
        sDove.mApplication.registerActivityLifecycleCallbacks(mMonitor);
    }
}
