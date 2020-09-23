package com.driver.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

import com.driver.R;
import com.driver.customclasses.CustomTextView;

import java.util.ArrayList;

/**
 * Created by chirag on 16/4/16.
 */
public class CustomSpinnerRegistration<T> implements SpinnerAdapter {
    Context context;
    ArrayList<T> arrayList;
    String title;
    boolean colorChange = true, isAstrix = false;

    public CustomSpinnerRegistration(Context context, ArrayList<T> arrayList, String title) {
        this.context = context;
        this.arrayList = arrayList;
        this.title = title;
    }

   /* public CustomSpinnerRegistration(Context context, ArrayList<T> arrayList, boolean colorChange) {
        this.context = context;
        this.arrayList = arrayList;
        this.colorChange = colorChange;
    }

    public CustomSpinnerRegistration(Context context, ArrayList<T> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public CustomSpinnerRegistration(boolean isAstrix, Context context, ArrayList<T> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.isAstrix = isAstrix;
    }*/


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomViewDropDown(position, convertView, parent);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public T getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public View getCustomViewDropDown(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.raw_spinner_item, parent, false);
        CustomTextView label = (CustomTextView) row.findViewById(R.id.txtCountryItem);
        label.setText(arrayList.get(position).toString());
       /* if (position == 0 ) {
            label.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else  {
            label.setTextColor(context.getResources().getColor(R.color.check_box_color));
        }*/
        return row;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.raw_spinner_header, parent, false);
        CustomTextView label = (CustomTextView) view.findViewById(R.id.txtCountryCode);
        label.setText(arrayList.get(position).toString());
        /*if (position == 0 && isAstrix) {
            label.setText(Html.fromHtml(arrayList.get(position).toString() + "<font color='" + context.getResources().getColor(R.color.red) + "'><sup>*</sup></font>"));
        }
        if (position > 0 && colorChange) {
            label.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else if (!colorChange) {
            label.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }*/

        return view;
    }

}