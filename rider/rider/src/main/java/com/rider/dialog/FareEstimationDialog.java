package com.rider.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.rider.LAOXI;
import com.rider.R;
import com.rider.customclasses.CustomRadioButton;
import com.rider.customclasses.CustomTextView;
import com.rider.fragments.NewHomeFragment;
import com.rider.navigator.CloseDriverDetailsDialogInterface;
import com.rider.navigator.NavigatorInterface;
import com.rider.pojo.CartypeList;
import com.rider.pojo.DriverDetails;
import com.rider.pojo.FareEstimationData;
import com.rider.pojo.GenericResponse;
import com.rider.pojo.OrderData;
import com.rider.pojo.User;
import com.rider.retrofit.APIRequestManager;
import com.rider.retrofit.ApiService;
import com.rider.retrofit.RetroClient;
import com.rider.utils.DataToPref;
import com.rider.utils.DebugLog;
import com.rider.utils.DialogDismissOneButton;
import com.rider.utils.LaoxiConstant;
import com.rider.utils.ParsingHelper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by chirag on 18/2/16.
 */
public class FareEstimationDialog extends DialogFragment implements View.OnClickListener {

    NavigatorInterface navigatorInterface;
    Context context;
    @Bind(R.id.txtPickupAddress)
    CustomTextView txtPickupAddress;
    @Bind(R.id.txtDropoffAddress)
    CustomTextView txtDropoffAddress;
    @Bind(R.id.txtVehicleName)
    CustomTextView txtVehicleName;
    @Bind(R.id.txtApproxKm)
    CustomTextView txtApproxKm;
    @Bind(R.id.txtApproxETA)
    CustomTextView txtApproxETA;
    @Bind(R.id.txtApproxCost)
    CustomTextView txtApproxCost;
    @Bind(R.id.tvBack)
    CustomTextView tvBack;
    @Bind(R.id.txtLetsGo)
    CustomTextView txtLetsGo;
    @Bind(R.id.btn_close)
    ImageView btnClose;
    DriverDetails driverDetails;
    String carTypeId;
    @Bind(R.id.textviewVehicle)
    CustomTextView textviewVehicle;
    @Bind(R.id.view)
    View lineview;
    CloseDriverDetailsDialogInterface callback;
    @Bind(R.id.radioNow)
    CustomRadioButton radioNow;
    @Bind(R.id.radioLater)
    CustomRadioButton radioLater;
    @Bind(R.id.radioSex)
    RadioGroup radioSex;
    @Bind(R.id.txtRideType)
    CustomTextView txtRideType;
    private User user;
    String strTripType = "";

    public DriverDetails getDriverDetails() {
        return driverDetails;
    }

    public void setDriverDetails(DriverDetails driverDetails) {
        this.driverDetails = driverDetails;
    }

    public String getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(String carTypeId) {
        this.carTypeId = carTypeId;
    }

