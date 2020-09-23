package com.rider.async;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.rider.navigator.OnTaskComplete;

import com.rider.utils.DebugLog;
import com.rider.utils.LaoxiConstant;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Chirag on 11/04/2016.
 */
public class GenericPutMultipartAsync extends AsyncTask<Void, Void, Response> {

    private final String methodName;
    private final String accessToken;

    HashMap<String, String> param;

    OnTaskComplete<Response> callback;
    boolean success = true;
    Context context;

    public GenericPutMultipartAsync(Context context, HashMap<String, String> param,
                                    @Nullable String accessToken, String methodName, OnTaskComplete<Response> callback) {

        this.context = context;
        this.methodName = methodName;

        this.param = param;

        this.callback = callback;
        this.accessToken = accessToken;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }


    @Override
    protected Response doInBackground(Void... params) {

        Response response = null;
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        String parameters = "";
        for (Map.Entry < String, String > entry: param.entrySet()) {
            parameters += entry.getKey() + "=" + entry.getValue() + "&";
        }
        if (parameters.length() > 0) {
            parameters.substring(0, parameters.length() - 1);
            DebugLog.i("data is======" + parameters.substring(0, parameters.length() - 1));
        } else {
            DebugLog.e("data is======" + parameters);
        }

        RequestBody body = RequestBody.create(mediaType, parameters);
        Request request;

        request = new Request.Builder()
                .url(methodName)
                .addHeader(LaoxiConstant.HEADER_AUTHORIZATION, LaoxiConstant.AUTHORIZATION_KEY_VALUE)
                .addHeader(LaoxiConstant.HEADER_TOCKEN, accessToken)
                .put(body)
                .build();


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        try {

            DebugLog.e("Request Url ::" + methodName);
            response = client.newCall(request).execute();

        } catch (IOException e) {
            success = false;
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(Response s) {
        super.onPostExecute(s);


        try {
            if(s.code()==401)
            {

                DebugLog.e("GenericPutMultipartAsync Responce Error : 401" + s.toString());

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
                DebugLog.i("GenericPutMultipartAsync Responce :" + s.toString());
                callback.onSuccess(s, success);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
