package com.rider.pojo;

/**
 * Created by hitesh on 22/7/16.
 */
public class CurrentRide {

    String userId;
    String rideId;
    String date;
    String time;
    String pickupAddress;
    String dropOffAddress;
    String tripType;
    String carTypeId;
    String Region;
    String carType;

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    String gender;

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(String carTypeId) {
        this.carTypeId = carTypeId;
    }

    double pickLatitude;
    double pickLongitude;

    double dropOffLatitude;
    double dropOffLongitude;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDropOffAddress() {
        return dropOffAddress;
    }

    public void setDropOffAddress(String dropOffAddress) {
        this.dropOffAddress = dropOffAddress;
    }

    public double getPickLatitude() {
        return pickLatitude;
    }

    public void setPickLatitude(double pickLatitude) {
        this.pickLatitude = pickLatitude;
    }

    public double getPickLongitude() {
        return pickLongitude;
    }

    public void setPickLongitude(double pickLongitude) {
        this.pickLongitude = pickLongitude;
    }

    public double getDropOffLatitude() {
        return dropOffLatitude;
    }

    public void setDropOffLatitude(double dropOffLatitude) {
        this.dropOffLatitude = dropOffLatitude;
    }

    public double getDropOffLongitude() {
        return dropOffLongitude;
    }

    public void setDropOffLongitude(double dropOffLongitude) {
        this.dropOffLongitude = dropOffLongitude;
    }
}
