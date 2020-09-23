package com.rider.dao;


import com.rider.navigator.OnTaskComplete;
import com.rider.navigator.OnTaskCompleteNearFreeDriver;

import java.util.HashMap;

/**
 * Created by chirag on 14/4/16.
 */
public interface CommonInterface {

    void doLogin(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void doSignUp(HashMap<String, String> param, String accesstoken, String path, OnTaskComplete onTaskComplete);

    void doEditProfile(HashMap<String, String> param, String accesstoken, String path, OnTaskComplete onTaskComplete);

    void doForgotPassword(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void doNearFreeDriver(int time,HashMap<String, String> param, String accesstoken, OnTaskCompleteNearFreeDriver onTaskComplete);

    void doPlaceOrder(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void doFindDriver(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void doFareEstimation(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void doStateManagement(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void doPastOrder(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void doFutureOrder(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void doCancelOrder(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void doLogOut(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void doGetDriverDetails(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void doRateAndReview(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void doTrackRide(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void doPayment(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void doAddCard(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void doEnterDropOff(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void doChangePassword(HashMap<String, String> param, String accessToken, OnTaskComplete onTaskComplete);

    void doGetFareEstimate(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void wsCallSupport(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void wsCallAddNewCard(HashMap<String, String> param, String accesstoken, String path, OnTaskComplete onTaskComplete);

    void doDeleteCard(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void doSms(HashMap<String, String> param, String accesstoken, String path, OnTaskComplete onTaskComplete);

    void doCarListLater(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void doGetLocation(HashMap<String, String> param, String url, OnTaskComplete onTaskComplete);

    //Added by prakash jyani for call put method for near free driver

    void doNearFreeDriverNew(int time,HashMap<String, String> param, String accesstoken, OnTaskCompleteNearFreeDriver onTaskComplete);

    void doGetOrderDetails(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void doVerifyOTP(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void doResendOTP(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void addFavouriteDriver(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void removeFavouriteDriver(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

    void redeemPromoCode(HashMap<String, String> param, String accesstoken, OnTaskComplete onTaskComplete);

 }
