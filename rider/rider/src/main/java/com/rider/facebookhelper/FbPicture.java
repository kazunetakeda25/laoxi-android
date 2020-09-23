package com.rider.facebookhelper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Chirag on 09/06/2016.
 */
public class FbPicture {
    @SerializedName("data")
    @Expose
    private FbData data;

    /**
     *
     * @return
     *     The data
     */
    public FbData getData() {
        return data;
    }

    /**
     *
     * @param data
     *     The data
     */
    public void setData(FbData data) {
        this.data = data;
    }

}
