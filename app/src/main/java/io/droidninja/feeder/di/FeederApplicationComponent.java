package io.droidninja.feeder.di;

import com.squareup.picasso.Picasso;

import dagger.Component;
import io.droidninja.feeder.api.networking.FeedApi;

/**
 *
 * Created by Zeeshan on 2/7/17.
 */
@Component(modules = {PicassoModule.class,FeedApiModule.class})
public interface FeederApplicationComponent {

    Picasso getPicasso();

    FeedApi getFeedApi();
}
