package com.rider.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.rider.R;
import com.rider.activity.LocationSearchActivityNew;

import java.util.List;

/**
 * Created by prakash on 3/12/16.
 */

public class GooglePlacesAutocompleteAdapterNew extends BaseAdapter {


    private final Context applicationContext;
    private final List<LocationSearchActivityNew.placeModel> placeModels;

    public GooglePlacesAutocompleteAdapterNew(Context applicationContext, List<LocationSearchActivityNew.placeModel> placeModels) {
        this.applicationContext = applicationContext;
        this.placeModels = placeModels;
    }

    @Override
    public int getCount() {
        return placeModels.size();
    }

    @Override
    public LocationSearchActivityNew.placeModel getItem(int i) {
        return placeModels.get(i);
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        PlaceViewHolder viewholder;


        try {
            if (view==null){
                view = LayoutInflater.from(applicationContext).inflate(R.layout.adapter_google_places_autocomplete, parent, false);
                viewholder=new PlaceViewHolder(view);
                view.setTag(viewholder);
            }
            viewholder= (PlaceViewHolder) view.getTag();
        /*viewholder.txtArea.setText(placeModels.get(position).getSecondaryTitle());
        viewholder.txtTitle.setText(placeModels.get(position).getMainTitle());*/

            try {
                if (placeModels.size() > position) {
                    viewholder.txtArea.setText(placeModels.get(position).getSecondaryTitle());
                    viewholder.txtTitle.setText(placeModels.get(position).getMainTitle());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        viewholder.txtArea.setText(placeModels.get(position).getSecondaryTitle());
        return view;
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    private class PlaceViewHolder {
        TextView txtTitle;
        TextView txtArea;
        public PlaceViewHolder(View view) {
            txtArea = (TextView) view.findViewById(R.id.txtArea);
            txtTitle = (TextView) view.findViewById(R.id.txtAddress);
        }
    }
}
