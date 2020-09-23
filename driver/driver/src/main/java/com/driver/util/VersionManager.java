package com.driver.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.driver.BuildConfig;
import com.driver.activity.HomeActivity;
import com.driver.activity.LoginActivity;
import com.driver.activity.SplashScreenActivity;
import com.driver.pojo.loginws.LoginResponse;
import com.driver.retrofit.ApiService;
import com.driver.retrofit.RetroClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by han on 7/20/17.
 */

public class VersionManager {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("android-versions");
    private Context context;
    static public boolean isShowingAlert = false;

    public VersionManager(Context context) {
        this.context = context;
        if (BuildConfig.BUILD_TYPE == "debug") {

        } else if (BuildConfig.BUILD_TYPE == "release") {
            databaseReference = databaseReference.child("Oscar Driver");
        } else if (BuildConfig.BUILD_TYPE == "internaltest") {
            databaseReference = databaseReference.child("Oscar Driver Test");
        }
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            PackageInfo info;
            try {
                info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
                int currentBuild = info.versionCode;

                String appStoreVersionString = dataSnapshot.getValue().toString();
                String appStoreBuildString = appStoreVersionString.substring(appStoreVersionString.indexOf("(") + 1, appStoreVersionString.indexOf(")"));
                int appStoreBuild = Integer.parseInt(appStoreBuildString);

                if (currentBuild < appStoreBuild && !isShowingAlert && context.getClass() != SplashScreenActivity.class) {
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Version Update");
                    alertDialog.setMessage("Hey, this app version is old, expired and clearly worn out from over use!? Please download V" + appStoreVersionString + " now.");
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            isShowingAlert = false;
                            gotoPlayStore();
                        }
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                    isShowingAlert = true;
                }
            } catch (PackageManager.NameNotFoundException e1) {
                Log.e("name not found", e1.toString());
            } catch (Exception e) {
                Log.e("exception", e.toString());
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public void start() {
        if (BuildConfig.BUILD_TYPE != "debug") {
            databaseReference.addValueEventListener(valueEventListener);
        }
    }

    public void stop() {
        if (BuildConfig.BUILD_TYPE != "debug") {
            databaseReference.removeEventListener(valueEventListener);
        }
    }

    private void gotoPlayStore() {
        HashMap<String, String> param = new HashMap<>();
        LoginResponse loginResponse = AppHelper.getInstance().getLoginUser();

        if (loginResponse != null) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);


            String strToken = AppHelper.getInstance().getToken();
            ApiService api = RetroClient.getApiService();
            Call<Void> call = api.logOut(strToken, AppHelper.getInstance().getDriverId());

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                    if (response.isSuccessful()) {
                        if (response.code() == ConstantClass.RESPONSE_CODE_200) {
                            AppHelper.getInstance().clearSharedPreferance(ConstantClass.PREFS_NAME);
                            AppHelper.getInstance().SaveInSharePrefWithBoolean(ConstantClass.IS_LOGIN, false);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }

        final String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }

        if (loginResponse != null) {
            if (context.getClass() == HomeActivity.class) {
                HomeActivity homeActivity = (HomeActivity) context;
                if (homeActivity.intentUpdateLatLong != null) {
                    homeActivity.stopService(homeActivity.intentUpdateLatLong);
                }
            }
            ((Activity) context).finish();
        }
    }
}
