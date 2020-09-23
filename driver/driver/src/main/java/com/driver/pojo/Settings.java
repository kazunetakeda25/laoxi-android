package com.driver.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahuateng on 2/13/18.
 */

public class Settings {

    @SerializedName("base_distance")
    @Expose
    private String base_distance;
    @SerializedName("settlement_day")
    @Expose
    private String settlement_day;
    @SerializedName("android_version")
    @Expose
    private String android_version;
    @SerializedName("ios_version")
    @Expose
    private String ios_version;
    @SerializedName("driver_android_version")
    @Expose
    private String driver_android_version;
    @SerializedName("driver_ios_version")
    @Expose
    private String driver_ios_version;

    public String getBase_distance() {
        return base_distance;
    }

    public void setBase_distance(String base_distance) {
        this.base_distance = base_distance;
    }

    public String getSettlement_day() {
        return settlement_day;
    }

    public void setSettlement_day(String settlement_day) {
        this.settlement_day = settlement_day;
    }

    public String getAndroid_version() {
        return android_version;
    }

    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }

    public String getIos_version() {
        return ios_version;
    }

    public void setIos_version(String ios_version) {
        this.ios_version = ios_version;
    }

    public String getDriver_android_version() {
        return driver_android_version;
    }

    public void setDriver_android_version(String driver_android_version) {
        this.driver_android_version = driver_android_version;
    }

    public String getDriver_ios_version() {
        return driver_ios_version;
    }

    public void setDriver_ios_version(String driver_ios_version) {
        this.driver_ios_version = driver_ios_version;
    }
}
