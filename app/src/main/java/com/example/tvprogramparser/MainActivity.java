package com.example.tvprogramparser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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