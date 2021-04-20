package com.example.tvprogramparser.Components;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.lang.ArrayIndexOutOfBoundsException;

import java.lang.Thread;
import java.lang.InterruptedException;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

import com.example.tvprogramparser.TLS;

public class FavouriteObject {
    public static ArrayList<Channel> favouriteChannels = new ArrayList<Channel>();
    public static ArrayList<Program> favouritePrograms = new ArrayList<Program>();

    public static ArrayList<String> getArrayOfFavouriteChannels(Context context) {
        ArrayList<String> channels = new ArrayList<>();
        for (Channel chan: favouriteChannels) {
            channels.add(chan.getName());
        }
        return channels;
    }

    public static ArrayList<String> getArrayOfFavouritePrograms(Context context) {
        ArrayList<String> programs = new ArrayList<>();
        for (Program prog: favouritePrograms) {
            programs.add(prog.getName());
        }
        return programs;
    }

    public static void addToFavouriteChannels(Channel ch, final Context context) {
        favouriteChannels.add(ch);
        final int nId = ch.getId();
        final String nName = ch.getName();
        final String nLink = ch.getLink();

        // section 2
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                FavouriteObjectsDB.ChannelsDao chDao = FavouriteObjectsDB.createInstance(context).getDB().channelsDao();
                chDao.insertChannel(new FavouriteObjectsDB.Channel(nId, nName, nLink));
            }
        });

        thread2.start();

    }

    public static void addToFavouritePrograms(Program pr, final Context context) {
        for (int i = 0; i < favouritePrograms.size(); i++) {
            if (favouritePrograms.get(i).isEqual(pr)) {
                try {
                    Toast.makeText(context, "Program is already in favourites", Toast.LENGTH_SHORT).show();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        favouritePrograms.add(pr);
        final int nId = pr.getId();
        final String nName = pr.getName();

        // section 3
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                FavouriteObjectsDB.ProgramsDao progDao = FavouriteObjectsDB.createInstance(context).getDB().programsDao();
                progDao.insertProgram(new FavouriteObjectsDB.Program(nId, nName));
            }
        });

        thread2.start();

    }

    public static void deleteFromFavouriteChannels(int position, final Context context) {
        try {
            final int nId = favouriteChannels.get(position).getId();
            final String nName = favouriteChannels.get(position).getName();
            final String nLink = favouriteChannels.get(position).getLink();

            favouriteChannels.remove(position);

            Thread thready = new Thread(new Runnable() {
                @Override
                public synchronized void run() {
                    FavouriteObjectsDB.ChannelsDao chDao = FavouriteObjectsDB.createInstance(context).getDB().channelsDao();
                    chDao.delete(new FavouriteObjectsDB.Channel(nId, nName, nLink));
                }
            });

            thready.start();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFromFavouritePrograms(int position, final Context context) {
        try {
            final int nId = favouritePrograms.get(position).getId();
            final String nName = favouritePrograms.get(position).getName();

            favouritePrograms.remove(position);

            Thread thready = new Thread(new Runnable() {
                @Override
                public synchronized void run() {
                    FavouriteObjectsDB.ProgramsDao prDao = FavouriteObjectsDB.createInstance(context).getDB().programsDao();
                    prDao.delete(new FavouriteObjectsDB.Program(nId, nName));
                }
            });

            thready.start();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public static void parseFavouriteProgram(String programName, Context context) {
        try {
            addToFavouritePrograms(new Program(parseProgram(programName).split("    ")[1].trim()), context);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public static String parseProgram(String programName) {
        return programName.split("\\(")[0].trim();
    }

    // TODO: reorganize favourite lists to boost this method (maybe to Decart tree)
    public static boolean isProgramInFavourites(Context context, Program pr) {
        for (Program favouritePr: favouritePrograms) {
            if ((pr.getName().toLowerCase()).equals(favouritePr.getName().toLowerCase()))
                return true;
        }
        return false;
    }

    public static ArrayList<Program> dailyProgramChecker(final Context context) throws java.io.IOException {

        Channel[] channelArray;
        synchronized (MainChannelsList.class) {
            channelArray = MainChannelsList.getChannelsList();
        }
        final ArrayList<Program> result = new ArrayList<>();

        final Document[] doc = new Document[1];
        for (Channel channel: channelArray) {

            final String mainLink = TLS.MAIN_URL + channel.getLink();
            try {
                doc[0] = Jsoup.connect(mainLink).get();
            } catch (java.io.IOException e) {
                e.printStackTrace();
                doc[0] = null;
            }

            if (doc[0] == null)
                throw new IOException("BAD INTERNET CONNECTION");

            Elements firEls = null;
            try {
                firEls = doc[0].select(TLS.QUERY_1_3);
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
                break;
            }

            Elements secEls = null;
            try {
                secEls = doc[0].select(TLS.QUERY_1_2);
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
                break;
            };

            for (int i = 0; i < firEls.size(); i++) {

                Program curProg = new Program(
                        FavouriteObject.parseProgram(firEls.get(i).ownText())
                );

                if (FavouriteObject.isProgramInFavourites(context, curProg)) {
                    result.add(new Program(firEls.get(i).ownText(),
                            secEls.get(i).ownText()));
                }
            }

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
