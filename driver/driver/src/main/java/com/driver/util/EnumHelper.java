package com.driver.util;

/**
 * Created by Chirag Solanki on 15/10/2016.
 */

public class EnumHelper {
    public enum CarType {
        OSCAR("Oscar"), BIG_OSCAR("Big Oscar"), FANCY_OSCAR("Fancy Oscar");
        String seletedString;

        public String getSeletedString() {
            return seletedString;
        }

        CarType(String type) {
            this.seletedString = type;
        }
    }
}
