package io.droidninja.feeder;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

import com.squareup.picasso.Picasso;

import io.droidninja.feeder.di.ContextModule;
import io.droidninja.feeder.di.DaggerFeederApplicationComponent;
import io.droidninja.feeder.di.FeederApplicationComponent;
import io.droidninja.feeder.api.networking.FeedApi;
import timber.log.Timber;

/**
 * Created by Zeeshan on 2/7/17.
 */

public class FeederApplication extends Application {

    private Picasso picasso;
    private FeedApi feedApi;

    FeederApplicationComponent feederApplicationComponent;


    public static FeederApplication get(Activity activity) {
        return (FeederApplication) activity.getApplication();
    }

    public static FeederApplication get(Service service) {
        return (FeederApplication) service.getApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        feederApplicationComponent = DaggerFeederApplicationComponent.builder()
                .contextModule(new ContextModule(this))
                .build();

        picasso = feederApplicationComponent.getPicasso();
        feedApi = feederApplicationComponent.getFeedApi();
        Timber.plant(new Timber.DebugTree());
    }


    public FeedApi getFeedApi() {
        return feedApi;
    }

    public Picasso getPicasso() {
        return picasso;
    }

    public FeederApplicationComponent component() {
        return feederApplicationComponent;
    }
}
