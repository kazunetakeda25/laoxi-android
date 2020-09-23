package com.driver.retrofit;

import com.driver.pojo.GenericResponse;
import com.driver.pojo.TrackRidePojo.TrackRide;
import com.driver.pojo.acceptorder.AcceptOrderResponse;
import com.driver.pojo.dropoffWs.DropOffResponse;
import com.driver.pojo.loginws.LoginResponse;
import com.driver.pojo.upcomingRide.UpcomingRide;
import com.driver.util.ConstantClass;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
 * Created by mahuateng on 2/7/18.
 */

public interface ApiService {

    @GET(ConstantClass.METHOD_FORGET_PASSWORD)
    Call<Void> callForgetPasswordWS(
            @Header(ConstantClass.HEADER_TOCKEN) String token,
            @Query("email") String email
    );

    @FormUrlEncoded
    @PUT(ConstantClass.METHOD_LOGIN)
    Call<LoginResponse> logIn(
            @Header(ConstantClass.HEADER_TOCKEN) String token,
            @Field(ConstantClass.EMAIL) String email,
            @Field(ConstantClass.PASSWORD) String password,
            @Field(ConstantClass.DEVICE_ID) String device_id,
            @Field(ConstantClass.DEVICE_TYPE) String device_type,
            @Field(ConstantClass.APP_VERSION) String app_version
    );

    @FormUrlEncoded
    @PUT(ConstantClass.METHOD_LOGOUT)
    Call<Void> logOut(
            @Header(ConstantClass.HEADER_TOCKEN) String token,
            @Field(ConstantClass.DRIVER_ID) String driver_id
    );

    @GET(ConstantClass.METHOD_STATE_MANAGEMENT)
    Call<TrackRide> callStateManagement(
            @Header(ConstantClass.HEADER_TOCKEN) String token,
            @Query(ConstantClass.USER_ID) String user_id,
            @Query(ConstantClass.USER_TYPE) String user_type
    );

    @FormUrlEncoded
    @PUT(ConstantClass.METHOD_UPDATE_LAT_LONG)
    Call<ResponseBody> callUpdateLatLong(
            @Header(ConstantClass.HEADER_TOCKEN) String token,
            @Field(ConstantClass.DRIVER_ID) String driver_id,
            @Field(ConstantClass.DRIVER_LATITUDE) String lat,
            @Field(ConstantClass.DRIVER_LONGITUDE) String lon
    );

    @GET(ConstantClass.METHOD_GET_ORDER)
    Call<TrackRide> doGetOrderDetailWS(
            @Header(ConstantClass.HEADER_TOCKEN) String token,
            @Query(ConstantClass.ORDER_ID) String orderId,
            @Query(ConstantClass.DRIVER_ID) String driver_id
    );

    @GET(ConstantClass.METHOD_DECLINE_REQUEST)
    Call<GenericResponse> callDeclineRequestWS(
            @Header(ConstantClass.HEADER_TOCKEN) String token,
            @Query(ConstantClass.ORDER_ID) String orderId,
            @Query(ConstantClass.DRIVER_ID) String driver_id
    );

    @GET(ConstantClass.METHOD_ACCEPT_RIDE)
    Call<AcceptOrderResponse> callAcceptRequestWS(
            @Header(ConstantClass.HEADER_TOCKEN) String token,
            @Query(ConstantClass.ORDER_ID) String orderId,
            @Query(ConstantClass.DRIVER_ID) String driver_id
    );

    @FormUrlEncoded
    @PUT(ConstantClass.METHOD_SERVICE_ON_OFF)
    Call<GenericResponse> callChangeServiceStatus(
            @Header(ConstantClass.HEADER_TOCKEN) String token,
            @Field(ConstantClass.DRIVER_ID) String driver_id,
            @Field(ConstantClass.SERVICE) String service
    );

    @FormUrlEncoded
    @PUT(ConstantClass.METHOD_CANCEL_TRIP)
    Call<GenericResponse> callCancelTripWS(
            @Header(ConstantClass.HEADER_TOCKEN) String token,
            @Field(ConstantClass.ID) String driver_id,
            @Field(ConstantClass.ORDER_ID) String orderId,
            @Field(ConstantClass.CANCELATION_REASON) String selectedReason,
            @Field(ConstantClass.USER_TYPE) String user_type
    );

    @FormUrlEncoded
    @PUT(ConstantClass.METHOD_CHANGE_PASSWORD)
    Call<GenericResponse> callChangePassword(
            @Header(ConstantClass.HEADER_TOCKEN) String token,
            @Field(ConstantClass.DRIVER_ID) String driver_id,
            @Field(ConstantClass.CHANGE_PASSWORD_OLD_PASSWORD) String old_pwd,
            @Field(ConstantClass.CHANGE_PASSWORD_NEW_PASSWORD) String new_pwd
    );

