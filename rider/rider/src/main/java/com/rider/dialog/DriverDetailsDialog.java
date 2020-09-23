package com.rider.dialog;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.github.siyamed.shapeimageview.ImageViewProfile;
import com.google.gson.Gson;
import com.rider.LAOXI;
import com.rider.R;
import com.rider.customclasses.CustomButton;
import com.rider.customclasses.CustomTextView;
import com.rider.fragments.NewHomeFragment;
import com.rider.navigator.CloseDriverDetailsDialogInterface;
import com.rider.navigator.NavigatorInterface;
import com.rider.pojo.DriverDetails;
import com.rider.pojo.Driverlist;
import com.rider.pojo.GenericResponse;
import com.rider.pojo.OrderData;
import com.rider.pojo.User;
import com.rider.retrofit.ApiService;
import com.rider.retrofit.RetroClient;
import com.rider.utils.DataToPref;
import com.rider.utils.DebugLog;
import com.rider.utils.DialogDismissOneButton;
import com.rider.utils.LaoxiConstant;
import com.rider.utils.ParsingHelper;
import com.rider.utils.RotateLoading;
import com.rider.utils.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by chirag on 18/2/16.
 */
public class DriverDetailsDialog extends DialogFragment implements View.OnClickListener, CloseDriverDetailsDialogInterface {

    @Bind(R.id.user_pic)
    ImageViewProfile userPic;
    @Bind(R.id.txtMainLikes)
    CustomTextView txtMainLikes;
    @Bind(R.id.frame_driver)
    FrameLayout frameDriver;
    @Bind(R.id.imageViewHi)
    ImageView imageViewHi;
    @Bind(R.id.textViewDriverName)
    CustomTextView textViewDriverName;
    @Bind(R.id.imageViewCar)
    ImageView imageViewCar;
    @Bind(R.id.txtCarName)
    CustomTextView txtCarName;
    @Bind(R.id.txtCarNumber)
    CustomTextView txtCarNumber;
    @Bind(R.id.txtEta)
    CustomTextView txtEta;
    @Bind(R.id.txtEstimateFare)
    CustomButton txtEstimateFare;
    @Bind(R.id.txtJumpin)
    CustomButton txtJumpin;
    @Bind(R.id.txtGoBack)
    ImageButton txtGoBack;
    private String carTypeId;
    private String driverId;
    private AlertDialog.Builder dialog;
    private AlertDialog alertDialog;

    NavigatorInterface navigatorInterface;
    Driverlist driverlist;
    Context context;

