package edu.gvsu.cis.campbjos.hearthstonebuilder.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author josiah
 * @version 10/27/15
 */
public class Deck {

  private String deckName;
  private List<Card> cardList;

  public Deck (String deckName, List<Card> cardList) {
    this.deckName = deckName;
    this.cardList = cardList;
  }

  public String getDeckName() {
    return deckName;
  }

  public List<Card> getCardList() {
    return cardList;
  }

  public int getId() {
    return this.deckName.hashCode();
  }

  public void setDeckName(String deckName) {
    this.deckName = deckName;
  }

  public void setCardList(List<Card> cardList) {
    this.cardList = cardList;
  }
}
