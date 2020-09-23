package com.rider.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;

import com.github.siyamed.shapeimageview.ImageViewSlider;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rider.LAOXI;
import com.rider.LocationHelper.LocationHelperService;
import com.rider.R;
import com.rider.adapter.ListAdapter;
import com.rider.customclasses.CustomTextView;
import com.rider.dialog.AlertUtils;
import com.rider.dialog.FareEstimationDialog;
import com.rider.dialog.GeneralStaticAlertDialog;
import com.rider.fragments.DriverArrivedFragment;
import com.rider.fragments.FeedBackFragment;
import com.rider.fragments.LetsGoFragment;
import com.rider.fragments.MainAboutUsFragment;
import com.rider.fragments.NewHomeFragment;
import com.rider.fragments.PastRideFutureRideViewPagerFragment;
import com.rider.fragments.PaymentFragment;
import com.rider.fragments.PhotoGuidelinesFragment;
import com.rider.fragments.ProfileFragment;
import com.rider.fragments.ReceiptFragment;
import com.rider.fragments.RedeemCodeFragment;
import com.rider.fragments.RideLaterFragment;
import com.rider.fragments.SupportFragment;
import com.rider.fragments.TrackDriverFragment;
import com.rider.navigator.CloseDriverDetailsDialogInterface;
import com.rider.navigator.NavigatorInterface;
import com.rider.pojo.DriverDetails;
import com.rider.pojo.GenericResponse;
import com.rider.pojo.ListDataPojo;
import com.rider.pojo.NotificationHandler;
import com.rider.pojo.OrderData;
import com.rider.pojo.User;

