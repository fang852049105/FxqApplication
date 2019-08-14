package com.fxq.lib.anrwatchdog;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Choreographer;
import android.view.Display;
import android.view.WindowManager;

import java.util.Arrays;

/**
 *
 * @author huiguo
 * @date 2019-08-07
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class FPSFrameCallBack implements Choreographer.FrameCallback {

    private static final String TAG = "FPSFrameCallBack";
    private long SKIPPED_FRAME_ANR_LIMIT = 0;

    /**
     * 跳帧的临界值，大于此值可能发生ANR
     */
    private static long SKIPPED_FRAME_WARNING_LIMIT = 0;
    private long mLastFrameTimeNanos;
    private long mFrameIntervalNanos;
    private static final long SKIPPED_FRAME_INTERVAL = 4000;
    private Context mContext;


    public FPSFrameCallBack(Context context, long anrTimeoutInterval) {
        this(context, anrTimeoutInterval, SKIPPED_FRAME_INTERVAL);
    }

    public FPSFrameCallBack(Context context, long anrTimeoutInterval, long skipFrameInterval) {
        if (context != null) {
            mContext = context.getApplicationContext();
        }
        float mRefreshRate = getRefreshRate();
        mFrameIntervalNanos = (long) (1000000000l / mRefreshRate); //帧率，也就是渲染一帧的时间，
        SKIPPED_FRAME_ANR_LIMIT = anrTimeoutInterval * 1000l * 1000l / mFrameIntervalNanos;
        SKIPPED_FRAME_WARNING_LIMIT = skipFrameInterval * 1000l * 1000l / mFrameIntervalNanos;
        Log.e(TAG, "mFrameIntervalNanos = "  + mFrameIntervalNanos + "====" + "SKIPPED_FRAME_ANR_LIMIT = "  +SKIPPED_FRAME_ANR_LIMIT
                + "======" + "SKIPPED_FRAME_WARNING_LIMIT = " + SKIPPED_FRAME_WARNING_LIMIT);

    }


    /**
     *
     * @return 刷新率，一般是60
     */
    private float getRefreshRate() {
        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display.getRefreshRate();
    }

    @Override
    public void doFrame(long frameTimeNanos) {
        if (mLastFrameTimeNanos == 0) {
            mLastFrameTimeNanos = frameTimeNanos;
            Choreographer.getInstance().postFrameCallback(this);
            return;
        }
        //时间差
        final long jitterNanos = frameTimeNanos - mLastFrameTimeNanos;
        //时间差大于帧率，认为跳帧
        if (jitterNanos >= mFrameIntervalNanos) {
            final long skippedFrames = jitterNanos / mFrameIntervalNanos;
            if (skippedFrames >= SKIPPED_FRAME_ANR_LIMIT) {
                Log.e(TAG, "jitterNanos = "+ jitterNanos + "======" + "skippedFrames = "  + skippedFrames + " ====ANR=====");
                ANRWatchManager.getInstance(mContext).filterFPSFrameANR();

            }
        }
        mLastFrameTimeNanos = frameTimeNanos;
        Choreographer.getInstance().postFrameCallback(this);

    }

}
