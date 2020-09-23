
package com.driver.pojo.TrackRidePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.driver.pojo.Settings;

public class TrackRide {

    @Expose
    private String id;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("car_type_id")
    @Expose
    private String carTypeId;
    @Expose
    private String gender;
    @SerializedName("pickup_address")
    @Expose
    private String pickupAddress;
    @SerializedName("delivery_address")
    @Expose
    private String deliveryAddress;
    @SerializedName("pickup_latitude")
    @Expose
    private String pickupLatitude;
    @SerializedName("pickup_longitude")
    @Expose
    private String pickupLongitude;
    @SerializedName("delivery_latitude")
    @Expose
    private String deliveryLatitude;
    @SerializedName("delivery_longitude")
    @Expose
    private String deliveryLongitude;
    @SerializedName("start_datetime")
    @Expose
    private String startDatetime;
    @SerializedName("end_datetime")
    @Expose
    private String endDatetime;
    @SerializedName("trip_duration")
    @Expose
    private String tripDuration;
    @SerializedName("trip_duration_fare")
    @Expose
    private String tripDurationFare;
    @Expose
    private String distance;
    @SerializedName("distance_fare")
    @Expose
    private String distanceFare;
    @Expose
    private String tip;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("trip_type")
    @Expose
    private String tripType;
    @Expose
    private String status;
    @SerializedName("cancellation_reason")
    @Expose
    private String cancellationReason;

    @SerializedName("car_type")
    @Expose
    private String carType;

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    @Expose
    private String tripdatetime;
    @Expose
    private String insertdate;
    @SerializedName("user_data")
    @Expose
    private TrackRideUserData userData;
    @SerializedName("driver_data")
    @Expose
    private DriverData driverData;
    @SerializedName("settings")
    @Expose
    private Settings settings;

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(String carTypeId) {
        this.carTypeId = carTypeId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(String pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public String getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(String pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public String getDeliveryLatitude() {
        return deliveryLatitude;
    }

    public void setDeliveryLatitude(String deliveryLatitude) {
        this.deliveryLatitude = deliveryLatitude;
    }

    public String getDeliveryLongitude() {
        return deliveryLongitude;
    }

    public void setDeliveryLongitude(String deliveryLongitude) {
        this.deliveryLongitude = deliveryLongitude;
    }

    public String getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public String getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }

    public String getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(String tripDuration) {
        this.tripDuration = tripDuration;
    }

    public String getTripDurationFare() {
        return tripDurationFare;
    }

    public void setTripDurationFare(String tripDurationFare) {
        this.tripDurationFare = tripDurationFare;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistanceFare() {
        return distanceFare;
    }

    public void setDistanceFare(String distanceFare) {
        this.distanceFare = distanceFare;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getTripdatetime() {
        return tripdatetime;
    }

    public void setTripdatetime(String tripdatetime) {
        this.tripdatetime = tripdatetime;
    }

    public String getInsertdate() {
        return insertdate;
    }

    public void setInsertdate(String insertdate) {
        this.insertdate = insertdate;
    }

    public TrackRideUserData getUserData() {
        return userData;
    }

    public void setUserData(TrackRideUserData userData) {
        this.userData = userData;
    }

    public DriverData getDriverData() {
        return driverData;
    }

    public void setDriverData(DriverData driverData) {
        this.driverData = driverData;
    }
}
