package com.rider.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CardInfo {

@SerializedName("customer_id")
@Expose
private String customerId;
@SerializedName("token")
@Expose
private String token;
@SerializedName("display_number")
@Expose
private String displayNumber;
@SerializedName("imageUrl")
@Expose
private String imageUrl;

/**
* 
* @return
* The customerId
*/
public String getCustomerId() {
return customerId;
}

/**
* 
* @param customerId
* The customer_id
*/
public void setCustomerId(String customerId) {
this.customerId = customerId;
}

/**
* 
* @return
* The token
*/
public String getToken() {
return token;
}

/**
* 
* @param token
* The token
*/
public void setToken(String token) {
this.token = token;
}

/**
* 
* @return
* The displayNumber
*/
public String getDisplayNumber() {
return displayNumber;
}

/**
* 
* @param displayNumber
* The display_number
*/
public void setDisplayNumber(String displayNumber) {
this.displayNumber = displayNumber;
}

/**
* 
* @return
* The imageUrl
*/
public String getImageUrl() {
return imageUrl;
}

/**
* 
* @param imageUrl
* The imageUrl
*/
public void setImageUrl(String imageUrl) {
this.imageUrl = imageUrl;
}

}