package com.rider.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.rider.LAOXI;
import com.rider.R;
import com.rider.customclasses.CustomButton;
import com.rider.customclasses.CustomEditText;
import com.rider.customclasses.CustomTextView;
import com.rider.dialog.AlertUtils;
import com.rider.dialog.GeneralStaticAlertDialog;
import com.rider.facebookhelper.FbHelper;
import com.rider.facebookhelper.FbResponse;
import com.rider.facebookhelper.IFacebookTaskComplet;
import com.rider.pojo.GenericResponse;
import com.rider.pojo.User;
import com.rider.retrofit.APIRequestManager;
import com.rider.retrofit.ApiService;
import com.rider.retrofit.RetroClient;
import com.rider.utils.AsteriskPasswordTransformationMethod;
import com.rider.utils.DataToPref;
import com.rider.utils.DebugLog;
import com.rider.utils.DialogDismissOneButton;
import com.rider.utils.FLogManager;
import com.rider.utils.LaoxiConstant;
import com.rider.utils.ParsingHelper;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by hitesh on 18/7/16.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, APIRequestManager.ResponseListener<User> {

    @Bind(R.id.imageView2)
    ImageView imageView2;
    @Bind(R.id.edtEmail)
    CustomEditText edtEmail;
    @Bind(R.id.edtPassword)
    CustomEditText edtPassword;
    @Bind(R.id.textForgotPassword)
    CustomTextView textForgotPassword;
    @Bind(R.id.buttonLogin)
    CustomButton buttonLogin;
    @Bind(R.id.btnSignupFacebook)
    CustomButton buttonLoginFacebook;
    @Bind(R.id.btnSigninGoogle)
    CustomButton btnSigninGoogle;
    @Bind(R.id.textDontHave)
    CustomTextView textDontHave;
    @Bind(R.id.relative_main)
    LinearLayout relativeMain;
    @Bind(R.id.textVersion)
    CustomTextView textVersion;
    @Bind(R.id.remember)
    CheckBox remember;
    FbHelper fb;
    String versionName;
    int versionCode;
    //for facebook login
    String strFBEmail;
    String strFBId;
    String strFBName;
    String strFBGender;
    //for google login
    String strGname;
    String strGmail;
    String strGid;

    private GoogleApiClient googleApiClient;
    static final int REQ_CODE = 9001;
    private static boolean glogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        // stopTrackRideService();
        fb = new FbHelper(this);

        ButterKnife.bind(this);
        LAOXI.setCurrentActivity(this);

        textForgotPassword.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
        buttonLoginFacebook.setOnClickListener(this);
        btnSigninGoogle.setOnClickListener(this);
        textDontHave.setOnClickListener(this);
        String firstPartForgotpass = "Forgot Your ";
        String secondPartForgotpass = " Password?";
        String strForgotPassword = firstPartForgotpass + "<b>" + secondPartForgotpass + "</b> ";
        textForgotPassword.setText(Html.fromHtml(strForgotPassword));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //      Don't have an account? Sign Up
        String strSignupFirstPart = "Don't have an account? ";
        String strSignupSecondPart = "Sign Up";
        String strSignUp = strSignupFirstPart + "<b>" + strSignupSecondPart + "</b> ";
        textDontHave.setText(Html.fromHtml(strSignUp));
        edtPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (DataToPref.getSharedPreferanceData(this, LaoxiConstant.STORED_USER, LaoxiConstant.LOGIN_USER_NAME) != null && !DataToPref.getSharedPreferanceData(this, LaoxiConstant.STORED_USER, LaoxiConstant.LOGIN_USER_NAME).equalsIgnoreCase("")) {
            edtEmail.setText(DataToPref.getSharedPreferanceData(this, LaoxiConstant.STORED_USER, LaoxiConstant.LOGIN_USER_NAME));
            edtPassword.setText(DataToPref.getSharedPreferanceData(this, LaoxiConstant.STORED_USER, LaoxiConstant.LOGIN_USER_PASSWORD));
            remember.setChecked(true);
        }

        PackageInfo info;
        textVersion.setText("");
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);

            versionName = info.versionName;
            versionCode = info.versionCode;
            textVersion.setText("v" + info.versionName + "(" + info.versionCode + ")");

            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //      String something = new String(Base64.encodeBytes(md.digest()));
                //      Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        LAOXI.currentActivity = LoginActivity.this;

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textDontHave:
                RegistrationActivity.fbLogin = false;
                Intent intentRegistration = new Intent(LoginActivity.this, RegistrationActivity.class);
