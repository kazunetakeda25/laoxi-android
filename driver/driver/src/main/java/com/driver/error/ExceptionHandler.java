package com.driver.error;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.driver.util.DebugLog;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by h-link9 on 10/09/2015.
 */
public class ExceptionHandler 
    {
        public static String TAG = "ExceptionsHandler";
        private static String[] stackTraceFileList = null;

        public ExceptionHandler() {
    }

    public static boolean register(Context var0) {
        Log.i(TAG, "Registering default exceptions handler");
        PackageManager var1 = var0.getPackageManager();

        try {
           PackageInfo var2 = var1.getPackageInfo(var0.getPackageName(), 0);
           G.APP_VERSION = var2.versionName;
           G.APP_PACKAGE = var2.packageName;
           G.FILES_PATH = var0.getFilesDir().getAbsolutePath();
           G.PHONE_MODEL = Build.MODEL;
           G.ANDROID_VERSION = Build.VERSION.RELEASE;
        } catch (PackageManager.NameNotFoundException var3) {
            var3.printStackTrace();
        }

        DebugLog.i(TAG + " : TRACE_VERSION: " + G.TraceVersion);
        DebugLog.i(TAG + " : APP_VERSION: " +G.APP_VERSION);
        DebugLog.i(TAG + " : APP_PACKAGE: " +G.APP_PACKAGE);
        DebugLog.i(TAG + " : FILES_PATH: " +G.FILES_PATH);
        DebugLog.i(TAG + " : URL: " +G.URL);
        boolean var4 = false;
        if(searchForStackTraces().length > 0) {
            var4 = true;
        }

        (new Thread() {
            public void run() {
                ExceptionHandler.submitStackTraces();
                UncaughtExceptionHandler var1 = Thread.getDefaultUncaughtExceptionHandler();
                if(var1 != null) {
                    Log.d(ExceptionHandler.TAG, "current handler class=" + var1.getClass().getName());
                }

                if(!(var1 instanceof DefaultExceptionHandler)) {
                    Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(var1));
                }

            }
        }).start();
        return var4;
    }

    public static void register(Context var0, String var1) {
        Log.i(TAG, "Registering default exceptions handler: " + var1);
        G.URL = var1;
        register(var0);
    }

    private static String[] searchForStackTraces() {
        if(stackTraceFileList != null) {
            return stackTraceFileList;
        } else {
            File var0 = new File(G.FILES_PATH + "/");
            var0.mkdir();
            FilenameFilter var1 = new FilenameFilter() {
                public boolean accept(File var1, String var2) {
                    return var2.endsWith(".stacktrace");
                }
            };
            return stackTraceFileList = var0.list(var1);
        }
    }

    public static void submitStackTraces() {
        boolean var22 = false;

        String[] var0;
        int var1;
        File var28;
        label166: {
            try {
                var22 = true;
                Log.d(TAG, "Looking for exceptions in: " +G.FILES_PATH);
                var0 = searchForStackTraces();
                if(var0 != null) {
                    if(var0.length <= 0) {
                        var22 = false;
                        break label166;
                    }

                    Log.d(TAG, "Found " + var0.length + " stacktrace(s)");

                    for(var1 = 0; var1 < var0.length; ++var1) {
                        String var2 =G.FILES_PATH + "/" + var0[var1];
                        String var3 = var0[var1].split("-")[0];
                        Log.d(TAG, "Stacktrace in file \'" + var2 + "\' belongs to version " + var3);
                        StringBuilder var4 = new StringBuilder();
                        BufferedReader var5 = new BufferedReader(new FileReader(var2));
                        String var6 = null;
                        String var7 = null;
                        String var8 = null;
                        var4.append(System.getProperty("line.separator"));
                        var4.append("====================================================================================================================================");
                        var4.append(System.getProperty("line.separator"));
                        var4.append("====================================================================================================================================");
                        var4.append(System.getProperty("line.separator"));
                        var4.append("ANDROID_VERSION: " +Build.VERSION.RELEASE);
                        var4.append(System.getProperty("line.separator"));
                        var4.append("APP_VERSION: "  + G.APP_VERSION);
                        var4.append(System.getProperty("line.separator"));
                        var4.append("APP_PACKAGE: " +G.APP_PACKAGE);
                        var4.append(System.getProperty("line.separator"));
                        var4.append("Board: " + Build.BOARD);
                        var4.append(System.getProperty("line.separator"));
                        var4.append("Brand: "+ Build.BRAND);
                        var4.append(System.getProperty("line.separator"));
                        var4.append("Device: "+ Build.DEVICE);
                        var4.append(System.getProperty("line.separator"));
                        var4.append("Model: "+ Build.MODEL);
                        var4.append(System.getProperty("line.separator"));
                        SimpleDateFormat mDateFormat= new SimpleDateFormat();
                        mDateFormat.applyPattern("dd/MM/yyyy hh:mm:ss");
                        var4.append("Device Time When Generate Error: " + mDateFormat.format(Calendar.getInstance().getTime()));
                        var4.append(System.getProperty("line.separator"));
                        var4.append("====================================================================================================================================");
                        var4.append(System.getProperty("line.separator"));
                        var4.append("====================================================================================================================================");
                        var4.append(System.getProperty("line.separator"));
                        while((var6 = var5.readLine()) != null) {
                            if(var7 == null) {
                                var7 = var6;
                            } else if(var8 == null) {
                                var8 = var6;
                            } else {
                                var4.append(var6);
                                var4.append(System.getProperty("line.separator"));
                            }
                        }
                        var4.append(System.getProperty("line.separator"));
                        var4.append("====================================================================================================================================");
                        var4.append(System.getProperty("line.separator"));
                        var4.append("http://hyperlinkinfosystem.com");
                        var4.append(System.getProperty("line.separator"));
                        var4.append(G.TraceAVersion);
                        var4.append(System.getProperty("line.separator"));
                        var4.append("====================================================================================================================================");
                        var5.close();
                        String var9 = var4.toString();
                        Log.d(TAG, "Transmitting stack trace: " + var9);
                        DefaultHttpClient var10 = new DefaultHttpClient();
                        HttpPost var11 = new HttpPost(G.URL);
                        ArrayList var12 = new ArrayList();
                        var12.add(new BasicNameValuePair("package_name",G.APP_PACKAGE));
                        var12.add(new BasicNameValuePair("package_version", var3));
                        var12.add(new BasicNameValuePair("phone_model", var8));
                        var12.add(new BasicNameValuePair("android_version", var7));
                        var12.add(new BasicNameValuePair("stacktrace", var9));
                        var11.setEntity(new UrlEncodedFormEntity(var12, "UTF-8"));
                        var10.execute(var11);
                    }

                    var22 = false;
                    break label166;
                }

                var22 = false;
                break label166;
            } catch (Exception var26) {
                var26.printStackTrace();
                var22 = false;
            } finally {
                if(var22) {
                    try {
                        String[] var14 = searchForStackTraces();

                        for(int var15 = 0; var15 < var14.length; ++var15) {
                            File var16 = new File(G.FILES_PATH + "/" + var14[var15]);
                            var16.delete();
                        }
                    } catch (Exception var23) {
                        var23.printStackTrace();
                    }

                }
            }

            try {
                var0 = searchForStackTraces();

                for(var1 = 0; var1 < var0.length; ++var1) {
                    var28 = new File(G.FILES_PATH + "/" + var0[var1]);
                    var28.delete();
                }

                return;
            } catch (Exception var24) {
                var24.printStackTrace();
                return;
            }
        }

        try {
            var0 = searchForStackTraces();

            for(var1 = 0; var1 < var0.length; ++var1) {
                var28 = new File(G.FILES_PATH + "/" + var0[var1]);
                var28.delete();
            }
        } catch (Exception var25) {
            var25.printStackTrace();
        }

    }
}
