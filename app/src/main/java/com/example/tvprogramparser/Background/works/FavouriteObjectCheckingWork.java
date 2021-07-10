package com.example.tvprogramparser.Background.works;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import android.content.Context;
import android.os.Bundle;
import android.util.Pair;

import com.example.tvprogramparser.Background.RestartService;
import com.example.tvprogramparser.Background.alarms.AlarmScheduler;
import com.example.tvprogramparser.Components.MainChannelsList;
import com.example.tvprogramparser.Components.OnCompleteListener;
import com.example.tvprogramparser.Components.Program;
import com.example.tvprogramparser.Components.WorkDoneListener;
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

        WorkDoneListener.setNewListener(new OnCompleteListener() {
            @Override
            public void doWork(Bundle bundle) {
                new Thread(() -> {

                    ArrayList<Program> programList = new ArrayList<>();
                    try {
                        programList = Program.FavouritePrograms
                                .dailyProgramChecker(getApplicationContext());
                    } catch (java.io.IOException ignored) { }

//                    todo: schedule these all notifications to fire
//                      before 10 minutes program starts
                    for (int i = 0; i < programList.size(); i++) {
                        String channelId = TLS.DEFAULT_CHANNEL_ID;
                        String channelName = "CHANNEL_NAME_" + String.valueOf(i);
                        String contentText = programList.get(i).getName()
                                + " at " + programList.get(i).getTimeBegin();

                        Bundle additionalData = new Bundle();
                        additionalData.putString("channelId", channelId);
                        additionalData.putString("channelName", channelName);
                        additionalData.putString("contentText", contentText);

                        Pair<Integer, Integer> times =
                                programList.get(i).getParsedTimeBegin();
                        (new AlarmScheduler(
                                getApplicationContext(),
                                RestartService.class,
                                TLS.PROGRAM_NOTIFIER_TAG,
                                times.first, times.second,
                                false
                        )).setAdditionalData(additionalData).setRepeating();
                    }
                }).start();

            }
        }.setTag(TLS.DAILY_CHECKER_TAG));
        MainChannelsList.define(getApplicationContext());
        return ListenableWorker.Result.success();
    }
}