//                intentRegistration.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentRegistration);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                break;
            case R.id.buttonLogin:

                validate();

                break;
            case R.id.btnSignupFacebook:
                try {
                    fb.onLogin(new IFacebookTaskComplet<FbResponse>() {
                        @Override
                        public void onTaskComplete(boolean success, @Nullable FbResponse data) {
                            try {
//                                wsCallLogIn(setFacebookData(data.getEmail(), data.getId()), data);

                                strFBEmail = data.getEmail();
                                strFBId = data.getId();
                                strFBName = data.getName();
                                strFBGender = data.getGender();


                                String device_id = FirebaseInstanceId.getInstance().getToken();

                                AlertUtils.showCustomProgressDialog(LoginActivity.this);
                                ApiService api = RetroClient.getApiService();
                                Call<User> call = api.loginFacebook("", strFBId, device_id != null ? device_id : "test", "A", versionName + " (" + versionCode + ")");

                                APIRequestManager<User> apiRequestManager = new APIRequestManager<>(LoginActivity.this, call);
                                apiRequestManager.execute();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.textForgotPassword:

                GeneralStaticAlertDialog.getInstance().createDialogForgotPassword(LoginActivity.this, new DialogDismissOneButton() {
                    @Override
                    public void onDismiss() {
                        GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(LoginActivity.this, "Thank you ! We sent you a password on your registered email address.", null);
                    }
                }, this);

                break;

            case R.id.btnSigninGoogle:
                googleLogin();
                break;
        }
    }

    /**
     * Validations for login screen.
     */

    private void validate() {
        try {
            if (isEditTextEmpty(edtEmail)) {
                showSnackBar(getString(R.string.please_enter_email), edtEmail);
            } else if (!isValidEmail(edtEmail.getText().toString().trim())) {
                showSnackBar(getString(R.string.please_enter_valid_email), edtEmail);
            } else if (isEditTextEmpty(edtPassword)) {
                showSnackBar(getString(R.string.please_enter_your_password), edtPassword);
            } else {
                if (webServiceState == WebServiceState.ACTIVE) {
                    String device_id = FirebaseInstanceId.getInstance().getToken();
                    AlertUtils.showCustomProgressDialog(LoginActivity.this);
                    ApiService api = RetroClient.getApiService();
                    Call<User> call = api.logIn("",
                            edtEmail.getText().toString(),
                            edtPassword.getText().toString(),
                            device_id != null ? device_id : "test",
                            "A",
                            versionName + " (" + versionCode + ")");
                    APIRequestManager<User> apiRequestManager = new APIRequestManager<>(LoginActivity.this, call);
                    apiRequestManager.execute();

                } else {
                    FLogManager.getInstance().get_loglevel(LoginActivity.this);
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

    private void googleLogin() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            fb.setCallbackManager(requestCode, resultCode, data);
            DebugLog.e("FirebaseInstanceId.getInstance().getToken() ::::: " + FirebaseInstanceId.getInstance().getToken());
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            glogin = true;
            GoogleSignInAccount account = result.getSignInAccount();
            strGname = account.getDisplayName();
            strGmail = account.getEmail();
            strGid = account.getId();
            String profile_image;
            if (account.getPhotoUrl() != null) profile_image = account.getPhotoUrl().toString();
            else profile_image = "";

            String device_id = FirebaseInstanceId.getInstance().getToken();

            AlertUtils.showCustomProgressDialog(LoginActivity.this);
            ApiService api = RetroClient.getApiService();
            Call<User> call = api.loginGoogle("", strGid, device_id != null ? device_id : "test", "A", versionName + " (" + versionCode + ")");

            APIRequestManager<User> apiRequestManager = new APIRequestManager<>(LoginActivity.this, call);
            apiRequestManager.execute();
/*            Bundle personal_infos = new Bundle();
            personal_infos.putString("name", account.getDisplayName());
            personal_infos.putString("email", account.getEmail());
            if(account.getPhotoUrl() != null)
                personal_infos.putString("profile_image", account.getPhotoUrl().toString());
            personal_infos.putString("token", account.getIdToken());
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            intent.putExtras(personal_infos);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/

        } else {
            Toast.makeText(this, R.string.not_google_login, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public boolean isEditTextEmpty(EditText editText) {
        if (editText != null && editText.getText().toString().trim().isEmpty())
            return true;
        return false;
    }

    private void showSnackBar(String message, final EditText editText) {

        Snackbar snackbar = Snackbar
                .make(relativeMain, message, Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
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

    @Override
    protected void onApplyThemeResource(Resources.Theme theme, int resid, boolean first) {
        super.onApplyThemeResource(theme, resid, first);
    }

    @Override
    public void onResponse(retrofit2.Response<User> response) {
        AlertUtils.dismissDialog();
        if (response.isSuccessful()) {
            DebugLog.i("Login Successful : " + response.body());

            User user = response.body();
            DebugLog.i("Login User Data : " + user);

            String data = new Gson().toJson(user);

            DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA, data);
            DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.ISLOGIN, "true");

            DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.HEADER_TOCKEN, user.getToken());
            DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.USER_ID, user.getId());

            if (remember.isChecked()) {
                DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.LOGIN_USER_NAME, edtEmail.getText().toString());
                DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.LOGIN_USER_PASSWORD, edtPassword.getText().toString());
            } else {
                DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.LOGIN_USER_NAME, "");
                DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.LOGIN_USER_PASSWORD, "");
            }

            FLogManager.getInstance().get_loglevel(LoginActivity.this);

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            if (response.code() == 400) {
                try {
                    String s = response.errorBody().string();
                    GenericResponse genericResponse = ParsingHelper.getInstance().genericResponseParsing(s);
                    showSnackBar(genericResponse.getMessage(), edtPassword);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (response.code() == 404) {

                try {
                    String s = response.errorBody().string();
                    GenericResponse genericResponse = ParsingHelper.getInstance().genericResponseParsing(s);
                    showSnackBar(genericResponse.getMessage(), edtPassword);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (response.code() == 406) {
                if (!glogin) {
                    Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                    RegistrationActivity.fbLogin = true;
                    intent.putExtra(LaoxiConstant.EMAIL, strFBEmail);
                    intent.putExtra(LaoxiConstant.GENDER, strFBGender);
                    intent.putExtra(LaoxiConstant.FB_ID, strFBId);
                    intent.putExtra(LaoxiConstant.FNAME, strFBName);
                    startActivity(intent);
                    //                finish();
                } else {
                    glogin = false;
                    Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                    RegistrationActivity.googleLogin = true;
                    intent.putExtra(LaoxiConstant.EMAIL, strGmail);
                    intent.putExtra(LaoxiConstant.GNAME, strGname);
                    intent.putExtra(LaoxiConstant.G_ID, strGid);
                    startActivity(intent);
                }
            } else if (response.code() == 412) {

                User user = response.body();
                DebugLog.e("Response Code 412 : User Data ======>" + user);
                String data = new Gson().toJson(user);
                DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA, data);

                Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
                startActivity(intent);
                finish();

            }
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        AlertUtils.dismissDialog();
        DebugLog.e("Login Request Failed Msg :" + throwable.getMessage());
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
} //end of main class
