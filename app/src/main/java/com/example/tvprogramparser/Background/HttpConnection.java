package com.example.tvprogramparser.Background;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

public class HttpConnection {

    public static class ImageLoader {
        private static volatile ImageLoader singleImageLoaderInstance = null;

        ImageLoader() { }

        public static ImageLoader createInstance() {
            ImageLoader local = singleImageLoaderInstance;
            if (local == null) {
                synchronized (ImageLoader.class) {
                    local = singleImageLoaderInstance;
                    if (local == null)
                        local = singleImageLoaderInstance = new ImageLoader();
                }
            }

            return local;
        }

        public Bitmap getBitmapFromUrl(String url) throws NullPointerException {
            URL newUrl = null;
            try {
                newUrl = new URL(url);
            } catch (MalformedURLException e) {
                Log.e("HttpConnection", "Cannot create URl from string in getBitmap");
            }
            return innerBitmapGetterFromURL(newUrl);
        }

        public Bitmap getBitmapFromUrl(@Nullable URL url) throws NullPointerException
        { return innerBitmapGetterFromURL(url); }

        private Bitmap innerBitmapGetterFromURL(@Nullable URL url) throws NullPointerException {
            Bitmap receivedBitmap = null;
            try {
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                receivedBitmap = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                Log.e("HttpConnection", "IOE when getting bitmap");
            } catch (NullPointerException e) {
                Log.e("HttpConnection", "Provided URL is null");
            }

            if (receivedBitmap == null)
                throw new NullPointerException();

            return receivedBitmap;
        }

    }

}
