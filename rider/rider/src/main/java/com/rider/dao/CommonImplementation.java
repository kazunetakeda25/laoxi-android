package com.rider.dao;


import android.content.Context;
import android.os.AsyncTask;

import com.rider.R;
import com.rider.application.Laoxi;
import com.rider.async.GenericAsync;
import com.rider.async.GenericAsyncNearFreeDrivers;
import com.rider.async.GenericPostMultipartAsync;
import com.rider.async.GenericPutMultipartAsync;
import com.rider.async.GenericPutMultipartAsyncNearFreeDriver;
import com.rider.navigator.OnTaskComplete;
import com.rider.navigator.OnTaskCompleteNearFreeDriver;
import com.rider.utils.DebugLog;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by chirag on 14/4/16.
 */
public class CommonImplementation implements CommonInterface {

    String requesturl;
    Context context = Laoxi.getInstance();

    public static CommonImplementation ourInstance = new CommonImplementation();
    OnTaskComplete onTaskComplete;
    OnTaskCompleteNearFreeDriver onTaskCompleteNearFreeDriver;
    GenericAsyncNearFreeDrivers genericAsyncNearFreeDrivers;
    GenericPutMultipartAsyncNearFreeDriver genericPutMultipartAsyncNearFreeDriver;

    public static CommonImplementation getInstance() {
        return ourInstance;
    }

    private CommonImplementation() {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void doLogin(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.LOGIN);
        new GenericPutMultipartAsync(Laoxi.getInstance(), param, accesstoken, requestUrl, onTaskComplete).execute();
    }

