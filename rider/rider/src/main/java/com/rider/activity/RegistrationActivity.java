package com.rider.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
import com.rider.utils.LaoxiConstant;
import com.rider.utils.IListenerCountryCode;
import com.rider.utils.ParsingHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;

import io.michaelrocks.libphonenumber.android.AsYouTypeFormatter;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber.PhoneNumber;

/**
 * Created by hitesh on 19/7/16.
 */
public class RegistrationActivity extends BaseActivity implements View.OnClickListener, APIRequestManager.ResponseListener<User> {

    @Bind(R.id.header_back)
    ImageView headerBack;
    @Bind(R.id.header_text)
    CustomTextView headerText;
    @Bind(R.id.btnSignupFacebook)
    CustomButton btnSignupFacebook;
    @Bind(R.id.edtLastName)
    CustomEditText edtName;
    @Bind(R.id.tILName)
    TextInputLayout tILName;
    @Bind(R.id.edtEmailAddress)
    CustomEditText edtEmailAddress;
    @Bind(R.id.tIEmail)
    TextInputLayout tIEmail;
    @Bind(R.id.edtPhoneNumber)
    CustomEditText edtPhoneNumber;
    /*@Bind(R.id.tIPhone)
    TextInputLayout tIPhone;*/
    @Bind(R.id.edtGender)
    CustomTextView txtGender;
    @Bind(R.id.radioMale)
    RadioButton radioMale;
    @Bind(R.id.radioFemale)
    RadioButton radioFemale;
    @Bind(R.id.radioSex)
    RadioGroup radioSex;
    @Bind(R.id.edtPassword)
    CustomEditText edtPassword;
    @Bind(R.id.tIPassword)
    TextInputLayout tIPassword;
    @Bind(R.id.btnFareEstimate)
    CustomButton btnSignUp;
    @Bind(R.id.relative_main)
    LinearLayout relativeMain;
    /*@Bind(R.id.spinnerCountryCode)
    Spinner spinnerCountryCode;*/
    String gender = "male";
    FbHelper fb;
    String FB_ID, G_ID;

    public static boolean fbLogin;
    public static boolean googleLogin;
    @Bind(R.id.edtCountryCode)
    CustomEditText edtCountryCode;
    @Bind(R.id.txtTems)
    CustomTextView txtTems;
    @Bind(R.id.CbTemsCondition)
    CheckBox CbTemsCondition;
    private Point p;

    private PhoneNumberUtil phoneUtil;
    private AsYouTypeFormatter phoneTextFieldFormatter;
    private boolean mSelfChange = false;
    private boolean mStopFormatting;
    private String selectedCountryCode; // E.g. +61
    private String selectedIsoCountryCode = "";
    private PhoneNumberTextWatcher phoneNumberTextWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);
        ButterKnife.bind(this);
        headerText.setText("register");
        fb = new FbHelper(this);
//        getHashKey();
        txtTems.setText(Html.fromHtml("<u>Terms & Conditions</u>"));

        phoneUtil = PhoneNumberUtil.createInstance(getApplicationContext());
        selectedIsoCountryCode = getApplicationContext().getString(R.string.DEFAULT_SELECTED_COUNTRY);
        selectedCountryCode = getApplicationContext().getString(R.string.DEFAULT_SELECTED_COUNTRY_PREFIX);
        phoneNumberTextWatcher = new PhoneNumberTextWatcher(selectedIsoCountryCode);
        setPhoneNumberTextWatcher();

        if (fbLogin) {

            edtEmailAddress.setText(getIntent().getStringExtra(LaoxiConstant.EMAIL));
            edtPassword.setEnabled(false);

            edtEmailAddress.setEnabled(false);

            if(getIntent().getStringExtra(LaoxiConstant.GENDER)!=null)
            {
                if (getIntent().getStringExtra(LaoxiConstant.GENDER).equalsIgnoreCase("male")) {
                    radioMale.setChecked(true);
                    gender = "male";
                } else {
                    radioFemale.setChecked(true);
                    gender = "female";
                }
            }

            FB_ID = getIntent().getStringExtra(LaoxiConstant.FB_ID);

        }

        if(googleLogin){
            edtName.setText(getIntent().getStringExtra(LaoxiConstant.GNAME));
            edtEmailAddress.setText(getIntent().getStringExtra(LaoxiConstant.EMAIL));
            edtPassword.setVisibility(View.INVISIBLE);
            tIPassword.setVisibility(View.INVISIBLE);
            edtPassword.setEnabled(false);
            edtEmailAddress.setEnabled(false);
            G_ID = getIntent().getStringExtra(LaoxiConstant.G_ID);

        }

        LAOXI.setCurrentActivity(this);
        btnSignUp.setOnClickListener(this);
        headerBack.setOnClickListener(this);
        btnSignupFacebook.setOnClickListener(this);
        edtCountryCode.setOnClickListener(this);
        edtPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());


        radioSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioMale:
                        gender = "male";
                        break;

                    case R.id.radioFemale:
                        gender = "female";
                        break;
                }
            }
        });

        txtTems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.MAIN_URL_DISCLAIMER)));
                startActivity(browserIntent);
            }
        });

