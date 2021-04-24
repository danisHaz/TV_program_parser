package com.example.tvprogramparser.Components;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.util.Log;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class InternalStorageManager {

    @Nullable
    public Bitmap getBitmapByPath(@NonNull String pathToBitmap, @NonNull String fileName) {
        Bitmap bitmap = null;
        try {
            File inputFile = new File(pathToBitmap, fileName);
            bitmap = BitmapFactory.decodeStream(new FileInputStream(inputFile));
        } catch (FileNotFoundException e) {
            Log.e("InternalStorageManager",
                    "FileNotFoundException: provided path to file is incorrect");
        }

        return bitmap;
    }

    @NonNull
    public String setBitmapToStorage(@NonNull Context context, @NonNull Bitmap bitmap, String fileName) {
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File dir = contextWrapper.getDir("MainChannelsIcons", Context.MODE_PRIVATE);
        File currentPath = new File(dir, fileName);
        FileOutputStream fostream = null;

        try {
            fostream = new FileOutputStream(currentPath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fostream);
        } catch (FileNotFoundException e) {
            Log.e("InternalStorageManager",
                    "FileNotFoundException: provided path to file is incorrect");
        } finally {
            try {
                fostream.close();
            } catch (java.io.IOException | java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }
        
        return dir.getAbsolutePath();
    }

}
