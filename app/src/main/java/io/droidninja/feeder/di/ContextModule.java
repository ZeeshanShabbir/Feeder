package io.droidninja.feeder.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

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
    public Context context(){
        return context;
    }
}
