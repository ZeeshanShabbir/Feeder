package io.droidninja.feeder.sync;

import android.app.Service;
import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Zeeshan on 2/19/17.
 * This class is used to background jobs for feeder e.g downloading feeds into local db
 */

public class FeederFirebaseJobService extends JobService {
    private AsyncTask<Void, Void, Void> mFetchFeeds;

    @Override
    public boolean onStartJob(final JobParameters job) {
        mFetchFeeds = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Context context = getApplicationContext();
                //Here is problem
                FeederSyncTask.syncArticles((Service) context);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                jobFinished(job, false);
            }
        };
        mFetchFeeds.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mFetchFeeds != null) {
            mFetchFeeds.cancel(true);
        }
        return true;
    }
}
