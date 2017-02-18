package io.droidninja.feeder.ui.activities;

import dagger.Component;
import io.droidninja.feeder.di.FeederApplicationComponent;
import io.droidninja.feeder.ui.adapters.CatalogAdapter;

/**
 * Created by Zeeshan on 2/18/17.
 */

@Component(modules = FeedsCatalogActivityModule.class ,dependencies = FeederApplicationComponent.class)
@FeedsCatalogActivityScope
public interface FeedsCatalogActivityComponent {
    CatalogAdapter getCatalogAdapter();
}
