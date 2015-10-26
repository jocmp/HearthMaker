package edu.gvsu.cis.campbjos.hearthstonebuilder.services;

import com.google.gson.JsonObject;

import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;
import edu.gvsu.cis.campbjos.hearthstonebuilder.NetworkUtil;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Query;
import rx.Observable;

/**
 * @author Josiah Campbell
 * @version 10/17/15
 */
public class HearthService {

  private static final String MASHAPE_API =
      "https://omgvamp-hearthstone-v1.p.mashape.com";
  private HearthApi mHearthApi;

  /**
   * Public constructor for {@link HearthService}
   */
  public HearthService() {

    RestAdapter restAdapter = new RestAdapter.Builder()
        .setEndpoint(MASHAPE_API)
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
}
