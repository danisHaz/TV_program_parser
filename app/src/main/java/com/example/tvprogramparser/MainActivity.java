package com.example.tvprogramparser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import android.widget.Toast;

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

        SharedPreferences prefs = getSharedPreferences(TLS.APPLICATION_PREFERENCES, MODE_PRIVATE);
        if (prefs.getInt(TLS.BACKGROUND_REQUEST_ID, 1) == 1) {

            RestartService.scheduleAlarm(this);
        }
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