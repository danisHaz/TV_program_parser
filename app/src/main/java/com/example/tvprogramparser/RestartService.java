package com.example.tvprogramparser;

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
import android.widget.Toast;
//import android.os.PersistableBundle;

import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Constraints;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.lang.Thread;

public class RestartService extends BroadcastReceiver {
    private static int currJobNum = 1;
    private static int repeatIntervalInHours = 1;
    private static String workTag = "com.example.tvprogramparser.workTag";

    public RestartService() {}

    @Override
    public void onReceive(final Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            NotificationBuilder builder = new NotificationBuilder(context,
                    R.mipmap.ic_launcher, "Reboot completed", "ChannelIDEBoy",
                    "ChannelNameEBoy");
            builder.setNotification();

            scheduleAlarm(context);
        } else if (intent.getAction().equals(TLS.ACTION_PERFORM_FAVOURITE)) {

            scheduleWork(context);
        }
    }

    public static void scheduleJob(Context context) {
        ComponentName jobService = new ComponentName(context, FavouriteJobService.class);
//        PersistableBundle bundle = new PersistableBundle();

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

        SharedPreferences.Editor prefs = context.getSharedPreferences(TLS.APPLICATION_PREFERENCES,
                Context.MODE_PRIVATE).edit();
        prefs.putInt(TLS.BACKGROUND_REQUEST_ID, 1);

        prefs.apply();
    }

    public static void testScheduleThread(final Context context) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Program> programList = new ArrayList<>();
                try {
                    programList = FavouriteObject.dailyProgramChecker(context);
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                    return;
                }
                for (int i = 0; i < programList.size(); i++) {
                    String channelId = "CHANNEL_ID_" + String.valueOf(i);
                    String channelName = "CHANNEL_NAME_" + String.valueOf(i);
                    String contentText = programList.get(i).getName() + " at " + programList.get(i).getTimeBegin();
                    NotificationBuilder builder = new NotificationBuilder(context,
                            R.mipmap.ic_launcher,
                            contentText,
                            channelId,
                            channelName
                    );
                    builder.setNotification();
                }
            }
        });

        thread.start();
        NotificationBuilder builder = new NotificationBuilder(context,
                R.mipmap.ic_launcher, "It works, shitty shit", "ChannelIDEBoy",
                "ChannelNameEBoy");
        builder.setNotification();
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
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 30);

        SharedPreferences.Editor prefs = context.getSharedPreferences(TLS.APPLICATION_PREFERENCES,
                Context.MODE_PRIVATE).edit();

        prefs.putInt(TLS.BACKGROUND_REQUEST_ID, 1);
        prefs.apply();

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_HALF_DAY, pendingIntent);

        Log.d("Eboy", "Eboyyyyy");
    }
}
