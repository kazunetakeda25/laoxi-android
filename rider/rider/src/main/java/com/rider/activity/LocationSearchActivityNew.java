package com.rider.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.rider.LocationHelper.LocationHelperService;
import com.rider.R;
import com.rider.adapter.GooglePlacesAutocompleteAdapterNew;
import com.rider.dao.CommonImplementation;
import com.rider.dialog.AlertUtils;
import com.rider.navigator.OnTaskComplete;
import com.rider.utils.DataToPref;
import com.rider.utils.LaoxiConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;


/**
 * Created by punit on 3/12/16.
 */


public class LocationSearchActivityNew extends AppCompatActivity {


    EditText edEnterLocation;
    ImageView imgClose, powered_by_google;
    ListView listview;
    TextView textviewSearchResult;
    @Bind(R.id.progressbarSearch)
    ProgressBar progressbarSearch;
    private GooglePlacesAutocompleteAdapterNew dataAdapter;

    List<placeModel> placeModels;


    List<String> placeList;
    List<String> secondList;
    //  private List<FavoritePlaceData> locationDatas = new ArrayList<FavoritePlaceData>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_location_search);
        ButterKnife.bind(this);
        placeModels = new ArrayList<>();
        dataAdapter = new GooglePlacesAutocompleteAdapterNew(getApplicationContext(), placeModels);
        edEnterLocation = (EditText) findViewById(R.id.edEnterLocation);
        imgClose = (ImageView) findViewById(R.id.imgClose);
        powered_by_google = (ImageView) findViewById(R.id.powered_by_google);
        listview = (ListView) findViewById(R.id.listview);
        textviewSearchResult = (TextView) findViewById(R.id.textviewSearchResult);
        listview.setAdapter(dataAdapter);

