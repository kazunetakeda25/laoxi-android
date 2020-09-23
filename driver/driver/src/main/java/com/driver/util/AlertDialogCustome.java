package com.driver.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;


/**
 * Created by chirag on 23/2/16.
 */
public class AlertDialogCustome {

    public static AlertDialog.Builder alertDialogBuilder;
    public static AlertDialog alertDialog;
    DialogDismiss dialogDismiss;

    public void createDialog(Context context, String title, String message,final DialogDismiss dialogDismiss) {

        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title).setMessage(message);
        alertDialogBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();

                if(dialogDismiss!=null){
                    dialogDismiss.onDismiss();
                }
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public interface  DialogDismiss
    {
        public void onDismiss();
    }

    public static void createDialogOnlyMeassage(Context context, String title, String message) {

        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title).setMessage(message);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();


            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public static void createDialogOnlyMeassageNoOkButton(Context context, String title, String message) {

        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title).setMessage(message);
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    public static void onDissmiss() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

}

