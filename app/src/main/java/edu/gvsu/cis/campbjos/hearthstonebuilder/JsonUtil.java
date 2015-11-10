package edu.gvsu.cis.campbjos.hearthstonebuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.jsoup.Jsoup;

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
    for (Map.Entry<String, JsonElement> entry : entries) {
      JsonArray jsonArray = entry.getValue().getAsJsonArray();
      for (int k = 0; k < jsonArray.size(); k++) {
        JsonObject jsonCard = jsonArray.get(k).getAsJsonObject();
        parseJsonCard(jsonCard, cardList);
      }
    }
  }

  public static void parseJsonCard(JsonObject jsonCardObject, List<Card> cards) {
      try {
        if (!checkKeyToString(jsonCardObject, "type").equals("Hero")) {
          // Create our card instance
          Card card = new Card();
          // Set the entire object as a String
          card.setCardJson(checkKeyToString(jsonCardObject, "card"));
          card.setCardCount(checkCardCount(jsonCardObject, "cardCount"));
          card.setCardId(checkKeyToString(jsonCardObject, "cardId"));
          card.setCardName(checkKeyToString(jsonCardObject, "name"));
          card.setCardSet(checkKeyToString(jsonCardObject, "cardSet"));
          card.setType(checkKeyToString(jsonCardObject, "type"));
          card.setRarity(checkKeyToString(jsonCardObject, "rarity"));
          card.setAttack(checkKeyToInt(jsonCardObject, "attack"));
          card.setDurability(checkKeyToInt(jsonCardObject, "durability"));
          card.setTextDescription(Jsoup.parse(checkKeyToString(
              jsonCardObject, "text")).text());
          card.setFlavor(checkKeyToString(jsonCardObject, "flavor"));
          card.setArtist(checkKeyToString(jsonCardObject, "artist"));
          card.setIsCollectible(jsonCardObject.get("collectible").getAsBoolean());
          card.setImageUrl(checkKeyToString(jsonCardObject, "img"));
          card.setGoldImageUrl(checkKeyToString(jsonCardObject, "imgGold"));
          card.setCost(checkKeyToInt(jsonCardObject, "cost"));
          card.setRace(checkKeyToString(jsonCardObject, "race"));
          card.setHealth(checkKeyToInt(jsonCardObject, "health"));
          //if there is no player class then the card is neutral
          if (checkKeyToString(jsonCardObject, "playerClass").isEmpty()) {
            card.setPlayerClass("Neutral");
          } else {
            card.setPlayerClass(checkKeyToString(jsonCardObject, "playerClass"));
          }
          // Add to entire list
          cards.add(card);
        }
      } catch (JSONException e) {
        e.printStackTrace();
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

  private static int checkCardCount(JsonObject obj, String key) throws JSONException {
    if (obj.has(key)) {
      return obj.get(key).getAsInt();
    } else {
      return 0;
    }
  }
}