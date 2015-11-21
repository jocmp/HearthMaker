package edu.gvsu.cis.campbjos.hearthstonebuilder;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import android.util.Log;

import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Deck;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * @author Josiah Campbell
 * @version 10/27/15
 */
public class DeckEncoder {

  private DeckEncoder() {

  }

  public static void writeJsonStream(OutputStream out, Deck deck) throws IOException {
    Gson gson = new Gson();
    JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
    writer.setIndent("  ");
    writeDeck(writer, gson, deck);
    writer.close();
  }

  private static void writeDeck(JsonWriter writer, Gson gson, Deck deck) throws IOException {
    writer.beginObject();
    writer.name("deckClass").value(deck.getDeckClass());
    writer.name("name").value(deck.getDeckName());
    writer.name("id").value(deck.getId());
    writer.name("cards");
    writeCardArray(writer,gson, deck);
    writer.endObject();
  }

  private static void writeCardArray(JsonWriter writer, Gson gson, Deck deck) throws IOException {
    writer.beginArray();
    for (Card card : deck.getCardList()) {
      writer.value(gson.toJson(card));
    }
    writer.endArray();
  }
}