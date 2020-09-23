package com.driver.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bluejamesbond.text.DocumentView;
import com.driver.R;
import com.driver.activity.HomeActivity;
import com.driver.customclasses.CustomTextView;
import com.driver.navigator.NavigatorInterface;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hitesh on 20/7/16.
 */
public class AboutUsFragment extends Fragment {

    @Bind(R.id.header_menu)
    ImageView headerMenu;
    @Bind(R.id.header_text)
    CustomTextView headerText;

    @Bind(R.id.txtAboutUs)
    DocumentView txtAboutUs;
    NavigatorInterface navigatorInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        ButterKnife.bind(this, view);

        headerText.setText(R.string.about_us);
        return view;
    }

    @OnClick(R.id.header_menu)
    public void onClick() {

        HomeActivity.toggleDrawer();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navigatorInterface.closeDrawer();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigatorInterface= (NavigatorInterface) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
