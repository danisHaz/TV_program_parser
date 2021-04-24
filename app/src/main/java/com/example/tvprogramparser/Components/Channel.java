package com.example.tvprogramparser.Components;

import com.example.tvprogramparser.Components.InternalStorageManager;
import com.example.tvprogramparser.R;

import java.io.Serializable;

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

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public int getId() {
        return id;
    }

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

}
