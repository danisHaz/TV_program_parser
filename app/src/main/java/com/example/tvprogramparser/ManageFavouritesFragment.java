package com.example.tvprogramparser;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ManageFavouritesFragment extends Fragment {

    public static ManageFavouritesFragment createInstance() {
        ManageFavouritesFragment frag = new ManageFavouritesFragment();
        return frag;
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
            ArrayList<String> channels = FavouriteObject.getArrayOfFavouriteChannels();
            ArrayList<String> programs = FavouriteObject.getArrayOfFavouritePrograms();

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

            ((ListView)view.findViewById(R.id.favouriteChannelsList)).setAdapter(adap1);
            ((ListView)view.findViewById(R.id.favouriteProgramsList)).setAdapter(adap2);
        } else {
            Toast.makeText(getActivity(), "shitty shit", Toast.LENGTH_SHORT).show();
        }
    }
}