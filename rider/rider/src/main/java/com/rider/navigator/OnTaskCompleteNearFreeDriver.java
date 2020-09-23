package com.rider.navigator;


import okhttp3.Response;

/**
 * Created by ${Vishnu} on 18/1/16.
 */
public  interface OnTaskCompleteNearFreeDriver<T> {
    public void onSuccess(int time,Response response, boolean success);

    public void onFailure();


}
