package com.driver.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartypeList {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("region_id")
        @Expose
        private String regionId;
        @SerializedName("car_type")
        @Expose
        private String carType;
        @SerializedName("car_type_image")
        @Expose
        private String carTypeImage;
        @SerializedName("base_fare")
        @Expose
        private String baseFare;
        @SerializedName("per_minute")
        @Expose
        private String perMinute;
        @SerializedName("per_km")
        @Expose
        private String perKm;
        @SerializedName("min_fare")
        @Expose
        private String minFare;
        @SerializedName("cancellation")
        @Expose
        private String cancellation;
        @SerializedName("booking_fare")
        @Expose
        private String bookingFare;
        @SerializedName("splitting_ratio")
        @Expose
        private String splittingRatio;
        @SerializedName("is_active")
        @Expose
        private String isActive;
        @SerializedName("insertdate")
        @Expose
        private String insertdate;
        @SerializedName("passenger_levy")
        @Expose
        private String passengerLevy;

        /**
         *
         * @return
         * The id
         */
        public String getId() {
            return id;
        }

        /**
         *
         * @param id
         * The id
         */
        public void setId(String id) {
            this.id = id;
        }

        public String getRegionId() {
                return regionId;
        }

        public void setRegionId(String regionId) {
                this.regionId = regionId;
        }
        /**
         *
         * @return
         * The carTypeImage
         */
        public String getCarTypeImage() {
            return carTypeImage;
        }

        /**
         *
         * @param carTypeImage
         * The carTypeImage
         */
        public void setCarTypeImage(String carTypeImage) {
            this.carTypeImage = carTypeImage;
        }

        /**
         *
         * @return
         * The carType
         */
        public String getCarType() {
            return carType;
        }

        /**
         *
         * @param carType
         * The car_type
         */
        public void setCarType(String carType) {
            this.carType = carType;
        }

        /**
         *
         * @return
         * The baseFare
         */
        public String getBaseFare() {
            return baseFare;
        }

        /**
         *
         * @param baseFare
         * The base_fare
         */
        public void setBaseFare(String baseFare) {
            this.baseFare = baseFare;
        }

        /**
         *
         * @return
         * The perMinute
         */
        public String getPerMinute() {
            return perMinute;
        }

        /**
         *
         * @param perMinute
         * The per_minute
         */
        public void setPerMinute(String perMinute) {
            this.perMinute = perMinute;
        }

        /**
         *
         * @return
         * The perKm
         */
        public String getPerKm() {
            return perKm;
        }

        /**
         *
         * @param perKm
         * The per_km
         */
        public void setPerKm(String perKm) {
            this.perKm = perKm;
        }

        /**
         *
         * @return
         * The minFare
         */
        public String getMinFare() {
            return minFare;
        }

        /**
         *
         * @param minFare
         * The min_fare
         */
        public void setMinFare(String minFare) {
            this.minFare = minFare;
        }

        /**
         *
         * @return
         * The cancellation
         */
        public String getCancellation() {
            return cancellation;
        }

        /**
         *
         * @param cancellation
         * The cancellation
         */
        public void setCancellation(String cancellation) {
            this.cancellation = cancellation;
        }

        public String getBookingFare() {
                return bookingFare;
        }

        public void setBookingFare(String bookingfare) {
                this.bookingFare = bookingfare;
        }

        public String getSplittingRatio() {
                return splittingRatio;
        }

        public void setSplittingRatio(String splittingRatio) {
                this.splittingRatio = splittingRatio;
        }
        /**
         *
         * @return
         * The isActive
         */
        public String getIsActive() {
            return isActive;
        }

        /**
         *
         * @param isActive
         * The is_active
         */
        public void setIsActive(String isActive) {
            this.isActive = isActive;
        }

        /**
         *
         * @return
         * The insertdate
         */
        public String getInsertdate() {
            return insertdate;
        }

        /**
         *
         * @param insertdate
         * The insertdate
         */
        public void setInsertdate(String insertdate) {
            this.insertdate = insertdate;
        }

        public String getPassengerLevy() {
                return passengerLevy;
        }

        public void setPassengerLevy(String passengerLevy) {
                this.passengerLevy = passengerLevy;
        }

}
