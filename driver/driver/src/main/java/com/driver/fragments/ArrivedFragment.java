package com.driver.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.driver.routedisplayhelper.RouteDisplay;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
import com.driver.util.PorterShapeImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;

public class ArrivedFragment extends BaseFragment implements View.OnClickListener, RoutingListener, OnMapReadyCallback {

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

    AlertDialog.Builder builder;
    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private LatLng pickUpLatlng, dropOffLatLng;
    NavigatorInterface navigatorInterface;
    private HomeActivity homeActivity;

    boolean firstCallOnly = true;
    LatLng WazePickup;
    LatLng oldLatLng;
    double Distance = 0.0;
    private Marker now;
    TrackRide trackRidePojo;
    private LatLng lat_lng_current;
    private View view;
    private Polyline polyline;
    private RouteDisplay routedisplay;
    private LatLng driver_LatLng;
    private LatLng pickup_LatLng;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        DebugLog.i("Reached Arrived Screen");

        headerText.setText(R.string.rider_location);
        imgbtnCall.setOnClickListener(this);
        ImgbtnCancel.setOnClickListener(this);
        headerMenu.setOnClickListener(this);
        //btnCancel.setOnClickListener(this);
        txtDriverStatus.setText(R.string.arrival_status);
        txtDriverStatus.setOnClickListener(this);
        headerMenu.setEnabled(false);
        headerMenu.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lat_lng_current = new LatLng(LocationHelperService.dblLatitude, LocationHelperService.dblLongitude);
      /*  mapFragment = new SupportMapFragment();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.map, mapFragment);
        fragmentTransaction.addToBackStack(SupportMapFragment.class.getName());
        fragmentTransaction.commit();*/
        googleMapInitialization();
        String data = AppHelper.getInstance().getSharedPref(ConstantClass.TRACK_RIDE_ORDER_DATA);
        trackRidePojo = ParsingHelper.getInstance().trackRideData(data);

