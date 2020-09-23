package com.rider.fragments;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.rider.LAOXI;
import com.rider.LocationHelper.LocationHelperService;
import com.rider.LocationHelper.RouteDisplay;
import com.rider.R;
import com.rider.activity.HomeActivity;
import com.rider.activity.LocationSearchActivityNew;
import com.rider.activity.LoginActivity;
import com.rider.customclasses.CustomEditText;
import com.rider.customclasses.CustomTextView;
import com.rider.dialog.AlertUtils;
import com.rider.dialog.DriverDetailsDialog;
import com.rider.dialog.GeneralStaticAlertDialog;
import com.rider.navigator.NavigatorInterface;
import com.rider.pojo.Cars;
import com.rider.pojo.CartypeList;
import com.rider.pojo.CurrentRide;
import com.rider.pojo.Driverlist;
import com.rider.pojo.GenericResponse;
import com.rider.pojo.NearFreeData;
import com.rider.pojo.OrderData;
import com.rider.pojo.PlaceOrderData;
import com.rider.pojo.User;
import com.rider.retrofit.ApiService;
import com.rider.retrofit.RetroClient;
import com.rider.utils.DataToPref;
import com.rider.utils.DebugLog;
import com.rider.utils.DialogDismissOneButton;
import com.rider.utils.DialogDismissTwoButton;
import com.rider.utils.DialogDismissTwoButtonwithText;
import com.rider.utils.HomePageRefresh;
import com.rider.utils.LaoxiConstant;
import com.rider.utils.MapStateManager;
import com.rider.utils.ParsingHelper;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by on 19/7/16.
 */
public class NewHomeFragment extends BaseFragment implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback, GoogleMap.OnCameraIdleListener, DriverDetailsDialog.CloseDialogToRefresh, HomePageRefresh {

