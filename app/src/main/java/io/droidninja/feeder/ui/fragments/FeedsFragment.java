package io.droidninja.feeder.ui.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.droidninja.feeder.FeederApplication;
import io.droidninja.feeder.R;
import io.droidninja.feeder.api.model.FeedsDTO;
import io.droidninja.feeder.contentProvider.FeederContract;
import io.droidninja.feeder.sync.FeederSyncUtil;
import io.droidninja.feeder.ui.adapters.FeedAdapter;
import io.droidninja.feeder.util.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Zeeshan on 2/12/17.
 */

public class FeedsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int ID_FEEDS_LOADER = 232;
    public static final String[] MAIN_FEED_PROJECTION = {
            FeederContract.ArticleEntry.TITLE,
            FeederContract.ArticleEntry.DESCRIPTION,
            FeederContract.ArticleEntry.AUTHOR,
            FeederContract.ArticleEntry.URL,
            FeederContract.ArticleEntry.PUBLISH_AT,
            FeederContract.ArticleEntry.URL_TO_IMAGE
    };
    public static final int INDEX_ARITICLE_TITLE = 0;
    public static final int INDEX_ARTICLE_DESCRIPTION = 1;
    public static final int INDEX_ARTICLE_AUTHOR = 2;
    public static final int INDEX_ARTICLE_URL = 3;
    public static final int INDEX_ARTICLE_PUBLISH = 4;
    public static final int INDEX_ARTICLE_URL_TO_IMAGE = 5;
    private final String TAG = getClass().getName();
    @BindView(R.id.rv_feeds)
    RecyclerView rcFeeds;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feeds_fragment, container, false);
        ButterKnife.bind(this, view);
        initRc();
        loadData();
        FeederSyncUtil.initialize(getActivity());
        getActivity().getSupportLoaderManager().initLoader(ID_FEEDS_LOADER, null, this);
        return view;
    }

    private void initRc() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcFeeds.setLayoutManager(linearLayoutManager);
    }

    private void loadData() {
        Call<FeedsDTO> feedsDTOCall = FeederApplication.get(getActivity()).getFeedApi().getFeeds(Constants.BASE_URL + "articles?source=techcrunch&apiKey=233f7903c53749eda5e31794a7260df0");
        feedsDTOCall.enqueue(new Callback<FeedsDTO>() {
            @Override
            public void onResponse(Call<FeedsDTO> call, Response<FeedsDTO> response) {
                Log.d(TAG, response.message());
                if (response.isSuccessful()) {
                    FeedAdapter feedAdapter = new FeedAdapter(response.body());
                    rcFeeds.setAdapter(feedAdapter);
                }
            }

            @Override
            public void onFailure(Call<FeedsDTO> call, Throwable t) {

            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                FeederContract.ArticleEntry.CONTENT_URI,
                MAIN_FEED_PROJECTION,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, data.getCount() + "");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
