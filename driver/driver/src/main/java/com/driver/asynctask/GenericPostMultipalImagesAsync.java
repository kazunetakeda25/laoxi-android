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
public class GenericPostMultipalImagesAsync extends AsyncTask<Void, Void, Response> {

    private final String methodName;
    private final String accessToken;
    private final String fileParamName;
    String path;
    HashMap<String, String> param;

    IAsyncCompleteCallback<Response> callback;
    boolean success = true;

    public GenericPostMultipalImagesAsync(HashMap<String, String> param, @Nullable String fileParamName, @Nullable String path,
                                          @Nullable String accessToken, String methodName, IAsyncCompleteCallback<Response> callback) {


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
            if(entry.getValue()!=null)
            {
                multipartBody.addFormDataPart(entry.getKey(), entry.getValue());
                DebugLog.d(entry.getKey() + "  " + entry.getValue());

            }
        }

        if (path != null) {

            String aa[] =  path.split(",,");

            String bb[] =  fileParamName.split(",,");

            for (int i = 0; i < aa.length; i++ ){

                File image = new File(aa[i]);

                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
                multipartBody.addFormDataPart(bb[i], image.getName(),
                        RequestBody.create(MEDIA_TYPE_PNG, image));

                DebugLog.d("image "+bb[i] + " .... " + image.getName());


          /*  final MediaType MEDIA_TYPE_PNG = MediaType.parse("image*//*");
            multipartBody.addFormDataPart(fileParamName, file.getName(),
                    RequestBody.create(MEDIA_TYPE_PNG, file));*/

            }
        }
        RequestBody requestBody = multipartBody.build();
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

        try {
            callback.onTaskComplete(success, s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
