package com.rider.facebookhelper;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Chirag on 09/06/2016.
 */
public class FbResponse {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("picture")
    @Expose
    private FbPicture picture;
    @SerializedName("gender")
    @Expose
    private String gender;

    /**
     *
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     *     The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     *     The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     *     The picture
     */
    public FbPicture getPicture() {
        return picture;
    }

    /**
     *
     * @param picture
     *     The picture
     */
    public void setPicture(FbPicture picture) {
        this.picture = picture;
    }

    /**
     *
     * @return
     *     The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     *
     * @param gender
     *     The gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

}
