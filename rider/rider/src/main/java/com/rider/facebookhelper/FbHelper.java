package com.rider.facebookhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rider.application.Laoxi;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Chirag on 09/06/2016.
 */
public class FbHelper {
    private final CallbackManager callbackmanager;
    Activity activity;


    public FbHelper(Activity activity) {
        this.activity = activity;
        FacebookSdk.sdkInitialize(Laoxi.getInstance());
        AppEventsLogger.activateApp(activity);
        callbackmanager = CallbackManager.Factory.create();
    }



    public void onLogin(final IFacebookTaskComplet<FbResponse> task) {
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                                Log.e("Test", "Json ::" + jsonObject.toString());
                                FbResponse fbResponse = fbResponseParsing(jsonObject.toString());

                                task.onTaskComplete(true, fbResponse);
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,picture,gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

                if (error instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                        task.onTaskComplete(false, null);
                    }
                }
            }
        });
    }



    public void setCallbackManager(int requestCode, int resultCode, Intent data) {
        callbackmanager.onActivityResult(requestCode, resultCode, data);
    }

    public FbResponse fbResponseParsing(String data) {
        try {
            Gson mGson = new Gson();
            FbResponse json = mGson.fromJson(data, FbResponse.class);
            return json;
        } catch (JsonSyntaxException e) {
            // TODO Auto-generated catch block
            Log.e("Json", "JsonSyntaxException :" + e);
            return null;

        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

}
