package com.rider.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.rider.LAOXI;
import com.rider.R;
import com.rider.customclasses.CustomButton;
import com.rider.customclasses.CustomTextView;
import com.rider.dialog.AlertUtils;
import com.rider.dialog.GeneralStaticAlertDialog;
import com.rider.navigator.CloseDriverDetailsDialogInterface;
import com.rider.navigator.NavigatorInterface;
import com.rider.pojo.DriverDetails;
import com.rider.pojo.GenericResponse;
import com.rider.pojo.User;
import com.rider.retrofit.ApiService;
import com.rider.retrofit.RetroClient;
import com.rider.utils.DataToPref;
import com.rider.utils.DebugLog;
import com.rider.utils.DialogDismissOneButton;
import com.rider.utils.LaoxiConstant;
import com.rider.utils.HomePageRefresh;
import com.rider.utils.ParsingHelper;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by  on 20/7/16.
 */
public class RideLaterFragment extends BaseFragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    NavigatorInterface navigatorInterface;
    @Bind(R.id.textViewDate)
    CustomTextView textViewDate;
    @Bind(R.id.textViewTime)
    CustomTextView textViewTime;
    @Bind(R.id.spinnerGender)
    Spinner spinnerGender;
    @Bind(R.id.tvEstimatedFare)
    CustomButton tvEstimatedFare;
    @Bind(R.id.txtLetsGo)
    CustomButton txtLetsGo;
    @Bind(R.id.imageViewCancel)
    ImageView imageViewCancel;


    private SimpleDateFormat dateFormatter;
    private DatePickerDialog firstMemberDatePickerDialog;

   HomePageRefresh homePageRefresh;

    public void setHomePageRefresh(HomePageRefresh homePageRefresh) {
        this.homePageRefresh = homePageRefresh;
    }
    // Time picker values

    static final int TIME_DIALOG_ID = 1111;
    private int hour;
    private int minute;

    Calendar now;
    int mMinute, mHours, mDay, mMonth, mYear;
    int selMinute, selHours, selDay, selMonth, selYear, mTimeSet;
    private String selectedDate;
    private String selectedTime;
    private User user;
    final ArrayList<String> listofGender = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rider_need_me_later_layout, container, false);
        ButterKnife.bind(this, view);
        dateFormatter = new SimpleDateFormat("dd / MM / yyyy", Locale.US);
        tvEstimatedFare.setOnClickListener(this);
        txtLetsGo.setOnClickListener(this);
        textViewDate.setOnClickListener(this);
        textViewTime.setOnClickListener(this);
        imageViewCancel.setOnClickListener(this);
        //setSpinnerItem();
        setDateTimeField();
        String strUserData = DataToPref.getSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA);
        Gson mGson = new Gson();
        user = mGson.fromJson(strUserData, User.class);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigatorInterface = (NavigatorInterface) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvEstimatedFare:
                validateForFareEstimation();
                break;
            case R.id.txtLetsGo:
                validate();
                break;


            case R.id.textViewDate:
