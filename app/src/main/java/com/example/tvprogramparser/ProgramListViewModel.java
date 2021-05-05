package com.example.tvprogramparser;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.graphics.Bitmap;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.example.tvprogramparser.Background.HttpConnection;
import com.example.tvprogramparser.Components.Channel;
import com.example.tvprogramparser.Components.OnCompleteListener;
import com.example.tvprogramparser.Components.WorkDoneListener;
import com.example.tvprogramparser.Ui.Activities.ManageFavouritesActivity;
import com.example.tvprogramparser.Ui.Fragments.BottomSheetFragment;

import org.jsoup.select.Elements;

import java.lang.NullPointerException;
import java.util.TreeMap;

public class ProgramListViewModel extends BaseObservable {
    public final ObservableBoolean isProgressBarEnabled = new ObservableBoolean(true);
    private final ObservableInt position = new ObservableInt();
    private Channel channel;
    public final ObservableField<String> channelName = new ObservableField<>();
    public final ObservableField<Drawable> channelImage = new ObservableField<>();
    public final ObservableField<ArrayAdapter<String>> listAdapter
            = new ObservableField<>();

    private final ManageFavouritesActivity activity;

    public ProgramListViewModel(@NonNull Context context,
                                @NonNull Intent intent) {
        if (!(context instanceof ManageFavouritesActivity)) {
            Log.e("ProgramListBinding",
                    "Binding to not ManageFavouritesActivity");
//            todo: notify app about not creating this viewmodel properly
            throw new RuntimeException();
        }
        activity = (ManageFavouritesActivity) context;
        getDataFromIntent(intent);

        if (channel == null || position.get() == -1) {
            Log.e("ProgramListViewModel",
                    "Data provided by activity is not valid");
            return;
        }

        setWorkDoneListener(context);
        loadChannelContent(context, channel);
    }

    public ArrayAdapter<String> getListAdapter() {
        return listAdapter.get();
    }

    private void getDataFromIntent(@NonNull Intent intent) {
        try {
            channel = (Channel) intent.getExtras()
                    .getSerializable(BottomSheetFragment.channelName);
            position.set(intent.getExtras()
                    .getInt(BottomSheetFragment.channelPosition));
        } catch (NullPointerException e) {
            Log.e("ProgramListViewModel",
                    "NullPointerException: provided intent is empty");
        }
    }

    private void loadChannelContent(@NonNull Context context,
                                    @NonNull Channel channel) {
        final String mainLink = TLS.MAIN_URL + channel.getLink();
        channelName.set(channel.getName());
        channelImage.set(new BitmapDrawable(context.getResources(), channel.getIcon(context)));

        final HttpConnection.JsoupDownloader downloader
                = HttpConnection.JsoupDownloader.createInstance();

//        start loading program schedule on new thread
        Thread t = new Thread(() -> {
            TreeMap<String, Elements> elements;
            try {
                elements = downloader.getDataByQueries(
                        mainLink,
                        new String[]{TLS.QUERY_1_2, TLS.QUERY_1_3},
                        true
                );
            } catch (RuntimeException e) {
                e.printStackTrace();
                return;
            }

            Elements _firEls = elements.get(TLS.QUERY_1_3);
            Elements _secEls = elements.get(TLS.QUERY_1_2);

            if (_firEls.size() != _secEls.size()) {
               Log.e("ProgramListViewModel",
                       "Data is not valid");
                WorkDoneListener.complete(
                        TLS.MANAGE_YOUR_FAVOURITES_TAG,
                        null,
                        OnCompleteListener.Result.FAILURE
                );
                return;
            }

            String[] strArr = new String[_firEls.size()];

            for (int i = 0; i < _firEls.size(); i++) {
                strArr[i] = _secEls.get(i).ownText()
                        + "    " + _firEls.get(i).ownText();
            }

            final Bundle bundle = new Bundle();
            bundle.putStringArray("strArr", strArr);
            activity.runOnUiThread(() -> {
                WorkDoneListener.complete(
                        TLS.MANAGE_YOUR_FAVOURITES_TAG,
                        bundle,
                        OnCompleteListener.Result.SUCCESS
                );
            });
        });
        t.setDaemon(true);
        t.start();
    }

    private void setWorkDoneListener(final Context context) {

        WorkDoneListener.setNewListener(new OnCompleteListener() {
            @Override
            public void doWork(@Nullable Bundle bundle) {
                if (bundle == null) {
                    Log.e("ProgramBinding",
                            "Bundle is null when doing work");

                    return;
                }
//          todo: try to find solution with binding adapter or smth else
                listAdapter.set(
                        new ArrayAdapter<String>(context,
                        android.R.layout.simple_list_item_1,
                        bundle.getStringArray("strArr"))
                );
                activity.setListViewAdapter(listAdapter.get());
                isProgressBarEnabled.set(false);
            }
        }.setTag(TLS.MANAGE_YOUR_FAVOURITES_TAG));
    }
}
