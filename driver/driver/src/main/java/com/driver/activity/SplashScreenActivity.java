
package com.driver.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.driver.R;
import com.driver.application.LAOXI;
import com.driver.dialog.GeneralStaticAlertDialog;
import com.driver.pojo.loginws.LoginResponse;
import com.driver.retrofit.APIRequestManager;
import com.driver.retrofit.ApiService;
import com.driver.retrofit.RetroClient;
import com.driver.util.AlertDialogCustome;
import com.driver.util.AnalyticsReporter;
import com.driver.util.AppHelper;
import com.driver.util.ConstantClass;
import com.driver.util.DialogDismissOneButton;
import com.driver.util.FLogManager;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;


/**
 * Created by hitesh on 18/7/16.
 */
public class SplashScreenActivity extends BaseActivity implements APIRequestManager.ResponseListener<LoginResponse> {

    private static final int EXTERNAL_READ = 1;
    private boolean anyLocationProv;
    private AlertDialogCustome alertDialogCustome;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        // ExceptionHandler.register(this, Constants.ERRORLINK);
    }

    @Override
    protected void onResume() {
        super.onResume();

        LAOXI.setCurrentActivity(this);

        /**
         * Check a location is enable or not.
         */

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        anyLocationProv |= locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        anyLocationProv |= locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (anyLocationProv) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermission()) {
                    openLogin();
                }
            } else {
                openLogin();
            }
        } else {
            alertDialogCustome = new AlertDialogCustome();

            alertDialogCustome.createDialog(this, getResources().getString(R.string.app_name), getString(R.string.alertdialog_errror_message_location_enable), new AlertDialogCustome.DialogDismiss() {
                @Override
                public void onDismiss() {
                    finish();
                }

            });
        }
    }

    /**
     * Get a call back when driver allow or deny a permissions.
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case EXTERNAL_READ: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openLogin();
                } else {
                    finish();
                }
                return;
            }
            default:
                break;
        }
    }

    /**
     * Check permissions for locations, camera and gallery.
     */

    //Remove a camera and gallery permissions modify by prakash and ask permissions when try to change profile pic.
    public boolean checkPermission() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) || (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))) {

                GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(this, getString(R.string.allow_permissions_from_settings), new DialogDismissOneButton() {
                    @Override
                    public void onDismiss() {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", SplashScreenActivity.this.getPackageName(), null);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setData(uri);
                        SplashScreenActivity.this.startActivity(intent);
                    }
                });

            } else {
                ActivityCompat.requestPermissions(SplashScreenActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, EXTERNAL_READ);
            }
            return false;
        } else {
            return true;
        }
    }


    /**
     * Navigate into the app based on session.
     */

    public void openLogin() {
        startLocationUpdate();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AppHelper.getInstance().getSharedPrefWithBoolean(ConstantClass.IS_LOGIN) &&
                        AppHelper.getInstance().getLoginUser() != null) {

                    LoginResponse loginResponse = AppHelper.getInstance().getLoginUser();
                    //rhc commented
                    /*Crashlytics.setUserIdentifier(loginResponse.getDriverId());
                    Crashlytics.setUserEmail(loginResponse.getEmail());
                    Crashlytics.setUserName(loginResponse.getFname() + " " + loginResponse.getLname());
                    */
                    LAOXI.getInstance().setLoginUser(loginResponse);

                    FLogManager.getInstance().get_loglevel();

                    AnalyticsReporter.getInstance().setProfile(
                            loginResponse.getDriverId(),
                            loginResponse.getEmail(),
                            loginResponse.getFname(),
                            loginResponse.getLname(),
                            loginResponse.getDeviceId(),
                            loginResponse.getDeviceType(),
                            loginResponse.getLastLogin());

                    Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

            }
        }, 3000);
    }

    private void autoLogin() {

        String device_id = FirebaseInstanceId.getInstance().getToken();

        ApiService api = RetroClient.getApiService();
        Call<LoginResponse> call = api.logIn("", AppHelper.getInstance().getSharedPref(ConstantClass.LOGIN_USER_NAME, true), AppHelper.getInstance().getSharedPref(ConstantClass.LOGIN_USER_PASSWORD, true), device_id != null ? device_id : "test", "A", "");

        APIRequestManager<LoginResponse> apiRequestManager = new APIRequestManager<>(SplashScreenActivity.this, call);
        apiRequestManager.execute();

    }

    @Override
    public void onResponse(retrofit2.Response<LoginResponse> response) {
        if (response.isSuccessful()) {
            LoginResponse loginResponse = response.body();
            LAOXI.getInstance().setLoginUser(loginResponse);

            AppHelper.getInstance().SaveInSharedPref(ConstantClass.HEADER_TOCKEN, loginResponse.getToken());
            AppHelper.getInstance().SaveInSharedPref(ConstantClass.DRIVER_ID, loginResponse.getDriverId());


            /*Crashlytics.setUserIdentifier(loginResponse.getDriverId());
            Crashlytics.setUserEmail(loginResponse.getEmail());
            Crashlytics.setUserName(loginResponse.getFname() + " " + loginResponse.getLname());
*/
            FLogManager.getInstance().get_loglevel();

            AnalyticsReporter.getInstance().setProfile(
                    loginResponse.getDriverId(),
                    loginResponse.getEmail(),
                    loginResponse.getFname(),
                    loginResponse.getLname(),
                    loginResponse.getDeviceId(),
                    loginResponse.getDeviceType(),
                    loginResponse.getLastLogin());

            Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            showSnackbar(response.message());
            Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        showSnackbar(getString(R.string.bad_server));
        Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}// end of main class
