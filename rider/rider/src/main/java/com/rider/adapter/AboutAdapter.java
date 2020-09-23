package com.rider.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rider.fragments.AboutUsFirstFragment;
import com.rider.fragments.AboutUsSecondFragment;
import com.rider.fragments.AboutUsThirdFragment;


public class AboutAdapter extends FragmentStatePagerAdapter {

    public AboutAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position) {
            case 0:
                frag = new AboutUsFirstFragment();
                break;
            case 1:
                frag = new AboutUsSecondFragment();
                break;

            case 2:
                frag = new AboutUsThirdFragment();
                break;

        }
        return frag;
    }

    @Override
    public int getCount() {
        return 3;
    }


}