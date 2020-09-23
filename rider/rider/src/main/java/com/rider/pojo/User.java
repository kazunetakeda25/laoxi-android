package com.rider.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class User {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("social_id")
    @Expose
    private String social_id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("login_type")
    @Expose
    private String loginType;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("is_login")
    @Expose
    private String isLogin;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("last_login")
    @Expose
    private String lastLogin;
    @SerializedName("insertdate")
    @Expose
    private String insertdate;
    @SerializedName("card_info")
    @Expose
    private List<CardInfo> cardInfo = new ArrayList<CardInfo>();
    @SerializedName("message")
    @Expose
    private String message;

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
     * The fbId
     */
    public String getFbId() {
        return social_id;
    }

    /**
     *
     * @param fbId
     * The fb_id
     */
    public void setFbId(String fbId) {
        this.social_id = fbId;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     * The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     * The countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     *
     * @param countryCode
     * The country_code
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     *
     * @return
     * The phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     *
     * @param phone
     * The phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
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
     * The profileImage
     */
    public String getProfileImage() {
        return profileImage;
    }

    /**
     *
     * @param profileImage
     * The profile_image
     */
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    /**
     *
     * @return
     * The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     * The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     * The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     * The longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     *
     * @return
     * The deviceId
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     *
     * @param deviceId
     * The device_id
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     *
     * @return
     * The deviceType
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     *
     * @param deviceType
     * The device_type
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     *
     * @return
     * The loginType
     */
    public String getLoginType() {
        return loginType;
    }

    /**
     *
     * @param loginType
     * The login_type
     */
    public void setLoginType(String loginType) {
        this.loginType = loginType;
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
     * The isLogin
     */
    public String getIsLogin() {
        return isLogin;
    }

    /**
     *
     * @param isLogin
     * The is_login
     */
    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
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
     * The lastLogin
     */
    public String getLastLogin() {
        return lastLogin;
    }

    /**
     *
     * @param lastLogin
     * The last_login
     */
    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
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
     * The cardInfo
     */
    public List<CardInfo> getCardInfo() {
        return cardInfo;
    }

    /**
     *
     * @param cardInfo
     * The card_info
     */
    public void setCardInfo(List<CardInfo> cardInfo) {
        this.cardInfo = cardInfo;
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
}