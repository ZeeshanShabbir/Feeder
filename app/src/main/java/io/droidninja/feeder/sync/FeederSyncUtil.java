package io.droidninja.feeder.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import io.droidninja.feeder.contentProvider.FeederContract;

/**
 * Created by Zeeshan on 2/15/17.
 */

public class FeederSyncUtil {
    private static final String TAG = "FeederSyncUtil";
    private static final int SYNC_INTERVAL_HOURS = 3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.MINUTES.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

    private static boolean sInitialized;


    synchronized public static void initialize(final Context context) {
        if (sInitialized)
            return;
        sInitialized = true;
        scheduleFirebaseJobDispatcherSync(context);
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

    static void scheduleFirebaseJobDispatcherSync(Context context) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(driver);

        Job syncJob = firebaseJobDispatcher.newJobBuilder()
                .setService(FeederFirebaseJobService.class)
                .setTag(TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();
        firebaseJobDispatcher.schedule(syncJob);
    }
}
