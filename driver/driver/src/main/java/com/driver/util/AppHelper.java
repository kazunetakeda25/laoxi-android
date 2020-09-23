package com.driver.util;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;

import com.driver.application.LAOXI;
import com.google.gson.Gson;
import com.driver.dao.ParsingHelper;
import com.driver.listener.ISpinnerItemClick;
import com.driver.pojo.loginws.LoginResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Created by Chirag on 13/04/2016.
 */
public class AppHelper {


    private static AppHelper ourInstance = new AppHelper();

    public static AppHelper getInstance() {
        return ourInstance;
    }

    private AppHelper() {


    }

    public void SaveInSharedPref(String Key, String text) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = LAOXI.getInstance().getSharedPreferences(ConstantClass.PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2
        editor.putString(Key, text); //3
        editor.commit(); //4
    }

    public void SaveInSharedPref(String Key, String text,boolean b) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = LAOXI.getInstance().getSharedPreferences(ConstantClass.PREFS_NAME_USERNAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2
        editor.putString(Key, text); //3
        editor.commit(); //4
    }

    public String getSharedPref(String Key,boolean b) {
        SharedPreferences settings;
        String text;
        settings = LAOXI.getInstance().getSharedPreferences(ConstantClass.PREFS_NAME_USERNAME, Context.MODE_PRIVATE); //1
        text = settings.getString(Key, null); //2
        return text;
    }


    public String getSharedPref(String Key) {
        SharedPreferences settings;
        String text;
        settings = LAOXI.getInstance().getSharedPreferences(ConstantClass.PREFS_NAME, Context.MODE_PRIVATE); //1
        text = settings.getString(Key, null); //2
        return text;
    }

    public void SaveInSharePrefWithBoolean(String Key, boolean value) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = LAOXI.getInstance().getSharedPreferences(ConstantClass.PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2
        editor.putBoolean(Key, value); //3
        editor.commit(); //4
    }

    public boolean getSharedPrefWithBoolean(String Key) {
        SharedPreferences settings;
        boolean text;
        settings = LAOXI.getInstance().getSharedPreferences(ConstantClass.PREFS_NAME, Context.MODE_PRIVATE); //1
        text = settings.getBoolean(Key, false); //2
        return text;
    }

    /**
     * it return encoaded Uri
     *
     * @param thisUrl
     * @return
     */
    public Uri getEncoadedURI(String thisUrl) {
        URL url = null;
        try {
            url = new URL(thisUrl);
        } catch (MalformedURLException e) {

            e.printStackTrace();
        }
        Uri.Builder builder = new Uri.Builder()
                .scheme(url.getProtocol())
                .authority(url.getAuthority())
                .appendPath(url.getPath());
        return builder.build();
    }

    public LoginResponse getLoginUser() {
        LoginResponse loginResponse = ParsingHelper.getInstance().loginResponseParsing(getSharedPref(ConstantClass.LOGIN_USER));
        if (loginResponse == null) {
            DebugLog.e("Login Data Parsing Error");
        }
        return loginResponse;
    }

    public String getToken() {

        String strToken = getSharedPref(ConstantClass.HEADER_TOCKEN);
        if (strToken == null) {
            DebugLog.e("strToken Parsing Error");
        }
        return strToken;
    }

    public String getDriverId() {

        String strDriverID = getSharedPref(ConstantClass.DRIVER_ID);
        if (strDriverID == null) {
            DebugLog.e("strDriverID Parsing Error");
        }
        return strDriverID;
    }

    public void clearSharedPrefWithKey(String prefName, String key) {
        SharedPreferences preferences = LAOXI.getInstance().getSharedPreferences(prefName, 0);
        preferences.edit().remove(key).commit();
    }

    public void clearSharedPreferance(String prefName) {
        SharedPreferences settings = LAOXI.getInstance().getSharedPreferences(ConstantClass.PREFS_NAME, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }

    public Fragment getCurrentFragment(FragmentManager supportFragmentManager) {
        FragmentManager fragmentManager = supportFragmentManager;
        DebugLog.e("Count ::" + fragmentManager.getBackStackEntryCount());
        Fragment currentFragment;
        if (fragmentManager.getBackStackEntryCount() == 0) {
            return null;
        } else {
            String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
            currentFragment = supportFragmentManager.findFragmentByTag(fragmentTag);
        }
        return currentFragment;
    }

    public String ObjectToString(Object o) {
        DebugLog.e("Message Data -------------------Test Call");
        Gson gson = new Gson();
        String json = gson.toJson(o);
        DebugLog.e("Message Data -------------------" + json);
        return json;
    }

    public long getDifferentBetweenTwoDate(Date current, Date nextDate) {
        long diff = nextDate.getTime() - current.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        DebugLog.e("diff ::" + diff);
        DebugLog.e("minutes ::" + minutes);
        return minutes;
    }
    /**
     * This method is used for to open spinner
     *
     * @param context  where you want to display spinner dialog.
     * @param dataList define spinner collection
     * @param callback return selected text
     */
    public void openSpinnerpopup(Context context, String header, final List<String> dataList, final ISpinnerItemClick callback) {

        final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, dataList);

        new AlertDialog.Builder(context)
                .setTitle(header)
                .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        callback.onItemClick(dataList.get(which));
                        dialog.dismiss();
                    }
                }).create().show();
    }


}
