package com.example.tvprogramparser;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     BottomSheetFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */

// TODO: add ListView.onItemClickListener
public class BottomSheetFragment extends Fragment {
    private static Document doc = null;
    private static String mainURL = "https://tv.mail.ru/kazan/";

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

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {

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
            String[] res = new String[fir.size()];
            for (int i = 0; i < fir.size(); i++) {
                res[i] = fir.get(i).ownText();
            }

            ListView list = view.findViewById(R.id.list);

            // TODO: handle NullPointerException from getActivity()
            ArrayAdapter<String> arr = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, res);
            list.setAdapter(arr);

        } else {
            Toast.makeText(getActivity(), "shitty shit", Toast.LENGTH_SHORT).show();
        }
    }
}