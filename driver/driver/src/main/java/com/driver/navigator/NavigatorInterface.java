package com.driver.navigator;

import android.support.v4.app.Fragment;

/**
 * Created by hitesh on 27/4/16.
 */
public interface NavigatorInterface {

    void openTrackRideFragment();

    void openReceiptFragment(Fragment fragment);

    void openFeedbackFragment(Fragment fragment);

    void openHomeFragment();

    void closeDrawer();

    void startUpdateLatLongService();

    void updateDriverProfile();

    void openArrivedAtPickupFragment();

    void openStartRideFragment();

    void openCompleteRideFragment();

    void drawerLock(boolean b);

    void stopUpdateLatLongService();
}
