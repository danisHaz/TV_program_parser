package com.example.tvprogramparser;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.IBinder;
import java.util.ArrayList;

@SuppressLint("SpecifyJobSchedulerIdRange")
public class FavouriteJobService extends JobService {
    public FavouriteJobService() {
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Program> programList = new ArrayList<>();
                try {
                    programList = FavouriteObject.dailyProgramChecker(getApplicationContext());
                } catch (java.io.IOException e) {
                    return;
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
            }
        });

        thread.start();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
