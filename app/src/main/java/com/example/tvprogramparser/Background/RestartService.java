package com.example.tvprogramparser.Background;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.app.job.JobInfo;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Constraints;

import com.example.tvprogramparser.R;
import com.example.tvprogramparser.TLS;

import java.util.Calendar;

public class RestartService extends BroadcastReceiver {
    private static int currJobNum = 1;
    private static String workTag = "com.example.tvprogramparser.workTag";

    public RestartService() {}

    @Override
    public void onReceive(final Context context, Intent intent) {
        NotificationBuilder builder = new NotificationBuilder(context,
                R.mipmap.ic_launcher, "Reboot completed", "ChannelIDEBoy",
                "ChannelNameEBoy");
        builder.setNotification();

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            scheduleAlarm(context);
        } else if (intent.getAction().equals(TLS.ACTION_PERFORM_FAVOURITE)) {
            scheduleWork(context);
        }
    }

    @Deprecated
    public static void scheduleJob(Context context) {
        ComponentName jobService = new ComponentName(context, FavouriteJobService.class);

        JobInfo.Builder jobBuilder = new JobInfo.Builder(currJobNum++, jobService)
                .setRequiresCharging(false)
                .setRequiresBatteryNotLow(false);

        JobScheduler jobScheduler
                = (JobScheduler) context.getSystemService(JobScheduler.class);

        int res = jobScheduler.schedule(jobBuilder.build());
        if (res == JobScheduler.RESULT_SUCCESS) {
            Log.d("tag", "success");
        } else {
            Log.d("tag", "failure");
        }
    }

    public static void scheduleWork(Context context) {
        WorkManager manager = WorkManager.getInstance(context);

        Constraints constrs = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(false)
                .setRequiresCharging(false)
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(FavouriteObjectCheckingWork.class)
                .addTag(workTag)
                .setConstraints(constrs)
                .build();

        manager.enqueueUniqueWork("checkFavourites",
                ExistingWorkPolicy.REPLACE,
                request);
    }

    public static void scheduleAlarm(Context context) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int requestCode = 12;
        int flags = 0;
        int perfectInterval = 1;

        Intent intent = new Intent(context, RestartService.class);
        intent.setAction(TLS.ACTION_PERFORM_FAVOURITE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode,
                intent, flags);

        try {
            manager.cancel(pendingIntent);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 45);

        SharedPreferences.Editor prefs = context.getSharedPreferences(TLS.APPLICATION_PREFERENCES,
                Context.MODE_PRIVATE).edit();

        prefs.putInt(TLS.BACKGROUND_REQUEST_ID, 1);
        prefs.apply();

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }
}
