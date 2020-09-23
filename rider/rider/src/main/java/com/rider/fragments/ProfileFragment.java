package com.rider.fragments;

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
import com.google.gson.Gson;
import com.rider.R;
import com.rider.activity.BaseActivity;
import com.rider.activity.HomeActivity;
import com.rider.customclasses.CustomButton;
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
import com.rider.utils.AsteriskPasswordTransformationMethod;
import com.rider.utils.DataToPref;
import com.rider.utils.DebugLog;
import com.rider.utils.DialogDismissChangePassword;
import com.rider.utils.DialogDismissOneButton;
import com.rider.utils.LaoxiConstant;
import com.rider.utils.ParsingHelper;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by hlink54 on 19/7/16.
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener {


    private static final int EXTERNAL_READ = 1;

    NavigatorInterface navigatorInterface;
    @Bind(R.id.header_menu)
    ImageView headerMenu;
    @Bind(R.id.header_text)
    CustomTextView headerText;
    @Bind(R.id.img_share)
    ImageView imgShare;
    @Bind(R.id.imgEdit)
    ImageView imgEdit;
    @Bind(R.id.layoutProfile)
    RelativeLayout layoutProfile;
    @Bind(R.id.edtName)
    CustomEditText edtName;
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
    @Bind(R.id.tIPhoneNumber)
    TextInputLayout tIPhoneNumber;
    @Bind(R.id.edtGender)
    CustomEditText edtGender;
    @Bind(R.id.tIGender)
    TextInputLayout tIGender;
    @Bind(R.id.edtPassword)
    CustomEditText edtPassword;
    @Bind(R.id.tIPassword)
    TextInputLayout tIPassword;
    @Bind(R.id.btnUpdate)
    CustomButton btnUpdate;
    @Bind(R.id.relative_main)
    RelativeLayout relativeMain;
    @Bind(R.id.imageViewProfile)
    ImageViewProfile imageViewProfile;

    private HomeActivity homeActivity;
    private User user;
    private String path;
    private File file;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        imgShare.setVisibility(View.GONE);

        //Get data from shared prefrence

        String strUserData = DataToPref.getSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA);
        Gson mGson = new Gson();
        user = mGson.fromJson(strUserData, User.class);

        headerText.setText("hi " + user.getName() + "!");
        imgEdit.setOnClickListener(this);
        imageViewProfile.setOnClickListener(this);
        imageViewProfile.setClickable(false);
        btnUpdate.setOnClickListener(this);
        edtPassword.setOnClickListener(this);
        edtPassword.setEnabled(false);
        // edtCountryCode.setOnClickListener(this);

        btnUpdate.setVisibility(View.INVISIBLE);


        if (user != null) {
            Picasso.with(getActivity())
                    .load(getResources().getString(R.string.MAINURL_FOR_IMAGE) + user.getProfileImage())
                    .fit()
                    .placeholder(R.drawable.avatar_icon)
                    .into(imageViewProfile);

            edtName.setText(user.getName());
            edtPhoneNumber.setText(user.getCountryCode() + " " + user.getPhone());

            edtEmailAddress.setText(user.getEmail());
            edtGender.setText(user.getGender());


            if (!user.getFbId().equalsIgnoreCase("")) {
                edtPassword.setEnabled(false);
            }

        }


        edtPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        return view;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navigatorInterface.closeDrawer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.header_menu)
    public void onClick() {

        HomeActivity.toggleDrawer();
    }


 /*   private void setSpinnerItem() {

        final ArrayList<String> listCountryCode = new ArrayList<String>();
        listCountryCode.add("Country Code");
        listCountryCode.add("+61");
        CustomSpinnerRegistration customSpinnerNoDrawableAdapter = new CustomSpinnerRegistration(getActivity(), listCountryCode, "");
        spinnerCountryCode.setEnabled(false);
        spinnerCountryCode.setClickable(false);
        spinnerCountryCode.setAdapter(customSpinnerNoDrawableAdapter);
        spinnerCountryCode.setSelection(1);
    }*/


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgEdit:
                enableAllField();

//                navigatorInterface.openPhotoGuidelines();

                break;

            case R.id.btnUpdate:
                validate();
                break;

