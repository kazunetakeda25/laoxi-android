package com.rider.utils;

import com.rider.BuildConfig;

/**
 * Created by hitesh on 30/7/16.
 */
public class LaoxiConstant {

    public static final String GOOGLE_MAP_TEST_API_KEY = "AIzaSyAF69JEtuW0gGF8AAuw_JFwzHzZpM5Vd_8";
    public static final String GOOGLE_MAP_API_KEY = "AIzaSyAF69JEtuW0gGF8AAuw_JFwzHzZpM5Vd_8";

    public static final String ERROR_LINK="http://hyperlinkserver.com/wconnect/ws/crashcall.php?";
//    public static final String PLACE_ORDER ="place_order" ;
//    public static final String PLACE_ORDER_DATA ="place_order_data" ;
    public static final String TRIPDATETIME = "tripdatetime";
    public static final String USER_TYPE ="user_type" ;
    public static final String PAGE = "page";
    public static final String ORDER = "order";
    public static final String ORDER_DATA ="order_data" ;
    public static final String FBDATA="fb_data";
    public static final String SIGN_UP_TYPE="sign_up_type";


    public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    public static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    public static final String OUT_JSON = "/json";
    public static final String MESSAGE ="message" ;
    public static final String SENDER_PHONE =  "sender_phone";
    public static final String TRIP_ID = "trip_id";



    public static String HEADER_AUTHORIZATION="Authorization-Laoxi";
    public static String AUTHORIZATION_KEY_VALUE="piyush";
    public static final String PUT_CONTENT_HEADER = "Content-Type";
    public static final String PUT_CONTENT_HEADER_VALUE = "application/x-www-form-urlencoded";

    public static final String HEADER_TOCKEN="token";
    public static final String REGION="region";

    public static final String EMAIL="email";
    public static final String PASSWORD="password";
    public static final String LATITUDE="latitude";
    public static final String LONGITUDE="longitude";
    public static final String FNAME="fname";
    public static final String NAME ="name";
    public static final String GNAME="gname";
    public static final String GENDER="gender";
    public static final String FB_ID="fb_id";
    public static final String G_ID="g_id";
    public static final String PROFILE_IMAGE="profile_image";
    public static final String COUNTRY_CODE="country_code";
    public static final String DEVICE_ID="device_id";
    public static final String DEVICE_TYPE="device_type";
    public static final String PHONE="phone";
    public static final String USER_ID="user_id";
    public static final String OLD_PASSWORD="old_password";
    public static final String NEW_PASSWORD="new_password";

    public static final String CAR_TYPE="car_type";
    public static final String PICKUP_ADDRESS="pickup_address";
    public static final String PICKUP_LATITUDE="pickup_latitude";
    public static final String PICKUP_LONGITUDE="pickup_longitude";
    public static final String DROPOFF_ADDRESS="delivery_address";
    public static final String DROPOFF_LATITUDE="delivery_latitude";
    public static final String DROPOFF_LONGITUDE="delivery_longitude";
    public static final String TRIP_TYPE="trip_type";
    public static final String ORDER_ID="order_id";
    public static final String VOTES="votes";
    public static final String COMMENT="comment";

    public static final String CARD_NUMBER="number";
    public static final String EXPIRATION_DATE="expirationdate";
    public static final String CVV="cvc";
    public static final String CARD_HOLDER_NAME="card_holder_name";
    public static final String ID="id";
    public static final String TIP="tip";
    public static final String MONTH="expiry_month";
    public static final String YEAR="expiry_year";
    public static final String ADDRESS_LINE_1="address_line1";
    public static final String CITY="address_city";
    public static final String COUNTRY="address_country";
    public static final String CARD_TOKEN="card_token";
    public static final String ROUNDED_FARE="round_fare";
    public static final String APP_VERSION="app_version";


    public static final String TAXI ="1";
    public static final String BIKE ="2";
    public static final String TUKTUK ="3";
    public static final String OWNER ="5";


    //save a key for stored data in shared prefrence
    public static final String STORED_SERVICE_TYPE = "stored_servie_type";
    public static final String STORED_USER ="stored_user";
    public static final String DATA ="data";

    public static final String LOGIN_USER_NAME="loginUserName";
    public static final String LOGIN_USER_PASSWORD="loginUserPassword";

    public static final String STORED_ORDER_FROM ="order_from";
    public static final String STORED_ORDER_FROM_DATA ="order_from_data";

