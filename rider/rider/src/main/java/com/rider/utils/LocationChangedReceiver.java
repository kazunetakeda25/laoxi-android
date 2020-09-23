package com.rider.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Button;

import com.rider.R;


public class LocationChangedReceiver extends android.content.BroadcastReceiver {

    public static  AlertDialog.Builder alertDialogBuilder;
    public static AlertDialog alertDialog;
    Button btn_ok;
    boolean anyLocationProv;


    @Override
    public void onReceive(Context context, Intent intent) {

        anyLocationProv = false;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        anyLocationProv |= locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        anyLocationProv |= locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.e("", "Location service status" + anyLocationProv);


        if (anyLocationProv) {

            if (null == alertDialog) {

            } else {

                alertDialog.dismiss();
            }

        } else {

            if (null == alertDialog) {

                createDialog(context);

            } else {
                alertDialog.dismiss();
                createDialog(context);
                alertDialog.show();
            }


        }


    }

    public static void createDialog(final Context context) {


        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(context.getString(R.string.app_name)).setMessage(context.getString(R.string.alertdialog_errror_message_location_enable)).setCancelable(false);
        alertDialogBuilder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(viewIntent);
            }
        });
        alertDialogBuilder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}