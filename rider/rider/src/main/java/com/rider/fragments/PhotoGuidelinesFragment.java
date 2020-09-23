package com.rider.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rider.R;
import com.rider.customclasses.CustomTextView;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chirag on 12/9/16.
 */

public class PhotoGuidelinesFragment extends BaseFragment {

    private static ViewPager mPager;
    private static final Integer[] IMAGES = {R.drawable.screen_1, R.drawable.screen_2};
    @Bind(R.id.header_menu)
    ImageView headerMenu;
    @Bind(R.id.header_text)
    CustomTextView headerText;
    @Bind(R.id.img_share)
    ImageView imgShare;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.indicator)
    CirclePageIndicator indicator;
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_guidelines, container, false);
        ButterKnife.bind(this, view);

        headerText.setText("Photo Guidelines");
        imgShare.setVisibility(View.GONE);
        headerMenu.setVisibility(View.GONE);

        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);

        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(getActivity(), ImagesArray));
        CirclePageIndicator indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

   /* @OnClick(R.id.header_menu)
    public void onClick() {
        headerMenu.openDrawer();
    }*/

    public class SlidingImage_Adapter extends PagerAdapter {

        private ArrayList<Integer> IMAGES;
        private LayoutInflater inflater;
        private Context context;

        public SlidingImage_Adapter(Context context, ArrayList<Integer> IMAGES) {
            this.context = context;
            this.IMAGES=IMAGES;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return IMAGES.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

            assert imageLayout != null;
            final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.imageaa);
            final LinearLayout aaa = (LinearLayout) imageLayout.findViewById(R.id.LnProfilePgoto);
            final LinearLayout bbb = (LinearLayout) imageLayout.findViewById(R.id.LnVehiclePgoto);

            imageView.setImageResource(IMAGES.get(position));

            if (position == 0){
                aaa.setVisibility(View.VISIBLE);
                bbb.setVisibility(View.GONE);
            }else if (position == 1){
                aaa.setVisibility(View.GONE);
                bbb.setVisibility(View.VISIBLE);
            }

            view.addView(imageLayout, 0);

            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }


}
