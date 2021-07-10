package com.example.tvprogramparser.Background.alarms;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.WorkManager;
import androidx.work.Worker;

public abstract class WorkRequest {
    protected final WorkManager manager;
    protected final Constraints constraints;
    protected final Class<? extends Worker> workerClass;
    protected final String workTag;

    public WorkRequest(@NonNull Context context,
                       @NonNull Class<? extends Worker> cls,
                       String workTag) {
        manager = WorkManager.getInstance(context);
        constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(false)
                .setRequiresCharging(false)
                .build();

        this.workTag = workTag;
        workerClass = cls;
    }

    public abstract void setRequest();
}
