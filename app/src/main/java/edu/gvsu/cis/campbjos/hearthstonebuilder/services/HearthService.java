package edu.gvsu.cis.campbjos.hearthstonebuilder.services;

import com.google.gson.JsonObject;

import android.util.Log;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import edu.gvsu.cis.campbjos.hearthstonebuilder.HearthApplication;

import java.io.File;
import java.io.IOException;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Query;
import rx.Observable;

/**
 * @author Josiah Campbell
 * @version 10/17/15
 */
public class HearthService {

  private static final String MASHAPE_API = "https://omgvamp-hearthstone-v1.p.mashape.com";
  private HearthApi mHearthApi;

  /**
   * Public constructor for {@link HearthService}
   */
  public HearthService() {
    OkHttpClient okHttpClient = new OkHttpClient();
    try {
      cacheResponse(okHttpClient);
    } catch (Exception e) {

    }
    okHttpClient.networkInterceptors().add(REWRITE_CACHE_CONTROL_INTERCEPTOR);
    RestAdapter restAdapter = new RestAdapter.Builder()
        .setEndpoint(MASHAPE_API)
        .setClient(new OkClient(okHttpClient))
        .build();

    mHearthApi = restAdapter.create(HearthApi.class);
  }

  public HearthApi getApi() {
    return this.mHearthApi;
  }

  public interface HearthApi {
    @GET("/cards")
    public Observable<JsonObject> getCardResponse(
        @Header("X-Mashape-Key") String key, @Query("collectible") String query);

  }

  private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
      Response originalResponse = chain.proceed(chain.request());
      return originalResponse.newBuilder()
          .header("Cache-Control",
              String.format("max-age=%d, only-if-cached, max-stale=%d",
                  60 * 60 * 24, 0))
          .build();
    }
  };

  private void cacheResponse(OkHttpClient client) throws Exception {
    int cacheSize = 10 * 1024 * 1024; // 10 MiB
    Cache cache = new Cache(HearthApplication.getAppContext().getCacheDir(), cacheSize);
    client.setCache(cache);
  }
}
