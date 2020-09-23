package com.rider;

import com.rider.application.Laoxi;
import com.rider.dao.CommonImplementation;
import com.rider.navigator.OnTaskComplete;
import com.rider.navigator.OnTaskCompleteNearFreeDriver;
import com.rider.utils.LaoxiConstant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.HashMap;

import okhttp3.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */

@RunWith(RobolectricTestRunner.class)
@Config(
        application = Laoxi.class,
        constants = BuildConfig.class
)
public class ApiMgrUnitTest {
    private static final String EMAIL = "t@t.com";
    private static final String PASSWORD = "password";
    private static final String DEVICE_ID = "device_id";
    private static final String PHONE = "";
    private static final String NAME = "";
    private static final String COUNTRY_CODE = "";
    private static final String GENDER = "m";
    private static final String USER_ID = "";
    private static final String FIRST_NAME = "";
    private static final String TOKEN = "";
    private static final String IMG_PATH = "";
    private static final String CAR_TYPE_ID = "";
    private static final String PICKUP_ADDRESS = "";
    private static final double LAT = 1.0;
    private static final double LNG = 1.0;
    private static final String DROPOFF_ADDRESS = "";
    private static final double DROPOFF_LAT = 1.0;
    private static final double DROPOFF_LNG = 1.0;
    private static final String TRIP_TYPE = "";
    private static final String DRIVER_ID = "";
    private static final String PAGE = "";
    private static final String ORDER_ID = "";
    private static final String CARD_TOKEN = "";
    private static final String COMMENT = "comment";
    private static final String VOTES = "votes";
    private static final float ROUNDED_FARE = 5.97f;
    private static final String NEW_PWD = "";
    private static final String OLD_PWD = "";
    private static final String SUPPORT_MSG = "";
    private static final String CCV = "";
    private static final String CARD_HOLDER_NAME = "";
    private static final String CARD_NUMBER = "";
    private static final String ADDRESS_LINE_1 = "";
    private static final String CITY = "";
    private static final String COUNTRY = "";
    private static final String CAR_TYPE = "";
    private static final String REGION = "";


    @Before
    public void setup() {
        CommonImplementation.getInstance().setContext(
                RuntimeEnvironment.application
        );
    }

    @Test
    public void test_DoLogin() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.EMAIL, EMAIL);
        param.put(LaoxiConstant.PASSWORD, PASSWORD);
        param.put(LaoxiConstant.DEVICE_ID, DEVICE_ID);
        param.put(LaoxiConstant.DEVICE_TYPE, "A");

        CommonImplementation.getInstance().doLogin(param, "", new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(200, response.code());
            }

            @Override
            public void onFailure() {
                fail("DoLogin::onFailure");
            }
        });
    }

    @Test
    public void test_DoSignUp() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.EMAIL, EMAIL);
        param.put(LaoxiConstant.PHONE, PHONE);
        param.put(LaoxiConstant.NAME, NAME);
        param.put(LaoxiConstant.COUNTRY_CODE, COUNTRY_CODE);
        param.put(LaoxiConstant.GENDER, GENDER);

        CommonImplementation.getInstance().doSignUp(param, null, "", new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(201, response.code());
            }

            @Override
            public void onFailure() {
                fail("DoSignUp::onFailure");
            }
        });
    }

    @Test
    public void test_DoEditProfile() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.NAME, NAME);
        param.put(LaoxiConstant.USER_ID, USER_ID);
        param.put(LaoxiConstant.FNAME, FIRST_NAME);

        CommonImplementation.getInstance().doEditProfile(param, TOKEN, IMG_PATH, new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(200, response.code());
            }

            @Override
            public void onFailure() {
                fail("DoEditProfile::onFailure");
            }
        });
    }

    @Test
    public void test_DoForgotPassword() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.EMAIL, EMAIL);
        CommonImplementation.getInstance().doForgotPassword(param, "", new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(200, response.code());
            }

            @Override
            public void onFailure() {
                fail("DoForgotPassword::onFailure");
            }
        });
    }

