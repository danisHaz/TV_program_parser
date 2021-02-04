package com.example.tvprogramparser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
//import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.Thread;
import java.lang.Runnable;
import java.lang.InterruptedException;

public class WatchProgramActivity extends AppCompatActivity {
    static Document doc = null;
    static String title = "jopa";
    static String mainURL = "https://tv.mail.ru/kazan/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    doc = Jsoup.connect(mainURL).get();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        });

        newThread.start();

        try {
            newThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Elements fir = doc.select("a.p-channels__item__info__title__link");
//        int res = fir.size();
        String[] res = new String[fir.size()];
        for (int i = 0; i < fir.size(); i++) {
            res[i] = fir.get(i).ownText();
        }

        ListView list = (ListView)findViewById(R.id.mainList);
        ArrayAdapter<String> arr = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, res);
        list.setAdapter(arr);
    }
}