package com.driver.pojo;


import com.google.gson.annotations.Expose;

public class ListDataPojo {

    @Expose
    private int id;

    @Expose
    private String strItemNames;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStrItemNames() {
        return strItemNames;
    }

    public void setStrItemNames(String strItemNames) {
        this.strItemNames = strItemNames;
    }
}
