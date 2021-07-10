package com.example.tvprogramparser.Components;

import com.example.tvprogramparser.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.lang.InterruptedException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.content.Context;

import androidx.annotation.NonNull;

public class Channel implements Serializable {
    private String name;
    private String link;
    private boolean favourite;
    private int id;
    private String pathToIcon;
    private final String fileName;

    public static class FavouriteChannels {
        private static boolean isDefined = false;

        private static void define(final Context context) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    favouriteChannels = FavouriteObjectsDB.createInstance(context)
                            .getAllFavouriteChannels();
                }
            });
            t.start();

            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public static ArrayList<Channel> favouriteChannels = new ArrayList<Channel>();

        public static ArrayList<String> getFavouriteChannelsNames(final Context context) {
            if (!isDefined)
                define(context);

            ArrayList<String> channels = new ArrayList<>();
            for (Channel chan: favouriteChannels) {
                channels.add(chan.getName());
            }
            return channels;
        }
    }

    public Channel(String name, String link) {
        this.name = name;
        this.link = link;
        this.favourite = false;

        this.id = Integer.parseInt(link.split("/")[3]);
        this.fileName = "icon_" + String.valueOf(id) + ".png";
    }

    public Channel(String name, String link, String pathToIcon) {
        this(name, link);
        this.pathToIcon = pathToIcon;
    }

    public String getName() { return name; }

    public String getLink() { return link; }

    public int getId() { return id; }

    public String getPathToIcon() { return pathToIcon; }

    public void setIcon(@NonNull Context context, @NonNull Bitmap icon) {
        InternalStorageManager storageManager = new InternalStorageManager();
        pathToIcon = storageManager.setBitmapToStorage(context, icon, fileName);
    }


    public Bitmap getIcon(Context context) {
        if (pathToIcon == null) {
            Log.e("Channel", "Path to icon is null when getting icon");
            return BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ic_baseline_tv_32);
        }

        return (new InternalStorageManager()).getBitmapByPath(pathToIcon, fileName);
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void addToFavouriteChannels(final Context context) {
        if (favourite) {
            Log.w("Channel", "Adding favourite channel to favourites");
            return;
        }

        favourite = true;
        FavouriteChannels.favouriteChannels.add(this);
//      Adding to db
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                FavouriteObjectsDB.createInstance(context)
                        .insertFavouriteChannel(Channel.this);
            }
        }).start();
    }

    public static void deleteFromFavouriteChannels(final int pos, final Context context) {
        if (!FavouriteChannels.favouriteChannels.get(pos).favourite) {
            Log.w("Channel", "Adding favourite channel to favourites");
            return;
        }

        FavouriteChannels.favouriteChannels.get(pos).favourite = false;
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                FavouriteObjectsDB.createInstance(context)
                        .deleteFavouriteChannel(FavouriteChannels.favouriteChannels.remove(pos));
                FavouriteChannels.favouriteChannels.remove(pos);
            }
        }).start();
    }

}
