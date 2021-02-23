package com.example.tvprogramparser;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.IBinder;
import android.app.Notification;
import android.widget.Toast;

public class FavouriteJobService extends JobService {
    public FavouriteJobService() {
    }

    @Override
    public boolean onStartJob(JobParameters params) {

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
