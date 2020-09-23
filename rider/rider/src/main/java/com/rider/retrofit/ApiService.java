package com.rider.retrofit;

import com.rider.pojo.CardInfo;
import com.rider.pojo.CartypeList;
import com.rider.pojo.DriverDetails;
import com.rider.pojo.FareEstimationData;
import com.rider.pojo.GenericResponse;
import com.rider.pojo.NearFreeData;
import com.rider.pojo.OrderData;
import com.rider.pojo.RidesDataList;
import com.rider.pojo.User;
import com.rider.pojo.VehicleType;
import com.rider.utils.LaoxiConstant;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Ma on 2/25/2018.
 */

public interface ApiService {

    @GET(LaoxiConstant.FORGOT_PASSWORD)
    Call<Void> doForgotPassword(
        @Header(LaoxiConstant.HEADER_TOCKEN) String token,
        @Query("email") String email
    );

    @FormUrlEncoded
    @PUT(LaoxiConstant.LOGIN)
    Call<User> logIn(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.EMAIL) String email,
            @Field(LaoxiConstant.PASSWORD) String password,
            @Field(LaoxiConstant.DEVICE_ID) String device_id,
            @Field(LaoxiConstant.DEVICE_TYPE) String device_type,
            @Field(LaoxiConstant.APP_VERSION) String app_version

    );

    @FormUrlEncoded
    @PUT(LaoxiConstant.LOGIN)
    Call<User> loginFacebook(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.FB_ID) String fbId,
            @Field(LaoxiConstant.DEVICE_ID) String device_id,
            @Field(LaoxiConstant.DEVICE_TYPE) String device_type,
            @Field(LaoxiConstant.APP_VERSION) String app_version
    );
    //20180621 added by rhc
    @FormUrlEncoded
    @PUT(LaoxiConstant.LOGIN)
    Call<User> loginGoogle(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.G_ID) String GId,
            @Field(LaoxiConstant.DEVICE_ID) String device_id,
            @Field(LaoxiConstant.DEVICE_TYPE) String device_type,
            @Field(LaoxiConstant.APP_VERSION) String app_version
    );

    @FormUrlEncoded
    @POST(LaoxiConstant.SIGNUP)
    Call<User> SignUp(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.EMAIL) String email,
            @Field(LaoxiConstant.PASSWORD) String password,
            @Field(LaoxiConstant.PHONE) String phone,
            @Field(LaoxiConstant.NAME) String name,
            @Field(LaoxiConstant.COUNTRY_CODE) String country_code,
            @Field(LaoxiConstant.GENDER) String gender
    );

    @FormUrlEncoded
    @POST(LaoxiConstant.SIGNUP)
    Call<User> FBSignUp(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.EMAIL) String email,
            @Field(LaoxiConstant.FB_ID) String fb_id,
            @Field(LaoxiConstant.PHONE) String phone,
            @Field(LaoxiConstant.NAME) String name,
            @Field(LaoxiConstant.COUNTRY_CODE) String country_code,
            @Field(LaoxiConstant.GENDER) String gender
    );

    @FormUrlEncoded
    @POST(LaoxiConstant.SIGNUP)
    Call<User> GSignUp(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.EMAIL) String email,
            @Field(LaoxiConstant.G_ID) String g_id,
            @Field(LaoxiConstant.PHONE) String phone,
            @Field(LaoxiConstant.NAME) String name,
            @Field(LaoxiConstant.COUNTRY_CODE) String country_code,
            @Field(LaoxiConstant.GENDER) String gender
    );
    @FormUrlEncoded
    @PUT(LaoxiConstant.RESEND_OTP)
    Call<GenericResponse> doResendOTP(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.USER_ID) String user_id

    );

    @FormUrlEncoded
    @PUT(LaoxiConstant.LOGOUT)
    Call<Void> logOut(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.USER_ID) String user_id
    );

    @GET(LaoxiConstant.GET_ORDER_DETAILS)
    Call<OrderData> doGetOrderDetails(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Query(LaoxiConstant.ORDER_ID) String orderId,
            @Query(LaoxiConstant.USER_ID) String user_id
    );


    @GET(LaoxiConstant.TRACK_RIDE)
    Call<OrderData> doTrackRide(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Query(LaoxiConstant.USER_ID) String user_id,
            @Query(LaoxiConstant.USER_TYPE) String user_type
    );

    @GET(LaoxiConstant.GET_SERVICE_TYPE)
    Call<VehicleType> getServiceType(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token
            );

    @FormUrlEncoded
    @PUT(LaoxiConstant.VERIFY_OTP)
    Call<User> doVerifyOTP(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field("otp") String otp,
            @Field(LaoxiConstant.USER_ID) String user_id,
            @Field(LaoxiConstant.DEVICE_ID) String device_id,
            @Field(LaoxiConstant.DEVICE_TYPE) String device_type,
            @Field(LaoxiConstant.APP_VERSION) String version
    );

    @GET(LaoxiConstant.DELETE_PAYMENT_CARD)
    Call<ArrayList<CardInfo>> doDeleteCard(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Query(LaoxiConstant.USER_ID) String user_id,
            @Query(LaoxiConstant.CARD_TOKEN) String card_token
    );

    @FormUrlEncoded
    @PUT(LaoxiConstant.CANCEL_ORDER)
    Call<GenericResponse> doCancelOrder(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.ID) String user_id,
            @Field(LaoxiConstant.USER_TYPE) String user_type,
            @Field(LaoxiConstant.ORDER_ID) String order_id,
            @Field(LaoxiConstant.CARD_TOKEN) String card_token
    );

    @GET(LaoxiConstant.GET_DRIVER_DETAILS)
    Call<DriverDetails> doGetDriverDetails(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Query(LaoxiConstant.DRIVER_ID) String driver_id,
            @Query(LaoxiConstant.USER_LAT) String lat,
            @Query(LaoxiConstant.USER_LNG) String lng
    );

    @FormUrlEncoded
    @POST(LaoxiConstant.PLACE_ORDER)
    Call<OrderData> doPlaceOrder(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.USER_ID) String user_id,
            @Field(LaoxiConstant.CAR_TYPE_ID) String car_type_id,
            @Field(LaoxiConstant.DRIVER_ID) String driver_id,
            @Field(LaoxiConstant.PICKUP_ADDRESS) String pickAddress,
            @Field(LaoxiConstant.PICKUP_LATITUDE) String pickLat,
            @Field(LaoxiConstant.PICKUP_LONGITUDE) String pickLng,
            @Field(LaoxiConstant.DROPOFF_ADDRESS) String dropAddress,
            @Field(LaoxiConstant.DROPOFF_LATITUDE) String dropLat,
            @Field(LaoxiConstant.DROPOFF_LONGITUDE) String dropLng,
            @Field(LaoxiConstant.TRIP_TYPE) String trip_type,
            @Field(LaoxiConstant.TRIPDATETIME) String triptime,
            @Field(LaoxiConstant.GENDER) String gender
    );

    @FormUrlEncoded
    @POST(LaoxiConstant.PLACE_ORDER)
    Call<GenericResponse> doPlaceOrderLater(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.USER_ID) String user_id,
            @Field(LaoxiConstant.CAR_TYPE_ID) String car_type_id,
            @Field(LaoxiConstant.DRIVER_ID) String driver_id,
            @Field(LaoxiConstant.PICKUP_ADDRESS) String pickAddress,
            @Field(LaoxiConstant.PICKUP_LATITUDE) String pickLat,
            @Field(LaoxiConstant.PICKUP_LONGITUDE) String pickLng,
            @Field(LaoxiConstant.DROPOFF_ADDRESS) String dropAddress,
            @Field(LaoxiConstant.DROPOFF_LATITUDE) String dropLat,
            @Field(LaoxiConstant.DROPOFF_LONGITUDE) String dropLng,
            @Field(LaoxiConstant.TRIP_TYPE) String trip_type,
            @Field(LaoxiConstant.TRIPDATETIME) String triptime,
            @Field(LaoxiConstant.GENDER) String gender
    );

    @FormUrlEncoded
    @PUT(LaoxiConstant.GET_FARE_ESTIMATE)
    Call<FareEstimationData> doGetFareEstimate(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.PICKUP_LATITUDE) String pick_lat,
            @Field(LaoxiConstant.PICKUP_LONGITUDE) String pick_lng,
            @Field(LaoxiConstant.DROPOFF_LATITUDE) String drop_lat,
            @Field(LaoxiConstant.DROPOFF_LONGITUDE) String drop_lng,
            @Field(LaoxiConstant.CAR_TYPE_ID) String car_type_id,
            @Field(LaoxiConstant.TRIP_TYPE) String trip_type
    );

    @FormUrlEncoded
    @POST(LaoxiConstant.UPDATE_DROPOFF)
    Call<Void> doEnterDropOff(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.ORDER_ID) String orderId,
            @Field(LaoxiConstant.DROPOFF_ADDRESS) String address,
            @Field(LaoxiConstant.DROPOFF_LATITUDE) String latitude,
            @Field(LaoxiConstant.DROPOFF_LONGITUDE) String longitude
    );

    @FormUrlEncoded
    @PUT(LaoxiConstant.NEAR_FREE_DRIVER)
    Call<NearFreeData> doNearFreeDriverNew(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.USER_ID) String user_id,
            @Field(LaoxiConstant.LATITUDE) String lat,
            @Field(LaoxiConstant.LONGITUDE) String lng,
            @Field(LaoxiConstant.CAR_TYPE) String car_type,
            @Field(LaoxiConstant.REGION) String region
    );

    @FormUrlEncoded
    @PUT(LaoxiConstant.GET_CARLIST)
    Call<CartypeList> doCarListLater(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.PICKUP_LATITUDE) String user_id,
            @Field(LaoxiConstant.PICKUP_LONGITUDE) String lat,
            @Field(LaoxiConstant.CAR_TYPE) String car_type,
            @Field(LaoxiConstant.REGION) String region
    );

    @FormUrlEncoded
    @PUT(LaoxiConstant.CHANGE_PASSWORD)
    Call<GenericResponse> doChangePassword(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.USER_ID) String user_id,
            @Field(LaoxiConstant.OLD_PASSWORD) String old_pwd,
            @Field(LaoxiConstant.NEW_PASSWORD) String new_pwd
    );

    @FormUrlEncoded
    @POST(LaoxiConstant.SEND_SMS)
    Call<GenericResponse> doSms(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.PHONE) String phone,
            @Field(LaoxiConstant.MESSAGE) String message,
            @Field(LaoxiConstant.SENDER_PHONE) String sender_phone,
            @Field(LaoxiConstant.TRIP_ID) String trip_id
    );

    @FormUrlEncoded
    @POST(LaoxiConstant.RATE_AND_REVIEW)
    Call<Void> doRateAndReview(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.ORDER_ID) String orderId,
            @Field(LaoxiConstant.DRIVER_ID) String driver_id,
            @Field(LaoxiConstant.USER_ID) String user_id,
            @Field(LaoxiConstant.USER_TYPE) String user_type,
            @Field(LaoxiConstant.COMMENT) String comment,
            @Field(LaoxiConstant.VOTES) String votes
    );

    @FormUrlEncoded
    @POST(LaoxiConstant.ADD_FAVOURITE_DRIVER)
    Call<Void> addFavouriteDriver(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.DRIVER_ID) String driver_id,
            @Field(LaoxiConstant.USER_ID) String user_id
    );

    @FormUrlEncoded
    @POST(LaoxiConstant.REMOVE_FAVOURITE_DRIVER)
    Call<Void> removeFavouriteDriver(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.DRIVER_ID) String driver_id,
            @Field(LaoxiConstant.USER_ID) String user_id
    );

    @FormUrlEncoded
    @PUT(LaoxiConstant.ONLINE_SUPPORT)
    Call<GenericResponse> wsCallSupport(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.USER_ID) String user_id,
            @Field(LaoxiConstant.USER_TYPE) String user_type,
            @Field(LaoxiConstant.NAME) String name,
            @Field(LaoxiConstant.EMAIL) String email,
            @Field(LaoxiConstant.MESSAGE) String message

    );

    @GET(LaoxiConstant.PAST_ORDER)
    Call<ArrayList<RidesDataList>> doPastOrder(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Query(LaoxiConstant.USER_ID) String user_id,
            @Query(LaoxiConstant.USER_TYPE) String user_type,
            @Query(LaoxiConstant.PAGE) String page

    );
