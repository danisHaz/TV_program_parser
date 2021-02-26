package com.example.tvprogramparser;
import com.example.tvprogramparser.sampledata.FavouriteObjectCheckingAlarm;

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
//import android.os.PersistableBundle;

import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkRequest;
import androidx.work.WorkManager;
import androidx.work.Constraints;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class RestartService extends BroadcastReceiver {
    private static int currJobNum = 1;
    private static int repeatIntervalInHours = 1;

    public RestartService() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")
            || intent.getAction().equals(TLS.ACTION_PERFORM_FAVOURITE)) {
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

        WorkRequest request = new PeriodicWorkRequest.Builder(FavouriteObjectCheckingWork.class,
                repeatIntervalInHours,
                TimeUnit.HOURS)
                .setConstraints(constrs)
                .build();

        manager.enqueue(request);
        SharedPreferences.Editor prefs = context.getSharedPreferences(TLS.APPLICATION_PREFERENCES,
                Context.MODE_PRIVATE).edit();
        prefs.putInt(TLS.BACKGROUND_REQUEST_ID, 0);

        prefs.apply();
    }

    public static void scheduleAlarm(Context context) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int requestCode = 12;
        int flags = 0;

        Intent intent = new Intent(context, FavouriteObjectCheckingAlarm.class);
        intent.setAction(TLS.ACTION_PERFORM_FAVOURITE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode,
                intent, flags);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 30);

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
