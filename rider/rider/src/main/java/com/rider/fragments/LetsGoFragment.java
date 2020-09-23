package com.rider.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.siyamed.shapeimageview.ImageViewLetsGo;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
import com.google.gson.Gson;
import com.rider.LAOXI;
import com.rider.LocationHelper.LocationHelperService;
import com.rider.LocationHelper.RouteDisplay;
import com.rider.R;
import com.rider.activity.TwilioCallActivity;
import com.rider.customclasses.CustomEditText;
import com.rider.customclasses.CustomTextView;
import com.rider.dialog.AlertUtils;
import com.rider.dialog.GeneralStaticAlertDialog;
import com.rider.navigator.NavigatorInterface;
import com.rider.pojo.DriverDetails;
import com.rider.pojo.GenericResponse;
import com.rider.pojo.OrderData;
import com.rider.pojo.User;
import com.rider.retrofit.ApiService;
import com.rider.retrofit.RetroClient;
import com.rider.route.Route;
import com.rider.route.Routing;
import com.rider.route.RoutingListener;
import com.rider.utils.DataToPref;
import com.rider.utils.DebugLog;
import com.rider.utils.DialogDismissTwoButton;
import com.rider.utils.DialogDismissTwoButtonwithText;
import com.rider.utils.LaoxiConstant;
import com.rider.utils.ParsingHelper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;

import static com.rider.R.id.map;
import static com.rider.utils.DistanceUtil.distance;

/**
 * Created by hitesh on 20/7/16.
 */
public class LetsGoFragment extends BaseFragment implements RoutingListener, OnMapReadyCallback {

    @Bind(R.id.header_menu)
    ImageView headerMenu;
    @Bind(R.id.header_text)
    CustomTextView headerText;
    @Bind(R.id.btnCall)
    ImageButton btnCall;

    @Bind(R.id.layoutCallMessage)
    LinearLayout layoutCallMessage;
    @Bind(R.id.imageViewProfile)
    ImageViewLetsGo imageViewProfile;
    @Bind(R.id.txtHi)
    ImageView txtHi;
    @Bind(R.id.txtName)
    CustomTextView txtName;
    @Bind(R.id.txtCarModel)
    CustomTextView txtCarModel;
    @Bind(R.id.imageViewComma)
    ImageView imageViewComma;
    @Bind(R.id.edtPickupFrom)
    CustomEditText edtPickupFrom;
    @Bind(R.id.edtDropOff)
    CustomEditText edtDropOff;
    @Bind(R.id.img_current_location)
    ImageView imgCurrentLocation;
    @Bind(R.id.txtRideLater)
    CustomTextView txtRideLater;
    @Bind(R.id.linear_main)

