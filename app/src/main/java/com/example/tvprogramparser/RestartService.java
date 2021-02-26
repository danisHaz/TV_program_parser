package com.example.tvprogramparser;

import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.app.job.JobInfo;
import android.content.SharedPreferences;
import android.util.Log;
//import android.os.PersistableBundle;

import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkRequest;
import androidx.work.WorkManager;
import androidx.work.Constraints;

import java.util.concurrent.TimeUnit;

public class RestartService {
    private static int currJobNum = 1;
    private static int repeatIntervalInHours = 1;

    public RestartService() {}

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
        prefs.putInt(TLS.BACKGROUND_REQUEST_ID, 1);

        prefs.apply();
    }
}
