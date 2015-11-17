package edu.gvsu.cis.campbjos.hearthstonebuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;

/**
 * @author HearthMaker Team
 */
public class CardFilter {

  public static ArrayList<Card> filterCards(
      List<Card> cards, String classFilter,
      String manaCostFilter, String typeFilter,
      String rarityFilter, String cardSetFilter,
      String textFilter) {

    //structure to sort the cards by mana cost.
    ArrayList<Card> sortedCards = new ArrayList<>();
    ArrayList<ArrayList<Card>> filteredCards = new ArrayList<>();
    int size;
    int i;
    for (i = 0, size = cards.size(); i < size; i++) {
      Card currentCard = cards.get(i);
      //does the card meet all filtering set on it?
      if (classFilterCard(classFilter, currentCard)
          && manaCostFilterCard(manaCostFilter, currentCard)
          && typeFilterCard(typeFilter, currentCard)
          && rarityFilterCard(rarityFilter, currentCard)
          && cardSetFilterCard(cardSetFilter, currentCard)
          && textFilterCard(textFilter, currentCard)) {
        //index in filteredCards to add the currentCard
        int costIndex = currentCard.getCost();
        //if this cost has not been initialized then initialize it
        if (filteredCards.size() <= costIndex) {
          for (int c = filteredCards.size(); c <= costIndex; c++) {
            //add an empty arrayList to the index
            filteredCards.add(new ArrayList<Card>());
          }
        }
        //get the arrayList at the mana cost index and add the current card
        filteredCards.get(costIndex).add(currentCard);
      }
    }
    //after putting the cards into their index, go through and all to the final list
    int k;
    int filterSize;
    for (k = 0, size = filteredCards.size(); k < size; k++) {
      Collections.sort(filteredCards.get(k), ALPHABETICAL_ORDER);
      sortedCards.addAll(filteredCards.get(k));
    }
    return sortedCards;
  }

  public static ArrayList<Card> deckFilterCards(
          List<Card> cards, String classFilter,
          String manaCostFilter, String typeFilter,
          String rarityFilter, String cardSetFilter,
          String textFilter, String currentClass) {

    //structure to sort the cards by mana cost.
    ArrayList<Card> sortedCards = new ArrayList<>();
    ArrayList<ArrayList<Card>> filteredCards = new ArrayList<>();
    int size;
    int i;
    for (i = 0, size = cards.size(); i < size; i++) {
      Card currentCard = cards.get(i);
      //does the card meet all filtering set on it?
      if (deckClassFilterCard(classFilter, currentClass, currentCard)
              && manaCostFilterCard(manaCostFilter, currentCard)
              && typeFilterCard(typeFilter, currentCard)
              && rarityFilterCard(rarityFilter, currentCard)
              && cardSetFilterCard(cardSetFilter, currentCard)
              && textFilterCard(textFilter, currentCard)) {
        //index in filteredCards to add the currentCard
        int costIndex = currentCard.getCost();
        //if this cost has not been initialized then initialize it
        if (filteredCards.size() <= costIndex) {
          for (int c = filteredCards.size(); c <= costIndex; c++) {
            //add an empty arrayList to the index
            filteredCards.add(new ArrayList<Card>());
          }
        }
        //get the arrayList at the mana cost index and add the current card
        filteredCards.get(costIndex).add(currentCard);
      }
    }
    //after putting the cards into their index, go through and all to the final list
    int k;
    int filterSize;
    for (k = 0, size = filteredCards.size(); k < size; k++) {
      Collections.sort(filteredCards.get(k), ALPHABETICAL_ORDER);
      sortedCards.addAll(filteredCards.get(k));
    }
    return sortedCards;
  }

  private static Comparator<Card> ALPHABETICAL_ORDER = new Comparator<Card>() {
    public int compare(Card card1, Card card2) {
      int res = String.CASE_INSENSITIVE_ORDER.compare(card1.getCardName(), card2.getCardName());
      if (res == 0) {
        res = card1.getCardName().compareTo(card2.getCardName());
      }
      return res;
    }
  };

  private static boolean deckClassFilterCard(String classFilter, String currentClass, Card card) {
    if (classFilter.equals("CLEAR") && (card.getPlayerClass().equals("Neutral") || card.getPlayerClass().equals(currentClass))) {
      return true;
    } else if (classFilter.equals(card.getPlayerClass())) {
      return true;
    }
    return false;
  }

  private static boolean classFilterCard(String classFilter, Card card) {
    if (classFilter.equals("CLEAR")) {
      return true;
    } else if (classFilter.equals(card.getPlayerClass())) {
      return true;
    }
    return false;
  }

  private static boolean manaCostFilterCard(String manaCostFilter, Card card) {
    if (manaCostFilter.equals("CLEAR")) {
      return true;
    } else if (manaCostFilter.equals("7+") && card.getCost() >= 7) {
      return true;
      //to get past the invalid int check to see if it is at the 7+
    } else if (!manaCostFilter.equals("7+") && Integer.parseInt(manaCostFilter) == card.getCost()) {

      return true;
    }
    return false;
  }

  private static boolean typeFilterCard(String typeFilter, Card card) {
    if (typeFilter.equals("CLEAR")) {
      return true;
    } else if (typeFilter.equals(card.getType())) {
      return true;
    }
    return false;
  }

  private static boolean rarityFilterCard(String rarityFilter, Card card) {
    if (rarityFilter.equals("CLEAR")) {
      return true;
    } else if (rarityFilter.equals(card.getRarity())) {
      return true;
    }
    return false;
  }

  private static boolean cardSetFilterCard(String cardSetFilter, Card card) {
    if (cardSetFilter.equals("CLEAR")) {
      return true;
    } else if (cardSetFilter.equals("Classic") && card.getCardSet().equals("Basic")) {
      return true;
    } else if (cardSetFilter.equals(card.getCardSet())) {
      return true;
    }
    return false;
  }

  private static boolean textFilterCard(String textFilter, Card card) {
    return card.getCardName() != null && card.getCardName().toLowerCase().contains(textFilter.toLowerCase())
        || card.getText() != null && card.getText().toLowerCase().contains(textFilter.toLowerCase())
        || card.getRace() != null && card.getRace().toLowerCase().contains(textFilter.toLowerCase());
  }

  public static void resetFilter(List<Card> list) {
    filterCards(list, "CLEAR", "CLEAR", "CLEAR", "CLEAR", "CLEAR", "CLEAR");
  }
}