/*
    @Test
    public void test_DoNearFreeDriver() {
    }
*/

    @Test
    public void test_DoPlaceOrder() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.USER_ID, USER_ID);
        param.put(LaoxiConstant.CAR_TYPE_ID, CAR_TYPE_ID);
        param.put(LaoxiConstant.PICKUP_ADDRESS, PICKUP_ADDRESS);
        param.put(LaoxiConstant.PICKUP_LATITUDE, Double.toString(LAT));
        param.put(LaoxiConstant.PICKUP_LONGITUDE, Double.toString(LNG));
        param.put(LaoxiConstant.PICKUP_LONGITUDE, String.valueOf(LAOXI.currentRide.getPickLongitude()));
        param.put(LaoxiConstant.DROPOFF_ADDRESS, DROPOFF_ADDRESS);
        param.put(LaoxiConstant.DROPOFF_LATITUDE, Double.toString(DROPOFF_LAT));
        param.put(LaoxiConstant.DROPOFF_LONGITUDE, Double.toString(DROPOFF_LNG));
        param.put(LaoxiConstant.TRIP_TYPE, TRIP_TYPE);
        param.put(LaoxiConstant.TRIPDATETIME, "");
        param.put(LaoxiConstant.DRIVER_ID, DRIVER_ID);

        CommonImplementation.getInstance().doPlaceOrder(param, TOKEN, new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(201, response.code());
            }

            @Override
            public void onFailure() {
                fail("DoPlaceOrder::onFailure");
            }
        });
    }

    /*
        @Test
        public void test_DoFindDriver() {
        }

        @Test
        public void test_DoFareEstimation() {
        }

        @Test
        public void test_DoStateManagement() {
        }
    */
    @Test
    public void test_DoPastOrder() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.USER_ID, USER_ID);
        param.put(LaoxiConstant.USER_TYPE, "user");
        param.put(LaoxiConstant.PAGE, PAGE);

        CommonImplementation.getInstance().doPastOrder(param, TOKEN, new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(200, response.code());
            }

            @Override
            public void onFailure() {
                fail("DoPastOrder::onFailure");
            }
        });
    }

    @Test
    public void test_DoFutureOrder() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.USER_ID, USER_ID);
        param.put(LaoxiConstant.USER_TYPE, "user");
        param.put(LaoxiConstant.PAGE, PAGE);

        CommonImplementation.getInstance().doFutureOrder(param, TOKEN, new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(200, response.code());
            }

            @Override
            public void onFailure() {
                fail("DoFutureOrder::onFailure");
            }
        });
    }

    @Test
    public void test_DoCancelOrder() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.ID, USER_ID);
        param.put(LaoxiConstant.USER_TYPE, "user");
        param.put(LaoxiConstant.ORDER_ID, ORDER_ID);
        param.put(LaoxiConstant.CARD_TOKEN, CARD_TOKEN);
        CommonImplementation.getInstance().doCancelOrder(param, TOKEN, new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(200, response.code());
            }

            @Override
            public void onFailure() {
                fail("DoEditProfile::onFailure");
            }
        });
    }

    @Test
    public void test_DoLogOut() {
        HashMap<String, String> dataList = new HashMap<>();
        dataList.put(LaoxiConstant.USER_ID, USER_ID);

        CommonImplementation.getInstance().doLogOut(dataList, TOKEN, new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(200, response.code());
            }

            @Override
            public void onFailure() {
                fail("DoLogOut::onFailure");
            }
        });
    }

    @Test
    public void test_DoGetDriverDetails() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.DRIVER_ID, DRIVER_ID);

        CommonImplementation.getInstance().doGetDriverDetails(param, TOKEN, new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(200, response.code());
            }

            @Override
            public void onFailure() {
                fail("DoGetDriverDetails::onFailure");
            }
        });
    }

    @Test
    public void test_DoRateAndReview() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.ORDER_ID, ORDER_ID);
        param.put(LaoxiConstant.DRIVER_ID, DRIVER_ID);
        param.put(LaoxiConstant.USER_ID, USER_ID);
        param.put(LaoxiConstant.USER_TYPE, "user");
        param.put(LaoxiConstant.VOTES, VOTES);
        param.put("comment", COMMENT);

        CommonImplementation.getInstance().doRateAndReview(param, TOKEN, new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(201, response.code());
            }

            @Override
            public void onFailure() {
                fail("DoRateAndReview::onFailure");
            }
        });
    }

    @Test
    public void test_DoTrackRide() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.USER_ID, USER_ID);
        param.put(LaoxiConstant.USER_TYPE, "user");

        CommonImplementation.getInstance().doTrackRide(param, TOKEN, new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(200, response.code());
            }

            @Override
            public void onFailure() {
                fail("DoTrackRide::onFailure");
            }
        });
    }


    @Test
    public void test_DoPayment() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.ORDER_ID, ORDER_ID);
        param.put(LaoxiConstant.TIP, "0");
        param.put(LaoxiConstant.CARD_TOKEN, CARD_TOKEN);
        param.put(LaoxiConstant.ROUNDED_FARE, Float.toString(ROUNDED_FARE));

        CommonImplementation.getInstance().doPayment(param, TOKEN, new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(200, response.code());
            }

            @Override
            public void onFailure() {
                fail("DoPayment::onFailure");
            }
        });
    }

    /*
        @Test
        public void test_DoAddCard() {
        }
    */
    @Test
    public void test_DoEnterDropOff() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.ORDER_ID, ORDER_ID);
        param.put(LaoxiConstant.DROPOFF_ADDRESS, DROPOFF_ADDRESS);
        param.put(LaoxiConstant.DROPOFF_LATITUDE, Double.toString(DROPOFF_LAT));
        param.put(LaoxiConstant.DROPOFF_LONGITUDE, Double.toString(DROPOFF_LNG));

        CommonImplementation.getInstance().doEnterDropOff(param, TOKEN, new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(200, response.code());
            }

            @Override
            public void onFailure() {
                fail("DoEnterDropOff::onFailure");
            }
        });
    }

    @Test
    public void test_DoChangePassword() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.USER_ID, USER_ID);
        param.put(LaoxiConstant.OLD_PASSWORD, OLD_PWD);
        param.put(LaoxiConstant.NEW_PASSWORD, NEW_PWD);

        CommonImplementation.getInstance().doChangePassword(param, TOKEN, new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(200, response.code());
            }

            @Override
            public void onFailure() {
                fail("DoChangePassword::onFailure");
            }
        });
    }

    @Test
    public void test_DoGetFareEstimate() {
        HashMap<String, String> param = new HashMap<>();
        param.put("pickup_latitude", Double.toString(LAT));
        param.put("pickup_longitude", Double.toString(LNG));
        param.put("delivery_latitude", Double.toString(LAT));
        param.put("delivery_longitude", Double.toString(LNG));
        param.put("car_type_id", CAR_TYPE_ID);
        param.put("trip_type", TRIP_TYPE);

        CommonImplementation.getInstance().doGetFareEstimate(param, TOKEN, new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(200, response.code());
            }

            @Override
            public void onFailure() {
                fail("DoChangePassword::onFailure");
            }
        });
    }

    @Test
    public void test_WsCallSupport() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.USER_ID, USER_ID);
        param.put(LaoxiConstant.USER_TYPE, "user");
        param.put("name", NAME);
        param.put("email", EMAIL);
        param.put("message", SUPPORT_MSG);

        CommonImplementation.getInstance().wsCallSupport(param, TOKEN, new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(200, response.code());
            }

            @Override
            public void onFailure() {
                fail("wsCallSupport::onFailure");
            }
        });
    }

    @Test
    public void test_WsCallAddNewCard() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.USER_ID, USER_ID);
        param.put(LaoxiConstant.CVV, CCV);
        param.put(LaoxiConstant.CARD_HOLDER_NAME, CARD_HOLDER_NAME);
        param.put(LaoxiConstant.CARD_NUMBER, CARD_NUMBER);
        param.put(LaoxiConstant.MONTH, Integer.toString(6));
        param.put(LaoxiConstant.YEAR, Integer.toString(2017));
        param.put(LaoxiConstant.ADDRESS_LINE_1, ADDRESS_LINE_1);
        param.put(LaoxiConstant.CITY, CITY);
        param.put(LaoxiConstant.COUNTRY, COUNTRY);

        CommonImplementation.getInstance().wsCallAddNewCard(param, TOKEN, "", new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(201, response.code());
            }

            @Override
            public void onFailure() {
                fail("wsCallAddNewCard::onFailure");
            }
        });
    }

    @Test
    public void test_DoDeleteCard() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.USER_ID, USER_ID);
        param.put(LaoxiConstant.CARD_TOKEN, CARD_TOKEN);

        CommonImplementation.getInstance().doDeleteCard(param, TOKEN, new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(200, response.code());
            }

            @Override
            public void onFailure() {
                fail("doDeleteCard::onFailure");
            }
        });
    }

    @Test
    public void test_DoSms() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.PHONE, PHONE);
        param.put(LaoxiConstant.MESSAGE, SUPPORT_MSG);

        CommonImplementation.getInstance().doSms(param, TOKEN, "", new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(201, response.code());
            }

            @Override
            public void onFailure() {
                fail("doSms::onFailure");
            }
        });
    }

    @Test
    public void test_DoCarListLater() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.PICKUP_LATITUDE, Double.toString(LAT));
        param.put(LaoxiConstant.PICKUP_LONGITUDE, Double.toString(LNG));
        param.put(LaoxiConstant.CAR_TYPE, CAR_TYPE);
        param.put(LaoxiConstant.REGION, REGION);

        CommonImplementation.getInstance().doCarListLater(param, TOKEN, new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(200, response.code());
            }

            @Override
            public void onFailure() {
                fail("doCarListLater::onFailure");
            }
        });
    }

    @Test
    public void test_DoNearFreeDriverNew() {
        int requestTime = (int) (System.currentTimeMillis());
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.USER_ID, USER_ID);
        param.put(LaoxiConstant.LATITUDE, Double.toString(LAT));
        param.put(LaoxiConstant.LONGITUDE, Double.toString(LNG));
        param.put(LaoxiConstant.CAR_TYPE, CAR_TYPE);
        param.put(LaoxiConstant.REGION, REGION);

        CommonImplementation.getInstance().doNearFreeDriverNew(requestTime, param, TOKEN, new OnTaskCompleteNearFreeDriver() {
            @Override
            public void onSuccess(int time, Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(200, response.code());
            }

            @Override
            public void onFailure() {
                fail("doNearFreeDriverNew::onFailure");
            }
        });
    }

    @Test
    public void test_DoGetOrderDetails() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.ORDER_ID, ORDER_ID);
        param.put(LaoxiConstant.USER_ID, USER_ID);

        CommonImplementation.getInstance().doGetOrderDetails(param, TOKEN, new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(200, response.code());
            }

            @Override
            public void onFailure() {
                fail("doGetOrderDetails::onFailure");
            }
        });
    }

    @Test
    public void test_DoVerifyOTP() {
        HashMap<String, String> param = new HashMap<>();
        param.put("otp", "123456");
        param.put(LaoxiConstant.USER_ID, USER_ID);

        CommonImplementation.getInstance().doVerifyOTP(param, "", new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(200, response.code());
            }

            @Override
            public void onFailure() {
                fail("doGetOrderDetails::onFailure");
            }
        });
    }

    @Test
    public void test_DoResendOTP() {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.USER_ID, USER_ID);
        CommonImplementation.getInstance().doResendOTP(param, "", new OnTaskComplete() {
            @Override
            public void onSuccess(Response response, boolean success) {
                assertEquals(true, success);
                assertEquals(200, response.code());
            }

            @Override
            public void onFailure() {
                fail("doResendOTP::onFailure");
            }
        });
    }

}