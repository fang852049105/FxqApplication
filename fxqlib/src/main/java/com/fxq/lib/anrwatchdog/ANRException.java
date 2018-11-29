package com.fxq.lib.anrwatchdog;

import android.os.Looper;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
* @author huiguo
* @date 2018/9/10
*/

public class ANRException extends Error {

   private ANRException(ThreadStackInfoWrapper.ThreadStackException threadStackInfo) {
       super("ANRWatchManager Catch ANRException", threadStackInfo);
   }


   public static ANRException getMainThreadException() {
       Thread mainThread = Looper.getMainLooper().getThread();
       StackTraceElement[] stackTraceElements = mainThread.getStackTrace();
       ThreadStackInfoWrapper.ThreadStackException threadStackInfo = new ThreadStackInfoWrapper(getThreadNameAndState(mainThread), stackTraceElements)
               .new ThreadStackException(null);
       return new ANRException(threadStackInfo);
   }

   public static ANRException getAllThreadException() {
       final Thread mainThread = Looper.getMainLooper().getThread();

       final Map<Thread, StackTraceElement[]> stackTraceMap = new TreeMap<Thread, StackTraceElement[]>(new Comparator<Thread>() {
           @Override
           public int compare(Thread lhs, Thread rhs) {
               if (lhs == rhs) {
                   return 0;
               } if (lhs == mainThread) {
                   return 1;
               } if (rhs == mainThread) {
                   return -1;
               }
               return rhs.getName().compareTo(lhs.getName());
           }
       });

       for (Map.Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
           Thread key = entry.getKey();
           StackTraceElement[] value = entry.getValue();
           if (value.length > 0) {
               stackTraceMap.put(key, value);
           }
       }

       // Sometimes main is not returned in getAllStackTraces() - ensure that we list it
       if (!stackTraceMap.containsKey(mainThread)) {
           stackTraceMap.put(mainThread, mainThread.getStackTrace());
       }

       ThreadStackInfoWrapper.ThreadStackException threadStackInfo = null;
       for (Map.Entry<Thread, StackTraceElement[]> entry : stackTraceMap.entrySet()) {
           Thread key = entry.getKey();
           StackTraceElement[] value = entry.getValue();
           threadStackInfo = new ThreadStackInfoWrapper(getThreadNameAndState(key), value).
                   new ThreadStackException(threadStackInfo);
       }
       return new ANRException(threadStackInfo);
   }

   @Override
   public Throwable fillInStackTrace() {
       setStackTrace(new StackTraceElement[] {});
       return this;
   }

   public static String getThreadNameAndState(Thread thread) {
       return thread.getName() + "-state-" + thread.getState();
   }

   private static class ThreadStackInfoWrapper implements Serializable {

       private String nameAndState;
       private StackTraceElement[] stackTraceElements;

       private ThreadStackInfoWrapper(String nameAndState, StackTraceElement[] stackTraceElements) {
           this.nameAndState = nameAndState;
           this.stackTraceElements = stackTraceElements;
       }

       private class ThreadStackException extends Throwable {

           private ThreadStackException(Throwable throwable) {
               super(nameAndState, throwable);
           }

           @Override
           public synchronized Throwable fillInStackTrace() {
               setStackTrace(stackTraceElements);
               return this;
           }
       }
   }

}
