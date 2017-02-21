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
import io.droidninja.feeder.R;
import io.droidninja.feeder.contentProvider.FeederContract;
import io.droidninja.feeder.sync.FeederSyncUtil;
import io.droidninja.feeder.ui.adapters.FeedsAdapter;

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
    //Keep index in variable so these can be used to extract data from cursor
    public static final int INDEX_ARITICLE_TITLE = 0;
    public static final int INDEX_ARTICLE_DESCRIPTION = 1;
    public static final int INDEX_ARTICLE_AUTHOR = 2;
    public static final int INDEX_ARTICLE_URL = 3;
    public static final int INDEX_ARTICLE_PUBLISH = 4;
    public static final int INDEX_ARTICLE_URL_TO_IMAGE = 5;
    private final String TAG = getClass().getName();

    @BindView(R.id.rv_feeds)
    RecyclerView rcFeeds;

    FeedsAdapter mFeedsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feeds_fragment, container, false);
        ButterKnife.bind(this, view);
        initRc();
        FeederSyncUtil.initialize(getActivity());
        getActivity().getSupportLoaderManager().initLoader(ID_FEEDS_LOADER, null, this);
        return view;
    }

    private void initRc() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcFeeds.setLayoutManager(linearLayoutManager);
        mFeedsAdapter = new FeedsAdapter();
        rcFeeds.setAdapter(mFeedsAdapter);
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
        mFeedsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFeedsAdapter.swapCursor(null);
    }
}
