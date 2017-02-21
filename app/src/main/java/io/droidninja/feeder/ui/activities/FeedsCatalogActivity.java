package io.droidninja.feeder.ui.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.droidninja.feeder.FeederApplication;
import io.droidninja.feeder.R;
import io.droidninja.feeder.api.model.CatalogDTO;
import io.droidninja.feeder.api.model.SourceDTO;
import io.droidninja.feeder.api.networking.FeedApi;
import io.droidninja.feeder.contentProvider.FeederContract;
import io.droidninja.feeder.di.components.DaggerFeedsCatalogActivityComponent;
import io.droidninja.feeder.di.components.FeedsCatalogActivityComponent;
import io.droidninja.feeder.di.modules.FeedsCatalogActivityModule;
import io.droidninja.feeder.ui.adapters.CatalogAdapter;
import io.droidninja.feeder.ui.adapters.SelectedInterfaceListener;
import io.droidninja.feeder.util.GridSpacingItemDecoration;
import io.droidninja.feeder.util.SharedPrefsUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedsCatalogActivity extends AppCompatActivity implements SelectedInterfaceListener {
    private static final String TAG = "FeedsCatalogActivity";

    @BindView(R.id.rc_catalog)
    RecyclerView recyclerView;

    @BindView(R.id.btn_next)
    Button btnNext;

    List<SourceDTO> selectedItems = new ArrayList<SourceDTO>();

    FeedsCatalogActivityComponent feedsCatalogActivityComponent;

    CatalogAdapter catalogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.activity_feeds_catalog);
        ButterKnife.bind(this);

        feedsCatalogActivityComponent = DaggerFeedsCatalogActivityComponent.builder()
                .baseComponent(FeederApplication.getsBaseComponent())
                .feedsCatalogActivityModule(new FeedsCatalogActivityModule(this))
                .build();
        initRc();
        FeedApi feedApi = FeederApplication.getsBaseComponent().getFeedApi();
        Call<CatalogDTO> catalogs = feedApi.getSources();
        catalogs.enqueue(new Callback<CatalogDTO>() {
            @Override
            public void onResponse(Call<CatalogDTO> call, Response<CatalogDTO> response) {
                Log.d(TAG, response.message());
                if (response.isSuccessful()) {
                    catalogAdapter.swap(response.body().getSources());
                }
            }

            @Override
            public void onFailure(Call<CatalogDTO> call, Throwable t) {
                // TODO: 2/19/17 Handle case when this will fail
            }
        });
    }
    /**
     * initializes the RecyclerView
     */
    private void initRc() {
        GridLayoutManager manager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        catalogAdapter = feedsCatalogActivityComponent.getCatalogAdapter();
        recyclerView.setAdapter(catalogAdapter);

        //Adding margin and padding to Grid layout
        int spanCount = 3; // 3 columns
        int spacing = 20; // 50px
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, true));
    }

    private void hideStatusBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onSelectedItems(List<SourceDTO> items) {
        Log.d(TAG, "" + items.size());
        if (!selectedItems.isEmpty()) {
            selectedItems.clear();
        }
        for (int i = 0; i < items.size(); i++) {
            selectedItems.add(items.get(i));
        }
        if (items.size() <= 0) {
            btnNext.setVisibility(View.GONE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
            btnNext.setText(items.size() + " items selected");
        }
    }

    @OnClick(R.id.btn_next)
    public void onNext() {
        ContentValues[] contentValues = new ContentValues[selectedItems.size()];
        int rowsDeleted = getContentResolver().delete(FeederContract.SourceEntry.CONTENT_URI, null, null);
        Log.d(TAG, rowsDeleted + " row has been deleted from db");
        for (int i = 0; i < selectedItems.size(); i++) {
            ContentValues value = new ContentValues();
            String name = selectedItems.get(i).getName();
            String identifier = selectedItems.get(i).getId();
            value.put(FeederContract.SourceEntry.IDENTIFIER, identifier);
            value.put(FeederContract.SourceEntry.NAME, name);
            contentValues[i] = value;
        }

        int rows = getContentResolver().bulkInsert(FeederContract.SourceEntry.CONTENT_URI, contentValues);
        Log.d(TAG, rows + " rows have been added");
        SharedPrefsUtils.setBooleanPreference(this, getString(R.string.catalog_saved), true);
        startActivity(new Intent(this, MainActivity.class));
    }
}