import com.rider.pojo.VehicleType;
import com.rider.retrofit.APIRequestManager;
import com.rider.retrofit.ApiService;
import com.rider.retrofit.RetroClient;
import com.rider.utils.DataToPref;
import com.rider.utils.DebugLog;
import com.rider.utils.DialogDismissOneButton;
import com.rider.utils.DialogDismissTwoButton;
import com.rider.utils.LaoxiConstant;
import com.rider.utils.HomePageRefresh;
import com.rider.utils.ParsingHelper;
import com.rider.utils.RecyclerItemClickSupport;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity implements View.OnClickListener, NavigatorInterface {

    @Bind(R.id.placeHolder)
    FrameLayout placeHolder;
    @Bind(R.id.linerLayoutRoot)
    LinearLayout linerLayoutRoot;
    @Bind(R.id.txtUserName)
    CustomTextView txtUserName;
    @Bind(R.id.linear_top)
    LinearLayout linearTop;
    @Bind(R.id.line)
    View line;
    @Bind(R.id.navigationListView)
    RecyclerView navigationListView;
    @Bind(R.id.line_bottom)
    View lineBottom;
    @Bind(R.id.imgIcon)
    ImageView imgIcon;
    @Bind(R.id.linear_bottom)
    LinearLayout linearBottom;
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.profileBG)
    ImageViewSlider profileBG;
    @Bind(R.id.txtILogout)
    CustomTextView txtILogout;

    public CountDownTimer cTimer;
    private float lastTranslate;
    //    private ImageView header_menu;
    public static DrawerLayout drawer;
    private static final Integer[] IMAGES = {R.drawable.book_your_ride_img_menu, R.drawable.your_rides_img_menu, R.drawable.my_payment_img_menu, R.drawable.shareapp_img_menu, R.drawable.about_img_menu, R.drawable.support_img_menu, R.drawable.my_payment_img_menu};
    private String[] MENUITEAMS;
    private ArrayList<ListDataPojo> list;
    private ListAdapter adapter;
    public static boolean activeornot;
    public static boolean openNotification;
    public static boolean openNotificationFromPush;

    private ShareActionProvider mShareActionProvider;
    private User user;
    static OrderData orderData;
    private VehicleType vehicleType;
    private boolean bGetOrder = false;
    ActionBarDrawerToggle toggle;

    public static OrderData getOrderData() {
        return orderData;
    }

    public static void setOrderData(OrderData orderData) {
        HomeActivity.orderData = orderData;
    }

    public static NotificationHandler notificationHandler;

    public static NotificationHandler getNotificationHandler() {
        return notificationHandler;
    }

    public static void setNotificationHandler(NotificationHandler notificationHandler) {
        HomeActivity.notificationHandler = notificationHandler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Get data from shared preference
        try {
            String strUserData = DataToPref.getSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA);
            Gson mGson = new Gson();
            user = mGson.fromJson(strUserData, User.class);

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        if (user != null) {
            Picasso.with(this)
                    .load(getResources().getString(R.string.MAINURL_FOR_IMAGE) + user.getProfileImage())
                    .resize(200, 200)
                    .into(profileBG);
            txtUserName.setText(user.getName());
        }

        LAOXI.clearActivity();
        LAOXI.setCurrentActivity(this);
        txtILogout.setOnClickListener(this);
        navigationListView = (RecyclerView) findViewById(R.id.navigationListView);
        linerLayoutRoot = (LinearLayout) findViewById(R.id.linerLayoutRoot);
        profileBG.setOnClickListener(this);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.app_name, R.string.app_name) {

            public void onDrawerSlide(View drawerView, float slideOffset) {
                float moveFactor = (navigationListView.getWidth() * slideOffset);
                closeKeyboard(HomeActivity.this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    linerLayoutRoot.setTranslationX(moveFactor);
                } else {
                    TranslateAnimation anim = new TranslateAnimation(0.0f, moveFactor, 0.0f, 0.0f);
                    anim.setDuration(0);
                    anim.setFillAfter(true);
                    linerLayoutRoot.startAnimation(anim);
                    lastTranslate = moveFactor;
                }
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        MENUITEAMS = getResources().getStringArray(R.array.Items);
        list = new ArrayList<ListDataPojo>();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        navigationListView.setLayoutManager(layoutManager);
        adapter = new ListAdapter(MENUITEAMS, IMAGES, this);
        navigationListView.setItemAnimator(new DefaultItemAnimator());
        navigationListView.setAdapter(adapter);

        if (savedInstanceState == null) {
            openHome();
        }

        /**
         * Set a sliders menu.
         */

        RecyclerItemClickSupport.addTo(navigationListView).setOnItemClickListener(new RecyclerItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Log.e("postion", "" + position);
                switch (position) {
                    case 0:
                        // Book your ride
                        closeKeyboard(HomeActivity.this);
                        NewHomeFragment fragment = new NewHomeFragment();
                        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        addFragment(fragment, true, fragment.getClass().getName());
                        break;
                    case 1:
                        //Your Rides
                        closeKeyboard(HomeActivity.this);
                        PastRideFutureRideViewPagerFragment pastRideFutureRideViewPagerFragment = new PastRideFutureRideViewPagerFragment();
                        getSupportFragmentManager().popBackStack(new NewHomeFragment().getClass().getName().toString(), 0);
                        addFragment(pastRideFutureRideViewPagerFragment, true, pastRideFutureRideViewPagerFragment.getClass().getName());
                        break;
                    case 2:
                        //My Payments
                        closeKeyboard(HomeActivity.this);
                        PaymentFragment paymentFragment = new PaymentFragment();
                        getSupportFragmentManager().popBackStack(new NewHomeFragment().getClass().getName().toString(), 0);
                        addFragment(paymentFragment, true, paymentFragment.getClass().getName());
                        break;
                    case 3:
                        //share App
                        doShare();
                        break;
                    case 4:
                        //About
                        closeKeyboard(HomeActivity.this);
                        MainAboutUsFragment viewPagerFragment = new MainAboutUsFragment();
                        getSupportFragmentManager().popBackStack(new NewHomeFragment().getClass().getName().toString(), 0);
                        addFragment(viewPagerFragment, true, viewPagerFragment.getClass().getName());
                        break;
                    case 5:
                        //Support
                        closeKeyboard(HomeActivity.this);
                        SupportFragment supportFragment = new SupportFragment();
                        getSupportFragmentManager().popBackStack(new NewHomeFragment().getClass().getName().toString(), 0);
                        addFragment(supportFragment, true, supportFragment.getClass().getName());
                        break;
                    case 6:
                        // Redeem code
                        closeKeyboard(HomeActivity.this);
                        RedeemCodeFragment redeemCodeFragment = new RedeemCodeFragment();
                        getSupportFragmentManager().popBackStack(new NewHomeFragment().getClass().getName().toString(), 0);
                        addFragment(redeemCodeFragment, true, redeemCodeFragment.getClass().getName());
                        break;
                }
            }
        });
    }// end of onCreate

    private void doShare() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.APP_URL));
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        LAOXI.setCurrentActivity(HomeActivity.this);
        LAOXI.currentActivity = HomeActivity.this;
        checkLocation(this);
        //modify by prakash
        if (openNotificationFromPush) {
            manageCancelTripPush(notificationHandler);
            openNotificationFromPush = false;
        }
        else
        {
            wsCallTackRide();
        }
        activeornot = true;
        wsCallGetCarType();
    }

    @Override
    protected void onPause() {
        super.onPause();
        activeornot = false;
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.placeHolder);
        closeKeyboard(this);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
                finish();
            } else if (fragment instanceof TrackDriverFragment) {

            } else if (fragment instanceof DriverArrivedFragment) {

            } else if (fragment instanceof LetsGoFragment) {

            } else if (fragment instanceof FeedBackFragment) {

            } else if (fragment instanceof ReceiptFragment) {

            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.profileBG:

                closeDrawer();
                closeKeyboard(HomeActivity.this);
                ProfileFragment profileFragment = new ProfileFragment();
                getSupportFragmentManager().popBackStack(new NewHomeFragment().getClass().getName().toString(), 0);
                addFragment(profileFragment, true, profileFragment.getClass().getName());
                break;

            case R.id.txtILogout:
                GeneralStaticAlertDialog.getInstance().createDialogWithTwoButton(HomeActivity.this, "Are you sure you want to logout?", new DialogDismissTwoButton() {
                    @Override
                    public void onOkClick() {

                        if (webServiceState == WebServiceState.ACTIVE) {
                            wsCallLogOut();
                        } else {
                            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelClick() {

                    }
                });
                break;
        }
    }

    public static void toggleDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    public void addFragment(Fragment fragment, Boolean backStack, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.placeHolder, fragment);
        if (backStack)
            fragmentTransaction.addToBackStack(tag);
        try {
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            fragmentTransaction.commitAllowingStateLoss();
        }

    }

    public void replaceFragment(Fragment fragment, Boolean backStack, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.placeHolder, fragment);
        if (backStack)
            fragmentTransaction.addToBackStack(tag);
        try {
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    String getClassName(Fragment fragment) {
        return fragment.getClass().getName().toString();
    }

    public void closeKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }


    @Override
    public void openRideLater(HomePageRefresh homePageRefresh) {
        closeDrawer();
        closeKeyboard(HomeActivity.this);
        RideLaterFragment rideLaterFragment = new RideLaterFragment();
        rideLaterFragment.setHomePageRefresh(homePageRefresh);
//        getSupportFragmentManager().popBackStack(new NewHomeFragment().getClass().getName().toString(), 0);
        addFragment(rideLaterFragment, true, rideLaterFragment.getClass().getName());

    }

    @Override
    public void openDriverDetails(String CarTypeIid, String id, String eta) {
       /* DriverDetailsDialog driverDetailsDialog = new DriverDetailsDialog();
        driverDetailsDialog.setContext(this);
        driverDetailsDialog.setCarTypeId(CarTypeIid);
        driverDetailsDialog.setDriverId(id);
        driverDetailsDialog.setETA(eta);

        driverDetailsDialog.show(getFragmentManager(), driverDetailsDialog.getClass().getName());*/
    }

    @Override
    public void openFareEstimation(DriverDetails driverDetails, String carTpeId, CloseDriverDetailsDialogInterface closeDriverDetailsDialogInterface) {

        closeDrawer();
        closeKeyboard(HomeActivity.this);
        /*
        FareEstimateFragment fareEstimateFragment = new FareEstimateFragment(carModel, driverId);
//        getSupportFragmentManager().popBackStack(new NewHomeFragment().getClass().getName().toString(), 0);
        addFragment(fareEstimateFragment, true, fareEstimateFragment.getClass().getName());*/

        FareEstimationDialog fareEstimationDialog = new FareEstimationDialog();
        fareEstimationDialog.setDriverDetails(driverDetails);
        fareEstimationDialog.setCarTypeId(carTpeId);
        fareEstimationDialog.setCallback(closeDriverDetailsDialogInterface);
        fareEstimationDialog.show(getFragmentManager(), fareEstimationDialog.getClass().getName());

    }

    @Override
    public void openTrackDriver() {
        closeDrawer();
        closeKeyboard(HomeActivity.this);
        TrackDriverFragment trackDriverFragment = new TrackDriverFragment();
        getSupportFragmentManager().popBackStack(new NewHomeFragment().getClass().getName().toString(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        replaceFragment(trackDriverFragment, true, trackDriverFragment.getClass().getName());
    }

    @Override
    public void openHome() {
        NewHomeFragment fragment = new NewHomeFragment();
        getSupportFragmentManager().popBackStack(null, 1);
        addFragment(fragment, true, fragment.getClass().getName());
    }

    @Override
    public void openDriverArrived() {
//        closeDrawer();
//        closeKeyboard(HomeActivity.this);
//        DriverArrivedFragment driverArrivedFragment = new DriverArrivedFragment();
//        getSupportFragmentManager().popBackStack(new NewHomeFragment().getClass().getName().toString(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        replaceFragment(driverArrivedFragment, true, driverArrivedFragment.getClass().getName());
    }

    @Override
    public void openLetsGoFragment() {
        closeDrawer();
        closeKeyboard(HomeActivity.this);
        LetsGoFragment letsGoFragment = new LetsGoFragment();
        getSupportFragmentManager().popBackStack(new NewHomeFragment().getClass().getName().toString(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        replaceFragment(letsGoFragment, true, letsGoFragment.getClass().getName());
    }

    @Override
    public void openReceipt() {
        closeDrawer();
        closeKeyboard(HomeActivity.this);
        ReceiptFragment receiptFragment = new ReceiptFragment();
        getSupportFragmentManager().popBackStack(new NewHomeFragment().getClass().getName().toString(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        addFragment(receiptFragment, true, receiptFragment.getClass().getName());
    }

    @Override
    public void openPaymentDetails() {
        closeDrawer();
        closeKeyboard(HomeActivity.this);
        PaymentFragment paymentFragment = new PaymentFragment();
//        getSupportFragmentManager().popBackStack(new NewHomeFragment().getClass().getName().toString(), 0);
        addFragment(paymentFragment, true, paymentFragment.getClass().getName());
    }

    @Override
    public void openFeedback() {
        closeDrawer();
        closeKeyboard(HomeActivity.this);
        FeedBackFragment feedBackLayout = new FeedBackFragment();
        getSupportFragmentManager().popBackStack(new NewHomeFragment().getClass().getName().toString(), 0);
        addFragment(feedBackLayout, true, feedBackLayout.getClass().getName());
    }

    @Override
    public void closeDrawer() {
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
        }
    }

    @Override
    public void startTrackRideService() {



    }

    @Override
    public void stopTrackRideService() {

    }

    @Override
    public void openPhotoGuidelines() {

        closeKeyboard(HomeActivity.this);
        PhotoGuidelinesFragment photoGuidelinesFragment = new PhotoGuidelinesFragment();
        getSupportFragmentManager().popBackStack(new NewHomeFragment().getClass().getName().toString(), 0);
        addFragment(photoGuidelinesFragment, true, photoGuidelinesFragment.getClass().getName());
    }

    @Override
    public void updateProfileData() {
        String strUserData = DataToPref.getSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA);
        Gson mGson = new Gson();
        user = mGson.fromJson(strUserData, User.class);


        if (user != null) {
            Picasso.with(this)
                    .load(getResources().getString(R.string.MAINURL_FOR_IMAGE) + user.getProfileImage())
                    .resize(200, 200)
                    .into(profileBG);
            txtUserName.setText(user.getName());
        }
    }

    @Override
    protected void onDestroy() {
        stopLocationUpdate();
        super.onDestroy();
        ButterKnife.unbind(this);

    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    /**
     * Call a log out api.
     */

    public void wsCallLogOut() {

        String strToken = user.getToken();
        String strUserId = user.getId();

        ApiService api = RetroClient.getApiService();
        Call<Void> call = api.logOut(strToken, strUserId);
        AlertUtils.showCustomProgressDialog(this);
        DebugLog.i("Call Logout");

        APIRequestManager<Void> apiRequestManager = new APIRequestManager<>(new APIRequestManager.ResponseListener<Void>() {
            @Override
            public void onResponse(retrofit2.Response<Void> response) {
                AlertUtils.dismissDialog();
                if (response.isSuccessful()) {
                    DebugLog.i("Logout Success Response");
                    if (response.code() == 200 ) {
                        DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA, "");
                        DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.ISLOGIN, "false");
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }
                } else {
                    DebugLog.i("Logout Failed Response");
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                AlertUtils.dismissDialog();
                DebugLog.e("Logout Failed Request");
            }
        }, call);
        apiRequestManager.execute();
    }


    /**
     * Called when push comes and app is running.
     */

    public void openPopUp(final OrderData orderData) {

        clearNotification();
        NewHomeFragment.showTransparentLayout = false;

        try {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    drawer.closeDrawer(GravityCompat.START);
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.placeHolder);
                    DebugLog.i("Order Data Status : " + orderData.getStatus());


                    if (orderData.getStatus().equalsIgnoreCase("Waiting") && orderData.getPaymentStatus().equalsIgnoreCase("0"))  {
                        if (fragment instanceof NewHomeFragment) {

                        } else {
                            openHome();
                        }
                    } else if (orderData.getStatus().equals("Processing")) {
                        if (fragment instanceof LetsGoFragment) {

                        } else {
                            openLetsGoFragment();
                        }

                    } else if (orderData.getStatus().equalsIgnoreCase("Assigned")) {
                        if (fragment instanceof TrackDriverFragment) {

                        } else {
                            openTrackDriver();
                        }

                    } else if (orderData.getStatus().equalsIgnoreCase("Arrived")) {


                        if (!DataToPref.getSharedPreferanceData(getApplicationContext(), LaoxiConstant.IS_PROVIDE_DROPOFF, LaoxiConstant.IS_PROVIDE_DROPOFF_DATA).equalsIgnoreCase("true")) {
                            if (fragment instanceof DriverArrivedFragment) {

                            } else {
//                                openDriverArrived();
                            }
                        } else {
                            if (fragment instanceof LetsGoFragment) {

                            } else {
                                openLetsGoFragment();
                            }
                        }

                    } else if (orderData.getStatus().equalsIgnoreCase("Completed") && orderData.getPaymentStatus().equalsIgnoreCase("0")) {
                        if (fragment instanceof ReceiptFragment) {

                        } else {
                            openReceipt();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(2);
    }

    public void wsCallGetCarType() {
        ApiService api = RetroClient.getApiService();
        Call<VehicleType> call = api.getServiceType(user.getToken());

        call.enqueue(new Callback<VehicleType>() {
            @Override
            public void onResponse(Call<VehicleType> call, Response<VehicleType> response) {
                if(response.isSuccessful()) {
                    if(response.code() == 200) {
                        vehicleType = response.body();
                        String data = new Gson().toJson(vehicleType);
                        DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.STORED_SERVICE_TYPE, LaoxiConstant.DATA, data);
                    }
                }
                else {
                    DebugLog.i("Logout Failed Response");
                }
            }

            @Override
            public void onFailure(Call<VehicleType> call, Throwable t) {
                DebugLog.e("GetServiceType Request Failed :" + t.getMessage());
            }
        });
    }
    /**
     * Call a trac ride api when comes to back ground to fore ground.
     */

    public void wsCallTackRide() {
        clearNotification();
        if(cTimer!=null)
        {
            cTimer.cancel();
            NewHomeFragment.showTransparentLayout = false;
        }

        NewHomeFragment.showTransparentLayout = false;

        String strToken = user.getToken();
        String strUserId = user.getId();

        DebugLog.i("TrackRide State Request");
        ApiService api = RetroClient.getApiService();
        Call<OrderData> call = api.doTrackRide(strToken, strUserId, "user");

        call.enqueue(new Callback<OrderData>() {
            @Override
            public void onResponse(Call<OrderData> call, retrofit2.Response<OrderData> response) {
                if ( response.isSuccessful() ) {
                    try {
                        orderData = response.body();
                        openPopUp(orderData);

                        final int apiAppVersion=Integer.parseInt(orderData.getSettings().getAndroidVersion());
                        int appCurrentVersion=getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

                        if(!DataToPref.getSharedPreferanceData(getApplicationContext(), LaoxiConstant.LAST_APP_VERSION_FROM_API, LaoxiConstant.LAST_APP_VERSION_FROM_API_DATA).equalsIgnoreCase(String.valueOf(apiAppVersion)))
                        {
                            if(appCurrentVersion<apiAppVersion)
                            {
                                GeneralStaticAlertDialog.getInstance().openAppVersionUpdateDialog(HomeActivity.this, new DialogDismissOneButton() {
                                    @Override
                                    public void onDismiss() {
                                        DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.IS_SHOW_APPVERSION_UPDATE_DIALOG, LaoxiConstant.IS_SHOW_APPVERSION_UPDATE_DIALOG_DATA, "false");
                                        DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.LAST_APP_VERSION_FROM_API, LaoxiConstant.LAST_APP_VERSION_FROM_API_DATA, String.valueOf(apiAppVersion));
                                    }
                                });
                            }
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    DebugLog.e("TrackRide Response Failed: " + response.code());
                    if (response.code() == 400 ) {
                        try {
                            String responseString = response.errorBody().string();
                            JSONObject jsonObject = new JSONObject(responseString);
                            JSONObject jsonObject1=jsonObject.getJSONObject("settings");
                            final int apiAppVersion=Integer.parseInt(jsonObject1.getString("android_version"));
                            int appCurrentVersion=getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
                            if(!DataToPref.getSharedPreferanceData(getApplicationContext(), LaoxiConstant.LAST_APP_VERSION_FROM_API, LaoxiConstant.LAST_APP_VERSION_FROM_API_DATA).equalsIgnoreCase(String.valueOf(apiAppVersion)))
                            {
                                if(appCurrentVersion<apiAppVersion)
                                {
                                    GeneralStaticAlertDialog.getInstance().openAppVersionUpdateDialog(HomeActivity.this, new DialogDismissOneButton() {
                                        @Override
                                        public void onDismiss() {
                                            DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.IS_SHOW_APPVERSION_UPDATE_DIALOG, LaoxiConstant.IS_SHOW_APPVERSION_UPDATE_DIALOG_DATA, "false");
                                            DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.LAST_APP_VERSION_FROM_API, LaoxiConstant.LAST_APP_VERSION_FROM_API_DATA, String.valueOf(apiAppVersion));
                                        }
                                    });
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderData> call, Throwable t) {
                DebugLog.e("TrackRide Request Failed :" + t.getMessage());
            }
        });
    }

    /**
     * Call a function when push is comes.
     */

    public void manageCancelTripPush(final NotificationHandler notificationHandler) {

//        AlertUtils.showCustomProgressDialog(this);
        clearNotification();
        if(cTimer!=null)
        {
            cTimer.cancel();
            NewHomeFragment.showTransparentLayout = false;
        }

        final Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.placeHolder);

        if (notificationHandler.getFlag().equalsIgnoreCase("accept_order")) {

            wsCallGetOrderDetails(notificationHandler.getOrderId());

        } else if (notificationHandler.getFlag().equalsIgnoreCase("arrived_pickup")) {
            wsCallGetOrderDetails(notificationHandler.getOrderId());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    GeneralStaticAlertDialog.getInstance().DismissDilog();
                    GeneralStaticAlertDialog.getInstance().createDialogWithOneButton(HomeActivity.this, "", notificationHandler.getMessage(), new DialogDismissOneButton() {
                        @Override
                        public void onDismiss() {

                        }
                    });
                }
            });
        } else if (notificationHandler.getFlag().equalsIgnoreCase("pickup")) {
            wsCallGetOrderDetails(notificationHandler.getOrderId());

        } else if (notificationHandler.getFlag().equalsIgnoreCase("dropoff_trip")) {
            wsCallGetOrderDetails(notificationHandler.getOrderId());

        } else if (notificationHandler.getFlag().equalsIgnoreCase("cancel_trip")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    GeneralStaticAlertDialog.getInstance().DismissDilog();
                    GeneralStaticAlertDialog.getInstance().createDialogWithOneButton(HomeActivity.this, "", notificationHandler.getMessage(), new DialogDismissOneButton() {
                        @Override
                        public void onDismiss() {
                            NewHomeFragment.showTransparentLayout = false;
                            DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.IS_PROVIDE_DROPOFF, LaoxiConstant.IS_PROVIDE_DROPOFF_DATA, "false");
                            openHome();
                            //stopTrackRideService();
                        }
                    });
                }
            });
        }else if (notificationHandler.getFlag().equalsIgnoreCase("decline_request")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    GeneralStaticAlertDialog.getInstance().DismissDilog();
                    GeneralStaticAlertDialog.getInstance().createDialogWithOneButton(HomeActivity.this, "", notificationHandler.getMessage(), new DialogDismissOneButton() {
                        @Override
                        public void onDismiss() {
                            AlertUtils.dismissDialog();
                            NewHomeFragment.showTransparentLayout = false;
                            DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.IS_PROVIDE_DROPOFF, LaoxiConstant.IS_PROVIDE_DROPOFF_DATA, "false");
                            //fired event when decline request

                            EventBus.getDefault().post(user);
                            //openHome();
                            //stopTrackRideService();
                        }
                    });
                }
            });
        }

    }


    public void stopLocationUpdate() {
        Intent intent = new Intent(HomeActivity.this, LocationHelperService.class);
        stopService(intent);
    }

    //modify by prakash for getting order details

    public void wsCallGetOrderDetails(String orderId) {
        bGetOrder = true;
        String strToken = user.getToken();
        String strUserId = user.getId();

//        AlertUtils.showCustomProgressDialog(this);
        DebugLog.i("GetOrder Detail Request");
        ApiService api = RetroClient.getApiService();
        Call<OrderData> call = api.doGetOrderDetails(strToken, orderId, strUserId);

        call.enqueue(new Callback<OrderData>() {
            @Override
            public void onResponse(Call<OrderData> call, retrofit2.Response<OrderData> response) {
                if ( response.isSuccessful() ) {
                    OrderData orderData = response.body();
                    drawer.closeDrawer(GravityCompat.START);
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.placeHolder);
                    String strOrderData = new Gson().toJson(orderData);
                    DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.ORDER, LaoxiConstant.ORDER_DATA, strOrderData);
                    DebugLog.i("Order Detail Success Response :" + orderData.getStatus());

                    if (orderData.getStatus().equalsIgnoreCase("Waiting") && orderData.getPaymentStatus().equalsIgnoreCase("0"))  {
                        if (fragment instanceof NewHomeFragment) {

                        } else {
                            openHome();
                        }
                    } else if (orderData.getStatus().equals("Processing")) {
                        if (fragment instanceof LetsGoFragment) {

                        } else {
                            openLetsGoFragment();
                        }
                    } else if (orderData.getStatus().equalsIgnoreCase("Assigned")) {
                        if (fragment instanceof TrackDriverFragment) {

                        } else {
                            openTrackDriver();
                        }
                    } else if (orderData.getStatus().equalsIgnoreCase("Arrived")) {
                        if (!DataToPref.getSharedPreferanceData(getApplicationContext(), LaoxiConstant.IS_PROVIDE_DROPOFF,
                                LaoxiConstant.IS_PROVIDE_DROPOFF_DATA).equalsIgnoreCase("true")) {
                            if (fragment instanceof DriverArrivedFragment) {

                            } else {
//                                            openDriverArrived();
                            }
                        } else {
                            if (fragment instanceof LetsGoFragment) {

                            } else {
                                openLetsGoFragment();
                            }
                        }
                    } else if (orderData.getStatus().equalsIgnoreCase("Completed") && orderData.getPaymentStatus().equalsIgnoreCase("0")) {
                        if (fragment instanceof ReceiptFragment) {
                        } else {
                            openReceipt();
                        }
                    }
                } else {
                    try {
                        String s = response.errorBody().string();
                        GenericResponse genericResponse = ParsingHelper.getInstance().genericResponseParsing(s);
                        DebugLog.e("GetOrder Response Failed :" + genericResponse.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (response.code() == 401) {

                    } else if (response.code() == 404) {

                    }
                }
            }

            @Override
            public void onFailure(Call<OrderData> call, Throwable t) {
//                AlertUtils.dismissDialog();
            }
        });

    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

}// end of main class
