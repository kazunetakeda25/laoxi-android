package com.driver.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.driver.R;
import com.driver.activity.HomeActivity;
import com.driver.adapter.AboutAdapter;
import com.driver.customclasses.CustomTextView;
import com.driver.navigator.NavigatorInterface;
import com.driver.pojo.TrackRidePojo.TrackRide;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainAboutUsFragment extends BaseFragment {


    @Bind(R.id.pager)
    ViewPager vPager;
    @Bind(R.id.rdBtnOne)
    RadioButton rdBtnOne;
    @Bind(R.id.rdBtnTwo)
    RadioButton rdBtnTwo;
    @Bind(R.id.rdBtnThree)
    RadioButton rdBtnThree;
    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;
    @Bind(R.id.header_menu)
    ImageView headerMenu;
    @Bind(R.id.header_text)
    CustomTextView headerText;
    @Bind(R.id.linearbackground)
    LinearLayout linearbackground;

    private HomeActivity homeActivity;
    NavigatorInterface navigatorInterface;
    private AboutAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_layout, container, false);
        ButterKnife.bind(this, view);

        headerText.setText(R.string.about_us);
        rdBtnOne.setChecked(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navigatorInterface.closeDrawer();
        FragmentManager manager = getChildFragmentManager();

        adapter = new AboutAdapter(manager);
        vPager.setAdapter(adapter);



        /**
         * When swipe view pager a containt is changed.
         */

        vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    linearbackground.setBackgroundResource(R.drawable.tree_icon);
                    rdBtnOne.setChecked(true);
                } else if (position == 1) {
                    linearbackground.setBackgroundResource(R.drawable.hand_icon);
                    rdBtnTwo.setChecked(true);
                } else {
                    linearbackground.setBackgroundResource(R.drawable.money_icon);
                    rdBtnThree.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
    public void getUpdateLatLongWsResponseFromService(TrackRide pojo) {

    }

    @Override
    public void drawerLock() {
        navigatorInterface.drawerLock(false);
    }
}
