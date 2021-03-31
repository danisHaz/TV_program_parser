package com.example.tvprogramparser.Components;

import java.util.TreeMap;

import android.util.Log;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WorkDoneListener {
    private static volatile TreeMap<String, OnCompleteListener> listenersContainer = new TreeMap<>();

    public static void setNewListener(OnCompleteListener listener) throws NullPointerException {
        if (!listener.isTagSet()) {
            Log.e("WorkDoneListener", "Tag is not set");
            return;
        }

        synchronized (WorkDoneListener.class) {
            listenersContainer.put(listener.getTag(), listener);
        }
    }

    public static void complete(@NonNull final String tag, @Nullable Bundle bundle, OnCompleteListener.Result result) throws NullPointerException {
        if (result == OnCompleteListener.Result.SUCCESS) {
            Log.i("WorkDoneListener", "Previous work complete");
        } else {
            Log.i("WorkDoneListener", "Previous work incomplete");
            if (listenersContainer.containsKey(tag)) {
                synchronized (WorkDoneListener.class) {
                    if (listenersContainer.containsKey(tag)) {
                        listenersContainer.remove(tag);
                    }
                }
            }
            return;
        }

        if (listenersContainer.containsKey(tag)) {
            synchronized (WorkDoneListener.class) {
                if (listenersContainer.containsKey(tag)) {
                    listenersContainer.get(tag).doWork(bundle);
                    listenersContainer.remove(tag);
                    Log.i("WorkDoneListener", "Work Done");
                    return;
                }
            }
        }

        Log.e("WorkDoneListener", "Want to do work in non-existing listener");
    }

    public static boolean isListenerSet(@NonNull final String tag) {
        return listenersContainer.containsKey(tag);
    }

}
