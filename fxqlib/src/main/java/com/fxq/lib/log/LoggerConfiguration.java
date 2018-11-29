package com.fxq.lib.log;

import android.content.Context;



public class LoggerConfiguration {

    public static final int VERBOSE = 0;
    public static final int DEBUG = 1;
    public static final int INFO = 2;
    public static final int WRAN = 3;
    public static final int ERROR = 4;

    private int loggerLevel = 0;
    private boolean isLogToDisk = false;
    private Context context;

    private static LoggerConfiguration mInstance;
    private static final String Logger_CONFIG_PATH = "LogConfig.properties";
    private static final String LOGGER_LEVEL = "logLevel";
    private static final String IS_LOG_TO_DISK = "isLogToDisk";

    private LoggerConfiguration() {
    }

    public static synchronized LoggerConfiguration getInstance() {
        if (mInstance == null) {
            mInstance = new LoggerConfiguration();
        }
        return mInstance;
    }

    public boolean isLoggable(int logLevel) {
        return this.loggerLevel <= logLevel;
    }

    public boolean isLogToDisk() {
        return this.isLogToDisk;
    }

    public Context getLogContext() {
        return this.context;
    }
}
