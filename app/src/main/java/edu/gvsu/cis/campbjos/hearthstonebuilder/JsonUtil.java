package edu.gvsu.cis.campbjos.hearthstonebuilder;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import android.util.Log;

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
  private static Gson gson;
  static {
    gson = new Gson();
  }
  /**
   * @param jsonResponse Json response
   * @param cardList     Master list of all the collectible cards
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
    if (jsonCardObject.get("type").getAsString().equals("Hero")) {
      return;
    }
    Card card = new Card();
    try {
      card = gson.fromJson(jsonCardObject, Card.class);
    } catch (JsonSyntaxException syn) {

    }
    if (card.getPlayerClass() == null) {
      card.setPlayerClass("Neutral");
    }
    String text = card.getText() != null ? card.getText() : "";
    card.setText(Jsoup.parse(text).text());
    cards.add(card);
  }

  /**
   * Convert JsonObject to Card directly.
   * @param jsonCardObject The JsonObject which is a card
   */
  public static Card parseJsonToCard(String json) {
    JsonObject jsonCardObject = new JsonPrimitive(json).getAsJsonObject();
    Card card = new Card();
    if (jsonCardObject.get("type").getAsString().equals("Hero")) {
      return card;
    }
    try {
      card = gson.fromJson(jsonCardObject, Card.class);
    } catch (JsonSyntaxException syn) {

    }
    if (card.getPlayerClass() == null) {
      card.setPlayerClass("Neutral");
    }
    String text = card.getText() != null ? card.getText() : "";
    card.setText(Jsoup.parse(text).text());
    return card;
  }
}