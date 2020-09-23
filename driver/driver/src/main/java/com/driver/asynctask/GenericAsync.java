package com.driver.asynctask;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.driver.dao.IAsyncCompleteCallback;
import com.driver.util.ConstantClass;
import com.driver.util.DebugLog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Chirag on 11/04/2016.
 */
public class GenericAsync extends AsyncTask<Void, Void, Response> {
    private final String methodName;
    private final String accessToken;

    HashMap<String, String> param;

    IAsyncCompleteCallback<Response> callback;
    boolean success = true;


    public GenericAsync(@Nullable HashMap<String, String> param, String methodName, @Nullable String accessToken, IAsyncCompleteCallback<Response> callback) {
        this.methodName = methodName;
        this.param = param;
        this.callback = callback;
        this.accessToken = accessToken;
    }

    @Override
    protected Response doInBackground(Void... params) {

        Response response = null;
        String requestUrl = buildRequestUrl();

        DebugLog.e("Access Token::" + accessToken);
        DebugLog.e("Request Url ::" + requestUrl);
        Request request;
        if (accessToken == null) {
            request = new Request.Builder()
                    .addHeader(ConstantClass.HEADER_AUTHORIZATION, ConstantClass.AUTHORIZATION_KEY_VALUE)
                    .url(requestUrl).build();
        } else {
            request = new Request.Builder()
                    .addHeader(ConstantClass.HEADER_AUTHORIZATION, ConstantClass.AUTHORIZATION_KEY_VALUE)
                    .addHeader(ConstantClass.HEADER_TOCKEN, accessToken)
                    .url(requestUrl).build();
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        try {

            response = client.newCall(request).execute();

        } catch (IOException ex) {
            success = false;
            DebugLog.e("Error " + ex);
        }


        return response;
    }

    @Override
    protected void onPostExecute(Response s) {
        super.onPostExecute(s);
        callback.onTaskComplete(success, s);

    }

    private String buildRequestUrl() {
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
