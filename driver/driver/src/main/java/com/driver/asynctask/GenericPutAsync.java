package com.driver.asynctask;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.driver.dao.IAsyncCompleteCallback;
import com.driver.util.ConstantClass;
import com.driver.util.DebugLog;

import java.io.File;
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


public class GenericPutAsync extends AsyncTask<Void, Void, Response> {

    private final String methodName;
    private final String accessToken;


    HashMap<String, String> param;
    File file;
    IAsyncCompleteCallback<Response> callback;
    boolean success = true;

    public GenericPutAsync(HashMap<String, String> param,
                           String methodName,
                           @Nullable String accessToken,
                           IAsyncCompleteCallback<Response> callback) {
        this.methodName = methodName;
        this.param = param;
        this.callback = callback;
        this.accessToken = accessToken;
        DebugLog.e("Token-------------- "+accessToken);
    }

    @Override
    protected Response doInBackground(Void... params) {
        Response response = null;
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        String parameters = "";
        for (Map.Entry < String, String > entry: param.entrySet()) {
            if(entry.getValue()!=null)
            {
                parameters += entry.getKey() + "=" + entry.getValue() + "&";
            }

        }
        parameters.substring(0, parameters.length() - 1);
        RequestBody body = RequestBody.create(mediaType, parameters);
        Request request;
        DebugLog.e("Request Url ::" + methodName);
        request = new Request.Builder()
                .url(methodName)
                .addHeader(ConstantClass.HEADER_AUTHORIZATION,ConstantClass.AUTHORIZATION_KEY_VALUE)
                .addHeader(ConstantClass.HEADER_TOCKEN, accessToken)
                .put(body)
                .build();


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        try {


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
        callback.onTaskComplete(success, s);
    }
}
