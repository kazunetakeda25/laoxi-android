package com.driver.dao.authenticationDAO;

import com.driver.dao.IAsyncCompleteCallback;

import java.util.HashMap;

/**
 * Created by Chirag on 12/04/2016.
 */
public interface IAuthenticationDAO {

    void callLogin(HashMap<String, String> param, String methodName, final IAsyncCompleteCallback callback);


}