    NavigatorInterface navigatorInterface;
    @Bind(R.id.header_menu)
    ImageView headerMenu;
    @Bind(R.id.header_text)
    CustomTextView headerText;
    @Bind(R.id.img_share)
    ImageView imgShare;
    @Bind(R.id.imageView1)
    ImageView imageView1;
    @Bind(R.id.img_current_location)
    ImageView imgCurrentLocation;
    @Bind(R.id.locationMarker)
    RelativeLayout locationMarker;
    @Bind(R.id.mainFrame)
    FrameLayout mainFrame;
    @Bind(R.id.edtPickupFrom)
    CustomEditText edtPickupFrom;
    @Bind(R.id.edtDropOff)
    CustomEditText edtDropOff;
    @Bind(R.id.relative_main)
    RelativeLayout relativeMain;
    @Bind(R.id.imgCancel)
    ImageView imgCancel;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.imgCarSelection)
    ImageView imgCarSelection;
    @Bind(R.id.linearLayoutRideNow)
    LinearLayout linearLayoutRideNow;
    @Bind(R.id.bottom_relativelayout)
    RelativeLayout bottom_relativelayout;
    @Bind(R.id.header)
    RelativeLayout header;
    @Bind(R.id.relpickup)
    RelativeLayout relpickup;
    @Bind(R.id.reldropoff)
    RelativeLayout reldropoff;
    private GoogleMap map;
    private List<Cars> listOfDrivers;
    private LatLng LAT_LNG = null;
    private Marker myMarker;
    int flagforcamerafocus = 0;

    private ArrayList<Marker> mMarkers;
    private User user;
    //String carType = "Taxi";
    String carType = "1";
    private LatLng center;
    NearFreeData nearFreeData;
    private PlaceOrderData placeOrderData;
    private long mLastClickTime = 0;

    LatLng pickUpLatLng, dropOffLatLng;
    MapStateManager mgr;
    CameraPosition savedCameraPosition;
    private int requestTime;
    private int IsZoome = 0;
    private float currentZoom = 14;
    private int decision;
    public static boolean showTransparentLayout = false;
    private Handler handler;
    boolean display_status = true;
    Marker currentMarker, dropoffMarker;
    LatLng currentLatLong;
    List<Marker> listOfRemovableMarker;

    //Modify
    String regionForNearFreeDriver = "";
    private Runnable myRunnable;
    LinearLayout linearLayoutTransparent;
    String region = "";
    private static final String TAG = "NewHomeFragment";
    private RouteDisplay routedisplay;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigatorInterface = (NavigatorInterface) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_home_layout, container, false);
        ButterKnife.bind(this, view);
        headerText.setText("Where are you?");

        EventBus.getDefault().register(NewHomeFragment.this);

        listOfRemovableMarker = new ArrayList<>();
        String strUserData = DataToPref.getSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA);
        Gson mGson = new Gson();

        user = mGson.fromJson(strUserData, User.class);

        imgShare.setVisibility(View.VISIBLE);
        LAOXI.currentRide = null;
        LAOXI.currentRide = new CurrentRide();

        listOfDrivers = new ArrayList<>();
        mMarkers = new ArrayList<>();
        LAOXI.currentRide.setCarType(carType);

        /**
         * This pop up is display for user first time how ride will placed.
         */

        if (!DataToPref.getSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.IS_FIRST_TIME, LaoxiConstant.IS_FIRST_TIME_DATA).equalsIgnoreCase(""))
            ;
        {
            String Status = DataToPref.getSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.IS_FIRST_TIME, LaoxiConstant.IS_FIRST_TIME_DATA);
            AlertUtils.dismissDialog();
            if (Status.equalsIgnoreCase("true")) {
                GeneralStaticAlertDialog.getInstance().createDialogWithOneButtonYellow(getContext(), "", getString(R.string.first_time_guide), new DialogDismissOneButton() {
                    @Override
                    public void onDismiss() {

                    }
                });
            }
            DataToPref.setSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.IS_FIRST_TIME, LaoxiConstant.IS_FIRST_TIME_DATA, "false");
        }

        if (showTransparentLayout) {
            linearLayoutTransparent = (LinearLayout) view.findViewById(R.id.linearLayoutTransparent);
            linearLayoutTransparent.setVisibility(View.VISIBLE);
            if (DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.STORED_ORDER_FROM, LaoxiConstant.STORED_ORDER_FROM_DATA).equalsIgnoreCase("selectdriver")) {
                startProgressBar();
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navigatorInterface.closeDrawer();
        placeOrderData = new PlaceOrderData();
        String s = FirebaseInstanceId.getInstance().getToken();
        DebugLog.e("Key ------------- " + s);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        // mgr = new MapStateManager(getContext());
        savedCameraPosition = null;

        StrictMode.setThreadPolicy(policy);
        setupMapIfNeeded();

        handler = new Handler();

        myRunnable = new Runnable() {
            public void run() {
                try {
                    if (LAT_LNG != null) {
//                        clearRemovableMarker();
                        wsCallNearFreeDriver(LAT_LNG, region);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                handler.postDelayed(myRunnable, 15000);

            }
        };
        handler.postDelayed(myRunnable, 15000);

    }

    /**
     * Start a progress bar for 25 second.
     */

    public void startProgressBar() {

        if (getActivity() != null) {
            ((HomeActivity) getActivity()).cTimer = new CountDownTimer(30000, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    wsCallCancelOrder(true);
                }
            };
            ((HomeActivity) getActivity()).cTimer.start();
        }
    }

    /**
     * Initialize map first time into container.
     */

    private void setupMapIfNeeded() {
        // if (map == null) {
        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        } catch (Exception e) {
            e.printStackTrace();
            // }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (!DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.USER_DEFAULT_LATITUDE, LaoxiConstant.USER_DEFAULT_LATITUDE_DATA).equalsIgnoreCase("") && !DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.USER_DEFAULT_LONGITUDE, LaoxiConstant.USER_DEFAULT_LONGITUDE_DATA).equalsIgnoreCase("") && map != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(Double.parseDouble(DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.USER_DEFAULT_LATITUDE, LaoxiConstant.USER_DEFAULT_LATITUDE_DATA)), Double.parseDouble(DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.USER_DEFAULT_LONGITUDE, LaoxiConstant.USER_DEFAULT_LONGITUDE_DATA)))).zoom(14).build();

            map.moveCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }

        map.getUiSettings().setZoomControlsEnabled(false);
        map.setOnCameraIdleListener(this);

        LAT_LNG = new LatLng(LocationHelperService.dblLatitude, LocationHelperService.dblLongitude);

        if (LAT_LNG != null) {
            setUpMap();
        }
    }

    /**
     * Set map first time .
     */

    private void setUpMap() {
        try {
            map.setMyLocationEnabled(false);
            MapsInitializer.initialize(getContext());
            map.getUiSettings().setMyLocationButtonEnabled(false);
            map.getUiSettings().setCompassEnabled(true);
            map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));

            currentLatLong = new LatLng(LocationHelperService.dblLatitude, LocationHelperService.dblLongitude);
            DebugLog.i("Current Location : " + currentLatLong);
            getAddressFromLatLong(currentLatLong);

            if (currentMarker != null) {
                currentMarker.remove();
            }
            currentMarker = map.addMarker(new MarkerOptions()
                    .position(currentLatLong)
                    .anchor(0.5f, 0.7f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin_new)));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(currentLatLong).zoom(14).build();

            map.moveCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            //}
            map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition pos) {

                    try {
                        if (IsZoome == 0) {
                            IsZoome = 1;
                            if (pos.zoom == currentZoom) {
                                currentZoom = pos.zoom;
                            } else {
                                currentZoom = pos.zoom;
                                if (center != null)
                                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, currentZoom));
                            }
                        } else {
                            IsZoome = 0;
                        }

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
            });


            /**
             * Get a callback when click on car icon on map and open a driver detail of particular car.
             */

            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    DebugLog.e("driver id is a" + marker.getSnippet());
                    if (LAOXI.currentRide.getPickupAddress() == null) {
                        showSnackBar("Please provide pick up address", null, relativeMain);
                    } else if (LAOXI.currentRide.getDropOffAddress() == null) {
                        showSnackBar("Please provide drop off location", null, relativeMain);
                    } else {
                        if (nearFreeData != null) {
                            for (int i = 0; i < nearFreeData.getDriverlist().size(); i++) {
                                if (nearFreeData.getDriverlist().get(i).getId().equalsIgnoreCase(marker.getSnippet())) {
                                    LAOXI.currentRide.setTripType("now");
                                    DataToPref.setSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.STORED_ORDER_FROM, LaoxiConstant.STORED_ORDER_FROM_DATA, "selectdriver");
                                    navigatorInterface.openDriverDetails(nearFreeData.getCartypeList().getId().toString(), nearFreeData.getDriverlist().get(i).getId(), nearFreeData.getDriverlist().get(i).getEta().toString());

                                    DriverDetailsDialog driverDetailsDialog = new DriverDetailsDialog();
                                    driverDetailsDialog.setContext(getContext());
                                    driverDetailsDialog.setCloseDialogToRefresh(NewHomeFragment.this);
                                    driverDetailsDialog.setCarTypeId(nearFreeData.getCartypeList().getId().toString());
                                    driverDetailsDialog.setDriverId(nearFreeData.getDriverlist().get(i).getId());
                                    driverDetailsDialog.setETA(nearFreeData.getDriverlist().get(i).getEta().toString());
                                    driverDetailsDialog.show(getActivity().getFragmentManager(), driverDetailsDialog.getClass().getName());

                                }
                            }
                        }
                    }
                    return true;
                }
            });

            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Handler handler = new Handler();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (display_status) {
                                yoyoAnimation(bottom_relativelayout, 200, Techniques.SlideOutDown);
                                yoyoAnimation(relpickup, 200, Techniques.SlideOutUp);
                                yoyoAnimation(reldropoff, 200, Techniques.SlideOutUp);
                                display_status = false;

                            } else {
                                yoyoAnimation(bottom_relativelayout, 200, Techniques.SlideInUp);
                                yoyoAnimation(relpickup, 200, Techniques.SlideInDown);
                                yoyoAnimation(reldropoff, 200, Techniques.SlideInDown);
                                display_status = true;
                            }
                        }
                    }, 200);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void yoyoAnimation(final View target, long time, Techniques tada) {
        YoYo.with(tada)
                .duration(time)
                /*.onStart(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        target.setVisibility(visible_type);
                    }
                })*/
                .playOn(target);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LAT_LNG != null) {
            wsCallNearFreeDriver(LAT_LNG, region);
        }