    LinearLayout linearMain;
    NavigatorInterface navigatorInterface;
    private SupportMapFragment mapFragment;
    //private GoogleMap map;
    private LatLng LAT_LNG;
    private User user;
    private OrderData orderData;
    private LatLng DropOff_LatLng;
    private Routing routing;
    private Marker myMarkerPickup, myMarkerDropOff;
    private Polyline polyline;
    private GoogleMap mMap;
    Location prevLoc = new Location("");
    private LatLng oldLatLng;
    Gson mGson = new Gson();
    private LatLng lat_lng_current;
    private RouteDisplay routeDisplay;
    Handler handler;
    Runnable myRunnable;
    private Marker dropoffMarker;
    private Marker riderMarker;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigatorInterface = (NavigatorInterface) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lets_go_layout, container, false);
        ButterKnife.bind(this, view);

        /**
         * Load a data from shared preference.
         */

        EventBus.getDefault().register(LetsGoFragment.this);
        String strUserData = DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA);
        user = mGson.fromJson(strUserData, User.class);
        String strOrderData = DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.ORDER, LaoxiConstant.ORDER_DATA);
        orderData = mGson.fromJson(strOrderData, OrderData.class);
        LAT_LNG = new LatLng(Double.valueOf(orderData.getDriverData().getLatitude()), Double.valueOf(orderData.getDriverData().getLongitude()));

        // headerText.setText(R.string.letsgo);

        String strProfile = orderData.getDriverData().getProfileImage();
        Picasso.with(getContext())
                .load(getResources().getString(R.string.MAINURL_FOR_DRIVER_IMAGE) + strProfile)
                .fit()
                .placeholder(R.drawable.avatar_icon)
                .into(imageViewProfile);

        txtName.setText("i'm " + orderData.getDriverData().getFname());
        txtCarModel.setText(orderData.getDriverData().getVehicleModel() + "\n" + orderData.getDriverData().getVehicleRegNumber());
        edtPickupFrom.setText(orderData.getPickupAddress());

        if (!orderData.getDeliveryAddress().equalsIgnoreCase("")) {

            edtDropOff.setText(orderData.getDeliveryAddress());
            edtDropOff.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_sign, 0);

        }
        else
        {
            edtDropOff.setText(LAOXI.currentRide.getDropOffAddress());
            edtDropOff.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_sign, 0);
        }

        /*if(LAOXI.currentRide.getDropOffAddress()!=null || !LAOXI.currentRide.getDropOffAddress().equalsIgnoreCase("")){
            txtDropOff.setText(LAOXI.currentRide.getDropOffAddress());
        }*/
        return view;
    }

    @OnClick({R.id.header_menu, R.id.edtDropOff, R.id.img_current_location, R.id.txtRideLater, R.id.btnCall})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_menu:
                //HomeActivity.toggleDrawer();
                break;
            case R.id.edtDropOff:
                // findPlaceDropOff(edtDropOff);
                break;
            case R.id.img_current_location:
                break;
            case R.id.txtRideLater:
                break;
            case R.id.btnCall:

                /**
                 * Make a call with driver using twilio calling .
                 */

               if(orderData!=null)
               {
                   String aa[] = {"Call", "Message"};
                   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                   builder.setTitle("Please select mode");
                   builder.setItems(aa, new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int i) {
                           try {
                               if (i == 0) {
                                   String phone;
                                   char first = orderData.getDriverData().getPhone().charAt(0);

                                   if (first == '0')
                                   {
                                       phone= orderData.getDriverData().getPhone();
                                       phone=phone.substring(1,phone.length());
                                   }
                                   else
                                   {
                                       phone=orderData.getDriverData().getPhone();
                                   }

                                   DebugLog.e("phone is a"+phone);


                                   Intent intent = new Intent(getActivity(), TwilioCallActivity.class);
                                   intent.putExtra("Client", orderData.getDriverData().getCountryCode() + phone);
                                   startActivity(intent);
                               } else if (i == 1) {
                                   GeneralStaticAlertDialog.getInstance().createDialogSendSms(getActivity(), "", new DialogDismissTwoButtonwithText() {
                                       @Override
                                       public void onOkClick(String s) {
                                           sendSmsFromTwilio(orderData.getDriverData().getCountryCode() + orderData.getDriverData().getPhone() , s, user.getCountryCode() + user.getPhone(), orderData.getId());
                                       }

                                       @Override
                                       public void onCancelClick() {

                                       }
                                   });
                               }
                           } catch (Exception e) {
                               e.printStackTrace();
                           }
                           dialog.dismiss();
                       }
                   });
                   builder.show();
               }

                break;
            /*case R.id.btnCancel:
                GeneralStaticAlertDialog.getInstance().createDialogWithTwoButton(getActivity(), "You wanna cancel trip?", new DialogDismissTwoButton() {
                    @Override
                    public void onOkClick() {

                        if(orderData!=null)
                        {
                            wsCallCancelOrder();
                        }

                    }

                    @Override
                    public void onCancelClick() {

                    }
                });

                break;*/
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lat_lng_current = new LatLng(LocationHelperService.dblLatitude, LocationHelperService.dblLongitude);
        GoogleMapInitialization();

        //put handler for get updated driver latlong

        handler=  new Handler();

        myRunnable = new Runnable() {
            public void run() {
                wsCallGetDriverDetails(orderData.getDriverId());
                handler.postDelayed(myRunnable,15000);

            }
        };
        handler.postDelayed(myRunnable,15000);
    }

    private void GoogleMapInitialization() {
        /*FragmentManager fm = getChildFragmentManager();

        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        fm.beginTransaction().replace(map, supportMapFragment).commit();*/
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(map);
        supportMapFragment.getMapAsync(LetsGoFragment.this);
    }

    @Override
    public void onResume() {
        super.onResume();
       // EventBus.getDefault().register(LetsGoFragment.this);

    }


    public void onEvent(final Location location) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                oldLatLng = LAT_LNG;

