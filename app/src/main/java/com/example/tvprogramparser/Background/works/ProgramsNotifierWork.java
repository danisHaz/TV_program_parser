package com.example.tvprogramparser.Background.works;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.tvprogramparser.Background.NotificationBuilder;
import com.example.tvprogramparser.R;

public class ProgramsNotifierWork extends Worker {

    public ProgramsNotifierWork(@NonNull Context context,
                                @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    @NonNull
    public ListenableWorker.Result doWork() {
        Data inputData = getInputData();
        final String channelId = inputData.getString("channelId");
        final String channelName = inputData.getString("channelName");
        final String contentText = inputData.getString("contentText");

        new NotificationBuilder(
                getApplicationContext(),
                R.mipmap.ic_launcher,
                contentText,
                channelId,
                channelName
        ).setNotification();

        return ListenableWorker.Result.success();
    }
}
