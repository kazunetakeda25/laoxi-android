package com.driver.fragments;

import android.animation.Animator;
import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.driver.pojo.CartypeList;
import com.github.siyamed.shapeimageview.ImageViewSlider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.driver.R;
import com.driver.activity.HomeActivity;
import com.driver.customclasses.CustomTextView;
import com.driver.dao.IAsyncCompleteCallback;
import com.driver.dao.ParsingHelper;
import com.driver.dao.homedao.HomeActivityDAO;
import com.driver.dialog.GeneralStaticAlertDialog;
import com.driver.navigator.NavigatorInterface;
import com.driver.pojo.GenericResponse;
import com.driver.pojo.TrackRidePojo.TrackRide;
import com.driver.pojo.loginws.LoginResponse;
import com.driver.retrofit.APIRequestManager;
import com.driver.retrofit.ApiService;
import com.driver.retrofit.RetroClient;
import com.driver.util.AlertUtils;
import com.driver.util.AppHelper;
import com.driver.util.ConstantClass;
import com.driver.util.DebugLog;
import com.driver.util.DialogDismissOneButton;
import com.driver.util.LocationHelperService;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import okhttp3.Response;
import retrofit2.Call;

/**
 * Created by hitesh on 20/7/16.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, OnMapReadyCallback {

    @Bind(R.id.header_menu)
    ImageView headerMenu;
    @Bind(R.id.header_text)
    CustomTextView headerText;
    @Bind(R.id.relative_main)
    RelativeLayout relativeMain;
    @Bind(R.id.toggleService)
    ToggleButton toggleService;
    @Bind(R.id.profileBG)
    ImageViewSlider profileBG;
    @Bind(R.id.textViewName)
    CustomTextView textViewName;
    @Bind(R.id.relativelayoutDetails)
    LinearLayout relativelayoutDetails;
    @Bind(R.id.img_current_location)
    ImageView img_current_location;
    @Bind(R.id.header) //added by rhc
    RelativeLayout header;
    @Bind(R.id.map_container)
    RelativeLayout map_container;
    private GoogleMap map;
    private LatLng LAT_LNG;
    NavigatorInterface navigatorInterface;
    LoginResponse loginData;
    private HomeActivity homeActivity;
    private Marker currentPin;
    private View view;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigatorInterface = (NavigatorInterface) context;
        navigatorInterface.closeDrawer();
        if (context instanceof HomeActivity) {
            homeActivity = (HomeActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.home_layout, container, false);
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.home_layout, container, false);
        } catch (InflateException e) {
            DebugLog.e("exception is a"+e.toString());
        }
        ButterKnife.bind(this, view);
        headerText.setText(R.string.my_location);
        toggleService.setOnClickListener(this);
        headerMenu.setOnClickListener(this);
        img_current_location.setOnClickListener(this);
        EventBus.getDefault().register(this);
        LAT_LNG = new LatLng(LocationHelperService.dblLatitude, LocationHelperService.dblLongitude);
        return view;
    }

    //c-UW_MlHbWo:APA91bHAZ0xw0pH58J_9zTVq29espg6y6R9nn3uA4aU1KIsDikVdfirZRaY17sE7HtiQ2ljArVbkW5AMzyzbhn_9nrfAJcNqkIg-CunnQtRUrYwxpU5XVitWZV5bhw17CVOVcko1_kF0
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DebugLog.i("Key -------- " + FirebaseInstanceId.getInstance().getToken());
        /*mapFragment = new SupportMapFragment();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.map, mapFragment);
        fragmentTransaction.addToBackStack(SupportMapFragment.class.getName());
        fragmentTransaction.commit();*/
        setupMapIfNeeded();
    }

    /**
     * Load a map into container.
     */

    private void setupMapIfNeeded() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (map == null) {
            try {
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loginData = AppHelper.getInstance().getLoginUser();
        textViewName.setText(loginData.getFname());
        Picasso.with(getContext()).load(ConstantClass.THUMB_IMAGE_URL + loginData.getProfileImage()).placeholder(R.drawable.avatar_icon).fit().into(profileBG);
        HashMap<String, String> profileHashMap = new HashMap<>();
        profileHashMap.put(ConstantClass.DRIVER_ID, loginData.getDriverId());

        if (loginData.getIsService().equalsIgnoreCase("0")) {
            toggleService.setChecked(false);
        } else {
            toggleService.setChecked(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Get a call back when location changed.
     */

    public void onEvent(Location location) {
        LAT_LNG = new LatLng(location.getLatitude(), location.getLongitude());
        loginData = AppHelper.getInstance().getLoginUser();
        if (LAT_LNG != null && map != null) {
            if (currentPin != null) {
                currentPin.remove();
            }
            setUpMap();
            BitmapDescriptor bitmapMarker = null;
            String carType= null;
            //String car_image_name = null;
            try {
                carType = loginData.getCarType();
                //CartypeList cartypedata = loginData.getCartypedata();
                //car_image_name = cartypedata.getCarTypeImage();
                //String arrayString[] = car_image_name.split(",");
                //carType = arrayString[arrayString.length-1];

            } catch (Exception e) {
                e.printStackTrace();
            }

            if(carType!=null)
           {
//               if (loginData.getGender().equalsIgnoreCase("male")) {

                   if (carType.equalsIgnoreCase(ConstantClass.TAXI)) {
                       bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.taxi_icon);
                   } else if (carType.equalsIgnoreCase(ConstantClass.BIKE)) {
                       bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.bike_icon);
                   } else if (carType.equalsIgnoreCase(ConstantClass.TUKTUK)) {
                       bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.tuktuk_icon);
                   } else {
                       bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.owner_icon);
                   }

/*               } else {
                   if (carType.equalsIgnoreCase(ConstantClass.TAXI)) {
                       bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.pink_oscar_icon);
                   } else if (carType.equalsIgnoreCase(ConstantClass.BIKE)) {
                       bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.pink_big_oscar_icon);
                   } else if (carType.equalsIgnoreCase(ConstantClass.TUKTUK)) {
                       bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.pink_fancy_oscar_icon);
                   }
               }*/
           }
            /*if(car_image_name != null) {
                Picasso.with(getContext()).load(ConstantClass.CAR_IMAGE_ICON + car_image_name).fetch();
            }*/
            currentPin = map.addMarker(new MarkerOptions()
                    .position(LAT_LNG)
                    .icon(bitmapMarker));

/*            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(LAT_LNG).zoom(14).build();

            map.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));*/
        }

    }

    //Get callback from updateprofile

    public void onEvent(LoginResponse loginResponse) {
        loginData = AppHelper.getInstance().getLoginUser();
        textViewName.setText(loginData.getFname());
        Picasso.with(getContext()).load(ConstantClass.THUMB_IMAGE_URL + loginData.getProfileImage()).placeholder(R.drawable.avatar_icon).fit().into(profileBG);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.header_menu:
                HomeActivity.toggleDrawer();
                break;

               /* NewRequestRideDialog newRequestRideDialog = new NewRequestRideDialog();
                newRequestRideDialog.createDialog(getActivity(), "", new DialogCancel() {
                    @Override
                    public void onDismiss() {
                        navigatorInterface.openTrackRideFragment();
                    }

                    @Override
                    public void onDialogCencel() {

                    }
                });*/


            case R.id.toggleService:
                callServiceOnOFF(toggleService.isChecked());
                break;
            case R.id.img_current_location:

                if (LAT_LNG != null && map != null) {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(LAT_LNG).zoom(14).build();

                    map.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                }

                break;
        }


    }

    /*@Subscribe
    public void getData(Location location) {

    }*/

    /**
     * Call a service on off api.
     */

    private void callServiceOnOFF(final boolean status) {
        AlertUtils.showCustomProgressDialog(getContext());

        String strToken = AppHelper.getInstance().getToken();
        String strDriverId = loginData.getDriverId();
        String strService = "0";
        if (status) {
            strService = "1";
        } else {
            strService = "0";
        }
        DebugLog.i("Service OnOff Request");
        ApiService api = RetroClient.getApiService();
        Call<GenericResponse> call = api.callChangeServiceStatus(strToken, strDriverId, strService);
        APIRequestManager<GenericResponse> apiRequestManager = new APIRequestManager<>(new APIRequestManager.ResponseListener<GenericResponse>() {
            @Override
            public void onResponse(retrofit2.Response<GenericResponse> response) {
                AlertUtils.dismissDialog();
                if (response.isSuccessful()){
                    if (response.code() == ConstantClass.RESPONSE_CODE_200) {
                        try {
                            DebugLog.i("Service Success Response :" + response.body().getMessage());
                            GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(getActivity(), response.body().getMessage(), new DialogDismissOneButton() {
                                @Override
                                public void onDismiss() {
                                    if (toggleService != null) {
                                        if (status) {
                                            loginData.setIsService("1");
                                            toggleService.setChecked(true);
                                        } else {
                                            loginData.setIsService("0");
                                            toggleService.setChecked(false);
                                        }
                                        String strData = new Gson().toJson(loginData);
                                        AppHelper.getInstance().SaveInSharedPref(ConstantClass.LOGIN_USER, strData);
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        DebugLog.i("Service Failed Response :" + response.body().getMessage());
                        if (response.code() == ConstantClass.RESPONSE_CODE_400) {
                            String s = response.errorBody().string();
                            GenericResponse responsebody = ParsingHelper.getInstance().genericResponseParsing(s);
                            showSnackBar(responsebody.getMessage(), null, relativeMain);
                        } else if (response.code() == ConstantClass.RESPONSE_CODE_401) {
                            if (homeActivity != null)
                                homeActivity.Logout(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                AlertUtils.dismissDialog();
                DebugLog.e("Service Request Failed Msg :" + throwable.getMessage());
            }
        }, call);
        apiRequestManager.execute();
    }

    /**
     * This method is used to check the service status of driver. whether has on the service or off service.
     *
     * @param param parameters
     * @param token user's token
     */
    private void callProfileData(HashMap<String, String> param, String token) {
        AlertUtils.dismissDialog();
        AlertUtils.showCustomProgressDialog(getActivity());
        HomeActivityDAO.getInstance().callGetProfileData(param, ConstantClass.METHOD_GET_PROFILE_DATA, token, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                AlertUtils.dismissDialog();
                if (success) {
                    try {
                        if (data.code() == ConstantClass.RESPONSE_CODE_200) {
                            String response = data.body().string();
                            LoginResponse loginResponse = ParsingHelper.getInstance().loginResponseParsing(response);


                            if (loginResponse.getIsService().equalsIgnoreCase("0")) {
                                toggleService.setChecked(false);
                            } else {
                                toggleService.setChecked(true);
                            }

                        } else if (data.code() == ConstantClass.RESPONSE_CODE_400) {
                           /* String s = data.body().string();
                            GenericResponse response = ParsingHelper.getInstance().genericResponseParsing(s);
                            showSnackBar(response.getMessage(), null, relativeMain);*/

                        } else if (data.code() == ConstantClass.RESPONSE_CODE_401) {
                            homeActivity.Logout(false);

                        } else {
                            /*String s = data.body().string();
                            GenericResponse response = ParsingHelper.getInstance().genericResponseParsing(s);
                            showSnackbar(response.getMessage());*/
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (getActivity() != null)
                        showSnackBar(getString(R.string.bad_server), null, relativeMain);
                }
            }

        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (AppHelper.getInstance().getSharedPref(ConstantClass.USER_DEFAULT_LATITUDE) != null && AppHelper.getInstance().getSharedPref(ConstantClass.USER_DEFAULT_LONGITUDE) != null && map != null) {

            BitmapDescriptor bitmapMarker = null;
            String carType= null;
            try {
                String str = loginData.getCarType();
                /*String arrayString[] = str.split(",");
                carType = arrayString[arrayString.length-1];*/

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

            map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));

            currentPin = map.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(AppHelper.getInstance().getSharedPref(ConstantClass.USER_DEFAULT_LATITUDE)), Double.parseDouble(AppHelper.getInstance().getSharedPref(ConstantClass.USER_DEFAULT_LONGITUDE))))
                    .icon(bitmapMarker));

/*            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(Double.parseDouble(AppHelper.getInstance().getSharedPref(ConstantClass.USER_DEFAULT_LATITUDE)), Double.parseDouble(AppHelper.getInstance().getSharedPref(ConstantClass.USER_DEFAULT_LONGITUDE)))).zoom(14).build();

            map.moveCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));*/
        }

    }

    /**
     * Set up map first time.
     */

    private void setUpMap() {
        try {
            if (map != null) {
                map.setMyLocationEnabled(false);
                MapsInitializer.initialize(getContext());
                map.getUiSettings().setMyLocationButtonEnabled(false);
                map.getUiSettings().setCompassEnabled(true);
                boolean success = map.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));

                if (LAT_LNG != null) {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(LAT_LNG).zoom(14).build();

                    map.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                }

                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run(){
                                if(relativelayoutDetails.getVisibility()==View.VISIBLE) {
                                    yoyo_animation(relativelayoutDetails, 200, Techniques.SlideOutDown, View.GONE);
                                    yoyo_animation(header, 200, Techniques.SlideOutUp, View.GONE);

                                }
                                else {
                                    yoyo_animation(relativelayoutDetails, 200, Techniques.SlideInUp, View.VISIBLE);
                                    yoyo_animation(header, 200, Techniques.SlideInDown, View.VISIBLE);
                                }
                            }
                        },300);
                    }
                });

            }

        /*    }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void yoyo_animation(final View target, long time, Techniques tada, final int visible_type){
        YoYo.with(tada)
                .duration(time)
                .onStart(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        target.setVisibility(visible_type);
                    }
                })
                .playOn(target);
    }

    @Override
    public void getUpdateLatLongWsResponseFromService(TrackRide pojo) {

    }

    @Override
    public void drawerLock() {
        navigatorInterface.drawerLock(false);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }
}// end of main class
