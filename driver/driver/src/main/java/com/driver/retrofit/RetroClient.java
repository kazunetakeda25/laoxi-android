package com.driver.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.driver.util.ConstantClass;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mahuateng on 2/7/18.
 */

public class RetroClient {
    /**
     * Get Retrofit Instance
     */
    private static Retrofit getRetrofitInstance() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                            .header(ConstantClass.PUT_CONTENT_HEADER, ConstantClass.PUT_CONTENT_HEADER_VALUE)
                            .header(ConstantClass.HEADER_AUTHORIZATION, ConstantClass.AUTHORIZATION_KEY_VALUE)
                            .build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        return new Retrofit.Builder()
                .baseUrl(ConstantClass.BASE_URL)
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
