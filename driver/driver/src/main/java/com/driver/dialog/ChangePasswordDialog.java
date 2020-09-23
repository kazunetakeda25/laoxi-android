package com.driver.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.driver.R;
import com.driver.activity.HomeActivity;
import com.driver.customclasses.CustomButton;
import com.driver.customclasses.CustomEditText;
import com.driver.dao.IAsyncCompleteCallback;
import com.driver.dao.ParsingHelper;
import com.driver.dao.homedao.HomeActivityDAO;
import com.driver.pojo.GenericResponse;
import com.driver.pojo.loginws.LoginResponse;
import com.driver.retrofit.APIRequestManager;
import com.driver.retrofit.ApiService;
import com.driver.retrofit.RetroClient;
import com.driver.util.AlertUtils;
import com.driver.util.AppHelper;
import com.driver.util.AsteriskPasswordTransformationMethod;
import com.driver.util.ConstantClass;
import com.driver.util.DialogCancel;
import com.driver.util.DialogDismissOneButton;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;
import retrofit2.Call;

/**
 * Created by chirag on 18/2/16.
 */
public class ChangePasswordDialog {


    Dialog dlg;
    Activity context;


    HomeActivity homeActivity;
    DialogCancel dialogCancel;
    @Bind(R.id.edtOldPassword)
    CustomEditText edtOldPassword;
    @Bind(R.id.edtNewPassword)
    CustomEditText edtNewPassword;
    @Bind(R.id.btnChangePassword)
    CustomButton btnChangePassword;
    @Bind(R.id.linear_main)
    LinearLayout linearMain;

    @Bind(R.id.btnClose)
    ImageButton btnClose;

    public void createDialog(final Activity context, final String response, final DialogCancel dialogCencel) {
        this.context = context;
        this.dialogCancel = dialogCencel;
        dlg = new Dialog(context);
        if (context instanceof HomeActivity) {
            homeActivity = (HomeActivity) context;
        }

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlg.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dlg.setCancelable(true);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.change_password_layout, null);
        ButterKnife.bind(this, view);

        edtOldPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        edtNewPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        dlg.setContentView(view);
        dlg.show();
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*dlg.dismiss();

                if (dialogCancel != null) {
                    dialogCancel.onDismiss();
                }*/

                if (isEditTextEmpty(edtOldPassword)) {
                    showSnackBar("Please enter old password", edtOldPassword, linearMain);
                } else if (isEditTextEmpty(edtNewPassword)) {
                    showSnackBar("Please enter new password", edtNewPassword, linearMain);
                } else if (edtOldPassword.getText().toString().equalsIgnoreCase(edtNewPassword.getText().toString())) {
                    showSnackBar("You can not set old password and new password same", edtOldPassword, linearMain);
                } else {
//                    LoginResponse loginData = AppHelper.getInstance().getLoginUser();
//                    HashMap<String, String> changePasswordData = new HashMap<>();
//                    changePasswordData.put(ConstantClass.DRIVER_ID, loginData.getDriverId());
//                    changePasswordData.put(ConstantClass.CHANGE_PASSWORD_OLD_PASSWORD, StringToUtf(edtOldPassword.getText().toString()));
//                    changePasswordData.put(ConstantClass.CHANGE_PASSWORD_NEW_PASSWORD, StringToUtf(edtNewPassword.getText().toString()));
//                    callChangePassword(changePasswordData, loginData.getToken());

                    String strToken = AppHelper.getInstance().getToken();
                    String strDriverId = AppHelper.getInstance().getDriverId();
                    AlertUtils.showCustomProgressDialog(context);
                    ApiService api = RetroClient.getApiService();
                    Call<GenericResponse> call = api.callChangePassword(strToken, strDriverId, StringToUtf(edtOldPassword.getText().toString()), StringToUtf(edtNewPassword.getText().toString()));

                    APIRequestManager<GenericResponse> apiRequestManager = new APIRequestManager<>(new APIRequestManager.ResponseListener<GenericResponse>() {
                        @Override
                        public void onResponse(retrofit2.Response<GenericResponse> response) {
                            AlertUtils.dismissDialog();
                            if ( response.isSuccessful() ) {
                                if (response.code() == ConstantClass.RESPONSE_CODE_200) {

                                    GenericResponse responseBody = response.body();
                                    GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(context, responseBody.getMessage(), new DialogDismissOneButton() {
                                        @Override
                                        public void onDismiss() {
                                            dlg.dismiss();

                                            if (dialogCancel != null) {
                                                dialogCancel.onDismiss();
                                            }
                                        }
                                    });


                                }
                            } else {
                                try {
                                    if (response.code() == ConstantClass.RESPONSE_CODE_400) {
                                        String s = response.errorBody().string();
                                        GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                                        showSnackBar(responseBody.getMessage(), null, linearMain);
                                    } else if (response.code() == ConstantClass.RESPONSE_CODE_401) {
                                        if (homeActivity != null) {
                                            homeActivity.Logout(false);
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            AlertUtils.dismissDialog();
                            showSnackBar(context.getString(R.string.bad_server), null, linearMain);
                        }
                    }, call);
                    apiRequestManager.execute();
                }
            }
        });
    }

    public boolean isEditTextEmpty(EditText editText) {
        if (editText.getText().toString().trim().isEmpty())
            return true;
        return false;
    }

    private void showSnackBar(String message, final EditText editText, LinearLayout linear_main) {

        Snackbar snackbar = Snackbar
                .make(linear_main, message, Snackbar.LENGTH_LONG).setAction("DISMISS", new View.OnClickListener() {
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

    private void callChangePassword(HashMap<String, String> param, String token) {
        AlertUtils.showCustomProgressDialog(context);
        HomeActivityDAO.getInstance().callChangePassword(param, ConstantClass.METHOD_CHANGE_PASSWORD, token, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                AlertUtils.dismissDialog();
                if (success) {
                    try {

                        if (data.code() == ConstantClass.RESPONSE_CODE_200) {
                            String s = data.body().string();
                            GenericResponse response = ParsingHelper.getInstance().genericResponseParsing(s);

                            GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(context, response.getMessage(), new DialogDismissOneButton() {
                                @Override
                                public void onDismiss() {
                                    dlg.dismiss();

                                    if (dialogCancel != null) {
                                        dialogCancel.onDismiss();
                                    }
                                }
                            });


                        } else if (data.code() == ConstantClass.RESPONSE_CODE_400) {
                            String s = data.body().string();
                            GenericResponse response = ParsingHelper.getInstance().genericResponseParsing(s);
                            showSnackBar(response.getMessage(), null, linearMain);
                        } else if (data.code() == ConstantClass.RESPONSE_CODE_401) {
                            if (homeActivity != null) {
                                homeActivity.Logout(false);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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

}// end of main class