//        setMAp(LAT_LNG, GanpatMeridian);

              if(orderData!=null)
              {
                  if (!orderData.getDeliveryAddress().equalsIgnoreCase("")) {

                      edtDropOff.setText(orderData.getDeliveryAddress());
                      edtDropOff.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_sign, 0);


                      DropOff_LatLng = new LatLng(Double.valueOf(orderData.getDeliveryLatitude()), Double.valueOf(orderData.getDeliveryLongitude()));
                      setupMap(mMap);

                  }
                  else
                  {
                      edtDropOff.setText(LAOXI.currentRide.getDropOffAddress());
                      edtDropOff.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_sign, 0);


                      DropOff_LatLng = new LatLng(LAOXI.currentRide.getDropOffLatitude(), LAOXI.currentRide.getDropOffLongitude());
                      setupMap(mMap);
                  }

              }
            }
        });


        DebugLog.e("location is " + "============" + location);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(LetsGoFragment.this);
        if(handler!=null)
        {
            handler.removeCallbacks(myRunnable);
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(!DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.USER_DEFAULT_LATITUDE, LaoxiConstant.USER_DEFAULT_LATITUDE_DATA).equalsIgnoreCase("") && !DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.USER_DEFAULT_LONGITUDE, LaoxiConstant.USER_DEFAULT_LONGITUDE_DATA).equalsIgnoreCase("") && mMap!=null)
        {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(Double.parseDouble(DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.USER_DEFAULT_LATITUDE, LaoxiConstant.USER_DEFAULT_LATITUDE_DATA)),Double.parseDouble(DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.USER_DEFAULT_LONGITUDE, LaoxiConstant.USER_DEFAULT_LONGITUDE_DATA)))).zoom(14).build();

            mMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }
        mMap.setMyLocationEnabled(false);
        MapsInitializer.initialize(getContext());
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));

        if(orderData!=null)
        {
            if (!orderData.getDeliveryAddress().equalsIgnoreCase("")) {

                DropOff_LatLng = new LatLng(Double.valueOf(orderData.getDeliveryLatitude()), Double.valueOf(orderData.getDeliveryLongitude()));
                setupMap(mMap);
            }
            else
            {
                DropOff_LatLng = new LatLng(LAOXI.currentRide.getDropOffLatitude(), LAOXI.currentRide.getDropOffLongitude());
                setupMap(mMap);
            }

        }

    }

    private void setupMap(GoogleMap mapFragment) {
        if (mapFragment != null) {
            try {
                //map = mapFragment.getMap();
/*
                CameraPosition cameraPosition = new CameraPosition.Builder().target(LAT_LNG).zoom(12).build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

                    //SetCar(LAT_LNG); //display vehicle's position
                    setPickupPin(LAT_LNG);
                    setDropOffPin(DropOff_LatLng);

                    /**
                     * added by rhc
                     */

                    routeDisplay = new RouteDisplay();
                    routeDisplay.RouteFromGoogle(mapFragment, LAT_LNG, DropOff_LatLng);
                    /**
                    * 20180626
                     */
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(LAT_LNG);
                    builder.include(DropOff_LatLng);
                    LatLngBounds bounds = builder.build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, (int) getResources().getDimension(R.dimen.dp_60));
                    mMap.moveCamera(cameraUpdate);
/*                    routing = new Routing(Routing.TravelMode.DRIVING);
                    routing.registerListener(LetsGoFragment.this);
                    routing.execute(LAT_LNG, DropOff_LatLng);*/

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Set a car icon on map based on car type and gender of driver .
     */

    private void SetCar(LatLng latLng) {
        if (myMarkerPickup != null) {
            myMarkerPickup.remove();
        }
        String strCarType = orderData.getCarType();

        if (strCarType.equalsIgnoreCase(LaoxiConstant.TAXI)) {
            myMarkerPickup = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_icon)));
        } else if (strCarType.equalsIgnoreCase(LaoxiConstant.BIKE)) {
            myMarkerPickup = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_icon)));
        } else if (strCarType.equalsIgnoreCase(LaoxiConstant.TUKTUK)) {
            myMarkerPickup = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.tuktuk_icon)));
        } else {
            myMarkerPickup = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.owner_taxi)));
        }

    }


    private void setPickupPin(LatLng latLng) {
        if (riderMarker != null) {
            riderMarker.remove();
        }
        riderMarker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.drop_location_pin)));
    }

    private void setDropOffPin(LatLng latLng) {
     /*   if (myMarkerDropOff != null) {
            myMarkerDropOff.remove();
        }*/
        /*if (myMarkerDropOff != null) {
            myMarkerDropOff.remove();
        }
        myMarkerDropOff = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.drop_location_pin)));*/
        if (dropoffMarker != null) {
            dropoffMarker.remove();
        }
        dropoffMarker = mMap.addMarker(new MarkerOptions()
                .position(DropOff_LatLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
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

        if (polyline != null) {
            polyline.remove();
        }
        polyline = mMap.addPolyline(polyoptions);
        polyline.setStartCap(new RoundCap());
        polyline.setEndCap(new RoundCap());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                // dropOffLatLng = place.getLatLng();

               /* CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(dropOffLatLng).zoom(14).build();

                map.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));*/

                edtDropOff.setText(place.getAddress() + "\n" + place.getPhoneNumber());
                LAOXI.currentRide.setDropOffAddress(place.getName() + ",\n" + place.getAddress() + "\n" + place.getPhoneNumber());

                LAOXI.currentRide.setDropOffLatitude(place.getLatLng().latitude);
                LAOXI.currentRide.setDropOffLongitude(place.getLatLng().longitude);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                DebugLog.e("Tag " + status.getStatusMessage());

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    private double getConvertedDistance(LatLng latlng1, LatLng latlng2) {
        double distance = distance(latlng1.latitude,
                latlng1.longitude,
                latlng2.latitude,
                latlng2.longitude);
        BigDecimal bd = new BigDecimal(distance);
        BigDecimal res = bd.setScale(100, RoundingMode.DOWN);
        return res.doubleValue();
    }

    /**
     * Call a send sms api using twilio.
     */


    public void sendSmsFromTwilio(String phone, String message, String sender_phone, String trip_id) {
        AlertUtils.showCustomProgressDialog(getActivity());
        String strToken = user.getToken();

        DebugLog.i("SMS Request");
        ApiService api = RetroClient.getApiService();
        Call<GenericResponse> call = api.doSms(strToken, phone, message, sender_phone, trip_id);

        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, retrofit2.Response<GenericResponse> response) {
                AlertUtils.dismissDialog();
                if (response.isSuccessful()) {
                    DebugLog.i("SMS Response Success Code :" + response.code());
                    GenericResponse responseBody = response.body();
                    if (response.code() == 201) {

                        showSnackBar(responseBody.getMessage(), null, linearMain);
                    }
                } else {
                    DebugLog.e("SMS Response Failed Code :" + response.code());
                    try {
                        String s = response.errorBody().string();
                        if (response.code() == 400 ){
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
                DebugLog.e("SMS Response Failed Code :" + t.getMessage());
                AlertUtils.dismissDialog();
            }
        });
    }

    public HashMap<String, String> setTwilioSmsData(String phone, String message, String sender_phone, String trip_id) {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.PHONE, phone);
        param.put(LaoxiConstant.MESSAGE, message);
        param.put(LaoxiConstant.SENDER_PHONE, sender_phone);
        param.put(LaoxiConstant.TRIP_ID, trip_id);

        return param;
    }


    /**
     * Call a get Driver details api .
     */

    public void wsCallGetDriverDetails(final String driverId) {

        DebugLog.i("Get Driver Detail Request");
        ApiService api = RetroClient.getApiService();
        String strToken = user.getToken();
        Call<DriverDetails> call = api.doGetDriverDetails(strToken, driverId, "", "");

        call.enqueue(new Callback<DriverDetails>() {
            @Override
            public void onResponse(Call<DriverDetails> call, retrofit2.Response<DriverDetails> response) {
                AlertUtils.dismissDialog();
                if ( response.isSuccessful() ) {
                    if ( response.code() == 200 ) {
                        DriverDetails driverDetails = response.body();
                        DebugLog.i("Get Driver Detail Success Response : " + driverDetails.getDeviceId());

                        LAT_LNG = new LatLng(Double.valueOf(driverDetails.getLatitude()), Double.valueOf(driverDetails.getLongitude()));
                    }

                } else {
                    DebugLog.w("Get DriverDetail Response Failed");
                    if (response.code() == 400) {
                        wsCallGetDriverDetails(driverId);
                    }
                }
            }

            @Override
            public void onFailure(Call<DriverDetails> call, Throwable t) {
                DebugLog.e("Get DriverDetail Request Failed" + t.getMessage());
                AlertUtils.dismissDialog();
            }
        });
    }

    public HashMap<String, String> setDriverData(String driverId) {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.DRIVER_ID, driverId);
        return param;
    }



}// end of main class
