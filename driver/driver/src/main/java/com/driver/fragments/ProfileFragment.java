package com.driver.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.siyamed.shapeimageview.ImageViewProfile;
import com.driver.R;
import com.driver.activity.HomeActivity;
import com.driver.application.LAOXI;
import com.driver.customclasses.CustomButton;
import com.driver.customclasses.CustomEditText;
import com.driver.customclasses.CustomTextView;
import com.driver.dao.IAsyncCompleteCallback;
import com.driver.dao.ParsingHelper;
import com.driver.dao.homedao.HomeActivityDAO;
import com.driver.dialog.ChangePasswordDialog;
import com.driver.dialog.GeneralStaticAlertDialog;
import com.driver.listener.ISelectedCarType;
import com.driver.navigator.NavigatorInterface;
import com.driver.pojo.GenericResponse;
import com.driver.pojo.TrackRidePojo.TrackRide;
import com.driver.pojo.loginws.LoginResponse;
import com.driver.retrofit.APIRequestManager;
import com.driver.retrofit.ApiService;
import com.driver.retrofit.RetroClient;
import com.driver.util.AlertUtils;
import com.driver.util.AppHelper;
import com.driver.util.AsteriskPasswordTransformationMethod;
import com.driver.util.ConstantClass;
import com.driver.util.DebugLog;
import com.driver.util.DialogDismissOneButton;
import com.driver.util.EnumHelper;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;

import static com.twilio.client.impl.TwilioImpl.context;

