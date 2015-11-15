package edu.gvsu.cis.campbjos.hearthstonebuilder.presenters;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import edu.gvsu.cis.campbjos.hearthstonebuilder.CardFilter;
import edu.gvsu.cis.campbjos.hearthstonebuilder.CardViewFragment;
import edu.gvsu.cis.campbjos.hearthstonebuilder.DeckEncoder;
import edu.gvsu.cis.campbjos.hearthstonebuilder.DeckFragment;
import edu.gvsu.cis.campbjos.hearthstonebuilder.DetailActivity;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Deck;
import edu.gvsu.cis.campbjos.hearthstonebuilder.JsonUtil;
import edu.gvsu.cis.campbjos.hearthstonebuilder.MainActivity;
import edu.gvsu.cis.campbjos.hearthstonebuilder.services.HearthService;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Josiah Campbell
 * @version Fall 2015
 */
public class DeckFragmentPresenter {

  private DeckFragment mView;

  /**
   * Presenter class for {@link CardViewFragment}.
   * @param fragment The fragment's containing class
   */
  public DeckFragmentPresenter(DeckFragment fragment) {
    mView = fragment;
  }

  public boolean saveDeck(Deck deck) {
    FileOutputStream fos = null;
    try {
      fos = mView.getActivity().openFileOutput(String.valueOf(deck.getId()), Context.MODE_PRIVATE);
      DeckEncoder.writeJsonStream(fos, deck);
      fos.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return true;
  }

  public void loadDeck(String fileName) {
    mView.getFragmentDeck().getCardList().clear();
    JsonObject currentDeckObject;
    JsonParser jsonParser = new JsonParser();
    Gson gson = new Gson();
    currentDeckObject = readFileStreamToJson(fileName, jsonParser);
    if (currentDeckObject == null) {
      return;
    }
    List<Card> tempList = mView.getFragmentDeck().getCardList();
    for (JsonElement elem : currentDeckObject.get("cards").getAsJsonArray()) {
      JsonObject jsonObject = jsonParser.parse(elem.getAsString()).getAsJsonObject();
      tempList.add(gson.fromJson(jsonObject, Card.class));
    }
    mView.getDeckAdapter().notifyDataSetChanged();
  }

  private JsonObject readFileStreamToJson(String fileName, JsonParser parser) {
    FileInputStream fis = null;
    JsonObject fileObject = new JsonObject();
    try {
      fis = mView.getActivity().openFileInput(fileName);
      fileObject = parser.parse(getString(fis)).getAsJsonObject();
      fis.close();
    } catch (FileNotFoundException e) {
      return null;
    } catch (IOException e) {
      return null;
    }
    return fileObject;
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
    return sb.toString();
  }

  public void startDetailIntent(Card card) {
    Intent intent = new Intent(mView.getActivity(), DetailActivity.class);
    intent.putExtra("card", card.getImageUrl());
    intent.putExtra("name", card.getCardName());
    intent.putExtra("flavor", card.getFlavor());
    intent.putExtra("class", card.getPlayerClass());
    intent.putExtra("rarity", card.getRarity());
    intent.putExtra("set",card.getCardSet());
    intent.putExtra("type",card.getType());
    // Alternate extras
    intent.putExtra("health", card.getHealth());
    intent.putExtra("attack", card.getAttack());
    intent.putExtra("artist", card.getArtist());
    intent.putExtra("mana", card.getCost());
    intent.putExtra("text", card.getText());
    mView.startActivity(intent);
  }
}