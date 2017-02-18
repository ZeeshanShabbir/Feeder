package io.droidninja.feeder.api.networking;

import io.droidninja.feeder.api.model.CatalogDTO;
import io.droidninja.feeder.api.model.FeedsDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Zeeshan on 2/7/17.
 */

public interface FeedApi {
    @GET("sources")
    Call<CatalogDTO> getSources();

    @GET
    Call<FeedsDTO> getFeeds(@Url String url);
}
