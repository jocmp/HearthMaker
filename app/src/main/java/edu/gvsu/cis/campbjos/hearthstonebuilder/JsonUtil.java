package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;

/**
 * Created by josiah on 9/24/15.
 */
public class JsonUtil {

  /**
   * @param jsonResponse Json response
   * @param cardList     Master list of all the collectible cards
   * @throws JSONException if we can't parse through the Json response
   */
  public static void parse(String jsonResponse, ArrayList<Card> cardList) throws IOException {
    try {
      JSONObject jsonObj = new JSONObject(jsonResponse);
      Iterator keys = jsonObj.keys();
      String key;
      JSONArray jsonArray;
      JSONObject jsonCard;
      while (keys.hasNext()) {
        key = (String) keys.next();
        jsonArray = jsonObj.getJSONArray(key);
        for (int k = 0; k < jsonArray.length(); k++) {
          jsonCard = jsonArray.getJSONObject(k);
          // Check type "Hero" before adding
          if (!checkKeyToString(jsonCard,"type").equals("Hero")){
            // Create our card instance
            Card card = new Card();
            card.setCardId(checkKeyToString(jsonCard,"cardId"));
            card.setCardName(checkKeyToString(jsonCard,"name"));
            card.setCardSet(checkKeyToString(jsonCard,"cardSet"));
            card.setType(checkKeyToString(jsonCard,"type"));
            card.setRarity(checkKeyToString(jsonCard,"rarity"));
            card.setAttack(checkKeyToInt(jsonCard, "attack"));
            card.setDurability(checkKeyToInt(jsonCard,"durability"));
            card.setTextDescription(checkKeyToString(jsonCard,"text"));
            card.setFlavor(checkKeyToString(jsonCard, "flavor"));
            card.setArtist(checkKeyToString(jsonCard,"artist"));
            card.setIsCollectible(jsonCard.getBoolean("collectible"));
            card.setImageUrl(checkKeyToString(jsonCard,"img"));
            card.setGoldImageUrl(checkKeyToString(jsonCard,"imgGold"));
            card.setCost(checkKeyToInt(jsonCard, "cost"));
            card.setFaction(checkKeyToString(jsonCard, "faction"));
            card.setHealth(checkKeyToInt(jsonCard, "health"));
            card.setPlayerClass(checkKeyToString(jsonCard,"playerClass"));
            // Add to deck
            cardList.add(card);
          }
        }
      }
    } catch (JSONException j) {
      Log.d("JsonException", "Error parsing card", j);
    }
  }

  // Not every card has a particular key
  private static String checkKeyToString(JSONObject obj, String key) throws JSONException {
    if (obj.has(key)) {
      return obj.getString(key);
    } else {
      return "";
    }
  }

  private static int checkKeyToInt(JSONObject obj, String key) throws JSONException {
    if (obj.has(key)) {
      return obj.getInt(key);
    } else {
      return -1;
    }
  }
}