    @Override
    public void doSignUp(HashMap<String, String> param, String accesstoken, String path, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.SIGNUP);
        new GenericPostMultipartAsync(context, param, "", path, null, requestUrl, new OnTaskComplete<Response>() {
            @Override
            public void onSuccess(Response response, boolean success) {
                onTaskComplete.onSuccess(response, success);
            }

            @Override
            public void onFailure() {
                onTaskComplete.onFailure();
            }
        }).execute();
    }

    @Override
    public void doEditProfile(HashMap<String, String> param, String accesstoken, String path, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.EDIT_PROFILE);
        new GenericPostMultipartAsync(context, param, "", path, accesstoken, requestUrl, new OnTaskComplete<Response>() {
            @Override
            public void onSuccess(Response response, boolean success) {
                onTaskComplete.onSuccess(response, success);
            }

            @Override
            public void onFailure() {
                onTaskComplete.onFailure();
            }
        }).execute();
    }

    @Override
    public void doForgotPassword(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = buildRequestUrl(param, context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.FORGOT_PASSWORD));
        new GenericAsync(Laoxi.getInstance(), requestUrl, accesstoken, onTaskComplete).execute();
    }

    @Override
    public void doNearFreeDriver(int time, HashMap<String, String> param, String accesstoken, final OnTaskCompleteNearFreeDriver onTaskComplete) {
        this.onTaskCompleteNearFreeDriver = onTaskComplete;
        String requestUrl = buildRequestUrl(param, context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.NEAR_FREE_DRIVER));


        if (genericAsyncNearFreeDrivers != null) {
            genericAsyncNearFreeDrivers.cancel(true);
        }
        genericAsyncNearFreeDrivers = new GenericAsyncNearFreeDrivers(time, Laoxi.getInstance(), requestUrl, accesstoken, new OnTaskCompleteNearFreeDriver<Response>() {
            @Override
            public void onSuccess(int time, Response response, boolean success) {

                onTaskCompleteNearFreeDriver.onSuccess(time, response, success);
            }

            @Override
            public void onFailure() {
                onTaskComplete.onFailure();
            }
        });

        genericAsyncNearFreeDrivers.execute();
    }


    @Override
    public void doPlaceOrder(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.PLACE_ORDER);
        new GenericPostMultipartAsync(context, param, "", null, accesstoken, requestUrl, onTaskComplete).execute();
    }

    @Override
    public void doFindDriver(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = buildRequestUrl(param, context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.FIND_DRIVER));
        new GenericAsync(Laoxi.getInstance(), requestUrl, accesstoken, onTaskComplete).execute();
    }

    @Override
    public void doFareEstimation(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.FARE_ESTIMATION);
        new GenericPutMultipartAsync(Laoxi.getInstance(), param, accesstoken, requestUrl, onTaskComplete).execute();
    }

    @Override
    public void doStateManagement(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = buildRequestUrl(param, context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.ORDER_STATUS));
        new GenericAsync(Laoxi.getInstance(), requestUrl, accesstoken, onTaskComplete).execute();
    }


    @Override
    public void doPastOrder(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = buildRequestUrl(param, context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.PAST_ORDER));
        new GenericAsync(Laoxi.getInstance(), requestUrl, accesstoken, onTaskComplete).execute();
    }

    @Override
    public void doFutureOrder(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = buildRequestUrl(param, context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.FUTURE_ORDER));
        new GenericAsync(Laoxi.getInstance(), requestUrl, accesstoken, onTaskComplete).execute();
    }

    @Override
    public void doCancelOrder(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.CANCEL_ORDER);
        new GenericPutMultipartAsync(context, param, accesstoken, requestUrl, new OnTaskComplete<Response>() {
            @Override
            public void onSuccess(Response response, boolean success) {
                onTaskComplete.onSuccess(response, success);
            }

            @Override
            public void onFailure() {
                onTaskComplete.onFailure();
            }
        }).execute();
    }

    @Override
    public void doLogOut(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.LOGOUT);
        new GenericPutMultipartAsync(context, param, accesstoken, requestUrl, new OnTaskComplete<Response>() {
            @Override
            public void onSuccess(Response response, boolean success) {
                onTaskComplete.onSuccess(response, success);
            }

            @Override
            public void onFailure() {
                onTaskComplete.onFailure();
            }
        }).execute();
    }

    @Override
    public void doGetDriverDetails(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = buildRequestUrl(param, context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.GET_DRIVER_DETAILS));
        new GenericAsync(Laoxi.getInstance(), requestUrl, accesstoken, onTaskComplete).execute();
    }

    @Override
    public void doRateAndReview(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.RATE_AND_REVIEW);
        new GenericPostMultipartAsync(context, param, "", null, accesstoken, requestUrl, new OnTaskComplete<Response>() {
            @Override
            public void onSuccess(Response response, boolean success) {
                onTaskComplete.onSuccess(response, success);
            }

            @Override
            public void onFailure() {
                onTaskComplete.onFailure();
            }
        }).execute();
    }

    @Override
    public void doTrackRide(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = buildRequestUrl(param, context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.TRACK_RIDE));
        new GenericAsync(Laoxi.getInstance(), requestUrl, accesstoken, onTaskComplete).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void doPayment(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.PAYMENT);
        new GenericPutMultipartAsync(context, param, accesstoken, requestUrl, new OnTaskComplete<Response>() {
            @Override
            public void onSuccess(Response response, boolean success) {
                onTaskComplete.onSuccess(response, success);
            }

            @Override
            public void onFailure() {
                onTaskComplete.onFailure();
            }
        }).execute();
    }

    @Override
    public void doAddCard(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.RATE_AND_REVIEW);
        new GenericPostMultipartAsync(context, param, "", null, accesstoken, requestUrl, new OnTaskComplete<Response>() {
            @Override
            public void onSuccess(Response response, boolean success) {
                onTaskComplete.onSuccess(response, success);
            }

            @Override
            public void onFailure() {
                onTaskComplete.onFailure();
            }
        }).execute();
    }

    @Override
    public void doEnterDropOff(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.UPDATE_DROPOFF);
        new GenericPutMultipartAsync(context, param, accesstoken, requestUrl, new OnTaskComplete<Response>() {
            @Override
            public void onSuccess(Response response, boolean success) {
                onTaskComplete.onSuccess(response, success);
            }

            @Override
            public void onFailure() {
                onTaskComplete.onFailure();
            }
        }).execute();
    }

    @Override
    public void doChangePassword(HashMap<String, String> param, String accessToken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.CHANGE_PASSWORD);
        new GenericPutMultipartAsync(Laoxi.getInstance(), param, accessToken, requestUrl, onTaskComplete).execute();

    }

    @Override
    public void doGetFareEstimate(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.GET_FARe_estimate);
        new GenericPutMultipartAsync(Laoxi.getInstance(), param, accesstoken, requestUrl, onTaskComplete).execute();
    }

    @Override
    public void wsCallSupport(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.ONLINE_SUPPORT);
        new GenericPutMultipartAsync(Laoxi.getInstance(), param, accesstoken, requestUrl, onTaskComplete).execute();
    }


    @Override
    public void wsCallAddNewCard(HashMap<String, String> param, String accesstoken, String path, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.ADD_NEW_CARD);
        new GenericPostMultipartAsync(context, param, "", path, accesstoken, requestUrl, new OnTaskComplete<Response>() {
            @Override
            public void onSuccess(Response response, boolean success) {
                onTaskComplete.onSuccess(response, success);
            }

            @Override
            public void onFailure() {
                onTaskComplete.onFailure();
            }
        }).execute();
    }

    @Override
    public void doDeleteCard(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = buildRequestUrl(param, context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.DELETE_PAYMENT_CARD));
        new GenericAsync(Laoxi.getInstance(), requestUrl, accesstoken, new OnTaskComplete<Response>() {
            @Override
            public void onSuccess(Response response, boolean success) {
                onTaskComplete.onSuccess(response, success);
            }

            @Override
            public void onFailure() {
                onTaskComplete.onFailure();
            }
        }).execute();
    }

    @Override
    public void doSms(HashMap<String, String> param, String accesstoken, String path, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.SEND_SMS);
        new GenericPostMultipartAsync(context, param, "", path, accesstoken, requestUrl, new OnTaskComplete<Response>() {
            @Override
            public void onSuccess(Response response, boolean success) {
                onTaskComplete.onSuccess(response, success);
            }

            @Override
            public void onFailure() {
                onTaskComplete.onFailure();
            }
        }).execute();
    }

    @Override
    public void doCarListLater(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.GET_CARLIST);
        new GenericPutMultipartAsync(Laoxi.getInstance(), param, accesstoken, requestUrl, onTaskComplete).execute();
    }

    @Override
    public void doGetLocation(HashMap<String, String> param, String url, final OnTaskComplete onTaskComplete) {
        new GenericAsync(Laoxi.getInstance(), url, onTaskComplete).execute();

    }

    //Added by prakash for call near free driver


    @Override
    public void doNearFreeDriverNew(int time, HashMap<String, String> param, String accesstoken, final OnTaskCompleteNearFreeDriver onTaskComplete) {
        this.onTaskCompleteNearFreeDriver = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.NEAR_FREE_DRIVER);
        if (genericPutMultipartAsyncNearFreeDriver != null) {
            genericPutMultipartAsyncNearFreeDriver.cancel(true);
        }
        genericPutMultipartAsyncNearFreeDriver = new GenericPutMultipartAsyncNearFreeDriver(time, Laoxi.getInstance(), param, accesstoken, requestUrl, new OnTaskCompleteNearFreeDriver<Response>() {
            @Override
            public void onSuccess(int time, Response response, boolean success) {

                onTaskCompleteNearFreeDriver.onSuccess(time, response, success);
            }

            @Override
            public void onFailure() {
                onTaskComplete.onFailure();
            }
        });

        genericPutMultipartAsyncNearFreeDriver.execute();
    }


    //modify by prakash for getting order  details

    @Override
    public void doGetOrderDetails(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = buildRequestUrl(param, context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.GET_ORDER_DETAILS));

        new GenericAsync(Laoxi.getInstance(), requestUrl, accesstoken, onTaskComplete).execute();

    }

    @Override
    public void doVerifyOTP(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.VERIFY_OTP);
        new GenericPutMultipartAsync(Laoxi.getInstance(), param, accesstoken, requestUrl, onTaskComplete).execute();

    }

    @Override
    public void doResendOTP(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.RESEND_OTP);
        new GenericPutMultipartAsync(Laoxi.getInstance(), param, accesstoken, requestUrl, onTaskComplete).execute();

    }

    @Override
    public void addFavouriteDriver(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.ADD_FAVOURITE_DRIVER);
        new GenericPostMultipartAsync(context, param, "", null, accesstoken, requestUrl, new OnTaskComplete<Response>() {
            @Override
            public void onSuccess(Response response, boolean success) {
                onTaskComplete.onSuccess(response, success);
            }

            @Override
            public void onFailure() {
                onTaskComplete.onFailure();
            }
        }).execute();
    }

    @Override
    public void removeFavouriteDriver(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.REMOVE_FAVOURITE_DRIVER);
        new GenericPostMultipartAsync(context, param, "", null, accesstoken, requestUrl, new OnTaskComplete<Response>() {
            @Override
            public void onSuccess(Response response, boolean success) {
                onTaskComplete.onSuccess(response, success);
            }

            @Override
            public void onFailure() {
                onTaskComplete.onFailure();
            }
        }).execute();
    }

    @Override
    public void redeemPromoCode(HashMap<String, String> param, String accesstoken, final OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
        String requestUrl = context.getResources().getString(R.string.MAINURL) + context.getResources().getString(R.string.REDEEM_PROMO_CODE);
        new GenericPostMultipartAsync(Laoxi.getInstance(), param, "", null, accesstoken, requestUrl, onTaskComplete).execute();
    }


    private String buildRequestUrl(HashMap<String, String> param, String methodName) {
        String method = methodName;
        if (param != null) {
            for (Map.Entry<String, String> entry : param.entrySet()) {
                DebugLog.d(entry.getKey() + "  " + entry.getValue());
                method = method + "/" + entry.getKey() + "/" + entry.getValue();
            }
        }
        return method;
    }
}
