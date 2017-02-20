package io.droidninja.feeder.di.components;

import com.squareup.picasso.Picasso;

import dagger.Component;
import io.droidninja.feeder.api.networking.FeedApi;
import io.droidninja.feeder.di.modules.FeedApiModule;
import io.droidninja.feeder.di.modules.PicassoModule;
import io.droidninja.feeder.di.scopes.ApplicationScope;

/**
 * Created by Zeeshan on 2/7/17.
 */
@ApplicationScope
@Component(modules = {PicassoModule.class, FeedApiModule.class}, dependencies = ApplicationComponent.class)
public interface BaseComponent {

    Picasso getPicasso();

    FeedApi getFeedApi();

}
