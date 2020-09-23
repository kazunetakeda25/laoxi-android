package com.driver.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.driver.R;
import com.driver.application.LAOXI;
import com.driver.customclasses.CustomButton;
import com.driver.customclasses.CustomEditText;
import com.driver.customclasses.CustomTextView;
import com.driver.dao.IAsyncCompleteCallback;
import com.driver.dao.ParsingHelper;
import com.driver.dao.authenticationDAO.AuthenticationDAO;
import com.driver.dialog.GeneralStaticAlertDialog;
import com.driver.pojo.GenericResponse;
import com.driver.pojo.loginws.LoginResponse;
import com.driver.retrofit.APIRequestManager;
import com.driver.retrofit.ApiService;
import com.driver.retrofit.RetroClient;
import com.driver.util.AlertUtils;
import com.driver.util.AnalyticsReporter;
import com.driver.util.AppHelper;
import com.driver.util.AsteriskPasswordTransformationMethod;
import com.driver.util.ConstantClass;
import com.driver.util.DebugLog;
import com.driver.util.DialogDismissOneButton;
import com.driver.util.FLogManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;
import retrofit2.Call;

/**
 * Created by hitesh on 18/7/16.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, APIRequestManager.ResponseListener<LoginResponse> {


    @Bind(R.id.imageView2)
    ImageView imageView2;
    @Bind(R.id.edtEmail)
    CustomEditText edtEmail;
    @Bind(R.id.edtPassword)
    CustomEditText edtPassword;
    @Bind(R.id.tIGender)
    TextInputLayout tIGender;
    @Bind(R.id.textForgotPassword)
    CustomTextView textForgotPassword;
    @Bind(R.id.buttonLogin)
    CustomButton buttonLogin;
    @Bind(R.id.btnsignup)
    CustomButton buttonsignup;
    @Bind(R.id.buttonFB)
    CustomButton buttonFB;
    @Bind(R.id.lrBase)
    LinearLayout lrBase;
    @Bind(R.id.remember)
    CheckBox remember;
    @Bind(R.id.textVersion)
    CustomTextView textVersion;

    String versionName = "";
    int versionCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);

        textForgotPassword.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
        buttonsignup.setOnClickListener(this);
        edtPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        if (AppHelper.getInstance().getSharedPref(ConstantClass.LOGIN_USER_NAME, true) != null && !AppHelper.getInstance().getSharedPref(ConstantClass.LOGIN_USER_NAME, true).equalsIgnoreCase("")) {
            edtEmail.setText(AppHelper.getInstance().getSharedPref(ConstantClass.LOGIN_USER_NAME, true));
            edtPassword.setText(AppHelper.getInstance().getSharedPref(ConstantClass.LOGIN_USER_PASSWORD, true));
            remember.setChecked(true);
        }

        PackageInfo info;
        textVersion.setText("");
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            versionName = info.versionName;
            versionCode = info.versionCode;
            textVersion.setText("v" + info.versionName + " (" + info.versionCode + ")");
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        }
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
    protected void onResume() {
        super.onResume();
        LAOXI.setCurrentActivity(LoginActivity.this);
        LAOXI.currentActivity = LoginActivity.this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                validate();
                break;
            case R.id.btnsignup:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ConstantClass.SIGNUP_URL));
                startActivity(browserIntent);
                break;
            case R.id.textForgotPassword:

                GeneralStaticAlertDialog.getInstance().createDialogForgotPassword(LoginActivity.this, new DialogDismissOneButton() {
                    @Override
                    public void onDismiss() {
                        GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(LoginActivity.this, "Thank you ! We sent you a password on your registered email address.", null);
                    }
                }, this);
                break;
        }

    }

    /**
     * Validations for log in.
     */

    private void validate() {

        try {
            if (isEditTextEmpty(edtEmail)) {
                showSnackBar("Please enter email address", edtEmail);
            } else if (!isValidEmail(edtEmail.getText().toString().trim())) {
                showSnackBar("Please enter valid email address", edtEmail);
            } else if (isEditTextEmpty(edtPassword)) {
                showSnackBar("Please enter your password", edtPassword);
            } else {

                if (webServiceState == WebServiceState.ACTIVE) {

                    String device_id = FirebaseInstanceId.getInstance().getToken();
                    DebugLog.d("device_id-->" + device_id);
                    AlertUtils.showCustomProgressDialog(this);
                    DebugLog.i("Login Request");
                    ApiService api = RetroClient.getApiService();
                    Call<LoginResponse> call = api.logIn("", edtEmail.getText().toString(), edtPassword.getText().toString(), device_id != null ? device_id : "test", "A", versionName + " (" + versionCode + ")");

                    APIRequestManager<LoginResponse> apiRequestManager = new APIRequestManager<>(LoginActivity.this, call);
                    apiRequestManager.execute();
                } else {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(retrofit2.Response<LoginResponse> response) {
        AlertUtils.dismissDialog();
        if (response.isSuccessful()) {
            DebugLog.i("Login Success Response : " + response.code());

            AppHelper.getInstance().SaveInSharePrefWithBoolean(ConstantClass.IS_LOGIN, true);

            if (remember.isChecked()) {
                AppHelper.getInstance().SaveInSharedPref(ConstantClass.LOGIN_USER_NAME, edtEmail.getText().toString(), true);
                AppHelper.getInstance().SaveInSharedPref(ConstantClass.LOGIN_USER_PASSWORD, edtPassword.getText().toString(), true);
            } else {
                AppHelper.getInstance().SaveInSharedPref(ConstantClass.LOGIN_USER_NAME, "", true);
                AppHelper.getInstance().SaveInSharedPref(ConstantClass.LOGIN_USER_PASSWORD, "", true);
            }

            LoginResponse loginResponse = response.body();
            LAOXI.getInstance().setLoginUser(loginResponse);

            String s = new Gson().toJson(loginResponse);

            AppHelper.getInstance().SaveInSharedPref(ConstantClass.LOGIN_USER, s);

            AppHelper.getInstance().SaveInSharedPref(ConstantClass.HEADER_TOCKEN, loginResponse.getToken());
            AppHelper.getInstance().SaveInSharedPref(ConstantClass.DRIVER_ID, loginResponse.getDriverId());
//          rhc commented
            /*Crashlytics.setUserIdentifier(loginResponse.getDriverId());
            Crashlytics.setUserEmail(loginResponse.getEmail());
            Crashlytics.setUserName(loginResponse.getFname() + "" + loginResponse.getLname());
*/
            AnalyticsReporter.getInstance().setIdentify(loginResponse.getDriverId());
            AnalyticsReporter.getInstance().setProfile(
                    loginResponse.getDriverId(),
                    loginResponse.getEmail(),
                    loginResponse.getFname(),
                    loginResponse.getLname(),
                    loginResponse.getDeviceId(),
                    loginResponse.getDeviceType(),
                    loginResponse.getLastLogin()
            );
            Bundle properties = new Bundle();
            properties.putString("driver_id", loginResponse.getDriverId());
            properties.putString("email", loginResponse.getEmail());
            properties.putString("first_name", loginResponse.getFname());
            properties.putString("last_name", loginResponse.getLname());
            properties.putString("device_id", loginResponse.getDeviceId());
            properties.putString("device_type", loginResponse.getDeviceType());
            properties.putString("last_login", loginResponse.getLastLogin());
            AnalyticsReporter.getInstance().track(AnalyticsReporter.Event.DRIVER_LOGIN, properties);

            FLogManager.getInstance().get_loglevel();

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        } else {
            try {
                String s = response.errorBody().string();
                DebugLog.i("Login Failed Response :" + response.code());

                if (response.code() == ConstantClass.RESPONSE_CODE_400) {
                    GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                    showSnackbar(responseBody.getMessage());
                } else {
                    GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                    showSnackbar(responseBody.getMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        DebugLog.e("Login Request Failed :" + throwable.getMessage());
        AlertUtils.dismissDialog();
        showSnackbar(getString(R.string.bad_server));
    }

    /**
     * Call a login api.
     */

    private void callLoginWS(HashMap<String, String> param) {

        AlertUtils.showCustomProgressDialog(this);
        AuthenticationDAO.getInstance().callLogin(param, ConstantClass.METHOD_LOGIN, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                AlertUtils.dismissDialog();
                if (success) {
                    try {
                        if (data.code() == ConstantClass.RESPONSE_CODE_200) {
                            String response = data.body().string();
                            DebugLog.e("Response " + response);
                            AppHelper.getInstance().SaveInSharedPref(ConstantClass.LOGIN_USER, response);
                            AppHelper.getInstance().SaveInSharePrefWithBoolean(ConstantClass.IS_LOGIN, true);

                            if (remember.isChecked()) {
                                AppHelper.getInstance().SaveInSharedPref(ConstantClass.LOGIN_USER_NAME, edtEmail.getText().toString(), true);
                                AppHelper.getInstance().SaveInSharedPref(ConstantClass.LOGIN_USER_PASSWORD, edtPassword.getText().toString(), true);
                            } else {
                                AppHelper.getInstance().SaveInSharedPref(ConstantClass.LOGIN_USER_NAME, "", true);
                                AppHelper.getInstance().SaveInSharedPref(ConstantClass.LOGIN_USER_PASSWORD, "", true);
                            }

                            LoginResponse loginResponse = AppHelper.getInstance().getLoginUser();
                            //rhc commented
                            /*Crashlytics.setUserIdentifier(loginResponse.getDriverId());
                            Crashlytics.setUserEmail(loginResponse.getEmail());
                            Crashlytics.setUserName(loginResponse.getFname() + "" + loginResponse.getLname());
*/
                            AnalyticsReporter.getInstance().setIdentify(loginResponse.getDriverId());
                            AnalyticsReporter.getInstance().setProfile(
                                    loginResponse.getDriverId(),
                                    loginResponse.getEmail(),
                                    loginResponse.getFname(),
                                    loginResponse.getLname(),
                                    loginResponse.getDeviceId(),
                                    loginResponse.getDeviceType(),
                                    loginResponse.getLastLogin());
                            Bundle properties = new Bundle();
                            properties.putString("driver_id", loginResponse.getDriverId());
                            properties.putString("email", loginResponse.getEmail());
                            properties.putString("first_name", loginResponse.getFname());
                            properties.putString("last_name", loginResponse.getLname());
                            properties.putString("device_id", loginResponse.getDeviceId());
                            properties.putString("device_type", loginResponse.getDeviceType());
                            properties.putString("last_login", loginResponse.getLastLogin());
                            AnalyticsReporter.getInstance().track(AnalyticsReporter.Event.DRIVER_LOGIN, properties);

                            FLogManager.getInstance().get_loglevel();

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else if (data.code() == ConstantClass.RESPONSE_CODE_400) {
                            String s = data.body().string();

                            DebugLog.e("responce is    " + s);

                            GenericResponse response = ParsingHelper.getInstance().genericResponseParsing(s);
                            showSnackbar(response.getMessage());

                        } else {
                            String s = data.body().string();

                            DebugLog.e("responce is      +++" + s);

                            GenericResponse response = ParsingHelper.getInstance().genericResponseParsing(s);
                            showSnackbar(response.getMessage());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    showSnackbar(getString(R.string.bad_server));
                }

            }
        });
    }
   /*private void showSnackBar(String message) {

        Snackbar snackbar = Snackbar
                .make(relativeMain, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }*/


    private void showSnackBar(String message, final EditText editText) {

        Snackbar snackbar = Snackbar
                .make(lrBase, message, Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
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


    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public boolean isEditTextEmpty(EditText editText) {
        if (editText.getText().toString().trim().isEmpty())
            return true;
        return false;
    }

    public String StringToUtf(String s) {
        String add = "";
        if (s != null) {

            try {
                add = URLEncoder.encode(s, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        return add;

    }
}
