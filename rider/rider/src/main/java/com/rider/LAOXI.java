package com.rider;

import android.app.Activity;
import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.rider.pojo.CurrentRide;

import java.util.ArrayList;

public class LAOXI extends MultiDexApplication {
    public static ArrayList<Activity> activity_List = new ArrayList<Activity>();
    public static Activity currentActivity;
    public static CurrentRide currentRide = new CurrentRide();

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
}