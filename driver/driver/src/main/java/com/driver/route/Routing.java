package com.driver.route;

/**
 * Async Task to access the Google Direction API and return the routing data
 * which is then parsed and converting to a route overlay using some classes created by Hesham Saeed.
 * @author Joel Dean
 * Requires an instance of the map activity and the application's current context for the progress dialog.
 *
 */

import android.content.res.Resources;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.driver.BuildConfig;
import com.driver.R;
import com.driver.util.ConstantClass;

import java.util.ArrayList;
import java.util.List;


public class Routing extends AsyncTask<LatLng, Void, Route> {
    protected ArrayList<RoutingListener> _aListeners;
    protected TravelMode _mTravelMode;

    public List<LatLng> getLatlonlist() {
        return latlonlist;
    }

    List<LatLng> latlonlist=new ArrayList<>();

    public enum TravelMode {
        BIKING("biking"),
        DRIVING("driving"),
        WALKING("walking"),
        TRANSIT("transit");

        protected String _sValue;

        private TravelMode(String sValue) {
            this._sValue = sValue;
        }

        protected String getValue() {
            return _sValue;
        }
    }


    public Routing(TravelMode mTravelMode) {
        this._aListeners = new ArrayList<RoutingListener>();
        this._mTravelMode = mTravelMode;
    }

    public void registerListener(RoutingListener mListener) {
        _aListeners.add(mListener);
    }

    protected void dispatchOnStart() {
        for (RoutingListener mListener : _aListeners) {
            mListener.onRoutingStart();
        }
    }

    protected void dispatchOnFailure() {
        for (RoutingListener mListener : _aListeners) {
            mListener.onRoutingFailure();
        }
    }

    protected void dispatchOnSuccess(PolylineOptions mOptions, Route route) {
        for (RoutingListener mListener : _aListeners) {
            mListener.onRoutingSuccess(mOptions, route);
        }
    }

    /**
     * Performs the call to the google maps API to acquire routing data and
     * deserializes it to a format the map can display.
     *
     * @param aPoints
     * @return
     */
    @Override
    protected Route doInBackground(LatLng... aPoints) {
        for (LatLng mPoint : aPoints) {
            if (mPoint == null) return null;
        }

        return new GoogleParser(constructURL(aPoints)).parse();
    }

    protected String constructURL(LatLng... points) {
        LatLng start = points[0];
        LatLng dest = points[1];
        String sJsonURL = "https://maps.googleapis.com/maps/api/directions/json?";
        String mapPremiumKey;
        if (BuildConfig.BUILD_TYPE == "release") {
            mapPremiumKey = ConstantClass.GOOGLE_MAP_API_KEY;
        } else {
            mapPremiumKey = ConstantClass.GOOGLE_MAP_TEST_API_KEY;
        }
        final StringBuffer mBuf = new StringBuffer(sJsonURL);
        mBuf.append("origin=");
        mBuf.append(start.latitude);
        mBuf.append(',');
        mBuf.append(start.longitude);
        mBuf.append("&destination=");
        mBuf.append(dest.latitude);
        mBuf.append(',');
        mBuf.append(dest.longitude);
        mBuf.append("&sensor=true&mode=");
        mBuf.append(_mTravelMode.getValue());
        mBuf.append("&key=");
        mBuf.append(mapPremiumKey);


        return mBuf.toString();
    }

    @Override
    protected void onPreExecute() {
        dispatchOnStart();
    }

    @Override
    protected void onPostExecute(Route result) {      
        if (result == null) {
            dispatchOnFailure();
        } else {
            PolylineOptions mOptions = new PolylineOptions();

            for (LatLng point : result.getPoints()) {
                latlonlist.add(point);

                mOptions.add(point);
            }

            dispatchOnSuccess(mOptions, result);
        }
    }//end onPostExecute method
}
