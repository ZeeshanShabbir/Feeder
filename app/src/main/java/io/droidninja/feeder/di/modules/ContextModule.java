package io.droidninja.feeder.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import io.droidninja.feeder.di.scopes.FeederApplicationScope;

/**
 * Created by Zeeshan on 2/7/17.
 */
@Module
public class ContextModule {
    private final Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @FeederApplicationScope
    public Context context(){
        return context;
    }
}
