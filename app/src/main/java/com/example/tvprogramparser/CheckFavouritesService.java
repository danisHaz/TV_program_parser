package com.example.tvprogramparser;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.lang.Thread;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CheckFavouritesService extends Service {
    private Channel[] channelsArray;

    public CheckFavouritesService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        MainChannelsList.define();
        channelsArray = MainChannelsList.getChannelsList();
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int id) {
        final boolean[] curFlag = new boolean[1];

        for (final Channel ch: channelsArray) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Elements els = null;
                    try {
                        els = Jsoup.connect(ch.getLink()).get().select(TLS.QUERY_1_3);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        for (Element el : els) {
                            if (FavouriteObject.isProgramInFavourites(
                                    new Program(FavouriteObject.parseProgram(
                                            el.ownText())))) {
                                curFlag[0] = true;
                            }
                        }
                    } catch (java.lang.NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();

            try {
                thread.join();
            } catch (java.lang.InterruptedException e) {
                e.printStackTrace();
            }

            if (curFlag[0])
                break;
        }

        // TODO: create push-notification about upcoming favourite here

        return super.onStartCommand(intent, flag, id);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent restart = new Intent("RestartService");
        sendBroadcast(restart);
    }
}
