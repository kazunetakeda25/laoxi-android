package com.rider.pojo;

/**
 * Created by hlink43 on 25/2/16.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class NotificationHandler {

    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("trip_type")
    @Expose
    private String tripType;
    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("order_id")
    @Expose
    private String orderId;

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
     * The flag
     */
    public String getFlag() {
        return flag;
    }

    /**
     *
     * @param flag
     * The flag
     */
    public void setFlag(String flag) {
        this.flag = flag;
    }

    /**
     *
     * @return
     * The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return
     * The orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     *
     * @param orderId
     * The order_id
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

}
