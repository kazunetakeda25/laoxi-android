package com.driver.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.driver.R;
import com.driver.activity.BaseActivity;
import com.driver.activity.HomeActivity;
import com.driver.activity.TwilioCallActivity;
import com.driver.customclasses.CustomTextView;
import com.driver.dao.ParsingHelper;
import com.driver.dialog.CancelRideDialog;
import com.driver.dialog.GeneralStaticAlertDialog;
import com.driver.listener.ISpinnerItemClick;
import com.driver.navigator.NavigatorInterface;
import com.driver.pojo.GenericResponse;
import com.driver.pojo.TrackRidePojo.TrackRide;
import com.driver.pojo.dropoffWs.DropOffResponse;
import com.driver.pojo.loginws.LoginResponse;
import com.driver.retrofit.ApiService;
import com.driver.retrofit.RetroClient;
import com.driver.route.Route;
import com.driver.route.Routing;
import com.driver.route.RoutingListener;
import com.driver.util.AlertUtils;
import com.driver.util.AnalyticsReporter;
import com.driver.util.AppHelper;
import com.driver.util.ConstantClass;
import com.driver.util.DebugLog;
import com.driver.util.DialogDismissTwoButton;
import com.driver.util.DialogDismissTwoButtonwithText;
import com.driver.util.LocationHelperService;
import com.driver.util.MyGeocoder;
import com.driver.util.PorterShapeImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by on 20/7/16.
 */
public class CompleteRideFragment extends BaseFragment implements View.OnClickListener, RoutingListener, OnMapReadyCallback {

    AlertDialog.Builder builder;
    @Bind(R.id.header_menu)
    ImageView headerMenu;
    @Bind(R.id.header_text)
    CustomTextView headerText;
    @Bind(R.id.txtPickupAddress)
    CustomTextView txtPickupAddress;
    @Bind(R.id.txtDropoffAddress)
    CustomTextView txtDropoffAddress;
    @Bind(R.id.txtDriverStatus)
    CustomTextView txtDriverStatus;
    @Bind(R.id.imgbtnCall)
    ImageButton imgbtnCall;
    @Bind(R.id.ImgbtnCancel)
    ImageButton ImgbtnCancel;
    @Bind(R.id.layoutCallMessage)
    LinearLayout layoutCallMessage;
    @Bind(R.id.imageViewProfile)
    PorterShapeImageView imageViewProfile;
    @Bind(R.id.txtHi)
    CustomTextView txtHi;
    @Bind(R.id.txtName)
    CustomTextView txtName;
    @Bind(R.id.imageViewComma)
    ImageView imageViewComma;
    @Bind(R.id.txtBottomTextview)
    CustomTextView txtBottomTextview;
    @Bind(R.id.imgGoogleNavigation)
    ImageView imgGoogleNavigation;
    @Bind(R.id.linear_main)
    LinearLayout linearMain;

    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;

    private LatLng pickUpLatlng, dropOffLatLng;
    NavigatorInterface navigatorInterface;
    private HomeActivity homeActivity;

    boolean firstCallOnly = true;

