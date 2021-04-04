package com.example.tvprogramparser.Ui.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.tvprogramparser.R;

public class ProgressFragment extends Fragment {
    public static ProgressFragment local;

    public ProgressFragment() {
        // Required empty public constructor
    }

    public static ProgressFragment createInstance() {
//        ProgressFragment fragment = local;
//        if (fragment == null) {
//            synchronized (ProgressFragment.class) {
//                fragment = local;
//                if (fragment == null)
//                    fragment = local = new ProgressFragment();
//            }
//        }
//        return fragment;
        return new ProgressFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle bundle) {

        ProgressBar loading = (ProgressBar) view.findViewById(R.id.loading);
        loading.setVisibility(ProgressBar.VISIBLE);
    }
}