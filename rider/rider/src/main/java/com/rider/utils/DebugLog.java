package com.rider.utils; /***
 * This is free and unencumbered software released into the public domain.
 * <p/>
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 * <p/>
 * For more information, please refer to <http://unlicense.org/>
 */


import android.util.Log;

import com.rider.BuildConfig;


/**
 * @author Mustafa Ferhan Akman
 * <p/>
 * Create a simple and more understandable Android logs.
 * @date 21.06.2012
 */

public class DebugLog {

    static String className;
    static String methodName;
    static int lineNumber;

    private DebugLog() {
        /* Protect from instantiations */
    }

    public static boolean isDebuggable() {
        return BuildConfig.DEBUG;

    }

    private static String createLog(String log) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(methodName);
        buffer.append(":");
        buffer.append(lineNumber);
        buffer.append("]");
        buffer.append(log);

        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void e(String message) {
        if (!isDebuggable())
            return;

        // Throwable instance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(message));
        //rhc commented
        /*Crashlytics.log(Log.ERROR, className, createLog(message));
        if (FLogManager.getInstance().logging_level > 0)
            Bugfender.e("ERORR", message);*/
    }

    public static void i(String message) {

        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
        //rhc commented
        /*Crashlytics.log(Log.INFO, className, createLog(message));
        if (FLogManager.getInstance().logging_level == 5)
            Bugfender.d("INFO", message);*/
    }

    public static void d(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(message));
        //rhc commented
        /*Crashlytics.log(Log.DEBUG, className, createLog(message));
        if (FLogManager.getInstance().logging_level == 5)
            Bugfender.d("DEBUG", message);*/
    }

    public static void v(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.v(className, createLog(message));
        //rhc commented
//        Crashlytics.log(Log.VERBOSE, className, createLog(message));
    }

    public static void w(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(message));
        /*Crashlytics.log(Log.WARN, className, createLog(message));
        if (FLogManager.getInstance().logging_level == 5)
            Bugfender.w("WARNING", message);*/
    }

    public static void wtf(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(className, createLog(message));
        //rhc commented
        //Crashlytics.log(Log.ASSERT, className, createLog(message));
    }

}
