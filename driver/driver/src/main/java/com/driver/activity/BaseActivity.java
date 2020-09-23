package com.driver.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.driver.util.LocationChangedReceiver;
import com.driver.util.LocationHelperService;
import com.driver.util.NetworkChangeReceiver;
import com.driver.util.VersionManager;
import com.google.firebase.FirebaseApp;

public class BaseActivity extends AppCompatActivity {

    public enum WebServiceState {ACTIVE, INACTIVE}
    public WebServiceState webServiceState;
    private NetworkChangeReceiver networkChangeReceiver;
    private LocationChangedReceiver locationChangedReceiver;
    private VersionManager versionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webServiceState = WebServiceState.ACTIVE;
        if (FirebaseApp.getInstance() == null) {
            FirebaseApp.initializeApp(this);
        }
        versionManager = new VersionManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, filter);

        IntentFilter intentFilter = new IntentFilter("android.location.PROVIDERS_CHANGED");
        locationChangedReceiver = new LocationChangedReceiver();
        registerReceiver(locationChangedReceiver, intentFilter);

        versionManager.start();
    }

    public void showSnackbar(String msg) {
        final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
        snackBar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();
            }
        });
        snackBar.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
        unregisterReceiver(locationChangedReceiver);

        versionManager.stop();
    }
    public void stopLocationUpdate() {
        Intent intent = new Intent(BaseActivity.this, LocationHelperService.class);
        stopService(intent);
    }

    public void startLocationUpdate() {
        Intent intent = new Intent(BaseActivity.this, LocationHelperService.class);
        startService(intent);
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

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
    }
}
