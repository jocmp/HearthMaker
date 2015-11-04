package edu.gvsu.cis.campbjos.hearthstonebuilder;

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

  public static void writeJsonStream(OutputStream out, Deck deck) throws IOException {
    JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
    writer.setIndent("  ");
    writeDeck(writer, deck);
    writer.close();
  }

  private static void writeDeck(JsonWriter writer, Deck deck) throws IOException {
    writer.beginObject();
    writer.name("name").value(deck.getDeckName());
    writer.name("id").value(deck.getId());
    writer.name("cards");
    writeCardArray(writer, deck);
    writer.endObject();
  }

  private static void writeCardArray(JsonWriter writer, Deck deck) throws IOException {
    writer.beginArray();
    for (Card card : deck.getCardList()) {
      writer.value(card.getCardJson());
    }
    writer.endArray();
  }
}