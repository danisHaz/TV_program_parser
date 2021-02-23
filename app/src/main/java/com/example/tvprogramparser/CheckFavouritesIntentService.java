package com.example.tvprogramparser;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;
import android.util.Log;

import androidx.core.content.ContextCompat;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class CheckFavouritesIntentService extends IntentService {
    public CheckFavouritesIntentService() {
        super("CheckFavouritesIntentService");
    }

    public static void startActionWriteFavourite(Context context) {
        Intent intent = new Intent(context, CheckFavouritesIntentService.class);
        intent.setAction(TLS.ACTION_WRITE_FAVOURITE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (TLS.ACTION_WRITE_FAVOURITE.equals(action)) {
                handleActionWriteFavourite();
            }
        }
    }

    public void handleActionWriteFavourite() {
        Log.d("tag", "Hello there");
    }

}