        if (trackRidePojo != null) {
            //DebugLog.e("First Name :" + trackRidePojo.getTrackTrip().getUserData().getName());
            //DebugLog.e("Path :: " + ConstantClass.USER_IMAGE_URL + trackRidePojo.getTrackTrip().getUserData().getProfileImage());
            txtName.setText("i'm " + trackRidePojo.getUserData().getName());
            txtPickupAddress.setText(trackRidePojo.getPickupAddress());
            Picasso.with(getContext()).
                    load(ConstantClass.USER_IMAGE_URL + trackRidePojo.getUserData().getProfileImage())
                    .resize(300, 300)
                    .placeholder(R.drawable.avatar_icon)
                    .into(imageViewProfile);
            //driver_LatLng = new LatLng(Double.parseDouble(trackRidePojo.getDriverData().getLatitude()), Double.parseDouble(trackRidePojo.getDriverData().getLongitude()));
            //pickup_LatLng = new LatLng(Double.parseDouble(trackRidePojo.getPickupLatitude()), Double.parseDouble(trackRidePojo.getPickupLongitude()));
        }

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }


        /**
         * open a google map or waze based on drive select.
         */

        imgGoogleNavigation.setOnClickListener(new View.OnClickListener() {
            public long doneButtonClickTime;

            @Override
            public void onClick(View v) {

                GeneralStaticAlertDialog.getInstance().createDialogWithWazeGoogle(getActivity(), new DialogDismissTwoButton() {
                    @Override
                    public void onOkClick() {
                        if (txtDriverStatus.getText().toString().equalsIgnoreCase("Arrived")) {
                            if (WazePickup != null) {

                                try {
                                    String url = "waze://?ll=" + WazePickup.latitude + "," + WazePickup.longitude + "&navigate=yes";
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(intent);
                                } catch (ActivityNotFoundException ex) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
                                    startActivity(intent);
                                }
                            }
                        } else {
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
                    }

                    @Override
                    public void onCancelClick() {


                        if (txtDriverStatus.getText().toString().equalsIgnoreCase("Arrived")) {
                            if (WazePickup != null) {

                                try {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + WazePickup.latitude + "," + WazePickup.longitude));
                                    startActivity(intent);
                                } catch (ActivityNotFoundException ex) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps"));
                                    startActivity(intent);
                                }
                            }
                        } else {
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
                String data = AppHelper.getInstance().getSharedPref(ConstantClass.TRACK_RIDE_ORDER_DATA);
                TrackRide trackRide = ParsingHelper.getInstance().trackRideData(data);

                Bundle properties = new Bundle();
                properties.putString("order_id", trackRide.getId());
                properties.putString("driver_id", trackRide.getDriverId());
                properties.putString("user_id", trackRide.getUserId());
                properties.putString("trip_type", trackRide.getTripType());
                AnalyticsReporter.getInstance().track(AnalyticsReporter.Event.DRIVER_ARRIVED_AT_PICKUP, properties);
                if (txtDriverStatus.getText().toString().equalsIgnoreCase("Arrived")) {
                    if (homeActivity.webServiceState == BaseActivity.WebServiceState.ACTIVE) {
                        callArrivedAtPickUpLocationWS();
                    } else {
                        headerText.setText("Ride");
                        txtDriverStatus.setText("Waiting for drop off location");
                    }
                }
                break;
        }
    }// end if onClick

    /**
     * Open a dialog for make call or sms to user.
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
//                            sendSmsFromTwilio(trackRidePojo.getUserData().getCountryCode() + trackRidePojo.getUserData().getPhone(), s, AppHelper.getInstance().getLoginUser().getCountryCode() + AppHelper.getInstance().getLoginUser().getPhone(), trackRidePojo.getId());
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
     * Call a arrived at pickup api.
     */

    private void callArrivedAtPickUpLocationWS() {
        AlertUtils.dismissDialog();
        AlertUtils.showCustomProgressDialog(getContext());

        String strToken = AppHelper.getInstance().getToken();
        String strDriverId = AppHelper.getInstance().getDriverId();
        String strOrderId = trackRidePojo.getId();
        DebugLog.i("Arrived Requestion with :" + strOrderId);
        ApiService api = RetroClient.getApiService();
        Call<GenericResponse> call = api.callArrivedAtPickupLocationWs(strToken, strDriverId, strOrderId);

        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, retrofit2.Response<GenericResponse> response) {
                AlertUtils.dismissDialog();
                if (response.isSuccessful()) {
                    DebugLog.i("Arrived Success Response Code :" + response.code());

                    if (response.code() == ConstantClass.RESPONSE_CODE_200) {

                        navigatorInterface.openStartRideFragment();
                    }
                } else {
                    try {
                        DebugLog.i("Arrived Failed Response Code :" + response.code());
                        String s = response.errorBody().string();
                        if (response.code() == ConstantClass.RESPONSE_CODE_400 ){
                            GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                            homeActivity.showSnackbar(responseBody.getMessage());
                        } else if ( response.code() == ConstantClass.RESPONSE_CODE_401 ) {
                            homeActivity.Logout(false);
                        } else {
                            GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                            if (responseBody == null) {
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
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                DebugLog.e("Arrived Failed Request Msg :" + t.getMessage());
                AlertUtils.dismissDialog();
                homeActivity.showSnackbar(getString(R.string.bad_server));

            }
        });

    }

    /**
     * Set map first time.
     */
    private void googleMapInitialization() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapRide);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        if(AppHelper.getInstance().getSharedPref(ConstantClass.USER_DEFAULT_LATITUDE)!=null && AppHelper.getInstance().getSharedPref(ConstantClass.USER_DEFAULT_LONGITUDE)!=null && googleMap!=null)
        {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(Double.parseDouble(AppHelper.getInstance().getSharedPref(ConstantClass.USER_DEFAULT_LATITUDE)), Double.parseDouble(AppHelper.getInstance().getSharedPref(ConstantClass.USER_DEFAULT_LONGITUDE)))).zoom(14).build();

            googleMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            // Added by Ma 12, March, 2018
            /*LoginResponse loginData = AppHelper.getInstance().getLoginUser();
            BitmapDescriptor bitmapMarker = null;
            String carType= null;
            try {
                String str = loginData.getCarType();
                String arrayString[] = str.split(",");
                carType = arrayString[arrayString.length-1];

            } catch (Exception e) {
                e.printStackTrace();
            }

            if(carType!=null)
            {
                if (carType.equalsIgnoreCase(ConstantClass.TAXI)) {
                    bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.taxi_icon);
                } else if (carType.equalsIgnoreCase(ConstantClass.BIKE)) {
                    bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.bike_icon);
                } else if (carType.equalsIgnoreCase(ConstantClass.TUKTUK)) {
                    bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.tuktuk_icon);
                } else {
                    bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.owner_icon);
                }
            }
*/
            /*now = googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(AppHelper.getInstance().getSharedPref(ConstantClass.USER_DEFAULT_LATITUDE)),Double.parseDouble(AppHelper.getInstance().getSharedPref(ConstantClass.USER_DEFAULT_LONGITUDE)))).icon(bitmapMarker));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(Double.parseDouble(AppHelper.getInstance().getSharedPref(ConstantClass.USER_DEFAULT_LATITUDE)),Double.parseDouble(AppHelper.getInstance().getSharedPref(ConstantClass.USER_DEFAULT_LONGITUDE)))).zoom(14).build();

            googleMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));*/
            googleMap.setMyLocationEnabled(false);
            MapsInitializer.initialize(getContext());
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
            //setMap(lat_lng_current, pickup_LatLng);
        }
      /*  if (lat_lng_current != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(lat_lng_current).zoom(12).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }*/
    }
    /**
     * Get a call back when driver location changed .
     */

    public void onEvent(Location location) {

        oldLatLng = pickUpLatlng;
        pickUpLatlng = new LatLng(location.getLatitude(), location.getLongitude());
        String data = AppHelper.getInstance().getSharedPref(ConstantClass.TRACK_RIDE_ORDER_DATA);
        trackRidePojo = ParsingHelper.getInstance().trackRideData(data);

        // if (firstCallOnly) {

        if (googleMap != null) {
            DebugLog.i("Delivery Latitude :" + trackRidePojo.getDeliveryLatitude());
            DebugLog.i("Delivery Longitude :" + trackRidePojo.getDeliveryLongitude());

            WazePickup = new LatLng(Double.parseDouble(trackRidePojo.getPickupLatitude()),
                    Double.parseDouble(trackRidePojo.getPickupLongitude()));
            setMap(pickUpLatlng, new LatLng(Double.parseDouble(trackRidePojo.getPickupLatitude()),
                    Double.parseDouble(trackRidePojo.getPickupLongitude())));

        }
        firstCallOnly = false;
        // }

    }

    public void setMap(LatLng latLngPickUp, LatLng latLngDestination) {

        if (latLngPickUp != null && latLngDestination != null) {
            try {
                googleMap.clear();
                drawPathBetweenTwoLocation(latLngPickUp, latLngDestination);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Draw a path between two locations .
     */

    private void drawPathBetweenTwoLocation(LatLng latLngPickUp, LatLng latLngDestination) {
        if (googleMap == null)
            return;
/*        Routing routing = new Routing(Routing.TravelMode.DRIVING);
        routing.registerListener(ArrivedFragment.this);
        routing.execute(latLngPickUp, latLngDestination);*/
        DebugLog.i("Pickup Latitude :" + latLngPickUp.latitude + ", Pickup Longitude : " + latLngPickUp.longitude);

        /**
         * here latLngPickUp is current vehicle's position, while latLngDestination is pickup position the rider selected.
         */
        //setMyCurrentLocationPin(latLngPickUp);

        getMyCarDrawable(latLngPickUp);

        googleMap.addMarker(new MarkerOptions()
                .position(latLngDestination)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin1)));
        //added by rhc
        routedisplay = new RouteDisplay();
        routedisplay.RouteFromGoogle(googleMap, latLngPickUp, latLngDestination);
        //end
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(latLngPickUp);
        builder.include(latLngDestination);
        //builder.include(Dropoff_LatLng); //added by rhc
        LatLngBounds bounds = builder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, (int) getResources().getDimension(R.dimen.dp_60));
        googleMap.moveCamera(cameraUpdate);
/*        int pixelWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

        float zoom = Math.round( Math.log(pixelWidth*360/0.008/256)/0.6931471805599453 );
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngPickUp, 16);
        googleMap.moveCamera(cameraUpdate);*/

    }


    /**
     * Set a car icon on map based on driver gender and car type.
     */

    private void getMyCarDrawable(LatLng latLng) {
        LoginResponse loginResponse = AppHelper.getInstance().getLoginUser();
        DebugLog.i("Car Type " + loginResponse.getCarType());

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

    @Override
    public void onRoutingFailure() {

    }

    @Override
    public void onRoutingStart() {

    }

    /**
     * Get a call back when successfully draw a path.
     */

    @Override
    public void onRoutingSuccess(PolylineOptions mPolyOptions, Route route) {

        PolylineOptions polyoptions = new PolylineOptions();
        polyoptions.color(Color.parseColor("#00b3fd"));
        polyoptions.width(22);
        polyoptions.addAll(mPolyOptions.getPoints());
        if (googleMap != null)
            polyline=googleMap.addPolyline(polyoptions);
        DebugLog.i("Draw Polyline");

        //Modify by prakash 22-5-2017


        polyline.setStartCap(new RoundCap());
        polyline.setEndCap(new RoundCap());




        //createDashedLine(googleMap, mPolyOptions.getPoints());
       /* if (googleMap != null) {
            createDashedLine(googleMap, pickUpLatlng, new LatLng(Double.parseDouble(currentTrackrideData.getTrackTrip().getPickupLatitude()),
                    Double.parseDouble(currentTrackrideData.getTrackTrip().getPickupLongitude())), Color.parseColor("#8f8f8f"));
        }*/

    }


    //Waiting,Assigned,Arrived,Processing
    @Override
    public void getUpdateLatLongWsResponseFromService(TrackRide pojo) {

    }

    @Override
    public void drawerLock() {
        navigatorInterface.drawerLock(true);
    }

    /**
     * Call a send sms api.
     */

    public void sendSmsFromTwilio(String phone, String message, String sender_phone, String trip_id) {
        AlertUtils.showCustomProgressDialog(getActivity());

        String strToken = AppHelper.getInstance().getToken();
        DebugLog.i("SMS Send Request");
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
                DebugLog.e("SMS Request Failed Msg : " + t.getMessage());
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
