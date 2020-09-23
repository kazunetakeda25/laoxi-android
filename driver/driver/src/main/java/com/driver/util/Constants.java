package com.driver.util;

/**
 * Created by hlink54 on 19/7/16.
 */
public interface Constants {
    String COMPLETE = "Completed";
    String CANCELED = "Cancelled";

    public static final String ERRORLINK = "http://hyperlinkserver.com/wconnect/ws/crashcall.php?";


    enum RideStatus {
        COMPLETE, CANCELED;
    }
}
