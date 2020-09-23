package com.rider.pojo;

/**
 * Created by hlink43 on 28/3/16.
 */
public class PlaceOrderData {

    String pickupAddress;
    String dropoffAddress;
    String pickupLatitude;
    String pickupLongitude;
    String dropoffLatitude;
    String dropoffLongitude;
    String promocode;
    String carType;
    String rideType;
    String date;
    String time;

    public String getCar_type_id() {
        return car_type_id;
    }

    public void setCar_type_id(String car_type_id) {
        this.car_type_id = car_type_id;
    }

    String car_type_id;

    public int getPositions() {
        return positions;
    }

    public void setPositions(int positions) {
        this.positions = positions;
    }

    int positions;

    public String getIsPackage() {
        return isPackage;
    }

    public void setIsPackage(String isPackage) {
        this.isPackage = isPackage;
    }

    String isPackage;
    public String getPackageRate() {
        return packageRate;
    }

    public void setPackageRate(String packageRate) {
        this.packageRate = packageRate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRideType() {
        return rideType;
    }

    public void setRideType(String rideType) {
        this.rideType = rideType;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }

    public String getDropoffLongitude() {
        return dropoffLongitude;
    }

    public void setDropoffLongitude(String dropoffLongitude) {
        this.dropoffLongitude = dropoffLongitude;
    }

    public String getDropoffLatitude() {
        return dropoffLatitude;
    }

    public void setDropoffLatitude(String dropoffLatitude) {
        this.dropoffLatitude = dropoffLatitude;
    }

    public String getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(String pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public String getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(String pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public String getDropoffAddress() {
        return dropoffAddress;
    }

    public void setDropoffAddress(String dropoffAddress) {
        this.dropoffAddress = dropoffAddress;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    String packageRate;
}
