package com.fxq.lib.log;

import android.os.Environment;
import android.util.Log;


import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Logger {

    private static LoggerConfiguration loggerConfiguration = LoggerConfiguration.getInstance();

    public static void v(String tag, String msg) {
        if (loggerConfiguration.isLoggable(LoggerConfiguration.VERBOSE)) {
            Log.v(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable throwable) {
        if (loggerConfiguration.isLoggable(LoggerConfiguration.VERBOSE)) {
            Log.v(tag, msg, throwable);
        }
    }

    public static void d(String tag, String msg) {
        if (loggerConfiguration.isLoggable(LoggerConfiguration.DEBUG)) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable throwable) {
        if (loggerConfiguration.isLoggable(LoggerConfiguration.VERBOSE)) {
            Log.d(tag, msg, throwable);
        }
    }

    public static void i(String tag, String msg) {
        if (loggerConfiguration.isLoggable(LoggerConfiguration.INFO)) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable throwable) {
        if (loggerConfiguration.isLoggable(LoggerConfiguration.VERBOSE)) {
            Log.i(tag, msg, throwable);
        }
    }

    public static void w(String tag, String msg) {
        if (loggerConfiguration.isLoggable(LoggerConfiguration.WRAN)) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable throwable) {
        if (loggerConfiguration.isLoggable(LoggerConfiguration.VERBOSE)) {
            Log.w(tag, msg, throwable);
        }
    }

    public static void e(String tag, String msg) {
        if (loggerConfiguration.isLoggable(LoggerConfiguration.ERROR)) {
            Log.e(tag, msg);

            if (loggerConfiguration.isLogToDisk()) {
                logToDisk(tag, msg, null);
            }
        }
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (loggerConfiguration.isLoggable(LoggerConfiguration.ERROR)) {
            Log.e(tag, msg, throwable);

            if (loggerConfiguration.isLogToDisk()) {
                logToDisk(tag, msg, throwable);
            }
        }
    }

    private static void logToDisk(String tag, String msg, Throwable throwable) {
        try {
            if (loggerConfiguration.getLogContext() == null) {
                return;
            }
            String state = Environment.getExternalStorageState();
            boolean mExternalStorageAvailable;
            boolean mExternalStorageWriteable;
            if ("mounted".equals(state)) {
                mExternalStorageAvailable = mExternalStorageWriteable = true;
            } else {
                if ("mounted_ro".equals(state)) {
                    mExternalStorageAvailable = true;
                    mExternalStorageWriteable = false;
                } else {
                    mExternalStorageAvailable = mExternalStorageWriteable = false;
                }
            }
            if ((mExternalStorageWriteable) && (mExternalStorageAvailable)) {
                String stackTrace = "";
                String error = "";
                if (!StringUtils.isEmpty(msg)) {
                    error = msg;
                }
                if (null != throwable) {
                    stackTrace = Log.getStackTraceString(throwable);
                    error = String.format("%s \r\n %s", new Object[] { error, stackTrace });
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());

                String errorMsg = String.format("%s %s %s", new Object[] { dateFormat.format(new Date()), tag, error });

                File file = null;
                FileWriter writer = null;
                try {
                    File directory = new File(loggerConfiguration.getLogContext().getExternalFilesDir(null) + "/log");
                    if(!directory.exists()) {
                        directory.mkdirs();
                    }
                    file = new File(loggerConfiguration.getLogContext().getExternalFilesDir(null) + "/log", "LatteFinanceError.log");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    if (file.length() > 1048576L) {
                        file.delete();
                    }
                    writer = new FileWriter(file, true);
                    writer.append(errorMsg);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (writer != null) {
                        writer.close();
                    }
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
