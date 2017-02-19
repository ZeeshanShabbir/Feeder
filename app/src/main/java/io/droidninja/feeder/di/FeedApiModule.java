package io.droidninja.feeder.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import io.droidninja.feeder.api.networking.FeedApi;
import io.droidninja.feeder.util.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Zeeshan on 2/7/17.
 */
@Module(includes = NetworkModule.class)
public class FeedApiModule {
    @Provides
    @FeederApplicationScope
    public FeedApi feedApi(Retrofit feedRetrofit){
        return feedRetrofit.create(FeedApi.class);
    }
    @Provides
    @FeederApplicationScope
    public Gson gson(){
        return new GsonBuilder().create();
    }
    @Provides
    @FeederApplicationScope
    public Retrofit retrofit(Gson gson, OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .baseUrl(Constants.BASE_URL)
                .build();
    }
}
