package com.driver.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.driver.R;
import com.driver.activity.BaseActivity;
import com.driver.activity.HomeActivity;
import com.driver.customclasses.CustomTextView;
import com.driver.dao.ParsingHelper;
import com.driver.pojo.GenericResponse;
import com.driver.pojo.TrackRidePojo.TrackRide;
import com.driver.pojo.acceptorder.AcceptOrderResponse;
import com.driver.retrofit.APIRequestManager;
import com.driver.retrofit.ApiService;
import com.driver.retrofit.RetroClient;
import com.driver.services.UpdateDriverLatLongService;
import com.driver.util.AlertUtils;
import com.driver.util.AnalyticsReporter;
import com.driver.util.AppHelper;
import com.driver.util.ConstantClass;
import com.driver.util.DebugLog;
import com.driver.util.DialogCancel;
import com.driver.util.PorterShapeImageView;
import com.eventbus.FcmEvent;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by chirag on 18/2/16.
 */
public class NewRequestRideDialog {


    @Bind(R.id.user_pic)
    PorterShapeImageView userPic;
    @Bind(R.id.frameLayout)
    FrameLayout frameLayout;
    @Bind(R.id.imageViewHi)
    ImageView imageViewHi;
    @Bind(R.id.txtRiderName)
    CustomTextView txtRiderName;
    @Bind(R.id.txtPickupAddress)
    CustomTextView txtPickupAddress;
    @Bind(R.id.txtDropoffAddress)
    CustomTextView txtDropoffAddress;
    @Bind(R.id.txtCancel)
    CustomTextView txtCancel;
    @Bind(R.id.txtAccept)
    CustomTextView txtAccept;
    CountDownTimer cdtimer;
    @Bind(R.id.imgviewLike)
    ImageView imgviewLike;
    @Bind(R.id.imageViewCar)
    ImageView imageViewCar;
    @Bind(R.id.favouriteImageView)
    ImageView favouriteImageView;
    private TrackRide trackRidePojo;

    private static NewRequestRideDialog ourInstance = new NewRequestRideDialog();

    public static NewRequestRideDialog getInstance() {
        return ourInstance;
    }

    private NewRequestRideDialog() {
        EventBus.getDefault().register(this);
    }

    private long mLastClickTime;
    float rate = 0;

    private int progress = 0, progressCount = 20, isCancel = 0;
    private final int pBarMax = 20;
    private Dialog dlg;
    private Activity context;

    public void createDialog(final Activity context, final DialogCancel dialogCancel) {
        this.context = context;
        String data = AppHelper.getInstance().getSharedPref(ConstantClass.TRACK_RIDE_ORDER_DATA);
        trackRidePojo = ParsingHelper.getInstance().trackRideData(data);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        dlg = new Dialog(context);
        UpdateDriverLatLongService.isDialogVisible = true;
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlg.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dlg.setCancelable(false);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.request_dilog, null);

        ButterKnife.bind(this, view);

        txtPickupAddress.setText(trackRidePojo.getPickupAddress());
        txtRiderName.setText(context.getString(R.string.i_m_alexender) + " " + trackRidePojo.getUserData().getName());
        txtDropoffAddress.setText(trackRidePojo.getDeliveryAddress());

        isCancel = 0;

        imgviewLike.setOnClickListener(v -> {
            DriverLikeDialog driverLikeDialog = new DriverLikeDialog();
            driverLikeDialog.show(context.getFragmentManager(), driverLikeDialog.getClass().getName());
        });


