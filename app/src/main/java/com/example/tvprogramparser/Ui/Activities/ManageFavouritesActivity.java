package com.example.tvprogramparser.Ui.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.view.View;

import com.example.tvprogramparser.Components.Channel;
import com.example.tvprogramparser.Components.OnCompleteListener;
import com.example.tvprogramparser.Components.WorkDoneListener;
import com.example.tvprogramparser.R;
import com.example.tvprogramparser.Ui.Fragments.SmallMenuFragment;
import com.example.tvprogramparser.TLS;

import java.lang.NullPointerException;
import java.lang.Thread;
import java.io.IOException;

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
        final ManageFavouritesActivity local = this;
        final ListView list = (ListView) findViewById(R.id.manageFavouritesList);
        final ProgressBar progressBar
                = (ProgressBar) findViewById(R.id.loading_while_favourites);

        progressBar.setVisibility(ProgressBar.VISIBLE);

        WorkDoneListener.setNewListener(new OnCompleteListener() {
            @Override
            public void doWork(Bundle bundle) {
                if (bundle == null) {
                    Log.e("ManageFavouritesActivity", "Bundle is null when doing work");
                    return;
                }

                progressBar.setVisibility(View.GONE);
                ArrayAdapter<String> arr = new ArrayAdapter<String>(local,
                        android.R.layout.simple_list_item_1,
                        bundle.getStringArray("strArr"));

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
                        WorkDoneListener.setNewListener(new OnCompleteListener() {
                            @Override
                            public void doWork(Bundle bundle) {
                                Toast.makeText(getApplicationContext(), "Program is set to favorites",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }.setTag(TLS.ADD_TO_FAVOURITES));
                        smallMenu.show(getSupportFragmentManager(), "smallMenu");
                    }
                });

                list.setAdapter(arr);
            }
        }.setTag(TLS.MANAGE_YOUR_FAVOURITES_TAG));

        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    doc = Jsoup.connect(link).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Elements fir = doc.select(TLS.QUERY_1_3);
                Elements sec = doc.select(TLS.QUERY_1_2);

                if (fir.size() != sec.size()) {
                    Log.e("ManageFavouritesActivity", "Something went wrong");
                    WorkDoneListener.complete(TLS.MANAGE_YOUR_FAVOURITES_TAG,
                            null,
                            OnCompleteListener.Result.FAILURE);
                    return;
                }

                String[] strArr = new String[fir.size()];

                for (int i = 0; i < fir.size(); i++) {
                    strArr[i] = sec.get(i).ownText() + "    " + fir.get(i).ownText();
                }

                final Bundle bundle = new Bundle();
                bundle.putStringArray("strArr", strArr);

                local.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WorkDoneListener.complete(TLS.MANAGE_YOUR_FAVOURITES_TAG,
                                    bundle,
                                    OnCompleteListener.Result.SUCCESS);
                        } catch (NullPointerException e) {
//                    pass
                        }
                    }
                });
            }
        });
        newThread.start();

    }
}