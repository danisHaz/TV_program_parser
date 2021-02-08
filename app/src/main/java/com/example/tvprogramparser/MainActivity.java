package com.example.tvprogramparser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    public MainActivity() {
        super(R.layout.activity_main);
    }

    // TODO: rewrite createInstance
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            this.setWatchProgramFragment();
        }
    }

    private void setWatchProgramFragment() {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.frag, BottomSheetFragment.createInstance(), null)
                .commit();
    }

    public void onWatchProgramClick(View view) {
        this.setWatchProgramFragment();
    }

    public void onManageFavouritesClick(View view) {
        Intent intent = new Intent(this, ManageFavouritesActivity.class);
        startActivity(intent);
    }
}