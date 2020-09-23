package com.rider.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.rider.R;
import com.rider.activity.HomeActivity;
import com.rider.adapter.FragmentPagerAdapter;
import com.rider.customclasses.CustomTextView;
import com.rider.navigator.NavigatorInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hlink54 on 19/7/16.
 */
public class PastRideFutureRideViewPagerFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener{

    @Bind(R.id.viewPagerRide)
    ViewPager viewPagerRide;
    @Bind(R.id.rdFuture)
    RadioButton rdFuture;
    @Bind(R.id.rdPast)
    RadioButton rdPast;
    @Bind(R.id.header_menu)
    ImageView headerMenu;
    @Bind(R.id.header_text)
    CustomTextView headerText;
    @Bind(R.id.img_share)
    ImageView imgShare;
    NavigatorInterface navigatorInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pastride_futureride_viewpager, container, false);
        ButterKnife.bind(this, view);
        imgShare.setVisibility(View.GONE);
        headerText.setText(R.string.your_rides);

        List<Fragment> listFragment = new ArrayList<>();
        listFragment.add(new PastRideFragment());
        listFragment.add(new FutureRideFragment());


        /**
         * Set adapter for view pager.
         */

        viewPagerRide.setAdapter(new FragmentPagerAdapter(getChildFragmentManager(), listFragment));

        // Give the PagerSlidingTabStrip the ViewPager
        // Attach the view pager to the tab strip


        return view;
    }

    @OnClick(R.id.header_menu)
    public void onClick() {

        HomeActivity.toggleDrawer();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigatorInterface= (NavigatorInterface) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        navigatorInterface.closeDrawer();

        rdPast.setOnCheckedChangeListener(this);
        rdFuture.setOnCheckedChangeListener(this);
        viewPagerRide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                       /* TextView textView = (TextView) tabsContainer.getChildAt(position);
                        textView.setTextColor(getResources().getColor(R.color.past_future_ride_tab_selected));

                        TextView textView2 = (TextView) tabsContainer.getChildAt(1);
                        textView2.setTextColor(getResources().getColor(R.color.past_future_ride_tab_unselected));*/
                    rdPast.setChecked(true);
                    rdFuture.setChecked(false);
                }
                if (position == 1) {
/*                        TextView textView2 = (TextView) tabsContainer.getChildAt(0);
                        textView2.setTextColor(getResources().getColor(R.color.past_future_ride_tab_unselected));

                        TextView textView = (TextView) tabsContainer.getChildAt(position);
                        textView.setTextColor(getResources().getColor(R.color.past_future_ride_tab_selected));*/
                    rdPast.setChecked(false);
                    rdFuture.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rdFuture:
                if (isChecked == true){
                    viewPagerRide.setCurrentItem(1);
                    rdFuture.setChecked(true);
                }
                break;
            case R.id.rdPast:
                if (isChecked == true){
                    viewPagerRide.setCurrentItem(0);
                    rdPast.setChecked(true);
                }
                break;

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
