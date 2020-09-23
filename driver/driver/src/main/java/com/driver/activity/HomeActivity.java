package com.driver.activity;


import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.driver.R;
import com.driver.adapter.ListAdapter;
import com.driver.application.LAOXI;
import com.driver.customclasses.CustomTextView;
import com.driver.dao.ParsingHelper;
import com.driver.dialog.GeneralStaticAlertDialog;
import com.driver.dialog.NewRequestRideDialog;
import com.driver.error.ExceptionHandler;
import com.driver.fragments.ArrivedFragment;
import com.driver.fragments.BaseFragment;
import com.driver.fragments.CompleteRideFragment;
import com.driver.fragments.FeedbackFragment;
import com.driver.fragments.HomeFragment;
import com.driver.fragments.MainAboutUsFragment;
import com.driver.fragments.PastRideFutureRideViewPagerFragment;
import com.driver.fragments.ProfileFragment;
import com.driver.fragments.ReceiptFragment;
import com.driver.fragments.StartRideFragment;
import com.driver.fragments.SupportFragment;
import com.driver.listener.IRingtoneHandler;
import com.driver.navigator.NavigatorInterface;
import com.driver.pojo.GenericResponse;
import com.driver.pojo.ListDataPojo;
import com.driver.pojo.NotificationHandler;
import com.driver.pojo.TrackRidePojo.TrackRide;
import com.driver.pojo.TrackRidePojo.TrackRidePojo;
import com.driver.pojo.loginws.LoginResponse;
import com.driver.retrofit.APIRequestManager;
import com.driver.retrofit.ApiService;
import com.driver.retrofit.RetroClient;
import com.driver.services.UpdateDriverLatLongService;
import com.driver.util.AlertUtils;
import com.driver.util.AnalyticsReporter;
import com.driver.util.AppHelper;
import com.driver.util.ConstantClass;
import com.driver.util.DebugLog;
import com.driver.util.DialogCancel;
import com.driver.util.DialogDismissOneButton;
import com.driver.util.DialogDismissTwoButton;
import com.driver.util.HomeWatcher;
import com.driver.util.LaoxiConstant;
import com.driver.util.RecyclerItemClickSupport;
import com.eventbus.FcmEvent;
import com.github.siyamed.shapeimageview.ImageViewSlider;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;

//Key Hash : J8PzdpjNH3dFwc5V3GpCirXcLAU=
public class HomeActivity extends BaseActivity implements View.OnClickListener, NavigatorInterface, IRingtoneHandler {

