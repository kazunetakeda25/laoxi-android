package com.rider.facebookhelper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Chirag on 09/06/2016.
 */
public class FbData {

    @SerializedName("is_silhouette")
    @Expose
    private Boolean isSilhouette;
    @SerializedName("url")
    @Expose
    private String url;

    /**
     *
     * @return
     *     The isSilhouette
     */
    public Boolean getIsSilhouette() {
        return isSilhouette;
    }

    /**
     *
     * @param isSilhouette
     *     The is_silhouette
     */
    public void setIsSilhouette(Boolean isSilhouette) {
        this.isSilhouette = isSilhouette;
    }

    /**
     *
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

}
