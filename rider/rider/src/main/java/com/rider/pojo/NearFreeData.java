package com.rider.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class NearFreeData {

    @SerializedName("cartype_list")
    @Expose
    private CartypeList cartypeList;
    @SerializedName("driverlist")
    @Expose
    private List<Driverlist> driverlist = new ArrayList<Driverlist>();
    @SerializedName("unread_notification")
    @Expose
    private Integer unreadNotification;

    /**
     * @return The cartypeList
     */
    public CartypeList getCartypeList() {
        return cartypeList;
    }

    /**
     * @param cartypeList The cartype_list
     */
    public void setCartypeList(CartypeList cartypeList) {
        this.cartypeList = cartypeList;
    }

    /**
     * @return The driverlist
     */

    public List<Driverlist> getDriverlist() {
        return driverlist;
    }

    /**
     * @param driverlist The driverlist
     */
    public void setDriverlist(List<Driverlist> driverlist) {
        this.driverlist = driverlist;
    }

    /**
     * @return The unreadNotification
     */
    public Integer getUnreadNotification() {
        return unreadNotification;
    }

    /**
     * @param unreadNotification The unread_notification
     */
    public void setUnreadNotification(Integer unreadNotification) {
        this.unreadNotification = unreadNotification;
    }

}