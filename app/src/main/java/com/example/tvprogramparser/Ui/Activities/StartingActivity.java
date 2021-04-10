package com.example.tvprogramparser.Ui.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.motion.widget.MotionLayout;

import com.example.tvprogramparser.Background.RestartService;
import com.example.tvprogramparser.Components.FavouriteObjectsDB;
import com.example.tvprogramparser.Components.MainChannelsList;
import com.example.tvprogramparser.Components.OnCompleteListener;
import com.example.tvprogramparser.Components.WorkDoneListener;
import com.example.tvprogramparser.R;
import com.example.tvprogramparser.TLS;

public class StartingActivity extends AppCompatActivity {
    public StartingActivity() { super(R.layout.activity_starting); }
    public static boolean isNetworkActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isNetworkActive = TLS.isNetworkProvided(this);

        FavouriteObjectsDB.createInstance(this);
        if (!isNetworkActive) {
//            provide with "No network" section
            return;
        }

        ((MotionLayout) findViewById(R.id.starting_layout)).transitionToState(R.id.end_position);

        WorkDoneListener.setNewListener(new OnCompleteListener() {
            @Override
            public void doWork(Bundle bundle) {
                Intent toMainActivity = new Intent(StartingActivity.this, MainActivity.class);
                SharedPreferences prefs
                        = getSharedPreferences(TLS.APPLICATION_PREFERENCES, MODE_PRIVATE);
                if (prefs.getInt(TLS.BACKGROUND_REQUEST_ID, 1) == 1) {
                    RestartService.scheduleAlarm(StartingActivity.this);
                }

                StartingActivity.this.startActivity(toMainActivity);
            }
        }.setTag(TLS.GET_CHANNELS_LIST));
        MainChannelsList.define();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        finish();
    }
}