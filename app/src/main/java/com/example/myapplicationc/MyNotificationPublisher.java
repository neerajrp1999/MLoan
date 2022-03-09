package com.example.myapplicationc;

import android.app.Notification ;
import android.app.NotificationChannel ;
import android.app.NotificationManager ;
import android.app.PendingIntent;
import android.content.BroadcastReceiver ;
import android.content.Context ;
import android.content.Intent ;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

public class MyNotificationPublisher extends BroadcastReceiver {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    public static String NOTIFICATION_ID = "notification-id" ;
    public static String NOTIFICATION = "notification" ;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String text = bundle.getString("event");
        String date = bundle.getString("date") + " " + bundle.getString("time");
        String username_userid="ID:"+bundle.getInt("u_id") + "\nName: " + bundle.getString("u_name");

        //Click on Notification
        Intent intent1 = new Intent(context, UserDash.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent1.putExtra("id", bundle.getInt("u_id"));
        intent1.putExtra("active", 1);

        //Builder
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent1, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notify_001");

        //properties
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.
                notification_alarm_layout);
        contentView.setImageViewResource(R.id.image, R.drawable.ic_action_notify);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        contentView.setOnClickPendingIntent(R.id.flashButton, pendingSwitchIntent);
        contentView.setTextViewText(R.id.message, text);
        contentView.setTextViewText(R.id.userid_username, username_userid);
        mBuilder.setSmallIcon(R.drawable.ic_action_notify);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(true);
        //mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.build().flags = Notification.FLAG_ONLY_ALERT_ONCE | Notification.PRIORITY_HIGH;
        mBuilder.setContent(contentView);
        mBuilder.setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_id";
            NotificationChannel channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }
        Notification notification = mBuilder.build();
        notificationManager.notify(bundle.getInt("u_n_id"), notification);
        //notificationManager.cancel(1);
    }

}