    public static final String CAR_TYPE_ID ="car_type_id";
    public static final String CAR_TYPE_ID_DATA ="car_type_id_data";

    public static final String DRIVER_ID ="driver_id";
    public static final String DRIVER_ID_DATA ="driver_id_data";

    public static final String USER_LAT ="user_lat";
    public static final String USER_LNG ="user_lng";

    public static String ISLOGIN="islogin";
    public static boolean ISFROMSEARCH=false;

    public static String IS_PROVIDE_DROPOFF="isProvideDropoff";
    public static String IS_PROVIDE_DROPOFF_DATA="isProvideDropoffData";

    public static String IS_FIRST_TIME="isFirstTime";
    public static String IS_FIRST_TIME_DATA="isFirstTimeData";
    public static final String SELECTED_PLACE_ID="selected_place_id";
    public static final String SELECTED_PLACE_ID_KEY="selected_place_id_key";

    public static final String USER_DEFAULT_LATITUDE ="userdefaultlatitude" ;
    public static final String USER_DEFAULT_LATITUDE_DATA ="userdefaultlatitude_data" ;

    public static final String USER_DEFAULT_LONGITUDE ="userdefaultlongitude" ;
    public static final String USER_DEFAULT_LONGITUDE_DATA ="userdefaultlongitude_data" ;

    public static String IS_SHOW_APPVERSION_UPDATE_DIALOG="isShowDialog";
    public static String IS_SHOW_APPVERSION_UPDATE_DIALOG_DATA="isShowDialogData";

    public static String LAST_APP_VERSION_FROM_API="lastAppVersionFromApi";
    public static String LAST_APP_VERSION_FROM_API_DATA="lastAppVersionFromApiData";

    public static final String BASE_URL = BuildConfig.BASE_URL ;
    public static final String LOGIN = "api/v1/rider/login/";
    public static final String SIGNUP = "api/v1/rider/signup";
    public static final String EDIT_PROFILE = "api/v1/rider/editinfo";
    public static final String FORGOT_PASSWORD = "api/v1/rider/passwordforgot";
    public static final String NEAR_FREE_DRIVER = "api/v1/driver/nighfreedrivers/";
    public static final String GET_CARLIST = "api/v1/trip/getnighcartype";
    public static final String PLACE_ORDER = "api/v1/trip/placetrip";
    public static final String FIND_DRIVER = "api/v1/orders/find_driver";
    public static final String FARE_ESTIMATION = "api/v1/trip/priceestimation/";
    public static final String ORDER_STATUS = "api/v1/orders/order_status";
    public static final String PAST_ORDER = "api/v1/trip/trips_past";
    public static final String CANCEL_ORDER = "api/v1/trip/trip_cancel/";
    public static final String LOGOUT = "api/v1/rider/logout/";
    public static final String GET_DRIVER_DETAILS = "api/v1/driver/driversdetail";
    public static final String GET_FARE_ESTIMATE = "api/v1/trip/priceestimation/";
    public static final String ONLINE_SUPPORT = "api/v1/trip/onlinesupport/";
    public static final String ADD_NEW_CARD = "api/v1/rider/addcard";

    public static final String FUTURE_ORDER = "api/v1/trip/approach_trips";
    public static final String RATE_AND_REVIEW = "api/v1/trip/price_review";
    public static final String TRACK_RIDE = "api/v1/trip/trip_track";
    public static final String GET_SERVICE_TYPE = "api/v1/rider/service_type";
    public static final String PAYMENT = "api/v1/trip/pay";
    public static final String UPDATE_DROPOFF = "api/v1/trip/updatedropout";
    public static final String CHANGE_PASSWORD = "api/v1/rider/passwordchange/";
    public static final String DELETE_PAYMENT_CARD = "api/v1/rider/trashcard";
    public static final String SEND_SMS = "api/v1/trip/transmit_sms";
    public static final String GET_ORDER_DETAILS = "api/v1/trip/get_trip";
    public static final String VERIFY_OTP = "api/v1/rider/otp_truth/";
    public static final String RESEND_OTP = "api/v1/rider/retransmitotp/";
    public static final String ADD_FAVOURITE_DRIVER = "api/v1/rider/add_prefer_driver";
    public static final String REMOVE_FAVOURITE_DRIVER = "api/v1/rider/remove_prefer_driver";
    public static final String REDEEM_PROMO_CODE = "api/v1/rider/redeem_promo_code";

    public static final String PAY_TYPE = "pay_type";// rhc modified
}// end of class


