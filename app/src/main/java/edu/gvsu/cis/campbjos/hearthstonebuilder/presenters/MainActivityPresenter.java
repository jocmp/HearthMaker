package edu.gvsu.cis.campbjos.hearthstonebuilder.presenters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.gvsu.cis.campbjos.hearthstonebuilder.CardFilter;
import edu.gvsu.cis.campbjos.hearthstonebuilder.CardViewFragment;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;
import edu.gvsu.cis.campbjos.hearthstonebuilder.JsonUtil;
import edu.gvsu.cis.campbjos.hearthstonebuilder.MainActivity;
import edu.gvsu.cis.campbjos.hearthstonebuilder.services.HearthService;
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
            //initial ordering of cards.
            mView.setSubscriberResult(CardFilter.filterCards(cardList, "CLEAR", "CLEAR", "CLEAR","CLEAR", "CLEAR", ""));
            //mView.setSubscriberResult(cardList);
          }
        });
  }

  public void loadDecks() {
    JsonObject currentDeckObject;
    JsonParser jsonParser = new JsonParser();
    for (String fileName : mView.fileList()) {
      currentDeckObject = readFileStreamToJson(fileName, jsonParser);
      if (currentDeckObject != null) {
        mView.setNavigationMenuItem(
            currentDeckObject.get("id").getAsInt(),
            currentDeckObject.get("name").getAsString());
      }
    }
  }

  private JsonObject readFileStreamToJson(String fileName, JsonParser parser) {
    FileInputStream fis = null;
    JsonObject fileObject = new JsonObject();
    try {
      fis = mView.openFileInput(fileName);
      fileObject = parser.parse(getString(fis)).getAsJsonObject();
      fis.close();
    } catch (FileNotFoundException e) {
      return null;
    } catch (IOException e) {
      return null;
    }
    return fileObject;
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

  private static String getString(FileInputStream stream) throws IOException {

    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
    StringBuilder sb = new StringBuilder();

    String line = null;
    try {
      while ((line = reader.readLine()) != null) {
        sb.append(line).append("\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    Log.d("getting FileInputStream", sb.toString());
    return sb.toString();
  }
}