package com.driver.dialog;

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

import com.driver.R;
import com.driver.customclasses.CustomTextView;
import com.driver.dao.ParsingHelper;
import com.driver.pojo.TrackRidePojo.TrackRide;
import com.driver.util.AppHelper;
import com.driver.util.ConstantClass;

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


        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String data = AppHelper.getInstance().getSharedPref(ConstantClass.TRACK_RIDE_ORDER_DATA);
        TrackRide trackRidePojo = ParsingHelper.getInstance().trackRideData(data);

       if(trackRidePojo!=null)
       {
           txtLikes.setText(trackRidePojo.getUserData().getUpVotes() + " people \n thought " + trackRidePojo.getUserData().getName() + " \n was excellent");
           txtDislikes.setText(trackRidePojo.getUserData().getDownVotes() + " people were \n unhappy with \n their ride");
       }
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
