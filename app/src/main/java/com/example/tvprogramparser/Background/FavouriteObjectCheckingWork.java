package com.example.tvprogramparser.Background;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import android.content.Context;
import android.os.Bundle;

import com.example.tvprogramparser.Components.MainChannelsList;
import com.example.tvprogramparser.Components.OnCompleteListener;
import com.example.tvprogramparser.Components.Program;
import com.example.tvprogramparser.Components.WorkDoneListener;
import com.example.tvprogramparser.R;
import com.example.tvprogramparser.TLS;

import java.util.ArrayList;

public class FavouriteObjectCheckingWork extends Worker {
    public FavouriteObjectCheckingWork(@NonNull Context context,
                                @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @Override
    @NonNull
    public ListenableWorker.Result doWork() {
        NotificationBuilder builder = new NotificationBuilder(
                getApplicationContext(),
                R.mipmap.ic_launcher,
                "Work is being done",
                TLS.DEFAULT_CHANNEL_ID,
                "ChannelNameEboy"
        );

        builder.setNotification();

        WorkDoneListener.setNewListener(new OnCompleteListener() {
            @Override
            public void doWork(Bundle bundle) {
                new Thread(() -> {
                    ArrayList<Program> programList = new ArrayList<>();
                    try {
                        programList = Program.FavouritePrograms
                                .dailyProgramChecker(getApplicationContext());
                    } catch (java.io.IOException e) {
                        // pass
                    }

                    for (int i = 0; i < programList.size(); i++) {
                        String channelId = TLS.DEFAULT_CHANNEL_ID;
                        String channelName = "CHANNEL_NAME_" + String.valueOf(i);
                        String contentText = programList.get(i).getName()
                                + " at " + programList.get(i).getTimeBegin();
                        NotificationBuilder builder = new NotificationBuilder(
                                getApplicationContext(),
                                R.mipmap.ic_launcher,
                                contentText,
                                channelId,
                                channelName
                        );
                        builder.setNotification();
                    }
                }).start();

            }
        }.setTag(TLS.DAILY_CHECKER_TAG));
        MainChannelsList.define(getApplicationContext());
        return ListenableWorker.Result.success();
    }
}
