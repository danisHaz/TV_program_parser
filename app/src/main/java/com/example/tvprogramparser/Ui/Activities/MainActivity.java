package com.example.tvprogramparser.Ui.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.tvprogramparser.Background.NotificationBuilder;
import com.example.tvprogramparser.Components.FavouriteObject;
import com.example.tvprogramparser.Components.Program;
import com.example.tvprogramparser.Ui.Fragments.BottomSheetFragment;
import com.example.tvprogramparser.Ui.Fragments.ManageFavouritesFragment;
import com.example.tvprogramparser.R;
import com.example.tvprogramparser.TLS;
import com.example.tvprogramparser.Ui.Fragments.ProgressFragment;

public class MainActivity extends AppCompatActivity {
    public MainActivity() {
        super(R.layout.activity_main);
    }
    private int defaultFragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar customToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(customToolbar);

        setDefaultFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

//    Actually now there is only one item in menu: set to favourites
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(final MenuItem menuItem) {
                final EditText texter = (EditText) menuItem.getActionView();
                texter.setHint(R.string.addNewProgram);
                texter.setSingleLine(true);
                texter.requestFocus();
                final InputMethodManager inputManager
                        = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                texter.setImeOptions(EditorInfo.IME_ACTION_GO);
                texter.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
                    if (actionId == EditorInfo.IME_ACTION_GO) {
                        String str = texter.getText().toString();
                        (new Program(str)).addToFavouritePrograms(MainActivity.this);
                        ManageFavouritesFragment.createInstance().updateAndRefresh();
                        texter.setText("");
                        inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        menuItem.collapseActionView();
                        Toast.makeText(MainActivity.this,
                                "Program is set to favourites",
                                Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    return false;
                });
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                final EditText texter = (EditText) menuItem.getActionView();
                texter.setText("");
                final InputMethodManager inputManager
                        = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                return true;
            }
        });

        return super.onOptionsItemSelected(item);
    }

    private void setDefaultFragment() {
        setWatchProgramFragment();
        setManageFavouritesFragment();
    }

    private void changeFragment(int fragmentId, Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(fragmentId, fragment, tag)
                .commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("MainActivity", "Activity started");
    }

    private void setProgressFragment() {
        changeFragment(R.id.frag, ProgressFragment.createInstance(), TLS.PROGRESS_FRAGMENT);
        changeFragment(R.id.secondFrag, ProgressFragment.createInstance(), TLS.PROGRESS_FRAGMENT);
    }

    private void setWatchProgramFragment() {
        changeFragment(R.id.frag, BottomSheetFragment.createInstance(), TLS.WATCH_PROGRAM_TAG);
    }

    private void setManageFavouritesFragment() {
        changeFragment(
                R.id.secondFrag,
                ManageFavouritesFragment.createInstance(),
                TLS.MANAGE_YOUR_FAVOURITES_TAG
        );
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