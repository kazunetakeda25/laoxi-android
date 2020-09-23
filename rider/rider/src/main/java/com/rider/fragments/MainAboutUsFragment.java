package com.rider.fragments;

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

import com.rider.R;
import com.rider.activity.HomeActivity;
import com.rider.adapter.AboutAdapter;
import com.rider.customclasses.CustomTextView;
import com.rider.navigator.NavigatorInterface;

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
    @Bind(R.id.img_share)
    ImageView imgShare;
    @Bind(R.id.linearlayoutbg)
    LinearLayout linearlayoutbg;
    private HomeActivity homeActivity;
    NavigatorInterface navigatorInterface;
    private AboutAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_layout, container, false);
        ButterKnife.bind(this, view);
        imgShare.setVisibility(View.GONE);
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
         * For load a containt of about us screen when swipe it.
         */

        vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    linearlayoutbg.setBackgroundResource(R.drawable.tree_icon);
                    rdBtnOne.setChecked(true);
                } else if (position == 1) {
                    linearlayoutbg.setBackgroundResource(R.drawable.hand_icon);
                    rdBtnTwo.setChecked(true);
                } else {
                    linearlayoutbg.setBackgroundResource(R.drawable.money_icon);
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
}
