package com.rider.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.rider.LAOXI;
import com.rider.R;
import com.rider.activity.BaseActivity;
import com.rider.customclasses.CustomEditText;
import com.rider.customclasses.CustomTextView;
import com.rider.pojo.GenericResponse;
import com.rider.pojo.User;
import com.rider.retrofit.APIRequestManager;
import com.rider.retrofit.ApiService;
import com.rider.retrofit.RetroClient;
import com.rider.utils.DataToPref;
import com.rider.utils.DebugLog;
import com.rider.utils.DialogDismissChangePassword;
import com.rider.utils.DialogDismissOneButton;
import com.rider.utils.DialogDismissTwoButton;
import com.rider.utils.DialogDismissTwoButtonwithText;
import com.rider.utils.LaoxiConstant;
import com.rider.utils.IListenerCountryCode;
import com.rider.utils.ParsingHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;

/**
 * Created by darshan on 9/6/15.
 */
public class GeneralStaticAlertDialog {

    public static GeneralStaticAlertDialog alertDialogCustom = new GeneralStaticAlertDialog();
    CustomEditText edtEmail, edtOldPassword, edtNewPassword;

    public static GeneralStaticAlertDialog getInstance() {
        return alertDialogCustom;
    }

    public GeneralStaticAlertDialog() {

    }

    Dialog dlg;
    AlertDialog alertDialog;
    Context context;
    private LayoutInflater layoutInflater;
    View view;
    TextView textViewTitle;
    TextView textViewDescription;
    Button buttonOk, buttonCancel;
    DialogDismissOneButton dialogDismissOneButton;
    AlertDialog.Builder alertDialogBuilder;
    ImageButton  btnClose;
    public void createDialogWithOneButton(Context context, String title, String description, final DialogDismissOneButton dialogDismiss) {

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createDialogForgotPassword(final Context context, final DialogDismissOneButton dialogDismiss, final BaseActivity activity) {

        try {
            final LinearLayout linear_main;
            this.context = context;
            dlg = new Dialog(context);
            dlg.setCancelable(true);

            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            this.view = layoutInflater.inflate(R.layout.custom_dialog_forgot_password, null);
            edtEmail = (CustomEditText) view.findViewById(R.id.edtEmail);
            linear_main = (LinearLayout) view.findViewById(R.id.linear_main);

            buttonOk = (Button) view.findViewById(R.id.btnOk);
            ImageButton close = (ImageButton) view.findViewById(R.id.btnClose);


            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (dialogDismiss != null) {

                        if (isEditTextEmpty(edtEmail)) {
                            showSnackBar("Please enter register email", edtEmail, linear_main);
                        } else if (!isValidEmail(edtEmail.getText())) {
                            showSnackBar("Please enter valid register email", edtEmail, linear_main);
                        } else {

                            if (activity.webServiceState == BaseActivity.WebServiceState.ACTIVE) {
                                wsCallForgotPassword(dialogDismiss, linear_main);
                            } else {
                                dlg.dismiss();
                                dialogDismiss.onDismiss();
                            }
                        }
                    }
                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dlg.dismiss();
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


    public void createDialogChangePassword(final User user, final Context context, final DialogDismissChangePassword dialogDismiss) {

        try {
            final LinearLayout linear_main;
            this.context = context;
            dlg = new Dialog(context);
            dlg.setCancelable(true);

            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            this.view = layoutInflater.inflate(R.layout.custom_dialog_change_password, null);
            edtOldPassword = (CustomEditText) view.findViewById(R.id.edtOldPassword);
            edtNewPassword = (CustomEditText) view.findViewById(R.id.edtNewPassword);

            linear_main = (LinearLayout) view.findViewById(R.id.linear_main);

            buttonOk = (Button) view.findViewById(R.id.btnChangePassword);

            ImageButton close = (ImageButton) view.findViewById(R.id.btnClose);


            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (dialogDismiss != null) {

                        if (isEditTextEmpty(edtOldPassword)) {
                            showSnackBar("Please enter old password", edtOldPassword, linear_main);
                        } else if (isEditTextEmpty(edtNewPassword)) {
                            showSnackBar("Please enter new password", edtNewPassword, linear_main);
                        } else if (edtNewPassword.getText().length() < 6) {
                            showSnackBar("Password should be minimum 6 characters", edtNewPassword,linear_main);

                        }else if (edtOldPassword.getText().toString().equals(edtNewPassword.getText().toString())) {
                            showSnackBar("You can not set old password and new password same", edtNewPassword, linear_main);
                        } else {
                            /*dialogDismiss.onDismiss();*/

                            doChangePassword(user, dialogDismiss, linear_main);
                        }
                    }
                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dlg.dismiss();
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


    public void createDialogWithTwoButton(Context context, String description, final DialogDismissTwoButton dialogDismiss) {

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createDialogSendSms(Context context, String description, final DialogDismissTwoButtonwithText dialogDismissTwoButtonwithText) {

        try {
            this.context = context;
            dlg = new Dialog(context);
            dlg.setCancelable(false);

            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            this.view = layoutInflater.inflate(R.layout.custom_dialog_send_sms, null);

             final EditText editTextBody;

            editTextBody= (EditText) view.findViewById(R.id.edtSmsBody);
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


    public void DismissDilog() {
        if (dlg != null && dlg.isShowing() == true) {
            dlg.dismiss();
        }
    }

    public void nativeDialogOneButton(Context context, String message, final DialogDismissOneButton dialogCallBack) {
        try {
            this.dialogDismissOneButton = dialogCallBack;
            alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage(message).setCancelable(false);
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (dialogCallBack != null) {
                        dialogCallBack.onDismiss();
                        alertDialog.dismiss();
                    } else {
                        alertDialog.dismiss();
                    }

                }
            });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
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
        if (editText.getText().toString().trim().isEmpty())
            return true;
        return false;
    }

    private void showSnackBar(String message, final EditText editText, LinearLayout linear_main) {

        Snackbar snackbar = Snackbar
                .make(linear_main, message, Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
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

    public void showSnackBar(String message, final TextView editText, View view) {

        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editText != null) {
                            editText.requestFocus();
                        }

                    }
                });
        snackbar.show();
    }

    public void openCountryCode(Context context, final IListenerCountryCode iListenerCountryCode) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Country");

        final HashMap<String,String> countries = new HashMap<String, String>();
        final ArrayAdapter<String> countryArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item);

        String[] isoCountryCodes = Locale.getISOCountries();
        for (String countryCode : isoCountryCodes) {
            Locale locale = new Locale("en_AU", countryCode);
            String countryName = locale.getDisplayCountry();

            DebugLog.i("Country Name :" + countryName);

            countries.put(countryName, countryCode);
            countryArrayAdapter.add(countryName);
        }

        // Sort alphabetically
        countryArrayAdapter.sort(new Comparator<String>() {
            @Override
            public int compare(String a1, String a2) {
                return (a1).compareTo(a2);
            }
        });

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (iListenerCountryCode != null) {
                    iListenerCountryCode.onCancel("");

                }
            }
        });

