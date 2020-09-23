package com.rider.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.rider.pojo.CardInfo;
import com.rider.pojo.CartypeList;
import com.rider.pojo.GenericResponse;

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

    public CardInfo cardInfoParsing(String s) {
        try {
            Gson mGson = new Gson();
            CardInfo json = mGson.fromJson(s, CardInfo.class);
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

    public List<CardInfo> cardListinfoParsing(String data) {


        try {

            Type token = new TypeToken<List<CardInfo>>() {

            }.getType();

            Gson mGson = new Gson();

            List<CardInfo> json = mGson.fromJson(data, token);

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

    public CartypeList carTypeListParsing(String s) {
        try {
            Gson mGson = new Gson();
            CartypeList json = mGson.fromJson(s, CartypeList.class);
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

}

