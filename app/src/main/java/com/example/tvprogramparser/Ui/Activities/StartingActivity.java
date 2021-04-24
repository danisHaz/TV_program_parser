package com.example.tvprogramparser.Ui.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import com.example.tvprogramparser.Background.NotificationBuilder;
import com.example.tvprogramparser.Background.RestartService;
import com.example.tvprogramparser.Components.FavouriteObjectsDB;
import com.example.tvprogramparser.Components.MainChannelsList;
import com.example.tvprogramparser.Components.OnCompleteListener;
import com.example.tvprogramparser.Components.WorkDoneListener;
import com.example.tvprogramparser.R;
import com.example.tvprogramparser.TLS;
import com.example.tvprogramparser.Ui.Views.Point;
import com.example.tvprogramparser.Ui.Views.ReverseTriangleView;
import com.example.tvprogramparser.Ui.Views.TriangleView;

public class StartingActivity extends AppCompatActivity {
    public static boolean isNetworkActive;
    private Handler motionHandler = new Handler();
    private int friend = 0;
    TriangleView view;
    ReverseTriangleView reverseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Point.countScreenDimensions(this);
        setContentView(R.layout.activity_starting);
        onDefine();

        if (!isNetworkActive) {
//            provide with "No network" section
            return;
        }

        WorkDoneListener.setNewListener(new OnCompleteListener() {
            @Override
            public void doWork(Bundle bundle) {
                Intent toMainActivity = new Intent(StartingActivity.this, MainActivity.class);
                SharedPreferences prefs
                        = getSharedPreferences(TLS.APPLICATION_PREFERENCES, MODE_PRIVATE);
                if (prefs.getInt(TLS.BACKGROUND_REQUEST_ID, 0) == 0) {
                    RestartService.scheduleAlarm(StartingActivity.this);
                }

                StartingActivity.this.startActivity(toMainActivity);
            }
        }.setTag(TLS.GET_CHANNELS_LIST));
        MainChannelsList.define(this);
    }

    private void onDefine() {
        view = findViewById(R.id.back_view_triangle_1);
        view.setMyLayoutParams(view.point2.x, view.point2.y);
        reverseView = findViewById(R.id.back_view_triangle_2);
        reverseView.setMyLayoutParams(reverseView.point3.x, reverseView.point1.y);

        isNetworkActive = TLS.isNetworkProvided(this);
        FavouriteObjectsDB.createInstance(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        ((MotionLayout) findViewById(R.id.starting_layout)).transitionToState(R.id.end_position);
        final TextView loadingText = (TextView) findViewById(R.id.loading_view);
        motionHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (friend == 0) {
                        loadingText.setText(getResources().getString(R.string.loading));
                        friend = 1;
                    } else if (friend == 1) {
                        loadingText.setText(getResources().getString(R.string.loadingD));
                        friend = 2;
                    } else if (friend == 2) {
                        loadingText.setText(getResources().getString(R.string.loadingDD));
                        friend = 3;
                    } else if (friend == 3) {
                        loadingText.setText(getResources().getString(R.string.loadingDDD));
                        friend = 0;
                    }
                    motionHandler.postDelayed(this, 800);
                } catch (NullPointerException e) {
                    Log.w("StartingActivity", "Null exception when execute motion");
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        finish();
    }
}