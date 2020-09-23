package com.driver.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.driver.R;
import com.driver.activity.BaseActivity;
import com.driver.activity.HomeActivity;
import com.driver.dao.IAsyncCompleteCallback;
import com.driver.dao.ParsingHelper;
import com.driver.dao.homedao.HomeActivityDAO;
import com.driver.pojo.GenericResponse;
import com.driver.pojo.TrackRidePojo.TrackRide;
import com.driver.retrofit.APIRequestManager;
import com.driver.retrofit.ApiService;
import com.driver.retrofit.RetroClient;
import com.driver.util.AlertUtils;
import com.driver.util.AppHelper;
import com.driver.util.ConstantClass;
import com.driver.util.DialogDismissOneButton;
import com.driver.util.DialogDismissTwoButton;
import com.driver.util.DialogDismissTwoButtonwithText;
import com.driver.util.IListenerCountryCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Response;
import retrofit2.Call;


/**
 * Created by darshan on 9/6/15.
 */
public class GeneralStaticAlertDialog {

    public static GeneralStaticAlertDialog alertDialogCustom = new GeneralStaticAlertDialog();
    private EditText edtEmail;

    public static GeneralStaticAlertDialog getInstance() {
        return alertDialogCustom;
    }

    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z]+(\\.[A-Za-z]+)*(\\.[A-Za-z]{2,})$";

    public GeneralStaticAlertDialog() {

    }

    Dialog dlg;
    AlertDialog alertDialog;
    Context context;
    private LayoutInflater layoutInflater;
    View view;
    TextView textViewTitle;
    TextView textViewDescription;
    Button buttonOk, buttonCancel,buttonWaze,buttonGoogle;
    DialogDismissOneButton dialogDismissOneButton;
    AlertDialog.Builder alertDialogBuilder;


    public void createDialogWithOneButton(Context context, String title, String description, final DialogDismissOneButton dialogDismiss) {

        this.context = context;
        dlg = new Dialog(context);
        dlg.setCancelable(false);

        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.view = layoutInflater.inflate(R.layout.custom_dialog_one_button, null);
        textViewTitle = (TextView) view.findViewById(R.id.txtTitle);
        textViewTitle.setText(title);
        textViewDescription = (TextView) view.findViewById(R.id.txtDescription);
        textViewDescription.setText(description);
        buttonOk = (Button) view.findViewById(R.id.btnOk);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();

                if (dialogDismiss != null) {
                    dialogDismiss.onDismiss();
                }
            }
        });

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.setContentView(view);

        dlg.show();

    }

    public void createDialogForgotPassword(Context context, final DialogDismissOneButton dialogDismiss, final BaseActivity baseActivity) {

        this.context = context;
        dlg = new Dialog(context);
        dlg.setCancelable(true);

        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.view = layoutInflater.inflate(R.layout.custom_dialog_forgot_password, null);
        textViewTitle = (TextView) view.findViewById(R.id.txtTitle);
        edtEmail = (EditText) view.findViewById(R.id.edtEmail);
        buttonOk = (Button) view.findViewById(R.id.btnOk);
        ImageButton btnClose = (ImageButton) view.findViewById(R.id.btnClose);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validation()) {
                    if (baseActivity.webServiceState == BaseActivity.WebServiceState.ACTIVE) {
                        callForgetPwdWS(dialogDismiss, baseActivity);
                    } else {
                        dlg.dismiss();
                    }
                }


            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.setContentView(view);

        dlg.show();

    }

    private void callForgetPwdWS(final DialogDismissOneButton dialogDismiss, final BaseActivity b) {
        AlertUtils.showCustomProgressDialog(b);

        String strEmail = edtEmail.getText().toString();
        ApiService api = RetroClient.getApiService();
        Call<Void> call = api.callForgetPasswordWS("", strEmail);
        APIRequestManager<Void> apiRequestManager = new APIRequestManager<>(new APIRequestManager.ResponseListener<Void>() {
            @Override
            public void onResponse(retrofit2.Response<Void> response) {
                AlertUtils.dismissDialog();
                if(response.isSuccessful()) {
                    if (response.code() == ConstantClass.RESPONSE_CODE_200) {
                        dlg.dismiss();
                        dialogDismiss.onDismiss();
                    }
                } else {
                    try {
                        if (response.code() == ConstantClass.RESPONSE_CODE_400) {
                            String s = response.errorBody().string();

                            GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                            b.showSnackbar(responseBody.getMessage());
                        } else if (response.code() == ConstantClass.RESPONSE_CODE_401) {
                            ((HomeActivity) context).callLogoutWS();
                        } else {
                            String s = response.errorBody().string();

                            GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                            b.showSnackbar(responseBody.getMessage());
                        }
                    } catch ( IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Throwable throwable) {
                AlertUtils.dismissDialog();
                b.showSnackbar(b.getString(R.string.bad_server));
            }
        }, call);
        apiRequestManager.execute();

    }

    private boolean validation() {

   /*     if (edtEmail.getText().toString().trim().isEmpty()) {
            showSnackBar("Please enter email", edtEmail, view);
            edtEmail.requestFocus();
            return false;
        } else {
            if (!Pattern.matches(EMAIL_PATTERN, edtEmail.getText().toString())) {
                showSnackBar("Please enter valid email", edtEmail, view);
                edtEmail.requestFocus();
                return false;
            }
        }
        return true;*/
        CharSequence target = edtEmail.getText().toString();
        boolean isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();

        if (target == null || target.toString().equalsIgnoreCase("")) {
            showSnackBar("Please enter email", edtEmail, view);
            edtEmail.requestFocus();
            return false;
        } else if (!isValid) {
            showSnackBar("Please enter valid email", edtEmail, view);
            edtEmail.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public void createDialogWithTwoButton(Context context, String description, final DialogDismissTwoButton dialogDismiss) {

        this.context = context;
        dlg = new Dialog(context);
        dlg.setCancelable(false);

        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.view = layoutInflater.inflate(R.layout.custom_dialog_two_button, null);
        textViewDescription = (TextView) view.findViewById(R.id.txtDescription);
        textViewDescription.setText(description);
        buttonOk = (Button) view.findViewById(R.id.btnOk);
        buttonCancel = (Button) view.findViewById(R.id.btnCancel);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
                if (dialogDismiss != null) {
                    dialogDismiss.onOkClick();
                }
            }
        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();

                if (dialogDismiss != null) {
                    dialogDismiss.onCancelClick();
                }
            }
        });
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.setContentView(view);

        dlg.show();

    }


    public void createDialogWithWazeGoogle(Context context,final DialogDismissTwoButton dialogDismiss) {

        this.context = context;
        dlg = new Dialog(context);
        dlg.setCancelable(false);

        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.view = layoutInflater.inflate(R.layout.custom_dialog_google_waze, null);
        buttonWaze = (Button) view.findViewById(R.id.btnWaze);
        buttonGoogle = (Button) view.findViewById(R.id.btnGoogle);
        ImageButton btnClose = (ImageButton) view.findViewById(R.id.btnClose);

        buttonWaze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
                if (dialogDismiss != null) {
                    dialogDismiss.onOkClick();
                }
            }
        });


        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();

                if (dialogDismiss != null) {
                    dialogDismiss.onCancelClick();
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });


        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.setContentView(view);

        dlg.show();

    }


    public void DismissDilog() {
        if (dlg != null && dlg.isShowing() == true) {
            dlg.dismiss();
        }
    }

    public void nativeDialogOneButton(Context context, String message, final DialogDismissOneButton dialogCallBack) {
        this.dialogDismissOneButton = dialogCallBack;
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message).setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogCallBack != null)
                    dialogCallBack.onDismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

   /* private void showSnackBar(String message) {

        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
*/

    public void showSnackBar(String message, final EditText editText, View view) {

        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editText != null)
                            editText.requestFocus();
                        editText.setSelection(editText.getText().length());

                    }
                });
        snackbar.show();
    }

    public void openCountryCode(Context context, final IListenerCountryCode iListenerCountryCode) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Country Code");


        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item);
        arrayAdapter.add("+61");
        arrayAdapter.add("+91");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (iListenerCountryCode != null) {
                    iListenerCountryCode.onCancel("");

                }
            }
        });

        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (iListenerCountryCode != null) {
                            String strName = arrayAdapter.getItem(which);
                            iListenerCountryCode.onSelect(strName);
                        }
                    }
                });
        builderSingle.show();
    }

    public void createDialogSendSms(Context context, String description, final DialogDismissTwoButtonwithText dialogDismissTwoButtonwithText) {

        try {
            this.context = context;
            dlg = new Dialog(context);
            dlg.setCancelable(false);

            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            this.view = layoutInflater.inflate(R.layout.custom_dialog_send_sms, null);

            final EditText editTextBody;

            editTextBody = (EditText) view.findViewById(R.id.edtSmsBody);
            buttonOk = (Button) view.findViewById(R.id.btnOk);
            buttonCancel = (Button) view.findViewById(R.id.btnCancel);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlg.dismiss();

                    if (dialogDismissTwoButtonwithText != null) {

                        dialogDismissTwoButtonwithText.onOkClick(editTextBody.getText().toString());
                    }
                }
            });


            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlg.dismiss();

                    if (dialogDismissTwoButtonwithText != null) {
                        dialogDismissTwoButtonwithText.onCancelClick();
                    }
                }
            });

            dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dlg.setContentView(view);

            dlg.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void openAppVersionUpdateDialog(Context context, final DialogDismissOneButton dialogDismiss) {
        try {

            this.context = context;
            dlg = new Dialog(context);
            dlg.setCancelable(false);
            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.view = layoutInflater.inflate(R.layout.custom_dialog_app_version_info, null);
            ImageButton btnClose = (ImageButton) view.findViewById(R.id.btnClose);

            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlg.dismiss();
                    if (dialogDismiss != null) {
                        dialogDismiss.onDismiss();
                    }
                }
            });

            dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dlg.setContentView(view);
            dlg.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
