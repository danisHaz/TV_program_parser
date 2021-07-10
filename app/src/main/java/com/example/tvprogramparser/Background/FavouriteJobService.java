package com.example.tvprogramparser.Background;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;

import com.example.tvprogramparser.Components.FavouriteObject;
import com.example.tvprogramparser.Components.Program;
import com.example.tvprogramparser.R;

import java.util.ArrayList;

@SuppressLint("SpecifyJobSchedulerIdRange")
public class FavouriteJobService extends JobService {
    public FavouriteJobService() {
    }

    @Override
    public boolean onStartJob(JobParameters params) { return true; }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
