package com.example.tvprogramparser.Background;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FavouriteObjectCheckingAlarm extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}
