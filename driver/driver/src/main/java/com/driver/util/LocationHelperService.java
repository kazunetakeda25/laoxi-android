package com.driver.util;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by hlink43 on 26/5/16.
 */
public class LocationHelperService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static double dblLatitude;
    public static double dblLongitude;
    private String TAG = "";
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;



    protected LocationManager locationManager;
    Location location;

    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Log.e("service is start", "");

        if (googleApiClient == null) {
            buildGoogleApiClient();

        }
        return super.onStartCommand(intent, flags, startId);

    }


    protected synchronized void buildGoogleApiClient() {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {

            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            if (!googleApiClient.isConnected() || !googleApiClient.isConnecting()) {
                googleApiClient.connect();
            }
        } else {
            Log.e(TAG, "unable to connect to google play services.");
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(100000); // milliseconds
        locationRequest.setFastestInterval(10000); // the fastest rate in milliseconds at which your app can handle location updates
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        //DebugLog.e("LOCATION FROM HERE:::::::::" + lastLocation);
        isNewLocationAvailable(lastLocation);
    }

    @Override
    public void onConnectionSuspended(int i) {
        DebugLog.e("connection failed*********************");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {


    }


    @Override
    public void onLocationChanged(Location location) {
        isNewLocationAvailable(location);
        // DebugLog.e("onLocation changed================");
    }

    public void isNewLocationAvailable(Location location) {
        if (location != null) {

            dblLatitude = location.getLatitude();
            dblLongitude = location.getLongitude();
           // LocationDataProvider.getInstance().post(location);
        } else {

        }
    }
}
