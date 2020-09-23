package com.rider.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Settings {

@SerializedName("base_distance")
@Expose
private String baseDistance;
@SerializedName("settlement_day")
@Expose
private String settlementDay;
@SerializedName("android_version")
@Expose
private String androidVersion;
@SerializedName("ios_version")
@Expose
private String iosVersion;
@SerializedName("driver_android_version")
@Expose
private String driverAndroidVersion;
@SerializedName("driver_ios_version")
@Expose
private String driverIosVersion;

public String getBaseDistance() {
return baseDistance;
}

public void setBaseDistance(String baseDistance) {
this.baseDistance = baseDistance;
}

public String getSettlementDay() {
return settlementDay;
}

public void setSettlementDay(String settlementDay) {
this.settlementDay = settlementDay;
}

public String getAndroidVersion() {
return androidVersion;
}

public void setAndroidVersion(String androidVersion) {
this.androidVersion = androidVersion;
}

public String getIosVersion() {
return iosVersion;
}

public void setIosVersion(String iosVersion) {
this.iosVersion = iosVersion;
}

public String getDriverAndroidVersion() {
return driverAndroidVersion;
}

public void setDriverAndroidVersion(String driverAndroidVersion) {
this.driverAndroidVersion = driverAndroidVersion;
}

public String getDriverIosVersion() {
return driverIosVersion;
}

public void setDriverIosVersion(String driverIosVersion) {
this.driverIosVersion = driverIosVersion;
}

}