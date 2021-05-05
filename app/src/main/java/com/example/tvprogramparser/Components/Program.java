package com.example.tvprogramparser.Components;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.tvprogramparser.TLS;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.lang.InterruptedException;
import java.util.concurrent.TimeUnit;

public class Program {
    private String programName;
    private String timeBegin;
    private String timeEnd;
    private String undefined = "undefined";
    private int id;
    private boolean favourite;

    public static class FavouritePrograms {
        public static ArrayList<Program> favouritePrograms = new ArrayList<Program>();
        private static boolean isDefined = false;

        private static void define(final Context context) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    favouritePrograms = FavouriteObjectsDB.createInstance(context)
                            .getAllFavouritePrograms();
                }
            });

            t.start();

            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public static ArrayList<String> getArrayOfFavouritePrograms(final Context context) {
            if (!isDefined)
                define(context);

            ArrayList<String> programs = new ArrayList<>();
            for (Program prog: favouritePrograms) {
                programs.add(prog.getName());
            }
            return programs;
        }

        public static ArrayList<Program> dailyProgramChecker(final Context context) throws java.io.IOException {
            if (!isDefined)
                define(context);

            Log.d("Program", "Program 1 point done");

            Channel[] channelArray;
            synchronized (MainChannelsList.class) {
                channelArray = MainChannelsList.getChannelsList();
            }

            final ArrayList<Program> result = new ArrayList<>();

            Log.d("Program", String.valueOf(channelArray.length));

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
                            Program.parseProgram(firEls.get(i).ownText())
                    );

                    if (curProg.isFavourite()) {
                        result.add(
                                new Program(firEls.get(i).ownText(),
                                secEls.get(i).ownText())
                        );
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

    // Called only for temporary needs
    public Program(String programName) {
        this.programName = programName;
        this.timeBegin = undefined;
        this.timeEnd = undefined;
        this.id = (new HashMaster(programName)).getHash();
        this.favourite = false;
    }

    public Program(String programName, String timeBegin) {
        this(programName);
        this.timeBegin = timeBegin;
    }

    public Program(String programName, String timeBegin, String timeEnd) {
        this(programName, timeBegin);
        this.timeEnd = timeEnd;
    }

    public void updateTimeBegin(String timeBegin) {
        this.timeBegin = timeBegin;
    }

    public boolean isEqual(Program pr) {
        return (pr.getId() == this.id);
    }

    public String getName() {
        return this.programName;
    }

    public int getId() {
        return this.id;
    }

    public String getTimeBegin() {
        return this.timeBegin;
    }

    public String getTimeEnd() {
        return this.timeEnd;
    }

    public boolean isFavourite() { return this.favourite; }

    public void addToFavouritePrograms(final Context context) {
        for (int i = 0; i < FavouritePrograms.favouritePrograms.size(); i++) {
            if (FavouritePrograms.favouritePrograms.get(i).isEqual(this)) {
                try {
                    Toast.makeText(context, "Program is already in favourites", Toast.LENGTH_SHORT).show();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        FavouritePrograms.favouritePrograms.add(this);
        favourite = true;

        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                FavouriteObjectsDB.createInstance(context)
                        .insertFavouriteProgram(Program.this);
            }
        }).start();
    }

    public static void deleteFromFavouritePrograms(final int pos, final Context context) {
        FavouritePrograms.favouritePrograms.get(pos).favourite = false;

        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                FavouriteObjectsDB.createInstance(context)
                        .deleteFavouriteProgram(FavouritePrograms.favouritePrograms.get(pos));

                FavouritePrograms.favouritePrograms.remove(pos);
            }
        }).start();
    }

    public static void parseFavouriteProgram(String name, Context context) {
        try {
            new Program(
                    parseProgram(name).split("    ")[1].trim()
            ).addToFavouritePrograms(context);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public static String parseProgram(String programName) {
        return programName.split("\\(")[0].trim();
    }
}
