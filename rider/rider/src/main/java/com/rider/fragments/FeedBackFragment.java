package com.rider.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.github.siyamed.shapeimageview.ImageViewProfile;
import com.google.gson.Gson;
import com.rider.R;
import com.rider.customclasses.CustomEditText;
import com.rider.customclasses.CustomTextView;
import com.rider.dialog.AlertUtils;
import com.rider.navigator.NavigatorInterface;
import com.rider.pojo.OrderData;
import com.rider.pojo.User;
import com.rider.retrofit.APIRequestManager;
import com.rider.retrofit.ApiService;
import com.rider.retrofit.RetroClient;
import com.rider.utils.DataToPref;
import com.rider.utils.DebugLog;
import com.rider.utils.LaoxiConstant;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by hitesh on 20/7/16.
 */
public class FeedBackFragment extends BaseFragment implements View.OnClickListener {

    NavigatorInterface navigatorInterface;
    @Bind(R.id.imageViewProfile)
    ImageViewProfile imageViewProfile;

    @Bind(R.id.edtSaySomething)
    CustomEditText edtSaySomething;
    @Bind(R.id.txtThanks)
    CustomTextView txtThanks;
    @Bind(R.id.txtName)
    CustomTextView txtName;
    @Bind(R.id.imgClose)
    ImageView imgClose;
    @Bind(R.id.RbLike)
    RadioButton RbLike;
    @Bind(R.id.RbDisLike)
    RadioButton RbDisLike;
    @Bind(R.id.RgFeedBack)
    RadioGroup RgFeedBack;
    @Bind(R.id.favouriteImageButton)
    ImageButton favouriteImageButton;

    @Bind(R.id.mainView)
    RelativeLayout mainView;

