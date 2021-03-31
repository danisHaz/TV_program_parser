package com.example.tvprogramparser.Ui.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;

import com.example.tvprogramparser.Components.FavouriteObject;
import com.example.tvprogramparser.Components.OnCompleteListener;
import com.example.tvprogramparser.Components.WorkDoneListener;
import com.example.tvprogramparser.R;
import com.example.tvprogramparser.TLS;

import java.util.ArrayList;

public class ManageFavouritesFragment extends Fragment {
    private static ManageFavouritesFragment local;

    public static ManageFavouritesFragment createInstance() {
        ManageFavouritesFragment newFragment = local;
        if (newFragment == null)  {
            synchronized (ManageFavouritesFragment.class) {
                newFragment = local;
                if (newFragment == null)
                    newFragment = local = new ManageFavouritesFragment();
            }
        }
        return newFragment;
    }

    public void updateAndRefresh() {
        try {
            FragmentTransaction transaction = getActivity()
                    .getSupportFragmentManager().beginTransaction();

            transaction.detach(local);
            transaction.attach(local);
            transaction.commit();
        } catch (NullPointerException e) {
            Log.e("ManageFavouritesFragment", "Error when refreshing");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_favourites, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            ArrayList<String> channels = FavouriteObject.getArrayOfFavouriteChannels(getContext());
            final ArrayList<String> programs = FavouriteObject.getArrayOfFavouritePrograms(getContext());

            // TODO: handle with null getActivity
            ArrayAdapter<String> adap1 = new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    channels
            );

            ArrayAdapter<String> adap2 = new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    programs
            );

            if (adap1.isEmpty())
                adap1.add(getResources().getString(R.string.channelsListEmpty));

            if (adap2.isEmpty())
                adap2.add(getResources().getString(R.string.programsListEmpty));

            final Bundle programsBundle = new Bundle();
            final Bundle channelsBundle = new Bundle();

            programsBundle.putInt(TLS.ARG_COUNT, 1);
            programsBundle.putInt(TLS.CURRENT_ARRAY_ID, R.array.favouriteProgramsBottomFragment);
            programsBundle.putString(TLS.CHOSEN_LAYOUT, TLS.DELETE_FROM_FAVOURITE_PROGRAMS);
            channelsBundle.putInt(TLS.ARG_COUNT, 1);
            channelsBundle.putInt(TLS.CURRENT_ARRAY_ID, R.array.favouriteChannelsBottomFragment);
            channelsBundle.putString(TLS.CHOSEN_LAYOUT, TLS.DELETE_FROM_FAVOURITE_CHANNELS);

            ListView channelsView = ((ListView)view.findViewById(R.id.favouriteChannelsList));
            channelsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    channelsBundle.putInt(TLS.CHOSEN_POSITION, pos);
                    SmallMenuFragment.createInstance(channelsBundle)
                            .show(getActivity().getSupportFragmentManager(), "manageFavourites");
                }
            });
            channelsView.setAdapter(adap1);
            ListView programsView = ((ListView)view.findViewById(R.id.favouriteProgramsList));
            programsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    programsBundle.putInt(TLS.CHOSEN_POSITION, pos);
                    WorkDoneListener.setNewListener(new OnCompleteListener() {
                        @Override
                        public void doWork(Bundle bundle) {
                            local.updateAndRefresh();
                        }
                    }.setTag(TLS.DELETE_FROM_FAVOURITE_PROGRAMS));
                    SmallMenuFragment.createInstance(programsBundle)
                            .show(getActivity().getSupportFragmentManager(), "manageFavourites");
                }
            });
            programsView.setAdapter(adap2);

        } else {
            Toast.makeText(getActivity(), "shitty shit", Toast.LENGTH_SHORT).show();
        }
    }
}