/**
 * Created by hlink54 on 19/7/16.
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener, ISelectedCarType, APIRequestManager.ResponseListener<LoginResponse> {

    private static final int EXTERNAL_READ = 1;


    NavigatorInterface navigatorInterface;
    @Bind(R.id.header_text)
    CustomTextView headerText;
    @Bind(R.id.imgEdit)
    ImageView imgEdit;
    @Bind(R.id.header_menu)
    ImageView headerMenu;
    @Bind(R.id.imageViewProfile)
    ImageViewProfile imageViewProfile;
    @Bind(R.id.imageViewCar)
    ImageViewProfile imageViewCar;
    @Bind(R.id.layoutProfile)
    LinearLayout layoutProfile;
    @Bind(R.id.edtFirstName)
    CustomEditText edtFirstName;
    @Bind(R.id.tIFName)
    TextInputLayout tIFName;
    @Bind(R.id.layoutName)
    LinearLayout layoutName;
    @Bind(R.id.edtEmailAddress)
    CustomEditText edtEmailAddress;
    @Bind(R.id.tIEmailAddress)
    TextInputLayout tIEmailAddress;
    @Bind(R.id.edtPhoneNumber)
    CustomEditText edtPhoneNumber;
    @Bind(R.id.tIPhone)
    TextInputLayout tIPhone;
    @Bind(R.id.edtPassword)
    CustomEditText edtPassword;
    @Bind(R.id.tIPassword)
    TextInputLayout tIPassword;
    @Bind(R.id.edtCarType)
    CustomEditText edtCarType;
    @Bind(R.id.tiCarType)
    TextInputLayout tiCarType;
    @Bind(R.id.btnUpdate)
    CustomButton btnUpdate;
    @Bind(R.id.relative_main)
    RelativeLayout relativeMain;

    private HomeActivity homeActivity;
    LoginResponse loginData;
    private String path;
    String ProfilePickPath = null, CarPicPath = null;
    private String selectedCar = "";
    private File file;
    private String PickImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        imgEdit.setVisibility(View.VISIBLE);
        imgEdit.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        headerMenu.setOnClickListener(this);
        edtPassword.setOnClickListener(this);
        edtPassword.setEnabled(false);
        imageViewProfile.setOnClickListener(this);
        edtCarType.setOnClickListener(this);
        edtCarType.setEnabled(false);
        imageViewProfile.setClickable(false);
        imageViewCar.setClickable(false);
        imageViewCar.setEnabled(false);
        imageViewCar.setOnClickListener(this);

        btnUpdate.setVisibility(View.INVISIBLE);
        DebugLog.i("Image path " + ConstantClass.IMAGE_URL + AppHelper.getInstance().getLoginUser().getProfileImage());
        Picasso.with(context)
                .load(ConstantClass.THUMB_IMAGE_URL + AppHelper.getInstance().getLoginUser().getProfileImage())
                .fit()
                .placeholder(R.drawable.avatar_icon)
                .into(imageViewProfile);

        Picasso.with(context)
                .load(ConstantClass.THUMB_CAR_TYPE_IMAGE + AppHelper.getInstance().getLoginUser().getCarTypeImage())
                .fit()
                .placeholder(R.drawable.car_default)
                .into(imageViewCar);


      /*  Picasso.with(getActivity())
                .load(R.drawable.profile)

                .into(imageViewProfile);*/
        edtPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        loginData = AppHelper.getInstance().getLoginUser();
        // set whatever car is default selected
        selectedCar = loginData.getCarType();

        HashMap<String, String> profileHashMap = new HashMap<>();
        profileHashMap.put(ConstantClass.DRIVER_ID, loginData.getDriverId());
        callProfileData(profileHashMap, loginData.getToken());
        headerText.setText("hi " + loginData.getFname() + "!");
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
            homeActivity = (HomeActivity) context;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgEdit:
                enableAllField();
                break;

            case R.id.btnUpdate:
                validate();
                break;
            case R.id.edtCarType:
               /* SelectCarTypeDialog selectCarTypeDialog = new SelectCarTypeDialog();
                selectCarTypeDialog.setCallback(this, selectedCar);
                selectCarTypeDialog.show(getFragmentManager(), selectCarTypeDialog.getClass().getName());*/
                break;
            case R.id.header_menu:
                HomeActivity.toggleDrawer();
                break;
            case R.id.edtPassword:
                ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog();
                changePasswordDialog.createDialog(getActivity(), "", null);

                break;

            case R.id.imageViewProfile:
                PickImage = "1";
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        startDialog();
                    }
                } else {
                    startDialog();
                }
                break;

            case R.id.imageViewCar:
                PickImage = "2";
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        startDialog();
                    }
                } else {
                    startDialog();
                }
                break;
        }
    }


    /**
     * Enable all field which is editable by driver.
     */

    private void enableAllField() {
        btnUpdate.setVisibility(View.VISIBLE);
        edtFirstName.setFocusableInTouchMode(true);
        edtFirstName.requestFocus();
        edtFirstName.setSelection(edtFirstName.getText().toString().length());
        edtCarType.setEnabled(false);
        edtCarType.setFocusableInTouchMode(false);

        edtPassword.setEnabled(true);
        edtPassword.setFocusableInTouchMode(false);

        edtEmailAddress.setFocusableInTouchMode(true);
        edtPhoneNumber.setFocusableInTouchMode(true);
        imageViewProfile.setClickable(true);
        imageViewCar.setClickable(true);
        imageViewCar.setEnabled(true);

    }


 /*   private void setSpinnerItem() {

        final ArrayList<String> listCountryCode = new ArrayList<String>();
        listCountryCode.add("Country Code");
        listCountryCode.add("+61");
        listCountryCode.add("+91");
        listCountryCode.add("+51");
        CustomSpinnerRegistration customSpinnerNoDrawableAdapter = new CustomSpinnerRegistration(getActivity(), listCountryCode, "");
        spinnerCountryCode.setEnabled(false);
        spinnerCountryCode.setClickable(false);
        spinnerCountryCode.setAdapter(customSpinnerNoDrawableAdapter);
        spinnerCountryCode.setSelection(1);
    }*/

    @NonNull
    private RequestBody createPartFromString(String desc) {
        return RequestBody.create(MultipartBody.FORM, desc);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String fileUrl)
    {
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
        File image = new File(fileUrl);

        return MultipartBody.Part.createFormData(partName, image.getName(), RequestBody.create(MEDIA_TYPE_PNG, image));
    }

    /**
     * Check a validations of all field.
     */

    private void validate() {


        String fileName = null;

        String pathimage = null;

        if (ProfilePickPath != null && CarPicPath != null) {
            pathimage = ProfilePickPath + ",," + CarPicPath;
            fileName = "profile_image" + ",," + "car_type_image";
        } else {
            if (ProfilePickPath != null) {
                pathimage = ProfilePickPath;
                fileName = "profile_image";
            }

            if (CarPicPath != null) {
                pathimage = CarPicPath;
                fileName = "car_type_image";
            }

        }


        if (isEditTextEmpty(edtFirstName)) {
            showSnackBar(getString(R.string.please_enter_first_name), edtFirstName, relativeMain);
        } else if (isEditTextEmpty(edtEmailAddress)) {
            showSnackBar(getString(R.string.please_enter_email), edtEmailAddress, relativeMain);
        } else if (!isValidEmail(edtEmailAddress)) {
            showSnackBar(getString(R.string.please_enter_valid_email), edtEmailAddress, relativeMain);
        } else if (isEditTextEmpty(edtPhoneNumber)) {
            showSnackBar(getString(R.string.please_enter_phone_number), edtPhoneNumber, relativeMain);
        } else if (edtPhoneNumber.getText().length() < 5 || edtPhoneNumber.getText().length() > 15) {
            showSnackBar(getString(R.string.please_enter_valid_phone), edtPhoneNumber, relativeMain);
        } else if (edtPassword.getText().length() < 5) {
            showSnackBar(getString(R.string.password_should_be_minimum_6_characters), edtPassword, relativeMain);
        } else {

            String str = edtFirstName.getText().toString();
            String arrayString[] = str.split("\\s+");

            String lname;
            if (arrayString.length > 1) {
                lname = arrayString[1];
            } else {
                lname = loginData.getLname();
            }


            String strToken = AppHelper.getInstance().getToken();
            String strDriverId = AppHelper.getInstance().getDriverId();
            String strFirstName = arrayString[0];
            String strLastName = lname;
            String strPhone = edtPhoneNumber.getText().toString();
            String strCountryCode = loginData.getCountryCode();

            DebugLog.i("Update Profile Request");
            ApiService api = RetroClient.getApiService();

            List<MultipartBody.Part> parts = new ArrayList<>();

            if ( pathimage != null) {

                String aa[] =  pathimage.split(",,");

                String bb[] =  fileName.split(",,");

                for (int i = 0; i < aa.length; i++ ){
                    parts.add(prepareFilePart(bb[i], aa[i]));
                }
            }
            Call<LoginResponse> call = api.callUpdateProfileForMultipleImage(strToken, createPartFromString(strDriverId), createPartFromString(strFirstName), createPartFromString(strLastName), createPartFromString(strPhone), createPartFromString(strCountryCode), parts);

            APIRequestManager<LoginResponse> apiRequestManager = new APIRequestManager<>(new APIRequestManager.ResponseListener<LoginResponse>() {
                @Override
                public void onResponse(retrofit2.Response<LoginResponse> response) {
                    AlertUtils.dismissDialog();
                    if ( response.isSuccessful()) {
                        if (response.code() == ConstantClass.RESPONSE_CODE_200) {
                            LoginResponse loginResponse = response.body();

                            DebugLog.i("Update Profile Success code: "+ response.code());

                            LAOXI.getInstance().setLoginUser(loginResponse);
                            AppHelper.getInstance().SaveInSharePrefWithBoolean(ConstantClass.IS_LOGIN, true);
                            EventBus.getDefault().post(loginResponse);
                            homeActivity.updateDriverProfile();

                            GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(getActivity(), "Profile updated successfully" + "", new DialogDismissOneButton() {
                                @Override
                                public void onDismiss() {
                                    homeActivity.onBackPressed();
                                }
                            });
                        }
                    } else {
                        DebugLog.i("Update Profile Failed code: "+ response.code());
                        try {
                            String s = response.errorBody().string();
                            if (response.code() == ConstantClass.RESPONSE_CODE_400) {

                                GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                                showSnackBar(responseBody.getMessage(), null, relativeMain);
                            } else if (response.code() == ConstantClass.RESPONSE_CODE_401) {
                                homeActivity.Logout(false);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    DebugLog.e("Update Request Failed Msg :" + throwable.getMessage());
                    AlertUtils.dismissDialog();
                    showSnackBar(getString(R.string.bad_server), null, relativeMain);
                }
            }, call);
            apiRequestManager.execute();
        }
    }


    /**
     * Call a driver details api for getting driver details.
     */

    private void callProfileData(HashMap<String, String> param, String token) {

        AlertUtils.showCustomProgressDialog(getActivity());

        String strToken = AppHelper.getInstance().getToken();
        String strDriverId = AppHelper.getInstance().getDriverId();

        ApiService api = RetroClient.getApiService();
        Call<LoginResponse> call = api.callGetProfileData(strToken, strDriverId);
        DebugLog.i("Calling Profile Data");
        APIRequestManager<LoginResponse> apiRequestManager = new APIRequestManager<>(this, call);
        apiRequestManager.execute();
    }

    @Override
    public void onResponse(retrofit2.Response<LoginResponse> response) {
        AlertUtils.dismissDialog();
        if (response.isSuccessful()) {
            DebugLog.i("Profile load Success Response :" + response);

            LoginResponse loginResponse = response.body();

            if (imageViewProfile != null)
                Picasso.with(getActivity())
                        .load(ConstantClass.THUMB_IMAGE_URL + loginResponse.getProfileImage())
                        .fit()
                        .into(imageViewProfile);

            edtFirstName.setText("" + loginResponse.getFname() + " " + loginResponse.getLname());
            edtEmailAddress.setText("" + loginResponse.getEmail() + "");
            edtPhoneNumber.setText(loginResponse.getPhone());

            edtEmailAddress.setEnabled(false);
            edtCarType.setText(loginResponse.getCarType().toLowerCase());

        } else {
            try {
                String s = response.errorBody().string();
                DebugLog.i("Profile load Failed :" + s);

                if (response.code() == ConstantClass.RESPONSE_CODE_400) {
                    GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                    showSnackBar(responseBody.getMessage(), null, relativeMain);
                } else {
                    GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                    showSnackBar(responseBody.getMessage(), null, relativeMain);
                }
            } catch ( IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        DebugLog.e("Profile Request Failed :" + throwable.getMessage());
        AlertUtils.dismissDialog();
        showSnackBar(getString(R.string.bad_server), null, relativeMain);
    }

    private void callUpdateProfile(HashMap<String, String> param, String filename, String path, String token) {
        AlertUtils.showCustomProgressDialog(getContext());
//        String requestUrl = context.getResources().getString(R.string.MAINURL)+context.getResources().getString(R.string.EDIT_PROFILE);

        HomeActivityDAO.getInstance().callUpdateProfile(param, filename, path, token, ConstantClass.METHOD_UPDATE_PROFILE, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                AlertUtils.dismissDialog();
                if (success) {

                    try {

                        if (data.code() == ConstantClass.RESPONSE_CODE_200) {
                            String response = data.body().string();

                            DebugLog.i("data is a---------------------" + response);

                            LoginResponse loginResponse = ParsingHelper.getInstance().loginResponseParsing(response);

                            AppHelper.getInstance().SaveInSharedPref(ConstantClass.LOGIN_USER, response);
                            AppHelper.getInstance().SaveInSharePrefWithBoolean(ConstantClass.IS_LOGIN, true);
                            EventBus.getDefault().post(loginResponse);
                            homeActivity.updateDriverProfile();

                            //event post for loginresponce


                            GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(getActivity(), "Profile updated successfully" + "", new DialogDismissOneButton() {
                                @Override
                                public void onDismiss() {
                                    homeActivity.onBackPressed();
                                }
                            });
                        } else if (data.code() == ConstantClass.RESPONSE_CODE_400) {
                            String s = data.body().string();
                            GenericResponse response = ParsingHelper.getInstance().genericResponseParsing(s);
                            showSnackBar(response.getMessage(), null, relativeMain);
                        } else if (data.code() == ConstantClass.RESPONSE_CODE_401) {
                            homeActivity.Logout(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    showSnackBar(getString(R.string.bad_server), null, relativeMain);
                }
            }
        });
    }



    /**
     * Call a update profile api.
     */

    private void callUpdateProfileForBothImage(HashMap<String, String> param, String filename, String path, String token) {
        AlertUtils.showCustomProgressDialog(getContext());
//        String requestUrl = context.getResources().getString(R.string.MAINURL)+context.getResources().getString(R.string.EDIT_PROFILE);

        HomeActivityDAO.getInstance().callUpdateProfileForMultipleImage(param, filename, path, token, ConstantClass.METHOD_UPDATE_PROFILE, new IAsyncCompleteCallback<Response>() {
            @Override
            public void onTaskComplete(boolean success, @Nullable Response data) {
                AlertUtils.dismissDialog();
                if (success) {

                    try {

                        if (data.code() == ConstantClass.RESPONSE_CODE_200) {
                            String response = data.body().string();

                            DebugLog.i("data is a---------------------" + response);

                            LoginResponse loginResponse = ParsingHelper.getInstance().loginResponseParsing(response);

                            AppHelper.getInstance().SaveInSharedPref(ConstantClass.LOGIN_USER, response);
                            AppHelper.getInstance().SaveInSharePrefWithBoolean(ConstantClass.IS_LOGIN, true);
                            EventBus.getDefault().post(loginResponse);
                            homeActivity.updateDriverProfile();

                            GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(getActivity(), "Profile updated successfully" + "", new DialogDismissOneButton() {
                                @Override
                                public void onDismiss() {
                                    homeActivity.onBackPressed();
                                }
                            });
                        } else if (data.code() == ConstantClass.RESPONSE_CODE_400) {
                            String s = data.body().string();
                            GenericResponse response = ParsingHelper.getInstance().genericResponseParsing(s);
                            showSnackBar(response.getMessage(), null, relativeMain);
                        } else if (data.code() == ConstantClass.RESPONSE_CODE_401) {
                            homeActivity.Logout(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    showSnackBar(getString(R.string.bad_server), null, relativeMain);
                }
            }
        });
    }


    /**
     * Check permissions for a camera and gallery for marshmallow.
     */

    public boolean checkPermission() {
        if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            if ((shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))) {

                showSnackBar("Allow us to External Storage permissions to access gallery from setting/app/Hi Oscar Driver/permisiions", edtPassword, relativeMain);

            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, EXTERNAL_READ);

            }
            return false;
        } else {
            return true;
        }
    }



    /**
     * Open a dialog for choosing a photo from camera or gallery.
     */

    private void startDialog() {


        final Dialog dialogCameraGallry = new Dialog(getContext());
        // this line removes title
        dialogCameraGallry.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCameraGallry.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //tell the Dialog to use the dialog.xml as it's layout description
        dialogCameraGallry.setContentView(R.layout.image_picker);
        dialogCameraGallry.setTitle("");

        ImageView imgViewGallery = (ImageView) dialogCameraGallry.findViewById(R.id.img_gallery);
        ImageView imgViewCamera = (ImageView) dialogCameraGallry.findViewById(R.id.img_camera);


        //		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        //		myAlertDialog.setTitle("Change Pictures Option");
        //		myAlertDialog.setMessage("How do you want to set your picture?");

        imgViewCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                captureImage();
                dialogCameraGallry.dismiss();

            }
        });

        imgViewGallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                uploadImage();
                dialogCameraGallry.dismiss();

            }
        });

        dialogCameraGallry.show();
    }

    public void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    public void uploadImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
    }



    /**
     * Get a callback when driver allow or deny a permissions.
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_READ: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startDialog();
                } else {

                }
                return;
            }
            default:

                break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * Get a callback when user select photo from gallery or photo.
     */

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final int maxMbSize = 2 * 1024 * 1024;
        if (resultCode == getActivity().RESULT_OK && requestCode == 1) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            if (thumbnail != null) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                DebugLog.i("file size is a=============" + destination.length());
                if (destination.length() <= maxMbSize) {

                    if (PickImage.equals("1")) {

                        ProfilePickPath = compressImage(destination.getAbsolutePath());
                        path = destination.getAbsolutePath();
                        Picasso.with(getContext())
                                .load(destination)
                                .fit()
                                .into(imageViewProfile);
                    }

                    if (PickImage.equals("2")) {
                        CarPicPath = compressImage(destination.getAbsolutePath());
                        path = destination.getAbsolutePath();
                        Picasso.with(getContext())
                                .load(destination)
                                .fit()
                                .into(imageViewCar);
                    }
                } else {
                    path = null;
                    DebugLog.e("file not uploaded");
                }


            }
        } else if (resultCode == getActivity().RESULT_OK && requestCode == 2) {

            Uri uri = data.getData();
            try {
                Cursor cursor = getActivity().getContentResolver().query(data.getData(), null, null, null, null);

                if (cursor == null) {
                    path = uri.getPath();

                } else {
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cursor.getString(idx);
                    cursor.close();
                }
                File f = new File(path);
                Bitmap thumbnail = (Bitmap) MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                if (thumbnail != null) {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 75, bytes);
                    file = new File(getActivity().getCacheDir(), f.getName());
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(bytes.toByteArray());
                    fos.flush();
                    fos.close();
                }

                DebugLog.i("file size is a=============" + file.length());
                if (file.length() <= maxMbSize) {

                    if (PickImage.equals("1")) {
                        ProfilePickPath = compressImage(path);
                        Picasso.with(getActivity()).load(file).fit().into(imageViewProfile);
                    }
                    if (PickImage.equals("2")) {
                        CarPicPath = compressImage(path);
                        Picasso.with(getContext())
                                .load(file)
                                .fit()
                                .into(imageViewCar);
                    }

                } else {
                    path = null;
                    DebugLog.e("file not uploaded");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


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
    public void selectedCarType(EnumHelper.CarType carType) {
        DebugLog.i("Selected Car Type :: " + carType);
        edtCarType.setText(carType.getSeletedString().toLowerCase());
        selectedCar = carType.getSeletedString().toString();
        switch (carType) {

            case OSCAR:
                edtCarType.setText("oscar");

                break;
            case BIG_OSCAR:
                edtCarType.setText("big oscar");
                break;
            case FANCY_OSCAR:
                edtCarType.setText("fancy oscar");
                break;
        }
    }



    /**
     * Compress a large image size to small image size.
     */

    public String compressImage(String filePath) {

        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 1920.0f;//1280.0f;//816.0f;
        float maxWidth = 1080.0f;//852.0f;//612.0f;

        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "oscar/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }


    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

}// end of main class
