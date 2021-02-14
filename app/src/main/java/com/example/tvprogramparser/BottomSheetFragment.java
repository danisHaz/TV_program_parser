package com.example.tvprogramparser;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

// TODO: add ListView.onItemClickListener
public class BottomSheetFragment extends Fragment {
    private static Document doc = null;

    // TODO: rewrite createInstance
    public static BottomSheetFragment createInstance() {
        BottomSheetFragment newFragment = new BottomSheetFragment();
        return newFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle onSavedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
    }

    private static Channel[] getChannelsArray(Elements fir, Elements sec) {
        Channel[] channelsArray = new Channel[fir.size()];
        for (int i = 0; i < fir.size(); i++) {
            channelsArray[i] = new Channel(fir.get(i).ownText(),
                    sec.get(i).attr("href"));
        }

//        for (int i = 0; i < TLS.ADDITIONAL_CHANNELS.size(); i++) {
//            channelsArray[i] = TLS.ADDITIONAL_CHANNELS.get(i);
//        }

        return channelsArray;
    }

    private static String[] getNamesArray(Channel[] arr) {
        String[] str = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            str[i] = arr[i].getName();
        }
        return str;
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {

            Thread newThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        doc = Jsoup.connect(TLS.LOCAL_MAIN_URL).get();
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
            Elements fir = doc.select(TLS.MAIN_QUERY);
            Elements sec = doc.select(TLS.QUERY_1_1);

            ListView list = (ListView) view.findViewById(R.id.list);

            final Channel[] ar = getChannelsArray(fir, sec);

            // TODO: handle NullPointerException from getActivity()
            ArrayAdapter<String> arr = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getNamesArray(ar));
            list.setAdapter(arr);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    Toast.makeText(getActivity(), ar[pos].getLink(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), ManageFavouritesActivity.class);
                    intent.putExtra("name", ar[pos]);
                    startActivity(intent);
                }
            });

        } else {
            Toast.makeText(getActivity(), "shitty shit", Toast.LENGTH_SHORT).show();
        }
    }
}