package io.droidninja.feeder.di;

import android.content.Context;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import io.droidninja.feeder.api.networking.FeedApi;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

/**
 * Created by Zeeshan on 2/7/17.
 */
@Module(includes = ContextModule.class)
public class NetworkModule {

    @Provides
    @FeederApplicationScope
    public OkHttpClient okHttpClient(HttpLoggingInterceptor httpLoggingInterceptor, Cache cache) {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .cache(cache)
                .build();
    }

    @Provides
    @FeederApplicationScope
    public HttpLoggingInterceptor httpLoggingInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.i(message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BASIC);
    }

    @Provides
    @FeederApplicationScope
    public Cache cache(File cacheFile) {
        return new Cache(cacheFile, 10 * 1000 * 1000);
    }

    @Provides
    @FeederApplicationScope
    public File file(Context context) {
        return new File(context.getCacheDir(), "okhttp_cache");
    }
}
