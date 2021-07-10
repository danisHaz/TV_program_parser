package com.example.tvprogramparser.Components;

import java.util.TreeMap;
import java.util.ArrayList;

import android.util.Log;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WorkDoneListener {
    private static volatile TreeMap<String, ArrayList<OnCompleteListener>> listenersContainer = new TreeMap<>();

    public static void setNewListener(OnCompleteListener listener) throws NullPointerException {
        if (!listener.isTagSet()) {
            Log.e("WorkDoneListener", "Tag is not set");
            return;
        }

        synchronized (WorkDoneListener.class) {
            if (listenersContainer.containsKey(listener.getTag()))
                listenersContainer.get(listener.getTag()).add(listener);
            else
                listenersContainer.put(listener.getTag(), new ArrayList<OnCompleteListener>() {{add(listener);}});
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
                    Log.d("WorkDoneListener", String.format("Something found for %s", tag));
                    for (OnCompleteListener local: listenersContainer.get(tag)) {
                        local.doWork(bundle);
                    }

                    Log.d("WorkDoneListener", String.format("Almost done for %s", tag));
                    listenersContainer.get(tag).clear();
                    listenersContainer.remove(tag);
                    Log.i("WorkDoneListener", String.format("Work Done: %s", tag));
                    return;
                }
            }
        }

        Log.e("WorkDoneListener", String.format("Want to do work in non-existing listener: %s", tag));
    }

    public static boolean isListenerSet(@NonNull final String tag) {
        return listenersContainer.containsKey(tag);
    }

}
