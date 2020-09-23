package com.driver.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;

import com.driver.R;
import com.driver.activity.HomeActivity;
import com.driver.activity.SplashScreenActivity;
import com.driver.application.LAOXI;
import com.driver.pojo.NotificationHandler;
import com.driver.util.AnalyticsReporter;
import com.driver.util.DebugLog;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

//modify by prakash

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    String msg = "";
    CountDownTimer waitTimer;

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
            Bundle properties = new Bundle();
            DebugLog.d(TAG + " From: " + remoteMessage.getFrom());
            properties.putString("from", remoteMessage.getFrom());

            if (remoteMessage.getNotification() != null) {
                DebugLog.d(TAG + "Message Notification Body: " + remoteMessage.getNotification().getBody());
                properties.putString("notification_body", remoteMessage.getNotification().getBody());
            }
            if (remoteMessage.getData().size() > 0) {
                DebugLog.d(TAG + "Message Data payload: " + remoteMessage.getData());
                properties.putString("data_payload", remoteMessage.getData().toString());
            } else {
                AnalyticsReporter.getInstance().track(AnalyticsReporter.Event.DRIVER_RECEIVED_FCM_MESSAGE, properties);
                return;
            }

            scheduleJob();

            AnalyticsReporter.getInstance().track(AnalyticsReporter.Event.DRIVER_RECEIVED_FCM_MESSAGE, properties);

            notificationHandler = new Gson().fromJson(remoteMessage.getData().get("message"), NotificationHandler.class);
            String message = notificationHandler.getMessage();

            if (LAOXI.currentActivity instanceof HomeActivity) {
                if (HomeActivity.activeornot) {
                    HomeActivity mActivity = (HomeActivity) LAOXI.currentActivity;
                    HomeActivity.openNotification = false;
                    mActivity.manageNotifications(notificationHandler);
                } else {
                    if (notificationHandler != null) {
                        if (notificationHandler.getFlag().equalsIgnoreCase("placeorder")) {
                            launchActivity();
                        } else {
                            sendNotificationOne(message);
                        }
                    }

                    HomeActivity.openNotification = true;
                    HomeActivity.setNotificationHandler(notificationHandler);
                }
            } else {
                if (notificationHandler != null) {
                    if (notificationHandler.getFlag().equalsIgnoreCase("placeorder")) {
                        launchActivity();
                    } else {
                        sendNotificationOne(message);
                    }
                }
                HomeActivity.openNotification = true;
                HomeActivity.setNotificationHandler(notificationHandler);
            }
        } catch (JsonSyntaxException e) {
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
            DebugLog.d(TAG + " Preparing to send notification...: " + msg);
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
            DebugLog.d(TAG + " Notification sent successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNotificationForNewOrder(String msg) {

        NotificationManager mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, HomeActivity.class), 0);
        HomeActivity.openNotification = false;

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
        note.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.new_sound_file);
        mNotificationManager.notify(2, note);
    }

    public void launchActivity() {
        Intent intent = new Intent(MyFirebaseMessagingService.this, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        MyFirebaseMessagingService.this.startActivity(intent);
    }
}