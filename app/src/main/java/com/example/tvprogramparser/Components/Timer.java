package com.example.tvprogramparser.Components;

/**
 * Main purpose of this class is to process
 * every operation that is connected to time,
 * periods and etc.
 */

import android.util.Pair;

public class Timer {
    private int hours = 0;
    private int minutes = 0;

//    this constructor is for setting
//    notifications for alarm scheduler
//    5 mins before program starts
    public Timer(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

//    name of method is too stupid and out-of-context, but i'm inadequate now, sorry:(
    public Pair<Integer, Integer> getPrettyEarlierTime() {
        int whole = (hours * 60 + minutes - 5);
        if (whole < 0)
            return new Pair<>(0, 0);

        hours = whole / 60;
        minutes = whole % 60;
        return new Pair<>(hours, minutes);
    }
}
