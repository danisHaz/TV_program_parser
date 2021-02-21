package com.example.tvprogramparser;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;

import java.util.concurrent.TimeUnit;
import java.lang.Thread;

public class RestartService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            TimeUnit.MINUTES.sleep(1);
        } catch (java.lang.InterruptedException e) {
            e.printStackTrace();
        }

        context.startService(new Intent(context, CheckFavouritesService.class));
    }
}
