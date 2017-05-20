package io.droidninja.feeder.ui.fragments;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.RuntimeExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.droidninja.feeder.R;
import io.droidninja.feeder.contentProvider.FeederContract;
import io.droidninja.feeder.sync.FeederSyncUtil;
import io.droidninja.feeder.ui.adapters.FeedsAdapter;
import io.droidninja.feeder.util.Helper;

/**
 * Created by Zeeshan on 2/12/17.
 */

public class FeedsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, FeedListener, SwipeRefreshLayout.OnRefreshListener {
    public static final int ID_FEEDS_LOADER = 232;
    public static final String[] MAIN_FEED_PROJECTION = {
            FeederContract.ArticleEntry.TITLE,
            FeederContract.ArticleEntry.DESCRIPTION,
            FeederContract.ArticleEntry.AUTHOR,
            FeederContract.ArticleEntry.URL,
            FeederContract.ArticleEntry.PUBLISH_AT,
            FeederContract.ArticleEntry.URL_TO_IMAGE,
            FeederContract.ArticleEntry.SOURCE
    };
    //Keep index in variable so these can be used to extract data from cursor
    public static final int INDEX_ARITICLE_TITLE = 0;
    public static final int INDEX_ARTICLE_DESCRIPTION = 1;
    public static final int INDEX_ARTICLE_AUTHOR = 2;
    public static final int INDEX_ARTICLE_URL = 3;
    public static final int INDEX_ARTICLE_PUBLISH = 4;
    public static final int INDEX_ARTICLE_URL_TO_IMAGE = 5;
    public static final int INDEX_ARTICLE_SOURCE = 6;
    private final String TAG = getClass().getName();

    @BindView(R.id.rv_feeds)
    RecyclerView rcFeeds;

    @BindView(R.id.adView)
    AdView mAdView;

    @BindView(R.id.swap_to_refresh)
    SwipeRefreshLayout swipeRefreshLayout;


    FeedsAdapter mFeedsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feeds_fragment, container, false);
        ButterKnife.bind(this, view);
        loadAd();
        initRc();
        // Starting loader to load feeds
        getActivity().getSupportLoaderManager().initLoader(ID_FEEDS_LOADER, null, this);
        // Starting service to download feeds
        FeederSyncUtil.initialize(getActivity());
        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    private void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void initRc() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcFeeds.setLayoutManager(linearLayoutManager);
        mFeedsAdapter = new FeedsAdapter(this);
        rcFeeds.setAdapter(mFeedsAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_FEEDS_LOADER:
                return new CursorLoader(getActivity(),
                        FeederContract.ArticleEntry.CONTENT_URI,
                        MAIN_FEED_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader not implement " + id);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, data.getCount() + "");

        if(data == null || data.getCount() < 0){

        }else{
            if (swipeRefreshLayout != null)
                swipeRefreshLayout.setRefreshing(false);
            rcFeeds.setVisibility(View.VISIBLE);
            mFeedsAdapter.swapCursor(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFeedsAdapter.swapCursor(null);
    }

    @Override
    public void openFeed(String title, String source, String description, String urlToImage, String url, ImageView cover, String transitionName, TextView titletv, String transitionText, TextView sourcetv, String sourceText) {
        FeedFragment feedFragment = FeedFragment.newInstance(title, description, url, urlToImage, transitionName, transitionText, sourceText, source);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, feedFragment, "Feed")
                    .addSharedElement(cover, transitionName)
                    .addSharedElement(titletv, transitionText)
                    .addSharedElement(sourcetv, sourceText)
                    .addToBackStack("Feed")
                    .commit();
        } else {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, feedFragment, "Feed")
                    .addToBackStack("Feed")
                    .commit();
        }
    }

    @Override
    public void onRefresh() {

        if(Helper.isConnected(getActivity())){
            rcFeeds.setVisibility(View.INVISIBLE);
            FeederSyncUtil.startSync(getActivity());
        }else{
            Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }


    }
}
