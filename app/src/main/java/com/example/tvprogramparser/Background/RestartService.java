package com.example.tvprogramparser.Background;

import android.annotation.SuppressLint;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.app.job.JobInfo;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Constraints;
import androidx.work.Worker;

import com.example.tvprogramparser.Background.alarms.AlarmScheduler;
import com.example.tvprogramparser.Background.works.FavouriteObjectCheckingWork;
import com.example.tvprogramparser.Background.works.ProgramsNotifierWork;
import com.example.tvprogramparser.TLS;

public class RestartService extends BroadcastReceiver {
    private static int currJobNum = 1;
    private static String workTag = "com.example.tvprogramparser.workTag";

    public RestartService() {}

    @Override
    public void onReceive(@NonNull final Context context, @NonNull Intent intent) {

        switch (intent.getAction()) {
            case "android.intent.action.BOOT_COMPLETED":
                scheduleAlarm(context);
                break;
            case TLS.ACTION_PERFORM_FAVOURITE:
                scheduleWork(context, FavouriteObjectCheckingWork.class, null);
                break;
            case TLS.PROGRAM_NOTIFIER_TAG:
                scheduleWork(context, ProgramsNotifierWork.class, intent.getExtras());
                break;
            default:
                Log.e("RestartService", "Unknown intent in broadcast receiver");
                break;
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

    @SuppressLint("DefaultLocale")
    public static void scheduleWork(@NonNull Context context,
                                    Class<? extends Worker> classType,
                                    @Nullable Bundle bundle) {
        WorkManager manager = WorkManager.getInstance(context);

        Constraints constrs = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(false)
                .setRequiresCharging(false)
                .build();

        Data data = null;
        if (bundle != null) {
            data = new Data.Builder()
                    .putString("channelId", bundle.getString("channelId"))
                    .putString("channelName", bundle.getString("channelName"))
                    .putString("contentText", bundle.getString("contentText"))
                    .build();

        } else if (classType == ProgramsNotifierWork.class) {
            Log.e("RestartService", "Trying to set notification, but no time provided");
            return;
        }

        OneTimeWorkRequest request = new OneTimeWorkRequest
                .Builder(classType)
                .addTag(String.format("com.example.tvprogramparser.workTag: %d", currJobNum))
                .setConstraints(constrs)
                .setInputData(data != null ? data : new Data.Builder().build())
                .build();

        manager.enqueueUniqueWork(String.format("checkFavourites: %d", currJobNum++),
                ExistingWorkPolicy.REPLACE,
                request);
    }

    public static void scheduleAlarm(Context context) {

        (new AlarmScheduler(context, RestartService.class, TLS.ACTION_PERFORM_FAVOURITE))
                .setAdditionalWork(() -> {
                    SharedPreferences.Editor prefs = context.getSharedPreferences(
                            TLS.APPLICATION_PREFERENCES,
                            Context.MODE_PRIVATE).edit();

                    prefs.putInt(TLS.BACKGROUND_REQUEST_ID, 1);
                    prefs.apply();
                })
                .setRepeating();

    }
}