    public FareEstimationDialog() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_fare_layout, container, false);

        WindowManager.LayoutParams wmlp = getDialog().getWindow().getAttributes();
        wmlp.gravity = Gravity.FILL_HORIZONTAL;
        getDialog().getWindow().requestFeature(STYLE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_color_blue)));
        setCancelable(false);
        ButterKnife.bind(this, view);

        btnClose.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        txtLetsGo.setOnClickListener(this);

        txtPickupAddress.setText(LAOXI.currentRide.getPickupAddress());
        txtDropoffAddress.setText(LAOXI.currentRide.getDropOffAddress());

        radioNow.setChecked(true);


        String strUserData = DataToPref.getSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA);
        Gson mGson = new Gson();
        user = mGson.fromJson(strUserData, User.class);

        if (driverDetails != null) {
            txtVehicleName.setText(driverDetails.getVehicleModel());
            radioSex.setVisibility(View.GONE);
            txtRideType.setVisibility(View.GONE);
        } else {
            textviewVehicle.setVisibility(View.INVISIBLE);
            txtVehicleName.setVisibility(View.INVISIBLE);
            lineview.setVisibility(View.INVISIBLE);
        }
        strTripType = "now";
        wsCallGetFareEstimate(setFareEstimationData());


        radioSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioNow:
                        if (driverDetails != null) {
                            txtVehicleName.setText(driverDetails.getVehicleModel());
                        } else {
                            textviewVehicle.setVisibility(View.INVISIBLE);
                            txtVehicleName.setVisibility(View.INVISIBLE);
                            lineview.setVisibility(View.INVISIBLE);
                        }


                        strTripType = "now";
                        wsCallGetFareEstimate(setFareEstimationData());
                        break;

                    case R.id.radioLater:
                        textviewVehicle.setVisibility(View.INVISIBLE);
                        txtVehicleName.setVisibility(View.INVISIBLE);
                        lineview.setVisibility(View.INVISIBLE);
                        strTripType = "later";
                        wsCallGetFareEstimate(setFareEstimationData());
                        break;
                }
            }
        });


        return view;
    }

    /**
     * Call a get fare estimation between two location api.
     */

    public void wsCallGetFareEstimate(HashMap<String, String> param) {
        AlertUtils.showCustomProgressDialog(context);

        String strToken = DataToPref.getSharedPreferanceData(context.getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.HEADER_TOCKEN);
        String strPickLat = String.valueOf(LAOXI.currentRide.getPickLatitude());
        String strPickLng = String.valueOf(LAOXI.currentRide.getPickLongitude());
        String strDropLat = String.valueOf(LAOXI.currentRide.getDropOffLatitude());
        String strDropLng = String.valueOf(LAOXI.currentRide.getDropOffLongitude());
        String strCarTypeId = String.valueOf(LAOXI.currentRide.getCarTypeId());
        DebugLog.i("FareEstimate Request");
        ApiService api = RetroClient.getApiService();
        Call<FareEstimationData> call = api.doGetFareEstimate(strToken, strPickLat, strPickLng, strDropLat, strDropLng, strCarTypeId, strTripType);

        call.enqueue(new Callback<FareEstimationData>() {
            @Override
            public void onResponse(Call<FareEstimationData> call, retrofit2.Response<FareEstimationData> response) {
                AlertUtils.dismissDialog();

                if ( response.isSuccessful() ) {
                    if (response.code() == 200) {
                        FareEstimationData data = response.body();

                        DebugLog.i("FareEstimate Success Responce :" + data.getDistance());

                        txtApproxKm.setText(data.getDistance());
                        txtApproxETA.setText(data.getTime() + " mins");
                        //txtApproxCost.setText("$" +  data.getMinTotalAmount() + " - $" + data.getMaxTotalAmount());
                        txtApproxCost.setText("₭" +  data.getTotalAmount());
                    }
                }
            }

            @Override
            public void onFailure(Call<FareEstimationData> call, Throwable t) {
                AlertUtils.dismissDialog();
            }
        });

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        navigatorInterface = (NavigatorInterface) activity;
        context = (Context) activity;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        AlertUtils.dismissDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                getDialog().dismiss();
                break;

            case R.id.tvBack:
                getDialog().dismiss();
                break;

            case R.id.txtLetsGo:

                if (radioNow.isChecked()) {
                    wsCallPlaceOrder();
                } else {
                    wsCallCarListForRideLater(new LatLng(LAOXI.currentRide.getPickLatitude(), LAOXI.currentRide.getPickLongitude()));
                }

                break;
        }
    }

    /**
     * Call a place order api.
     */

    public void wsCallPlaceOrder() {
        AlertUtils.showCustomProgressDialog(context);

        String strToken = user.getToken();
        String strUserId = user.getId();
        String strCarTypeId = LAOXI.currentRide.getCarTypeId() != null ? LAOXI.currentRide.getCarTypeId() : "";

        String strPickAddress = LAOXI.currentRide.getPickupAddress();
        String strPickLat= String.valueOf(LAOXI.currentRide.getPickLatitude());
        String strPickLng = String.valueOf(LAOXI.currentRide.getPickLongitude());
        String strDropAddress = LAOXI.currentRide.getDropOffAddress();
        String strDropLat = String.valueOf(LAOXI.currentRide.getDropOffLatitude());
        String strDropLng = String.valueOf(LAOXI.currentRide.getDropOffLongitude());

        String strTripType = "";
        String strTripTime = "";
        String strGender = "";
        String strDriverId = "";
        if (LAOXI.currentRide.getTripType().equalsIgnoreCase("later")) {
            strTripType = "later";
            strTripTime = localToUTC24(LAOXI.currentRide.getDate());
            strGender = LAOXI.currentRide.getGender();
        } else {
            if (driverDetails != null) {
                if (driverDetails.getId() != null) {
                    strTripType = "now";
                    strDriverId = driverDetails.getDriverId();
                }
            } else {
                strTripType = "now";
                strDriverId = "";
            }
        }
        DebugLog.i("Call PlaceOrder");
        ApiService api = RetroClient.getApiService();
        Call<OrderData> call = api.doPlaceOrder(strToken, strUserId, strCarTypeId, strDriverId, strPickAddress, strPickLat, strPickLng, strDropAddress, strDropLat, strDropLng, strTripType, strTripTime, strGender);

        call.enqueue(new Callback<OrderData>() {
            @Override
            public void onResponse(Call<OrderData> call, retrofit2.Response<OrderData> response) {
                AlertUtils.dismissDialog();
                if ( response.isSuccessful() ) {
                    DebugLog.i("PlaceOrder Success Response");
                    if (response.code() == 201) {
                        OrderData orderData = response.body();

                        String data = new Gson().toJson(orderData);
                        DataToPref.setSharedPreferanceData(context.getApplicationContext(), LaoxiConstant.ORDER, LaoxiConstant.ORDER_DATA, data);

                        if (LAOXI.currentRide.getTripType().equalsIgnoreCase("now")) {
                            // navigatorInterface.stopTrackRideService();
                            // navigatorInterface.startTrackRideService();
                            NewHomeFragment.showTransparentLayout = true;
                        }

                        AlertUtils.dismissDialog();
                        getDialog().dismiss();
                        navigatorInterface.openHome();
                        if (callback != null) {
                            callback.onClose();
                        }
                    }
                } else {
                    DebugLog.e("PlaceOrder Failed Response code :" + response.code());
                    if (response.code() == 400) {
                        try {
                            // String data = response.body().string();
                            AlertUtils.dismissDialog();
                            getDialog().dismiss();
                            if (callback != null) {
                                callback.onClose();
                            }

                            String s = response.errorBody().string();
                            GenericResponse genericResponse = ParsingHelper.getInstance().genericResponseParsing(s);

                            if (genericResponse.getMessage().equalsIgnoreCase("Please enter card information on your account")) {
                                navigatorInterface.openPaymentDetails();
                            } else {
                                if (getActivity() != null) {
                                    AlertUtils.dismissDialog();
                                    getDialog().dismiss();
                                    if (callback != null) {
                                        callback.onClose();
                                    }
                                    navigatorInterface.openHome();
                                }

                                GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(context, genericResponse.getMessage(), new DialogDismissOneButton() {
                                    @Override
                                    public void onDismiss() {

                                    }
                                });
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderData> call, Throwable t) {
                AlertUtils.dismissDialog();
            }
        });
    }


    /**
     * Set a parameter for call a place order api.
     */

    public HashMap<String, String> setOrderData() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.USER_ID, user.getId());
        param.put(LaoxiConstant.CAR_TYPE_ID, LAOXI.currentRide.getCarTypeId() != null ? LAOXI.currentRide.getCarTypeId() : "");

        param.put(LaoxiConstant.PICKUP_ADDRESS, LAOXI.currentRide.getPickupAddress());
        param.put(LaoxiConstant.PICKUP_LATITUDE, String.valueOf(LAOXI.currentRide.getPickLatitude()));
        param.put(LaoxiConstant.PICKUP_LONGITUDE, String.valueOf(LAOXI.currentRide.getPickLongitude()));
        param.put(LaoxiConstant.DROPOFF_ADDRESS, LAOXI.currentRide.getDropOffAddress());
        param.put(LaoxiConstant.DROPOFF_LATITUDE, String.valueOf(LAOXI.currentRide.getDropOffLatitude()));
        param.put(LaoxiConstant.DROPOFF_LONGITUDE, String.valueOf(LAOXI.currentRide.getDropOffLongitude()));


        if (LAOXI.currentRide.getTripType().equalsIgnoreCase("later")) {
            param.put(LaoxiConstant.TRIP_TYPE, "later");
            param.put(LaoxiConstant.TRIPDATETIME, localToUTC24(LAOXI.currentRide.getDate()));
            param.put(LaoxiConstant.GENDER, LAOXI.currentRide.getGender());
        } else {
            if (driverDetails != null) {
                if (driverDetails.getId() != null) {
                    param.put(LaoxiConstant.TRIP_TYPE, "now");
                    param.put(LaoxiConstant.DRIVER_ID, driverDetails.getDriverId());
                }
            } else {
                param.put(LaoxiConstant.TRIP_TYPE, "now");
                param.put(LaoxiConstant.DRIVER_ID, "");
            }

        }
        return param;
    }

    /**
     * function for convert date and time to UTC format.
     */


    public String localToUTC24(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd / MM / yyyy HH:mm", Locale.ENGLISH);
        TimeZone utcZone = TimeZone.getDefault();
        simpleDateFormat.setTimeZone(utcZone);

        Date date = null;
        String dateAsString = "";
        String dateS = null;
        try {
            date = simpleDateFormat.parse(dateString);
            SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
            simple.setTimeZone(TimeZone.getTimeZone("GMT"));
            dateS = simple.format(date);
//            DebugLog.e("time" + String.valueOf(date) + "" + simpleDateFormat.format(date) + " " + utcZone);
            DebugLog.e("new " + dateS + "" + " " + simple.getTimeZone());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateS;
    }

    public void setCallback(CloseDriverDetailsDialogInterface callback) {
        this.callback = callback;
    }


    public HashMap<String, String> setFareEstimationData() {
        HashMap<String, String> param = new HashMap<>();
        param.put("pickup_latitude", String.valueOf(LAOXI.currentRide.getPickLatitude()));
        param.put("pickup_longitude", String.valueOf(LAOXI.currentRide.getPickLongitude()));
        param.put("delivery_latitude", String.valueOf(LAOXI.currentRide.getDropOffLatitude()));
        param.put("delivery_longitude", String.valueOf(LAOXI.currentRide.getDropOffLongitude()));
        param.put("car_type_id", String.valueOf(LAOXI.currentRide.getCarTypeId()));
        param.put("trip_type", strTripType);

        return param;
    }


    public void wsCallCarListForRideLater(LatLng latLng) {


        if (latLng.longitude != 0.0 && latLng.latitude != 0.0) {
            AlertUtils.showCustomProgressDialog(getActivity());

            String strToken = DataToPref.getSharedPreferanceData(context.getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.HEADER_TOCKEN);
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
            DebugLog.i("Call RideLater");
            ApiService api = RetroClient.getApiService();
            Call<CartypeList> call = api.doCarListLater(strToken, strLat, strLng, strCarType, strRegion);

            APIRequestManager<CartypeList> apiRequestManager = new APIRequestManager<>(new APIRequestManager.ResponseListener<CartypeList>() {
                @Override
                public void onResponse(retrofit2.Response<CartypeList> response) {
                    AlertUtils.dismissDialog();

                    if ( response.isSuccessful() ) {
                        DebugLog.i("RideLater Success Response");
                        if (response.code() == 200) {
                            CartypeList cartypeList = response.body();
                            LAOXI.currentRide.setCarTypeId(cartypeList.getId());
                            LAOXI.currentRide.setTripType("later");
                            getDialog().dismiss();
                            navigatorInterface.openRideLater(null);

                        }
                    } else {
                        DebugLog.e("RideLater Response Failed");
                        if (response.code() == 400) {
                            try {
                                String res = response.errorBody().string();
                                GenericResponse genericResponse = ParsingHelper.getInstance().genericResponseParsing(res);
                                GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(context, genericResponse.getMessage(), new DialogDismissOneButton() {
                                    @Override
                                    public void onDismiss() {

                                    }
                                });
                            } catch ( IOException e ) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    AlertUtils.dismissDialog();
                }
            }, call);
            apiRequestManager.execute();

        }
    }

    public HashMap<String, String> setCarListdata(LatLng latLng) {
        HashMap<String, String> param = new HashMap<>();

        param.put(LaoxiConstant.PICKUP_LATITUDE, String.valueOf(latLng.latitude));
        param.put(LaoxiConstant.PICKUP_LONGITUDE, String.valueOf(latLng.longitude));
        if (LAOXI.currentRide.getCarType() != null) {
            param.put(LaoxiConstant.CAR_TYPE, LAOXI.currentRide.getCarType());
        }
        if (LAOXI.currentRide.getRegion() != null) {
            param.put(LaoxiConstant.REGION, LAOXI.currentRide.getRegion());
        } else {
            param.put(LaoxiConstant.REGION, "");
        }

        return param;
    }


} // end of main class
