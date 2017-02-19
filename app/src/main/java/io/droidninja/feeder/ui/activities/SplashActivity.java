package io.droidninja.feeder.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.droidninja.feeder.R;
import io.droidninja.feeder.util.SharedPrefsUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPrefsUtils.getBooleanPreference(this, getString(R.string.catalog_saved), false)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, FeedsCatalogActivity.class));
            finish();
        }
    }
}
