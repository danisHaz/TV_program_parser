package com.example.tvprogramparser;

import java.util.ArrayList;

public class TLS {
    public static final String WATCH_PROGRAM_TAG = "watchProgram";
    public static final String MANAGE_YOUR_FAVOURITES_TAG = "manageFavourites";
    public static final String MAIN_URL = "https://tv.mail.ru/kazan/";
    public static final String MAIN_QUERY = "a.p-channels__item__info__title__link";
    public static final String QUERY_1_1 = "a.p-channels__item__info__title__link[href]";
    public static final ArrayList<Channel> ADDITIONAL_CHANNELS = new ArrayList<Channel>() {{
        add(new Channel("Paramount Comedy", "https://tv.mail.ru/kazan/channel/808/"));
    }};
}
