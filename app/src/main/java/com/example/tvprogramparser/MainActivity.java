package com.example.tvprogramparser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    private final static int FRAGMENT_COUNT = 1;
    private final static String TAG = "dialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       BottomSheetFragment frag = BottomSheetFragment.newInstance(FRAGMENT_COUNT);
       frag.show(getSupportFragmentManager(), TAG);
    }

    public void onWatchProgramClick(View view) {
        Intent intent = new Intent(this, WatchProgramActivity.class);
        startActivity(intent);
    }

    public void onManageFavouritesClick(View view) {
        Intent intent = new Intent(this, ManageFavouritesActivity.class);
        startActivity(intent);
    }
}