package com.dooplus.keng.tvsenate.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.dooplus.keng.tvsenate.R;
import com.dooplus.keng.tvsenate.SenateChannelLandingPage;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String TAG = "MyFirebaseMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        /*Log.d(TAG, "Message received ["+ remoteMessage +"]");
        Log.d(TAG, "Message received ["+ remoteMessage +"]");
        Log.d(TAG, "Message getMessageId ["+ remoteMessage.getMessageId() +"]");
        Log.d(TAG, "Message getData ["+ remoteMessage.getData().toString() +"]");
        Log.d(TAG, "Message toString ["+ remoteMessage.toString()+"]");
        Log.d(TAG, "Message getCollapseKey ["+ remoteMessage.getCollapseKey()+"]");
        Log.d(TAG, "Message getData().get(\"title\") ["+ remoteMessage.getData().get("title")+"]");
        Log.d(TAG, "Message getData().get(\"content-text\") ["+ remoteMessage.getData().get("content-text")+"]");
        Log.d(TAG, "Message getNotification().getTitle() ["+ remoteMessage.getNotification().getTitle()+"]");*/

        String data = remoteMessage.getData().toString().split("=")[1];
        String title = "";
        String body = "";
        try {
            JSONObject jObj = new JSONObject(data);
            title = jObj.optString("title");
            body = jObj.optString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Create Notification
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        Intent intent = new Intent(this, SenateChannelLandingPage.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(SenateChannelLandingPage.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        String channelID = "channelId";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.mipmap.ic_launcher)
                //.setContentTitle(remoteMessage.getNotification().getTitle())
                //.setContentText(remoteMessage.getNotification().getBody())
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        /*NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Title")
                .setContentText("Message")
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setLargeIcon(icon)
                .setColor(Color.RED)
                .setSmallIcon(R.mipmap.ic_launcher);

        //NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //notificationManager.notify(1, notificationBuilder.build());

        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setLights(Color.BLUE, 1000, 300);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());*/
    }
}
