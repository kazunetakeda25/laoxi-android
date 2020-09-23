package com.rider.fragments;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.rider.LAOXI;
import com.rider.R;
import com.rider.activity.LocationSearchActivityNew;
import com.rider.customclasses.CustomButton;
import com.rider.customclasses.CustomTextView;
import com.rider.dialog.AlertUtils;
import com.rider.navigator.NavigatorInterface;
import com.rider.pojo.OrderData;
import com.rider.pojo.User;
import com.rider.retrofit.APIRequestManager;
import com.rider.retrofit.ApiService;
import com.rider.retrofit.RetroClient;
import com.rider.utils.DataToPref;
import com.rider.utils.DebugLog;
import com.rider.utils.LaoxiConstant;
import com.rider.utils.IRingtoneHandler;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by hitesh on 20/7/16.
 */
public class DriverArrivedFragment extends BaseFragment implements View.OnClickListener, IRingtoneHandler {


    NavigatorInterface navigatorInterface;
    @Bind(R.id.btnEnterDropOffLocation)
    CustomTextView btnEnterDropOffLocation;
    @Bind(R.id.btnContinue)
    CustomButton btnContinue;
    @Bind(R.id.linear_main)
    LinearLayout linearMain;

    private User user;
    private OrderData orderData;
    private static MediaPlayer mp;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_arrived_layout, container, false);
        ButterKnife.bind(this, view);
        clearNotification();
        String strUserData = DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA);
        Gson mGson = new Gson();
        user = mGson.fromJson(strUserData, User.class);

        String strOrderData = DataToPref.getSharedPreferanceData(getContext().getApplicationContext(), LaoxiConstant.ORDER, LaoxiConstant.ORDER_DATA);
        orderData = mGson.fromJson(strOrderData, OrderData.class);

        try {
            stopRing();
            startRing();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // imgShare.setVisibility(View.GONE);
        // headerMenu.setVisibility(View.GONE);
        //headerText.setText("Driver arrived");
        btnContinue.setOnClickListener(this);
        btnEnterDropOffLocation.setOnClickListener(this);

        if(!orderData.getDeliveryAddress().equalsIgnoreCase(""))
        {
            btnEnterDropOffLocation.setText(orderData.getDeliveryAddress());
            LAOXI.currentRide.setDropOffAddress(orderData.getDeliveryAddress());
            LAOXI.currentRide.setDropOffLatitude(Double.parseDouble(orderData.getDeliveryLatitude()));
            LAOXI.currentRide.setDropOffLongitude(Double.parseDouble(orderData.getDeliveryLongitude()));
        }


      /*  if(LAOXI.currentRide.getDropOffAddress()== null || LAOXI.currentRide.getDropOffAddress().equalsIgnoreCase("")){
            btnEnterDropOffLocation.setVisibility(View.VISIBLE);
        }else{
            btnEnterDropOffLocation.setVisibility(View.INVISIBLE);
        }*/

        /*if (orderData.getDeliveryAddress().equalsIgnoreCase("")) {
            btnEnterDropOffLocation.setVisibility(View.VISIBLE);
            btnContinue.setVisibility(View.VISIBLE);
        } else {
            btnEnterDropOffLocation.setVisibility(View.INVISIBLE);
            btnContinue.setVisibility(View.INVISIBLE);
        }*/


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
                validate();
                stopRing();
                break;

            case R.id.btnEnterDropOffLocation:
                stopRing();
                getAddress();

                break;
        }
    }

    public void getAddress() {

        Intent intent = new Intent(getActivity(), LocationSearchActivityNew.class);
        startActivityForResult(intent, 111);
    }

    /**
     * Get a call back when user enter address in edit text .
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            try {
                String result = data.getStringExtra("result");
                String[] LocationData = result.split(",,,");


                if (LocationData[0] != null && LocationData[1] != null) {

                    LAOXI.currentRide.setDropOffAddress(LocationData[7]);
                    btnEnterDropOffLocation.setText(LocationData[7]);
                    LAOXI.currentRide.setDropOffLatitude(Double.parseDouble(LocationData[0]));
                    LAOXI.currentRide.setDropOffLongitude(Double.parseDouble(LocationData[1]));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * validation for drop off address.
     */

    private void validate() {
        if (LAOXI.currentRide.getDropOffAddress() == null || LAOXI.currentRide.getDropOffAddress().equalsIgnoreCase("")) {
            showSnackBar("Please enter your drop off address", null, linearMain);
        } else {
            wsCallDropOffData();
        }
    }


    /**
     * Call a update drop off data api.
     */

    public void wsCallDropOffData() {
        AlertUtils.showCustomProgressDialog(getContext());

        String strToken = user.getToken();

        String strOrderId = orderData.getId();
        String strDropAddress = LAOXI.currentRide.getDropOffAddress();
        String strDropLat = String.valueOf(LAOXI.currentRide.getDropOffLatitude());
        String strDropLng = String.valueOf(LAOXI.currentRide.getDropOffLongitude());

        DebugLog.i("Update DropOff Request");
        ApiService api = RetroClient.getApiService();
        Call<Void> call = api.doEnterDropOff(strToken, strOrderId, strDropAddress, strDropLat, strDropLng);

        APIRequestManager<Void> apiRequestManager = new APIRequestManager<>(new APIRequestManager.ResponseListener<Void>() {
            @Override
            public void onResponse(retrofit2.Response<Void> response) {
                AlertUtils.dismissDialog();
                if (response.code() == 200) {
                    DebugLog.i("Update DropOff Success" + response.message());
                    btnContinue.setVisibility(View.INVISIBLE);
                    btnEnterDropOffLocation.setEnabled(false);
                    /*GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(getActivity(), "Thanks! drop off address received", null);*/
                    DataToPref.setSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.IS_PROVIDE_DROPOFF, LaoxiConstant.IS_PROVIDE_DROPOFF_DATA, "true");
                    navigatorInterface.openLetsGoFragment();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                AlertUtils.dismissDialog();
                DebugLog.i("Update DropOff Request Failed" + throwable.getMessage());
            }
        }, call);
        apiRequestManager.execute();

    }

    /**
     * Set a parameter for call a update drop off api.
     */

    public HashMap<String, String> setDropoffData() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.ORDER_ID, orderData.getId());
        param.put(LaoxiConstant.DROPOFF_ADDRESS, LAOXI.currentRide.getDropOffAddress());
        param.put(LaoxiConstant.DROPOFF_LATITUDE, String.valueOf(LAOXI.currentRide.getDropOffLatitude()));
        param.put(LaoxiConstant.DROPOFF_LONGITUDE, String.valueOf(LAOXI.currentRide.getDropOffLongitude()));
        return param;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void startRing() {
       /* try {
            mp = MediaPlayer.create(getActivity(), R.raw.car_horn_onetime);
            mp.setLooping(false);
            mp.seekTo(0);
            mp.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void stopRing() {

        /*try {
            if (mp != null)
                mp.stop();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }*/
    }

    public void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}// end of main class
