package com.example.tvprogramparser.Components;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tvprogramparser.Background.HttpConnection;
import com.example.tvprogramparser.TLS;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainChannelsList extends Application {
    private static Channel[] channelsList;
    private static Bitmap[] imagesList;
    private static Document doc;
    private static boolean isDefined = false;

    public static void define(@NonNull final Context context) {
        if (isDefined) {
            notifyChannelsListInitDone();
            return;
        }

        SharedPreferences prefs =
                (context).getSharedPreferences(
                        TLS.APPLICATION_PREFERENCES,
                        MODE_PRIVATE
                );


        FavouriteObjectsDB myDb = FavouriteObjectsDB
                .createInstance((context).getApplicationContext());

        final FavouriteObjectsDB.MainChannelsDao mainDao = myDb.getDB().mainChannelsDao();

        if (prefs.getBoolean(TLS.MAIN_CHANNELS_CACHE_STATE, false)) {
            Thread tempThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    ArrayList<FavouriteObjectsDB.MainChannels> mainChannelsList
                            = (ArrayList<FavouriteObjectsDB.MainChannels>) mainDao.getAll();

                    channelsList = new Channel[mainChannelsList.size()];
                    int pos = 0;
                    for (FavouriteObjectsDB.MainChannels channel: mainChannelsList) {
                        channelsList[pos] = new Channel(channel.name, channel.link, channel.pathToIcon);
                        pos++;
                    }

                    notifyChannelsListInitDone();
                }
            });
            tempThread.start();
        } else {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(TLS.MAIN_CHANNELS_CACHE_STATE, true);
            editor.apply();

            receiveAllData(context);
//            todo: make explicit addition to db implicit
//                  by hiding logic in Channel and FavouriteObjectsDB classes
            WorkDoneListener.setNewListener(new OnCompleteListener() {
                @Override
                public void doWork(@Nullable Bundle bundle) {
                    for (Channel channel: channelsList) {
                        mainDao.insertChannel(new FavouriteObjectsDB.MainChannels(
                                channel.getId(),
                                channel.getName(),
                                channel.getLink(),
                                channel.getPathToIcon()));
                    }
                }
            }.setTag(TLS.MAIN_CHANNELS_INITIAL_TAG));
        }
    }

    private static void notifyChannelsListInitDone() {
        try {
            WorkDoneListener.complete(TLS.GET_CHANNELS_LIST,
                    null,
                    OnCompleteListener.Result.SUCCESS);
        } catch (NullPointerException e) {
//            pass
        }

        try {
            WorkDoneListener.complete(TLS.DAILY_CHECKER_TAG,
                    null,
                    OnCompleteListener.Result.SUCCESS);
        } catch (NullPointerException e) {
//            pass
        }
    }

    public static Channel[] getChannelsList() {
        return channelsList;
    }

    @Deprecated
    public static Bitmap[] getImagesList() { return imagesList; }

    private static Channel[] getChannelsArray(Elements fir, Elements sec) {
        Channel[] channelsArray = new Channel[
                fir.size() + TLS.ADDITIONAL_CHANNELS.size()];

        for (int i = 0; i < TLS.ADDITIONAL_CHANNELS.size(); i++) {
            channelsArray[i] = TLS.ADDITIONAL_CHANNELS.get(i);
        }

        for (int i = 0; i < fir.size(); i++) {
            channelsArray[i + TLS.ADDITIONAL_CHANNELS.size()] = new Channel(
                    fir.get(i).ownText(),
                    sec.get(i).attr("href")
            );
        }

        return channelsArray;
    }

    // todo: remove downloading images when doing daily stuff
    private static Bitmap[] getImagesArray(Channel[] channels) {
        Bitmap[] bitmaps = new Bitmap[channels.length];
        HttpConnection.ImageLoader loader = HttpConnection.ImageLoader.createInstance();

        for (int i = 0; i < channels.length; i++) {
            try {
                doc = Jsoup.connect(TLS.MAIN_URL + channels[i].getLink())
                        .userAgent("Mozilla").get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Elements elements = doc.select(TLS.QUERY_GET_IMAGES);
                if (elements.size() == 0) {
                    Log.d("size", "is 0 for " + channels[i].getName());
                    return new Bitmap[0];
                }

                bitmaps[i] = loader.getBitmapFromUrl((elements.get(0).attr("src")));
            } catch (NullPointerException e) {
                Log.e("MainChannelsList", "Doc is null");
                e.printStackTrace();
            }

//            Sleeping provided to avoid too frequent connections by Jsoup
//            try {
//                TimeUnit.SECONDS.sleep(100);
//            } catch (java.lang.InterruptedException e) {
//                e.printStackTrace();
//            }
        }

        return bitmaps;
    }

    // todo: cache it!
    private static void receiveAllData(final Context context) {
        isDefined = true;

        Log.d("Deb", "deb1");

        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    doc = Jsoup.connect(TLS.LOCAL_MAIN_URL).userAgent("Mozilla").get();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
                Elements fir = doc.select(TLS.MAIN_QUERY);
                Elements sec = doc.select(TLS.QUERY_1_1);

                channelsList = getChannelsArray(fir, sec);
                imagesList = getImagesArray(channelsList);

                Log.d("Deb", "Download is done");

                try {
                    for (int i = 0; i < channelsList.length; i++)
                        channelsList[i].setIcon(context, imagesList[i]);
                } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                notifyChannelsListInitDone();
                try {
                    WorkDoneListener.complete(
                            TLS.MAIN_CHANNELS_INITIAL_TAG,
                            null,
                            OnCompleteListener.Result.SUCCESS
                    );
                } catch (NullPointerException e) {
//            pass
                }
            }
        });

        newThread.start();
    }
}
