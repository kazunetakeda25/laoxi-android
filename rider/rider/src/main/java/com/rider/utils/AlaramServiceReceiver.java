package com.rider.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Created by darshan on 14/5/15.
 */
public class AlaramServiceReceiver extends android.content.BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Service Stops", "Ohhhhhhh");

    }

}