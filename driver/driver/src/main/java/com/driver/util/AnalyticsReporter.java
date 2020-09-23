package com.driver.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

/**
 * Created by Ma on 9/7/17.
 */

public class AnalyticsReporter {

    private static final String MIXPANEL_TOKEN = "9a3cada283a5060ed6bb68f2b9d8075e";

    private static AnalyticsReporter mAnalyticsReporter = new AnalyticsReporter();
    private Context mContext;
    //rhc commented
    //private MixpanelAPI mMixpanelAPI;
    private String version = "";

    public static AnalyticsReporter getInstance() {
        return mAnalyticsReporter;
    }

    public void start(Context context) {
        mContext = context;
        //rhc commented
        //mMixpanelAPI = MixpanelAPI.getInstance(context, MIXPANEL_TOKEN);

        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            String versionName = info.versionName;
            int versionCode = info.versionCode;
            version = "v" + versionName + " (" + versionCode + ")";
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        }
    }

    public void setIdentify(String distinctId) {
        //rhc commented
        //mMixpanelAPI.identify(distinctId);
        //mMixpanelAPI.getPeople().identify(distinctId);
    }

    public void removeIdentify() {
        //rhc commented
        /*mMixpanelAPI.identify(null);
        mMixpanelAPI.getPeople().identify(null);*/
    }

    public void setProfile(String driverId,
                           String email,
                           String firstName,
                           String lastName,
                           String deviceId,
                           String deviceType,
                           String lastLogin) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("driver_id", driverId);
            properties.put("email", email);
            properties.put("first_name", firstName);
            properties.put("last_name", lastName);
            properties.put("device_id", deviceId);
            properties.put("device_type", deviceType);
            properties.put("last_login", lastLogin);
            properties.put("version", version);
            properties.put("package_name", mContext.getPackageName());
            //mMixpanelAPI.getPeople().set(properties); rhc commented
        } catch (JSONException e) {
            DebugLog.e(e.getLocalizedMessage());
        }
    }

    public void track(String eventName, Bundle properties) {
        //rhc commented
/*        if (mMixpanelAPI != null) {
            JSONObject jsonProperties = new JSONObject();
            Set<String> keys = properties.keySet();
            try {
                for (String key : keys) {
                    jsonProperties.put(key, JSONObject.wrap(properties.get(key)));
                }
                jsonProperties.put("version", version);
                jsonProperties.put("package_name", mContext.getPackageName());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mMixpanelAPI.track(eventName, jsonProperties);
        }*/
    }

    public static class Event {
        public static final String DRIVER_LOGIN = "driver_login";
        public static final String DRIVER_RECEIVED_FCM_MESSAGE = "driver_received_fcm_message";
        public static final String DRIVER_RECEIVED_ORDER_REQUEST = "driver_received_order_request";
        public static final String DRIVER_RECEIVED_PAYMENT = "driver_received_payment";
        public static final String DRIVER_RECEIVED_ORDER_CANCELED = "driver_received_order_canceled";
        public static final String DRIVER_ACCEPTED_ORDER_REQUEST = "driver_accepted_order_request";
        public static final String DRIVER_DECLINED_ORDER_REQUEST = "driver_declined_order_request";
        public static final String DRIVER_ARRIVED_AT_PICKUP = "driver_arrived_at_pickup";
        public static final String DRIVER_STARTED_RIDE = "driver_started_ride";
        public static final String DRIVER_COMPLETED_RIDE = "driver_completed_ride";
        public static final String DRIVER_CANCELED_RIDE = "driver_canceled_ride";
        public static final String DRIVER_LOGOUT = "driver_logout";

        protected Event() {
        }
    }
}