    LatLng WazePickup;
    LatLng oldLatLng;
    float Distance = 0.0f;
    private Marker now, myMarkerDropOff;
    TrackRide trackRidePojo;
    private LatLng lat_lng_current;
    private View view;
    Location newLocation = new Location("");
    Location oldLocation = new Location("");
    float bearingMoveCamera = 0.0f;
    private Polyline polyline;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigatorInterface = (NavigatorInterface) context;
        if (context instanceof HomeActivity) {
            homeActivity = (HomeActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_rider_location, container, false);
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_rider_location, container, false);
        } catch (InflateException e) {
        }
        ButterKnife.bind(this, view);

        DebugLog.i("Reached Complete Screen");

        headerText.setText("great! you made it.");
        txtDropoffAddress.setVisibility(View.VISIBLE);
        txtDriverStatus.setText("Complete ride");

        txtDriverStatus.setBackground(getResources().getDrawable(R.drawable.rectangle_complete_button));
        txtBottomTextview.setText("Let's go!");
        imgGoogleNavigation.setVisibility(View.VISIBLE);
        imgbtnCall.setOnClickListener(this);
        ImgbtnCancel.setOnClickListener(this);
        headerMenu.setOnClickListener(this);

        //btnCancel.setOnClickListener(this);
        txtDriverStatus.setOnClickListener(this);
        headerMenu.setEnabled(false);
        headerMenu.setVisibility(View.GONE);


        //Get a current locations

        if (AppHelper.getInstance().getSharedPref(ConstantClass.USER_DEFAULT_LATITUDE) != null && AppHelper.getInstance().getSharedPref(ConstantClass.USER_DEFAULT_LONGITUDE) != null) {
           pickUpLatlng=new LatLng(Double.parseDouble(AppHelper.getInstance().getSharedPref(ConstantClass.USER_DEFAULT_LATITUDE)), Double.parseDouble(AppHelper.getInstance().getSharedPref(ConstantClass.USER_DEFAULT_LONGITUDE)));
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /**
         * Load  a data from shared preference and fill it .
         */

        lat_lng_current = new LatLng(LocationHelperService.dblLatitude, LocationHelperService.dblLongitude);

        googleMapInitialization();
        String data = AppHelper.getInstance().getSharedPref(ConstantClass.TRACK_RIDE_ORDER_DATA);
        trackRidePojo = ParsingHelper.getInstance().trackRideData(data);

        if (trackRidePojo != null) {
            //DebugLog.e("First Name :" + trackRidePojo.getTrackTrip().getUserData().getName());
            //DebugLog.e("Path :: " + ConstantClass.USER_IMAGE_URL + trackRidePojo.getTrackTrip().getUserData().getProfileImage());
            txtName.setText("i'm " + trackRidePojo.getUserData().getName());
            Picasso.with(getContext()).
                    load(ConstantClass.USER_IMAGE_URL + trackRidePojo.getUserData().getProfileImage())
                    .resize(300, 300)
                    .placeholder(R.drawable.avatar_icon)
                    .into(imageViewProfile);
            String pickup = String.format(getContext().getString(R.string.pickup), trackRidePojo.getPickupAddress());
            String dropoff = String.format(getContext().getString(R.string.dropOff), trackRidePojo.getDeliveryAddress());
            txtDropoffAddress.setText(dropoff);
            txtPickupAddress.setText(pickup);

        }

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        imgGoogleNavigation.setOnClickListener(new View.OnClickListener() {
            public long doneButtonClickTime;

            @Override
            public void onClick(View v) {

                GeneralStaticAlertDialog.getInstance().createDialogWithWazeGoogle(getActivity(), new DialogDismissTwoButton() {
                    @Override
                    public void onOkClick() {
                        if (dropOffLatLng != null) {

                            try {
                                String url = "waze://?ll=" + dropOffLatLng.latitude + "," + dropOffLatLng.longitude + "&navigate=yes";
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(intent);
                            } catch (ActivityNotFoundException ex) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
                                startActivity(intent);
                            }
                        }

                    }

                    @Override
                    public void onCancelClick() {

                        if (dropOffLatLng != null) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + dropOffLatLng.latitude + "," + dropOffLatLng.longitude));
                                startActivity(intent);
                            } catch (ActivityNotFoundException ex) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps"));
                                startActivity(intent);
                            }

                        }


                    }
                });

                //store time of button click


            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (mapFragment != null) {
            getFragmentManager().beginTransaction().remove(mapFragment).commit();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.header_menu:
                HomeActivity.toggleDrawer();
                break;


            case R.id.ImgbtnCancel:

                if (trackRidePojo != null) {
                    CancelRideDialog cancelRideDialog = new CancelRideDialog();
                    cancelRideDialog.createDialog(homeActivity, trackRidePojo, null);
                }

                break;
            case R.id.imgbtnCall:
                if (trackRidePojo != null) {
                    openCommunicationDialogMode();
                }

                //msgDriver("1234567890");
                break;

            case R.id.txtDriverStatus:

                Bundle properties = new Bundle();
                properties.putString("order_id", trackRidePojo.getId());
                properties.putString("driver_id", trackRidePojo.getDriverId());
                properties.putString("user_id", trackRidePojo.getUserId());
                properties.putString("trip_type", trackRidePojo.getTripType());
                AnalyticsReporter.getInstance().track(AnalyticsReporter.Event.DRIVER_COMPLETED_RIDE, properties);

                //if (trackRidePojo != null) {
//                    AlertUtils.showCustomProgressDialog(getContext());

                    String address = getAddressFromLatlong(getContext(), pickUpLatlng);
                    if(address!=null)
                    {
                        callDropOffTripWS(trackRidePojo,address);
                    }
                    else
                    {
                        if(pickUpLatlng!=null)
                        {
                            address= MyGeocoder.getLocationInfo(pickUpLatlng.latitude,pickUpLatlng.longitude);
                        }
                        if(address!=null)
                        {
                            callDropOffTripWS(trackRidePojo,address);
                        }
                        else
                        {
                            callDropOffTripWS(trackRidePojo,trackRidePojo.getDeliveryAddress());
                        }
                    }

               /* } else {
                    DebugLog.e("Current Track ride record null");
                }*/


                if (homeActivity.webServiceState == BaseActivity.WebServiceState.ACTIVE) {

                } else {
                    headerText.setText("Ride");
                    ReceiptFragment fragment = new ReceiptFragment();
                    navigatorInterface.openReceiptFragment(fragment);
                }


                break;



            /*case btnCall:

                break;
            case btnSms:

                break;*/


            //callDriver("1234567890");
            //msgDriver("1234567890");
        }
    }// end if onClick


    /**
     * Open a dialog with two option sms or call.
     */

    private void openCommunicationDialogMode() {
        final String Call = "Call";
        String Message = "Message";
        List<String> data = new ArrayList<>();
        data.add(Call);
        data.add(Message);
        AppHelper.getInstance().openSpinnerpopup(getContext(), "Please select mode", data, new ISpinnerItemClick() {
            @Override
            public void onItemClick(String text) {
                if (text.equalsIgnoreCase(Call)) {
                    String phone;
                    char first = trackRidePojo.getUserData().getPhone().charAt(0);

                    if (first == '0') {
                        phone = trackRidePojo.getUserData().getPhone();
                        phone = phone.substring(1, phone.length());
                    } else {
                        phone = trackRidePojo.getUserData().getPhone();
                    }

                    DebugLog.i("phone is a" + phone);


                    Intent intent = new Intent(getActivity(), TwilioCallActivity.class);
                    intent.putExtra("Client", trackRidePojo.getUserData().getCountryCode() + phone);
                    startActivity(intent);

                } else {

                    GeneralStaticAlertDialog.getInstance().createDialogSendSms(getActivity(), "", new DialogDismissTwoButtonwithText() {
                        @Override
                        public void onOkClick(String s) {
                            sendSmsFromTwilio(trackRidePojo.getUserData().getCountryCode() + trackRidePojo.getUserData().getPhone(), s, AppHelper.getInstance().getLoginUser().getCountryCode() + AppHelper.getInstance().getLoginUser().getPhone(), trackRidePojo.getId());
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    });
                }
            }
        });
    }

    /**
     * Call a drop off api.
     */

    private void callDropOffTripWS(TrackRide rideData,String address) {


        if(rideData!=null)
        {
            String strToken = AppHelper.getInstance().getToken();
            String strOrderId = trackRidePojo.getId();
            String strDistance = "";
            String strLat = "";
            String strLon = "";
            AlertUtils.showCustomProgressDialog(getContext());
            if (AppHelper.getInstance().getSharedPref(ConstantClass.SHARED_PREFRENCE_DISTANCE) != null){
                strDistance = String.valueOf((Float.valueOf(AppHelper.getInstance().getSharedPref(ConstantClass.SHARED_PREFRENCE_DISTANCE)) / 1000));
            }

            if (pickUpLatlng != null) {
                strLat = "" + pickUpLatlng.latitude;
                strLon = "" + pickUpLatlng.longitude;
            }

            DebugLog.i("Complete Ride Request :" + strOrderId + " Address :" + address + " Distance :" + strDistance);

            ApiService api = RetroClient.getApiService();
            Call<DropOffResponse> call = api.callDropOff_TripWS(strToken, strOrderId, strDistance, address, strLat, strLon);

            call.enqueue(new Callback<DropOffResponse>() {
                @Override
                public void onResponse(Call<DropOffResponse> call, retrofit2.Response<DropOffResponse> response) {
                    AlertUtils.dismissDialog();
                    if (response.isSuccessful()) {
                        DebugLog.i("Complete Ride Success Response Code :" + response.code());
                        DropOffResponse dropOffResponse = response.body();
                        if (response.code() == ConstantClass.RESPONSE_CODE_200) {
                            DebugLog.i("Main ::::::::::::::::::::::::: " + dropOffResponse.getDeliveryAddress());
                            ReceiptFragment receiptFragment = new ReceiptFragment();
                            receiptFragment.setDropOffResponse(dropOffResponse);
                            homeActivity.openReceiptFragment(receiptFragment);

                        }
                    } else {
                        try {
                            DebugLog.i("Complete Failed Response Code :" + response.code());
                            String s = response.errorBody().string();
                            if (response.code() == ConstantClass.RESPONSE_CODE_400 ){
                                GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                                homeActivity.showSnackbar(responseBody.getMessage());
                            } else if (response.code() == ConstantClass.RESPONSE_CODE_401) {
                                homeActivity.Logout(false);
                            } else {

                                GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                                if (response == null) {
                                    homeActivity.showSnackbar("Can't send notification to user because user doesn't have device type.");
                                } else {
                                    homeActivity.showSnackbar(responseBody.getMessage());
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<DropOffResponse> call, Throwable t) {
                    DebugLog.e("CompleteRide Request Failed Msg :" + t.getMessage());
                    AlertUtils.dismissDialog();
                    homeActivity.showSnackbar(getString(R.string.bad_server));
                }
            });
        }
        else
        {
            AlertUtils.dismissDialog();
        }

    }

    private void googleMapInitialization() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapRide);
        supportMapFragment.getMapAsync(this);
    }

    /**
     * get a address from lat lng.
     */

    public String getAddressFromLatlong(Context context, LatLng latLng) {

        Geocoder geocoder;
        String area = null;
        if (latLng != null) {
            try {
                List<Address> addresses = new ArrayList<Address>();
                geocoder = new Geocoder(context, Locale.getDefault());
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (!addresses.isEmpty()) {

                    if (addresses.get(0).getAddressLine(0) != null) {
                        area = addresses.get(0).getAddressLine(0);
                        if (addresses.get(0).getAddressLine(1) != null) {
                            area = area + " " + addresses.get(0).getAddressLine(1);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return area;
    }

    /**
     * Set up map.
     */

    public void setMap(LatLng latLngPickUp, LatLng latLngDestination) {

        if (latLngPickUp != null && latLngDestination != null) {
            try {
                // googleMap = mapFragment.getMap();
                googleMap.setMyLocationEnabled(false);
                MapsInitializer.initialize(getContext());
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.clear();

                DebugLog.i("Draw Path call From Setup Map -------------------------- ");
                drawPathBetweenTwoLocation(latLngPickUp, latLngDestination);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Set a car icon on map based on driver gender and car type.
     */

    private void getMyCarDrawable(LatLng latLng) {
//        LoginResponse loginResponse = AppHelper.getInstance().getLoginUser();
        LoginResponse loginResponse = AppHelper.getInstance().getLoginUser();
        DebugLog.i("Car Type " + loginResponse.getCarType());

        if (now != null) {
            now.remove();
        }
        if (trackRidePojo != null) {
            String strCarType = trackRidePojo.getCarType();

            if (strCarType.equalsIgnoreCase(ConstantClass.TAXI)) {
                now = googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_icon)));
            } else if (strCarType.equalsIgnoreCase(ConstantClass.BIKE)) {
                now = googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_icon)));
            } else if (strCarType.equalsIgnoreCase(ConstantClass.TUKTUK)) {
                now = googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.tuktuk_icon)));
            } else {
                now = googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.owner_icon)));
            }
        }
    }

    /**
     * Draw a path between two locations.
     */

    private void drawPathBetweenTwoLocation(LatLng latLngPickUp, LatLng latLngDestination) {
        if (googleMap == null)
            return;
//        googleMap.clear();
//        Routing routing = new Routing(Routing.TravelMode.DRIVING);
//        routing.registerListener(CompleteRideFragment.this);
//        routing.execute(latLngPickUp, latLngDestination);
        //setMyCurrentLocationPin(latLngPickUp);

        getMyCarDrawable(latLngPickUp);

        if (myMarkerDropOff != null) {
            myMarkerDropOff.remove();
        }
        myMarkerDropOff = googleMap.addMarker(new MarkerOptions()
                .position(latLngDestination)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin1)));
        /*LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(latLngDestination);
        builder.include(latLngPickUp);
        LatLngBounds bounds = builder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, (int) getResources().getDimension(R.dimen.dp_50));
        googleMap.moveCamera(cameraUpdate);*/

        /*
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLngPickUp).zoom(20).build();

        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));*/
        int pixelWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

        float zoom = Math.round( Math.log(pixelWidth*360/0.008/256)/0.6931471805599453 );
        DebugLog.i("Zoom Level:" + zoom);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngPickUp, 16);
        googleMap.moveCamera(cameraUpdate);

        Routing routing = new Routing(Routing.TravelMode.DRIVING);
        routing.registerListener(CompleteRideFragment.this);
        routing.execute(latLngPickUp, latLngDestination);

    }

    @Override
    public void onRoutingFailure() {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(PolylineOptions mPolyOptions, Route route) {

        PolylineOptions polyoptions = new PolylineOptions();
        polyoptions.color(Color.parseColor("#00b3fd"));
        polyoptions.width(22);
        polyoptions.addAll(mPolyOptions.getPoints());
        if (googleMap != null)
            polyline=googleMap.addPolyline(polyoptions);


        //Modify by prakash 22-5-2017

        polyline.setStartCap(new RoundCap());
        polyline.setEndCap(new RoundCap());


    }


    /**
     * Get a call back when driver location changed.
     */

    public void onEvent(Location location) {

        oldLocation = newLocation;
        newLocation = location;
        bearingMoveCamera = oldLocation.bearingTo(newLocation);

        oldLatLng = pickUpLatlng;
        pickUpLatlng = new LatLng(location.getLatitude(), location.getLongitude());

        DebugLog.i("Current Latitude :" + pickUpLatlng);

        String data = AppHelper.getInstance().getSharedPref(ConstantClass.TRACK_RIDE_ORDER_DATA);
        trackRidePojo = ParsingHelper.getInstance().trackRideData(data);
        // if (firstCallOnly) {
        /**
         * Calculate a distance between two location and stored in shared preference.
         */

        try {
            if (AppHelper.getInstance().getSharedPref(ConstantClass.SHARED_PREFRENCE_DISTANCE) != null && !AppHelper.getInstance().getSharedPref(ConstantClass.SHARED_PREFRENCE_DISTANCE).equals("0.0") && !AppHelper.getInstance().getSharedPref(ConstantClass.SHARED_PREFRENCE_DISTANCE).equals("")) {
                Distance = Float.parseFloat(AppHelper.getInstance().getSharedPref(ConstantClass.SHARED_PREFRENCE_DISTANCE));
            }
            if (pickUpLatlng != null && oldLatLng != null) {

                float[] results = new float[1];
                Location.distanceBetween(oldLatLng.latitude, oldLatLng.longitude, pickUpLatlng.latitude, pickUpLatlng.longitude, results);
                float distance = results[0];

                Distance += distance;

                // Distance += distFrom(oldLatLng.latitude, oldLatLng.longitude, pickUpLatlng.latitude, pickUpLatlng.longitude);
                AppHelper.getInstance().SaveInSharedPref(ConstantClass.SHARED_PREFRENCE_DISTANCE, String.valueOf(Distance));
                //DataToPref.setSharedPreferanceData(getActivity(), Constant.DISTANCE_DATA, Constant.DISTANCE, String.valueOf(Distance));
                DebugLog.i("distance is" + Distance);
            }
        } catch (Exception e) {

        }

        if (googleMap != null) {

            if (!trackRidePojo.getDeliveryAddress().equalsIgnoreCase("")) {
                dropOffLatLng = new LatLng(Double.parseDouble(trackRidePojo.getDeliveryLatitude()),
                        Double.parseDouble(trackRidePojo.getDeliveryLongitude()));

                setMap(pickUpLatlng, new LatLng(Double.parseDouble(trackRidePojo.getDeliveryLatitude()),
                        Double.parseDouble(trackRidePojo.getDeliveryLongitude())));
            }

        }
        firstCallOnly = false;

        //Distance code

        //Log.e("rider location is ", "============" + location);
    }


    //Waiting,Assigned,Arrived,Processing
    @Override
    public void getUpdateLatLongWsResponseFromService(TrackRide pojo) {


    }

    @Override
    public void drawerLock() {
        navigatorInterface.drawerLock(true);
    }


    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        if (AppHelper.getInstance().getSharedPref(ConstantClass.USER_DEFAULT_LATITUDE) != null && AppHelper.getInstance().getSharedPref(ConstantClass.USER_DEFAULT_LONGITUDE) != null && googleMap != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(Double.parseDouble(AppHelper.getInstance().getSharedPref(ConstantClass.USER_DEFAULT_LATITUDE)), Double.parseDouble(AppHelper.getInstance().getSharedPref(ConstantClass.USER_DEFAULT_LONGITUDE)))).zoom(14).build();

            googleMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
        }

        if (googleMap != null) {
            if (!trackRidePojo.getDeliveryAddress().equalsIgnoreCase("")) {
                dropOffLatLng = new LatLng(Double.parseDouble(trackRidePojo.getDeliveryLatitude()),
                        Double.parseDouble(trackRidePojo.getDeliveryLongitude()));


                setMap(pickUpLatlng, new LatLng(Double.parseDouble(trackRidePojo.getDeliveryLatitude()),
                        Double.parseDouble(trackRidePojo.getDeliveryLongitude())));
            }
        }
       /* if (lat_lng_current != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(lat_lng_current).zoom(12).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }*/
    }


    /**
     * Call a send sms api .
     */



    public void sendSmsFromTwilio(String phone, String message, String sender_phone, String trip_id) {
        AlertUtils.showCustomProgressDialog(getActivity());

        String strToken = AppHelper.getInstance().getToken();
        DebugLog.i("SMS Request Send");
        ApiService api = RetroClient.getApiService();
        Call<GenericResponse> call = api.doSms(strToken, phone, message, sender_phone, trip_id);

        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, retrofit2.Response<GenericResponse> response) {
                AlertUtils.dismissDialog();
                if (response.isSuccessful()) {
                    DebugLog.i("SMS Success Response Code ::" + response.code());
                    GenericResponse responseBody = response.body();
                    if (response.code() == 201) {
                        showSnackBar(responseBody.getMessage(), null, linearMain);
                    }
                } else {
                    DebugLog.i("SMS Failed Response Code :" + response.code());
                    try {
                        String s = response.errorBody().string();
                        if (response.code() == ConstantClass.RESPONSE_CODE_400 ){
                            GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                            showSnackBar(responseBody.getMessage(), null, linearMain);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                DebugLog.e("SMS Request Failed:" + t.getMessage());
                AlertUtils.dismissDialog();
                homeActivity.showSnackbar(getString(R.string.bad_server));
            }
        });

    }

    public HashMap<String, String> setTwilioSmsData(String phone, String message, String sender_phone, String trip_id) {
        HashMap<String, String> param = new HashMap<>();
        param.put(ConstantClass.PHONE_NUMBER, phone);
        param.put(ConstantClass.MESSAGE, message);
        param.put(ConstantClass.SENDER_PHONE, sender_phone);
        param.put(ConstantClass.TRIP_ID, trip_id);

        return param;
    }
}
