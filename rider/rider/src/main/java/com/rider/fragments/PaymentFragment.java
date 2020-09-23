package com.rider.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rider.R;
import com.rider.activity.HomeActivity;
import com.rider.adapter.MultiplCardAdapter;
import com.rider.customclasses.CustomEditText;
import com.rider.customclasses.CustomTextView;
import com.rider.dialog.AlertUtils;
import com.rider.dialog.GeneralStaticAlertDialog;
import com.rider.navigator.NavigatorInterface;
import com.rider.pojo.CardInfo;
import com.rider.pojo.GenericResponse;
import com.rider.pojo.User;
import com.rider.retrofit.APIRequestManager;
import com.rider.retrofit.ApiService;
import com.rider.retrofit.RetroClient;
import com.rider.utils.DataToPref;
import com.rider.utils.DebugLog;
import com.rider.utils.DialogDismissOneButton;
import com.rider.utils.FourDigitCardFormatWatcher;
import com.rider.utils.LaoxiConstant;
import com.rider.utils.ParsingHelper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by hitesh on 20/7/16.
 */
public class PaymentFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.header_menu)
    ImageView headerMenu;
    @Bind(R.id.header_text)
    CustomTextView headerText;
    @Bind(R.id.img_share)
    ImageView imgShare;

    NavigatorInterface navigatorInterface;
    int day1 = 0, month1 = 0, year1 = 0;


    @Bind(R.id.imageViewVisa)
    ImageView imageViewVisa;
    @Bind(R.id.edtCardNumber)
    CustomEditText edtCardNumber;
    @Bind(R.id.edtCardHolderName)
    CustomEditText edtCardHolderName;
    @Bind(R.id.tvExpiryDate)
    CustomTextView tvExpiryDate;
    @Bind(R.id.edtCvv)
    CustomEditText edtCvv;
    @Bind(R.id.tvAddCard)
    CustomTextView tvAddCard;
    @Bind(R.id.edtAddress)
    CustomEditText edtAddress;
    @Bind(R.id.et_city)
    CustomEditText etCity;
    @Bind(R.id.ed_country)
    CustomEditText edCountry;
    @Bind(R.id.recycleViewCardList)
    RecyclerView recycleViewCardList;
    @Bind(R.id.scrollViewCardDetails)
    ScrollView scrollViewCardDetails;
    @Bind(R.id.line)
    ImageView line;
    @Bind(R.id.tvaddnewCard)
    CustomTextView tvaddnewCard;
    @Bind(R.id.relative_main)
    LinearLayout relativeMain;
    @Bind(R.id.linerdetailsofcard)
    LinearLayout linerdetailsofcard;
    @Bind(R.id.imageViewCancel)
    ImageView imageViewCancel;

    private SimpleDateFormat dateFormatter;
    private User user;
    List<CardInfo> cardInfos;
    int month, year;
    private LinearLayoutManager mLayoutManager;
    String a;
    int keyDel;
    MultiplCardAdapter multiplCardAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payment_layout, container, false);
        ButterKnife.bind(this, view);

        dateFormatter = new SimpleDateFormat("MM / yyyy/dd", Locale.getDefault());

        String strUserData = DataToPref.getSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA);
        Gson mGson = new Gson();
        user = mGson.fromJson(strUserData, User.class);

        edtCardNumber.addTextChangedListener(new FourDigitCardFormatWatcher());
        imgShare.setVisibility(View.GONE);
        headerText.setText(R.string.payment);
        tvAddCard.setOnClickListener(this);
        tvExpiryDate.setOnClickListener(this);
        imageViewCancel.setOnClickListener(this);

        tvExpiryDate.setText(getString(R.string._01_26) + " ");
        mLayoutManager = new LinearLayoutManager(getContext());
        cardInfos = user.getCardInfo();
        multiplCardAdapter = new MultiplCardAdapter(cardInfos, getActivity());
        recycleViewCardList.setLayoutManager(mLayoutManager);
        recycleViewCardList.setAdapter(multiplCardAdapter);


        return view;
    }

    @OnClick(R.id.header_menu)
    public void onClick() {

        HomeActivity.toggleDrawer();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigatorInterface = (NavigatorInterface) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navigatorInterface.closeDrawer();


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAddCard:
                // navigatorInterface.stopTrackRideService();

                if (linerdetailsofcard.isShown()) {
                    validate();
                } else {
                    linerdetailsofcard.setVisibility(View.VISIBLE);
                    tvaddnewCard.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    imageViewCancel.setVisibility(View.VISIBLE);
                    //validate();
                }


                break;


            case R.id.tvExpiryDate:
                createDialogWithoutDateField().show();
                break;

            case R.id.imageViewCancel:
                closeKeyboard(getActivity());
                linerdetailsofcard.setVisibility(View.GONE);
                tvaddnewCard.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
                imageViewCancel.setVisibility(View.GONE);

                break;
        }
    }

    private void validate() {
        if (isEditTextEmpty(edtCardNumber)) {
            showSnackBar("Please enter card number", edtCardNumber, relativeMain);
        } else if (isEditTextEmpty(edtCardHolderName)) {
            showSnackBar("Please enter card holder name", edtCardHolderName, relativeMain);
        }else if (!edtCardHolderName.getText().toString().trim().contains(" ")){
            showSnackBar("Please enter card holder name in two word", edtCardHolderName, relativeMain);
        } else if (isEditTextEmpty(tvExpiryDate)) {
            showSnackBar("Please select expiry date", tvExpiryDate, relativeMain);
        } else if (isEditTextEmpty(edtCvv)) {
            showSnackBar("Please enter CVC number", edtCvv, relativeMain);
        } else if (edtCvv.getText().toString().trim().length() < 3) {
            showSnackBar("Please enter valid CVC number", edtCvv, relativeMain);
        } else if (isEditTextEmpty(edtAddress)) {
            showSnackBar("Please enter address", edtCvv, relativeMain);
        } else if (isEditTextEmpty(etCity)) {
            showSnackBar("Please enter city", edtCvv, relativeMain);
        } else if (isEditTextEmpty(edCountry)) {
            showSnackBar("Please enter country", edtCvv, relativeMain);
        }
        else {

            wsCallAddNewCard();

        }
    }


    public void closeKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    private DatePickerDialog createDialogWithoutDateField() {

        {
            final Calendar newCalendar = Calendar.getInstance();
            if (year1 == 0) {
                year1 = newCalendar.get(Calendar.YEAR);
            }
            if (month1 == 0) {
                month1 = newCalendar.get(Calendar.MONTH);
            }
            if (day1 == 0) {
                day1 = newCalendar.get(Calendar.DAY_OF_MONTH);
            }

            DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {

                    year1 = selectedYear;
                    month1 = monthOfYear;
                    day1 = dayOfMonth;
                    Calendar newDate = Calendar.getInstance();

                    int currentYear = newDate.get(Calendar.YEAR);
                    int currentMonth = newDate.get(Calendar.MONTH);

                    newDate.set(selectedYear, monthOfYear, dayOfMonth);

                    if (selectedYear > currentYear) {

                        year = selectedYear;
                        month = monthOfYear;
                        String dateToSet = dateFormatter.format(newDate.getTime()).toString();
                        dateToSet = dateToSet.substring(0, dateToSet.length() - 3);

                        tvExpiryDate.setText(dateToSet.toString());

                    } else if (selectedYear == currentYear) {
                        // selected year is same or less
                        // now check for month
                        //6 > 1
                        if (monthOfYear >= currentMonth) {
                            // it means
                            year = selectedYear;
                            String dateToSet = dateFormatter.format(newDate.getTime()).toString();
                            month = monthOfYear;
                            DebugLog.e("year and month is a" + dateToSet);

                            dateToSet = dateToSet.substring(0, dateToSet.length() - 3);
                            tvExpiryDate.setText("" + dateToSet.toString());

                        } else {
                            Toast.makeText(getActivity(), "You can not select previous date.", Toast.LENGTH_LONG).show();
                            tvExpiryDate.setText("");
                        }
                    } else {
                        //selected year is lesser than current year so display toast
                        Toast.makeText(getActivity(), "You can not select previous date.", Toast.LENGTH_LONG).show();
                        tvExpiryDate.setText("");
                    }

                }
            }, year1, month1, day1);

            dpd.getDatePicker().setMinDate(newCalendar.getTimeInMillis());

            //	=========================================================================
            try {

                Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
                for (Field datePickerDialogField : datePickerDialogFields) {
                    if (datePickerDialogField.getName().equals("mDatePicker")) {
                        datePickerDialogField.setAccessible(true);
                        DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);

                        Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                        for (Field datePickerField : datePickerFields) {
                            //						Log.i("test", datePickerField.getName());
                            if ("mDaySpinner".equals(datePickerField.getName())) {
                                datePickerField.setAccessible(true);
                                Object dayPicker = new Object();
                                dayPicker = datePickerField.get(datePicker);
                                ((View) dayPicker).setVisibility(View.GONE);
                            }
                        }
                    }
                }

                ((ViewGroup) dpd.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            dpd.getDatePicker().setSpinnersShown(true);
            dpd.getDatePicker().setCalendarViewShown(false);
            return dpd;

        }
    }


    public void wsCallAddNewCard() {
        AlertUtils.showCustomProgressDialog(getContext());

        String strToken = user.getToken();
        String strUserId = user.getId();
        String strCVV = edtCvv.getText().toString();
        String strCardName = edtCardHolderName.getText().toString();
        String strCardNum = edtCardNumber.getText().toString();
        String strMonth = String.valueOf(month+1);
        String strYear = String.valueOf(year);
        String strAddress = edtAddress.getText().toString();
        String strCity = etCity.getText().toString();
        String strCountry = edCountry.getText().toString();
        DebugLog.i("Call Add New Card");
        ApiService api = RetroClient.getApiService();
        Call<ArrayList<CardInfo>> call = api.wsCallAddNewCard(strToken, strUserId, strCVV, strCardName, strCardNum, strMonth, strYear, strAddress, strCity, strCountry);

        APIRequestManager<ArrayList<CardInfo>> apiRequestManager = new APIRequestManager<>(new APIRequestManager.ResponseListener<ArrayList<CardInfo>>() {
            @Override
            public void onResponse(retrofit2.Response<ArrayList<CardInfo>> response) {
                AlertUtils.dismissDialog();

                if ( response.isSuccessful() ) {
                    DebugLog.i("Add Card Success Response : " + response.body() + "Response Code : " + response.code());

                    if ( response.code() == 201 ) {
                        linerdetailsofcard.setVisibility(View.GONE);
                        tvaddnewCard.setVisibility(View.GONE);
                        line.setVisibility(View.GONE);
                        imageViewCancel.setVisibility(View.GONE);


                        cardInfos.clear();
                        cardInfos = response.body();
                        user.setCardInfo(cardInfos);
                        multiplCardAdapter.updateData(cardInfos);

                        String s = new Gson().toJson(user);
                        DataToPref.setSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA, s);

                        GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(getActivity(), getString(R.string.you_register_card), new DialogDismissOneButton() {
                            @Override
                            public void onDismiss() {
                                getActivity().onBackPressed();
                            }
                        });
                    }
                } else {
                    DebugLog.w("Add Card Response Failed");
                    if (response.code() == 400) {
                        try {
                            String s = response.errorBody().string();
                            GenericResponse genericResponse = ParsingHelper.getInstance().genericResponseParsing(s);
                            GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(getActivity(), genericResponse.getMessage(), null);
                        } catch (IOException e) {
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


    public HashMap<String, String> setCardData() {
        HashMap<String, String> param = new HashMap<>();

        param.put(LaoxiConstant.USER_ID, user.getId());
        param.put(LaoxiConstant.CVV, edtCvv.getText().toString());
        param.put(LaoxiConstant.CARD_HOLDER_NAME, edtCardHolderName.getText().toString());
        param.put(LaoxiConstant.CARD_NUMBER, edtCardNumber.getText().toString());
        param.put(LaoxiConstant.MONTH, String.valueOf(month+1));
        param.put(LaoxiConstant.YEAR, String.valueOf(year));
        param.put(LaoxiConstant.ADDRESS_LINE_1, edtAddress.getText().toString());
        param.put(LaoxiConstant.CITY, etCity.getText().toString());
        param.put(LaoxiConstant.COUNTRY, edCountry.getText().toString());
        return param;
    }


}// end of main class
