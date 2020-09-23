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
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Chirag on 11/04/2016.
 */
public class GenericMultipartAsync extends AsyncTask<Void, Void, Response> {

    private final String methodName;
    private final String accessToken;
    private final String fileParamName;

    HashMap<String, String> param;
    File file;
    IAsyncCompleteCallback<Response> callback;
    boolean success = true;

    public GenericMultipartAsync(HashMap<String, String> param,
                                 @Nullable String fileParamName,
                                 @Nullable File file,
                                 @Nullable String accessToken,
                                 String methodName,
                                 IAsyncCompleteCallback<Response> callback) {
        this.methodName = methodName;
        this.fileParamName = fileParamName;
        this.param = param;
        this.file = file;
        this.callback = callback;
        this.accessToken = accessToken;
    }

    @Override
    protected Response doInBackground(Void... params) {

        Response response = null;
        MultipartBody.Builder multipartBody = new MultipartBody.Builder();
        multipartBody.setType(MultipartBody.FORM);
        for (Map.Entry<String, String> entry : param.entrySet()) {
            multipartBody.addFormDataPart(entry.getKey(), entry.getValue());
            DebugLog.d(entry.getKey() + "  " + entry.getValue());
        }

        if (file != null) {
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
            multipartBody.addFormDataPart(fileParamName, file.getName(),
                    RequestBody.create(MEDIA_TYPE_PNG, file));
        }
        RequestBody requestBody = multipartBody
                .build();
        Request request;
        if (accessToken == null) {
            request = new Request.Builder()
                    .url(methodName)
                    .addHeader(ConstantClass.HEADER_AUTHORIZATION, ConstantClass.AUTHORIZATION_KEY_VALUE)
                    .post(requestBody)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(methodName)
                    .addHeader(ConstantClass.HEADER_AUTHORIZATION, ConstantClass.AUTHORIZATION_KEY_VALUE)
                    .addHeader(ConstantClass.HEADER_TOCKEN, accessToken)
                    .post(requestBody)
                    .build();
        }


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
        callback.onTaskComplete(success, s);
    }
}
