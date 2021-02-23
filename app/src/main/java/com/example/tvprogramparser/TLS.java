package com.example.tvprogramparser;

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
    public static final String ARG_COUNT = "argCount";
    public static final String CURRENT_ARRAY_ID = "smallMenuId";
    public static final String CHOSEN_OBJECT_NAME = "chosenObjectName";
    public static final String CHOSEN_LAYOUT = "chosenLayout";
    public static final String ADD_TO_FAVOURITES = "addToFavourites";
    public static final String DELETE_FROM_FAVOURITE_PROGRAMS = "deleteFromPrograms";
    public static final String CHOSEN_POSITION = "chosenPosition";
    public static final String DELETE_FROM_FAVOURITE_CHANNELS = "deleteFromChannels";
    public static final String ACTION_WRITE_FAVOURITE = "actionWriteFavourite";
    public static final String ACTION_PERFORM_FAVOURITE = "com.example.tvprogramparser.ACTION_NAME";

    public static final ArrayList<Channel> ADDITIONAL_CHANNELS = new ArrayList<Channel>() {{
        add(new Channel("Paramount Comedy", "/kazan/channel/808/"));
        add(new Channel("KHL HD", "/kazan/channel/1590/"));
        add(new Channel("Kino TV", "/kazan/channel/1653/"));
    }};

    public static String favouriteProgramParser(String programName) {
        return programName.split("\\)")[0];
    }
}
