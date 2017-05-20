package io.droidninja.feeder.sync;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import io.droidninja.feeder.FeederApplication;
import io.droidninja.feeder.R;
import io.droidninja.feeder.api.model.FeedsDTO;
import io.droidninja.feeder.api.networking.FeedApi;
import io.droidninja.feeder.contentProvider.FeederContract;
import io.droidninja.feeder.ui.activities.MainActivity;
import io.droidninja.feeder.util.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Zeeshan on 2/15/17.
 */

public class FeederSyncTask {
    private static final String TAG = "FeederSyncTask";
    private static int rowss = 0;

    public synchronized static void syncArticles(final Context context) {
        String[] selections = {FeederContract.SourceEntry._ID, FeederContract.SourceEntry.IDENTIFIER};

        Cursor cursor = context.getContentResolver().query(
                FeederContract.SourceEntry.CONTENT_URI,
                selections,
                null,
                null,
                null);
        if (cursor != null) {
            cursor.getCount();
            if (cursor.moveToFirst()) {
                do {
                    final String data = cursor.getString(1);
                    Log.d(TAG, data);
                    //https://newsapi.org/v1/articles?source=techcrunch&apiKey=233f7903c53749eda5e31794a7260df0
                    String url = Constants.BASE_URL + "articles?source=" + data + "&apiKey=" + Constants.API_KEY;
                    FeedApi feedApi = FeederApplication.getsBaseComponent().getFeedApi();
                    Call<FeedsDTO> feedsCall = feedApi.getFeeds(url);
                    feedsCall.enqueue(new Callback<FeedsDTO>() {
                        @Override
                        public void onResponse(Call<FeedsDTO> call, Response<FeedsDTO> response) {
                            Log.d(TAG, response.message());
                            //Completed: 2/19/17 Add feeds to db
                            ContentValues[] values = new ContentValues[response.body().getArticle().size()];
                            for (int i = 0; i < response.body().getArticle().size(); i++) {
                                ContentValues contentValue = new ContentValues();
                                String name = response.body().getArticle().get(i).getAuthor();
                                String description = response.body().getArticle().get(i).getDescription();
                                String title = response.body().getArticle().get(i).getTitle();
                                String url = response.body().getArticle().get(i).getUrl();
                                String urlToImage = response.body().getArticle().get(i).getUrlToImage();
                                String puslishedAt = response.body().getArticle().get(i).getPublishedAt();
                                contentValue.put(FeederContract.ArticleEntry.AUTHOR, name);
                                contentValue.put(FeederContract.ArticleEntry.DESCRIPTION, description);
                                contentValue.put(FeederContract.ArticleEntry.TITLE, title);
                                contentValue.put(FeederContract.ArticleEntry.URL, url);
                                contentValue.put(FeederContract.ArticleEntry.URL_TO_IMAGE, urlToImage);
                                contentValue.put(FeederContract.ArticleEntry.PUBLISH_AT, puslishedAt);
                                contentValue.put(FeederContract.ArticleEntry.SOURCE, data);
                                values[i] = contentValue;
                            }
                            rowss = rowss + context.getContentResolver().bulkInsert(FeederContract.ArticleEntry.CONTENT_URI, values);
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
        cursor.close();

        if (!FeederApplication.isActivityVisible()) {
            try {
                Thread.sleep(10000);
                if (rowss > 0) {
                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.drawable.ic_stat_fiber_new)
                                    .setContentTitle("Your Feeds are ready")
                                    .setAutoCancel(true)
                                    .setContentText("You have " + rowss + " new feeds to read");

                    int NOTIFICATION_ID = 12345;
                    Intent targetIntent = new Intent(context, MainActivity.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(context,
                            0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);
                    NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    nManager.notify(NOTIFICATION_ID, builder.build());
                    rowss = 0;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
