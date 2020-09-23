package com.rider.fragments;

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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.siyamed.shapeimageview.ImageViewLetsGo;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.gson.Gson;
import com.rider.LocationHelper.LocationHelperService;
import com.rider.LocationHelper.RouteDisplay;
import com.rider.R;
import com.rider.activity.HomeActivity;
import com.rider.activity.TwilioCallActivity;
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
import com.rider.utils.DateTimeClass;
import com.rider.utils.DebugLog;
import com.rider.utils.DialogDismissTwoButton;
import com.rider.utils.DialogDismissTwoButtonwithText;
import com.rider.utils.DistanceUtil;
import com.rider.utils.LaoxiConstant;
import com.rider.utils.ParsingHelper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by hitesh on 20/7/16.
 */
public class TrackDriverFragment extends BaseFragment implements View.OnClickListener, RoutingListener, OnMapReadyCallback {

    NavigatorInterface navigatorInterface;
    @Bind(R.id.header_menu)
    ImageView headerMenu;
    @Bind(R.id.header_text)
    CustomTextView headerText;
    @Bind(R.id.img_share)
    ImageView imgShare;
    @Bind(R.id.txtArrivalTime)
    CustomTextView txtArrivalTime;
    @Bind(R.id.linear_main)
    LinearLayout linearMain;
    @Bind(R.id.mainFrame)
    FrameLayout mainFrame;
    @Bind(R.id.btnCall)
    ImageButton btnCall;
    @Bind(R.id.btnMessage)
    ImageButton btnMessage;
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

    @Bind(R.id.linear_main_time)
    LinearLayout linear_main_time;
    private long mLastClickTime = 0;

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private LatLng Current_LatLng;
    private LatLng Driver_LatLng;
    private LatLng PickUp_LatLng;
    private LatLng Dropoff_LatLng;

