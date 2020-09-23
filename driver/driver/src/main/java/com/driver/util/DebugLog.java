package com.driver.util; /***
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

import com.driver.BuildConfig;


/**
 * @author Mustafa Ferhan Akman
 *         <p/>
 *         Create a simple and more understandable Android logs.
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
        // Throwable instance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());
        String msg = createLog(message);
        if (isDebuggable())
            Log.e(className, msg);
        //rhc commented
        /*Crashlytics.log(Log.ERROR, className, msg);
        if (FLogManager.getInstance().logging_level > 0 )
            Bugfender.e("ERORR", message);*/
    }

    public static void i(String message) {

        getMethodNames(new Throwable().getStackTrace());
        String msg = createLog(message);
        if (isDebuggable())
            Log.i(className, msg);
        //rhc commented
        /*Crashlytics.log(Log.INFO, className, msg);
        if (FLogManager.getInstance().logging_level == 5 )
            Bugfender.d("INFO", message);
*/
    }

    public static void d(String message) {
        getMethodNames(new Throwable().getStackTrace());
        String msg = createLog(message);
        if (isDebuggable())
            Log.d(className, msg);
        //rhc commented
        /*Crashlytics.log(Log.DEBUG, className, msg);
        if (FLogManager.getInstance().logging_level == 5 )
            Bugfender.d("DEBUG", message);
*/    }

    public static void v(String message) {
        getMethodNames(new Throwable().getStackTrace());
        String msg = createLog(message);
        if (isDebuggable())
            Log.v(className, msg);
        //Crashlytics.log(Log.VERBOSE, className, msg);
    }

    public static void w(String message) {
        getMethodNames(new Throwable().getStackTrace());
        String msg = createLog(message);
        if (isDebuggable())
            Log.w(className, msg);
        /*Crashlytics.log(Log.WARN, className, msg);
        if (FLogManager.getInstance().logging_level == 5 )
            Bugfender.w("WARNING", message);*/
    }

    public static void wtf(String message) {
        getMethodNames(new Throwable().getStackTrace());
        String msg = createLog(message);
        if (isDebuggable())
            Log.wtf(className, msg);
        //Crashlytics.log(Log.ASSERT, className, msg);
    }

}
