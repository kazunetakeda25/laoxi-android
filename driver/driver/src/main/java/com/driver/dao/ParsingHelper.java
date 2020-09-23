package com.driver.dao;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.driver.pojo.GenericResponse;
import com.driver.pojo.TrackRidePojo.TrackRide;
import com.driver.pojo.TrackRidePojo.TrackRidePojo;
import com.driver.pojo.acceptorder.AcceptOrderResponse;
import com.driver.pojo.dropoffWs.DropOffResponse;
import com.driver.pojo.loginws.LoginResponse;
import com.driver.pojo.upcomingRide.UpcomingRide;
import com.driver.util.DebugLog;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Chirag Solanki on 06/08/2016.
 */
public class ParsingHelper {
    private static ParsingHelper ourInstance = new ParsingHelper();

    public static ParsingHelper getInstance() {
        return ourInstance;
    }

    private ParsingHelper() {
    }

    public GenericResponse genericResponseParsing(String s) {
        try {
            Gson mGson = new Gson();
            GenericResponse json = mGson.fromJson(s, GenericResponse.class);
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

    public LoginResponse loginResponseParsing(String responce) {
        try {
            Gson mGson = new Gson();
            LoginResponse json = mGson.fromJson(responce, LoginResponse.class);
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

    public AcceptOrderResponse acceptOrderResponse(String responce) {
        try {
            Gson mGson = new Gson();
            AcceptOrderResponse json = mGson.fromJson(responce, AcceptOrderResponse.class);
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

  /*  public TrackRidePojo trackRideResponse(String responce) {
        try {
            Gson mGson = new Gson();
            TrackRidePojo json = mGson.fromJson(responce, TrackRidePojo.class);
            return json;
        } catch (JsonSyntaxException e) {
            // TODO Auto-generated catch block
            DebugLog.e("JsonSyntaxException :" + e);
            return null;

        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }


    }*/
    public List<UpcomingRide> upcomingRideList(String data) {
        try {
            Type token = new TypeToken<List<UpcomingRide>>() {
            }.getType();
            Gson mGson = new Gson();
            List<UpcomingRide> json = mGson.fromJson(data, token);
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

    public DropOffResponse dropoffResponse(String responce) {
        try {
            Gson mGson = new Gson();
            DropOffResponse json = mGson.fromJson(responce, DropOffResponse.class);
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


    public TrackRide trackRideData(String responce) {
        try {
            Gson mGson = new Gson();
            TrackRide json = mGson.fromJson(responce, TrackRide.class);
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


    /**
     * Format
     *
     */
    /*if (success) {
        try {


            if (data.code() == ConstantClass.RESPONSE_CODE_200) {
                String response = data.body().string();

            } else if (data.code() == ConstantClass.RESPONSE_CODE_404) {
                String s = data.body().string();
                GenericResponse response = ParsingHelper.getInstance().genericResponseParsing(s);
                showSnackbar(response.getMessage());
            } else {
                String s = data.body().string();
                GenericResponse response = ParsingHelper.getInstance().genericResponseParsing(s);
                showSnackbar(response.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    } else {
        showSnackbar(getString(R.string.bad_server));
    }*/


}

