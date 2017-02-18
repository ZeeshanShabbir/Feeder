package io.droidninja.feeder.ui.activities;

import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;
import io.droidninja.feeder.ui.adapters.CatalogAdapter;
import io.droidninja.feeder.ui.adapters.FeedAdapter;
import io.droidninja.feeder.ui.adapters.SelectedInterfaceListener;

/**
 * Created by Zeeshan on 2/18/17.
 */
@Module
public class FeedsCatalogActivityModule {

    private final SelectedInterfaceListener selectedInterfaceListener;

    public FeedsCatalogActivityModule(SelectedInterfaceListener selectedInterfaceListener) {
        this.selectedInterfaceListener = selectedInterfaceListener;
    }

    @Provides
    @FeedsCatalogActivityScope
    public CatalogAdapter catalogAdapter(Picasso picasso, SelectedInterfaceListener mSelectesInterfaceListener) {
        return new CatalogAdapter(picasso, mSelectesInterfaceListener);
    }
}
