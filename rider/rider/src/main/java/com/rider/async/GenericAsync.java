package com.rider.async;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.rider.navigator.OnTaskComplete;
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
public class GenericAsync extends AsyncTask<Void, Void, Response> {


    private final String accessToken;
    String requestUrl ;

    HashMap<String, String> param;

    OnTaskComplete<Response> callback;
    boolean success = true;
    Context context;


    public GenericAsync(Context context,@Nullable String requesturl, @Nullable String accessToken, OnTaskComplete<Response> callback) {

        this.context = context;
        this.requestUrl=requesturl;
        this.callback = callback;
        this.accessToken = accessToken;
    }

    public GenericAsync(Context context,@Nullable String requesturl, OnTaskComplete<Response> callback) {

        this.context = context;
        this.requestUrl=requesturl;
        this.callback = callback;
        this.accessToken = null;
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

        try {

            if(s.code()==401)
            {
               /* Intent intentService = new Intent(context, TrackRideService.class);
                if(LAOXI.currentActivity instanceof HomeActivity){
                    LAOXI.currentActivity.stopService(intentService);
                }

                DataToPref.setSharedPreferanceData(context, LaoxiConstant.STORED_USER, LaoxiConstant.DATA, "");
                DataToPref.setSharedPreferanceData(context, LaoxiConstant.STORED_USER, LaoxiConstant.ISLOGIN, "false");

                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


                context.startActivity(intent);
                ((Activity)context).finish();*/

                DebugLog.e("Responce Error : 401" + s.toString());


//                DataToPref.setSharedPreferanceData(context, LaoxiConstant.STORED_USER, LaoxiConstant.DATA, "");
//                DataToPref.setSharedPreferanceData(context, LaoxiConstant.STORED_USER, LaoxiConstant.ISLOGIN, "false");
//                Intent intent = new Intent(context, LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                if(LAOXI.currentActivity instanceof HomeActivity){
//                    LAOXI.currentActivity.startActivity(intent);
//                    LAOXI.currentActivity.finish();
//                }
            }
            else
            {
                DebugLog.e("Responce ::" + s.toString());
                callback.onSuccess(s, success);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
