package com.example.tvprogramparser.Background.alarms;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.Worker;

import java.util.concurrent.TimeUnit;

public class NotifyPeriodicWorkRequest extends WorkRequest {
    private final long repeatInterval;
    private final boolean uniqueRequest;
    private final Data inputData;

    public NotifyPeriodicWorkRequest(@NonNull Context context,
                                     @NonNull Class<? extends Worker> cls,
                                     String workTag, long repeatIntervalInHours,
                                     boolean uniqueRequest) {
        super(context, cls, workTag);
        this.repeatInterval = repeatIntervalInHours;
        this.uniqueRequest = uniqueRequest;
        this.inputData = null;
    }

    public NotifyPeriodicWorkRequest(@NonNull Context context,
                                     @NonNull Class<? extends Worker> cls,
                                     @NonNull Data inputData,
                                     String workTag, long repeatIntervalInHours,
                                     boolean uniqueRequest) {
        super(context, cls, workTag);
        this.repeatInterval = repeatIntervalInHours;
        this.uniqueRequest = uniqueRequest;
        this.inputData = inputData;
    }

    @Override
    public void setRequest() {
        PeriodicWorkRequest request = new PeriodicWorkRequest
                .Builder(workerClass, repeatInterval, TimeUnit.HOURS)
                .addTag(workTag)
                .setInputData(inputData == null ? new Data.Builder().build() : inputData)
                .setConstraints(constraints)
                .build();

        if (!uniqueRequest) {
            manager.enqueue(request);
            return;
        }

        manager.enqueueUniquePeriodicWork(workTag, ExistingPeriodicWorkPolicy.REPLACE, request);
    }
}
