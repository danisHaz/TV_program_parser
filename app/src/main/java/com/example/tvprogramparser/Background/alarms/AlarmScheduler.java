package com.example.tvprogramparser.Background.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.example.tvprogramparser.Components.Timer;
import com.example.tvprogramparser.TLS;

import java.util.Calendar;
/**
 * SOME ATTENTION!
 * This Alarm scheduler works only(!) in 'inexact repeating' mode
 * Add not repeating mode for sending notifications
 **/

public class AlarmScheduler {
    protected final Context context;
    protected final Class<? extends BroadcastReceiver> classType;
    protected static int requestCode = 0;
    protected static final int flags = 0;
    protected final AlarmManager manager;
    protected Runnable runnable;
    protected Bundle data;
    protected Calendar calendar;
    protected long intervalOfRepeating = AlarmManager.INTERVAL_DAY;
    protected boolean toRepeat = true;
//    vars to know when alarm should be scheduled
    protected int hours;
    protected int minutes;
    protected String action = TLS.ACTION_DEFAULT;

    public AlarmScheduler(@NonNull Context context,
                          @NonNull Class<? extends BroadcastReceiver> classType,
                          String action) {
        this.context = context;
        this.classType = classType;
        manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.action = action;
        this.hours = 7;
        this.minutes = 30;
    }

    public AlarmScheduler(@NonNull Context context,
                          @NonNull Class<? extends BroadcastReceiver> classType,
                          String action, boolean toRepeat) {
        this.context = context;
        this.classType = classType;
        manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.action = action;
        this.hours = 7;
        this.minutes = 30;
        this.toRepeat = toRepeat;
    }

    public AlarmScheduler(@NonNull Context context,
                          @NonNull Class<? extends BroadcastReceiver> classType,
                          String action,
                          int hours, int minutes) {
        this.context = context;
        this.classType = classType;
        manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.hours = hours;
        this.minutes = minutes;
        this.action = action;
    }

    public AlarmScheduler(@NonNull Context context,
                          @NonNull Class<? extends BroadcastReceiver> classType,
                          String action,
                          int hours, int minutes, boolean toRepeat) {
        this.context = context;
        this.classType = classType;
        manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.hours = hours;
        this.minutes = minutes;
        this.action = action;
        this.toRepeat = toRepeat;
    }

    public AlarmScheduler setAdditionalData(@NonNull Bundle bundle) {
        this.data = bundle;
        return this;
    }

    public AlarmScheduler setAdditionalWork(@NonNull Runnable runnable) {
        this.runnable = runnable;
        return this;
    }

    private void setCalendar() {
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        if (!toRepeat) {
            Pair<Integer, Integer> p = (new Timer(this.hours, this.minutes)).getPrettyEarlierTime();
            this.hours = p.first;
            this.minutes = p.second;
        }

        calendar.set(Calendar.HOUR_OF_DAY, this.hours);
        calendar.set(Calendar.MINUTE, this.minutes);
    }

    public void setRepeating() {
        if (runnable != null)
            runnable.run();

        setCalendar();

        if (!toRepeat) {
            Calendar currentDate = Calendar.getInstance();
            int currentHours = currentDate.get(Calendar.HOUR_OF_DAY);
            int currentMinutes = currentDate.get(Calendar.MINUTE);
            if (currentHours > hours || currentMinutes > minutes && currentHours == hours) {
                Log.i("AlarmScheduler", "Tried to set alarm with rotten time");
                return;
            }
        }

        Intent intent = new Intent(context, classType);
        intent.setAction(action);
        if (data != null)
            intent.putExtras(data);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode++,
                intent, flags);

//      This is for avoiding equal alarms (if they exist)
//        try {
//            manager.cancel(pendingIntent);
//        } catch (java.lang.Exception e) {
//            e.printStackTrace();
//        }

        if (toRepeat)
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    intervalOfRepeating, pendingIntent);
        else
            manager.set(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), pendingIntent);
    }
}
