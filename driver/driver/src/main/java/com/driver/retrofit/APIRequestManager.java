package com.driver.retrofit;

import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mahuateng on 2/7/18.
 */

public class APIRequestManager<T> {
    private final WeakReference<ResponseListener<T>> weakListener;
    private final Call<T> call;
    private final Callback<T> callback;

    public APIRequestManager(final ResponseListener<T> listener, Call<T> call) {
        weakListener = new WeakReference<>(listener);
        this.call = call;

        callback = new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (weakListener.get() != null) {
                    if (weakListener.get() instanceof Fragment && ((Fragment) weakListener.get()).isDetached()) {
                        return;
                    }

                    weakListener.get().onResponse(response);
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                if (weakListener.get() != null) {
                    if (weakListener.get() instanceof Fragment && ((Fragment) weakListener.get()).isDetached()) {
                        return;
                    }

                    weakListener.get().onFailure(t);
                }
            }
        };

    }

    public void execute() {
        call.enqueue(callback);
    }

    public interface ResponseListener<T> {
        void onResponse(Response<T> response);

        void onFailure(Throwable throwable);
    }
}
