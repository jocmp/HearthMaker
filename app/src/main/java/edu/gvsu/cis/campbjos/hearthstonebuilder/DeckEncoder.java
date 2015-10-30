package edu.gvsu.cis.campbjos.hearthstonebuilder;

import com.google.gson.stream.JsonWriter;

import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Deck;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * @author josiah
 * @version 10/27/15
 */
public class DeckEncoder {

  public void writeJsonStream(OutputStream out, List<Deck> decks) throws IOException {
    JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
    writer.setIndent("  ");
    writeDecksArray(writer, decks);
    writer.close();
  }

  public void writeDecksArray(JsonWriter writer, List<Deck> decks) throws IOException {
    writer.beginArray();
    for (Deck deck : decks) {
      writeDeck(writer, deck);
    }
    writer.endArray();
  }

  public void writeDeck(JsonWriter writer, Deck deck) throws IOException {
    writer.beginObject();
    writer.name("name").value(deck.getDeckName());
    writer.name("id").value(deck.getId());
    writer.name("cards");
    writeCardArray(writer, deck);
    writer.endObject();
  }


  public void writeCardArray(JsonWriter writer, Deck deck) throws IOException {
    writer.beginArray();
    for (Card card : deck.getCardList()) {
      writer.value(card.getCardJson());
    }
    writer.endArray();
  }
}