    @GET(ConstantClass.METHOD_ARRIVED_AT_PICK_UP)
    Call<GenericResponse> callArrivedAtPickupLocationWs(
            @Header(ConstantClass.HEADER_TOCKEN) String token,
            @Query(ConstantClass.DRIVER_ID) String driver_id,
            @Query(ConstantClass.ORDER_ID) String orderId

    );

    @GET(ConstantClass.METHOD_PICKUP)
    Call<Void> callPickupWS(
            @Header(ConstantClass.HEADER_TOCKEN) String token,
            @Query(ConstantClass.DRIVER_ID) String driver_id,
            @Query(ConstantClass.ORDER_ID) String orderId

    );

    @FormUrlEncoded
    @POST(ConstantClass.METHOD_SEND_SMS)
    Call<GenericResponse> doSms(
            @Header(ConstantClass.HEADER_TOCKEN) String token,
            @Field(ConstantClass.PHONE_NUMBER) String phone,
            @Field(ConstantClass.MESSAGE) String message,
            @Field(ConstantClass.SENDER_PHONE) String sender_phone,
            @Field(ConstantClass.TRIP_ID) String trip_id
    );

    @FormUrlEncoded
    @POST(ConstantClass.METHOD_DROPOFF_TRIP)
    Call<DropOffResponse> callDropOff_TripWS(
            @Header(ConstantClass.HEADER_TOCKEN) String token,
            @Field(ConstantClass.ORDER_ID) String orderId,
            @Field(ConstantClass.DISTANCE) String distance,
            @Field(ConstantClass.DELIVERY_ADDRESS) String address,
            @Field(ConstantClass.DELIVERY_LATITUDE) String latitude,
            @Field(ConstantClass.DELIVERY_LONGITUDE) String longitude
    );

    @FormUrlEncoded
    @POST(ConstantClass.METHOD_FEEDBACK)
    Call<Void> callFeedBack(
            @Header(ConstantClass.HEADER_TOCKEN) String token,
            @Field(ConstantClass.DRIVER_ID) String driver_id,
            @Field(ConstantClass.USER_ID) String user_id,
            @Field(ConstantClass.USER_TYPE) String user_type,
            @Field(ConstantClass.ORDER_ID) String orderId,
            @Field(ConstantClass.COMMENT) String comment,
            @Field(ConstantClass.VOTES) String votes
    );

    @FormUrlEncoded
    @PUT(ConstantClass.METHOD_ONLINE_SUPPORT)
    Call<GenericResponse> callOnlineSupportWS(
            @Header(ConstantClass.HEADER_TOCKEN) String token,
            @Field(ConstantClass.USER_ID) String user_id,
            @Field(ConstantClass.NAME) String name,
            @Field(ConstantClass.EMAIL) String email,
            @Field(ConstantClass.MESSAGE) String message,
            @Field(ConstantClass.USER_TYPE) String user_type
    );

    @GET(ConstantClass.METHOD_PAST_RIDE)
    Call<ArrayList<UpcomingRide>> callPastRide(
            @Header(ConstantClass.HEADER_TOCKEN) String token,
            @Query(ConstantClass.USER_ID) String user_id,
            @Query(ConstantClass.USER_TYPE) String user_type,
            @Query(ConstantClass.PAGE) String page

    );

    @GET(ConstantClass.METHOD_UPCOMING_TRIP)
    Call<ArrayList<UpcomingRide>> callUpcomingRide(
            @Header(ConstantClass.HEADER_TOCKEN) String token,
            @Query(ConstantClass.USER_ID) String user_id,
            @Query(ConstantClass.USER_TYPE) String user_type,
            @Query(ConstantClass.PAGE) String page
    );

    @GET(ConstantClass.METHOD_GET_PROFILE_DATA)
    Call<LoginResponse> callGetProfileData(
            @Header(ConstantClass.HEADER_TOCKEN) String token,
            @Query(ConstantClass.DRIVER_ID) String driver_id
    );

    @Multipart
    @POST(ConstantClass.METHOD_UPDATE_PROFILE)
    Call<LoginResponse> callUpdateProfileForMultipleImage(
            @Header(ConstantClass.HEADER_TOCKEN) String token,
            @Part(ConstantClass.DRIVER_ID) RequestBody driver_id,
            @Part(ConstantClass.FIRST_NAME) RequestBody fname,
            @Part(ConstantClass.LAST_NAME) RequestBody lname,
            @Part(ConstantClass.PHONE_NUMBER) RequestBody phone,
            @Part(ConstantClass.COUNTRY_CODE) RequestBody ctcode,
            @Part List<MultipartBody.Part> profiles
    );
}
