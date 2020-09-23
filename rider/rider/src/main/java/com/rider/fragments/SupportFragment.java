package com.rider.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import com.rider.retrofit.APIRequestManager;
import com.rider.retrofit.ApiService;
import com.rider.retrofit.RetroClient;
import com.rider.utils.DataToPref;
import com.rider.utils.DebugLog;
import com.rider.utils.DialogDismissOneButton;
import com.rider.utils.LaoxiConstant;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by hitesh on 20/7/16.
 */
public class SupportFragment extends BaseFragment implements View.OnClickListener {

    NavigatorInterface navigatorInterface;
    @Bind(R.id.header_menu)
    ImageView headerMenu;
    @Bind(R.id.header_text)
    CustomTextView headerText;
    @Bind(R.id.img_share)
    ImageView imgShare;
    @Bind(R.id.edtFullName)
    CustomEditText edtFullName;
    @Bind(R.id.edtEmail)
    CustomEditText edtEmail;
    @Bind(R.id.edtMessage)
    CustomEditText edtMessage;
    @Bind(R.id.tvSend)
    CustomTextView tvSend;
    @Bind(R.id.relative_main)
    LinearLayout relativeMain;

    private User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.support_layout, container, false);
        ButterKnife.bind(this, view);

        Gson mGson = new Gson();
        String strUserData = DataToPref.getSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA);
        user = mGson.fromJson(strUserData, User.class);

        imgShare.setVisibility(View.GONE);
        headerText.setText("support");
        tvSend.setOnClickListener(this);

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

    /**
     * Check validations .
     */

    private void validate() {
        if (isEditTextEmpty(edtFullName)) {
            showSnackBar("Please enter name", edtFullName, relativeMain);
        } else if (isEditTextEmpty(edtEmail)) {
            showSnackBar("Please enter email address", edtEmail, relativeMain);
        } else if (!isValidEmail(edtEmail)) {
            showSnackBar("Please enter valid email address", edtEmail, relativeMain);
        } else if (isEditTextEmpty(edtMessage)) {
            showSnackBar("Please enter message", edtMessage, relativeMain);
        } else {

            HashMap<String, String> param = new HashMap<>();
            param.put(LaoxiConstant.USER_ID, user.getId());
            param.put(LaoxiConstant.USER_TYPE, "user");
            param.put("name", edtFullName.getText().toString());
            param.put("email", edtEmail.getText().toString());
            param.put("message", edtMessage.getText().toString());

            wsCallSupport(param);


        }
    }


    /**
     * Call a support api.
     */

    public void wsCallSupport(HashMap<String, String> param) {
        AlertUtils.showCustomProgressDialog(getActivity());

        String strToken = user.getToken();
        String strUserId = user.getId();
        String strUserType = "user";
        String strName = edtFullName.getText().toString();
        String strEmail = edtEmail.getText().toString();
        String strMessage = edtMessage.getText().toString();

        ApiService api = RetroClient.getApiService();
        Call<GenericResponse> call = api.wsCallSupport(strToken, strUserId, strUserType, strName, strEmail, strMessage);

        APIRequestManager<GenericResponse> apiRequestManager = new APIRequestManager<>(new APIRequestManager.ResponseListener<GenericResponse>() {
            @Override
            public void onResponse(retrofit2.Response<GenericResponse> response) {
                AlertUtils.dismissDialog();

                if ( response.isSuccessful() ) {
                    GenericResponse genericResponse = response.body();

                    DebugLog.i("Online Support Success Response" + genericResponse);
                    GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(getActivity(), "We successfully posted your message to support team", new DialogDismissOneButton() {
                        @Override
                        public void onDismiss() {
                            getActivity().onBackPressed();
                        }
                    });
                } else {
                    DebugLog.e("Online Support Failed Response " + response.code());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                AlertUtils.dismissDialog();
                DebugLog.i("Online Support Request Failed "+ throwable.getMessage());
            }
        }, call);
        apiRequestManager.execute();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSend:
                validate();
                break;
        }
    }
}