    private User user;
    private OrderData orderData;
    Gson mGson = new Gson();
    private Routing routing;
    private Marker myMarkerPickup, myMarkerDropOff;
    private Polyline polyline; Location prevLoc = new Location("");
    private LatLng oldLatLng;
    SupportMapFragment supportMapFragment;
    LatLng lat_lng_current;
    private DriverDetails driverDetails;
    Handler handler;
    Runnable myRunnable;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem GAP = new Gap(15);
    //
// Create a stroke pattern of a gap followed by a dot.
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);
    private RouteDisplay routedisplay;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.track_driver_layout, container, false);
        ButterKnife.bind(this, view);

        /**
         * Load a data from shared preference and shows on screen.
         */

        String strUserData = DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA);

        user = mGson.fromJson(strUserData, User.class);

        String strOrderData = DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.ORDER, LaoxiConstant.ORDER_DATA);
        orderData = mGson.fromJson(strOrderData, OrderData.class);

        Driver_LatLng = new LatLng(Double.valueOf(orderData.getDriverData().getLatitude()), Double.valueOf(orderData.getDriverData().getLongitude()));
        PickUp_LatLng = new LatLng(Double.valueOf(orderData.getPickupLatitude()), Double.valueOf(orderData.getPickupLongitude()));
        Dropoff_LatLng = new LatLng(Double.valueOf(orderData.getDeliveryLatitude()), Double.valueOf(orderData.getDeliveryLongitude()));
        EventBus.getDefault().register(TrackDriverFragment.this);

        Picasso.with(getContext())
                .load(getResources().getString(R.string.MAINURL_FOR_DRIVER_IMAGE) + orderData.getDriverData().getProfileImage())
                .fit()
                .placeholder(R.drawable.avatar_icon)
                .into(imageViewProfile);

        txtName.setText("i'm "+orderData.getDriverData().getFname());
        txtCarModel.setText(orderData.getDriverData().getVehicleModel()+"\n"+orderData.getDriverData().getVehicleRegNumber());

        imgShare.setVisibility(View.GONE);
        headerText.setText(R.string.track_driver);
        btnCall.setOnClickListener(this);
        btnMessage.setOnClickListener(this);
        //btnCancel.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lat_lng_current = new LatLng(LocationHelperService.dblLatitude, LocationHelperService.dblLongitude);

        googleMapInitialization();


        showConfirmAlert();
        //put handler for get updated driver latlong

        handler=  new Handler();

        myRunnable = new Runnable() {
            public void run() {
                wsCallGetDriverDetails(orderData.getDriverId());
                handler.postDelayed(myRunnable,30000);

            }
        };
        handler.postDelayed(myRunnable,30000);

    }

    private void googleMapInitialization() {
       // FragmentManager fm = getChildFragmentManager();

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }
    public void showConfirmAlert() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 1500ms
                GeneralStaticAlertDialog.getInstance().createDialogWithTwoButton(getActivity(), getString(R.string.CONFIRM_TRIP), new DialogDismissTwoButton() {
                    @Override
                    public void onOkClick() {

                    }

                    @Override
                    public void onCancelClick() {
                        if(orderData!=null)
                        {
                            wsCallCancelOrder();
                        }
                    }
                });

            }
        }, 1500);
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void onEvent(final Location location)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                oldLatLng = Driver_LatLng;

                //provider name is unecessary

                Current_LatLng = new LatLng(location.getLatitude(), location.getLongitude());

            }
        });
        DebugLog.e("location is " + "============" + location);
    }

    @OnClick(R.id.header_menu)
    public void onClick() {
        HomeActivity.toggleDrawer();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigatorInterface = (NavigatorInterface) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(TrackDriverFragment.this);
        if(handler!=null)
        {
            handler.removeCallbacks(myRunnable);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCall:

               if(orderData!=null)
               {
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
               }

                break;

            /*case R.id.btnCancel:

                *//**
                 * Cancel a ride at that time calculate a tie from current time to start trip time.
                 *//*

                String strOrderData = DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.ORDER, LaoxiConstant.ORDER_DATA);
                orderData = mGson.fromJson(strOrderData, OrderData.class);

                String strTripStartDateTime=orderData.getStartDatetime();
                String cancellationAmount=orderData.getCancellation()+"";

                Calendar c = Calendar.getInstance();
                System.out.println("Current time => "+c.getTime());
                SimpleDateFormat dfCurrent = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateTime = dfCurrent.format(c.getTime());

                String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
                Date dateCurrent = new Date();
                SimpleDateFormat sdfCurrent = new SimpleDateFormat(DATE_FORMAT_NOW);
                String stringcurrentDate = sdfCurrent.format(dateCurrent);

                Date dateCurrentFinal = null;
                Date dateTrip = null;
                try {
                    dateCurrentFinal = sdfCurrent.parse(stringcurrentDate);
                    DebugLog.e("dateCurrentFinal"+dateCurrentFinal);
                } catch(ParseException e){
                    e.printStackTrace();
                } catch(Exception e){
                    e.printStackTrace();
                }

                // convert in local

                strTripStartDateTime= DateTimeClass.getInstance().utcToLocalTimeNew(strTripStartDateTime);
                String DATE_FORMAT_TRIP = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat sdfTripDate = new SimpleDateFormat(DATE_FORMAT_TRIP);
                try {
                    dateTrip = sdfTripDate.parse(strTripStartDateTime);
                    DebugLog.e("dateTrip"+dateTrip);
                } catch(ParseException e){
                    e.printStackTrace();
                 } catch(Exception e){
                    e.printStackTrace();
                 }

                long diff = dateCurrentFinal.getTime() - dateTrip.getTime();
                long seconds = diff / 1000;
                long minutes = seconds / 60;

                if(minutes>5){
                    GeneralStaticAlertDialog.getInstance().createDialogWithTwoButton(getActivity(), "You wanna cancel trip? \n You will be charged $" + cancellationAmount+".", new DialogDismissTwoButton() {
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
                }else{
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
                }

                break;*/
            case R.id.btnMessage:
                GeneralStaticAlertDialog.getInstance().createDialogSendSms(getActivity(), "", new DialogDismissTwoButtonwithText() {
                    @Override
                    public void onOkClick(String s) {

                        String strOrderId = orderData.getId();
                        String strSenderPhone = user.getCountryCode() + user.getPhone();
                        String strDriverPhone = orderData.getDriverData().getCountryCode()+orderData.getDriverData().getPhone();
                        DebugLog.i("Order id :" + strOrderId + " Sender Phone : " + strSenderPhone + " Driver Phone :" + strDriverPhone);
                        sendSmsFromTwilio(strDriverPhone, s, strSenderPhone, strOrderId);
                    }

                    @Override
                    public void onCancelClick() {

                    }
                });
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        // map.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(23.0446275,72.6683333) , 14.0f) );
        //map = mapFragment.getMap();

        if(!DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.USER_DEFAULT_LATITUDE, LaoxiConstant.USER_DEFAULT_LATITUDE_DATA).equalsIgnoreCase("") && !DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.USER_DEFAULT_LONGITUDE, LaoxiConstant.USER_DEFAULT_LONGITUDE_DATA).equalsIgnoreCase("") && map!=null)
        {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(Double.parseDouble(DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.USER_DEFAULT_LATITUDE, LaoxiConstant.USER_DEFAULT_LATITUDE_DATA)),Double.parseDouble(DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.USER_DEFAULT_LONGITUDE, LaoxiConstant.USER_DEFAULT_LONGITUDE_DATA)))).zoom(14).build();

            map.moveCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }

        map.setMyLocationEnabled(false);
        MapsInitializer.initialize(getContext());
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));


        Driver_LatLng = new LatLng(Double.valueOf(orderData.getDriverData().getLatitude()), Double.valueOf(orderData.getDriverData().getLongitude()));
        if(map!=null){
            setupMap(map);
        }

        /*if(lat_lng_current!=null)
        {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(lat_lng_current).zoom(12).build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }*/

    }

    private void setupMap(GoogleMap mapFragment) {
        if (mapFragment != null) {
            try {
                /*CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(PickUp_LatLng).zoom(12).build();
                map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
                setPickupPin(PickUp_LatLng);
                setDriverPin(Driver_LatLng);
                //added by rhc
                routedisplay = new RouteDisplay();
                routedisplay.RouteFromGoogle(mapFragment, PickUp_LatLng, Driver_LatLng);
                //end
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(PickUp_LatLng);
                builder.include(Driver_LatLng);
                //builder.include(Dropoff_LatLng); //added by rhc
                LatLngBounds bounds = builder.build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, (int) getResources().getDimension(R.dimen.dp_60));
                map.moveCamera(cameraUpdate);

                /*routing = new Routing(Routing.TravelMode.DRIVING);
                routing.registerListener(TrackDriverFragment.this);
                routing.execute(Driver_LatLng, PickUp_LatLng);*/


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Set a driver car icon on map based on driver gender and car type.
     */

    private void setDriverPin(LatLng latLng) {
        if (myMarkerPickup != null) {
            myMarkerPickup.remove();
        }

        DebugLog.e("Car type " + orderData.getDriverData().getCarType());
        String strCarType = orderData.getCarType();

        if (strCarType.equalsIgnoreCase(LaoxiConstant.TAXI)) {
            myMarkerPickup = map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_icon)));
        } else if (strCarType.equalsIgnoreCase(LaoxiConstant.BIKE)) {
            myMarkerPickup = map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_icon)));
        } else if (strCarType.equalsIgnoreCase(LaoxiConstant.TUKTUK)) {
            myMarkerPickup = map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.tuktuk_icon)));
        } else {
            myMarkerPickup = map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.owner_taxi)));
        }
     }

    private void setPickupPin(LatLng latLng) {
        if (myMarkerDropOff != null) {
            myMarkerDropOff.remove();
        }
        myMarkerDropOff = map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.drop_location_pin)));
    }


    /**
     * Call a cancel order api.
     */

    public void wsCallCancelOrder() {
        String strToken = user.getToken();
        String strUserId = user.getId();
        String strOrderId = orderData.getId();
        String strCardToken;
        if (user.getCardInfo().isEmpty()) strCardToken = "";
        else strCardToken = user.getCardInfo().get(0).getToken();//To do

        DebugLog.i("Call Cancel Order Request");
        AlertUtils.showCustomProgressDialog(getContext());
        ApiService api = RetroClient.getApiService();

        Call<GenericResponse> call = api.doCancelOrder(strToken, strUserId, "user", strOrderId, strCardToken);
        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, retrofit2.Response<GenericResponse> response) {
                AlertUtils.dismissDialog();
                if (response.isSuccessful()) {
                    DebugLog.i("Cancel Success Response");

                    if (response.code() == 200) {
                        DataToPref.setSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.IS_PROVIDE_DROPOFF, LaoxiConstant.IS_PROVIDE_DROPOFF_DATA,"false");
                        NewHomeFragment.showTransparentLayout = false;
                        navigatorInterface.openHome();
                        // navigatorInterface.stopTrackRideService();
                    }
                } else {
                    DebugLog.e("Cancel Failed Response");
                    try {
                        String s = response.errorBody().string();
                        if (response.code() == 402) {

                                GenericResponse genericResponse = ParsingHelper.getInstance().genericResponseParsing(s);
                                GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(getActivity(), genericResponse.getMessage(), null);
                                navigatorInterface.openPaymentDetails();
                        } else {
                            GenericResponse genericResponse = ParsingHelper.getInstance().genericResponseParsing(s);
                            showSnackBar(genericResponse.getMessage(), null, linearMain);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                AlertUtils.dismissDialog();
                DebugLog.e("CancelOrder Request Failed Msg :" + t.getMessage());
            }
        });
    }

    @Override
    public void onRoutingFailure() {

    }

    @Override
    public void onRoutingStart() {

    }

    /**
     * Get a call back when draw a path on map.
     */

    @Override
    public void onRoutingSuccess(PolylineOptions mPolyOptions, Route route) {
        try {
            PolylineOptions polyoptions = new PolylineOptions();
            polyoptions.color(Color.parseColor("#00b3fd"));
            polyoptions.width(22);

            polyoptions.addAll(mPolyOptions.getPoints());
            linear_main_time.setVisibility(View.VISIBLE);
            txtArrivalTime.setText(route.getDurationText());
            if (polyline != null) {
                polyline.remove();
            }

            polyline = map.addPolyline(polyoptions);
            polyline.setStartCap(new RoundCap());
            polyline.setEndCap(new RoundCap());
           // polyline.setPattern(PATTERN_POLYLINE_DOTTED);
            //createDashedLine(map, mPolyOptions.getPoints());

/*            linear_main_time.setVisibility(View.VISIBLE);
            txtArrivalTime.setText(route.getDurationText());
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private double getConvertedDistance(LatLng latlng1, LatLng latlng2) {
        double distance = DistanceUtil.distance(latlng1.latitude,
                latlng1.longitude,
                latlng2.latitude,
                latlng2.longitude);
        BigDecimal bd = new BigDecimal(distance);
        BigDecimal res = bd.setScale(100, RoundingMode.DOWN);
        return res.doubleValue();
    }

    public void createDashedLine(GoogleMap mMap, List<LatLng> listOfPoints) {
        DebugLog.e("Dashed Line Call ");
        int color = Color.parseColor("#8f8f8f");
        //int color = Color.parseColor("#B3C4C9");
        boolean added = false;
        for (int i = 0; i < listOfPoints.size() - 1; i++) {
        /* Get distance between current and next point */
            double distance = getConvertedDistance(listOfPoints.get(i), listOfPoints.get(i + 1));

        /* If distance is less than 0.002 kms */
            if (distance < 0.002) {
                if (!added) {
                    mMap.addPolyline(new PolylineOptions()
                            .add(listOfPoints.get(i))
                            .add(listOfPoints.get(i + 1))
                            .color(color));
                    added = true;
                } else {/* Skip this piece */
                    added = false;
                }
            } else {
            /* Get how many divisions to make of this line */
                int countOfDivisions = (int) ((distance / 0.002));

            /* Get difference to add per lat/lng */
                double latdiff = (listOfPoints.get(i + 1).latitude - listOfPoints
                        .get(i).latitude) / countOfDivisions;
                double lngdiff = (listOfPoints.get(i + 1).longitude - listOfPoints
                        .get(i).longitude) / countOfDivisions;

            /* Last known indicates start point of polyline. Initialized to ith point */
                LatLng lastKnowLatLng = new LatLng(listOfPoints.get(i).latitude, listOfPoints.get(i).longitude);
                for (int j = 0; j < countOfDivisions; j++) {

                /* Next point is point + diff */
                    LatLng nextLatLng = new LatLng(lastKnowLatLng.latitude + latdiff, lastKnowLatLng.longitude + lngdiff);
                    if (!added) {
                        mMap.addPolyline(new PolylineOptions()
                                .add(lastKnowLatLng)
                                .add(nextLatLng)
                                .color(color));
                        added = true;
                    } else {
                        added = false;
                    }
                    lastKnowLatLng = nextLatLng;
                }
            }
        }
    }

    /**
     * Call a sms api using twillio.
     */

    public void sendSmsFromTwilio(String phone,String message, String sender_phone, String trip_id)
    {
        AlertUtils.showCustomProgressDialog(getActivity());

        String strToken = user.getToken();

        DebugLog.i("Call SMS");
        ApiService api = RetroClient.getApiService();
        Call<GenericResponse> call = api.doSms(strToken, phone, message, sender_phone, trip_id);
        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, retrofit2.Response<GenericResponse> response) {
                AlertUtils.dismissDialog();
                if (response.isSuccessful()) {
                    DebugLog.i("SMS Send Response Code ::" + response.code());
                    GenericResponse responseBody = response.body();
                    if (response.code() == 201) {
                        showSnackBar(responseBody.getMessage(), null, linearMain);
                    }
                } else {
                    DebugLog.e("SMS Send Failed Response");
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
                AlertUtils.dismissDialog();
                DebugLog.e("SMS Send Request Failed :" + t.getMessage());
            }
        });


    }

    public HashMap<String, String> setTwilioSmsData(String phone,String message, String sender_phone, String trip_id) {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.PHONE, phone);
        param.put(LaoxiConstant.MESSAGE, message);
        param.put(LaoxiConstant.SENDER_PHONE, sender_phone);
        param.put(LaoxiConstant.TRIP_ID, trip_id);
        return param;
    }

    //Get driver updated latlong

    public void wsCallGetDriverDetails(final String driverId) {

        DebugLog.i("Call GetDriver Details Request");
        ApiService api = RetroClient.getApiService();
        String strToken = user.getToken();

        Call<DriverDetails> call = api.doGetDriverDetails(strToken, driverId, null, null);

        call.enqueue(new Callback<DriverDetails>() {
            @Override
            public void onResponse(Call<DriverDetails> call, retrofit2.Response<DriverDetails> response) {
                AlertUtils.dismissDialog();
                if ( response.isSuccessful() ) {
                    DebugLog.e("Driver Details are Success Response : " + response.body().getDeviceId());
                    if ( response.code() == 200 ) {
                        driverDetails = response.body();
                        Driver_LatLng = new LatLng(Double.valueOf(driverDetails.getLatitude()), Double.valueOf(driverDetails.getLongitude()));
                        setupMap(map);
                    }
                } else {
                    DebugLog.e("Driver Details Response Failed");
                    if (response.code() == 400) {
                        wsCallGetDriverDetails(driverId);
                    }
                }
            }

            @Override
            public void onFailure(Call<DriverDetails> call, Throwable t) {
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
