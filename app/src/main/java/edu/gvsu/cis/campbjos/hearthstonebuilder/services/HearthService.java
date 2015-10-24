package edu.gvsu.cis.campbjos.hearthstonebuilder.services;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * @author Josiah Campbell
 * @version 10/17/15
 */
public class HearthService {

  private static final String MASHAPE_API =
      "https://omgvamp-hearthstone-v1.p.mashape.com";//cards?";
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

    //@GET("/locationcampusSearch")
    //public Observable<Location> getCard(@Query("q") String location);
  }
}
