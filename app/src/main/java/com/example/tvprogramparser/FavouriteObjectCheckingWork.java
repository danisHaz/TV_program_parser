package com.example.tvprogramparser;

import androidx.annotation.NonNull;
import	androidx.work.Worker;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import android.content.Context;

import java.util.ArrayList;

public class FavouriteObjectCheckingWork extends Worker {
    FavouriteObjectCheckingWork(@NonNull Context context,
                                @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @Override
    @NonNull
    public ListenableWorker.Result doWork() {
        ArrayList<Program> programList = new ArrayList<>();
        try {
            programList = FavouriteObject.dailyProgramChecker(getApplicationContext());
        } catch (java.io.IOException e) {
            return ListenableWorker.Result.failure();
        }
        for (int i = 0; i < programList.size(); i++) {
            String channelId = "CHANNEL_ID_" + String.valueOf(i);
            String channelName = "CHANNEL_NAME_" + String.valueOf(i);
            String contentText = programList.get(i).getName() + " at " + programList.get(i).getTimeBegin();
            NotificationBuilder builder = new NotificationBuilder(getApplicationContext(),
                    R.mipmap.ic_launcher,
                    contentText,
                    channelId,
                    channelName
            );
            builder.setNotification();
        }

       return ListenableWorker.Result.success();
    }
}
