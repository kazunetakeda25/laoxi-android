package com.driver.dao.homedao;

import android.support.annotation.Nullable;

import com.driver.asynctask.GenericAsync;
import com.driver.asynctask.GenericMultipartAsync;
import com.driver.asynctask.GenericPostMultipalImagesAsync;
import com.driver.asynctask.GenericPostMultipartAsync;
import com.driver.asynctask.GenericPutAsync;
import com.driver.dao.IAsyncCompleteCallback;
import com.driver.util.AppHelper;
import com.driver.util.ConstantClass;

import java.util.HashMap;

import okhttp3.Response;

/**
 * Created by Chirag Solanki on 06/08/2016.
 */
public class HomeActivityDAO implements IHomeWs {
    private static HomeActivityDAO ourInstance = new HomeActivityDAO();

    public static HomeActivityDAO getInstance() {
        return ourInstance;
    }

    public HomeActivityDAO() {
    }

    @Override
    public void callLogoutWS(HashMap<String, String> param, String methodName, final IAsyncCompleteCallback callback) {
//        LoginResponse data = LAOXI.getInstance().getLoginUser();
        String accessToken = AppHelper.getInstance().getToken();
        new GenericPutAsync(param, methodName, accessToken, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();
    }

    @Override
    public void callDeclineRequestWS(HashMap<String, String> param, String methodName, final IAsyncCompleteCallback callback) {
//        LoginResponse data = LAOXI.getInstance().getLoginUser();
        String accessToken = AppHelper.getInstance().getToken();
        new GenericAsync(param, methodName, accessToken,
                new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();
    }

    @Override
    public void callAcceptOrderWS(HashMap<String, String> param, String methodName, final IAsyncCompleteCallback callback) {
//        LoginResponse data = LAOXI.getInstance().getLoginUser();
        String accessToken = AppHelper.getInstance().getToken();
        new GenericAsync(param, methodName, accessToken,
                new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();
    }

    @Override
    public void callArrivedAtPickupLocationWs(HashMap<String, String> param, String methodName, final IAsyncCompleteCallback callback) {
//        LoginResponse data = LAOXI.getInstance().getLoginUser();
        String accessToken = AppHelper.getInstance().getToken();
        new GenericAsync(param, methodName, accessToken, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();
    }

    @Override
    public void callPickupWS(HashMap<String, String> param, String methodName, final IAsyncCompleteCallback callback) {
//        LoginResponse data = LAOXI.getInstance().getLoginUser();
        String accessToken = AppHelper.getInstance().getToken();
        new GenericAsync(param, methodName, accessToken, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();
    }

    @Override
    public void callDropOff_TripWS(HashMap<String, String> param, String methodName, final IAsyncCompleteCallback<Response> callback) {
//        LoginResponse data = LAOXI.getInstance().getLoginUser();
        String accessToken = AppHelper.getInstance().getToken();
        new GenericMultipartAsync(param, null, null, accessToken, methodName, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();
    }

    @Override
    public void callForgetPasswordWS(HashMap<String, String> param, String methodName, final IAsyncCompleteCallback callback) {
        new GenericAsync(param, methodName, null, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();
    }

    @Override
    public void callGetProfileData(HashMap<String, String> param, String methodName, String token, final IAsyncCompleteCallback callback) {
        new GenericAsync(param, methodName, token, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();
    }

    @Override
    public void callChangePassword(HashMap<String, String> param, String methodName, String token, final IAsyncCompleteCallback callback) {

        new GenericPutAsync(param, methodName, token, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();
    }

    @Override
    public void callUpdateProfile(HashMap<String, String> param, String filename, String path, String token, String methodName, final IAsyncCompleteCallback callback) {
        String requestUrl = ConstantClass.BASE_URL + ConstantClass.METHOD_UPDATE_PROFILE;

        new GenericPostMultipartAsync(param, filename, path, token, methodName, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();
    }

    @Override
    public void callChangeServiceStatus(HashMap<String, String> param, String methodName, String token, final IAsyncCompleteCallback callback) {

        new GenericPutAsync(param, methodName, token, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();
    }

    @Override
    public void callUpdateLatLong(HashMap<String, String> param, String methodName, String token, final IAsyncCompleteCallback callback) {
        new GenericPutAsync(param, methodName, token, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();
    }

    @Override
    public void callCancelTripWS(HashMap<String, String> param, String methodName, String token, final IAsyncCompleteCallback callback) {
        new GenericPutAsync(param, methodName, token, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();

    }

    @Override
    public void callOnlineSupportWS(HashMap<String, String> param, String methodName, String token, final IAsyncCompleteCallback callback) {
        new GenericPutAsync(param, methodName, token, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();
    }

    @Override
    public void callFeedBack(HashMap<String, String> param, String methodName, String token, final IAsyncCompleteCallback callback) {


        new GenericPostMultipartAsync(param, null, null, token, methodName, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();

    }

    @Override
    public void callUpcomingRide(HashMap<String, String> param, String methodName, String token, final IAsyncCompleteCallback callback) {
        new GenericAsync(param, methodName, token, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();
    }

    @Override
    public void callPastRide(HashMap<String, String> param, String methodName, String token, final IAsyncCompleteCallback callback) {
        new GenericAsync(param, methodName, token, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();
    }

    @Override
    public void callUpdateProfileForMultipleImage(HashMap<String, String> param, String filename, String path, String accessToken, String methodName, final IAsyncCompleteCallback callback) {
        String requestUrl = ConstantClass.BASE_URL + ConstantClass.METHOD_UPDATE_PROFILE;

        new GenericPostMultipalImagesAsync(param, filename, path, accessToken, methodName, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();
    }

    @Override
    public void doSms(HashMap<String, String> param, String accesstoken, String path, final String methodName, final IAsyncCompleteCallback callback) {
        String requestUrl = ConstantClass.BASE_URL + ConstantClass.METHOD_SEND_SMS;

        new GenericPostMultipalImagesAsync(param, "", path, accesstoken, methodName, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();
    }

    @Override
    public void doGetOrderDetailWS(HashMap<String, String> param, String methodName, final IAsyncCompleteCallback callback) {
//        LoginResponse data = LAOXI.getInstance().getLoginUser();
        String accessToken = AppHelper.getInstance().getToken();
        new GenericAsync(param, methodName, accessToken, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();
    }

    @Override
    public void callStateManagement(HashMap<String, String> param, String methodName, final IAsyncCompleteCallback callback) {
//        LoginResponse data = LAOXI.getInstance().getLoginUser();
        String accessToken = AppHelper.getInstance().getToken();
        new GenericAsync(param, methodName, accessToken, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();
    }

}
