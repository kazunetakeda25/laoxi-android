package com.driver.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.github.siyamed.shapeimageview.ImageViewProfile;
import com.driver.R;
import com.driver.activity.HomeActivity;
import com.driver.customclasses.CustomEditText;
import com.driver.customclasses.CustomTextView;
import com.driver.dao.IAsyncCompleteCallback;
import com.driver.dao.ParsingHelper;
import com.driver.dao.homedao.HomeActivityDAO;
import com.driver.navigator.NavigatorInterface;
import com.driver.pojo.GenericResponse;
import com.driver.pojo.TrackRidePojo.TrackRide;
import com.driver.pojo.dropoffWs.DropOffResponse;
import com.driver.retrofit.ApiService;
import com.driver.retrofit.RetroClient;
import com.driver.util.AlertUtils;
import com.driver.util.AppHelper;
import com.driver.util.ConstantClass;
import com.driver.util.DebugLog;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by hitesh on 20/7/16.
 */
public class FeedbackFragment extends BaseFragment implements View.OnClickListener {

    NavigatorInterface navigatorInterface;


    String vote = "";
    @Bind(R.id.mainView)
    RelativeLayout mainView;
    @Bind(R.id.imageViewProfile)
    ImageViewProfile imageViewProfile;
    @Bind(R.id.txtName)
    CustomTextView txtName;
    @Bind(R.id.RbLike)
    RadioButton RbLike;
    @Bind(R.id.RbDisLike)
    RadioButton RbDisLike;
    @Bind(R.id.RgFeedBack)
    RadioGroup RgFeedBack;
    @Bind(R.id.edtSaySomething)
    CustomEditText edtSaySomething;
    @Bind(R.id.txtThanks)
    CustomTextView txtThanks;
    @Bind(R.id.imgClose)
    ImageView imgClose;
    private HomeActivity homeActivity;

    public void setDropOffResponse(DropOffResponse dropOffResponse) {
        this.dropOffResponse = dropOffResponse;
    }

    private DropOffResponse dropOffResponse;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        ButterKnife.bind(this, view);

        DebugLog.i("Reached FeedBack Screen");

        String data = AppHelper.getInstance().getSharedPref(ConstantClass.TRACK_RIDE_ORDER_DATA);

        txtName.setText("i'm " + dropOffResponse.getUserData().getName());
        Picasso.with(getContext())
                .load(ConstantClass.USER_IMAGE_URL + dropOffResponse.getUserData().getProfileImage())
                .resize(200,200)
                .placeholder(R.drawable.avatar_icon)
                .into(imageViewProfile);

        txtThanks.setOnClickListener(this);
        imgClose.setOnClickListener(this);


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
        if (context instanceof HomeActivity) {
            homeActivity = (HomeActivity) context;
        }
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
                navigatorInterface.openHomeFragment();
                break;
            case R.id.txtThanks:
                closeKeyboard(getActivity());
                if (vote.equalsIgnoreCase("")) {
                    showSnackBar("Please give votes", null, mainView);
                } else {
                    if (vote.equalsIgnoreCase("0")) {
                        if (isEditTextEmpty(edtSaySomething)) {
                            showSnackBar("Please say something about rider", null, mainView);
                        } else {
                            wsCallFeedBack(setFeedbackData());
                        }
                    } else {
                        wsCallFeedBack(setFeedbackData());
                    }
                }

            /*    if (vote.equalsIgnoreCase("")) {
                    showSnackBar("Please give votes", null, mainView);
                } else if (vote.equalsIgnoreCase("0")) {
                    showSnackBar("Please enter comment", null, mainView);
                } else {
                    wsCallFeedBack(setFeedbackData());
                }*/
                break;

        }
    }

    @Override
    public void getUpdateLatLongWsResponseFromService(TrackRide pojo) {

    }

    @Override
    public void drawerLock() {
        navigatorInterface.drawerLock(true);
    }


    /**
     * Call a give review to user.
     */

    public void wsCallFeedBack(HashMap<String, String> param) {
        AlertUtils.showCustomProgressDialog(getContext());

        String strToken = AppHelper.getInstance().getToken();
        String strDriverId = AppHelper.getInstance().getDriverId();
        String strOrderId = dropOffResponse.getId();
        String strUserId = dropOffResponse.getUserData().getId();
        String strUserType = "driver";
        String strVote = vote;
        String strComment = edtSaySomething.getText().toString();
        DebugLog.i("Feedback Request Sending");
        ApiService api = RetroClient.getApiService();
        Call<Void> call = api.callFeedBack(strToken, strDriverId, strUserId, strUserType, strOrderId, strComment, strVote);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                AlertUtils.dismissDialog();
                if (response.isSuccessful()) {
                    DebugLog.i("Feedback Success Response Code :" + response.code());

                    if (response.code() == 201) {
                        navigatorInterface.openHomeFragment();
                    }
                } else {
                    try {
                        DebugLog.i("Feedback Failed Response Code :" + response.code());
                        String s = response.errorBody().string();
                        if ( response.code() == ConstantClass.RESPONSE_CODE_401 ) {
                            ((HomeActivity) getActivity()).callLogoutWS();
                        } else {
                            GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                            if (responseBody == null) {
                                homeActivity.showSnackbar("Can't send notification to user because user doesn't have device type.");
                            } else {
                                homeActivity.showSnackbar(responseBody.getMessage());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                DebugLog.e("Feedback Request Failed Msg :" + t.getMessage());
                AlertUtils.dismissDialog();
                homeActivity.showSnackbar(getString(R.string.bad_server));
            }
        });

    }


    public HashMap<String, String> setFeedbackData() {
        HashMap<String, String> param = new HashMap<>();
        param.put(ConstantClass.ORDER_ID, dropOffResponse.getId());
//        param.put(ConstantClass.DRIVER_ID, AppHelper.getInstance().getLoginUser().getDriverId());
        param.put(ConstantClass.DRIVER_ID, AppHelper.getInstance().getDriverId());
        param.put(ConstantClass.USER_ID, dropOffResponse.getUserData().getId());
        param.put(ConstantClass.USER_TYPE, "driver");
        param.put(ConstantClass.VOTES, vote);
        param.put("comment", edtSaySomething.getText().toString());

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
