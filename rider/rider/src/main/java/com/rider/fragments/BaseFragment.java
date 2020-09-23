package com.rider.fragments;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.rider.pojo.RidesDataList;
import com.rider.utils.DebugLog;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by hlink54 on 20/7/16.
 */
public class BaseFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public boolean isEditTextEmpty(EditText editText) {
        if (editText.getText().toString().trim().isEmpty())
            return true;
        return false;
    }

    public boolean isEditTextEmpty(TextView editText) {
        if (editText.getText().toString().trim().isEmpty())
            return true;
        return false;
    }

    public void showSnackBar(String message, final EditText editText, View view) {

        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editText != null) {
                            editText.requestFocus();
                            editText.setSelection(editText.getText().length());
                        }

                    }
                });
        snackbar.show();
    }

    public void showSnackBar(String message, final TextView editText, View view) {

        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editText != null) {
                            editText.requestFocus();
                        }

                    }
                });
        snackbar.show();
    }

    public boolean isValidEmail(EditText edtText) {
        if (edtText.getText().toString().trim() == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(edtText.getText().toString().trim()).matches();
        }
    }

    public List<RidesDataList> userPlaylistParsing(String data) {


        try {

            Type token = new TypeToken<List<RidesDataList>>() { }.getType();

            Gson mGson = new Gson();

            List<RidesDataList> json = mGson.fromJson(data, token);

            return json;

        } catch (JsonSyntaxException e) {

            // TODO Auto-generated catch block

            DebugLog.e("JsonSyntaxException :" + e);

            return null;


        } catch (Exception e) {

            // TODO: handle exception

            return null;

        }

    }

    public String localToUTC24(String dateString){

        DebugLog.e("string is==="+dateString);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd / MM / yyyy HH:mm", Locale.ENGLISH);
        TimeZone utcZone = TimeZone.getDefault();
        simpleDateFormat.setTimeZone(utcZone);

        Date date = null;
        String dateAsString = "";
        String dateS = null;
        try {
            date = simpleDateFormat.parse(dateString);
            SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.ENGLISH);
            simple.setTimeZone(TimeZone.getTimeZone("GMT"));
            dateS = simple.format(date);
//            DebugLog.e("time" + String.valueOf(date) + "" + simpleDateFormat.format(date) + " " + utcZone);
            DebugLog.e("new " + dateS + "" + " " + simple.getTimeZone());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateS;
    }

    public static String localToUTC(String utcDate) {
        try {

            DebugLog.e("local is a"+utcDate);

            String fromPattern="dd / MM / yyyy hh:mm";
            String toPattern="yyyy-MM-dd hh:mm";
            SimpleDateFormat df = new SimpleDateFormat(fromPattern);
            df.setTimeZone(TimeZone.getDefault());
            Date date = df.parse(utcDate);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            df.applyPattern(toPattern);
            return df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }


    public String StringToUtf(String s) {
        String add = "";
        if (s != null) {

            try {
                add = URLEncoder.encode(s, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        return add;

    }

}
