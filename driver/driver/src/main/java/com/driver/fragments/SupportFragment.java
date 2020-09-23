package com.driver.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.driver.R;
import com.driver.activity.HomeActivity;
import com.driver.customclasses.CustomEditText;
import com.driver.customclasses.CustomTextView;
import com.driver.dao.IAsyncCompleteCallback;
import com.driver.dao.ParsingHelper;
import com.driver.dao.homedao.HomeActivityDAO;
import com.driver.dialog.GeneralStaticAlertDialog;
import com.driver.navigator.NavigatorInterface;
import com.driver.pojo.GenericResponse;
import com.driver.pojo.TrackRidePojo.TrackRide;
import com.driver.retrofit.APIRequestManager;
import com.driver.retrofit.ApiService;
import com.driver.retrofit.RetroClient;
import com.driver.util.AlertUtils;
import com.driver.util.AppHelper;
import com.driver.util.ConstantClass;
import com.driver.util.DebugLog;
import com.driver.util.DialogDismissOneButton;

import java.io.IOException;
import java.util.HashMap;
import java.util.zip.CheckedOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;
import retrofit2.Call;

/**
 * Created by hitesh on 20/7/16.
 */
public class SupportFragment extends BaseFragment implements APIRequestManager.ResponseListener<GenericResponse> {


    @Bind(R.id.header_menu)
    ImageView headerMenu;
    @Bind(R.id.header_text)
    CustomTextView headerText;

    @Bind(R.id.editTextName)
    CustomEditText editTextName;
    @Bind(R.id.editTextEmailAddress)
    CustomEditText editTextEmailAddress;
    @Bind(R.id.editTextEmailMessage)
    CustomEditText editTextEmailMessage;
    @Bind(R.id.tIName)
    TextInputLayout tIName;
    @Bind(R.id.tIEmail)
    TextInputLayout tIEmail;
    @Bind(R.id.tIEmailMessage)
    TextInputLayout tIEmailMessage;
    @Bind(R.id.relative_main)
    RelativeLayout relativeMain;
    @Bind(R.id.txtSend)
    CustomTextView txtSend;
    NavigatorInterface navigatorInterface;
    private HomeActivity homeactivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.support_layout, container, false);
        ButterKnife.bind(this, view);

        headerText.setText("Support");
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        navigatorInterface.closeDrawer();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigatorInterface = (NavigatorInterface) context;
        if (context instanceof HomeActivity) {
            homeactivity = (HomeActivity) context;
        }
    }

    /**
     * Validations for all text field.
     */

    private boolean validate() {
        if (isEditTextEmpty(editTextName)) {
            showSnackBar("Please enter name", editTextName, relativeMain);
            return false;
        } else if (isEditTextEmpty(editTextEmailAddress)) {
            showSnackBar("Please enter email address", editTextEmailAddress, relativeMain);
            return false;
        } else if (!isValidEmail(editTextEmailAddress)) {
            showSnackBar("Please enter valid email address", editTextEmailAddress, relativeMain);
            return false;
        } else if (isEditTextEmpty(editTextEmailMessage)) {
            showSnackBar("Please enter message", editTextEmailMessage, relativeMain);
            return false;
        } else {
            return true;
        }
    }

    /**
     * Call a support api.
     */

    private void callSupportWS() {

        AlertUtils.showCustomProgressDialog(getContext());

        String strToken = AppHelper.getInstance().getToken();
        String strUserId = AppHelper.getInstance().getDriverId();
        String strName = editTextName.getText().toString().trim();
        String strEmail = editTextEmailAddress.getText().toString().trim();
        String strUserType = "driver";
        String strMessage = editTextEmailMessage.getText().toString().trim();

        DebugLog.i("Online Support Request");
        ApiService api = RetroClient.getApiService();
        Call<GenericResponse> call = api.callOnlineSupportWS(strToken, strUserId, strName, strEmail, strMessage, strUserType);

        APIRequestManager<GenericResponse> apiRequestManager = new APIRequestManager<>( this, call);
        apiRequestManager.execute();
    }

    @OnClick({R.id.header_menu, R.id.txtSend})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_menu:
                HomeActivity.toggleDrawer();
                break;
            case R.id.txtSend:
                if (validate()) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            callSupportWS();
                        }
                    });

                }
                break;
        }
    }

    @Override
    public void getUpdateLatLongWsResponseFromService(TrackRide pojo) {

    }

    @Override
    public void drawerLock() {
        navigatorInterface.drawerLock(false);
    }

    @Override
    public void onResponse(retrofit2.Response<GenericResponse> response) {
        AlertUtils.dismissDialog();
        if (response.isSuccessful()) {
            DebugLog.i("Support Response Success Code :" + response.code());

            if (response.code() == ConstantClass.RESPONSE_CODE_200) {
                navigatorInterface.openHomeFragment();
                GenericResponse res = response.body();
                GeneralStaticAlertDialog.getInstance().createDialogWithOneButton(getActivity(), "Thank you !", res.getMessage() + "", new DialogDismissOneButton() {
                                        @Override
                                        public void onDismiss() {
                                            homeactivity.onBackPressed();
                                        }
                                    });
            }
        } else {
            DebugLog.i("Support Response Failed Code :" + response.code());
            try {
                String s = response.errorBody().string();
                if ( response.code() == ConstantClass.RESPONSE_CODE_401 ) {
                    ((HomeActivity) getActivity()).callLogoutWS();
                } else if (response.code() == ConstantClass.RESPONSE_CODE_404) {
                    GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                    homeactivity.showSnackbar(responseBody.getMessage());
                } else {
                    GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                    homeactivity.showSnackbar(responseBody.getMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        AlertUtils.dismissDialog();
        DebugLog.e("Support Request Failed :" + throwable.getMessage());
        homeactivity.showSnackbar(getString(R.string.bad_server));
    }
}
