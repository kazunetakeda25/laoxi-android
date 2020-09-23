package com.driver.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.driver.R;
import com.driver.dao.IAsyncCompleteCallback;
import com.driver.dao.homedao.HomeActivityDAO;
import com.driver.pojo.loginws.LoginResponse;
import com.driver.retrofit.APIRequestManager;
import com.driver.retrofit.ApiService;
import com.driver.retrofit.RetroClient;
import com.driver.util.AppHelper;
import com.driver.util.ConstantClass;
import com.driver.util.DebugLog;

import java.io.IOException;
import java.util.HashMap;

import de.greenrobot.event.EventBus;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by chirag on 22/3/16.
 */
public class UpdateDriverLatLongService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final int NOTIFICATION_ID = 1;
    public static Runnable runnable = null;
    public static boolean sendNotificationOnce;
    public static boolean isDialogVisible;
    private LoginResponse loginData;
    public static LatLng currentLatLng;
    public static final String BROADCAST_ACTION = "";

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    String lat, lon;
    Location mLastLocation;
    Handler handler = new Handler();
    private boolean currentlyProcessingLocation = false;

    String latitude="",Longitude="",last_latitude="",last_longitude="";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        DebugLog.e("Service is create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!currentlyProcessingLocation) {
            currentlyProcessingLocation = true;
            buildGoogleApiClient();
        }

        return START_STICKY;
    }

    synchronized void buildGoogleApiClient() {

        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            if (!mGoogleApiClient.isConnected() || !mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        } else {
            DebugLog.e("unable to connect to google play services.");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            mGoogleApiClient.disconnect();
            handler.removeCallbacks(runnable);
            stopLocationUpdates();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void wsCallUpdateLatLong(final HashMap<String, String> param, final String token) {

        runnable = new Runnable() {
            @Override
            public void run() {

                //DebugLog.e("Driver Id  in ws call"+param.get(ConstantClass.DRIVER_ID));
                HomeActivityDAO.getInstance().callUpdateLatLong(param, ConstantClass.METHOD_UPDATE_LAT_LONG, token, new IAsyncCompleteCallback<Response>() {
                    @Override
                    public void onTaskComplete(boolean success, @Nullable Response response) {
                        if (success) {

                            if (response.code() == 200) {

                                String data = null;
                                try {
                                    data = response.body().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                DebugLog.e("updatelocation data is"+data);

                             /*   try {
                                    String data = response.body().string();
                                    TrackRidePojo trackRidePojo = ParsingHelper.getInstance().trackRideResponse(data);

                                    if (trackRidePojo.getTrackTrip() == null) {

                                    } else {
                                        DebugLog.e("data is Status======>" + data);
                                        DebugLog.e("data is Status======>" + trackRidePojo.getTrackTrip().getStatus());
                                        AppHelper.getInstance().SaveInSharedPref(ConstantClass.TRACK_RIDE_ORDER_DATA, data);

                                        if (LAOXI.currentActivity instanceof HomeActivity) {
                                            LocationDataProvider.getInstance().post(trackRidePojo);

                                            if (trackRidePojo.getTrackTrip().getStatus() != null && !trackRidePojo.getTrackTrip().getStatus().equalsIgnoreCase("null")) {
                                                if (HomeActivity.activeornot) {
                                                    HomeActivity mActivity = (HomeActivity) LAOXI.currentActivity;
                                                    HomeActivity.openNotification = false;
                                                    mActivity.openPopUp(trackRidePojo);
                                                } else {
                                                    HomeActivity.openNotification = true;
                                                    if (trackRidePojo.getTrackTrip().getStatus().equalsIgnoreCase("waiting") &&  !isDialogVisible) {
                                                        sendNotificationForNewOrder("Track Your Ride");
                                                    }
                                                }
                                            }
                                        } else {
                                            if (trackRidePojo.getTrackTrip().getStatus() != null && !trackRidePojo.getTrackTrip().getStatus().equalsIgnoreCase("null")) {
                                                sendNotificationForNewOrder("Track Your Ride");
                                            }
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }*/
                            } else if (response.code() == 400) {
                                stopSelf();
                            } else if (response.code() == 401) {
                                try {

                                    stopSelf();
                                    DebugLog.e("data is Status======>" + response.code());
                                    DebugLog.e("data is Status======>" + response.body().string());

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            DebugLog.e("Bad Server Response");
                        }
                    }
                });
            }
        };
        waitForSometime();
    }

    public void waitForSometime() {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 5000);
    }

    private void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public HashMap<String, String> setDriverLatLong(String lat, String lon) {
        HashMap<String, String> param = new HashMap<>();
       if(AppHelper.getInstance().getDriverId()!=null)
       {
           param.put(ConstantClass.DRIVER_ID, AppHelper.getInstance().getDriverId());
       }
        last_latitude=""+lat;
        last_longitude=""+lon;
        param.put(ConstantClass.DRIVER_LATITUDE, lat);
        param.put(ConstantClass.DRIVER_LONGITUDE, lon);
        return param;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        try {
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(5000); // Update location every 10 second
            mLocationRequest.setFastestInterval(10000);

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                lat = String.valueOf(mLastLocation.getLatitude());
                lon = String.valueOf(mLastLocation.getLongitude());
            } else {

            }

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());
        currentLatLng=new LatLng(location.getLatitude(),location.getLongitude());
        EventBus.getDefault().post(location);

        //Store a user latlong
        AppHelper.getInstance().SaveInSharedPref(ConstantClass.USER_DEFAULT_LATITUDE, lat);
        AppHelper.getInstance().SaveInSharedPref(ConstantClass.USER_DEFAULT_LONGITUDE, lon);

        latitude=lat;
        Longitude=lon;

        if(!latitude.equalsIgnoreCase(last_latitude) && !Longitude.equalsIgnoreCase(last_longitude))
        {
            if(AppHelper.getInstance().getToken()!=null)
            {
                String strToken = AppHelper.getInstance().getToken();
                String strDriverId = AppHelper.getInstance().getDriverId();
                DebugLog.i("Location Updating Request");
                ApiService api = RetroClient.getApiService();
                Call<ResponseBody> call = api.callUpdateLatLong( strToken, strDriverId, ""+lat, ""+lon);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        if(response.isSuccessful()) {
                            DebugLog.i("Location Successfuly Updated Status :" + response.message());
                        } else {
                            DebugLog.e("Location Failed Response :" + response.code());
                            stopSelf();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        DebugLog.e("Update location Failed :" + t.getMessage());
                        call.clone();
                    }
                });
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }
}