    @Bind(R.id.placeHolder)
    FrameLayout placeHolder;
    @Bind(R.id.linerLayoutRoot)
    LinearLayout linerLayoutRoot;
    @Bind(R.id.txtILogout)
    CustomTextView txtILogout;
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.tvDriverName)
    CustomTextView tvDriverName;
    @Bind(R.id.profileBG)
    ImageViewSlider profileBG;

    AlertDialog.Builder builder;
    public static boolean activeornot;
    public static boolean openNotification;
    ActionBarDrawerToggle toggle;

    private static MediaPlayer mp;
    private float lastTranslate;
    private RecyclerView navigationListView;
    public static DrawerLayout drawer;
    private static final Integer[] IMAGES = {R.drawable.home, R.drawable.your_ride_icon, R.drawable.my_account_icon, R.drawable.about_icon, R.drawable.support_icon};
    private String[] MENUITEAMS;
    private ArrayList<ListDataPojo> list;
    private ListAdapter adapter;
    private ProgressDialog dialog;
    private boolean bGetOrder = false;
    public Intent intentUpdateLatLong;
    public static NotificationHandler notificationHandler;
    public boolean callStateManagment = true;
    private HomeWatcher mHomeWatcher;

    public static void setNotificationHandler(NotificationHandler notificationHandler) {
        HomeActivity.notificationHandler = notificationHandler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExceptionHandler.register(this, LaoxiConstant.ERROR_LINK);
        setContentView(R.layout.activity_main);

        try {
            // LocationDataProvider.getInstance().register(this); // Bus Register
            ButterKnife.bind(this);
            tvDriverName.setText(AppHelper.getInstance().getLoginUser().getFname());
            DebugLog.i("Profile Path " + ConstantClass.IMAGE_URL + AppHelper.getInstance().getLoginUser().getProfileImage());
            Picasso.with(this)
                    .load(ConstantClass.THUMB_IMAGE_URL + AppHelper.getInstance().getLoginUser().getProfileImage())
                    .fit()
                    .into(profileBG);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            startUpdateLatLongService();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            navigationListView = (RecyclerView) findViewById(R.id.navigationListView);
            linerLayoutRoot = (LinearLayout) findViewById(R.id.linerLayoutRoot);
            profileBG.setOnClickListener(this);
            txtILogout.setOnClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        if (savedInstanceState == null) {
            openHomeFragment();
        }

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

        /**
         * Set a adapter for slider get a click event when click any item.
         */

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        navigationListView.setLayoutManager(layoutManager);
        adapter = new ListAdapter(MENUITEAMS, IMAGES, this);
        navigationListView.setItemAnimator(new DefaultItemAnimator());
        navigationListView.setAdapter(adapter);

        RecyclerItemClickSupport.addTo(navigationListView).setOnItemClickListener(new RecyclerItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Log.e("postion", "" + position);
                switch (position) {
                    case 0:
                        HomeFragment fragment = new HomeFragment();
                        getSupportFragmentManager().popBackStack(new HomeFragment().getClass().getName().toString(), 1);
                        addFragment(fragment, true, fragment.getClass().getName());
                        break;
                    case 1:
                        //My Payments
                        /*closeDrawer();
                        closeKeyboard(HomeActivity.this);
                        PaymentFragment paymentFragment = new PaymentFragment();
                        getSupportFragmentManager().popBackStack(new HomeFragment().getClass().getName().toString(), 0);
                        addFragment(paymentFragment, true, paymentFragment.getClass().getName());*/
                        closeKeyboard(HomeActivity.this);
                        PastRideFutureRideViewPagerFragment pastRideFutureRideViewPagerFragment = new PastRideFutureRideViewPagerFragment();
                        getSupportFragmentManager().popBackStack(new HomeFragment().getClass().getName().toString(), 0);
                        addFragment(pastRideFutureRideViewPagerFragment, true, pastRideFutureRideViewPagerFragment.getClass().getName());

                        break;

                    case 2:
                        //my Account
                        ProfileFragment profileFragment = new ProfileFragment();
                        getSupportFragmentManager().popBackStack(new HomeFragment().getClass().getName().toString(), 0);
                        addFragment(profileFragment, true, profileFragment.getClass().getName());
                        break;
                    case 3:
                        //Support
                        closeKeyboard(HomeActivity.this);
                        //AboutUsFragment aboutUsFragment = new AboutUsFragment();
                        MainAboutUsFragment aboutUsFragment = new MainAboutUsFragment();
                        getSupportFragmentManager().popBackStack(new HomeFragment().getClass().getName().toString(), 0);
                        addFragment(aboutUsFragment, true, aboutUsFragment.getClass().getName());

                        break;
                    case 4:
                        closeKeyboard(HomeActivity.this);
                        SupportFragment supportFragment = new SupportFragment();
                        getSupportFragmentManager().popBackStack(new HomeFragment().getClass().getName().toString(), 0);
                        addFragment(supportFragment, true, supportFragment.getClass().getName());
                        break;
                }
            }
        });

        //added by prakash for getting a event when user press home btton
        try {

            mHomeWatcher = new HomeWatcher(this);
            mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
                @Override
                public void onHomePressed() {
                    callStateManagment = true;
                    Log.e("dfs", "onHomePressed");
                }

                @Override
                public void onHomeLongPressed() {
                    callStateManagment = true;
                    Log.e("fdg", "recent apps");
                }
            });
            mHomeWatcher.startWatch();
        } catch (Exception e) {
        }

        getSupportFragmentManager().addOnBackStackChangedListener(new android.support.v4.app.FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                BaseFragment baseFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.placeHolder);
                if (baseFragment != null) {
                    baseFragment.drawerLock();
                }
            }
        });
    }// end of onCreate

    @Override
    protected void onResume() {
        super.onResume();

        checkLocation(this);
        EventBus.getDefault().register(this);
        LAOXI.setCurrentActivity(HomeActivity.this);
        LAOXI.currentActivity = HomeActivity.this;

        if (openNotification) {
            manageNotifications(notificationHandler);
            openNotification = false;
        } else {
            if (callStateManagment) {
                wsCallTrackRide();
            }
            callStateManagment = false;
        }
        activeornot = true;
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        closeKeyboard(this);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.placeHolder);
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
            } else if (fragment instanceof FeedbackFragment) {

            } else if (fragment instanceof ReceiptFragment) {

            } else if (fragment instanceof ArrivedFragment) {

            } else if (fragment instanceof StartRideFragment) {

            } else if (fragment instanceof CompleteRideFragment) {

            } else if (fragment instanceof HomeFragment) {
                finish();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        activeornot = false;
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profileBG:
                closeKeyboard(HomeActivity.this);
                ProfileFragment profileFragment = new ProfileFragment();
                getSupportFragmentManager().popBackStack(new HomeFragment().getClass().getName().toString(), 0);
                addFragment(profileFragment, true, profileFragment.getClass().getName());
                break;
            case R.id.txtILogout:
                GeneralStaticAlertDialog.getInstance().createDialogWithTwoButton(HomeActivity.this, "Are you sure you want to logout?", new DialogDismissTwoButton() {
                    @Override
                    public void onOkClick() {
                        callLogoutWS();
                    }

                    @Override
                    public void onCancelClick() {

                    }
                });
                break;
        }
    }

    /**
     * Call a log out api.
     */
    public void callLogoutWS() {
        // Retrofit API
        AlertUtils.showCustomProgressDialog(this);
        String strToken = AppHelper.getInstance().getToken();
        DebugLog.i("Call Logout Request");
        ApiService api = RetroClient.getApiService();
        Call<Void> call = api.logOut(strToken, AppHelper.getInstance().getDriverId());

        APIRequestManager<Void> apiRequestManager = new APIRequestManager<>(new APIRequestManager.ResponseListener<Void>() {
            @Override
            public void onResponse(retrofit2.Response<Void> response) {
                AlertUtils.dismissDialog();
                if (response.isSuccessful()) {
                    DebugLog.i("Logout Success Response");
                    if (intentUpdateLatLong != null) {
                        stopService(intentUpdateLatLong);
                    }

                    LoginResponse loginResponse = AppHelper.getInstance().getLoginUser();
                    AnalyticsReporter.getInstance().setProfile(
                            loginResponse.getDriverId(),
                            loginResponse.getEmail(),
                            loginResponse.getFname(),
                            loginResponse.getLname(),
                            "",
                            "",
                            loginResponse.getLastLogin());
                    Bundle properties = new Bundle();
                    properties.putString("driver_id", loginResponse.getDriverId());
                    properties.putString("email", loginResponse.getEmail());
                    properties.putString("first_name", loginResponse.getFname());
                    properties.putString("last_name", loginResponse.getLname());
                    properties.putString("last_login", loginResponse.getLastLogin());
                    AnalyticsReporter.getInstance().track(AnalyticsReporter.Event.DRIVER_LOGOUT, properties);
                    AnalyticsReporter.getInstance().removeIdentify();

                    AppHelper.getInstance().clearSharedPreferance(ConstantClass.PREFS_NAME);
                    AppHelper.getInstance().SaveInSharePrefWithBoolean(ConstantClass.IS_LOGIN, false);

                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    DebugLog.i("Logout Failed Response");
                    if (response.code() == ConstantClass.RESPONSE_CODE_401) {
                        AppHelper.getInstance().SaveInSharePrefWithBoolean(ConstantClass.IS_LOGIN, false);

                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                DebugLog.e("Logout Request Failed Msg :" + throwable.getMessage());
                AlertUtils.dismissDialog();
                showSnackbar(getString(R.string.bad_server));
            }
        }, call);
        apiRequestManager.execute();
    }

    public void Logout(boolean directLogout) {
        if (webServiceState == WebServiceState.ACTIVE) {

            //modify by prakash
            AppHelper.getInstance().clearSharedPreferance(ConstantClass.PREFS_NAME);
            AppHelper.getInstance().SaveInSharePrefWithBoolean(ConstantClass.IS_LOGIN, false);

            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        } else {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    public static void toggleDrawer() {
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        }
    }

    public void addFragment(Fragment fragment, Boolean backStack, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.placeHolder, fragment);
        if (backStack)
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    public void replaceFragment(Fragment fragment, Boolean backStack, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.placeHolder, fragment);
        if (backStack)
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
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
    public void closeDrawer() {
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
        }
    }

    @Override
    public void startUpdateLatLongService() {
        intentUpdateLatLong = new Intent(HomeActivity.this, UpdateDriverLatLongService.class);
        startService(intentUpdateLatLong);
    }

    @Override
    public void updateDriverProfile() {
        tvDriverName.setText(AppHelper.getInstance().getLoginUser().getFname());
        Picasso.with(this)
                .load(ConstantClass.THUMB_IMAGE_URL + AppHelper.getInstance().getLoginUser().getProfileImage())
                .resize(200, 200)
                .into(profileBG);
    }

    @Override
    public void openArrivedAtPickupFragment() {
        ArrivedFragment arrivedFragment = new ArrivedFragment();
        getSupportFragmentManager().popBackStack(new HomeFragment().getClass().getName().toString(), 0);
        replaceFragment(arrivedFragment, true, arrivedFragment.getClass().getName());
    }

    @Override
    public void openStartRideFragment() {
        StartRideFragment startRideFragment = new StartRideFragment();
        getSupportFragmentManager().popBackStack(new HomeFragment().getClass().getName().toString(), 0);
        replaceFragment(startRideFragment, true, startRideFragment.getClass().getName());
    }

    @Override
    public void openCompleteRideFragment() {
        CompleteRideFragment completeRideFragment = new CompleteRideFragment();
        getSupportFragmentManager().popBackStack(new HomeFragment().getClass().getName().toString(), 0);
        replaceFragment(completeRideFragment, true, completeRideFragment.getClass().getName());
    }

    @Override
    public void drawerLock(boolean b) {
        if (b) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    @Override
    public void stopUpdateLatLongService() {

    }

    @Override
    protected void onDestroy() {
        stopLocationUpdate();
        if (mHomeWatcher != null) {
            mHomeWatcher.stopWatch();
        }
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void openTrackRideFragment() {

    }

    @Override
    public void openReceiptFragment(Fragment fragment) {
        try {
            getSupportFragmentManager().popBackStack(new HomeFragment().getClass().getName().toString(), 0);
            addFragment(fragment, true, fragment.getClass().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openFeedbackFragment(Fragment fragment) {

        closeKeyboard(this);
        getSupportFragmentManager().popBackStack(new HomeFragment().getClass().getName().toString(), 0);
        addFragment(fragment, true, fragment.getClass().getName());
    }

    @Override
    public void openHomeFragment() {
        HomeFragment fragment = new HomeFragment();
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        addFragment(fragment, true, fragment.getClass().getName());
    }

    public void callSupportTeam(String s) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + s));
            startActivity(intent);
        } catch (Exception e) {
            Log.e("Demo application", "Failed to invoke call", e);
        }
    }

    public void openPopUp(final TrackRidePojo orderData) {

        clearNotification();

        if (orderData.getTrackTrip().getStatus() == null)
            return;
        try {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

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

    /**
     * Play music.
     */

    @Override
    public void startRing() {
        try {
            mp = MediaPlayer.create(HomeActivity.this, R.raw.new_sound_file);
            mp.setLooping(true);
            mp.seekTo(0);
            mp.setVolume(1, 1);
            mp.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop music.
     */

    @Override
    public void stopRing() {

        try {
            if (mp != null)
                mp.stop();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Display a dialog when cancellation push comes.
     */

    public void showRequestExpiredDialog(String strMessage) {
        if (builder == null) {
            builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.app_name));
            builder.setMessage(strMessage)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
//                            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
//                            startActivity(intent);
//                            HomeActivity.this.finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    /**
     * Display a dialog when cancellation push comes.
     */

    public void showRideCancelDialog() {
        if (builder == null) {
            builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.app_name));
            builder.setMessage("Your ride has been cancelled by rider")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> {
                        openHomeFragment();
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    //added by rhc 20180625
    public void showAlreadyAcceptedDialog() {
        if (builder == null) {
            builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.app_name));
            builder.setMessage(R.string.already_accepted)
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> openHomeFragment());
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    /**
     * Handle a notification.
     */
    public void manageNotifications(final NotificationHandler notificationHandler) {
        try {
            this.runOnUiThread(() -> {

                drawer.closeDrawer(GravityCompat.START);
                stopRing();
                clearNotification();

                Bundle properties = new Bundle();
                properties.putString("order_id", notificationHandler.getOrderId());
                properties.putString("driver_id", notificationHandler.getDriverId());
                properties.putString("user_id", notificationHandler.getUserId());
                properties.putString("flag", notificationHandler.getFlag());
                properties.putString("trip_type", notificationHandler.getTripType());
                properties.putString("message", notificationHandler.getMessage());

                String flag = notificationHandler.getFlag().toLowerCase();
                DebugLog.i("Received Notification Flag : " + flag + " , Message :" + notificationHandler.getMessage());

                switch (flag) {
                    case "placeorder":
                        AnalyticsReporter.getInstance().track(AnalyticsReporter.Event.DRIVER_RECEIVED_ORDER_REQUEST, properties);
                        callGetOrderWs(notificationHandler.getOrderId());
                        break;
                    case "request_expired":
                        AnalyticsReporter.getInstance().track(AnalyticsReporter.Event.DRIVER_RECEIVED_ORDER_CANCELED, properties);
                        showRequestExpiredDialog(notificationHandler.getMessage());
                        break;
                    case "payment":
                        AnalyticsReporter.getInstance().track(AnalyticsReporter.Event.DRIVER_RECEIVED_PAYMENT, properties);
                        break;
                    case "cancel_trip":
                        AnalyticsReporter.getInstance().track(AnalyticsReporter.Event.DRIVER_RECEIVED_ORDER_CANCELED, properties);
                        showRideCancelDialog();
                        break;
                    case "updatedropoff":
                        callGetOrderWs(notificationHandler.getOrderId());
                        break;
                    case "already_accepted":
                        EventBus.getDefault().post(FcmEvent.AlreadyAccepted.INSTANCE);
                        showAlreadyAcceptedDialog();
                        break;
                    default:
                        break;
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Call a get order details api.
     */

    private void callGetOrderWs(String orderId) {

        AlertUtils.dismissDialog();
        String strToken = AppHelper.getInstance().getToken();
        String strDriverId = AppHelper.getInstance().getDriverId();
        ApiService api = RetroClient.getApiService();
        Call<TrackRide> call = api.doGetOrderDetailWS(strToken, orderId, strDriverId);
        DebugLog.i("Get New Order");

        call.enqueue(new Callback<TrackRide>() {
            @Override
            public void onResponse(Call<TrackRide> call, retrofit2.Response<TrackRide> response) {
                AlertUtils.dismissDialog();
                if (response.isSuccessful()) {
                    DebugLog.i("Get Order Success Response");
                    TrackRide trackRidePojo = response.body();

                    String data = new Gson().toJson(trackRidePojo);
                    AppHelper.getInstance().SaveInSharedPref(ConstantClass.TRACK_RIDE_ORDER_DATA, data);

                    if (trackRidePojo.getStatus().equalsIgnoreCase("Waiting")) {
                        stopRing();
                        startRing();

                        if (HomeActivity.this != null) {
                            NewRequestRideDialog.getInstance().createDialog(HomeActivity.this, new DialogCancel() {
                                @Override
                                public void onDismiss() {

                                }

                                @Override
                                public void onDialogCencel() {

                                }
                            });
                        }
                    } else if (trackRidePojo.getStatus().equalsIgnoreCase("Arrived")) {
                        openStartRideFragment();
                    }
                } else {
                    try {

                        String s = response.errorBody().string();
                        DebugLog.i("Get Order Failed Response " + s);
                        GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
                        if (response.code() == ConstantClass.RESPONSE_CODE_400) {
                            if (responseBody != null)
                                showSnackbar(responseBody.getMessage());
                        } else if (response.code() == ConstantClass.RESPONSE_CODE_401) {
                            Logout(false);
                        } else {
                            if (responseBody != null)
                                showSnackbar(responseBody.getMessage());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TrackRide> call, Throwable t) {
                AlertUtils.dismissDialog();
                DebugLog.i("Get Order Request Failed :" + t.getMessage());
                showSnackbar(getString(R.string.bad_server));
            }
        });
    }

    public void onEvent(String str) {
        stopRing();
        openArrivedAtPickupFragment();
    }

    public void wsCallTrackRide() {

        AlertUtils.dismissDialog();
        stopRing();

        String strToken = AppHelper.getInstance().getToken();
        String strDriverId = AppHelper.getInstance().getDriverId();
        ApiService api = RetroClient.getApiService();
        // AlertUtils.showCustomProgressDialog(this);
        DebugLog.i("TrackRide State Request");
        Call<TrackRide> call = api.callStateManagement(strToken, strDriverId, "driver");

        call.enqueue(new Callback<TrackRide>() {
            @Override
            public void onResponse(Call<TrackRide> call, retrofit2.Response<TrackRide> response) {
                DebugLog.i("TrackRide State Success Response Code: " + response.code());
                if (response.isSuccessful()) {
                    try {

                        TrackRide trackRidePojo = response.body();
                        if (response.code() == ConstantClass.RESPONSE_CODE_200) {

                            String data = new Gson().toJson(trackRidePojo);
                            AppHelper.getInstance().SaveInSharedPref(ConstantClass.TRACK_RIDE_ORDER_DATA, data);

                            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.placeHolder);
                            if (trackRidePojo != null) {
                                if (trackRidePojo.getStatus().equalsIgnoreCase("Waiting")) {

                                    if (!NewRequestRideDialog.getInstance().isShown()) {

                                        stopRing();
                                        startRing();
                                        NewRequestRideDialog.getInstance().createDialog(HomeActivity.this, new DialogCancel() {
                                            @Override
                                            public void onDismiss() {

                                            }

                                            @Override
                                            public void onDialogCencel() {

                                            }
                                        });
                                    }

                                } else if (trackRidePojo.getStatus().equalsIgnoreCase("Processing")) {
                                    if (fragment instanceof CompleteRideFragment) {

                                    } else {
                                        openCompleteRideFragment();
                                    }


                                } else if (trackRidePojo.getStatus().equalsIgnoreCase("Assigned")) {
                                    if (fragment instanceof ArrivedFragment) {

                                    } else {
                                        openArrivedAtPickupFragment();
                                    }

                                } else if (trackRidePojo.getStatus().equalsIgnoreCase("Arrived")) {

                                    if (fragment instanceof StartRideFragment) {

                                    } else {
                                        openStartRideFragment();
                                    }


                                }
                                final int apiAppVersion = Integer.parseInt(trackRidePojo.getSettings().getDriver_android_version());
                                int appCurrentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

                                if (appCurrentVersion < apiAppVersion) {
                                    if (AppHelper.getInstance().getSharedPref(ConstantClass.LAST_APP_VERSION_FROM_API) != null) {
                                        if (!AppHelper.getInstance().getSharedPref(ConstantClass.LAST_APP_VERSION_FROM_API).equalsIgnoreCase(String.valueOf(apiAppVersion))) {
                                            GeneralStaticAlertDialog.getInstance().openAppVersionUpdateDialog(HomeActivity.this, new DialogDismissOneButton() {
                                                @Override
                                                public void onDismiss() {
                                                    AppHelper.getInstance().SaveInSharedPref(ConstantClass.LAST_APP_VERSION_FROM_API, String.valueOf(apiAppVersion));
                                                }

                                            });
                                        }
                                    } else {
                                        GeneralStaticAlertDialog.getInstance().openAppVersionUpdateDialog(HomeActivity.this, new DialogDismissOneButton() {
                                            @Override
                                            public void onDismiss() {
                                                AppHelper.getInstance().SaveInSharedPref(ConstantClass.LAST_APP_VERSION_FROM_API, String.valueOf(apiAppVersion));
                                            }

                                        });
                                    }

                                }

                            }

                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (response.code() == ConstantClass.RESPONSE_CODE_400) {
                            String responseString = response.errorBody().string();

                            DebugLog.i("TrackRide State Failed Response Error body: ");
                            JSONObject jsonObject = new JSONObject(responseString);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("settings");
                            final int apiAppVersion = Integer.parseInt(jsonObject1.getString("driver_android_version"));
                            int appCurrentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;


                            if (appCurrentVersion < apiAppVersion) {
                                if (AppHelper.getInstance().getSharedPref(ConstantClass.LAST_APP_VERSION_FROM_API) != null) {
                                    if (!AppHelper.getInstance().getSharedPref(ConstantClass.LAST_APP_VERSION_FROM_API).equalsIgnoreCase(String.valueOf(apiAppVersion))) {
                                        GeneralStaticAlertDialog.getInstance().openAppVersionUpdateDialog(HomeActivity.this, new DialogDismissOneButton() {
                                            @Override
                                            public void onDismiss() {
                                                AppHelper.getInstance().SaveInSharedPref(ConstantClass.LAST_APP_VERSION_FROM_API, String.valueOf(apiAppVersion));
                                            }

                                        });
                                    }
                                } else {
                                    GeneralStaticAlertDialog.getInstance().openAppVersionUpdateDialog(HomeActivity.this, new DialogDismissOneButton() {
                                        @Override
                                        public void onDismiss() {
                                            AppHelper.getInstance().SaveInSharedPref(ConstantClass.LAST_APP_VERSION_FROM_API, String.valueOf(apiAppVersion));
                                        }

                                    });
                                }
                            }
                        } else if (response.code() == ConstantClass.RESPONSE_CODE_401) {
                            Logout(false);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TrackRide> call, Throwable t) {
                showSnackbar(getString(R.string.bad_server));
                DebugLog.i("TrackRide State Request Failed Msg: " + t.getMessage());
            }
        });

    }

}// end of main class
