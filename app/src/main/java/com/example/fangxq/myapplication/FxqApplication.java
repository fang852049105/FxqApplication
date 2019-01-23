package com.example.fangxq.myapplication;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.fangxq.myapplication.utils.ActivityProvider;
import com.fxq.lib.anrwatchdog.ANRWatchManager;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;


/**
 * Created by Fangxq on 2017/9/7.
 */
public class FxqApplication extends Application {

    private static FxqApplication mMyApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("fxq", "FxqApplication onCreate()");
        //initData();
        //LeakCanary.install(this);
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ActivityProvider.getInstance().registerActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                ActivityProvider.getInstance().setStatus(ActivityProvider.RESUMING);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                ActivityProvider.getInstance().setStatus(ActivityProvider.PAUSED);
            }

            @Override
            public void onActivityStopped(Activity activity) {
                ActivityProvider.getInstance().setStatus(ActivityProvider.STOPPED);
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityProvider.getInstance().unregisterActivity(activity);
            }
        });
//        new ANRWatchDog(2000).setANRListener(new ANRWatchDog.ANRListener() {
//            @Override
//            public void onAppNotResponding(ANRError error) {
//                Log.e("fxq onAppNotResponding", error.getMessage());
//            }
//        }).start();
//        ANRWatchManager.getInstance(this)
//                .setTimeoutInterval(3000)
//                .setReportAllThreadInfo(false)
//                .setSaveExceptionToFile(true)
//                .start();
    }

    public void initData() {
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }

    public static final FxqApplication getInstance() {
        return mMyApplication;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.e("fxq", "FxqApplication attachBaseContext()");
        initSophix();
    }

    private void initSophix() {
        String appVersion;
        try {
            appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
            Log.e("fxq", "appVersion = " + appVersion);

        } catch (Exception e) {
            appVersion = "1.0.0";
        }

        SophixManager.getInstance().setContext(this)
                .setAppVersion(appVersion)
                .setAesKey(null)
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        Log.e("fxq", "PatchLoadStatusListener code = " + code);

                        if (code == PatchStatus.CODE_DOWNLOAD_SUCCESS) {

                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                        }

                    }
                }).initialize();
    }


    private void initMatrix() {

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
