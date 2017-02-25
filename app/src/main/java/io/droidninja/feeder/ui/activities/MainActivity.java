package io.droidninja.feeder.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.droidninja.feeder.ui.fragments.FeedsFragment;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new FeedsFragment(), this.toString())
                    .commit();
        }
    }
}