    private User user;
    String ETA;
    String carModel;
    DriverDetails driverDetails;
    Activity activity;
    private RotateLoading rotateLoading;
    public CloseDialogToRefresh closeDialogToRefresh;
    public DriverDetailsDialog() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_details_dialog, container, false);
        WindowManager.LayoutParams wmlp = getDialog().getWindow().getAttributes();
        wmlp.gravity = Gravity.FILL_HORIZONTAL;
        getDialog().getWindow().requestFeature(STYLE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_color_blue)));
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
      /*  WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window =   getDialog().getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);*/
        setCancelable(false);
        ButterKnife.bind(this, view);

        String strUserData = DataToPref.getSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA);
        Gson mGson = new Gson();
        user = mGson.fromJson(strUserData, User.class);

        txtJumpin.setEnabled(false);
        txtEstimateFare.setEnabled(false);

        txtJumpin.setOnClickListener(this);
        txtGoBack.setOnClickListener(this);
        txtEstimateFare.setOnClickListener(this);
        txtMainLikes.setOnClickListener(this);

        return view;
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
        wsCallGetDriverDetails(driverId);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtJumpin:

                if (LAOXI.currentRide.getDropOffAddress() == null || LAOXI.currentRide.getDropOffAddress().equalsIgnoreCase("")) {
                    GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(context, "Please provide drop off location", null);
                } else {
                    wsCallPlaceOrder();
                }

                break;
            case R.id.txtGoBack:

                if(getActivity() !=null){
                    closeDialogToRefresh.callRefreshMapAndDismissDialog();
                    getDialog().dismiss();
//                    navigatorInterface.openHome();

                }
                break;
            case R.id.txtEstimateFare:
                if (LAOXI.currentRide.getDropOffAddress() == null || LAOXI.currentRide.getDropOffAddress().equalsIgnoreCase("")) {
                    GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(context, "Please provide drop off location", null);
                } else {
                    navigatorInterface.openFareEstimation(driverDetails, carTypeId, DriverDetailsDialog.this);
                    /*FareEstimationDialog fareEstimationDialog = new FareEstimationDialog();
                    fareEstimationDialog.setDriverDetails(driverDetails);
                    fareEstimationDialog.setCarTypeId(carTypeId);
                    fareEstimationDialog.setCallback(DriverDetailsDialog.this);
                    fareEstimationDialog.show(getFragmentManager(), fareEstimationDialog.getClass().getName());*/
                }
                break;

            case R.id.txtMainLikes:
                DriverLikeDialog driverLikeDialog = new DriverLikeDialog();
                driverLikeDialog.setDriverDetails(driverDetails);
                driverLikeDialog.show(getFragmentManager(), driverLikeDialog.getClass().getName());
                break;
        }
    }


    /**
     * Call a driver detail api.
     */


    public void wsCallGetDriverDetails(final String driverId) {
        AlertUtils.showCustomProgressDialog(getActivity());

        String strToken = user.getToken();
        ApiService api = RetroClient.getApiService();
        Call<DriverDetails> call = api.doGetDriverDetails(strToken, driverId, String.valueOf(LAOXI.currentRide.getPickLatitude()), String.valueOf(LAOXI.currentRide.getPickLongitude()));
        DebugLog.i("Call DriverDetails");
        call.enqueue(new Callback<DriverDetails>() {
            @Override
            public void onResponse(Call<DriverDetails> call, retrofit2.Response<DriverDetails> response) {
                AlertUtils.dismissDialog();
                if ( response.isSuccessful() ) {
                    txtJumpin.setEnabled(true);
                    txtEstimateFare.setEnabled(true);

                    driverDetails = response.body();
                    DebugLog.i("DriverDetails Success Response => Driver ID : " + driverDetails.getDeviceId());

                    Picasso.with(context)
                            .load(getResources().getString(R.string.MAINURL_FOR_DRIVER_IMAGE) + driverDetails.getProfileImage())
                            .fit()
                            .placeholder(R.drawable.avatar_icon)
                            .into(userPic);

                        /*Picasso.with(context)
                                .load(getResources().getString(R.string.MAINURL_FOR_DRIVER_CAR) + driverDetails.getVehiclePhoto()).transform(new RoundedTransformation(100,0))
                                .into(imageViewCar);*/
                    Picasso.with(context)

                            .load(getResources().getString(R.string.MAINURL_FOR_DRIVER_CAR) + driverDetails.getCarTypeImage()).transform(new RoundedTransformation(15, 0))
                            .fit()
                            .placeholder(R.drawable.car_default)
                            .into(imageViewCar);


                    ETA = driverDetails.getEta();
                    txtEta.setText(ETA);
                    textViewDriverName.setText("i'm " + driverDetails.getFname());
                    txtMainLikes.setText(driverDetails.getUpVotes());

                    txtCarNumber.setText(driverDetails.getVehicleRegNumber());
                    txtCarName.setText(driverDetails.getVehicleMake() + " " + driverDetails.getVehicleModel());
                    carModel = driverDetails.getVehicleModel();
                } else {
                    DebugLog.e("DriverDetails Response Failed");
                    if (response.code() == 400) {
                        wsCallGetDriverDetails(driverId);
                    }
                }
            }

            @Override
            public void onFailure(Call<DriverDetails> call, Throwable t) {
                AlertUtils.dismissDialog();
                DebugLog.e("DriverDetails Request Failed");
            }
        });
    }

    public HashMap<String, String> setDriverData(String driverId) {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.DRIVER_ID, driverId);
        param.put(LaoxiConstant.USER_LAT, String.valueOf(LAOXI.currentRide.getPickLatitude()));
        param.put(LaoxiConstant.USER_LNG, String.valueOf(LAOXI.currentRide.getPickLongitude()));
        return param;
    }

    /**
     * Call a place order api.
     */

    public void wsCallPlaceOrder() {
        AlertUtils.showCustomProgressDialog(getActivity());

        String strToken = user.getToken();
        String strUserId = user.getId();
        String strCarTypeId = LAOXI.currentRide.getCarTypeId();
        String strDriverId = "";
        if (driverId != null) {
            strDriverId = driverDetails.getDriverId();
        }
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
        if (LAOXI.currentRide.getTripType().equalsIgnoreCase("later")) {
            strTripTime = "";
        }
        String strGender = "";
        DebugLog.i("Call DoPlaceOrder");
        ApiService api = RetroClient.getApiService();
        Call<OrderData> call = api.doPlaceOrder(strToken, strUserId, strCarTypeId, strDriverId, strPickAddress, strPickLat, strPickLng, strDropAddress, strDropLat, strDropLng, strTripType, strTripTime, strGender);
        call.enqueue(new Callback<OrderData>() {
            @Override
            public void onResponse(Call<OrderData> call, retrofit2.Response<OrderData> response) {
                AlertUtils.dismissDialog();
                if ( response.isSuccessful() ) {
                    DebugLog.i("DoPlaceOrder Success Response Code : " + response.code());
                    if (response.code() == 201) {
                        OrderData orderData = response.body();

                        String data = new Gson().toJson(orderData);
                        DataToPref.setSharedPreferanceData(context.getApplicationContext(), LaoxiConstant.ORDER, LaoxiConstant.ORDER_DATA, data);
                        DebugLog.e("data is======>" + orderData);
                        NewHomeFragment.showTransparentLayout = true;

                        if (getActivity() != null) {
                            getDialog().dismiss();
                            navigatorInterface.openHome();
                        }

                    }
                } else {
                    DebugLog.i("DoPlaceOrder Failed Response Code : " + response.code());
                    if ( response.code() == 400 ) {
                        String s = null;
                        try {
                            s = response.errorBody().string();
                            GenericResponse genericResponse = ParsingHelper.getInstance().genericResponseParsing(s);

                            if (genericResponse.getMessage().equalsIgnoreCase("Please enter card information on your account")) {
                                getDialog().dismiss();
                                navigatorInterface.openPaymentDetails();
                            } else {
                                GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(context, genericResponse.getMessage(), new DialogDismissOneButton() {
                                    @Override
                                    public void onDismiss() {
                                        if (getActivity() != null) {
                                            getDialog().dismiss();
                                            navigatorInterface.openHome();
                                        }

                                    }
                                });
                            }

                            DebugLog.e("cancel order date error:" + genericResponse);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderData> call, Throwable t) {
                AlertUtils.dismissDialog();
                DebugLog.i("DoPlaceOrder Request Failed Msg : " + t.getMessage());
            }
        });
    }

    /**
     * Set a parameter for call place order api.
     */

    public HashMap<String, String> setOrderData() {
        try {
            HashMap<String, String> param = new HashMap<>();
            param.put(LaoxiConstant.USER_ID, user.getId());
            param.put(LaoxiConstant.CAR_TYPE_ID, LAOXI.currentRide.getCarTypeId());
            param.put(LaoxiConstant.PICKUP_ADDRESS, LAOXI.currentRide.getPickupAddress());
            param.put(LaoxiConstant.PICKUP_LATITUDE, String.valueOf(LAOXI.currentRide.getPickLatitude()));
            param.put(LaoxiConstant.PICKUP_LONGITUDE, String.valueOf(LAOXI.currentRide.getPickLongitude()));
            if (LAOXI.currentRide.getDropOffAddress() != null) {
                param.put(LaoxiConstant.DROPOFF_ADDRESS, LAOXI.currentRide.getDropOffAddress());
                param.put(LaoxiConstant.DROPOFF_LATITUDE, String.valueOf(LAOXI.currentRide.getDropOffLatitude()));
                param.put(LaoxiConstant.DROPOFF_LONGITUDE, String.valueOf(LAOXI.currentRide.getDropOffLongitude()));
            }
            param.put(LaoxiConstant.TRIP_TYPE, LAOXI.currentRide.getTripType());

            if (LAOXI.currentRide.getTripType().equalsIgnoreCase("later")) {
                param.put(LaoxiConstant.TRIPDATETIME, "");
            }
            if (driverId != null) {
                param.put(LaoxiConstant.DRIVER_ID, driverDetails.getDriverId());
            }
            return param;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(String carTypeId) {
        this.carTypeId = carTypeId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }


    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getETA() {
        return ETA;
    }

    public void setETA(String ETA) {
        this.ETA = ETA;
    }

    @Override
    public void onClose() {
        dismiss();
    }

    public interface CloseDialogToRefresh{
        void callRefreshMapAndDismissDialog();
    }

    public CloseDialogToRefresh getCloseDialogToRefresh() {
        return closeDialogToRefresh;
    }

    public void setCloseDialogToRefresh(CloseDialogToRefresh closeDialogToRefresh) {
        this.closeDialogToRefresh = closeDialogToRefresh;
    }
}// end of main class
