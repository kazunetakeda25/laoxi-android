package com.rider.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rider.LAOXI;
import com.rider.R;
import com.rider.activity.HomeActivity;
import com.rider.activity.SplashScreenActivity;
import com.rider.pojo.NotificationHandler;
import com.rider.utils.DebugLog;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

   String msg="";


    public static final int NOTIFICATION_ID = 1;
    private NotificationHandler notificationHandler;


    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        try {
            // [START_EXCLUDE]
            // There are two types of messages data messages and notification messages. ListOfData messages are handled
            // here in onMessageReceived whether the app is in the foreground or background. ListOfData messages are the type
            // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
            // is in the foreground. When the app is in the background an automatically generated notification is displayed.
            // When the user taps on the notification they are returned to the app. Messages containing both notification
            // and data payloads are treated as notification messages. The Firebase console always sends notification
            // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
            // [END_EXCLUDE]

            // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

            DebugLog.d(TAG+ " From: " + remoteMessage.getFrom());

            // Check if message contains a data payload.
            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                DebugLog.d(TAG+ " Message Notification Body: " + remoteMessage.getNotification().getBody());

            }
            if (remoteMessage.getData().size() > 0) {
                DebugLog.d(TAG +  " Message " +"" +" payload: " + remoteMessage.getData());
            } else {
                return;
            }

            scheduleJob();

            notificationHandler = new Gson().fromJson(remoteMessage.getData().get("message"), NotificationHandler.class);
            String message = notificationHandler.getMessage();


            if (LAOXI.currentActivity instanceof HomeActivity) {

                if (HomeActivity.activeornot) {
                    HomeActivity mActivity = (HomeActivity) LAOXI.currentActivity;
                    HomeActivity.openNotificationFromPush = false;
                    mActivity.manageCancelTripPush(notificationHandler);
                } else {
                    sendNotificationOne(message);
                    HomeActivity.openNotificationFromPush = true;
                   if(((HomeActivity) LAOXI.currentActivity).cTimer!=null)
                   {
                       ((HomeActivity) LAOXI.currentActivity).cTimer.cancel();
                   }
                    HomeActivity.setNotificationHandler(notificationHandler);

                }
            } else {
                sendNotificationOne(message);
                HomeActivity.openNotificationFromPush = true;
                HomeActivity.setNotificationHandler(notificationHandler);

            }

            if(notificationHandler.getFlag().equalsIgnoreCase("arrived_pickup")){
                sendNotificationArrived(message);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void scheduleJob() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
    }

    private void sendNotificationOne(String msg) {
        try {
            DebugLog.d(TAG+ " Preparing to send notification...: " + msg);
            mNotificationManager = (NotificationManager) this
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, SplashScreenActivity.class), 0);
            // HomeActivity.openNotification = false;
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    this).setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(getString(R.string.app_name))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                    .setContentText(msg)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true);

            Notification note = mBuilder.build();
            note.defaults |= Notification.DEFAULT_VIBRATE;
            note.defaults |= Notification.DEFAULT_SOUND;
            //note.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.music);
            mNotificationManager.notify(2, note);
            DebugLog.d(TAG+ " Notification sent successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNotificationArrived(String msg) {
        try {
            DebugLog.d(TAG+ " Preparing to send notification...: " + msg);
            mNotificationManager = (NotificationManager) this
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, SplashScreenActivity.class), 0);
            // HomeActivity.openNotification = false;
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    this).setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(getString(R.string.app_name))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                    .setContentText(msg)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true);

            Notification note = mBuilder.build();
            note.defaults |= Notification.DEFAULT_VIBRATE;
//        note.defaults |= Notification.DEFAULT_SOUND;
            note.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.car_horn_onetime);
            mNotificationManager.notify(2, note);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}