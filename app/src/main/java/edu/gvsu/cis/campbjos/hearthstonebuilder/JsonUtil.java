package edu.gvsu.cis.campbjos.hearthstonebuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;

/**
 * @author HearthMaker Team
 */
public class JsonUtil {

  /**
   * @param jsonResponse Json response
   * @param cardList     Master list of all the collectible cards
   * @throws JSONException if we can't parse through the Json response
   */
  public static void parse(JsonObject jsonResponse, List<Card> cardList) {
    cardList.clear();
    Set<Map.Entry<String, JsonElement>> entries = jsonResponse.entrySet();
    for (Map.Entry<String, JsonElement> entry: entries) {
      JsonArray jsonArray = entry.getValue().getAsJsonArray();
      for (int k = 0; k < jsonArray.size(); k++) {
        JsonObject jsonCard = (JsonObject) jsonArray.get(k);
        // Check type "Hero" before adding
        try {
          if (!checkKeyToString(jsonCard, "type").equals("Hero")) {
            // Create our card instance
            Card card = new Card();
            // Set the entire object as a String
            card.setCardJson(jsonArray.get(k).getAsString());
            card.setCardId(checkKeyToString(jsonCard, "cardId"));
            card.setCardName(checkKeyToString(jsonCard, "name"));
            card.setCardSet(checkKeyToString(jsonCard, "cardSet"));
            card.setType(checkKeyToString(jsonCard, "type"));
            card.setRarity(checkKeyToString(jsonCard, "rarity"));
            card.setAttack(checkKeyToInt(jsonCard, "attack"));
            card.setDurability(checkKeyToInt(jsonCard, "durability"));
            card.setTextDescription(Jsoup.parse(checkKeyToString(
                jsonCard, "text")).text());
            card.setFlavor(checkKeyToString(jsonCard, "flavor"));
            card.setArtist(checkKeyToString(jsonCard, "artist"));
            card.setIsCollectible(jsonCard.get("collectible").getAsBoolean());
            card.setImageUrl(checkKeyToString(jsonCard, "img"));
            card.setGoldImageUrl(checkKeyToString(jsonCard, "imgGold"));
            card.setCost(checkKeyToInt(jsonCard, "cost"));
            card.setRace(checkKeyToString(jsonCard, "race"));
            card.setHealth(checkKeyToInt(jsonCard, "health"));
            //if there is no player class then the card is neutral
            if (checkKeyToString(jsonCard, "playerClass").isEmpty()) {
              card.setPlayerClass("Neutral");
            } else {
              card.setPlayerClass(checkKeyToString(jsonCard, "playerClass"));
            }
            // Add to entire list
            cardList.add(card);
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }
  }

  // Not every card has a particular key
  private static String checkKeyToString(JsonObject obj, String key) throws JSONException {
    if (obj.has(key)) {
      return obj.get(key).getAsString();
    } else {
      return "";
    }
  }

  private static int checkKeyToInt(JsonObject obj, String key) throws JSONException {
    if (obj.has(key)) {
      return obj.get(key).getAsInt();
    } else {
      return -1;
    }
  }
}