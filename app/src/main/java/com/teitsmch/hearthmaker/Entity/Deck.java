package com.teitsmch.hearthmaker.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author josiah
 * @version 10/27/15
 */
public class Deck {

  private String deckName;
  private String deckClass;
  private List<Card> cardList;
  private int id;

  /**
   * Public constructor for {@link Deck} class.
   * @param deckClass Deck class
   * @param deckName Deck name
   * @param id Unique deck id
   */
  public Deck (String deckClass, String deckName, int id) {
    this.deckClass = deckClass;
    this.deckName = deckName;
    this.id = id;
    this.cardList = new ArrayList<>();
  }

  public String getDeckName() {
    return deckName;
  }

  public List<Card> getCardList() {
    return cardList;
  }

  public int getId() {
    return this.id;
  }

  public void setDeckClass(String deckClass) {
    this.deckClass = deckClass;
  }

  public void setDeckName(String deckName) {
    this.deckName = deckName;
  }

  public String getDeckClass() {
    return this.deckClass;
  }

  public void setCardList(List<Card> cardList) {
    this.cardList = cardList;
  }
}
