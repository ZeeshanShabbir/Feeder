package io.droidninja.feeder.ui.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import io.droidninja.feeder.ui.adapters.CatalogAdapter;
import io.droidninja.feeder.ui.adapters.SelectedInterfaceListener;
import io.droidninja.feeder.util.GridSpacingItemDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedsCatalogActivity extends AppCompatActivity implements SelectedInterfaceListener {
    private static final String TAG = "FeedsCatalogActivity";
    private FeedApi feedApi;

    @BindView(R.id.rc_catalog)
    RecyclerView recyclerView;
    @BindView(R.id.btn_next)
    Button btnNext;
    List<SourceDTO> selectedItems = new ArrayList<SourceDTO>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_feeds_catalog);
        ButterKnife.bind(this);
        GridLayoutManager manager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        final CatalogAdapter catalogAdapter = new CatalogAdapter(FeederApplication.get(this).getPicasso(), this);
        recyclerView.setAdapter(catalogAdapter);
        feedApi = FeederApplication.get(this).getFeedApi();
        int spanCount = 3; // 3 columns
        int spacing = 20; // 50px
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
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

            }
        });


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
        ContentValues value = new ContentValues();
        for (int i = 0; i < selectedItems.size(); i++) {
            value.put(FeederContract.SourceEntry.IDENTIFIER, selectedItems.get(i).getId());
            value.put(FeederContract.SourceEntry.NAME, selectedItems.get(i).getName());
            contentValues[i] = value;
        }
        int rows = getContentResolver().bulkInsert(FeederContract.SourceEntry.CONTENT_URI, contentValues);
        Log.d(TAG, rows + " rows have been added");
        startActivity(new Intent(this, MainActivity.class));
    }
}
