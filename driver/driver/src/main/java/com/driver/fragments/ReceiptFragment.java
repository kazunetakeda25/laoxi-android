package com.driver.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.driver.R;
import com.driver.customclasses.CustomButton;
import com.driver.customclasses.CustomTextView;
import com.driver.navigator.NavigatorInterface;
import com.driver.pojo.TrackRidePojo.TrackRide;
import com.driver.pojo.dropoffWs.DropOffResponse;
import com.driver.util.DebugLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hitesh on 20/7/16.
 */
public class ReceiptFragment extends BaseFragment {


    NavigatorInterface navigatorInterface;
    @Bind(R.id.header_menu)
    ImageView headerMenu;
    @Bind(R.id.header_text)
    CustomTextView headerText;

    @Bind(R.id.txtPickupAddress)
    CustomTextView txtPickupAddress;
    @Bind(R.id.txtDropoffAddress)
    CustomTextView txtDropoffAddress;
    @Bind(R.id.txtDistanceTime)
    CustomTextView txtDistanceTime;
    @Bind(R.id.txtDistanceKm)
    CustomTextView txtDistanceKm;
    @Bind(R.id.txtDriverName)
    CustomTextView txtDriverName;
    @Bind(R.id.txtTotalAmount)
    CustomTextView txtTotalAmount;
    @Bind(R.id.btnOk)
    CustomButton btnOk;


    public void setDropOffResponse(DropOffResponse dropOffResponse) {
        this.dropOffResponse = dropOffResponse;
    }

    private DropOffResponse dropOffResponse;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receipt_layout, container, false);
        ButterKnife.bind(this, view);
        headerMenu.setVisibility(View.GONE);
        headerText.setText("receipt");

        DebugLog.i("Reached Payment Screen");
        /**
         * Shows a receipt data.
         */

        try {
            txtPickupAddress.setText(dropOffResponse.getPickupAddress());
            txtDropoffAddress.setText(dropOffResponse.getDeliveryAddress());
            /*txtDistanceTime.setText(dropOffResponse.getTripDuration());*/
            txtDistanceTime.setText(setTime(dropOffResponse.getTripDuration()));
//            txtDistanceKm.setText(dropOffResponse.getDistance());
            txtDistanceKm.setText(dropOffResponse.getDistance());
            txtDriverName.setText(dropOffResponse.getUserData().getName());
            txtTotalAmount.setText("₭" + dropOffResponse.getTotalAmount());

        } catch (Exception e) {
            e.printStackTrace();
        }

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


    @OnClick({R.id.btnOk})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOk:
                FeedbackFragment feedbackFragment = new FeedbackFragment();
                feedbackFragment.setDropOffResponse(dropOffResponse);
                navigatorInterface.openFeedbackFragment(feedbackFragment);

                break;
        }
    }

    @Override
    public void getUpdateLatLongWsResponseFromService(TrackRide pojo) {

    }

    @Override
    public void drawerLock() {
        navigatorInterface.drawerLock(true);
    }

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
