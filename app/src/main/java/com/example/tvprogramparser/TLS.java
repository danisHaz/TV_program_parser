package com.example.tvprogramparser;

import android.content.Context;

import com.example.tvprogramparser.Components.Channel;

import java.util.ArrayList;

public class TLS {
    public static final String WATCH_PROGRAM_TAG = "watchProgram";
    public static final String MANAGE_YOUR_FAVOURITES_TAG = "manageFavourites";
    public static final String LOCAL_MAIN_URL = "https://tv.mail.ru/kazan/";
    public static final String MAIN_URL = "https://tv.mail.ru";
    public static final String MAIN_QUERY = "a.p-channels__item__info__title__link";
    public static final String QUERY_1_1 = "a.p-channels__item__info__title__link[href]";
    public static final String QUERY_1_2 = "span.p-programms__item__time-value";
    public static final String QUERY_1_3 = "span.p-programms__item__name-link";
    public static final String QUERY_GET_IMAGES = "img.p-picture__image[src]";
    public static final String ARG_COUNT = "argCount";
    public static final String CURRENT_ARRAY_ID = "smallMenuId";
    public static final String CHOSEN_OBJECT_NAME = "chosenObjectName";
    public static final String CHOSEN_LAYOUT = "chosenLayout";
    public static final String ADD_TO_FAVOURITES = "addToFavourites";
    public static final String DELETE_FROM_FAVOURITE_PROGRAMS = "deleteFromPrograms";
    public static final String CHOSEN_POSITION = "chosenPosition";
    public static final String DELETE_FROM_FAVOURITE_CHANNELS = "deleteFromChannels";
    public static final String GET_CHANNELS_LIST = "getChannelsList";
    public static final String ACTION_PERFORM_FAVOURITE = "com.example.tvprogramparser.ACTION_NAME";
    public static final String APPLICATION_PREFERENCES = "com.example.tvprogramparser.APPLICATION_PREFERENCES";
    public static final String BACKGROUND_REQUEST_ID = "BACKGROUND_REQUEST_ID";
    public static final String DAILY_CHECKER_TAG = "dailyCheckerTag";
    public static final String PROGRESS_FRAGMENT = "progressFragment";

    public static final ArrayList<Channel> ADDITIONAL_CHANNELS = new ArrayList<Channel>() {{
        add(new Channel("Paramount Comedy", "/kazan/channel/808/"));
        add(new Channel("KHL HD", "/kazan/channel/1590/"));
        add(new Channel("Kino TV", "/kazan/channel/1653/"));
    }};

    public static String favouriteProgramParser(String programName) {
        return programName.split("\\)")[0];
    }

    public static boolean isNetworkProvided(Context context) {
        android.net.ConnectivityManager manager =
                (android.net.ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        for (android.net.Network network: manager.getAllNetworks()) {
            try {
                if (manager.getNetworkInfo(network).isConnected()) {
                    return true;
                }
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
