package com.driver.util;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.location.Location;
import android.view.Window;

import com.google.android.gms.maps.model.LatLng;
import com.driver.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc

/**
 * The Class AlertUtils.
 */
public class AlertUtils {
    AlertUtilsListener callback;
    public static Dialog dialog;
    public static RotateLoading rotateLoading;
    public interface AlertUtilsListener {
        void onButtonClick();
    }

    /**
     * Alert info.
     *
     * @param mContext           the m context
     * @param title              the title
     * @param msg                the msg
     * @param positiveButtonText the positive button text
     */

    public static void alertInfo(Context mContext, String title, String msg, String positiveButtonText, final AlertUtilsListener callback, boolean isCancelable)// dialogbox
    {
        String title1 = title;
        String msg1 = msg;
        AlertDialog.Builder al = new AlertDialog.Builder(mContext);
        al.setCancelable(isCancelable);
        al.setTitle(title1);
        al.setMessage(msg1);
        al.setPositiveButton(positiveButtonText, new OnClickListener() {

            @Override
            public void onClick(DialogInterface d, int arg1) {

                d.cancel();
                if (callback != null) {
                    callback.onButtonClick();
                }

            }
        });
        al.show();
    }


    public static boolean isValidMail(String strEmail) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(strEmail);
        check = m.matches();

        return check;
    }

    /**
     * Hitesh Kamani
     *
     * @param //title:-       this is title for custom dialog box
     * @param //description:- this is description for custom dialog box
     */
    public static void showCustomProgressDialog(Context con) {
        // create a Dialog component

      //  DebugLog.e("this is called from home ");
        if (dialog != null ) {
            dialog.dismiss();
        }
        dialog = new Dialog(con);
        dialog.setCancelable(false);
        // this line removes title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //tell the Dialog to use the dialog.xml as it's layout description
        dialog.setContentView(R.layout.loading_custom_taxiapp);
        rotateLoading = (RotateLoading) dialog.findViewById(R.id.rotateloading);
        dialog.show();
        rotateLoading.start();
    }// end of showCustomProgressDialog

    public static void dismissDialog() {
        try {
            if (dialog != null ) {
                dialog.dismiss();
                rotateLoading.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static float distance(Location l1, Location l2) {
        if (l1 == null || l2 == null) return 0;
        return l1.distanceTo(l2);
    }



    public static float distance(LatLng point1, LatLng point2) {
        if (point1 == null || point2 == null) return 0;
        Location location1 = new Location("");
        location1.setLatitude(point1.latitude);
        location1.setLongitude(point1.longitude);
        Location location2 = new Location("");
        location2.setLatitude(point2.latitude);
        location2.setLongitude(point2.longitude);
        return location1.distanceTo(location2);


    }


}
