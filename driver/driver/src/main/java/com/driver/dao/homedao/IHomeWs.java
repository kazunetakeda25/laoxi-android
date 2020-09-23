package com.driver.dao.homedao;

import com.driver.dao.IAsyncCompleteCallback;

import java.util.HashMap;

import okhttp3.Response;

/**
 * Created by Chirag Solanki on 06/08/2016.
 */
public interface IHomeWs {
    void callLogoutWS(HashMap<String, String> param, String methodName, IAsyncCompleteCallback callback);
    void callDeclineRequestWS(HashMap<String, String> param, String methodName, IAsyncCompleteCallback callback);
    void callAcceptOrderWS(HashMap<String, String> param, String methodName, IAsyncCompleteCallback callback);
    void callArrivedAtPickupLocationWs(HashMap<String, String> param, String methodName, IAsyncCompleteCallback callback);
    void callPickupWS(HashMap<String, String> param, String methodName, IAsyncCompleteCallback callback);
    void callDropOff_TripWS(HashMap<String, String> param, String methodName, IAsyncCompleteCallback<Response> callback);
    void callForgetPasswordWS(HashMap<String, String> param, String methodName, IAsyncCompleteCallback callback);
    void callGetProfileData(HashMap<String, String> param,String methodName,String token,IAsyncCompleteCallback callback);
    void callChangePassword(HashMap<String, String> param,String methodName,String token,IAsyncCompleteCallback callback);
    void callUpdateProfile(HashMap<String, String> param,String filename,String path,String accessToken,String methodName,IAsyncCompleteCallback callback);
    void callChangeServiceStatus(HashMap<String, String> param, String methodName, String token,IAsyncCompleteCallback callback);
    void callUpdateLatLong(HashMap<String, String> param, String methodName, String token, final IAsyncCompleteCallback callback);
    void callCancelTripWS(HashMap<String, String> param, String methodName, String token, final IAsyncCompleteCallback callback);
    void callOnlineSupportWS(HashMap<String, String> param, String methodName, String token, final IAsyncCompleteCallback callback);
    void callFeedBack(HashMap<String, String> param, String methodName, String token, final IAsyncCompleteCallback callback);
    void callUpcomingRide(HashMap<String, String> param, String methodName, String token, final IAsyncCompleteCallback callback);
    void callPastRide(HashMap<String, String> param, String methodName, String token, final IAsyncCompleteCallback callback);
    void callUpdateProfileForMultipleImage(HashMap<String, String> param,String filename,String path,String accessToken,String methodName,IAsyncCompleteCallback callback);
    void doSms(HashMap<String, String> param, String accesstoken, String path,String methodName, IAsyncCompleteCallback callback);
    void doGetOrderDetailWS(HashMap<String, String> param, String methodName, IAsyncCompleteCallback callback);
    void callStateManagement(HashMap<String, String> param, String methodName, IAsyncCompleteCallback callback);
}
