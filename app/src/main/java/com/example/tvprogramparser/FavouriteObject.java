package com.example.tvprogramparser;

import androidx.room.Room;
import android.content.Context;

import java.util.ArrayList;
import java.lang.ArrayIndexOutOfBoundsException;

import java.lang.Thread;
import java.lang.InterruptedException;

// TODO: rewrite thread sections ('cause they are SOOOOO stupid, guys)
public class FavouriteObject {
    private static ArrayList<Channel> favouriteChannels = new ArrayList<Channel>();
    private static ArrayList<Program> favouritePrograms = new ArrayList<Program>();
    private static FavouriteObjectsDB.DefaultDb appDb;
    private static boolean isDbDefined = false;

    private static void defineDb(Context context) {
        appDb = Room.databaseBuilder(context, FavouriteObjectsDB.DefaultDb.class,
                "defDb").build();

        // section 1
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                FavouriteObjectsDB.ChannelsDao chanDao = appDb.channelsDao();
                for (FavouriteObjectsDB.Channel ch: chanDao.getAll()) {
                    favouriteChannels.add(new Channel(ch.name, ch.link));
                }

                FavouriteObjectsDB.ProgramsDao progDao = appDb.programsDao();
                for (FavouriteObjectsDB.Program prog: progDao.getAll()) {
                    favouritePrograms.add(new Program(prog.name));
                }
            }
        });

        thread1.start();

        try {
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        isDbDefined = true;
    }

    public static ArrayList<String> getArrayOfFavouriteChannels(Context context) {
        if (!isDbDefined)
            defineDb(context);

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
        final int nId = ch.getId();
        final String nName = ch.getName();
        final String nLink = ch.getLink();

        // section 2
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                FavouriteObjectsDB.ChannelsDao chDao = appDb.channelsDao();
                chDao.insertChannel(new FavouriteObjectsDB.Channel(nId, nName, nLink));
            }
        });

        thread2.start();

    }

    public static void addToFavouritePrograms(Program pr) {
        favouritePrograms.add(pr);
        final int nId = pr.getId();
        final String nName = pr.getName();

        // section 3
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                FavouriteObjectsDB.ProgramsDao progDao = appDb.programsDao();
                progDao.insertProgram(new FavouriteObjectsDB.Program(nId, nName));
            }
        });

        thread2.start();


    }

    public static void deleteFromFavouriteChannels(int position) {
        try {
            final int nId = favouriteChannels.get(position).getId();
            final String nName = favouriteChannels.get(position).getName();
            final String nLink = favouriteChannels.get(position).getLink();

            favouriteChannels.remove(position);

            Thread thready = new Thread(new Runnable() {
                @Override
                public void run() {
                    FavouriteObjectsDB.ChannelsDao chDao = appDb.channelsDao();
                    chDao.delete(new FavouriteObjectsDB.Channel(nId, nName, nLink));
                }
            });

            thready.start();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFromFavouritePrograms(int position) {
        try {
            final int nId = favouritePrograms.get(position).getId();
            final String nName = favouritePrograms.get(position).getName();

            favouritePrograms.remove(position);

            Thread thready = new Thread(new Runnable() {
                @Override
                public void run() {
                    FavouriteObjectsDB.ProgramsDao prDao = appDb.programsDao();
                    prDao.delete(new FavouriteObjectsDB.Program(nId, nName));
                }
            });

            thready.start();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    static void parseFavouriteProgram(String programName) {
        try {
            addToFavouritePrograms(new Program(parseProgram(programName)));
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public static String parseProgram(String programName) {
        return programName.split("\\(")[0].trim().split("    ")[1].trim();
    }

    // TODO: reorganize favourite lists to boost this method (maybe to Decart tree)
    public static boolean isProgramInFavourites(Program pr) {
        for (Program favouritePr: favouritePrograms) {
            if (pr.isEqual(favouritePr))
                return true;
        }
        return false;
    }
}
