package com.example.tvprogramparser.Ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.EditText;

import com.example.tvprogramparser.Background.RestartService;
import com.example.tvprogramparser.Components.OnCompleteListener;
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
                SharedPreferences prefs = getSharedPreferences(TLS.APPLICATION_PREFERENCES, MODE_PRIVATE);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        EditText texter = (EditText) item.getActionView();
        texter.setText("Texter text set");
        return super.onOptionsItemSelected(item);
    }

    private void setDefaultFragment() {
        switch (defaultFragment) {
            case 0:
                setWatchProgramFragment();
                break;
            case 1:
                setManageFavouritesFragment();
                break;
            default:
                Log.d("MainActivity", "Undefined default fragment id");
        }
    }

    private void setProgressFragment() {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frag, ProgressFragment.createInstance(), TLS.PROGRESS_FRAGMENT)
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
                .replace(R.id.frag, ManageFavouritesFragment.createInstance(), TLS.MANAGE_YOUR_FAVOURITES_TAG)
                .commit();
    }

    public void onWatchProgramClick(View view) {
        if (WorkDoneListener.isListenerSet(TLS.GET_CHANNELS_LIST))
            return;

        this.setWatchProgramFragment();
    }

    public void onManageFavouritesClick(View view) {
        if (WorkDoneListener.isListenerSet(TLS.GET_CHANNELS_LIST))
            return;

        this.setManageFavouritesFragment();
    }
}