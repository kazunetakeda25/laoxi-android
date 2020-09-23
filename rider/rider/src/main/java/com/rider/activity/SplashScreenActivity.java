package com.rider.activity;

import android.Manifest;
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
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.rider.LocationHelper.LocationHelperService;
import com.rider.R;
import com.rider.dialog.AlertUtils;
import com.rider.dialog.GeneralStaticAlertDialog;
import com.rider.pojo.User;
import com.rider.retrofit.APIRequestManager;
import com.rider.retrofit.ApiService;
import com.rider.retrofit.RetroClient;
import com.rider.utils.AlertDialogCustome;
import com.rider.utils.DataToPref;
import com.rider.utils.DebugLog;
import com.rider.utils.DialogDismissOneButton;
import com.rider.utils.FLogManager;
import com.rider.utils.LaoxiConstant;

import retrofit2.Call;


/**
 * Created by hitesh on 18/7/16.
 */
public class SplashScreenActivity extends BaseActivity implements APIRequestManager.ResponseListener<User>  {

    private static final int EXTERNAL_READ = 1;
    private boolean anyLocationProv;
    private AlertDialogCustome alertDialogCustome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash);

    }


    @Override
    protected void onResume() {
        super.onResume();

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
        }else{
                alertDialogCustome = new AlertDialogCustome();

                alertDialogCustome.createDialog(this, getResources().getString(R.string.app_name), getString(R.string.alertdialog_errror_message_location_enable), new AlertDialogCustome.DialogDismiss() {
                    @Override
                    public void onDismiss() {
                        finish();
                    }

                });
            }
        }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

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



    //Remove a camera and gallery permissions modify by prakash and ask permissions when try to change profile pic.


    public boolean checkPermission() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)){
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) || (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))) {

                GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(this, getString(R.string.allow_permissions_from_settings), new DialogDismissOneButton() {
                    @Override
                    public void onDismiss() {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", SplashScreenActivity.this.getPackageName(), null);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setData(uri);
                        SplashScreenActivity.this.startActivity(intent);
                    }
                });
                // ActivityCompat.requestPermissions(SplashScreenActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, EXTERNAL_READ);


            } else {
                ActivityCompat.requestPermissions(SplashScreenActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, EXTERNAL_READ);
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Based on user session navigation is made.
     */

    public void openLogin() {
        startLocationUpdate();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (DataToPref.getSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.ISLOGIN).equals("true")) {

//                    autoLogin();
                    FLogManager.getInstance().get_loglevel(SplashScreenActivity.this);

                    Intent mainIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mainIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    Intent mainIntent1 = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    mainIntent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mainIntent1);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        }, 3000);
    }


    public void startLocationUpdate() {
        Intent intent = new Intent(SplashScreenActivity.this, LocationHelperService.class);
        startService(intent);
    }

    private void autoLogin() {

        String device_id = FirebaseInstanceId.getInstance().getToken();

        ApiService api = RetroClient.getApiService();
        Call<User> call = api.logIn("", DataToPref.getSharedPreferanceData(this, LaoxiConstant.STORED_USER, LaoxiConstant.LOGIN_USER_NAME), DataToPref.getSharedPreferanceData(this, LaoxiConstant.STORED_USER, LaoxiConstant.LOGIN_USER_PASSWORD), device_id != null ? device_id : "test", "A", "");

        APIRequestManager<User> apiRequestManager = new APIRequestManager<>(SplashScreenActivity.this, call);
        apiRequestManager.execute();

    }

    @Override
    public void onResponse(retrofit2.Response<User> response) {
        AlertUtils.dismissDialog();
        if (response.isSuccessful()) {
            DebugLog.i("Login Successful : "+response.body());

            User LoginResponse = response.body();
            DebugLog.i("Login User Data : "+LoginResponse);

            DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.ISLOGIN, "true");

            DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.HEADER_TOCKEN, LoginResponse.getToken());
            DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.USER_ID, LoginResponse.getId());


            FLogManager.getInstance().get_loglevel(SplashScreenActivity.this);

            Intent mainIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainIntent);
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

    @Override
    public void onFailure(Throwable throwable) {
        AlertUtils.dismissDialog();
    }

}// end of main class
