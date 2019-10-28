package com.fxq.findactivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;

/**
 * @author Fanxq
 * @date 2019-09-30
 */
public class NotificationMonitor {

    private Context mContext;
    private Notification.Builder mBuilder;

    private int id = 1;
    private RemoteViews mRemoteViews;
    private static final String channelId = "whoareyou";
    private static final String channelName = "faChannel";

    public NotificationMonitor(Context context){
        this.mContext = context;
        this.mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.fa_notification);
    }

    private void setRemoteViews(ActivityInstanceInfo activityInstanceInfo, ActivityMonitor.StackEvent stackEvent){
        mRemoteViews.setTextViewText(R.id.task_id_tv,"task id: " + activityInstanceInfo.getTaskId());
        TaskStack taskStack = stackEvent.getTaskStack();
        ActivityInstanceInfo currentActivityInstanceInfo = taskStack.getActivityInstanceInfoStack().peek();
        if (currentActivityInstanceInfo != null) {
            mRemoteViews.setTextViewText(R.id.current_activity_name_tv, "当前 Activity: " + currentActivityInstanceInfo.getName());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void notification(ActivityInstanceInfo activityInstanceInfo, ActivityMonitor.StackEvent stackEvent) {
        if (stackEvent == null || stackEvent.getTaskStack() == null
                || stackEvent.getTaskStack().getActivityInstanceInfoStack() == null
                || stackEvent.getTaskStack().getActivityInstanceInfoStack().empty()){
            return;
        }

        if (mBuilder == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder = new Notification.Builder(mContext, channelId);
            } else {
                mBuilder = new Notification.Builder(mContext);
            }
        }
        setRemoteViews(activityInstanceInfo,stackEvent);
        mBuilder.setContent(mRemoteViews);
        mBuilder.setSmallIcon(R.drawable.fa_ic_notification);
        Notification notification = mBuilder.build();


        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        if (mNotificationManager != null) {
            mNotificationManager.notify(id, notification);
        }
    }
}
