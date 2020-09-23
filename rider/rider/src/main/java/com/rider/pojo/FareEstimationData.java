package com.rider.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class FareEstimationData {

@SerializedName("distance")
@Expose
private String distance;
@SerializedName("time")
@Expose
private String time;
@SerializedName("base_fare")
@Expose
private String baseFare;
@SerializedName("trip_duration_fare")
@Expose
private String tripDurationFare;
@SerializedName("distance_fare")
@Expose
private String distanceFare;
@SerializedName("total_amount")
@Expose
private String totalAmount;
@SerializedName("max_total_amount")
@Expose
private Double maxTotalAmount;
@SerializedName("min_total_amount")
@Expose
private Double minTotalAmount;

    public Double getMinTotalAmount() {
        return minTotalAmount;
    }

    public void setMinTotalAmount(Double minTotalAmount) {
        this.minTotalAmount = minTotalAmount;
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
* The time
*/
public String getTime() {
return time;
}

/**
*
* @param time
* The time
*/
public void setTime(String time) {
this.time = time;
}

/**
*
* @return
* The baseFare
*/
public String getBaseFare() {
return baseFare;
}

/**
*
* @param baseFare
* The base_fare
*/
public void setBaseFare(String baseFare) {
this.baseFare = baseFare;
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
* The maxTotalAmount
*/
public Double getMaxTotalAmount() {
return maxTotalAmount;
}

/**
*
* @param maxTotalAmount
* The max_total_amount
*/
public void setMaxTotalAmount(Double maxTotalAmount) {
this.maxTotalAmount = maxTotalAmount;
}

}