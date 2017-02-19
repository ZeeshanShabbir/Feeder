package io.droidninja.feeder.ui.activities;

import com.squareup.picasso.Picasso;

import dagger.Component;
import io.droidninja.feeder.api.networking.FeedApi;
import io.droidninja.feeder.di.FeederApplicationComponent;
import io.droidninja.feeder.ui.adapters.CatalogAdapter;

/**
 * Created by Zeeshan on 2/18/17.
 */
@FeedsCatalogActivityScope
@Component(modules = FeedsCatalogActivityModule.class, dependencies = FeederApplicationComponent.class)
public interface FeedsCatalogActivityComponent {
    CatalogAdapter getCatalogAdapter();

    Picasso getPicasso();

    FeedApi getFeedApi();
}
