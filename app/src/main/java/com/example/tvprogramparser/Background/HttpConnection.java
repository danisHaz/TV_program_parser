package com.example.tvprogramparser.Background;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
import java.util.TreeMap;
import java.util.List;

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
            HttpsURLConnection connection = null;
            try {
                connection = (HttpsURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                receivedBitmap = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                Log.e("HttpConnection", "IOE when getting bitmap");
            } catch (NullPointerException e) {
                Log.e("HttpConnection", "Provided URL is null");
                throw new NullPointerException();
            }

            if (connection != null)
                connection.disconnect();

            if (receivedBitmap == null)
                throw new NullPointerException();

            return receivedBitmap;
        }

    }

    public static class JsoupDownloader {
        private static volatile JsoupDownloader downloader;
        private Document doc;
//        not recommended to use `true` statement
        private boolean needsAllDownloaded;

        private JsoupDownloader() {

        }

        public static JsoupDownloader createInstance() {
            JsoupDownloader copy = downloader;
            if (copy == null) {
                synchronized (JsoupDownloader.class) {
                    copy = downloader;
                    if (copy == null)
                        copy = downloader = new JsoupDownloader();
                }
            }

            return copy;
        }

        public TreeMap<String, Elements> getDataByQueries(
                String link,
                List<String> queries,
                boolean needsAllDownloaded
        ) throws RuntimeException {

            this.needsAllDownloaded = needsAllDownloaded;
            try {
                doc = Jsoup.connect(link).get();
            } catch (IOException e) {
                Log.e("HttpConnection",
                        "IOException: downloading failed");
                if (needsAllDownloaded)
                    throw new RuntimeException();
                return new TreeMap<String, Elements>();
            }

            TreeMap<String, Elements> result = new TreeMap<>();
            int numOfFailedDownloads = 0;

            for (String query: queries) {
                try {
                    Elements el = doc.select(query);
                    result.put(query, el);
                } catch (NullPointerException e) {
                    if (needsAllDownloaded)
                        throw new RuntimeException();
                    numOfFailedDownloads++;
                }
            }

            if (numOfFailedDownloads != 0)
                Log.e("HttpConnection",
                        String.format(
                                "NullPointerException: %d failed queries",
                                numOfFailedDownloads
                        )
                );

            return result;
        }

        public TreeMap<String, Elements> getDataByQueries(URL url,
                                                          List<String> queries,
                                                          boolean needsAllDownloaded) {
            return getDataByQueries(url.toString(), queries, needsAllDownloaded);
        }

        public TreeMap<String, Elements> getDataByQueries(String link,
                                                          String[] queries,
                                                          boolean needsAllDownloaded) {
            return getDataByQueries(link, java.util.Arrays.asList(queries), needsAllDownloaded);
        }

        public TreeMap<String, Elements> getDataByQueries(URL url,
                                                          String[] queries,
                                                          boolean needsAllDownloaded) {
            return getDataByQueries(
                    url.toString(),
                    java.util.Arrays.asList(queries),
                    needsAllDownloaded
            );
        }

    }

}
