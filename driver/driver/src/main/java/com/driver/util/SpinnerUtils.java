package com.driver.util;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.driver.R;

import java.util.List;

public class SpinnerUtils {

    ItemEventListener callback;
    List<?> listArray;

    public void setButtonToSpinnerWithoutRadioBtn(final Activity activity, final TextView buttonSpinner, List<String> arrString, ItemEventListener callback) {
        // TODO Auto-generated method stub

        // initialize a pop up window type
        this.callback = callback;
        this.listArray = arrString;
        final PopupWindow popupWindow = new PopupWindow(activity);

        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(buttonSpinner.getWidth());
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        //popupWindow.showAtLocation(buttonSpinner, Gravity.NO_GRAVITY, 0, 5);

        // the drop down list is a list view
        //ListView listViewPopUp = new ListView(activity);

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.spinner_layout_main, null);
        ListView listViewPopUp = (ListView) view.findViewById(R.id.list_view_spinner_layout_main);

		/*spinnerArrayAdapter
				.setDropDownViewResource(R.layout.spinner_layout_item);*/

        listViewPopUp.setAdapter(new ArrayAdapter(activity, R.layout.raw_spinner, R.id.raw_spinner_textView, arrString));
        buttonSpinner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
               // Debug.e("BUTTON SPINNSE IS CALLLED ::::::::::::");
                popupWindow.showAsDropDown(buttonSpinner, 0, 0);
            }
        });

        // set the item click listener
        listViewPopUp.setOnItemClickListener(
                new DropdownOnItemClickListener(activity, buttonSpinner, popupWindow));


        // set the list view as pop up window content
        popupWindow.setContentView(view);
    }


    public class DropdownOnItemClickListener implements OnItemClickListener {

        TextView buttonSpinner;
        PopupWindow popupWindow;
        Activity activity;

        public DropdownOnItemClickListener() {
            // TODO Auto-generated constructor stub
        }

        public DropdownOnItemClickListener(Activity activity, TextView buttonSpinner, PopupWindow popupWindow) {
            // TODO Auto-generated constructor stub
            this.buttonSpinner = buttonSpinner;
            this.popupWindow = popupWindow;
            this.activity = activity;

        }

        @Override
        public void onItemClick(AdapterView<?> arg0, final View v, int arg2, long arg3) {

            // get the context and main activity to access variables

            // add some animation when a list item was clicked
            Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
            fadeInAnimation.setDuration(10);
            v.startAnimation(fadeInAnimation);

            if (callback != null) {
                callback.onItemEventFired(arg2 + "", listArray.get(arg2));
            }
            popupWindow.dismiss();
        }
    }


    public int positionList(int pos) {
        return pos;
    }
}
