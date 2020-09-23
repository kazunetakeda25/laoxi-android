package com.rider.activity;

import android.content.Context;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.rider.error.ExceptionHandler;
import com.rider.utils.LaoxiConstant;
import com.rider.utils.LocationChangedReceiver;
import com.rider.utils.NetworkChangeReceiver;
import com.rider.utils.VersionManager;

/**
 * Created by hitesh on 30/7/16.
 */
public class BaseActivity extends AppCompatActivity {

    private NetworkChangeReceiver networkChangeReceiver;
    private LocationChangedReceiver locationChangedReceiver;

    public enum WebServiceState{ACTIVE,INACTIVE};

    public WebServiceState webServiceState;

    public VersionManager versionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webServiceState=WebServiceState.ACTIVE;
        ExceptionHandler.register(this, LaoxiConstant.ERROR_LINK);

        versionManager = new VersionManager(this);
    }// end of onCreate

    @Override
    protected void onResume() {
        super.onResume();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, filter);

        IntentFilter intentFilter = new IntentFilter("android.location.PROVIDERS_CHANGED");
        locationChangedReceiver = new LocationChangedReceiver();
        registerReceiver(locationChangedReceiver, intentFilter);

        versionManager.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            if(networkChangeReceiver!=null)
            {
                unregisterReceiver(networkChangeReceiver);
            }
            if(locationChangedReceiver!=null)
            {
                unregisterReceiver(locationChangedReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        versionManager.stop();
    }

    public void checkLocation(Context context) {
        boolean anyLocationProv = false;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        anyLocationProv |= locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        anyLocationProv |= locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!anyLocationProv) {
            LocationChangedReceiver.createDialog(context);
        }
    }
}
