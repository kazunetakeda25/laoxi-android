package com.rider.async;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.rider.navigator.OnTaskCompleteNearFreeDriver;
import com.rider.utils.DebugLog;
import com.rider.utils.LaoxiConstant;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Chirag on 11/04/2016.
 */
public class GenericAsyncNearFreeDrivers extends AsyncTask<Void, Void, Response> {


    private final String accessToken;
    String requestUrl ;

    HashMap<String, String> param;

    OnTaskCompleteNearFreeDriver<Response> callback;
    boolean success = true;
    Context context;
    int time;


    public GenericAsyncNearFreeDrivers(int time,Context context, @Nullable String requesturl, @Nullable String accessToken, OnTaskCompleteNearFreeDriver<Response> callback) {

        this.context = context;
        this.requestUrl=requesturl;
        this.callback = callback;
        this.accessToken = accessToken;
        this.time=time;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //AlertUtils.showCustomProgressDialog(context);
    }

    @Override
    protected Response doInBackground(Void... params) {

        Response response = null;


        DebugLog.e("Access Token::" + accessToken);
        DebugLog.e("Request Url ::" + requestUrl);
        Request request;
        if (accessToken == null) {
            request = new Request.Builder()
                    .addHeader(LaoxiConstant.HEADER_AUTHORIZATION, LaoxiConstant.AUTHORIZATION_KEY_VALUE)

                    .url(requestUrl).build();
        } else {
            request = new Request.Builder()
                    .addHeader(LaoxiConstant.HEADER_AUTHORIZATION, LaoxiConstant.AUTHORIZATION_KEY_VALUE)
                    .addHeader(LaoxiConstant.HEADER_TOCKEN, accessToken)
                    .url(requestUrl).build();
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        try {
            //DebugLog.e("Request request ::" + request.toString());
            response = client.newCall(request).execute();

        } catch (IOException ex) {
            success = false;
            callback.onFailure();
            DebugLog.e("Error " + ex);
        }

        return response;
    }

    @Override
    protected void onPostExecute(Response s) {
        super.onPostExecute(s);

        //AlertUtils.dismissDialog();

        //Toast.makeText(context, "Post is called", Toast.LENGTH_SHORT).show();

        try {

            if(s.code()==401)
            {

                /*DataToPref.setSharedPreferanceData(context, LaoxiConstant.STORED_USER, LaoxiConstant.DATA, "");
                DataToPref.setSharedPreferanceData(context, LaoxiConstant.STORED_USER, LaoxiConstant.ISLOGIN, "false");


                Intent intentService = new Intent(context, TrackRideService.class);
                if(LAOXI.currentActivity instanceof HomeActivity){
                    LAOXI.currentActivity.stopService(intentService);
                }



                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);*/

                DebugLog.e("Responce Error : 401" + s.toString());

//                DataToPref.setSharedPreferanceData(context, LaoxiConstant.STORED_USER, LaoxiConstant.DATA, "");
//                DataToPref.setSharedPreferanceData(context, LaoxiConstant.STORED_USER, LaoxiConstant.ISLOGIN, "false");
//                Intent intent = new Intent(context, LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                if(LAOXI.currentActivity instanceof HomeActivity){
//                    LAOXI.currentActivity.startActivity(intent);
//                    LAOXI.currentActivity.finish();
//                }

            }
            else
            {
                DebugLog.e("Responce ::" + s.toString());
                callback.onSuccess(time, s, success);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
