package io.droidninja.feeder.sync;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Zeeshan on 2/15/17.
 */

public class FeederSyncIntentService extends IntentService {
    private static final String TAG = "FeederSyncIntentService";

    public FeederSyncIntentService() {
        super("FeederSyncIntentService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public FeederSyncIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "starting");
        FeederSyncTask.syncArticles(this);
    }
}
