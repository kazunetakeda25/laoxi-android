package com.rider.navigator;

import com.rider.dialog.DriverDetailsDialog;
import com.rider.pojo.DriverDetails;
import com.rider.utils.HomePageRefresh;

/**
 * Created by hitesh on 27/4/16.
 */
public interface NavigatorInterface {

    void openRideLater(HomePageRefresh homePageRefresh);

    void openDriverDetails(String s, String id, String eta);

    void openFareEstimation(DriverDetails driverDetails, String carTypeId,CloseDriverDetailsDialogInterface closeDriverDetailsDialogInterface);

    void openTrackDriver();

    void openHome();

    void openDriverArrived();

    void openLetsGoFragment();

    void openReceipt();

    void openPaymentDetails();

    void openFeedback();

    void closeDrawer();

    void startTrackRideService();

    void stopTrackRideService();

    void openPhotoGuidelines();

    void updateProfileData();

}
