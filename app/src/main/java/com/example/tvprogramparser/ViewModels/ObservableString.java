package com.example.tvprogramparser.ViewModels;

import androidx.databinding.BaseObservable;
import androidx.annotation.NonNull;
import androidx.databinding.Bindable;

public class ObservableString extends BaseObservable {
    private String value = "";

    public ObservableString(@NonNull String newValue) { value = newValue; }

    @Bindable
    public String getValue() { return value != null ? value : ""; }

    public void setValue(@NonNull String newValue) { value = newValue; }

    public boolean isEmpty() { return value == null || value.isEmpty(); }

    public void clear() { setValue(""); }
}
