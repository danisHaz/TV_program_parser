package com.example.tvprogramparser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;
import android.content.Context;
import android.util.Log;

import java.lang.Thread;

public class MainActivity extends AppCompatActivity {

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            this.setWatchProgramFragment();
        }
        FavouriteObject.defineDb(this);
        MainChannelsList.define();

        // TODO: provide logic for scheduleJob
        RestartService.scheduleJob(this);
    }

    // TODO: rewrite createInstance
    private void setWatchProgramFragment() {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frag, BottomSheetFragment.createInstance(), TLS.WATCH_PROGRAM_TAG)
                .commit();
    }

    private void setManageFavouritesFragment() {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frag, ManageFavouritesFragment.createInstance(), TLS.MANAGE_YOUR_FAVOURITES_TAG)
                .commit();
    }

    public void onWatchProgramClick(View view) {
        this.setWatchProgramFragment();
    }

    public void onManageFavouritesClick(View view) {
        this.setManageFavouritesFragment();
    }
}