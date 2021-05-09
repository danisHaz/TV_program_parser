package com.example.tvprogramparser.ViewModels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BaseObservable;

public class ActivityViewModel<T extends AppCompatActivity> extends BaseObservable {
    protected final T activity;
    public ActivityViewModel(@NonNull T activity) { this.activity = activity; }
    public T getActivity() { return activity; }

//    add some lifecycle listener methods
    public void onStop() {
//        notify lifecycle about onStop
    }

    public void onStart() {

    }

    public void onResume() {

    }
}
