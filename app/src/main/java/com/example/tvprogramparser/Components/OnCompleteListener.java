package com.example.tvprogramparser.Components;

public abstract class OnCompleteListener {
    private String tag = null;

    public synchronized OnCompleteListener setTag(String tag) {
        if (this.tag == null)
            this.tag = tag;

        return this;
    }

    public String getTag() {
        return tag;
    }

    public boolean isTagSet() {
        return tag != null;
    }

    public enum Result {
        SUCCESS,
        FAILURE
    }

    public abstract void doWork();
}
