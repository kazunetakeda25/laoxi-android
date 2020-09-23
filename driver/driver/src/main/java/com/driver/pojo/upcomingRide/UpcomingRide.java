package com.driver.pojo.upcomingRide;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Chirag Solanki on 04/10/2016.
 */

public class UpcomingRide {

    @SerializedName("id")
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
    @SerializedName("gender")
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
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("distance_fare")
    @Expose
    private String distanceFare;
    @SerializedName("tip")
    @Expose
    private String tip;
    @SerializedName("round_fare")
    @Expose
    private String roundFare;
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
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("cancellation_reason")
    @Expose
    private String cancellationReason;
    @SerializedName("tripdatetime")
    @Expose
    private String tripdatetime;
    @SerializedName("insertdate")
    @Expose
    private String insertdate;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("user_profile")
    @Expose
    private String userProfile;
    @SerializedName("drivername")
    @Expose
    private String drivername;
    @SerializedName("driver_profile")
    @Expose
    private String driverProfile;
    @SerializedName("driver_earning")
    @Expose
    private String driverEarning;

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
     * The driverId
     */
    public String getDriverId() {
        return driverId;
    }

    /**
     *
     * @param driverId
     * The driver_id
     */
    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    /**
     *
     * @return
     * The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     * The carTypeId
     */
    public String getCarTypeId() {
        return carTypeId;
    }

    /**
     *
     * @param carTypeId
     * The car_type_id
     */
    public void setCarTypeId(String carTypeId) {
        this.carTypeId = carTypeId;
    }

    /**
     *
     * @return
     * The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     *
     * @param gender
     * The gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     *
     * @return
     * The pickupAddress
     */
    public String getPickupAddress() {
        return pickupAddress;
    }

    /**
     *
     * @param pickupAddress
     * The pickup_address
     */
    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    /**
     *
     * @return
     * The deliveryAddress
     */
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     *
     * @param deliveryAddress
     * The delivery_address
     */
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    /**
     *
     * @return
     * The pickupLatitude
     */
    public String getPickupLatitude() {
        return pickupLatitude;
    }

    /**
     *
     * @param pickupLatitude
     * The pickup_latitude
     */
    public void setPickupLatitude(String pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    /**
     *
     * @return
     * The pickupLongitude
     */
    public String getPickupLongitude() {
        return pickupLongitude;
    }

    /**
     *
     * @param pickupLongitude
     * The pickup_longitude
     */
    public void setPickupLongitude(String pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    /**
     *
     * @return
     * The deliveryLatitude
     */
    public String getDeliveryLatitude() {
        return deliveryLatitude;
    }

    /**
     *
     * @param deliveryLatitude
     * The delivery_latitude
     */
    public void setDeliveryLatitude(String deliveryLatitude) {
        this.deliveryLatitude = deliveryLatitude;
    }

    /**
     *
     * @return
     * The deliveryLongitude
     */
    public String getDeliveryLongitude() {
        return deliveryLongitude;
    }

    /**
     *
     * @param deliveryLongitude
     * The delivery_longitude
     */
    public void setDeliveryLongitude(String deliveryLongitude) {
        this.deliveryLongitude = deliveryLongitude;
    }

    /**
     *
     * @return
     * The startDatetime
     */
    public String getStartDatetime() {
        return startDatetime;
    }

    /**
     *
     * @param startDatetime
     * The start_datetime
     */
    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    /**
     *
     * @return
     * The endDatetime
     */
    public String getEndDatetime() {
        return endDatetime;
    }

    /**
     *
     * @param endDatetime
     * The end_datetime
     */
    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }

    /**
     *
     * @return
     * The tripDuration
     */
    public String getTripDuration() {
        return tripDuration;
    }

    /**
     *
     * @param tripDuration
     * The trip_duration
     */
    public void setTripDuration(String tripDuration) {
        this.tripDuration = tripDuration;
    }

    /**
     *
     * @return
     * The tripDurationFare
     */
    public String getTripDurationFare() {
        return tripDurationFare;
    }

    /**
     *
     * @param tripDurationFare
     * The trip_duration_fare
     */
    public void setTripDurationFare(String tripDurationFare) {
        this.tripDurationFare = tripDurationFare;
    }

    /**
     *
     * @return
     * The distance
     */
    public String getDistance() {
        return distance;
    }

    /**
     *
     * @param distance
     * The distance
     */
    public void setDistance(String distance) {
        this.distance = distance;
    }

    /**
     *
     * @return
     * The distanceFare
     */
    public String getDistanceFare() {
        return distanceFare;
    }

    /**
     *
     * @param distanceFare
     * The distance_fare
     */
    public void setDistanceFare(String distanceFare) {
        this.distanceFare = distanceFare;
    }

    /**
     *
     * @return
     * The tip
     */
    public String getTip() {
        return tip;
    }

    /**
     *
     * @param tip
     * The tip
     */
    public void setTip(String tip) {
        this.tip = tip;
    }

    /**
     *
     * @return
     * The roundFare
     */
    public String getRoundFare() {
        return roundFare;
    }

    /**
     *
     * @param roundFare
     * The round_fare
     */
    public void setRoundFare(String roundFare) {
        this.roundFare = roundFare;
    }

    /**
     *
     * @return
     * The totalAmount
     */
    public String getTotalAmount() {
        return totalAmount;
    }

    /**
     *
     * @param totalAmount
     * The total_amount
     */
    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     *
     * @return
     * The driverEarning
     */
    public String getDriverEarning() {
        return driverEarning;
    }

    /**
     *
     * @param driverEarning
     * The driver_earning
     */
    public void setDriverEarning(String driverEarning) {
        this.driverEarning = driverEarning;
    }

    /**
     *
     * @return
     * The paymentStatus
     */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     *
     * @param paymentStatus
     * The payment_status
     */
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     *
     * @return
     * The transactionId
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     *
     * @param transactionId
     * The transaction_id
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     *
     * @return
     * The tripType
     */
    public String getTripType() {
        return tripType;
    }

    /**
     *
     * @param tripType
     * The trip_type
     */
    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The cancellationReason
     */
    public String getCancellationReason() {
        return cancellationReason;
    }

    /**
     *
     * @param cancellationReason
     * The cancellation_reason
     */
    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    /**
     *
     * @return
     * The tripdatetime
     */
    public String getTripdatetime() {
        return tripdatetime;
    }

    /**
     *
     * @param tripdatetime
     * The tripdatetime
     */
    public void setTripdatetime(String tripdatetime) {
        this.tripdatetime = tripdatetime;
    }

    /**
     *
     * @return
     * The insertdate
     */
    public String getInsertdate() {
        return insertdate;
    }

    /**
     *
     * @param insertdate
     * The insertdate
     */
    public void setInsertdate(String insertdate) {
        this.insertdate = insertdate;
    }

    /**
     *
     * @return
     * The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The userProfile
     */
    public String getUserProfile() {
        return userProfile;
    }

    /**
     *
     * @param userProfile
     * The user_profile
     */
    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    /**
     *
     * @return
     * The drivername
     */
    public String getDrivername() {
        return drivername;
    }

    /**
     *
     * @param drivername
     * The drivername
     */
    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    /**
     *
     * @return
     * The driverProfile
     */
    public String getDriverProfile() {
        return driverProfile;
    }

    /**
     *
     * @param driverProfile
     * The driver_profile
     */
    public void setDriverProfile(String driverProfile) {
        this.driverProfile = driverProfile;
    }
}