        edEnterLocation.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                try {
                    listview.setVisibility(View.VISIBLE);
                    placeModels.clear();

                    if (placeModels != null) {
                        dataAdapter.notifyDataSetChanged();
                    }
                    progressbarSearch.setVisibility(View.VISIBLE);
                    new AutocompleteApi(new LatLng(LocationHelperService.dblLatitude, LocationHelperService.dblLongitude), s.toString()).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(final CharSequence s, int start, int before, final int count) {

            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*String str = placeModels.get(position).getMainTitle();
                String Data = getLocationFromAddress(LocationSearchActivityNew.this, str);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", Data + ",,," + str);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();*/

                String str = placeModels.get(position).getMainTitle() + " , " + placeModels.get(position).getSecondaryTitle();

                DataToPref.setSharedPreferanceData(getApplicationContext(), LaoxiConstant.SELECTED_PLACE_ID, LaoxiConstant.SELECTED_PLACE_ID_KEY, placeModels.get(position).getPlaceID() + "");
                String Data = getLocationFromAddress(LocationSearchActivityNew.this, str);

                if (Data == null || Data.equalsIgnoreCase("")) {

                    // call methood
                    GetLocation(str);
                } else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", Data + ",,," + str);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }


            }
        });


        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    /**
     * Get a all details of address which is selected by user.
     */

    public static String getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        String data = "";

        try {
            // address = coder.getFromLocationName(strAddress, 5);
            address = coder.getFromLocationName(strAddress, 15);
            if (address == null || address.size() <= 0) {
                return data;
            }
            Address location = address.get(0);

            System.out.println("============" + location.getLatitude() + "===================" + location.getLongitude() + "====================");
            System.out.println("=====Local=======" + location.getSubLocality());
            System.out.println("=====City=======" + location.getLocality());
            System.out.println("=====State=======" + location.getAdminArea());
            System.out.println("=====PinCode=======" + location.getPostalCode());
            System.out.println("=====Country=======" + location.getCountryName());

            data = location.getLatitude() + ",,," + location.getLongitude() + ",,," + location.getSubLocality() + ",,," + location.getLocality() + ",,," + location.getAdminArea() + ",,," +
                    location.getPostalCode() + ",,," + location.getCountryName();


        } catch (Exception ex) {

            ex.printStackTrace();
            return data;
        }
        return data;

    }


    /**
     * Call a google places api when user enter character into edit text.
     */

    private class AutocompleteApi extends AsyncTask<Void, Void, StringBuilder> {
        private final LatLng latLng;
        private final String s;


        public AutocompleteApi(LatLng latLng, String s) {
            this.latLng = latLng;
            this.s = s;
        }

        @Override
        protected StringBuilder doInBackground(Void... voids) {
            return autocomplete(latLng, s);
        }

        @Override
        protected void onPostExecute(StringBuilder strings) {
            if (strings != null) {
                try {
                    progressbarSearch.setVisibility(View.INVISIBLE);
                    // Create a JSON object hierarchy from the results
                    JSONObject jsonObj = new JSONObject(strings.toString());
                    JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

                    // Extract the Place descriptions from the results
                    ArrayList<String> resultList = new ArrayList<String>(predsJsonArray.length());
                    placeList = new ArrayList<String>(predsJsonArray.length());
                    secondList = new ArrayList<String>(predsJsonArray.length());

                    placeModels.clear();
                    for (int i = 0; i < predsJsonArray.length(); i++) {
                        System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                        System.out.println("============================================================");
                        resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                        placeModel placeModel = new placeModel();
                        placeList.add(predsJsonArray.getJSONObject(i).getJSONObject("structured_formatting").getString("main_text"));
                        placeModel.setMainTitle(predsJsonArray.getJSONObject(i).getJSONObject("structured_formatting").getString("main_text"));
                        placeModel.setPlaceID(predsJsonArray.getJSONObject(i).getString("place_id"));
                        if (predsJsonArray.getJSONObject(i).getJSONObject("structured_formatting").has("secondary_text")) {
                            placeModel.setSecondaryTitle(predsJsonArray.getJSONObject(i).getJSONObject("structured_formatting").getString("secondary_text"));
                            secondList.add(predsJsonArray.getJSONObject(i).getJSONObject("structured_formatting").getString("secondary_text"));
                        } else {
                            placeModel.setSecondaryTitle("");
                            secondList.add("");
                        }
                        placeModels.add(placeModel);
                    }
                } catch (JSONException e) {

                }
                try {
                    if (placeModels != null) {
                        dataAdapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public StringBuilder autocomplete(LatLng latLng, String input) {
            ArrayList<String> resultList = null;
            HttpURLConnection conn = null;
            StringBuilder jsonResults = new StringBuilder();
            try {
                StringBuilder sb = new StringBuilder(LaoxiConstant.PLACES_API_BASE + LaoxiConstant.TYPE_AUTOCOMPLETE + LaoxiConstant.OUT_JSON);
                sb.append("?key=" + getString(R.string.browser_key));
                //sb.append("&components=country:au");
                sb.append("&location=" + String.valueOf(LocationHelperService.dblLatitude) + "," + String.valueOf(LocationHelperService.dblLongitude));
                sb.append("&input=" + URLEncoder.encode(input, "utf8"));
                sb.append("&radius=" + String.valueOf(50));

                URL url = new URL(sb.toString());
                System.out.println("URL: " + url);
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());

                // Load the results into a StringBuilder
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
            } catch (MalformedURLException e) {

                return null;
            } catch (IOException e) {

                return null;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return jsonResults;


        }

    }

    public class placeModel {
        String mainTitle;
        String SecondaryTitle;
        String placeID;

        public String getPlaceID() {
            return placeID;
        }

        public void setPlaceID(String placeID) {
            this.placeID = placeID;
        }

        public String getMainTitle() {
            return mainTitle;
        }

        public void setMainTitle(String mainTitle) {
            this.mainTitle = mainTitle;
        }

        public String getSecondaryTitle() {
            return SecondaryTitle;
        }

        public void setSecondaryTitle(String secondaryTitle) {
            SecondaryTitle = secondaryTitle;
        }
    }


    /**
     * Call a google place detail api .
     */

    private void GetLocation(final String str) {
        AlertUtils.showCustomProgressDialog(LocationSearchActivityNew.this);
        String selectedPlaceKey = DataToPref.getSharedPreferanceData(LocationSearchActivityNew.this, LaoxiConstant.SELECTED_PLACE_ID, LaoxiConstant.SELECTED_PLACE_ID_KEY);

        CommonImplementation.getInstance().doGetLocation(null, "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + selectedPlaceKey + "&sensor=false&key=" + getString(R.string.browser_key) + "&language=" + Locale.getDefault().getCountry(), new OnTaskComplete() {
            @Override
            public void onSuccess(Response data, boolean success) {
                AlertUtils.dismissDialog();
                if (data.code() == 200) {

                    String LAT = "";
                    String Long = "";
                    String Location = "";
                    String City = "";
                    String State = "";
                    String postal_code = "";
                    String country = "";

                    try {

                        String response = data.body().string();
                        JSONObject object = new JSONObject(response);

                        String status = object.getString("status").toString();

                        if (object.has("result")) {
                            //JSONArray results = object.getJSONArray("results");
                            int i = 0;
                            //Log.i("i", i + "," + results.length());
                            JSONObject r = object.getJSONObject("result");
                            JSONArray addressComponentsArray = r.getJSONArray("address_components");
                            do {

                                JSONObject addressComponents = addressComponentsArray.getJSONObject(i);
                                JSONArray typesArray = addressComponents.getJSONArray("types");
                                String types = typesArray.getString(0);

                                if (types.equalsIgnoreCase("sublocality")) {
                                    Location = addressComponents.getString("short_name");
                                    Log.i("Locality", Location);

                                } else if (types.equalsIgnoreCase("locality")) {
                                    City = addressComponents.getString("long_name");
                                    Log.i("City", City);

                                } else if (types.equalsIgnoreCase("administrative_area_level_1")) {
                                    State = addressComponents.getString("long_name");
                                    Log.i("State", State);

                                } else if (types.equalsIgnoreCase("postal_code")) {
                                    postal_code = addressComponents.getString("long_name");
                                    Log.i("postal_code", postal_code);
                                } else if (types.equalsIgnoreCase("country")) {
                                    country = addressComponents.getString("long_name");
                                    Log.i("country", country);
                                }

                                i++;
                            } while (i < addressComponentsArray.length());


                            JSONObject geometry = r.getJSONObject("geometry");
                            JSONObject location = geometry.getJSONObject("location");

                            LAT = location.getString("lat");
                            Long = location.getString("lng");


                                  /*  Log.i("JSON Geo Locatoin =>", currentLocation);
                                    return currentLocation;*/

                            String Data = LAT + ",,," + Long + ",,," + Location + ",,," + City + ",,," + State + ",,," +
                                    postal_code + ",,," + country;


                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("result", Data + ",,," + str);
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (data.code() == 404) {

                }
            }

            @Override
            public void onFailure() {
                AlertUtils.dismissDialog();
            }
        });
    }
}// end of main class
