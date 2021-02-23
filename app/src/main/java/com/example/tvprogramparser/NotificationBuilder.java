package com.example.tvprogramparser;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

public class NotificationBuilder {
    private Context context;
    private int smallIcon;
    private String contentText;
    private String channelId;
    private String channelName;
    private static int notificationId;

    NotificationBuilder(Context context, int smallIcon, String contentText,
                        String channelId, String channelName) {
        this.contentText = contentText;
        this.context = context;
        this.channelId = channelId;
        this.smallIcon = smallIcon;
        this.channelName = channelName;
    }

    public void setNotification() {
        Notification.Builder builder = new Notification.Builder(context, "Channel id")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.contentTitle))
                .setContentText("Some text here");

        Notification notification = builder.build();

        NotificationChannel channel = new NotificationChannel("Channel id", "Channel name",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Channel description");

        NotificationManager manager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        manager.notify(notificationId++, notification);
    }

    public int getNotificationId() {
        return notificationId;
    }
}
