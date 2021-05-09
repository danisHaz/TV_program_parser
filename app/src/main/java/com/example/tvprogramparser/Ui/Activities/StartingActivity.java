package com.example.tvprogramparser.Ui.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import com.example.tvprogramparser.R;
import com.example.tvprogramparser.TLS;
import com.example.tvprogramparser.Ui.Views.ReverseTriangleView;
import com.example.tvprogramparser.Ui.Views.TriangleView;
import com.example.tvprogramparser.StartingViewModel;

public class StartingActivity extends AppCompatActivity {
    private Handler motionHandler = new Handler();
    private int friend = 0;
    private TriangleView view;
    private ReverseTriangleView reverseView;
    private StartingViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new StartingViewModel(this);
        int currentContentView = R.layout.activity_starting;
        SharedPreferences prefs
                = getSharedPreferences(TLS.APPLICATION_PREFERENCES, Context.MODE_PRIVATE);

        if (!TLS.isNetworkProvidedCheck && !prefs.getBoolean(TLS.MAIN_CHANNELS_CACHE_STATE, false))
            currentContentView = R.layout.fragment_no_network;

        setContentView(currentContentView);
    }

    public void onDefineViews() {
        view = findViewById(R.id.back_view_triangle_1);
        view.setMyLayoutParams(view.point2.x, view.point2.y);
        reverseView = findViewById(R.id.back_view_triangle_2);
        reverseView.setMyLayoutParams(reverseView.point3.x, reverseView.point1.y);
    }

//    todo: try to use data binding instead
    public void performMotion() {
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
    public void onStart() {
        super.onStart();
        viewModel.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        finish();
    }
}