        try {
            cdtimer = new CountDownTimer(30000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    UpdateDriverLatLongService.isDialogVisible = false;
                    callCancelRideWs(context, txtAccept);
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        dlg.setContentView(view);
        dlg.show();

        txtAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String data = AppHelper.getInstance().getSharedPref(ConstantClass.TRACK_RIDE_ORDER_DATA);
                TrackRide trackRide = ParsingHelper.getInstance().trackRideData(data);

                Bundle properties = new Bundle();
                properties.putString("order_id", trackRide.getId());
                properties.putString("driver_id", trackRide.getDriverId());
                properties.putString("user_id", trackRide.getUserId());
                properties.putString("trip_type", trackRide.getTripType());
                AnalyticsReporter.getInstance().track(AnalyticsReporter.Event.DRIVER_ACCEPTED_ORDER_REQUEST, properties);

                // pBarThread.currentThread().interrupt();
                resetVariable();

                if (((HomeActivity) context).webServiceState == BaseActivity.WebServiceState.ACTIVE) {
                    callAcceptRideWS(context, txtAccept, dialogCancel);

                    isCancel = 1;

                    dlg.dismiss();

                    if (dialogCancel != null) {
                        dialogCancel.onDismiss();
                    }
                } else {
                    isCancel = 1;

                    dlg.dismiss();

                    if (dialogCancel != null) {
                        dialogCancel.onDismiss();
                    }
                }
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String data = AppHelper.getInstance().getSharedPref(ConstantClass.TRACK_RIDE_ORDER_DATA);
                TrackRide trackRide = ParsingHelper.getInstance().trackRideData(data);

                Bundle properties = new Bundle();
                properties.putString("order_id", trackRide.getId());
                properties.putString("driver_id", trackRide.getDriverId());
                properties.putString("user_id", trackRide.getUserId());
                properties.putString("trip_type", trackRide.getTripType());
                AnalyticsReporter.getInstance().track(AnalyticsReporter.Event.DRIVER_DECLINED_ORDER_REQUEST, properties);

                if (((HomeActivity) context).webServiceState == BaseActivity.WebServiceState.ACTIVE) {
                    resetVariable();
                    callCancelRideWs(context, txtAccept);
                } else {
                    isCancel = 1;
                    dlg.dismiss();
                }

            }
        });
        setOrderData(trackRidePojo);
    }

    private void callAcceptRideWS(final Activity context, final View v, final DialogCancel dlg) {
        try {
            ((HomeActivity) context).stopRing();
            AlertUtils.dismissDialog();
            cancelTimer();
            AlertUtils.showCustomProgressDialog(context);
            String strToken = AppHelper.getInstance().getToken();
            String strDriverId = AppHelper.getInstance().getDriverId();

            String data = AppHelper.getInstance().getSharedPref(ConstantClass.TRACK_RIDE_ORDER_DATA);
            TrackRide trackRidePojo = ParsingHelper.getInstance().trackRideData(data);
            String orderId = trackRidePojo.getId();
            DebugLog.i("Accepted Order Request with Order ID :" + orderId);
            ApiService api = RetroClient.getApiService();
            Call<AcceptOrderResponse> call = api.callAcceptRequestWS(strToken, orderId, strDriverId);

            call.enqueue(new Callback<AcceptOrderResponse>() {
                @Override
                public void onResponse(Call<AcceptOrderResponse> call, retrofit2.Response<AcceptOrderResponse> response) {
                    AlertUtils.dismissDialog();
                    if (response.isSuccessful()) {

                        UpdateDriverLatLongService.isDialogVisible = false;

                        DebugLog.i("Accepted Success Reseponse :" + response);
                        if (response.code() == ConstantClass.RESPONSE_CODE_200) {

                            AcceptOrderResponse acceptResponse = response.body();
                            NewRequestRideDialog.this.dlg.dismiss();
                            EventBus.getDefault().post("complete");
                            if (dlg != null) {
                                dlg.onDismiss();

                            }

                        }

                    } else {
                        try {
                            String s = response.errorBody().string();
                            GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                            DebugLog.i("Accepted Failed Request: " + s);

                            if (response.code() == ConstantClass.RESPONSE_CODE_404) {

                                showSnackbar(responseBody.getMessage(), v);
                            } else if (response.code() == ConstantClass.RESPONSE_CODE_401) {
                                ((HomeActivity) context).callLogoutWS();
                            } else if (response.code() == ConstantClass.RESPONSE_CODE_400) {

                                if (response.message() != null) {
                                    GeneralStaticAlertDialog.getInstance().createDialogWithOneButton(context, "", responseBody.getMessage(), () -> {
                                        NewRequestRideDialog.this.dlg.dismiss();
                                        if (dlg != null) {
                                            dlg.onDismiss();
                                        }
                                    });
                                }

                            } else {
                                if (responseBody == null) {
                                    showSnackbar("Can't send notification to user because user doesn't have device type.", v);
                                } else {
                                    showSnackbar(responseBody.getMessage(), v);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AcceptOrderResponse> call, Throwable t) {
                    DebugLog.e("Accepted Request Failed : " + t.getMessage());
                    AlertUtils.dismissDialog();
                    showSnackbar(context.getString(R.string.bad_server), v);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callCancelRideWs(final Activity context, final View v) {
        if (context != null) {
            ((HomeActivity) context).stopRing();
            AlertUtils.dismissDialog();
            cdtimer.cancel();
            isCancel = 1;
            AlertUtils.showCustomProgressDialog(context);

            String strToken = AppHelper.getInstance().getToken();
            String strDriverId = AppHelper.getInstance().getDriverId();

            String data = AppHelper.getInstance().getSharedPref(ConstantClass.TRACK_RIDE_ORDER_DATA);
            TrackRide trackRidePojo = ParsingHelper.getInstance().trackRideData(data);
            String orderId = trackRidePojo.getId();

            DebugLog.i("Cancel Order Request with Order ID :" + orderId);
            ApiService api = RetroClient.getApiService();
            Call<GenericResponse> call = api.callDeclineRequestWS(strToken, orderId, strDriverId);
            APIRequestManager<GenericResponse> apiRequestManager = new APIRequestManager<>(new APIRequestManager.ResponseListener<GenericResponse>() {
                @Override
                public void onResponse(retrofit2.Response<GenericResponse> response) {
                    AlertUtils.dismissDialog();
                    if (response.isSuccessful()) {
                        DebugLog.i("Cancel Success Response :" + response.code());

                        UpdateDriverLatLongService.isDialogVisible = false;

                        if (response.code() == ConstantClass.RESPONSE_CODE_200) {
                            //isCancel = 1;

                            dlg.dismiss();
                            GenericResponse responseBody = response.body();
                            if (responseBody != null) {
                                if (responseBody.getMessage() != null) {
                                    ((BaseActivity) context).showSnackbar(responseBody.getMessage());
                                }
                            }

                        }


                    } else {
                        try {
                            String s = response.errorBody().string();
                            DebugLog.i("Cancel Failed Response :" + s);
                            if (response.code() == ConstantClass.RESPONSE_CODE_401) {
                                ((HomeActivity) context).callLogoutWS();
                            } else if (response.code() == ConstantClass.RESPONSE_CODE_404) {
                                GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                                showSnackbar(responseBody.getMessage(), v);

                            } else if (response.code() == ConstantClass.RESPONSE_CODE_400) {
                                GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                                GeneralStaticAlertDialog.getInstance().createDialogWithOneButton(context, "", responseBody.getMessage(), () -> {
                                    if (dlg != null) {
                                        dlg.dismiss();
                                    }
                                });

                            } else {
                                GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                                showSnackbar(responseBody.getMessage(), v);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    DebugLog.e("Cancel Ride Request Failed : " + throwable.getMessage());
                    AlertUtils.dismissDialog();
                    showSnackbar(context.getString(R.string.bad_server), v);
                }
            }, call);
            apiRequestManager.execute();
        }
    }

    public void showSnackbar(String message, View v) {
        final Snackbar snackBar = Snackbar.make(v, message, Snackbar.LENGTH_LONG);

        snackBar.setAction("Dismiss", v1 -> snackBar.dismiss());
        snackBar.show();
    }

    public boolean isShown() {
        if (dlg != null && dlg.isShowing()) {
            return true;
        } else {
            return false;
        }
    }

    private void resetVariable() {
        progress = 0;
        progressCount = 20;
        //progressBar.setMax(pBarMax);

    }

    private void setOrderData(TrackRide trackRidePojo) {
        // change the up votes and down votes once the user's vote integrate in ws
        try {
            Picasso.with(context)
                    .load(ConstantClass.USER_IMAGE_URL + trackRidePojo.getUserData().getProfileImage())
                    .fit()
                    .placeholder(R.drawable.avatar_icon)
                    .into(userPic);

            String strCarType = trackRidePojo.getCarType();
            if (trackRidePojo.getDriverData().getGender().equalsIgnoreCase("male")) {

                if (strCarType.equalsIgnoreCase(ConstantClass.TAXI)) {
                    Picasso.with(context)
                            .load(R.drawable.taxi_icon)
                            .into(imageViewCar);

                } else if (strCarType.equalsIgnoreCase(ConstantClass.BIKE)) {
                    Picasso.with(context)
                            .load(R.drawable.bike_icon)
                            .into(imageViewCar);

                } else if (strCarType.equalsIgnoreCase(ConstantClass.TUKTUK)) {

                    Picasso.with(context)
                            .load(R.drawable.tuktuk_icon)
                            .into(imageViewCar);

                } else {
                    Picasso.with(context)
                            .load(R.drawable.owner_icon)
                            .into(imageViewCar);
                }
            }
            favouriteImageView.setVisibility(trackRidePojo.getDriverData().isFavouriteDriver() ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onEvent(FcmEvent.AlreadyAccepted inst) {
        if (isShown()) dlg.dismiss();
        cancelTimer();
    }


    void cancelTimer() {
        if (cdtimer == null) return;
        isCancel = 1;
        cdtimer.cancel();
        cdtimer = null;
    }
}// end of main class
