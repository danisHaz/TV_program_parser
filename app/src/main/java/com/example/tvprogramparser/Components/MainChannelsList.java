package com.example.tvprogramparser.Components;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.tvprogramparser.Background.HttpConnection;
import com.example.tvprogramparser.TLS;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class MainChannelsList {
    private static Channel[] channelsList;
    private static Bitmap[] imagesList;
    private static Document doc;
    private static boolean isDefined = false;

    public static void define() {
        if (!isDefined)
            receiveAllData();
        else {
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
    }

    public static Channel[] getChannelsList() {
        return channelsList;
    }
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
                    sec.get(i).attr("href"));
        }

        return channelsArray;
    }

    private static Bitmap[] getImagesArray(Channel[] channels) {
        Bitmap[] bitmaps = new Bitmap[channels.length];
        HttpConnection.ImageLoader loader = HttpConnection.ImageLoader.createInstance();

        for (int i = 0; i < channels.length; i++) {
            try {
                doc = Jsoup.connect(TLS.MAIN_URL + channels[i].getLink()).userAgent("Mozilla").get();
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
        }

        return bitmaps;
    }

    // todo: cache it!
    private static void receiveAllData() {
        isDefined = true;

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
        });

        newThread.setDaemon(true);

        newThread.start();
    }
}
