package com.rider.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class VehicleType {

    @SerializedName("service_type")
    @Expose
    private List<Vehicle> serviceType = new ArrayList<Vehicle>();

    public List<Vehicle> getServicetype() {
        return serviceType;
    }

    public void setServiceType(List<Vehicle> serviceType) {
        this.serviceType = serviceType;
    }
}
