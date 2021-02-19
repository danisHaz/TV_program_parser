package com.example.tvprogramparser;

import androidx.room.Room;
import android.content.Context;

import java.util.ArrayList;
import java.lang.ArrayIndexOutOfBoundsException;

// TODO: get rid of favouriteLists
public class FavouriteObject {
    private static ArrayList<Channel> favouriteChannels = new ArrayList<Channel>();
    private static ArrayList<Program> favouritePrograms = new ArrayList<Program>();
    private static FavouriteObjectsDB.DefaultDb appDb;
    private static boolean isDbDefined = false;

    private static void defineDb(Context context) {
        appDb = Room.databaseBuilder(context, FavouriteObjectsDB.DefaultDb.class,
                "defDb").build();
        isDbDefined = true;
    }

    public static ArrayList<String> getArrayOfFavouriteChannels(Context context) {
        if (!isDbDefined)
            defineDb(context);

        FavouriteObjectsDB.ChannelsDao dao = appDb.channelsDao();
        ArrayList<FavouriteObjectsDB.Channel> restoredChannels = dao.getAll();
        ArrayList<String> channels = new ArrayList<>();
        for (Channel chan: favouriteChannels) {
            channels.add(chan.getName());
        }
        return channels;
    }

    public static ArrayList<String> getArrayOfFavouritePrograms(Context context) {
        if (!isDbDefined)
            defineDb(context);

        ArrayList<String> programs = new ArrayList<>();
        for (Program prog: favouritePrograms) {
            programs.add(prog.getName());
        }
        return programs;
    }

    public static void addToFavouriteChannels(Channel ch) {
        favouriteChannels.add(ch);
    }

    public static void addToFavouritePrograms(Program pr) {
        favouritePrograms.add(pr);
    }

    public static void deleteFromFavouriteChannels(int position) {
        try {
            favouriteChannels.remove(position);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFromFavouritePrograms(int position) {
        try {
            favouritePrograms.remove(position);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    static void parseFavouriteProgram(String programName) {
        String[] temp = programName.split("\\(")[0].trim().split("    ");
        try {
            addToFavouritePrograms(new Program(temp[1].trim()));
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
