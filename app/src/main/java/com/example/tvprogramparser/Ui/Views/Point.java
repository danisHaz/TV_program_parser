package com.example.tvprogramparser.Ui.Views;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public class Point {
    public int x, y;
    private static int screenX = -1, screenY = -1;

    //  Actually this var is for setting more width and height to custom views cause uncertainty
    public static final int ROUNDING_PX = 0;

    public Point(int x, int y) throws NullPointerException {
        this.x = x;
        this.y = y;
        if (screenY == -1)
            throw new NullPointerException("Screen dimensions not counted");
    }

    public static int convertDpToPx(int dp, @NonNull Context context) {
        return (int)TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }

    public static int convertPxToDp(int px, @NonNull Context context) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX,
                px,
                context.getResources().getDisplayMetrics()
        );
    }

    public static int getX() { return screenX; }
    public static int getY() { return screenY; }

    public static void countScreenDimensions(@NonNull Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(metrics);

        screenX = metrics.widthPixels;
        screenY = metrics.heightPixels;
    }
}
