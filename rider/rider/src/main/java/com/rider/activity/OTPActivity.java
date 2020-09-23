package com.rider.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rider.LAOXI;
import com.rider.R;
import com.rider.customclasses.CustomButton;
import com.rider.customclasses.CustomEditText;
import com.rider.customclasses.CustomTextView;
import com.rider.dialog.AlertUtils;
import com.rider.dialog.GeneralStaticAlertDialog;
import com.rider.pojo.GenericResponse;
import com.rider.pojo.User;
import com.rider.retrofit.APIRequestManager;
import com.rider.retrofit.ApiService;
import com.rider.retrofit.RetroClient;
import com.rider.utils.DataToPref;
import com.rider.utils.DebugLog;
import com.rider.utils.DialogDismissOneButton;
import com.rider.utils.LaoxiConstant;
import com.rider.utils.ParsingHelper;

import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by hitesh on 19/7/16.
 */
public class OTPActivity extends BaseActivity implements TextWatcher, View.OnKeyListener {


    @Bind(R.id.header_back)
    ImageView headerBack;
    @Bind(R.id.header_text)
    CustomTextView headerText;
    @Bind(R.id.editTextOtpFirst)
    CustomEditText editTextOtpFirst;
    @Bind(R.id.editTextOtpSecond)
    CustomEditText editTextOtpSecond;
    @Bind(R.id.editTextOtpThird)
    CustomEditText editTextOtpThird;
    @Bind(R.id.editTextOtpFourth)
    CustomEditText editTextOtpFourth;
    @Bind(R.id.textViewResendOtp)
    CustomTextView textViewResendOtp;
    @Bind(R.id.btn_editProfile)
    CustomButton btnEditProfile;
    @Bind(R.id.linearmain)
    LinearLayout linearmain;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_screen);
        ButterKnife.bind(this);
        LAOXI.setCurrentActivity(this);


        try {
            String strUserData = DataToPref.getSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA);
            Gson mGson = new Gson();
            user = mGson.fromJson(strUserData, User.class);

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        headerText.setText("verification");

        editTextOtpFirst.addTextChangedListener(this);
        editTextOtpSecond.addTextChangedListener(this);
        editTextOtpThird.addTextChangedListener(this);
        editTextOtpFourth.addTextChangedListener(this);
        editTextOtpThird.setOnKeyListener(this);
        editTextOtpFourth.setOnKeyListener(this);
        editTextOtpSecond.setOnKeyListener(this);

    }// end of onCreate

    @Override
    protected void onResume() {
        super.onResume();
        LAOXI.currentActivity = OTPActivity.this;

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    public boolean isEditTextEmpty(EditText editText) {
        if (editText.getText().toString().trim().isEmpty())
            return true;
        return false;
    }


    @OnClick({R.id.header_back, R.id.textViewResendOtp, R.id.btn_editProfile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_back:
                Intent intent = new Intent(OTPActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.textViewResendOtp:
                wsCallResendOTP();

                break;
            case R.id.btn_editProfile:
                if (isEditTextEmpty(editTextOtpFirst) || isEditTextEmpty(editTextOtpSecond) || isEditTextEmpty(editTextOtpThird) || isEditTextEmpty(editTextOtpFourth)) {
                    showSnackBar("Please enter OTP", editTextOtpFirst);
                }
                else
                {
                    wsCallVerifyOTP();
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (editTextOtpFirst.getText().length() > 0 && !isEditTextEmpty(editTextOtpFirst)) {
            editTextOtpSecond.requestFocus();
        }
        if (editTextOtpSecond.getText().length() > 0 && !isEditTextEmpty(editTextOtpSecond)) {
            editTextOtpThird.requestFocus();
        }
        if (editTextOtpThird.getText().length() > 0 && !isEditTextEmpty(editTextOtpThird)) {
            editTextOtpFourth.requestFocus();
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (v.getId() == R.id.editTextOtpThird) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    editTextOtpSecond.requestFocus();
                    return true;
                }
            }
        }
        if (v.getId() == R.id.editTextOtpFourth) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    editTextOtpThird.requestFocus();
                    return true;
                }
            }
        }
        if (v.getId() == R.id.editTextOtpSecond) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    editTextOtpFirst.requestFocus();
                    return true;
                }
            }
        }

        return false;
    }

    private void showSnackBar(String message, final EditText editText) {

        Snackbar snackbar = Snackbar
                .make(linearmain, message, Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editText != null) {
                            editText.requestFocus();
                            editText.setSelection(editText.getText().length());
                        }
                    }
                });
        snackbar.show();
    }

    /**
     * Call  a verify otp api.
     */

    public void wsCallVerifyOTP() {
        AlertUtils.showCustomProgressDialog(OTPActivity.this);

        String otp = editTextOtpFirst.getText().toString()+editTextOtpSecond.getText().toString()+editTextOtpThird.getText().toString()+editTextOtpFourth.getText().toString();
        String strUserId = user.getId();

        String deviceid = FirebaseInstanceId.getInstance().getToken();
        DebugLog.e("Device id is a " + deviceid);
        String strVersion = "";
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            String versionName = info.versionName;
            int versionCode = info.versionCode;
            strVersion = versionName + " (" + versionCode + ")";
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }

        ApiService api = RetroClient.getApiService();
        Call<User> call = api.doVerifyOTP("", otp, strUserId, deviceid, "A", strVersion);

        APIRequestManager<User> apiRequestManager = new APIRequestManager<>(new APIRequestManager.ResponseListener<User>() {
            @Override
            public void onResponse(retrofit2.Response<User> response) {
                AlertUtils.dismissDialog();
                if ( response.isSuccessful() ) {
                    if (response.code() == 200) {
                        User user = response.body();
                        String data = new Gson().toJson(user);

                        DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA, data);
                        DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.ISLOGIN, "true");
                        DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.HEADER_TOCKEN, user.getToken());
                        DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.USER_ID, user.getId());

                        DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.IS_FIRST_TIME, LaoxiConstant.IS_FIRST_TIME_DATA, "true");

                        GeneralStaticAlertDialog.getInstance().createDialogWithOneButtonYellow(OTPActivity.this, "", user.getMessage(), new DialogDismissOneButton() {
                            @Override
                            public void onDismiss() {
                                Intent intent = new Intent(OTPActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                } else {
                    if (response.code() == 400) {
                        try {
                            String s = response.errorBody().string();
                            GenericResponse genericResponse = ParsingHelper.getInstance().genericResponseParsing(s);
                            showSnackBar(genericResponse.getMessage(), editTextOtpFirst);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else if (response.code() == 404) {

                        try {
                            String s = response.errorBody().string();
                            GenericResponse genericResponse = ParsingHelper.getInstance().genericResponseParsing(s);
                            showSnackBar(genericResponse.getMessage(), editTextOtpFirst);
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

    public HashMap<String, String> setOTPdata() {
        HashMap<String, String> param = new HashMap<>();
        param.put("otp", editTextOtpFirst.getText().toString()+editTextOtpSecond.getText().toString()+editTextOtpThird.getText().toString()+editTextOtpFourth.getText().toString());
        param.put(LaoxiConstant.USER_ID,user.getId());

        String deviceid = FirebaseInstanceId.getInstance().getToken();
        DebugLog.e("Device id is a " + deviceid);
        if (deviceid != null) {
            param.put(LaoxiConstant.DEVICE_ID, deviceid);
            param.put(LaoxiConstant.DEVICE_TYPE, "A");
        }

        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            String versionName = info.versionName;
            int versionCode = info.versionCode;
            param.put(LaoxiConstant.APP_VERSION, versionName + " (" + versionCode + ")");
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }

        return param;
    }


    /**
     * Call a resend otp api.
     */

    public void wsCallResendOTP() {
        AlertUtils.showCustomProgressDialog(OTPActivity.this);

        ApiService api = RetroClient.getApiService();
        Call<GenericResponse> call = api.doResendOTP("", user.getId());

        APIRequestManager<GenericResponse> apiRequestManager = new APIRequestManager<>(new APIRequestManager.ResponseListener<GenericResponse>() {
            @Override
            public void onResponse(retrofit2.Response<GenericResponse> response) {
                AlertUtils.dismissDialog();
                if ( response.isSuccessful() ) {
                    if (response.code() == 200) {
                        GenericResponse genericResponse = response.body();
                        showSnackBar(genericResponse.getMessage(), editTextOtpFirst);
                    }
                } else {
                    if (response.code() == 400) {
                        try {
                            String s = response.errorBody().string();
                            GenericResponse genericResponse = ParsingHelper.getInstance().genericResponseParsing(s);
                            showSnackBar(genericResponse.getMessage(), editTextOtpFirst);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else if (response.code() == 404) {

                        try {
                            String s = response.errorBody().string();
                            GenericResponse genericResponse = ParsingHelper.getInstance().genericResponseParsing(s);
                            showSnackBar(genericResponse.getMessage(), editTextOtpFirst);
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

    public HashMap<String, String> setresendOTPdata() {


        HashMap<String, String> param = new HashMap<>();

        param.put(LaoxiConstant.USER_ID,user.getId());
        return param;
    }


}// end of class
