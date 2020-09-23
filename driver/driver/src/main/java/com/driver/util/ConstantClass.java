package com.driver.util;

import com.driver.BuildConfig;

/**
 * Created by Chirag Solanki on 06/08/2016.
 */
public class ConstantClass {

    //----------------------------- Google Map API Key ----------------
    public static final String GOOGLE_MAP_API_KEY = "AIzaSyAF69JEtuW0gGF8AAuw_JFwzHzZpM5Vd_8";
    public static final String GOOGLE_MAP_TEST_API_KEY = "AIzaSyAF69JEtuW0gGF8AAuw_JFwzHzZpM5Vd_8";

    //----------------------------- Car Type ------------------
    public static final String TAXI = "1";
    public static final String BIKE = "2";
    public static final String TUKTUK = "3";
    public static final String OWNER = "5";


    //----------------------------- Header ------------------
    public static final String HEADERS_AUTHORIZATION = "Authorization-Laoxi:piyush";
    public static final String HEADER_AUTHORIZATION = "Authorization-Laoxi";
    public static final String AUTHORIZATION_KEY_VALUE = "piyush";
    public static final String PUT_CONTENT_HEADER = "Content-Type";
    public static final String PUT_CONTENT_HEADER_VALUE = "application/x-www-form-urlencoded";

    public static final String HEADER_TOCKEN = "token";

    //----------------------------- Shared Preferance ------------------
    public static final String PREFS_NAME = "Laoxi";
    public static final String LOGIN_USER = "loginUser";
    public static final String IS_LOGIN = "islogin";
    public static final String TRACK_RIDE_ORDER_DATA = "track_ride_order_data";
    public static final String LOGIN_USER_NAME = "loginUserName";
    public static final String LOGIN_USER_PASSWORD = "loginUserPassword";
    public static final String PREFS_NAME_USERNAME = "LaoxiUserNAme";
    public static final String USER_DEFAULT_LATITUDE = "userdefaultlatitude";
    public static final String USER_DEFAULT_LONGITUDE = "userdefaultlongitude";
    public static final String ISSTOREDPICKUPLATITUDE = "isstorepickuplatitude";


    //----------------------------- Response Code ------------------
    public static final int RESPONSE_CODE_200 = 200;
    public static final int RESPONSE_CODE_400 = 400;
    public static final int RESPONSE_CODE_404 = 404;
    public static final int RESPONSE_CODE_406 = 406;
    public static final int RESPONSE_CODE_401 = 401;


    //----------------------------- Base URL ------------------

    public static final String BASE_URL = BuildConfig.BASE_URL;
    public static final String SIGNUP_URL = BuildConfig.BASE_URL + "/driver-registration-form/";
    public static final String IMAGE_URL = BuildConfig.BASE_URL + "/assets/uploads/driver/";
    public static final String THUMB_IMAGE_URL = BuildConfig.BASE_URL + "/assets/uploads/driver/";
    public static final String CAR_IMAGE_ICON = BuildConfig.BASE_URL + "/assets/uploads/car_type_image/thumb/";

    public static final String USER_IMAGE_URL = BuildConfig.BASE_URL + "/assets/uploads/user/";
    public static final String TUMB_URL_USER = BuildConfig.BASE_URL + "/assets/uploads/user/";

    public static final String THUMB_CAR_TYPE_IMAGE = BuildConfig.BASE_URL + "/assets/uploads/car_type_image/";
    public static final String TOKEN_SERVICE_URL = BuildConfig.BASE_URL + "/twilio/token.php";

    //----------------------------- WS Method ------------------
    public static final String METHOD_FORGET_PASSWORD ="/api/v1/driver/passwordforgot/";
    public static final String METHOD_LOGIN = "api/v1/driver/login/";
    public static final String METHOD_LOGOUT = "api/v1/driver/logout/";
    public static final String METHOD_DECLINE_REQUEST = "/api/v1/trip/reject_request";
    public static final String METHOD_ACCEPT_RIDE = "/api/v1/trip/accept_trip";
    public static final String METHOD_ARRIVED_AT_PICK_UP = "/api/v1/trip/arrived_pickup";
    public static final String METHOD_PICKUP = "/api/v1/trip/pickup_status";
    public static final String METHOD_DROPOFF_TRIP = "/api/v1/trip/dropout_trip";
    public static final String METHOD_GET_PROFILE_DATA = "/api/v1/driver/driversdetail/";
    public static final String METHOD_CHANGE_PASSWORD = "/api//v1/driver/passwordchange/"; // No back slash in get method
    public static final String METHOD_UPDATE_PROFILE = "/api/v1/driver/editdriver/";
    public static final String METHOD_SERVICE_ON_OFF = "/api/v1/driver/changeservice/";
    public static final String METHOD_UPDATE_LAT_LONG = "/api/v1/driver/updatecoordinate/";
    public static final String METHOD_CANCEL_TRIP = "/api/v1/trip/trip_cancel";
    public static final String METHOD_ONLINE_SUPPORT = "/api/v1/trip/onlinesupport/";
    public static final String METHOD_FEEDBACK = "/api/v1/trip/price_review";
    public static final String METHOD_UPCOMING_TRIP = "/api/v1/trip/approach_trips";
    public static final String METHOD_PAST_RIDE = "/api/v1/trip/trips_past";
    public static final String METHOD_SEND_SMS="/api/v1/trip/transmit_sms";
    public static final String METHOD_GET_ORDER = "/api/v1/trip/get_trip";
    public static final String METHOD_STATE_MANAGEMENT = "api/v1/trip/trip_track";
    //----------------------------- Webservice Parameter ------------------
    public static final String EMAIL = "email";
    public static final String USER_TYPE = "user_type";
    public static final String PASSWORD = "password";
    public static final String DEVICE_ID = "device_id";
    public static final String DEVICE_TYPE = "device_type";
    public static final String DRIVER_ID = "driver_id";
    public static final String ORDER_ID = "order_id";
    public static final String ID = "id";
    public static final String USER_ID = "user_id";
    public static final String DISTANCE = "distance";
    public static final String DELIVERY_ADDRESS = "delivery_address";
    public static final String DELIVERY_LATITUDE = "delivery_latitude";
    public static final String DELIVERY_LONGITUDE = "delivery_longitude";
    public static final String DRIVER_LONGITUDE = "longitude";
    public static final String DRIVER_LATITUDE = "latitude";
    public static final String COMMENT = "comment";
    public static final String VOTES = "votes";
    public static final String APP_VERSION = "app_version";
    public static final String SERVICE = "service";

    // Change Password Parameters

    public static final String CHANGE_PASSWORD_OLD_PASSWORD = "old_password";
    public static final String CHANGE_PASSWORD_NEW_PASSWORD = "new_password";

    // Change Profile Data
    public static final String FIRST_NAME = "fname";
    public static final String NAME = "name";
    public static final String LAST_NAME = "lname";
    public static final String PHONE_NUMBER = "phone";
    public static final String COUNTRY_CODE = "country_code";
    public static final String CAR_TYPE = "car_type";
    public static final String PROFILE_IMAGE = "profile_image";
    public static final String MESSAGE = "message";
    public static final String PAGE = "page";
    public static final String SENDER_PHONE =  "sender_phone";
    public static final String TRIP_ID = "trip_id";

    public static final String SHARED_PREFRENCE_DISTANCE = "spdistance";


    public static final String CANCELATION_REASON = "cancellation_reason";

    public static String LAST_APP_VERSION_FROM_API="lastAppVersionFromApi";

}
