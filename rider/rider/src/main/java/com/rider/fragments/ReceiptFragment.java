package com.rider.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.rider.R;
import com.rider.customclasses.CustomButton;
import com.rider.customclasses.CustomTextView;
import com.rider.dialog.AlertUtils;
import com.rider.dialog.GeneralStaticAlertDialog;
import com.rider.navigator.NavigatorInterface;
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

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by hitesh on 20/7/16.
 */
public class ReceiptFragment extends BaseFragment implements View.OnClickListener {


    @Bind(R.id.header_menu)
    ImageView headerMenu;
    @Bind(R.id.header_text)
    CustomTextView headerText;
    @Bind(R.id.img_share)
    ImageView imgShare;
    @Bind(R.id.imageViewPickUp)
    ImageView imageViewPickUp;
    @Bind(R.id.textViewPickupLable)
    CustomTextView textViewPickupLable;
    @Bind(R.id.txtPickupAddress)
    CustomTextView txtPickupAddress;
    @Bind(R.id.imageviewLocationLine)
    ImageView imageviewLocationLine;
    @Bind(R.id.imageViewDropOff)
    ImageView imageViewDropOff;
    @Bind(R.id.textViewDropOffLable)
    CustomTextView textViewDropOffLable;
    @Bind(R.id.textViewDropOff)
    CustomTextView textViewDropOff;
    @Bind(R.id.relativelayoutDetails)
    RelativeLayout relativelayoutDetails;
    @Bind(R.id.textViewDistanceTime)
    CustomTextView textViewDistanceTime;
    @Bind(R.id.textViewDistanceKm)
    CustomTextView textViewDistanceKm;
    @Bind(R.id.textViewDriverName)
    CustomTextView textViewDriverName;
    @Bind(R.id.textViewCarType)
    CustomTextView textViewCarType;
    @Bind(R.id.textViewCost)
    CustomTextView textViewCost;
    @Bind(R.id.toggleService)
    ToggleButton toggleService;
    @Bind(R.id.btnContinue)
    CustomButton btnContinue;
    @Bind(R.id.imageViewQuestion)
    ImageView imageViewQuestion;
    private OrderData orderData;
    NavigatorInterface navigatorInterface;
    Gson mGson = new Gson();
    Double roundedFare=0.0;
    private User user;
    private String pay_type="cash"; //rhc added
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rider_receipt_layout, container, false);
        ButterKnife.bind(this, view);

        /**
         * Get a data from shared preference and show on screen.
         */

        String strUserData = DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA);

        user = mGson.fromJson(strUserData, User.class);

        String strOrderData = DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.ORDER, LaoxiConstant.ORDER_DATA);
        DataToPref.setSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.IS_PROVIDE_DROPOFF, LaoxiConstant.IS_PROVIDE_DROPOFF_DATA, "false");
        orderData = mGson.fromJson(strOrderData, OrderData.class);


        if (orderData != null) {
            txtPickupAddress.setText(orderData.getPickupAddress());
            textViewDropOff.setText(orderData.getDeliveryAddress());
            textViewDistanceTime.setText(setTime(orderData.getTripDuration()));
            textViewDistanceKm.setText(orderData.getDistance());
            textViewDriverName.setText(orderData.getDriverData().getFname());
            textViewCarType.setText(orderData.getDriverData().getVehicleModel());
            textViewCost.setText("₭" + orderData.getTotalAmount());
        }

        headerMenu.setVisibility(View.GONE);
        imgShare.setVisibility(View.GONE);
        headerText.setText(R.string.receipt);

        btnContinue.setOnClickListener(this);

        toggleService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    /*DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);
*/

  /*                  String value= String.valueOf(round(Double.parseDouble(orderData.getTotalAmount()),2));*/

                    /*DecimalFormat df = new DecimalFormat("#.##");
                    String value=df.format(Double.parseDouble(orderData.getTotalAmount()));*/

                    DecimalFormat precision = new DecimalFormat("0.00");
                    String value = precision.format(Math.ceil(Double.parseDouble(orderData.getTotalAmount())));

                    roundedFare=(Double.parseDouble(value)-Double.parseDouble(orderData.getTotalAmount()));

                    DebugLog.e("Rounded fare is"+roundedFare);

                    textViewCost.setText("$" + value);
                } else {
                    roundedFare=0.0;
                    textViewCost.setText("$" + orderData.getTotalAmount());
                }
            }
        });

        imageViewQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralStaticAlertDialog.getInstance().createDialogQuestion(getActivity(), new DialogDismissOneButton() {
                    @Override
                    public void onDismiss() {
                    }
                });
            }
        });

        return view;
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnContinue:
                wsCallPayment();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String strUserData = DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA);

        user = mGson.fromJson(strUserData, User.class);

        String strOrderData = DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.ORDER, LaoxiConstant.ORDER_DATA);
        DataToPref.setSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.IS_PROVIDE_DROPOFF, LaoxiConstant.IS_PROVIDE_DROPOFF_DATA, "false");
        orderData = mGson.fromJson(strOrderData, OrderData.class);
    }


    /**
     * Call a make payment done api.
     */

    public void wsCallPayment() {

        AlertUtils.showCustomProgressDialog(getContext());

        String strToken = user.getToken();
        String strUserId = user.getId();
        String strCardToken = "";
        if ( user.getCardInfo().size() > 0 ) {
            strCardToken = user.getCardInfo().get(0).getToken();
        }
        DebugLog.i("DoPayment Request");
        ApiService api = RetroClient.getApiService();
        Call<Void> call = api.doPayment(strToken, strUserId, orderData.getId(), pay_type, "0", strCardToken, String.valueOf(roundedFare)); //rhc modified
        //Call<Void> call = api.doPayment(strToken, strUserId, orderData.getId(), "0", strCardToken, String.valueOf(roundedFare));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                AlertUtils.dismissDialog();

                if ( response.isSuccessful() ) {
                    DebugLog.e("Payment Success response ======>" + response.body());
                    if (response.code() == 200) {

                        navigatorInterface.openFeedback();
                    }
                } else {
                    try {
                        String s = response.errorBody().string();
                        GenericResponse genericResponse = ParsingHelper.getInstance().genericResponseParsing(s);

                        DebugLog.e("Payment Request Failed Message ======>" + genericResponse.getMessage());
                        if ( response.code() == 400 ) {
                            GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(getActivity(), genericResponse.getMessage(), null);
                        } else if ( response.code() == 402 ) {
                            GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(getActivity(), genericResponse.getMessage(), null);
                            navigatorInterface.openPaymentDetails();
                        }

                    } catch ( IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                AlertUtils.dismissDialog();
            }
        });
    }


    public HashMap<String, String> setPaymentData() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.ORDER_ID, orderData.getId());
        param.put(LaoxiConstant.TIP, "0");
        if ( user.getCardInfo().size() > 0 ) {
            param.put(LaoxiConstant.CARD_TOKEN, user.getCardInfo().get(0).getToken());
        }
        param.put(LaoxiConstant.ROUNDED_FARE,String.valueOf(roundedFare));
        return param;
    }


    /**
     * Display time in particular time format.
     */

    private String setTime(String myDateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date date = sdf.parse(myDateString);

            Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
            calendar.setTime(date);   // assigns calendar to given date
            int hour = calendar.get(Calendar.HOUR);
            int min = calendar.get(Calendar.MINUTE);
            String strTime = "";
            if (hour <= 0) {
                strTime = min + " min ";
            } else {
                strTime = hour + " hour " + min + " min ";
            }

            return strTime;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

}
