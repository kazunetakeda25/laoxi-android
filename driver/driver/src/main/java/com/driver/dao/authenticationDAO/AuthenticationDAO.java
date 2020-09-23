package com.driver.dao.authenticationDAO;

import android.support.annotation.Nullable;

import com.driver.asynctask.GenericAsync;
import com.driver.asynctask.GenericPutAsync;
import com.driver.dao.IAsyncCompleteCallback;

import java.util.HashMap;

import okhttp3.Response;

/**
 * Created by Chirag on 12/04/2016.
 */
public class AuthenticationDAO implements IAuthenticationDAO {
    private static AuthenticationDAO ourInstance = new AuthenticationDAO();

    public static AuthenticationDAO getInstance() {
        return ourInstance;
    }

    private AuthenticationDAO() {


    }

    @Override
    public void callLogin(HashMap<String, String> param, String methodName, final IAsyncCompleteCallback callback) {

     /*   new GenericAsync(param, methodName, null, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();*/

        new GenericPutAsync(param, methodName, "", new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                callback.onTaskComplete(success, data);
            }
        }).execute();

    }


}
