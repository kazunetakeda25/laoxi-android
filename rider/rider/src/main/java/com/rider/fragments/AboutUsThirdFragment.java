package com.rider.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rider.R;
import com.rider.customclasses.CustomTextView;
import com.rider.navigator.NavigatorInterface;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hitesh on 20/7/16.
 */
public class AboutUsThirdFragment extends Fragment {

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

        view3.setText("We're taxpayers too");
        view.setText("Unlike some other ride -sharing \n companies, we don't hide our profits \n in offshore tax havens. We're \n Laos, and keep our taxes within \n Laos - for all passengers.");

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