//                createDialogWithoutDateField().show();
                openDatePicker();
                break;
            case R.id.textViewTime:
                onTimerViewOpen();

                break;

            case R.id.imageViewCancel:

                if(homePageRefresh!=null)
                {
                    homePageRefresh.refreshPage();
                    getActivity().onBackPressed();
                }
                else
                {
                    getActivity().onBackPressed();
                }
                //navigatorInterface.openHome();
                break;
        }
    }

    private void openDatePicker() {
        final Calendar now = Calendar.getInstance();
        mYear = now.get(Calendar.YEAR);
        mMonth = now.get(Calendar.MONTH);
        mDay = now.get(Calendar.DAY_OF_MONTH);
        mMonth = mMonth + 1;

        DebugLog.e(""+mYear+mMonth+mDay);

        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog, int year, int month, int date) {


                        int j = month + 1;

                        selDay = date;
                        selMonth = month + 1;
                        selYear = year;

                        DebugLog.e(""+selYear+selMonth+selDay);


                        //String mnth = String.format("%02d", j);
                        //   String mnth = new DateFormatSymbols().getMonths()[j]; // for String of month
                        String dt = String.format("%02d", date);
                        textViewTime.setText("");
                        textViewDate.setText(dt + " / " + j + " / " + year);
                        selectedDate = year + "-" + j + "-" + dt;
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMinDate(now);
        dpd.dismissOnPause(true);

        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    private String getTimeInAmPmFormat(String s) {
/*        String _24HourTime = s;
        Date _24HourDt = null;

        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
        try {
            _24HourDt = _24HourSDF.parse(_24HourTime);
            System.out.println(_24HourDt);
            System.out.println(_12HourSDF.format(_24HourDt));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _12HourSDF.format(_24HourDt);*/
        try {

            String _24HourTime = s.replace(" ", "");
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            System.out.println(_24HourSDF.format(_24HourDt));


            System.out.println(_12HourSDF.format(_24HourDt));
            return _12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

  /*  private void openTimePicker() {
        final Calendar now = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog();
        timePickerDialog.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                String time;
                selectedTime = hourOfDay + ":" + minute;
                time = getTimeInAmPmFormat(hourOfDay + " : " + minute);
                textViewTime.setText(time);
                DebugLog.e("utc time is a" + localToUTC(textViewDate.getText().toString() + " " + selectedTime));
            }



        });

        *//*timePickerDialog.setMinTime(
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND));
        now.add(Calendar.MINUTE, 60);*//*
        timePickerDialog.setStartTime(now.get(Calendar.HOUR_OF_DAY) + 1, now.get(Calendar.MINUTE));
        timePickerDialog.setTitle("Time");
        timePickerDialog.show(getActivity().getFragmentManager(), "Test");
    }*/

    public void onTimerViewOpen() {
        now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                RideLaterFragment.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );




        if (mYear == selYear && mDay == selDay && mMonth == selMonth) {

            tpd.setMinTime(
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    now.get(Calendar.SECOND));
            now.add(Calendar.MINUTE, 60);

            Date date=now.getTime();

            tpd.setMinTime(date.getHours(), date.getMinutes(), 0);



        }
        tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private void setDateTimeField() {

        final Calendar newCalendar = Calendar.getInstance();

        //		final int defaultYear = newCalendar.get(Calendar.YEAR)-18;
        //		final int defaultMonth = newCalendar.get(Calendar.MONTH);
        //		final int defaultDay = newCalendar.get(Calendar.DAY_OF_MONTH);

        firstMemberDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                textViewDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        firstMemberDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }

    private void validate() {
        if (isEditTextEmpty(textViewDate)) {
            showSnackBar("Please select date", textViewDate, textViewDate);
        } else if (isEditTextEmpty(textViewTime)) {
            showSnackBar("Please select time", textViewTime, textViewDate);
        } /*else if (spinnerGender.getSelectedItemPosition() <= 0) {
            showSnackBar("Please select driver type", null, textViewDate);
        } */ else {
            LAOXI.currentRide.setDate(textViewDate.getText().toString() + " " + selectedTime);
            LAOXI.currentRide.setGender("");
            wsCallPlaceOrder();
        }
    }


    private void validateForFareEstimation() {
        if (isEditTextEmpty(textViewDate)) {
            showSnackBar("Please select date", textViewDate, textViewDate);
        } else if (isEditTextEmpty(textViewTime)) {
            showSnackBar("Please select time", textViewTime, textViewDate);
        } /*else if (spinnerGender.getSelectedItemPosition() <= 0) {
            showSnackBar("Please select driver type", null, textViewDate);
        } */ else {
            LAOXI.currentRide.setDate(textViewDate.getText().toString() + " " + textViewTime.getText().toString());
            LAOXI.currentRide.setGender("");
            DriverDetails driverDetails = null;
            navigatorInterface.openFareEstimation(driverDetails, "", new CloseDriverDetailsDialogInterface() {
                @Override
                public void onClose() {

                }
            });

        }
    }

    public void wsCallPlaceOrder() {
        AlertUtils.showCustomProgressDialog(getContext());

        ApiService api = RetroClient.getApiService();

        String strToken = user.getToken();
        String strUserId = user.getId();
        String strCarTypeId = LAOXI.currentRide.getCarTypeId() != null ? LAOXI.currentRide.getCarTypeId() : "";
        String strDriverId = "";

        String strPickAddress = LAOXI.currentRide.getPickupAddress();
        String strPickLat = String.valueOf(LAOXI.currentRide.getPickLatitude());
        String strPickLng = String.valueOf(LAOXI.currentRide.getPickLongitude());
        String strDropAddress = LAOXI.currentRide.getDropOffAddress();
        String strDropLat = String.valueOf(LAOXI.currentRide.getDropOffLatitude());
        String strDropLng = String.valueOf(LAOXI.currentRide.getDropOffLongitude());

        String strTripType = LAOXI.currentRide.getTripType();
        String strTripTime = "";
        if (LAOXI.currentRide.getTripType().equalsIgnoreCase("later")) {
            strTripTime = localToUTC24(textViewDate.getText().toString() + " " + selectedTime);
        }
        String strGender = user.getGender();
        DebugLog.i("Call Place Order Later Request");
        Call<GenericResponse> call = api.doPlaceOrderLater(strToken, strUserId, strCarTypeId, strDriverId, strPickAddress, strPickLat, strPickLng, strDropAddress, strDropLat, strDropLng, strTripType, strTripTime, strGender);
        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, retrofit2.Response<GenericResponse> response) {
                AlertUtils.dismissDialog();

                DebugLog.i("PlaceOrder Later Success Response : " + response.body());
                if ( response.isSuccessful() ) {
                    if (response.code() == 201) {
                        GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(getActivity(), getString(R.string.ride_place_successfully), new DialogDismissOneButton() {
                            @Override
                            public void onDismiss() {
                                navigatorInterface.openHome();
                            }
                        });

                    }
                } else {
                    DebugLog.i("PlaceOrder Later Failed Response");
                    if (response.code() == 400) {

                        try {
                            String s = response.errorBody().string();
                            GenericResponse genericResponse = ParsingHelper.getInstance().genericResponseParsing(s);

                            if (genericResponse.getMessage().equalsIgnoreCase("Please enter card information on your account")) {
                                navigatorInterface.openPaymentDetails();
                            } else {
                                showSnackBar(genericResponse.getMessage(), null, textViewDate);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
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


    public HashMap<String, String> setOrderData() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.USER_ID, user.getId());
        param.put(LaoxiConstant.CAR_TYPE_ID, LAOXI.currentRide.getCarTypeId() != null ? LAOXI.currentRide.getCarTypeId() : "");
        param.put(LaoxiConstant.PICKUP_ADDRESS, LAOXI.currentRide.getPickupAddress());
        param.put(LaoxiConstant.DROPOFF_ADDRESS, LAOXI.currentRide.getDropOffAddress());
        param.put(LaoxiConstant.PICKUP_LATITUDE, String.valueOf(LAOXI.currentRide.getPickLatitude()));
        param.put(LaoxiConstant.PICKUP_LONGITUDE, String.valueOf(LAOXI.currentRide.getPickLongitude()));
        param.put(LaoxiConstant.DROPOFF_LATITUDE, String.valueOf(LAOXI.currentRide.getDropOffLatitude()));
        param.put(LaoxiConstant.DROPOFF_LONGITUDE, String.valueOf(LAOXI.currentRide.getDropOffLongitude()));
        param.put(LaoxiConstant.TRIP_TYPE, LAOXI.currentRide.getTripType());

        if (LAOXI.currentRide.getTripType().equalsIgnoreCase("later")) {

            param.put(LaoxiConstant.TRIPDATETIME, localToUTC24(textViewDate.getText().toString() + " " + selectedTime));
        }
        param.put(LaoxiConstant.DRIVER_ID, "");
        param.put(LaoxiConstant.GENDER, user.getGender());

        return param;
    }


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String time;
        selectedTime = hourOfDay + ":" + minute;
        time = getTimeInAmPmFormat(hourOfDay + " : " + minute);
        textViewTime.setText(time);
        DebugLog.e("utc time is a" + localToUTC(textViewDate.getText().toString() + " " + selectedTime));
        DateFormat inputFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat format = new SimpleDateFormat("hh:mm",Locale.ENGLISH);
        selectedTime=inputFormat.format(new Time(hourOfDay, minute, second));
    }




}// end of main class
