package com.example.tvprogramparser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.view.View;
import android.util.Log;

import java.lang.NullPointerException;
import java.lang.Thread;
import java.io.IOException;
import java.lang.InterruptedException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ManageFavouritesActivity extends AppCompatActivity {
    private Document doc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_favourites);

        Intent intent = getIntent();
        Channel ch = null;
        try {
            ch = (Channel) intent.getExtras().getSerializable("name");
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this,
                    "An error has occurred, please try later",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (ch == null)
            return;

        final String link = TLS.MAIN_URL + ch.getLink();

        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    doc = Jsoup.connect(link).get();
                } catch (IOException e) {
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

        Elements fir = doc.select(TLS.QUERY_1_3);
        Elements sec = doc.select(TLS.QUERY_1_2);

        if (fir.size() != sec.size()) {
            Toast.makeText(this,
                    "Something went wrong, please try later",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String[] strArr = new String[fir.size()];

        for (int i = 0; i < fir.size(); i++) {
            strArr[i] = sec.get(i).ownText() + "    " + fir.get(i).ownText();
        }

        ListView list = (ListView) findViewById(R.id.manageFavouritesList);

        ArrayAdapter<String> arr = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                strArr);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
                Bundle fragmentInfo = new Bundle();
                fragmentInfo.putInt(TLS.ARG_COUNT, getResources().getStringArray(R.array.favouriteProgramsMenu).length);
                fragmentInfo.putInt(TLS.CURRENT_ARRAY_ID, R.array.favouriteProgramsMenu);
                fragmentInfo.putString(TLS.CHOSEN_OBJECT_NAME, ((TextView)view).getText().toString());
                fragmentInfo.putString(TLS.CHOSEN_LAYOUT, TLS.ADD_TO_FAVOURITES);
                fragmentInfo.putInt(TLS.CHOSEN_POSITION, pos);

                SmallMenuFragment smallMenu = SmallMenuFragment.createInstance(fragmentInfo);
                smallMenu.show(getSupportFragmentManager(), "smallMenu");
            }
        });

        list.setAdapter(arr);
    }
}