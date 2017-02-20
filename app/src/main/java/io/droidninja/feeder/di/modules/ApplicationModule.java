package io.droidninja.feeder.di.modules;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Zeeshan on 2/20/17.
 */
@Module
public class ApplicationModule {
    private static Application sApplication;

    public ApplicationModule(Application sApplication) {
        this.sApplication = sApplication;
    }

    @Singleton
    @Provides
    Application application() {
        return sApplication;
    }
}
