package io.droidninja.feeder.di.components;

import dagger.Component;
import io.droidninja.feeder.di.modules.FeedsCatalogActivityModule;
import io.droidninja.feeder.di.scopes.FeedsCatalogActivityScope;
import io.droidninja.feeder.ui.adapters.CatalogAdapter;

/**
 * Created by Zeeshan on 2/20/17.
 */
@FeedsCatalogActivityScope
@Component(modules = {FeedsCatalogActivityModule.class}, dependencies = BaseComponent.class)
public interface FeedsCatalogActivityComponent {
    CatalogAdapter getCatalogAdapter();
}
