package com.rider.pojo;

/**
 * Created by hlink43 on 20/8/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GenericResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;

    /**
     *
     * @return
     * The status
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(Boolean status) {
        this.status = status;
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