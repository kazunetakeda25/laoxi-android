package com.driver.application;

import android.app.Activity;
import android.media.MediaPlayer;
import android.support.multidex.MultiDexApplication;

import com.driver.pojo.CurrentRide;
import com.driver.pojo.loginws.LoginResponse;
import com.driver.util.AnalyticsReporter;

import java.util.ArrayList;


public class LAOXI extends MultiDexApplication {
    public static ArrayList<Activity> activity_List = new ArrayList<Activity>();
    public static Activity currentActivity;
    public static CurrentRide currentRide = new CurrentRide();
    public MediaPlayer mediaPlayer;

    public static void setCurrentActivity(Activity mCurrentActivity) {
        try {
            activity_List.add(mCurrentActivity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearActivity() {

        try {
            if (activity_List.size() > 0) {
                for (int i = 0; i < activity_List.size(); i++) {
                    activity_List.get(i).finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LoginResponse loginUser;

    public LoginResponse getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(LoginResponse loginUser) {
        this.loginUser = loginUser;
    }

    private static LAOXI singleton;

    public static LAOXI getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //rhc commented
        //Fabric.with(this, new Crashlytics());
        AnalyticsReporter.getInstance().start(this);

        singleton = this;
        //rhc commented
        /*
        Bugfender.init(this, "P8iGYYK07LoD46YZHKEQ01LSPigSJhy4", BuildConfig.DEBUG);
        if (AppHelper.getInstance().getDriverId() != null)
            Bugfender.setDeviceInteger("driver", Integer.parseInt(AppHelper.getInstance().getDriverId()));
        */
    }
}
