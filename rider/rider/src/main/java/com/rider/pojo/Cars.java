package com.rider.pojo;

import com.google.gson.annotations.Expose;

/**
 * Created by hitesh on 20/7/16.
 */
public class Cars {

    @Expose
    private int carId;

    @Expose
    private int distance;

    @Expose
    private String eta;

    @Expose
    private double latitude;

    @Expose
    private double longitude;


    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
