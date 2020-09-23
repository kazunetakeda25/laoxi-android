package com.driver.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.driver.R;
import com.driver.activity.HomeActivity;
import com.driver.customclasses.CustomButton;
import com.driver.customclasses.CustomEditText;
import com.driver.customclasses.CustomRadioButton;
import com.driver.customclasses.CustomTextView;
import com.driver.dao.IAsyncCompleteCallback;
import com.driver.dao.ParsingHelper;
import com.driver.dao.homedao.HomeActivityDAO;
import com.driver.pojo.GenericResponse;
import com.driver.pojo.TrackRidePojo.TrackRide;
import com.driver.pojo.TrackRidePojo.TrackRidePojo;
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
import com.driver.util.DialogDismissOneButton;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by chirag on 18/2/16.
 */
public class CancelRideDialog {


    Dialog dlg;
    HomeActivity context;
    @Bind(R.id.txtRiderName)
    CustomTextView txtRiderName;

    @Bind(R.id.cb1)
    CustomRadioButton cb1;
    @Bind(R.id.cb2)
    CustomRadioButton cb2;
    @Bind(R.id.cb3)
    CustomRadioButton cb3;
    @Bind(R.id.cb4)
    CustomRadioButton cb4;
    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;
    @Bind(R.id.btnOk)
    CustomButton btnOk;
    @Bind(R.id.edtReson)
    CustomEditText edtReson;
    private TrackRide trackridePojo;
    private String selectedReason;
    View view;

    public void createDialog(final HomeActivity context, final TrackRide response, final DialogCancel dialogCencel) {
        this.context = context;
        this.trackridePojo = response;
        dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlg.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        //dlg.setCancelable(false);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = layoutInflater.inflate(R.layout.cancel_ride_dilog, null);
        ButterKnife.bind(this, view);
        dlg.setContentView(view);
        dlg.show();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonID = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) group.findViewById(radioButtonID);
                //int idx = group.indexOfChild(radioButton);
                selectedReason = radioButton.getText().toString();
                if (radioButton.getId() == R.id.cb4) {
                    edtReson.setVisibility(View.VISIBLE);
                } else {
                    edtReson.setVisibility(View.GONE);
                }

            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validation()) {
                    Bundle properties = new Bundle();
                    properties.putString("order_id", trackridePojo.getId());
                    properties.putString("driver_id", trackridePojo.getDriverId());
                    properties.putString("user_id", trackridePojo.getUserId());
                    properties.putString("trip_type", trackridePojo.getTripType());
                    properties.putString("cancel_reason", selectedReason);
                    AnalyticsReporter.getInstance().track(AnalyticsReporter.Event.DRIVER_CANCELED_RIDE, properties);

                    callCancelTripWS();
                }
            }
        });

      /*  btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dlg.dismiss();

            }
        });*/

    }

    private boolean validation() {
        if (!cb4.isChecked()) {
            return true;
        } else {
            if (edtReson.getText().toString().isEmpty()) {

                showAlertMessage(view,"Please enter reason");
                //  context.showSnackbar("Please enter reason");
                return false;
            }
        }
        return true;
    }

    private void callCancelTripWS() {
        AlertUtils.showCustomProgressDialog(context);
        String strToken = AppHelper.getInstance().getToken();
        String strDriverId = AppHelper.getInstance().getDriverId();
        String strOrderId = trackridePojo.getId();
        String strReason = selectedReason;
        DebugLog.i("Call Cancel Trip Request");

        ApiService api = RetroClient.getApiService();
        Call<GenericResponse> call = api.callCancelTripWS(strToken, strDriverId, strOrderId, strReason, "driver");
        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, retrofit2.Response<GenericResponse> response) {
                AlertUtils.dismissDialog();
                if (response.isSuccessful()) {

                    if (response.code() == ConstantClass.RESPONSE_CODE_200) {

                        AppHelper.getInstance().SaveInSharedPref(ConstantClass.SHARED_PREFRENCE_DISTANCE, "0.0");

                        dlg.dismiss();
                        GenericResponse genericResponse = response.body();

                        DebugLog.i("Cancel Success Reseponse :" + genericResponse);


                        if (genericResponse != null) {
                            context.showSnackbar(genericResponse.getMessage());
                        }
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        context.finish();
                    }
                } else {
                    try {
                        String s = response.errorBody().string();
                        GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                        DebugLog.i("Cancel Ride Response Failed : " + s);

                        if (response.code() == ConstantClass.RESPONSE_CODE_404) {

                            context.showSnackbar(responseBody.getMessage());
                        } else if (response.code() == ConstantClass.RESPONSE_CODE_401) {
                            ((HomeActivity) context).callLogoutWS();
                        } else {
                            if (responseBody == null) {
                                context.showSnackbar("Can't send notification to user because user doesn't have device type.");
                            } else {
                                context.showSnackbar(responseBody.getMessage());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                DebugLog.e("Cancel Failed Request : " + t.getMessage());
                AlertUtils.dismissDialog();
                context.showSnackbar(context.getString(R.string.bad_server));
            }
        });

    }

    public void showAlertMessage(View view, String message) {

        if (view != null) {
            final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            }).show();
        }

    }

}
