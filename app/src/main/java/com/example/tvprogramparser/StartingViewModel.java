package com.example.tvprogramparser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.tvprogramparser.Background.RestartService;
import com.example.tvprogramparser.Components.FavouriteObjectsDB;
import com.example.tvprogramparser.Components.MainChannelsList;
import com.example.tvprogramparser.Components.OnCompleteListener;
import com.example.tvprogramparser.Components.WorkDoneListener;
import com.example.tvprogramparser.Ui.Activities.MainActivity;
import com.example.tvprogramparser.Ui.Activities.StartingActivity;
import com.example.tvprogramparser.Ui.Views.Point;
import com.example.tvprogramparser.ViewModels.ActivityViewModel;

public class StartingViewModel extends ActivityViewModel<StartingActivity> {

    public StartingViewModel(@NonNull StartingActivity activity) {
        super(activity);
        Point.countScreenDimensions(activity);
        TLS.isNetworkProvided(activity);
    }

    private void setWork() {

        WorkDoneListener.setNewListener(new OnCompleteListener() {
            @Override
            public void doWork(Bundle bundle) {
                Intent toMainActivity = new Intent(activity, MainActivity.class);
                SharedPreferences prefs
                        = activity.getSharedPreferences(TLS.APPLICATION_PREFERENCES, Context.MODE_PRIVATE);
                if (prefs.getInt(TLS.BACKGROUND_REQUEST_ID, 0) == 0) {
                    RestartService.scheduleAlarm(activity);
                }

                activity.startActivity(toMainActivity);
            }
        }.setTag(TLS.GET_CHANNELS_LIST));
        MainChannelsList.define(activity);
    }

    @Override
    public void onStart() {
        SharedPreferences prefs = activity.getSharedPreferences(
                TLS.APPLICATION_PREFERENCES, Context.MODE_PRIVATE);

        if (!TLS.isNetworkProvidedCheck && !prefs.getBoolean(TLS.MAIN_CHANNELS_CACHE_STATE, false))
            return;

        activity.onDefineViews();
        FavouriteObjectsDB.createInstance(activity);
        setWork();

        if (prefs.getBoolean(TLS.MAIN_CHANNELS_CACHE_STATE, false))
            return;

        activity.performMotion();
    }
}
