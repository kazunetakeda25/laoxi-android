package com.rider.async;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.rider.navigator.OnTaskComplete;
import com.rider.utils.DebugLog;
import com.rider.utils.LaoxiConstant;

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
public class GenericPostMultipartAsync extends AsyncTask<Void, Void, Response> {

    private final String methodName;
    private final String accessToken;
    private final String fileParamName;
    String path;
    HashMap<String, String> param;

    OnTaskComplete<Response> callback;
    boolean success = true;
    Context context;

    public GenericPostMultipartAsync(Context context, HashMap<String, String> param, @Nullable String fileParamName, @Nullable String path,
                                     @Nullable String accessToken, String methodName, OnTaskComplete<Response> callback) {

        this.context = context;
        this.methodName = methodName;
        this.fileParamName = fileParamName;
        this.param = param;
        this.path = path;
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
        MultipartBody.Builder multipartBody = new MultipartBody.Builder();
        multipartBody.setType(MultipartBody.FORM);
        for (Map.Entry<String, String> entry : param.entrySet()) {
            multipartBody.addFormDataPart(entry.getKey(), entry.getValue());
            DebugLog.d(entry.getKey() + "  " + entry.getValue());
        }

        if (path != null && !path.equalsIgnoreCase("")) {

            File file=new File(path);
            DebugLog.e("path of image is"+file.getPath());
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
            multipartBody.addFormDataPart("profile_image", file.getName(),
                    RequestBody.create(MEDIA_TYPE_PNG, file));
        }

        RequestBody requestBody = multipartBody.build();
        Request request;
        if (accessToken == null) {
            request = new Request.Builder()
                    .url(methodName)
                    .addHeader(LaoxiConstant.HEADER_AUTHORIZATION, LaoxiConstant.AUTHORIZATION_KEY_VALUE)
                    .post(requestBody)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(methodName)
                    .addHeader(LaoxiConstant.HEADER_AUTHORIZATION, LaoxiConstant.AUTHORIZATION_KEY_VALUE)
                    .addHeader(LaoxiConstant.HEADER_TOCKEN, accessToken)
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
       // DebugLog.e("Response is a ::" + s.toString());

        try {

            if(s.code()==401)
            {/*
                Intent intentService = new Intent(context, TrackRideService.class);
                if(LAOXI.currentActivity instanceof HomeActivity){
                    LAOXI.currentActivity.stopService(intentService);
                }

                DataToPref.setSharedPreferanceData(context, LaoxiConstant.STORED_USER, LaoxiConstant.DATA, "");
                DataToPref.setSharedPreferanceData(context, LaoxiConstant.STORED_USER, LaoxiConstant.ISLOGIN, "false");
                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
                ((Activity)context).finish();*/

                DebugLog.e("GenericPostMultipartAsync Responce Error : 401" + s.toString());

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
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
