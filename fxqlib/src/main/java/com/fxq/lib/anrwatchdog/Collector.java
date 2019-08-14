package com.fxq.lib.anrwatchdog;

/**
 * @author huiguo
 * @date 2019-08-09
 */
public interface Collector {
    String[] getStackTraceInfo();

    void add(String stackTrace);
}
