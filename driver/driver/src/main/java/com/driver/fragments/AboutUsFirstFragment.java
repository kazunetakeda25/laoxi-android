package com.driver.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.driver.R;
import com.driver.customclasses.CustomTextView;
import com.driver.navigator.NavigatorInterface;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hitesh on 20/7/16.
 */
public class AboutUsFirstFragment extends Fragment {

    NavigatorInterface navigatorInterface;
    @Bind(R.id.view3)
    CustomTextView view3;
    @Bind(R.id.view)
    CustomTextView view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rider_about_laoxi, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        view3.setText("We're fair");
        view.setText("We live in the land of the Fair Go,\n and believe everyone should be \n treated fairly and ethically. So, we've \n eliminated surge pricing (as we're \n always happy to have you on board!).");
        navigatorInterface.closeDrawer();
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
}
