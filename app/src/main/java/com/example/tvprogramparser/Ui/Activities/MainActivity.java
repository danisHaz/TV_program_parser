package com.example.tvprogramparser.Ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.motion.widget.MotionLayout;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.SharedPreferences;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.tvprogramparser.Background.RestartService;
import com.example.tvprogramparser.Components.FavouriteObject;
import com.example.tvprogramparser.Components.OnCompleteListener;
import com.example.tvprogramparser.Components.Program;
import com.example.tvprogramparser.Components.WorkDoneListener;
import com.example.tvprogramparser.Ui.Fragments.BottomSheetFragment;
import com.example.tvprogramparser.Components.FavouriteObjectsDB;
import com.example.tvprogramparser.Components.MainChannelsList;
import com.example.tvprogramparser.Ui.Fragments.ManageFavouritesFragment;
import com.example.tvprogramparser.R;
import com.example.tvprogramparser.TLS;
import com.example.tvprogramparser.Ui.Fragments.NoNetworkFragment;
import com.example.tvprogramparser.Ui.Fragments.ProgressFragment;

public class MainActivity extends AppCompatActivity {
    public MainActivity() {
        super(R.layout.activity_main);
    }
    private int defaultFragment = 0;
    public static boolean isNetworkActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isNetworkActive = TLS.isNetworkProvided(this);

        Toolbar customToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(customToolbar);

        FavouriteObjectsDB.createInstance(this);
        if (!isNetworkActive) {
            setWatchProgramFragment();
            return;
        }

        setProgressFragment();

        final MainActivity activity = this;

        WorkDoneListener.setNewListener(new OnCompleteListener() {
            @Override
            public void doWork(Bundle bundle) {
                activity.setDefaultFragment();
                SharedPreferences prefs
                        = getSharedPreferences(TLS.APPLICATION_PREFERENCES, MODE_PRIVATE);
                if (prefs.getInt(TLS.BACKGROUND_REQUEST_ID, 1) == 1) {
                    RestartService.scheduleAlarm(activity);
                }
            }
        }.setTag(TLS.GET_CHANNELS_LIST));
        MainChannelsList.define();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

//    Actually now there is only one item in menu: set to favourites
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final EditText texter = (EditText) item.getActionView();
        texter.setHint(R.string.addNewProgram);
        texter.setSingleLine(true);
        texter.requestFocus();
        final InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        texter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = texter.getText().toString();
                FavouriteObject.addToFavouritePrograms(new Program(str), MainActivity.this);
                ManageFavouritesFragment.createInstance().updateAndRefresh();
                texter.setText("");
                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                item.collapseActionView();
                Toast.makeText(MainActivity.this,
                        "Program is set to favourites",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return super.onOptionsItemSelected(item);
    }

    private void setDefaultFragment() {
        setWatchProgramFragment();
        setManageFavouritesFragment();
    }

    private void setProgressFragment() {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frag, ProgressFragment.createInstance(), TLS.PROGRESS_FRAGMENT)
                .commit();
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.secondFrag, ProgressFragment.createInstance(), TLS.PROGRESS_FRAGMENT)
                .commit();
    }

    private void setWatchProgramFragment() {
        if (!isNetworkActive) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.frag, NoNetworkFragment.createInstance(), TLS.WATCH_PROGRAM_TAG)
                    .commit();
            return;
        }

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frag, BottomSheetFragment.createInstance(), TLS.WATCH_PROGRAM_TAG)
                .commit();
    }

    private void setManageFavouritesFragment() {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.secondFrag, ManageFavouritesFragment.createInstance(), TLS.MANAGE_YOUR_FAVOURITES_TAG)
                .commit();
    }

    public void onWatchProgramClick(View view) {
        ToggleButton button = (ToggleButton) view;
        button.setCompoundDrawablesRelativeWithIntrinsicBounds(0,
                R.drawable.ic_baseline_list_enabled_24, 0, 0);

        ((MotionLayout)findViewById(R.id.main_layout)).transitionToState(R.id.mainList);

        ((ToggleButton)findViewById(R.id.manageFavourites)).setCompoundDrawablesRelativeWithIntrinsicBounds(0,
                R.drawable.ic_baseline_home_24, 0, 0);
    }

    public void onManageFavouritesClick(View view) {
        ToggleButton button = (ToggleButton) view;
        button.setCompoundDrawablesRelativeWithIntrinsicBounds(0,
                R.drawable.ic_baseline_home_enabled_24, 0, 0);

        ((MotionLayout)findViewById(R.id.main_layout)).transitionToState(R.id.favourites);

        ((ToggleButton)findViewById(R.id.watchProgram)).setCompoundDrawablesRelativeWithIntrinsicBounds(0,
                R.drawable.ic_baseline_list_24, 0, 0);
    }
}