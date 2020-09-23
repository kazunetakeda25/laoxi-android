package com.rider.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Driverlist {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("eta")
    @Expose
    private Integer eta;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @SerializedName("gender")
    @Expose
    private String gender;

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    @SerializedName("car_type")
    @Expose
    private String carType;


    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * @return The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * @return The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude The longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    /**
     * @return The eta
     */
    public Integer getEta() {
        return eta;
    }

    /**
     * @param eta The eta
     */
    public void setEta(Integer eta) {
        this.eta = eta;
    }

    public boolean isFavouriteDriver() {
        return isFavouriteDriver;
    }

    public void setFavouriteDriver(boolean isFavouriteDriver) {
        this.isFavouriteDriver = isFavouriteDriver;
    }

    @SerializedName("is_favourite_driver")
    @Expose
    private boolean isFavouriteDriver;

}