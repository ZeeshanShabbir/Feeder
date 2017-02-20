package io.droidninja.feeder.di.components;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import io.droidninja.feeder.di.modules.ApplicationModule;

/**
 * Created by Zeeshan on 2/20/17.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    Application getApplication();
}
