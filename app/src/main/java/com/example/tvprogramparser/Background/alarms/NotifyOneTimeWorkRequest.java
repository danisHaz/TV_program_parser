package com.example.tvprogramparser.Background.alarms;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Worker;

public class NotifyOneTimeWorkRequest extends WorkRequest {
    final boolean uniqueRequest;
    final Data inputData;

    public NotifyOneTimeWorkRequest(@NonNull Context context,
                                    @NonNull Class<? extends Worker> cls,
                                    String workTag, boolean uniqueRequest) {
        super(context, cls, workTag);
        this.uniqueRequest = uniqueRequest;
        this.inputData = null;
    }

    public NotifyOneTimeWorkRequest(@NonNull Context context,
                                    @NonNull Class<? extends Worker> cls,
                                    @NonNull Data inputData,
                                    String workTag, boolean uniqueRequest) {
        super(context, cls, workTag);
        this.uniqueRequest = uniqueRequest;
        this.inputData = inputData;
    }

    @Override
    public void setRequest() {
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(workerClass)
                .addTag(workTag)
                .setInputData(inputData == null ? new Data.Builder().build() : inputData)
                .setConstraints(constraints)
                .build();

        if (!uniqueRequest) {
            manager.enqueue(request);
            return;
        }

        manager.enqueueUniqueWork(workTag, ExistingWorkPolicy.REPLACE, request);
    }
}
