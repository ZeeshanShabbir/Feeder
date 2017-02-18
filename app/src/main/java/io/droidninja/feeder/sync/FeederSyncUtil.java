package io.droidninja.feeder.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import java.util.concurrent.TimeUnit;

import io.droidninja.feeder.contentProvider.FeederContract;

/**
 * Created by Zeeshan on 2/15/17.
 */

public class FeederSyncUtil {
    private static final String TAG = "FeederSyncUtil";
    private static final int SYNC_INTERVAL_HOURS = 3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

    private static boolean sInitialized;


    synchronized public static void initialize(final Context context) {
        if (sInitialized)
            return;
        sInitialized = true;
        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {
                String[] selection = {FeederContract.ArticleEntry._ID};
                Cursor cursor = context.getContentResolver().query(FeederContract.ArticleEntry.CONTENT_URI,
                        selection,
                        null,
                        null,
                        null);
                if (cursor == null || cursor.getCount() == 0) {
                    startSync(context);
                }
                cursor.close();
            }
        });
        checkForEmpty.start();

    }

    public static void startSync(final Context context) {
        Intent intent = new Intent(context, FeederSyncIntentService.class);
        context.startService(intent);
    }
}