    private User user;
    private OrderData orderData;
    String vote = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rider_feedback_layout, container, false);
        ButterKnife.bind(this, view);

        /**
         * Load a data from shared preference and show on screen .
         */

        Gson mGson = new Gson();
        String strUserData = DataToPref.getSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA);
        user = mGson.fromJson(strUserData, User.class);

        String strOrderData = DataToPref.getSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.ORDER, LaoxiConstant.ORDER_DATA);
        orderData = mGson.fromJson(strOrderData, OrderData.class);

        txtName.setText("i'm "+orderData.getDriverData().getFname());
        Picasso.with(getContext())
                .load(getResources().getString(R.string.MAINURL_FOR_DRIVER_IMAGE) + orderData.getDriverData().getProfileImage())
                .placeholder(R.drawable.avatar_icon)
                .into(imageViewProfile);

        txtThanks.setOnClickListener(this);
        imgClose.setOnClickListener(this);
        favouriteImageButton.setOnClickListener(this);
        if (orderData.getDriverData().isFavouriteDriver()) {
            favouriteImageButton.setImageResource(R.drawable.star);
        } else {
            favouriteImageButton.setImageResource(R.drawable.star_empty);
        }


        RgFeedBack.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.RbLike) {
                    vote = "1";
                } else if (checkedId == R.id.RbDisLike) {
                    vote = "0";
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigatorInterface = (NavigatorInterface) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imgClose:
                closeKeyboard(getActivity());
                navigatorInterface.openHome();
                break;

            case R.id.txtThanks:
                closeKeyboard(getActivity());

                /*if (vote.equalsIgnoreCase("")) {
                    showSnackBar("Please give votes", null, linearMain);
                }else if (vote.equalsIgnoreCase("0")) {

                    if(isEditTextEmpty(edtSaySomething)){
                        showSnackBar("Please enter comment", null, linearMain);
                    }else {
                        wsCallFeedBack(setFeedbackData());
                    }

                }else {
                    wsCallFeedBack(setFeedbackData());
                }*/
                if (vote.equalsIgnoreCase("")) {
                    showSnackBar("Please give votes", null, mainView);
                } else {

                    if (vote.equalsIgnoreCase("0")) {
                        if (isEditTextEmpty(edtSaySomething)) {
                            showSnackBar("Please say something about driver", null, mainView);
                        } else {
                            wsCallFeedBack(setFeedbackData());
                        }
                    } else {
                        wsCallFeedBack(setFeedbackData());
                    }

                }

                break;

            /*case R.id.btnThumbsUp:
                vote = "1";
                imgThumbsUp.setSelected(true);
                imgThumbsdown.setSelected(false);
                imgThumbsUp.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.image_click));

                break;
            case R.id.btnThumbsDown:
                imgThumbsUp.setSelected(false);
                imgThumbsdown.setSelected(true);
                vote = "0";
                imgThumbsdown.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.image_click));

                break;*/

            case R.id.favouriteImageButton:
                if (orderData.getDriverData().isFavouriteDriver()) {
                    wsCallRemoveFavouriteDriver(setFavouriteData());
                } else {
                    wsCallAddFavouriteDriver(setFavouriteData());
                }
                break;
        }
    }


    /**
     * Call a feedback api for driver.
     */

    public void wsCallFeedBack(HashMap<String, String> param) {
        AlertUtils.showCustomProgressDialog(getContext());

        String strToken = user.getToken();
        String strUserId = user.getId();

        String strOrderId = orderData.getId();
        String strDriverId = orderData.getDriverId();

        String strVote = vote;
        String strComment = edtSaySomething.getText().toString();
        DebugLog.i("Call FeedBack Request");
        ApiService api = RetroClient.getApiService();
        Call<Void> call = api.doRateAndReview(strToken, strOrderId, strDriverId, strUserId, "user", strComment, strVote);

        APIRequestManager<Void> apiRequestManager = new APIRequestManager<>(new APIRequestManager.ResponseListener<Void>() {
            @Override
            public void onResponse(retrofit2.Response<Void> response) {
                AlertUtils.dismissDialog();

                if ( response.isSuccessful() ) {
                    if (response.code() == 201) {
                        DebugLog.i("FeedBack Success Response");
                        navigatorInterface.openHome();
                    }
                } else {
                    DebugLog.w("FeedBack Failed Response");
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                AlertUtils.dismissDialog();
                DebugLog.e("FeedBack Request Failed");
            }
        }, call);
        apiRequestManager.execute();

    }


    /**
     * Set a parameter for call feedback driver api.
     */

    public HashMap<String, String> setFeedbackData() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.ORDER_ID, orderData.getId());
        param.put(LaoxiConstant.DRIVER_ID, orderData.getDriverId());
        param.put(LaoxiConstant.USER_ID, user.getId());
        param.put(LaoxiConstant.USER_TYPE, "user");
        param.put(LaoxiConstant.VOTES, vote);
        param.put("comment", edtSaySomething.getText().toString());

        return param;
    }

    /**
     * Call a add favourite driver api.
     */

    public void wsCallAddFavouriteDriver(HashMap<String, String> param) {
        AlertUtils.showCustomProgressDialog(getContext());

        String strDriverId = orderData.getDriverId();

        String strToken = user.getToken();
        String strUserId = user.getId();

        ApiService api = RetroClient.getApiService();
        Call<Void> call = api.addFavouriteDriver(strToken, strDriverId, strUserId);

        APIRequestManager<Void> apiRequestManager = new APIRequestManager<>(new APIRequestManager.ResponseListener<Void>() {
            @Override
            public void onResponse(retrofit2.Response<Void> response) {
                AlertUtils.dismissDialog();
                orderData.getDriverData().setFavouriteDriver(true);
                favouriteImageButton.setImageResource(R.drawable.star);
            }

            @Override
            public void onFailure(Throwable throwable) {
                AlertUtils.dismissDialog();
            }
        }, call);
        apiRequestManager.execute();

    }


    /**
     * Call a add favourite driver api.
     */

    public void wsCallRemoveFavouriteDriver(HashMap<String, String> param) {
        AlertUtils.showCustomProgressDialog(getContext());

        String strToken = user.getToken();
        String strUserId = user.getId();

        String strDriverId = orderData.getDriverId();

        ApiService api = RetroClient.getApiService();
        Call<Void> call = api.removeFavouriteDriver(strToken, strDriverId, strUserId);

        APIRequestManager<Void> apiRequestManager = new APIRequestManager<>(new APIRequestManager.ResponseListener<Void>() {
            @Override
            public void onResponse(retrofit2.Response<Void> response) {
                AlertUtils.dismissDialog();
                orderData.getDriverData().setFavouriteDriver(false);
                favouriteImageButton.setImageResource(R.drawable.star_empty);
            }

            @Override
            public void onFailure(Throwable throwable) {
                AlertUtils.dismissDialog();
            }
        }, call);
        apiRequestManager.execute();

    }


    /**
     * Set a parameter for call feedback driver api.
     */

    public HashMap<String, String> setFavouriteData() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.DRIVER_ID, orderData.getDriverId());
        param.put(LaoxiConstant.USER_ID, user.getId());

        return param;
    }

    public void closeKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }
}
