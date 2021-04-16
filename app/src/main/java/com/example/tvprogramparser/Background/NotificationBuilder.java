package com.example.tvprogramparser.Background;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import com.example.tvprogramparser.R;

public class NotificationBuilder {
    private Context context;
    private int smallIcon;
    private String contentText;
    private String channelId;
    private String channelName;
    private static int notificationId = 0;

    public NotificationBuilder(Context context, int smallIcon, String contentText,
                        String channelId, String channelName) {
        this.contentText = contentText;
        this.context = context;
        this.channelId = channelId;
        this.smallIcon = smallIcon;
        this.channelName = channelName;
    }

    public void setNotification() {
        Notification.Builder builder = new Notification.Builder(context, channelId)
                .setSmallIcon(smallIcon)
                .setContentTitle(context.getString(R.string.contentTitle))
                .setContentText(contentText)
                .setStyle(new Notification.BigTextStyle().bigText(contentText));

        Notification notification = builder.build();

        NotificationChannel channel = new NotificationChannel(channelId, channelName,
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
