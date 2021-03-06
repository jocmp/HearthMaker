package com.teitsmch.hearthmaker.presenters;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.teitsmch.hearthmaker.CardViewFragment;
import com.teitsmch.hearthmaker.DeckEncoder;
import com.teitsmch.hearthmaker.DeckFragment;
import com.teitsmch.hearthmaker.DetailActivity;
import com.teitsmch.hearthmaker.Entity.Card;
import com.teitsmch.hearthmaker.Entity.Deck;

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

  public boolean saveDeck(Deck deck, boolean setDelete) {
    if (setDelete) {
      return true;
    }
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
    if (mView.getFragmentDeck().getDeckClass().isEmpty()) {
      mView.getFragmentDeck().setDeckClass(currentDeckObject.get("deckClass").getAsString());
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
      return null;
    }
    return sb.toString();
  }

  public void startDetailIntent(Card card) {
    Intent intent = new Intent(mView.getActivity(), DetailActivity.class);
    String cardJson = new Gson().toJson(card);
    intent.putExtra("card", cardJson);
    intent.putExtra("fileName", String.valueOf(mView.getFragmentDeck().getId()));
    mView.startActivity(intent);
  }
}