package com.example.tvprogramparser.Components;

import android.util.Log;

import com.example.tvprogramparser.TLS;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

public class MainChannelsList {
    private static Channel[] channelsList;
    private static Document doc;
    private static boolean isDefined = false;

    public static void define() {
        if (!isDefined)
            receiveAllData();

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

                try {
                    WorkDoneListener.complete(TLS.GET_CHANNELS_LIST,
                            null,
                            OnCompleteListener.Result.SUCCESS);
                    WorkDoneListener.complete(TLS.DAILY_CHECKER_TAG,
                            null,
                            OnCompleteListener.Result.SUCCESS);
                } catch (NullPointerException e) {
                    Log.e("MainChannelsList", "Listener not found");
                }
            }
        });

        newThread.setDaemon(true);

        newThread.start();
    }
}
