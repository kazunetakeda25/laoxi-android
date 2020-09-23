package com.rider.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.rider.R;
import com.rider.activity.HomeActivity;
import com.rider.customclasses.CustomEditText;
import com.rider.customclasses.CustomTextView;
import com.rider.dialog.AlertUtils;
import com.rider.dialog.GeneralStaticAlertDialog;
import com.rider.navigator.NavigatorInterface;
import com.rider.pojo.GenericResponse;
import com.rider.pojo.User;
import com.rider.retrofit.ApiService;
import com.rider.retrofit.RetroClient;
import com.rider.utils.DataToPref;
import com.rider.utils.DebugLog;
import com.rider.utils.DialogDismissOneButton;
import com.rider.utils.LaoxiConstant;
import com.rider.utils.ParsingHelper;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class RedeemCodeFragment extends BaseFragment implements View.OnClickListener {


    View view;
    NavigatorInterface navigatorInterface;

    @Bind(R.id.header_menu)
    ImageView headerMenu;
    @Bind(R.id.header_text)
    CustomTextView headerText;
    @Bind(R.id.img_share)
    ImageView imgShare;
    @Bind(R.id.edtPromoCode)
    CustomEditText edtPromoCode;
    @Bind(R.id.txtSubmitCode)
    CustomTextView txtSubmitCode;

    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_redeem_code, container, false);
        ButterKnife.bind(this, view);

        imgShare.setVisibility(View.GONE);
        headerText.setText("Redeem code");
        txtSubmitCode.setOnClickListener(this);

        Gson mGson = new Gson();
        String strUserData = DataToPref.getSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA);
        user = mGson.fromJson(strUserData, User.class);

        return view;
    }

    @OnClick(R.id.header_menu)
    public void onClick() {
        HomeActivity.toggleDrawer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtSubmitCode:
                submitPromoCode();
                break;
        }
    }

    private void submitPromoCode() {
        if (isEditTextEmpty(edtPromoCode)) {
            showSnackBar("Please enter code", edtPromoCode, view);
        } else {

            AlertUtils.showCustomProgressDialog(getActivity());

            String strToken = user.getToken();
            String strUserId = user.getId();
            DebugLog.i("Call Redeem Promo Code Request");
            ApiService api = RetroClient.getApiService();
            Call<GenericResponse> call = api.redeemPromoCode( strToken, strUserId, edtPromoCode.getText().toString());
            call.enqueue(new Callback<GenericResponse>() {
                @Override
                public void onResponse(Call<GenericResponse> call, retrofit2.Response<GenericResponse> response) {
                    AlertUtils.dismissDialog();

                    DebugLog.i("Redeem PromoCode Success Response : " + response.body());
                    if ( response.isSuccessful() ) {
                        GenericResponse genericResponse = response.body();

                        GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(getActivity(), genericResponse.getMessage(), new DialogDismissOneButton() {
                            @Override
                            public void onDismiss() {
                                RedeemCodeFragment.this.edtPromoCode.setText("");
                            }
                        });
                    } else {
                        DebugLog.e("Promocode Failed Response");
                        try {
                            String s = response.errorBody().string();

                            GenericResponse genericResponse = ParsingHelper.getInstance().genericResponseParsing(s);
                            GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(getActivity(), genericResponse.getMessage(), new DialogDismissOneButton() {
                            @Override
                            public void onDismiss() {
                                RedeemCodeFragment.this.edtPromoCode.setText("");
                            }
                        });

                            DebugLog.e("Redeem Response Error Message : " + genericResponse.getMessage());
                        } catch ( IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GenericResponse> call, Throwable t) {
                    DebugLog.e("Promocode Request Failed Msg :" + t.getMessage());
                    AlertUtils.dismissDialog();
                }
            });

        }
    }

}