        builderSingle.setAdapter(countryArrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (iListenerCountryCode != null) {
                            String strName = countryArrayAdapter.getItem(which);
                            String countryCode = countries.get(strName);

                            iListenerCountryCode.onSelect(countryCode);
                        }
                    }
                });
        builderSingle.show();
    }

    public void wsCallForgotPassword(final DialogDismissOneButton dialogDismiss, final LinearLayout linear_main) {

        AlertUtils.showCustomProgressDialog(context);

        ApiService api = RetroClient.getApiService();
        Call<Void> call = api.doForgotPassword("", edtEmail.getText().toString());
        APIRequestManager<Void> apiRequestManager = new APIRequestManager<>(new APIRequestManager.ResponseListener<Void>() {
            @Override
            public void onResponse(retrofit2.Response<Void> response) {
                AlertUtils.dismissDialog();
                if(response.isSuccessful()) {
                    if (response.code() == 200) {
                        if (dialogDismiss != null) {
                            dlg.dismiss();
                            dialogDismiss.onDismiss();
                        }
                    }
                } else {
                    try {
                        if (response.code() == 400) {
                            String s = response.errorBody().string();

                            GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                            showSnackBar(responseBody.getMessage(), edtEmail, linear_main);
                        } else if (response.code() == 404) {
                            showSnackBar("Bad Server Response", edtEmail, linear_main);
                        } else {
                            String s = response.errorBody().string();

                            GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                            showSnackBar(responseBody.getMessage(), edtEmail, linear_main);
                        }
                    } catch ( IOException e) {
                        e.printStackTrace();
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

    public void createRegistrationDialog1(Context context, String description, EditText editText) {

        try {

            int pos = editText.getSelectionStart();
            Layout layout = editText.getLayout();
            int line = layout.getLineForOffset(pos);
            int baseline = layout.getLineBaseline(line);
            int ascent = layout.getLineAscent(line);
            float x = layout.getPrimaryHorizontal(pos);
            float y = baseline + ascent;


            this.context = context;
            dlg = new Dialog(context);
            dlg.setCancelable(true);

            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            this.view = layoutInflater.inflate(R.layout.custom_registration_dialog, null);
            textViewDescription = (TextView) view.findViewById(R.id.txtDescription);
            textViewDescription.setText(description);

            dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            /*WindowManager.LayoutParams wlp = dlg.getWindow().getAttributes();
            wlp.x= editText.getLeft();
            wlp.y= editText.getTop();
            dlg.getWindow().setAttributes(wlp);*/
            dlg.setContentView(view);
            dlg.show();

            dlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    DismissDilog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createDialogWithOneButtonYellow(Context context, String title, String description, final DialogDismissOneButton dialogDismiss) {

        try {
            this.context = context;
            dlg = new Dialog(context);
            dlg.setCancelable(true);

            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            this.view = layoutInflater.inflate(R.layout.custom_dialog_one_button_yellow, null);
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public HashMap<String, String> setChangePasswordData(User user) {


        HashMap<String, String> changePasswordList = new HashMap<>();
        changePasswordList.put(LaoxiConstant.USER_ID, user.getId());
        changePasswordList.put(LaoxiConstant.OLD_PASSWORD, StringToUtf(edtOldPassword.getText().toString()));
        changePasswordList.put(LaoxiConstant.NEW_PASSWORD, StringToUtf(edtNewPassword.getText().toString()));
        return changePasswordList;
    }

    public void doChangePassword(User user, final DialogDismissChangePassword dialogDismiss, final LinearLayout linear_main) {
        AlertUtils.showCustomProgressDialog(context);

        String strToken = DataToPref.getSharedPreferanceData(context.getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.HEADER_TOCKEN);
        String strUserId = DataToPref.getSharedPreferanceData(context.getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.USER_ID);

        AlertUtils.showCustomProgressDialog(context);
        ApiService api = RetroClient.getApiService();
        Call<GenericResponse> call = api.doChangePassword(strToken, strUserId, StringToUtf(edtOldPassword.getText().toString()), StringToUtf(edtNewPassword.getText().toString()));

        APIRequestManager<GenericResponse> apiRequestManager = new APIRequestManager<>(new APIRequestManager.ResponseListener<GenericResponse>() {
            @Override
            public void onResponse(retrofit2.Response<GenericResponse> response) {
                AlertUtils.dismissDialog();
                if ( response.isSuccessful() ) {
                    if (response.code() == 200) {

                        GenericResponse responseBody = response.body();
                        dlg.dismiss();
                        dialogDismiss.onDismiss(responseBody.getMessage());

                        showSnackBar(responseBody.getMessage(), null, linear_main);
                    }
                } else {
                    try {
                        if (response.code() == 400) {
                            String s = response.errorBody().string();
                            GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                            showSnackBar(responseBody.getMessage(), edtEmail, linear_main);

                        } else if (response.code() == 404) {
                            showSnackBar("Bad Server Response", edtEmail, linear_main);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                AlertUtils.dismissDialog();
                showSnackBar("Bad Server Response", edtEmail, linear_main);
            }
        }, call);
        apiRequestManager.execute();

    }

    public void createDialogQuestion(Context context, final DialogDismissOneButton dialogDismiss) {
        try {

            this.context = context;
            dlg = new Dialog(context);
            dlg.setCancelable(false);
            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.view = layoutInflater.inflate(R.layout.custom_dialog_question, null);
            btnClose = (ImageButton) view.findViewById(R.id.btnClose);
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

    public void openTutorialDialog(Context context,String msg, final DialogDismissOneButton dialogDismiss) {
        try {

            this.context = context;
            dlg = new Dialog(context);
            dlg.setCancelable(false);
            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.view = layoutInflater.inflate(R.layout.custom_dialog_info, null);
            btnClose = (ImageButton) view.findViewById(R.id.btnClose);
            TextView tvMessage = (TextView) view.findViewById(R.id.tvMessage);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlg.dismiss();
                    if (dialogDismiss != null) {
                        dialogDismiss.onDismiss();
                    }
                }
            });
            tvMessage.setText(msg);
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
            btnClose = (ImageButton) view.findViewById(R.id.btnClose);

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



    public void createDialogConfirmRideNow(Context context, final DialogDismissTwoButtonwithText dialogDismissTwoButtonwithText) {

        try {
            final LinearLayout linear_main;
            this.context = context;
            dlg = new Dialog(context);
            dlg.setCancelable(false);

            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            this.view = layoutInflater.inflate(R.layout.custom_dialog_confirm_ride_now, null);

            btnClose = (ImageButton) view.findViewById(R.id.btnClose);


            linear_main = (LinearLayout) view.findViewById(R.id.linear_main);
            LinearLayout linearLayout= (LinearLayout) view.findViewById(R.id.linearLayoutRideNow);
            Button btnEstimateFare = (Button) view.findViewById(R.id.txtEstimateFare);
            Button btnRideLater = (Button) view.findViewById(R.id.txtRideLater);

            alertDialogBuilder = new AlertDialog.Builder(this.context);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (LAOXI.currentRide.getCarTypeId() != null) {
                        dlg.dismiss();
                    } else {
                        alertDialogBuilder.setMessage("Sorry, No Drivers Are Available").setCancelable(false);
                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialog.dismiss();
                            }
                        });

                        alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        return;
                    }


                    if (dialogDismissTwoButtonwithText != null) {

                        dialogDismissTwoButtonwithText.onOkClick("Ride Now");
                    }
                }
            });


            btnEstimateFare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlg.dismiss();

                    if (dialogDismissTwoButtonwithText != null) {

                        dialogDismissTwoButtonwithText.onOkClick("Fare estimation");
                    }
                }
            });


            btnRideLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlg.dismiss();
                    if (dialogDismissTwoButtonwithText != null) {

                        dialogDismissTwoButtonwithText.onOkClick("Ride Later");
                    }

                }
            });

            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlg.dismiss();

                }
            });

            dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            dlg.setContentView(view);

            dlg.show();
            if (LAOXI.currentRide.getCarTypeId() == null) {
                linearLayout.setBackgroundResource(R.drawable.rounded_corner_dark_grey);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void createDialogForCarSelections(Context context,final DialogDismissTwoButtonwithText dialogDismissTwoButtonwithText) {

        try {
            this.context = context;
            dlg = new Dialog(context);
            dlg.setCancelable(false);
            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.view = layoutInflater.inflate(R.layout.custom_dialog_car_selection, null);

            btnClose = (ImageButton) view.findViewById(R.id.btnClose);
            final Button btnTaxi = (Button) view.findViewById(R.id.txtTaxi);
            final Button btnBike = (Button) view.findViewById(R.id.txtBike);
            final Button btnTukTuk = (Button) view.findViewById(R.id.txtTuktuk);
            final Button btnOwner = (Button) view.findViewById(R.id.txtOwner);

            btnTaxi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlg.dismiss();

                    if (dialogDismissTwoButtonwithText != null) {

                        //dialogDismissTwoButtonwithText.onOkClick(btnTaxi.getText().toString());
                        dialogDismissTwoButtonwithText.onOkClick("1");
                    }
                }
            });


            btnBike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlg.dismiss();


                    if (dialogDismissTwoButtonwithText != null) {

                        //dialogDismissTwoButtonwithText.onOkClick(btnBike.getText().toString());
                        dialogDismissTwoButtonwithText.onOkClick("2");
                    }
                }
            });


            btnTukTuk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlg.dismiss();


                    if (dialogDismissTwoButtonwithText != null) {

                        //dialogDismissTwoButtonwithText.onOkClick(btnTukTuk.getText().toString());
                        dialogDismissTwoButtonwithText.onOkClick("3");
                    }
                }
            });

            btnOwner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlg.dismiss();

                    if (dialogDismissTwoButtonwithText != null) {

                        //dialogDismissTwoButtonwithText.onOkClick(btnOwner.getText().toString());
                        dialogDismissTwoButtonwithText.onOkClick("5");
                    }
                }
            });

            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlg.dismiss();

                }
            });

            dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            WindowManager.LayoutParams wmlp = dlg.getWindow().getAttributes();
            wmlp.gravity = Gravity.FILL_HORIZONTAL;
            dlg.setContentView(view);

            dlg.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void createRegistrationDialog(final Activity context, Point p, final CustomEditText edtName, final String string) {


        int[] location = new int[2];
               // Get the x, y location and store it in the location[] array
               // location[0] = x, location[1] = y.
               edtName.getLocationOnScreen(location);

               //Initialize the Point with x, and y positions
               p = new Point();
               p.x = location[0];
               p.y = location[1];

               int popupWidth = 200;
               int popupHeight = 150;

               // Inflate the popup_layout.xml
               LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
               LayoutInflater layoutInflater = (LayoutInflater) context
                       .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);

               // Creating the PopupWindow
               final PopupWindow popup = new PopupWindow(context);
               popup.setContentView(layout);
               // popup.setWidth(popupWidth);
               //  popup.setHeight(popupHeight);
               popup.setFocusable(true);

               // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
               int OFFSET_X = 30;
               int OFFSET_Y = -100;

               // Clear the default translucent background
               popup.setBackgroundDrawable(new BitmapDrawable());

               // Displaying the popup at the specified location, + offsets.

               popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);

               CustomTextView aa = (CustomTextView) layout.findViewById(R.id.txtDescription);
               aa.setText(string);


               // Getting a reference to Close button, and close the popup when clicked.
       /* Button close = (Button) layout.findViewById(R.id.close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });*/
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
