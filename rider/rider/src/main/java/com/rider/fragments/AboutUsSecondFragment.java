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
public class AboutUsSecondFragment extends Fragment {

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

        view3.setText("We're local");
        view.setText("We may not know every back street \n and shortcut, but as locals, we know \n and care about our community. \n We're proud to be Laos owned \n and operated. Cheers to that!");
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
