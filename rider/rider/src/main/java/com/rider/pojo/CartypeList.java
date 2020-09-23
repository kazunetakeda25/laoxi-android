package com.rider.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CartypeList {

@SerializedName("id")
@Expose
private String id;
@SerializedName("city_id")
@Expose
private String cityId;
@SerializedName("car_type")
@Expose
private String carType;
@SerializedName("base_fare")
@Expose
private String baseFare;
@SerializedName("per_minute")
@Expose
private String perMinute;
@SerializedName("per_km")
@Expose
private String perKm;
@SerializedName("min_fare")
@Expose
private String minFare;
@SerializedName("cancellation")
@Expose
private String cancellation;
@SerializedName("is_active")
@Expose
private String isActive;
@SerializedName("insertdate")
@Expose
private String insertdate;
@SerializedName("city")
@Expose
private String city;

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
* The cityId
*/
public String getCityId() {
return cityId;
}

/**
*
* @param cityId
* The city_id
*/
public void setCityId(String cityId) {
this.cityId = cityId;
}

/**
*
* @return
* The carType
*/
public String getCarType() {
return carType;
}

/**
*
* @param carType
* The car_type
*/
public void setCarType(String carType) {
this.carType = carType;
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
* The perMinute
*/
public String getPerMinute() {
return perMinute;
}

/**
*
* @param perMinute
* The per_minute
*/
public void setPerMinute(String perMinute) {
this.perMinute = perMinute;
}

/**
*
* @return
* The perKm
*/
public String getPerKm() {
return perKm;
}

/**
*
* @param perKm
* The per_km
*/
public void setPerKm(String perKm) {
this.perKm = perKm;
}

/**
*
* @return
* The minFare
*/
public String getMinFare() {
return minFare;
}

/**
*
* @param minFare
* The min_fare
*/
public void setMinFare(String minFare) {
this.minFare = minFare;
}

/**
*
* @return
* The cancellation
*/
public String getCancellation() {
return cancellation;
}

/**
*
* @param cancellation
* The cancellation
*/
public void setCancellation(String cancellation) {
this.cancellation = cancellation;
}

/**
*
* @return
* The isActive
*/
public String getIsActive() {
return isActive;
}

/**
*
* @param isActive
* The is_active
*/
public void setIsActive(String isActive) {
this.isActive = isActive;
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
* The city
*/
public String getCity() {
return city;
}

/**
*
* @param city
* The city
*/
public void setCity(String city) {
this.city = city;
}

}