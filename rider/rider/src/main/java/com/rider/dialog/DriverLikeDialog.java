package com.rider.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.rider.R;
import com.rider.customclasses.CustomTextView;
import com.rider.pojo.DriverDetails;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by chirag on 18/2/16.
 */
public class DriverLikeDialog extends DialogFragment implements View.OnClickListener {

    @Bind(R.id.txtDislikes)
    CustomTextView txtDislikes;
    @Bind(R.id.txtLikes)
    CustomTextView txtLikes;
    @Bind(R.id.btn_close)
    ImageView btn_close;

    public DriverDetails getDriverDetails() {
        return driverDetails;
    }

    public void setDriverDetails(DriverDetails driverDetails) {
        this.driverDetails = driverDetails;
    }

    DriverDetails driverDetails;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_details_like_layout, container, false);

        WindowManager.LayoutParams wmlp = getDialog().getWindow().getAttributes();
        wmlp.gravity = Gravity.FILL_HORIZONTAL;
        getDialog().getWindow().requestFeature(STYLE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_color_blue)));
        setCancelable(false);
        ButterKnife.bind(this, view);
        btn_close.setOnClickListener(this);

        txtLikes.setText(driverDetails.getUpVotes() + " people \n thought " + driverDetails.getFname() + " \n was excellent");
        txtDislikes.setText(driverDetails.getDownVotes() + " people were \n unhappy with \n their ride");
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                getDialog().dismiss();
                break;
        }
    }

}