//
    @GET(LaoxiConstant.FUTURE_ORDER)
    Call<ArrayList<RidesDataList>> doFutureOrder(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Query(LaoxiConstant.USER_ID) String user_id,
            @Query(LaoxiConstant.USER_TYPE) String user_type,
            @Query(LaoxiConstant.PAGE) String page
    );

    @FormUrlEncoded
    @POST(LaoxiConstant.ADD_NEW_CARD)
    Call<ArrayList<CardInfo>> wsCallAddNewCard(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.USER_ID) String user_id,
            @Field(LaoxiConstant.CVV) String cvv,
            @Field(LaoxiConstant.CARD_HOLDER_NAME) String card_name,
            @Field(LaoxiConstant.CARD_NUMBER) String card_num,
            @Field(LaoxiConstant.MONTH) String month,
            @Field(LaoxiConstant.YEAR) String year,
            @Field(LaoxiConstant.ADDRESS_LINE_1) String address,
            @Field(LaoxiConstant.CITY) String city,
            @Field(LaoxiConstant.COUNTRY) String country
    );

    @FormUrlEncoded
    @PUT(LaoxiConstant.PAYMENT)
    Call<Void> doPayment(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.USER_ID) String user_id,
            @Field(LaoxiConstant.ORDER_ID) String order_id,
            @Field(LaoxiConstant.PAY_TYPE) String pay_type,
            @Field(LaoxiConstant.TIP) String tip,
            @Field(LaoxiConstant.CARD_TOKEN) String card_token,
            @Field(LaoxiConstant.ROUNDED_FARE) String round_fare
    );

    @FormUrlEncoded
    @POST(LaoxiConstant.REDEEM_PROMO_CODE)
    Call<GenericResponse> redeemPromoCode(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Field(LaoxiConstant.USER_ID) String user_id,
            @Field("code") String code
    );

    @Multipart
    @POST(LaoxiConstant.EDIT_PROFILE)
    Call<User> doEditProfile(
            @Header(LaoxiConstant.HEADER_TOCKEN) String token,
            @Part(LaoxiConstant.NAME) RequestBody driver_id,
            @Part(LaoxiConstant.USER_ID) RequestBody fname,
            @Part(LaoxiConstant.FNAME) RequestBody lname,
            @Part MultipartBody.Part profiles
    );
}