//        setSpinnerItem();
    }// end of onCreate

    @Override
    protected void onResume() {
        super.onResume();
        LAOXI.currentActivity = RegistrationActivity.this;

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
            case R.id.btnFareEstimate:
                int x = edtEmailAddress.getLeft();
                DebugLog.e("x" + x);
                validate();
                break;

            case R.id.header_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.btnSignupFacebook:
                fb.onLogin(new IFacebookTaskComplet<FbResponse>() {
                    @Override
                    public void onTaskComplete(boolean success, @Nullable FbResponse data) {
                        if (data != null) {
                            Log.e("Task", "Data " + data.getName() + data.getEmail());
                            fbLogin = true;


                            edtEmailAddress.setText(data.getEmail());
                            edtName.setText(data.getName());

                            tIPassword.setVisibility(View.INVISIBLE);
                            edtPassword.setVisibility(View.INVISIBLE);
                            edtPassword.setEnabled(false);

                            if(data.getEmail()!=null)
                            {
                                edtEmailAddress.setEnabled(false);
                            }
                            else
                            {
                                edtEmailAddress.setEnabled(true);
                            }

                            if (data.getGender().equalsIgnoreCase("male")) {
                                radioMale.setChecked(true);
                                gender = "male";
                            } else {
                                radioFemale.setChecked(true);
                                gender = "female";
                            }
                            FB_ID = data.getId();
                        } else {
                            GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(RegistrationActivity.this, "Oops, please try again", null);
                        }
                    }
                });
                break;

            case R.id.edtCountryCode:
                GeneralStaticAlertDialog.getInstance().openCountryCode(RegistrationActivity.this, new IListenerCountryCode() {
                    @Override
                    public void onSelect(String isoCountryCode) {
                        selectedIsoCountryCode = isoCountryCode;
                        edtCountryCode.setText(isoCountryCode);

                        int countryCode;

                        try {
                            // E.g. converts "AU" into +61
                            countryCode = phoneUtil.getCountryCodeForRegion(isoCountryCode);
                        } catch(Exception e) {
                            // Fallback on default prefix
                            countryCode = R.string.DEFAULT_SELECTED_COUNTRY_PREFIX;
                        }
                        selectedCountryCode = "+" + String.valueOf(countryCode);

                        // Set prefix
                        edtPhoneNumber.setText(selectedCountryCode);

                        // Create formatter for phone input field
                        phoneTextFieldFormatter = phoneUtil.getAsYouTypeFormatter(isoCountryCode);

                        setPhoneNumberTextWatcher();
                    }

                    @Override
                    public void onCancel(String strBlank) {

                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        fb.setCallbackManager(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void setPhoneNumberTextWatcher()
    {
        edtPhoneNumber.removeTextChangedListener(phoneNumberTextWatcher);

        // Set formatter
        edtPhoneNumber.addTextChangedListener(new PhoneNumberTextWatcher(selectedIsoCountryCode) {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);

                try {
                    String phoneNumber = s.toString();
                    PhoneNumber numberProto = phoneUtil.parse(phoneNumber, "");

                    String regionCode = phoneUtil.getRegionCodeForCountryCode(numberProto.getCountryCode());

                    if(regionCode.compareTo(selectedIsoCountryCode) != 0) {
                        selectedIsoCountryCode = regionCode;
                        edtCountryCode.setText(regionCode);
                        selectedCountryCode = "+" + String.valueOf(numberProto.getCountryCode());

                        setPhoneNumberTextWatcher();
                    }
                } catch(Exception e) {
                    // Ignore, perhaps user did not finish entering a phone number
                }
            }
        });}

    /**
     * Phone number input field
     */
    public class PhoneNumberTextWatcher implements TextWatcher {
        public PhoneNumberTextWatcher() {
            this(Locale.getDefault().getCountry());
        }

        PhoneNumberTextWatcher(String countryCode) {
            if (countryCode == null) throw new IllegalArgumentException();
            phoneTextFieldFormatter = phoneUtil.getAsYouTypeFormatter(countryCode);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            if (mSelfChange || mStopFormatting) {
                return;
            }
            // If the user manually deleted any non-dialable characters, stop formatting
            if (count > 0 && hasSeparator(s, start, count)) {
                stopFormatting();
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mSelfChange || mStopFormatting) {
                return;
            }
            // If the user inserted any non-dialable characters, stop formatting
            if (count > 0 && hasSeparator(s, start, count)) {
                stopFormatting();
            }
        }

        @Override
        public synchronized void afterTextChanged(Editable s) {
            if (mStopFormatting) {
                // Restart the formatting when all texts were clear.
                mStopFormatting = !(s.length() == 0);
                return;
            }
            if (mSelfChange) {
                // Ignore the change caused by s.replace().
                return;
            }
            String formatted = reformat(s, Selection.getSelectionEnd(s));
            if (formatted != null) {
                int rememberedPos = phoneTextFieldFormatter.getRememberedPosition();
                mSelfChange = true;
                s.replace(0, s.length(), formatted, 0, formatted.length());
                // The text could be changed by other TextWatcher after we changed it. If we found the
                // text is not the one we were expecting, just give up calling setSelection().
                if (formatted.equals(s.toString())) {
                    Selection.setSelection(s, rememberedPos);
                }
                mSelfChange = false;
            }
        }
        /**
         * Generate the formatted number by ignoring all non-dialable chars and stick the cursor to the
         * nearest dialable char to the left. For instance, if the number is  (650) 123-45678 and '4' is
         * removed then the cursor should be behind '3' instead of '-'.
         */
        private String reformat(CharSequence s, int cursor) {
            // The index of char to the leftward of the cursor.
            int curIndex = cursor - 1;
            String formatted = null;
            phoneTextFieldFormatter.clear();
            char lastNonSeparator = 0;
            boolean hasCursor = false;
            int len = s.length();
            for (int i = 0; i < len; i++) {
                char c = s.charAt(i);
                if (PhoneNumberUtils.isNonSeparator(c)) {
                    if (lastNonSeparator != 0) {
                        formatted = getFormattedNumber(lastNonSeparator, hasCursor);
                        hasCursor = false;
                    }
                    lastNonSeparator = c;
                }
                if (i == curIndex) {
                    hasCursor = true;
                }
            }
            if (lastNonSeparator != 0) {
                formatted = getFormattedNumber(lastNonSeparator, hasCursor);
            }
            return formatted;
        }

        private String getFormattedNumber(char lastNonSeparator, boolean hasCursor) {
            return hasCursor ? phoneTextFieldFormatter.inputDigitAndRememberPosition(lastNonSeparator)
                    : phoneTextFieldFormatter.inputDigit(lastNonSeparator);
        }

        private void stopFormatting() {
            mStopFormatting = true;
            phoneTextFieldFormatter.clear();
        }

        private boolean hasSeparator(final CharSequence s, final int start, final int count) {
            for (int i = start; i < start + count; i++) {
                char c = s.charAt(i);
                if (!PhoneNumberUtils.isNonSeparator(c)) {
                    return true;
                }
            }
            return false;
        }
    }
    /**
     * Validations for sign up screen based on normal user or fb user.
     */

    private void validate() {

        if (fbLogin) {
            // Facebook registration
            if (isEditTextEmpty(edtName)) {
                showSnackBar(getString(R.string.please_enter_name), edtName);
                //GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, p, edtName, getString(R.string.please_enter_name));
                //GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, getString(R.string.please_enter_name), edtName);
            } else if (isEditTextEmpty(edtPhoneNumber)) {
                showSnackBar(getString(R.string.please_enter_phone_number), edtPhoneNumber);
                //GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, p, edtPhoneNumber, getString(R.string.please_enter_phone_number));
                // GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, getString(R.string.please_enter_phone_number), edtPhoneNumber);
            } else if (edtPhoneNumber.getText().length() < 5 || edtPhoneNumber.getText().length() > 15) {
                showSnackBar(getString(R.string.please_enter_valid_phone), edtPhoneNumber);
                //GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, p, edtPhoneNumber, getString(R.string.please_enter_valid_phone));
                // GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, getString(R.string.please_enter_valid_phone), edtPhoneNumber);
            } else if (!CbTemsCondition.isChecked()) {
                showSnackBar(getString(R.string.signup_fragment_agree_terms_and_conditions), edtName);
               // GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, p, edtPhoneNumber, getString(R.string.signup_fragment_agree_terms_and_conditions));
            }else {
                wsCallSignUp();
            }

        } else if(googleLogin) {
            // GoogleInfo registration
            if (isEditTextEmpty(edtName)) {
                showSnackBar(getString(R.string.please_enter_name), edtName);
                //GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, p, edtName, getString(R.string.please_enter_name));
                //GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, getString(R.string.please_enter_name), edtName);
            } else if (isEditTextEmpty(edtPhoneNumber)) {
                showSnackBar(getString(R.string.please_enter_phone_number), edtPhoneNumber);
                //GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, p, edtPhoneNumber, getString(R.string.please_enter_phone_number));
                // GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, getString(R.string.please_enter_phone_number), edtPhoneNumber);
            } else if (edtPhoneNumber.getText().length() < 5 || edtPhoneNumber.getText().length() > 15) {
                showSnackBar(getString(R.string.please_enter_valid_phone), edtPhoneNumber);
                //GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, p, edtPhoneNumber, getString(R.string.please_enter_valid_phone));
                // GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, getString(R.string.please_enter_valid_phone), edtPhoneNumber);
            } else if (!CbTemsCondition.isChecked()) {
                showSnackBar(getString(R.string.signup_fragment_agree_terms_and_conditions), edtName);
                // GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, p, edtPhoneNumber, getString(R.string.signup_fragment_agree_terms_and_conditions));
            }else {
                wsCallSignUp();
            }
        } else {
            // simple registration

            if (isEditTextEmpty(edtName)) {
                showSnackBar(getString(R.string.please_enter_name), edtName);
                //GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, p, edtName, getString(R.string.please_enter_name));
                // GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, getString(R.string.please_enter_name), edtName);
            } else if (isEditTextEmpty(edtEmailAddress)) {
                showSnackBar(getString(R.string.please_enter_email), edtEmailAddress);
                //GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, p, edtEmailAddress, getString(R.string.please_enter_email));
                // GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, getString(R.string.please_enter_email), edtEmailAddress);
            } else if (!isValidEmail(edtEmailAddress)) {
                showSnackBar(getString(R.string.please_enter_valid_email), edtEmailAddress);
                //GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, p, edtEmailAddress, getString(R.string.please_enter_valid_email));
                // GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, getString(R.string.please_enter_valid_email), edtEmailAddress);
            } else if (isEditTextEmpty(edtPhoneNumber)) {
                showSnackBar(getString(R.string.please_enter_phone_number), edtPhoneNumber);
                //GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, p, edtPhoneNumber, getString(R.string.please_enter_phone_number));
                // GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, getString(R.string.please_enter_phone_number), edtPhoneNumber);
            } else if (edtPhoneNumber.getText().length() < 5 || edtPhoneNumber.getText().length() > 15) {
                showSnackBar(getString(R.string.please_enter_valid_phone), edtPhoneNumber);
                //GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, p, edtPhoneNumber, getString(R.string.please_enter_valid_phone));
                // GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, getString(R.string.please_enter_valid_phone), edtPhoneNumber);
            } else if (edtPassword.getText().length() < 6) {
                showSnackBar(getString(R.string.password_should_be_minimum_6_characters), edtPassword);
                //GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, p, edtPassword, getString(R.string.password_should_be_minimum_6_characters));
                // GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, getString(R.string.password_should_be_minimum_6_characters), edtPassword);
            }else if (!CbTemsCondition.isChecked()) {
                showSnackBar(getString(R.string.signup_fragment_agree_terms_and_conditions), edtName);
               // GeneralStaticAlertDialog.getInstance().createRegistrationDialog(RegistrationActivity.this, p, edtPhoneNumber, getString(R.string.signup_fragment_agree_terms_and_conditions));
            } else {
                wsCallSignUp();
            }
        }
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

    public boolean isValidEmail(EditText edtText) {
        if (edtText.getText().toString().trim() == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(edtText.getText().toString().trim()).matches();
        }
    }

    public boolean isEditTextEmpty(EditText editText) {
        if (editText.getText().toString().trim().isEmpty())
            return true;
        return false;
    }


    /**
     * Call a sign up api.
     */

    public void wsCallSignUp() {
        String phoneNumber = edtPhoneNumber.getText().toString();
        try {
            PhoneNumber numberProto = phoneUtil.parse(edtPhoneNumber.getText().toString(), "");
            phoneNumber = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
            phoneNumber = phoneNumber.replaceAll("\\s+","");
        } catch(Exception e) {
            // Ignore parsing error, given phone number is used
        }

        if (fbLogin) {
            AlertUtils.showCustomProgressDialog(this);
            ApiService api = RetroClient.getApiService();
            Call<User> call = api.FBSignUp("", edtEmailAddress.getText().toString(), FB_ID, phoneNumber, edtName.getText().toString(), selectedCountryCode, gender);

            APIRequestManager<User> apiRequestManager = new APIRequestManager<>(RegistrationActivity.this, call);
            apiRequestManager.execute();
        } else if(googleLogin){
            AlertUtils.showCustomProgressDialog(this);
            ApiService api = RetroClient.getApiService();
            Call<User> call = api.GSignUp("", edtEmailAddress.getText().toString(), G_ID, phoneNumber, edtName.getText().toString(), selectedCountryCode, gender);

            APIRequestManager<User> apiRequestManager = new APIRequestManager<>(RegistrationActivity.this, call);
            apiRequestManager.execute();
        }
        else {

            AlertUtils.showCustomProgressDialog(this);
            ApiService api = RetroClient.getApiService();

            Call<User> call = api.SignUp("", edtEmailAddress.getText().toString(), StringToUtf(edtPassword.getText().toString()), phoneNumber, edtName.getText().toString(), selectedCountryCode, gender);

            APIRequestManager<User> apiRequestManager = new APIRequestManager<>(RegistrationActivity.this, call);
            apiRequestManager.execute();
        }
    }

    /**
     * Set a parameter for call a sign up api.
     */

    public HashMap<String, String> setSignUpData() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.EMAIL, edtEmailAddress.getText().toString());

        param.put(LaoxiConstant.PHONE, edtPhoneNumber.getText().toString());
        param.put(LaoxiConstant.NAME, edtName.getText().toString());
        param.put(LaoxiConstant.COUNTRY_CODE, selectedCountryCode);
        param.put(LaoxiConstant.GENDER, gender);
        if (fbLogin) {
            param.put(LaoxiConstant.FB_ID, FB_ID);
        } else {
            param.put(LaoxiConstant.PASSWORD, StringToUtf(edtPassword.getText().toString()));
        }

        return param;

    }

  /*  private void setSpinnerItem() {

        final ArrayList<String> listCountryCode = new ArrayList<String>();
        listCountryCode.add("Country Code");
        listCountryCode.add("+61");
        CustomSpinnerRegistration customSpinnerNoDrawableAdapter = new CustomSpinnerRegistration(RegistrationActivity.this, listCountryCode, "");
        spinnerCountryCode.setAdapter(customSpinnerNoDrawableAdapter);
    }*/

    private void getHashKey() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.rider", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }

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

    @Override
    public void onResponse(retrofit2.Response<User> response) {
        AlertUtils.dismissDialog();
        if (response.isSuccessful()) {
            DebugLog.i("Login Successful : "+response.body());

            User user = response.body();
            DebugLog.i("Login User Data : "+user);
            if (response.code() == 201){
                if (user.getLoginType().equalsIgnoreCase("S")) {
                    // Normal user
                    DebugLog.e("data is======>" + user);

                    String data = new Gson().toJson(user);
                    DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA, data);

                    Intent intent = new Intent(RegistrationActivity.this, OTPActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    // Facebook or Google user
                    DebugLog.e("data is======>" + user);

                    String data = new Gson().toJson(user);
                    DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA, data);

                    Intent intent = new Intent(RegistrationActivity.this, OTPActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        } else {
            if (response.code() == 400) {
//                    showSnackBar("Please enter correct email or password", edtPassword);
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
            }
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        AlertUtils.dismissDialog();
    }
}// end of class
