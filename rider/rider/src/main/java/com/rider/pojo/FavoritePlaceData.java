package com.rider.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hlink on 23/6/16.
 */
public class FavoritePlaceData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("passenger_id")
    @Expose
    private String passengerId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("insertdata")
    @Expose
    private String insertdata;

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The passengerId
     */
    public String getPassengerId() {
        return passengerId;
    }

    /**
     *
     * @param passengerId
     * The passenger_id
     */
    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    /**
     *
     * @return
     * The address
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     * The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return
     * The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     * The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     * The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     * The longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     *
     * @return
     * The insertdata
     */
    public String getInsertdata() {
        return insertdata;
    }

    /**
     *
     * @param insertdata
     * The insertdata
     */
    public void setInsertdata(String insertdata) {
        this.insertdata = insertdata;
    }


}
