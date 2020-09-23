package com.rider.application;

import android.support.multidex.MultiDexApplication;

import com.google.gson.Gson;
import com.rider.pojo.User;
import com.rider.utils.DataToPref;
import com.rider.utils.LaoxiConstant;

/**
 * Created by Chirag on 06/04/2016.
 */
public class Laoxi extends MultiDexApplication {

    private static Laoxi singleton;

    public static Laoxi getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //rhc commented
        //Fabric.with(this, new Crashlytics());
        singleton = this;


        String strUserData = DataToPref.getSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA);
        Gson mGson = new Gson();
        User user = mGson.fromJson(strUserData, User.class);
        //rhc commented
        /*Bugfender.init(this, "P8iGYYK07LoD46YZHKEQ01LSPigSJhy4", BuildConfig.DEBUG);
        if (user != null)
            Bugfender.setDeviceInteger("rider", Integer.parseInt(user.getId()));*/
    }

}
