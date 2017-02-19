package io.droidninja.feeder.sync;

import android.app.Service;
import android.database.Cursor;
import android.util.Log;

import io.droidninja.feeder.FeederApplication;
import io.droidninja.feeder.api.model.FeedsDTO;
import io.droidninja.feeder.api.networking.FeedApi;
import io.droidninja.feeder.contentProvider.FeederContract;
import io.droidninja.feeder.util.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Zeeshan on 2/15/17.
 */

public class FeederSyncTask {
    private static final String TAG = "FeederSyncTask";

    public static void syncArticles(Service context) {
        String[] selections = {FeederContract.SourceEntry._ID, FeederContract.SourceEntry.IDENTIFIER};

        Cursor cursor = context.getContentResolver().query(
                FeederContract.SourceEntry.CONTENT_URI,
                selections,
                null,
                null,
                null);
        cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                String data = cursor.getString(1);
                Log.d(TAG, data);
                //https://newsapi.org/v1/articles?source=techcrunch&apiKey=233f7903c53749eda5e31794a7260df0
                String url = Constants.BASE_URL + "articles?source=" + data + "&apiKey=" + Constants.API_KEY;

                FeedApi feedApi = FeederApplication.get(context).getFeedApi();

                Call<FeedsDTO> feedsCall = feedApi.getFeeds(url);

                feedsCall.enqueue(new Callback<FeedsDTO>() {
                    @Override
                    public void onResponse(Call<FeedsDTO> call, Response<FeedsDTO> response) {
                        Log.d(TAG, response.message());
                        // TODO: 2/19/17 Add feeds to db
                    }

                    @Override
                    public void onFailure(Call<FeedsDTO> call, Throwable t) {
                        Log.d(TAG, t.getMessage());
                        // TODO: 2/19/17 handle this case
                    }
                });


            } while (cursor.moveToNext());
        }
    }
}
