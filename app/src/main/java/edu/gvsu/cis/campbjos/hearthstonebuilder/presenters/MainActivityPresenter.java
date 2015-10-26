package edu.gvsu.cis.campbjos.hearthstonebuilder.presenters;

import com.google.gson.JsonObject;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;

import edu.gvsu.cis.campbjos.hearthstonebuilder.CardFilter;
import edu.gvsu.cis.campbjos.hearthstonebuilder.CardViewFragment;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;
import edu.gvsu.cis.campbjos.hearthstonebuilder.JsonUtil;
import edu.gvsu.cis.campbjos.hearthstonebuilder.MainActivity;
import edu.gvsu.cis.campbjos.hearthstonebuilder.services.HearthService;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Josiah Campbell
 * @version Fall 2015
 */
public class MainActivityPresenter {
  private MainActivity mView;
  private HearthService mHearthService;
  private List<Card> cardList;
  private final String EMPTY_STRING = "";
  private List<Card> filterList;
  /**
   * Presenter class for {@link CardViewFragment}.
   * @param activity The fragment's containing class
   */
  public MainActivityPresenter(MainActivity activity, HearthService hearthService) {
    mView = activity;
    mHearthService = hearthService;
    cardList = new ArrayList<>();
    filterList = new ArrayList<>();
  }

  public void getCardFilter(
      String cardClass, String cost, String type, String rarity, String set) {
    filterList.clear();
    filterList.addAll(CardFilter.filterCards(cardList, cardClass, cost, type, rarity, set, ""));
    if (filterList.isEmpty()) {
      mView.setNotifyListEmpty();
    } else {
      mView.setSubscriberResult(filterList);
    }
  }

  public void getCardFilter(
      String cardClass, String cost, String type, String rarity, String set, String query) {
    mView.setSubscriberResult(
        CardFilter.filterCards(cardList, cardClass, cost, type, rarity, set, query));
  }

  public void loadCards() {

    mHearthService.getApi()
        .getCardResponse(getKeyFromManifest(mView.getManifestHearthApiKey()),
            mView.getCollectibleOption())
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<JsonObject>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onNext(JsonObject jsonObject) {
            JsonUtil.parse(jsonObject, cardList);
            mView.setSubscriberResult(cardList);
          }
        });
  }

  private String getKeyFromManifest(String key) {
    try {
      Context context = mView.getBaseContext();
      ApplicationInfo applicationInfo = context.getPackageManager()
          .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
      return (String) applicationInfo.metaData.get(key);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return EMPTY_STRING;
  }

}