
package com.driver.pojo.TrackRidePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrackRidePojo {

    @Expose
    private Boolean status;
    @SerializedName("track_trip")
    @Expose
    private TrackRide trackTrip;
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public TrackRide getTrackTrip() {
        return trackTrip;
    }

    public void setTrackTrip(TrackRide trackTrip) {
        this.trackTrip = trackTrip;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