/*
            case R.id.edtCountryCode:
                GeneralStaticAlertDialog.getInstance().openCountryCode(getActivity(), new IListenerCountryCode() {
                    @Override
                    public void onSelect(String strCountCode) {
                        edtCountryCode.setText(strCountCode);
                    }

                    @Override
                    public void onCancel(String strBlank) {

                    }
                });
                break;*/
            case R.id.imageViewProfile:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermision()) {
                        startDialog();
                    }
                } else {
                    startDialog();
                }
                break;

            case R.id.edtPassword:
                GeneralStaticAlertDialog.getInstance().createDialogChangePassword(user, getActivity(), new DialogDismissChangePassword() {
                    @Override
                    public void onDismiss(String message) {
                        GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(getActivity(), message, null);
                    }
                });

                break;

        }
    }

    /**
     * Enable all edit text and field for modify data.
     */

    private void enableAllField() {
        btnUpdate.setVisibility(View.VISIBLE);
        edtName.setFocusableInTouchMode(true);
        edtName.requestFocus();
        edtName.setSelection(edtName.getText().toString().length());

        if (!user.getFbId().equalsIgnoreCase("")) {
            edtPassword.setEnabled(false);
            edtPassword.setFocusableInTouchMode(true);
        }
        else
        {
            edtPassword.setEnabled(true);
            edtPassword.setFocusableInTouchMode(false);
        }

        imageViewProfile.setClickable(true);
    }

    /**
     * validations for edit containt of profile .
     */

    private void validate() {
        if (isEditTextEmpty(edtName)) {
            showSnackBar(getString(R.string.please_enter_first_name), edtName, relativeMain);
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

            if (homeActivity.webServiceState == BaseActivity.WebServiceState.ACTIVE) {
                wsCallEditProfile();
            } else {
                GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(getActivity(), getString(R.string.profile_updated_successfully), new DialogDismissOneButton() {
                    @Override
                    public void onDismiss() {

                    }
                });
            }
        }
    }

    @NonNull
    private RequestBody createPartFromString(String desc) {
        return RequestBody.create(MultipartBody.FORM, desc);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String fileUrl)
    {
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        File image = new File(fileUrl);

        return MultipartBody.Part.createFormData(partName, image.getName(), RequestBody.create(MEDIA_TYPE_PNG, image));
    }
    /**
     * Call a edit profile api.
     */

    public void wsCallEditProfile() {
        AlertUtils.showCustomProgressDialog(getContext());

        String strToken = user.getToken();
        String strUserId = user.getId();

        String strName = edtName.getText().toString();
        String strFName = edtName.getText().toString();
        DebugLog.i("Call Update Profile Request");
        ApiService api = RetroClient.getApiService();
        Call<User> call = api.doEditProfile(strToken, createPartFromString(strName), createPartFromString(strUserId), createPartFromString(strFName), prepareFilePart("profile_image", path));

        APIRequestManager<User> apiRequestManager = new APIRequestManager<>(new APIRequestManager.ResponseListener<User>() {
            @Override
            public void onResponse(retrofit2.Response<User> response) {
                AlertUtils.dismissDialog();
                if ( response.isSuccessful()) {
                    if (response.code() == 200) {
                        User user = response.body();

                        DebugLog.i("Update Profile Success Code : "+ response.code());

                        String data = new Gson().toJson(user);
                        DataToPref.setSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA, data);

                        homeActivity.updateProfileData();
                        GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(getActivity(), getString(R.string.profile_updated_successfully), new DialogDismissOneButton() {
                            @Override
                            public void onDismiss() {
                                navigatorInterface.openHome();
                            }
                        });
                    }
                } else {
                    DebugLog.e("Update Profile Failed Code : "+ response.code());
                    try {
                        String s = response.errorBody().string();
                        if (response.code() == 400) {

                            GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                            showSnackBar(responseBody.getMessage(), edtPassword, relativeMain);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                AlertUtils.dismissDialog();
                showSnackBar("Bad Server Response", edtPassword, relativeMain);
            }
        }, call);
        apiRequestManager.execute();

    }

    /**
     * Set a parameter for call a edit profile api.
     */

    public HashMap<String, String> setEditProfileData() {
        HashMap<String, String> latlonlist = new HashMap<>();
        latlonlist.put(LaoxiConstant.NAME, edtName.getText().toString());
        latlonlist.put(LaoxiConstant.USER_ID, user.getId());
        latlonlist.put(LaoxiConstant.FNAME, edtName.getText().toString());
        return latlonlist;

    }

    //Getting image from gallery or camera

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

    /**
     * Get a call back when select a image from gallery or camera.
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

                DebugLog.e("file size is a=============" + destination);

                path = compressImage(destination.getAbsolutePath());

                Picasso.with(getContext())
                        .load(destination)
                        .fit()
                        .into(imageViewProfile);
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

                path = compressImage(path);
                if (file.length() <= maxMbSize) {
                    Picasso.with(getActivity()).load(file).fit().into(imageViewProfile);
                } else {
                    path = null;
                    DebugLog.e("file not uploaded");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void uploadImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
    }

    public void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }


    /**
     * Check permissions for gallery and camera.
     */

    public boolean checkPermision() {
        if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            if ((shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))) {

                showSnackBar("Allow us to External Storage permissions to access gallery from setting/app/laoxi/permissions", edtPassword, relativeMain);


            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, EXTERNAL_READ);

            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Get a call back when allow or deny permissions.
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
     * Compress image .
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
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "ultimatetaxi/Images");
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
