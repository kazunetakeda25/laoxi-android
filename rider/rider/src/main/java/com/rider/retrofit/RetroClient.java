package com.rider.retrofit;

import com.rider.utils.LaoxiConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Good Morning on 2/25/2018.
 */

public class RetroClient {
    /**
     * Get Retrofit Instance
     */
    private static Retrofit getRetrofitInstance() {

//        Gson gson = new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
//                .create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header(LaoxiConstant.PUT_CONTENT_HEADER, LaoxiConstant.PUT_CONTENT_HEADER_VALUE)
                        .header(LaoxiConstant.HEADER_AUTHORIZATION, LaoxiConstant.AUTHORIZATION_KEY_VALUE)
                        .build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();
        return new Retrofit.Builder()
                .baseUrl(LaoxiConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    /**
     * Get API Service
     *
     * @return API Service
     */
    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }

}

