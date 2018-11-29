package com.fxq.lib.anrwatchdog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author huiguo
 * @date 2018/10/16
 */
public class ANRBroadcastReceiver extends BroadcastReceiver {

    private static final String ACTION_ANR = "android.intent.action.ANR";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        if (ACTION_ANR.equals(intent.getAction())) {
            ANRWatchManager.getInstance(context).filterANR();
        }
    }
}
