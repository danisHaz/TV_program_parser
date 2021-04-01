package com.example.tvprogramparser.Ui.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tvprogramparser.R;

public class NoNetworkFragment extends Fragment {
    private static volatile NoNetworkFragment local;


    public static NoNetworkFragment createInstance() {
        NoNetworkFragment fragment = local;
        if (fragment == null) {
            synchronized (NoNetworkFragment.class) {
                fragment = local;
                if (fragment == null)
                    fragment = local = new NoNetworkFragment();
            }
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_no_network, container, false);
    }
}