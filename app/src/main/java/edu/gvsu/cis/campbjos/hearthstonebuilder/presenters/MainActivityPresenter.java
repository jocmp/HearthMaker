package edu.gvsu.cis.campbjos.hearthstonebuilder.presenters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

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
import edu.gvsu.cis.campbjos.hearthstonebuilder.FragmentView;
import edu.gvsu.cis.campbjos.hearthstonebuilder.JsonUtil;
import edu.gvsu.cis.campbjos.hearthstonebuilder.MainActivity;
import edu.gvsu.cis.campbjos.hearthstonebuilder.NetworkUtil;
import edu.gvsu.cis.campbjos.hearthstonebuilder.R;
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
  private static final String EMPTY_STRING = "";
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

  public void getCardFilter(
      String cardClass,
      String cost, String type, String rarity, String set, String query, String currentClass) {
    mView.setSubscriberResult(
        CardFilter.deckFilterCards(cardList,
            cardClass, cost, type, rarity, set, query, currentClass));
  }

  public boolean resetFilter() {
    CardFilter.resetFilter(cardList);
    return true;
  }

  public void loadCards() {
    if (NetworkUtil.isOnline(mView)) {
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
                  // initial ordering of cards.
                  mView.setSubscriberResult(
                          CardFilter.filterCards(cardList, "CLEAR", "CLEAR", "CLEAR", "CLEAR", "CLEAR", ""));
                }
              });
    } else {
      final FragmentView fragmentView = (FragmentView) mView.getActivityFragment();
      fragmentView.setProgress(false);

      Snackbar.make(mView.getNavigationView(), "No network connection. Please reconnect and try again", Snackbar.LENGTH_INDEFINITE).setAction("Retry",
              new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  fragmentView.setProgress(true);
                  loadCards();
                  //mEmptyTextView.setVisibility(View.GONE);
                }
              }).show();
    }
  }


  public void loadDecks(String[] fileList) {
    String[] files = fileList;
    Double fileId = 0D;
    JsonObject currentDeckObject;
    JsonParser jsonParser = new JsonParser();
    int fileSize = files.length;
    for (String file : files) {
      try {
        fileId = Double.parseDouble(file);
      } catch (NumberFormatException numExcept) {
        // Hm. That was weird. . .
        continue;
      }
      currentDeckObject = readFileStreamToJson(file, jsonParser);
      int deckId = currentDeckObject.get("id").getAsInt();
      if (currentDeckObject != null) {
        mView.setNavigationMenuItem(
            deckId,
            currentDeckObject.get("name").getAsString());
      }
      //mView.getNavigationView().setItemIconTintList(null);
      switch (currentDeckObject.get("deckClass").getAsString()){
        case "Warrior":
          mView.getNavigationView().getMenu().findItem(deckId).setIcon(R.drawable.warrior_icon);
          break;
        case "Druid":
          mView.getNavigationView().getMenu().findItem(deckId).setIcon(R.drawable.druid_icon);
          break;
        case "Hunter":
          mView.getNavigationView().getMenu().findItem(deckId).setIcon(R.drawable.hunter_icon);
          break;
        case "Mage":
          mView.getNavigationView().getMenu().findItem(deckId).setIcon(R.drawable.mage_icon);
          break;
        case "Paladin":
          mView.getNavigationView().getMenu().findItem(deckId).setIcon(R.drawable.paladin_icon);
          break;
        case "Priest":
          mView.getNavigationView().getMenu().findItem(deckId).setIcon(R.drawable.priest_icon);
          break;
        case "Rogue":
          mView.getNavigationView().getMenu().findItem(deckId).setIcon(R.drawable.rogue_icon);
          break;
        case "Shaman":
          mView.getNavigationView().getMenu().findItem(deckId).setIcon(R.drawable.shaman_icon);
          break;
        case "Warlock":
          mView.getNavigationView().getMenu().findItem(deckId).setIcon(R.drawable.warlock_icon);
          break;
        default:
          break;
      }


      Drawable drawable = mView.getNavigationView().getMenu().findItem(deckId).getIcon();
      drawable.mutate();
      drawable.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
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

  public List<Card> getCardList() {
    return this.cardList;
  }
}