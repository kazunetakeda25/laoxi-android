package com.rider.pojo;

/**
 * Created by hlink54 on 19/7/16.
 */
public class Ride {
    String profile;
    String rideId;
    String date;
    String pickupAddress;
    String dropoffAddress;

    public Ride(String profile, String rideId, String date, String pickupAddress, String dropoffAddress, String detail, String status, String amount) {
        this.profile = profile;
        this.rideId = rideId;
        this.date = date;
        this.pickupAddress = pickupAddress;
        this.dropoffAddress = dropoffAddress;
        this.detail = detail;
        this.status = status;
        this.amount = amount;
    }

    public String getProfile() {

        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
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

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDropoffAddress() {
        return dropoffAddress;
    }

    public void setDropoffAddress(String dropoffAddress) {
        this.dropoffAddress = dropoffAddress;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    String detail;
    String status;
    String amount;
}