/**
 *  added by rhc
 */
        if (map != null)
            map.clear();
        /**
         * Display selected dropoff marker and draw route between two points, fit zoom level
         * added by rhc 20180620
         */
        if (dropoffMarker != null) {
            dropoffMarker.remove();
        }

        if (dropOffLatLng != null) {
            if (LAT_LNG == null) LAT_LNG = currentLatLong;
            dropoffMarker = map.addMarker(new MarkerOptions()
                    .position(dropOffLatLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            routedisplay = new RouteDisplay();
            routedisplay.RouteFromGoogle(map, LAT_LNG, dropOffLatLng);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(LAT_LNG);
            builder.include(dropOffLatLng);
            LatLngBounds bounds = builder.build();

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
            map.animateCamera(cu, new GoogleMap.CancelableCallback() {
                @Override
                public void onCancel() {

                }

                @Override
                public void onFinish() {
                    CameraUpdate zout = CameraUpdateFactory.zoomBy(-1);
                    map.animateCamera(zout);
                }
            });
            /* Drawing route from simplyfied algorithm test */
            /*List<LatLng> polylinepoints = new ArrayList<LatLng>();
            double pointY[]={105.45526,105.57364,105.53505,105.45523,105.51962,105.77320};
            double pointX[]={9.99222,9.88347,9.84184,9.77197,9.55501,9.67768,9};
            for (int i = 0 ; i < pointX.length-1; i++){
                polylinepoints.add(new LatLng(pointX[i],pointY[i]));
            }
            routedisplay = new RouteDisplay();
            routedisplay.RouteFromTrace(map , polylinepoints);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(9.99222, 105.45526), 10));*/
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(NewHomeFragment.this);
        if (handler != null) {
            handler.removeCallbacks(myRunnable);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.header_menu)
    public void onClick() {
        HomeActivity.toggleDrawer();
    }

    @OnClick({R.id.linearLayoutRideNow, R.id.edtPickupFrom, R.id.edtDropOff, R.id.img_share, R.id.img_current_location, R.id.imgCancel, R.id.imgCarSelection})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.imgCancel:
                GeneralStaticAlertDialog.getInstance().createDialogWithTwoButton(getActivity(), "You wanna cancel trip?", new DialogDismissTwoButton() {
                    @Override
                    public void onOkClick() {
                        wsCallCancelOrder(false);
                    }

                    @Override
                    public void onCancelClick() {

                    }
                });
                break;

            case R.id.linearLayoutRideNow:

                if (LAOXI.currentRide.getPickupAddress() == null) {
                    showSnackBar("Please provide pick up address", null, relativeMain);
                } else if (LAOXI.currentRide.getDropOffAddress() == null) {
                    showSnackBar("Please provide drop off location", null, relativeMain);
                } else if (LAOXI.currentRide.getCarTypeId() == null) {
//                    showSnackBar("Sorry, no drivers currently available", null, relativeMain);
                    GeneralStaticAlertDialog.getInstance().createDialogWithOneButton(getActivity(), "", "Sorry, no drivers currently available", new DialogDismissOneButton() {
                        @Override
                        public void onDismiss() {
                            if (getView() != null) {
                                linearLayoutTransparent = (LinearLayout) getView().findViewById(R.id.linearLayoutTransparent);
                                linearLayoutTransparent.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                } else {
//                    if (LAOXI.currentRide.getCarTypeId() != null) {
                    LAOXI.currentRide.setTripType("now");
                    DataToPref.setSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.STORED_ORDER_FROM, LaoxiConstant.STORED_ORDER_FROM_DATA, "ridenow");

                    GeneralStaticAlertDialog.getInstance().createDialogConfirmRideNow(getContext(), new DialogDismissTwoButtonwithText() {
                        @Override
                        public void onOkClick(String s) {
                            if (s.equalsIgnoreCase("Ride Now")) {
                                wsCallPlaceOrder();
                            } else if (s.equalsIgnoreCase("Ride Later")) {
                                wsCallCarListForRideLater(new LatLng(LAOXI.currentRide.getPickLatitude(), LAOXI.currentRide.getPickLongitude()), carType);
                            } else if (s.equalsIgnoreCase("Fare estimation")) {
                                navigatorInterface.openFareEstimation(null, LAOXI.currentRide.getCarTypeId(), null);
                            }
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    });
//                    } else {
//                        showSnackBar("All drivers are busy right now, Please try again soon", null, relativeMain);
//                    }
                }

                break;
            case R.id.edtPickupFrom:
                LaoxiConstant.ISFROMSEARCH = true;
                getAddress(1);
                //findPlacePickup(edtPickupFrom);
                break;
            case R.id.edtDropOff:
                //findPlaceDropOff(edtDropOff);
                getAddress(2);
                break;
            case R.id.img_share:
                openInfoScreen();
                break;
            case R.id.img_current_location:
                currentLatLong = new LatLng(LocationHelperService.dblLatitude, LocationHelperService.dblLongitude);
                LAT_LNG = currentLatLong;
                if (currentLatLong != null && map != null) {
                    LaoxiConstant.ISFROMSEARCH = false;
                    getAddressFromLatLong(currentLatLong);
                    currentZoom = 14;
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(currentLatLong).zoom(14).build();

                    map.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                }


                break;

            case R.id.imgCarSelection:
                GeneralStaticAlertDialog.getInstance().createDialogForCarSelections(getContext(), new DialogDismissTwoButtonwithText() {
                    @Override
                    public void onOkClick(String s) {
                        DebugLog.e("car is a==" + s);
                        carType = s;
                        LAOXI.currentRide.setCarType(carType);
                        if (s.equalsIgnoreCase("1")) {
                            imgCarSelection.setImageResource(R.drawable.taxi_popup);
                        } else if (s.equalsIgnoreCase("2")) {
                            imgCarSelection.setImageResource(R.drawable.bike_popup);
                        } else if (s.equalsIgnoreCase("3")) {
                            imgCarSelection.setImageResource(R.drawable.tuktuk_popup);
                        } else {
                            imgCarSelection.setImageResource(R.drawable.owner_taxi);
                        }
                        LAOXI.currentRide.setCarTypeId(null);

                        if (LAT_LNG != null) {
                            clearRemovableMarker();
                            wsCallNearFreeDriver(LAT_LNG, region);
                        }
                    }

                    @Override
                    public void onCancelClick() {

                    }
                });
                break;

        }
    }

    /**
     * Information pop up open when click on info icon.
     */
    private void openInfoScreen() {
        String msg = "Hi there!\nTouch any car on the map to see the driver, driver ratings, and vehicle.  When you’re ready press “Let’s go!”.  Not fussed? Just press “Ride now”";
        GeneralStaticAlertDialog.getInstance().openTutorialDialog(getContext(), msg, new DialogDismissOneButton() {
            @Override
            public void onDismiss() {

            }
        });
    }


    public void getAddress(int i) {
        decision = i;
        Intent intent = new Intent(getActivity(), LocationSearchActivityNew.class);
        startActivityForResult(intent, 111);
    }


    /**
     * Get a callback when user select or enter a pickup and dropoff address.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            try {
                String result = data.getStringExtra("result");
                Log.d(TAG, "LocationSearchResult-->" + result);
                String[] LocationData = result.split(",,,");

                if (decision == 1) {//get pickup Latlng

                    if (LocationData[0] != null && LocationData[1] != null) {


                        pickUpLatLng = new LatLng(Double.parseDouble(LocationData[0]), Double.parseDouble(LocationData[1]));
                        LAT_LNG = pickUpLatLng;

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(pickUpLatLng).zoom(14).build();

                        map.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));
                        //DebugLog.e("Tag " + " Place: " + place.getAddress() + place.getPhoneNumber());

                        if (LocationData[3] == null || LocationData[3].equalsIgnoreCase("null")) {
                            region = "";
                            LAOXI.currentRide.setRegion(region);
                        } else {
                            region = LocationData[3];
                            LAOXI.currentRide.setRegion(region);
                        }

                        wsCallNearFreeDriver(pickUpLatLng, region);
                        edtPickupFrom.setText(LocationData[7]);
                        edtPickupFrom.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_sign, 0);
                        LAOXI.currentRide.setPickLatitude(Double.parseDouble(LocationData[0]));
                        LAOXI.currentRide.setPickLongitude(Double.parseDouble(LocationData[1]));
                        LAOXI.currentRide.setPickupAddress(LocationData[7]);
                    }
                } else {//get dropoff latlng
                    dropOffLatLng = new LatLng(Double.parseDouble(LocationData[0]), Double.parseDouble(LocationData[1]));
                    LAOXI.currentRide.setDropOffAddress(LocationData[7]);
                    edtDropOff.setText(LocationData[7]);
                    edtDropOff.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_sign, 0);
                    DecimalFormat f = new DecimalFormat("##.000000");
                    LAOXI.currentRide.setDropOffLatitude(Double.parseDouble(f.format(Double.parseDouble(LocationData[0]))));
                    LAOXI.currentRide.setDropOffLongitude(Double.parseDouble(f.format(Double.parseDouble(LocationData[1]))));
                    DataToPref.setSharedPreferanceData(getActivity(), LaoxiConstant.IS_PROVIDE_DROPOFF, LaoxiConstant.IS_PROVIDE_DROPOFF_DATA, "true");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * Selector of laoxi car and other is deselect and call near free api of laoxi car.
     */


    /**
     * Event fired when current location is change .
     */


    public void onEvent(final Location location) {
        currentLatLong = new LatLng(location.getLatitude(), location.getLongitude());
        DebugLog.i("Current Location : " + currentLatLong);
    }

    /**
     * Event fired when push is coming.
     */

    public void onEvent(User user) {
        if (getView() != null) {
            linearLayoutTransparent = (LinearLayout) getView().findViewById(R.id.linearLayoutTransparent);
            linearLayoutTransparent.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        DebugLog.e("" + marker.getPosition().latitude);

        return false;
    }

    public void prepareList() {

        for (int i = 0; i < 5; i++) {
            Cars cars = new Cars();
            cars.setEta("5");
            cars.setCarId(i);
            cars.setDistance(5);
            LatLng latLng = getLocation(LAT_LNG.longitude, LAT_LNG.latitude, 1000);
            cars.setLatitude(latLng.latitude);
            cars.setLongitude(latLng.longitude);
            listOfDrivers.add(cars);
        }
    }

    public LatLng getLocation(double x0, double y0, int radius) {
        Random random = new Random();

        // Convert radius from meters to degrees
        double radiusInDegrees = radius / 111000f;

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos(y0);

        double foundLongitude = new_x + x0;
        double foundLatitude = y + y0;
//        System.out.println("Longitude: " + foundLongitude + "  Latitude: " + foundLatitude );

        LatLng latLng = new LatLng(foundLatitude, foundLongitude);
        return latLng;
    }

    private void removeMarkers() {
        for (Marker marker : mMarkers) {
            marker.remove();
        }
        listOfDrivers.clear();
        mMarkers.clear();
        map.clear();
    }

    public void findPlacePickup(View view) {
        try {
            Intent intent = new PlaceAutocomplete
                    .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(getActivity());

            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    public void findPlaceDropOff(View view) {
        try {
            Intent intent = new PlaceAutocomplete
                    .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(getActivity());
            startActivityForResult(intent, 2);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    /**
     * Get a address of current location.
     */
    public void getAddressFromLatLong(LatLng latLng) {
        if (!LaoxiConstant.ISFROMSEARCH && latLng.latitude != 0.0 && latLng.longitude != 0.0) {
            List<Address> addresses = new ArrayList<>();
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            // DebugLog.e(addresses.toString());
            if (!addresses.isEmpty()) {
                String area = null;

                Address location = addresses.get(0);

                if (addresses.get(0).getAddressLine(0) != null) {
                    area = addresses.get(0).getAddressLine(0);
                    if (addresses.get(0).getAddressLine(1) != null) {
                        area = area + " " + addresses.get(0).getAddressLine(1);
                    }
                }
                //region = location.getAdminArea();
                region = location.getLocality();
                Log.e(TAG, "region-->" + location);
                LAOXI.currentRide.setRegion(region);
                edtPickupFrom.setText(area);
                edtPickupFrom.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_sign, 0);
                LAOXI.currentRide.setPickupAddress(area);
                LAOXI.currentRide.setPickLatitude(latLng.latitude);
                LAOXI.currentRide.setPickLongitude(latLng.longitude);
            }
        }
        LaoxiConstant.ISFROMSEARCH = false;
        wsCallNearFreeDriver(latLng, region);
    }

    /**
     * Share app details on social app.
     */

    private void doShare() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.APP_URL));
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }


    /**
     * Call a near free driver api and get a response in json string.
     */
    Call<NearFreeData> callNearFreeDriver;

    public void wsCallNearFreeDriver(final LatLng latLng, String region) {

        if (latLng.longitude != 0.0 && latLng.latitude != 0.0) {
            requestTime = (int) (System.currentTimeMillis());

            String strToken = user.getToken();
            String strUserId = user.getId();
            DebugLog.i("Call NearFree Driver");
            ApiService api = RetroClient.getApiService();
            Log.d(TAG, "token-->" + strToken);
            callNearFreeDriver = api.doNearFreeDriverNew(strToken, strUserId, String.valueOf(latLng.latitude), String.valueOf(latLng.longitude), carType, region);
            callNearFreeDriver.enqueue(new Callback<NearFreeData>() {
                @Override
                public void onResponse(Call<NearFreeData> call, retrofit2.Response<NearFreeData> response) {
                    if (response.isSuccessful()) {
                        if (response.code() == 200) {

                            DebugLog.i("NearFree Driver Success Response :" + response.body().getDriverlist().size());
                            nearFreeData = response.body();
                            drawPinList(nearFreeData, LAT_LNG);
                            LAOXI.currentRide.setCarTypeId(nearFreeData.getCartypeList().getId());
                        }
                    } else {
                        try {
                            String s = response.errorBody().string();
                            GenericResponse genericResponse = ParsingHelper.getInstance().genericResponseParsing(s);
                            DebugLog.e("Driver Find Failed Code :" + response.code() + ", Message :" + genericResponse.getMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (response.code() == 401) {
//                            DataToPref.setSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA, "");
                            DataToPref.setSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.ISLOGIN, "false");
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            if (LAOXI.currentActivity instanceof HomeActivity) {
                                LAOXI.currentActivity.startActivity(intent);
                                LAOXI.currentActivity.finish();
                            }
                        } else {
                            if (map != null) {
                                //map.clear();
                                clearRemovableMarker();// added by rhc
                                if (currentMarker != null) {
                                    currentMarker.remove();
                                }

                                LAOXI.currentRide.setCarTypeId(null);
                                currentMarker = map.addMarker(new MarkerOptions()
                                        .position(LAT_LNG)
                                        .anchor(0.5f, 0.7f)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin_new)));

                                /*CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(LAT_LNG).zoom(14).build();

                                map.moveCamera(CameraUpdateFactory
                                        .newCameraPosition(cameraPosition));*/
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<NearFreeData> call, Throwable t) {
                    DebugLog.e("NearFree Request Failed");
                }
            });

        }
    }


    /**
     * Set a required parameter of call near free driver api.
     */

    public HashMap<String, String> setNearFreeDriverdata(LatLng latLng, String region) {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.USER_ID, user.getId());
        param.put(LaoxiConstant.LATITUDE, String.valueOf(latLng.latitude));
        param.put(LaoxiConstant.LONGITUDE, String.valueOf(latLng.longitude));
        param.put(LaoxiConstant.CAR_TYPE, carType);
        param.put(LaoxiConstant.REGION, region);
        return param;
    }


    /**
     * Draw a near free driver list pin on map in home page.
     */

    public void drawPinList(NearFreeData nearFreeData, LatLng currentLatLong) {
        if (map != null) {
            clearRemovableMarker();// added by rhc
            if (currentMarker != null) {
                currentMarker.remove();
            }
            currentMarker = map.addMarker(new MarkerOptions()
                    .position(currentLatLong)
                    .anchor(0.5f, 0.7f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin_new)));

            /*CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(currentLatLong).zoom(14).build();

            map.moveCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            */

            List<Driverlist> driverList = nearFreeData.getDriverlist();

            for (int i = 0; i < driverList.size(); i++) {

                String str = driverList.get(i).getCarType();
                String arrayString[] = str.split(",");
                for (String driverCarType : arrayString) {
                    if (driverCarType.equalsIgnoreCase(carType) || driverList.get(i).isFavouriteDriver()) {

                        LatLng latLng = new LatLng(Double.parseDouble(driverList.get(i).getLatitude()), Double.parseDouble(driverList.get(i).getLongitude()));

                        Location targetLocation = new Location("");//provider name is unecessary
                        targetLocation.setLatitude(latLng.latitude);//your coords of course
                        targetLocation.setLongitude(latLng.longitude);
                        BitmapDescriptor bitmapMarker = null;

                        if (driverList.get(i).isFavouriteDriver()) {
                            bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.star_on_map);
                        } else if (driverList.get(i).getGender().equalsIgnoreCase("male")) {
                            if (nearFreeData.getCartypeList().getCarType().equalsIgnoreCase(LaoxiConstant.TAXI)) {
                                bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.taxi_icon);
                            } else if (nearFreeData.getCartypeList().getCarType().equalsIgnoreCase(LaoxiConstant.BIKE)) {
                                bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.bike_icon);
                            } else if (nearFreeData.getCartypeList().getCarType().equalsIgnoreCase(LaoxiConstant.TUKTUK)) {
                                bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.tuktuk_icon);
                            } else {
                                bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.owner_icon);
                            }

                        }

                        if (bitmapMarker != null) {
                            Marker cMarker = map.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .snippet(driverList.get(i).getId())
                                    .icon(bitmapMarker));

                            listOfRemovableMarker.add(cMarker);
                        }
                    }
                }
            }
        }
    }


    @Override
    public void onCameraIdle() {
/*
        center = map.getCameraPosition().target;
        pickUpLatLng = center;
//        getAddressFromLatLong(map.getCameraPosition().target);
        wsCallNearFreeDriver(map.getCameraPosition().target);*/
    }


    public void wsCallCarListForRideLater(LatLng latLng, String cartypes) {
        if (latLng.longitude != 0.0 && latLng.latitude != 0.0) {
            AlertUtils.showCustomProgressDialog(getActivity());

            String strToken = user.getToken();

            String strLat = String.valueOf(latLng.latitude);
            String strLng = String.valueOf(latLng.longitude);
            String strCarType = "";
            String strRegion = "";
            if (LAOXI.currentRide.getCarType() != null) {
                strCarType = LAOXI.currentRide.getCarType();
            }
            if (LAOXI.currentRide.getRegion() != null) {
                strRegion = LAOXI.currentRide.getRegion();
            }

            DebugLog.i("Call Ride Later");

            ApiService api = RetroClient.getApiService();
            Call<CartypeList> call = api.doCarListLater(strToken, strLat, strLng, strCarType, strRegion);

            call.enqueue(new Callback<CartypeList>() {
                @Override
                public void onResponse(Call<CartypeList> call, retrofit2.Response<CartypeList> response) {
                    AlertUtils.dismissDialog();

                    if (response.isSuccessful()) {
                        if (response.code() == 200) {
                            CartypeList cartypeList = response.body();
                            DebugLog.i("RideLater Success Response :" + cartypeList.getCarType());
                            LAOXI.currentRide.setCarTypeId(cartypeList.getId());
                            LAOXI.currentRide.setTripType("later");
                            navigatorInterface.openRideLater(NewHomeFragment.this);

                        }
                    } else {
                        DebugLog.e("RideLater Response Failed");
                        if (response.code() == 400) {
                            try {
                                String s = response.errorBody().string();
                                GenericResponse genericResponse = ParsingHelper.getInstance().genericResponseParsing(s);
                                showSnackBar(genericResponse.getMessage(), null, relativeMain);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<CartypeList> call, Throwable t) {
                    AlertUtils.dismissDialog();
                    DebugLog.e("RideLater Request Failed :" + t.getMessage());
                }
            });

        }
    }

    public HashMap<String, String> setCarListdata(LatLng latLng, String cartypes) {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.PICKUP_LATITUDE, String.valueOf(latLng.latitude));
        param.put(LaoxiConstant.PICKUP_LONGITUDE, String.valueOf(latLng.longitude));
        param.put(LaoxiConstant.CAR_TYPE, cartypes);
        param.put(LaoxiConstant.REGION, region);
        return param;
    }


    /**
     * Set a parameter for call cancel order api.
     */

    public HashMap<String, String> setCancelOrderData() {
        HashMap<String, String> param = new HashMap<>();
        Gson mGson = new Gson();
        if (getActivity() != null) {
            String strOrderData = DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.ORDER, LaoxiConstant.ORDER_DATA);
            OrderData orderData = mGson.fromJson(strOrderData, OrderData.class);

            param.put(LaoxiConstant.ID, user.getId());
            param.put(LaoxiConstant.USER_TYPE, "user");
            param.put(LaoxiConstant.ORDER_ID, orderData.getId());

            if (user.getCardInfo().size() > 0) {
                param.put(LaoxiConstant.CARD_TOKEN, user.getCardInfo().get(0).getToken());
            }

        }


        return param;
    }


    /**
     * Call a cancel order api.
     */

    public void wsCallCancelOrder(final boolean showMessage) {

        AlertUtils.showCustomProgressDialog(getContext());
        if (getActivity() != null && ((HomeActivity) getActivity()).cTimer != null) {
            ((HomeActivity) getActivity()).cTimer.cancel();
        }
        String strToken = user.getToken();
        String strUserId = user.getId();

        Gson mGson = new Gson();
        String strOrderData = DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.ORDER, LaoxiConstant.ORDER_DATA);
        OrderData orderData = mGson.fromJson(strOrderData, OrderData.class);


        String strCardToken = "";
        if (user.getCardInfo().size() > 0) {
            strCardToken = user.getCardInfo().get(0).getToken();
        }
        DebugLog.i("Call CancelOrder Request");
        ApiService api = RetroClient.getApiService();
        Call<GenericResponse> call = api.doCancelOrder(strToken, strUserId, "user", orderData.getId(), strCardToken);

        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, retrofit2.Response<GenericResponse> response) {
                AlertUtils.dismissDialog();
                if (response.isSuccessful()) {
                    GenericResponse data = response.body();

                    DebugLog.i("Cancel Response Success " + data.getMessage());

                    if (response.code() == 200) {
                        DataToPref.setSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.IS_PROVIDE_DROPOFF, LaoxiConstant.IS_PROVIDE_DROPOFF_DATA, "false");
                        showTransparentLayout = false;
                        if (showMessage) {
                            GeneralStaticAlertDialog.getInstance().createDialogWithOneButton(getActivity(), "", getString(R.string.driver_not_response), new DialogDismissOneButton() {
                                @Override
                                public void onDismiss() {
                                    if (getView() != null) {
                                        linearLayoutTransparent = (LinearLayout) getView().findViewById(R.id.linearLayoutTransparent);
                                        linearLayoutTransparent.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                        } else {
                            GeneralStaticAlertDialog.getInstance().createDialogWithOneButton(getActivity(), "", getString(R.string.trip_cancelled), new DialogDismissOneButton() {
                                @Override
                                public void onDismiss() {
                                    if (getView() != null) {
                                        linearLayoutTransparent = (LinearLayout) getView().findViewById(R.id.linearLayoutTransparent);
                                        linearLayoutTransparent.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });

                        }
                    }
                } else {
                    String s = null;
                    try {
                        s = response.errorBody().string();
                        GenericResponse genericResponse = ParsingHelper.getInstance().genericResponseParsing(s);

                        DebugLog.e("Cancel Response Failed " + genericResponse.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (response.code() == 400) {
                        DataToPref.setSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.IS_PROVIDE_DROPOFF, LaoxiConstant.IS_PROVIDE_DROPOFF_DATA, "false");
                        showTransparentLayout = false;
                        if (getView() != null) {
                            linearLayoutTransparent = (LinearLayout) getView().findViewById(R.id.linearLayoutTransparent);
                            linearLayoutTransparent.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                AlertUtils.dismissDialog();
            }
        });

    }


    /**
     * Remove a near free driver car icon from map.
     */

    private void clearRemovableMarker() {
        if (listOfRemovableMarker != null) {
            for (int i = 0; i < listOfRemovableMarker.size(); i++) {
                listOfRemovableMarker.get(i).remove();
            }
        }
    }

    @Override
    public void callRefreshMapAndDismissDialog() {
        try {
            if (LAT_LNG != null) {
                clearRemovableMarker();
                wsCallNearFreeDriver(LAT_LNG, region);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refreshPage() {
        try {
            if (LAT_LNG != null) {
                clearRemovableMarker();
                wsCallNearFreeDriver(LAT_LNG, region);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Place order for ride now functionality with out passing driver id


    /**
     * Call a place order api.
     */
    Call<OrderData> callPlaceOrder;

    public void wsCallPlaceOrder() {
        AlertUtils.showCustomProgressDialog(getActivity());

        ApiService api = RetroClient.getApiService();

        String strToken = user.getToken();
        String strUserId = user.getId();
        String strCarTypeId = LAOXI.currentRide.getCarTypeId();
        String strDriverId = "";

        String strPickAddress = LAOXI.currentRide.getPickupAddress();
        String strPickLat = String.valueOf(LAOXI.currentRide.getPickLatitude());
        String strPickLng = String.valueOf(LAOXI.currentRide.getPickLongitude());
        String strDropAddress = "";
        String strDropLat = "";
        String strDropLng = "";
        if (LAOXI.currentRide.getDropOffAddress() != null) {
            strDropAddress = LAOXI.currentRide.getDropOffAddress();
            strDropLat = String.valueOf(LAOXI.currentRide.getDropOffLatitude());
            strDropLng = String.valueOf(LAOXI.currentRide.getDropOffLongitude());
        }
        String strTripType = LAOXI.currentRide.getTripType();
        String strTripTime = "";
        String strGender = "";
        DebugLog.i("Call DoPlaceOrder");

        callPlaceOrder = api.doPlaceOrder(strToken, strUserId, strCarTypeId, strDriverId, strPickAddress, strPickLat, strPickLng, strDropAddress, strDropLat, strDropLng, strTripType, strTripTime, strGender);

        callPlaceOrder.enqueue(new Callback<OrderData>() {
            @Override
            public void onResponse(Call<OrderData> call, retrofit2.Response<OrderData> response) {
                AlertUtils.dismissDialog();
                if (response.isSuccessful()) {
                    DebugLog.i("PlaceOrder Success Response");
                    if (response.code() == 201) {
                        OrderData orderData = response.body();
                        String data = new Gson().toJson(orderData);
                        DataToPref.setSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.ORDER, LaoxiConstant.ORDER_DATA, data);
                        //showTransparentLayout = true;
                        linearLayoutTransparent = (LinearLayout) getView().findViewById(R.id.linearLayoutTransparent);
                        linearLayoutTransparent.setVisibility(View.VISIBLE);
                        startProgressBar();
                        //navigatorInterface.openHome()
                    }
                } else {
                    DebugLog.e("PlaceOrder Response Failed");
                    if (response.code() == 400) {
                        String s = null;
                        try {

                            s = response.errorBody().string();
                            GenericResponse genericResponse = ParsingHelper.getInstance().genericResponseParsing(s);

                            if (genericResponse.getMessage().equalsIgnoreCase("Please enter card information on your account")) {
                                navigatorInterface.openPaymentDetails();
                            } else {
                                GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(getContext(), genericResponse.getMessage(), new DialogDismissOneButton() {
                                    @Override
                                    public void onDismiss() {
                                    }
                                });
                            }

                            DebugLog.e("Order Response Failed Message :" + genericResponse.getMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderData> call, Throwable t) {
                DebugLog.e("PlaceOrder Request Failed Msg" + t.getMessage());
                AlertUtils.dismissDialog();
            }
        });
    }

}// end of main class

