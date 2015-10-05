package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;

import edu.gvsu.cis.campbjos.hearthstonebuilder.CustomAdapters.CardAdapter;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;

/**
 * AsyncTask responsible for gathering the HS card information from the
 * MashApe API.
 *
 * @author HearthBuilder Team
 * @version Fall 2015
 */
public class LoadCardJsonTask extends AsyncTask<Object, Void, Void> {
  private CardAdapter adapter;

  @Override
  protected void onPostExecute(Void vd) {
    super.onPostExecute(vd);
    adapter.notifyDataSetChanged();
  }

  /**
   * doInBackground calls on the MashApe HS API, which we process using the
   * {@link JsonUtil} class, and notify the calling fragment onPostExecute.
   *
   * @param params Context, String url, String key, ArrayList deck
   * @return null
   */
  @Override
  @SuppressWarnings(value = "unchecked")
  protected Void doInBackground(Object... params) {
    adapter = (CardAdapter) params[0];
    String url = (String) params[1];
    String key = (String) params[2];
    ArrayList<Card> cardList = (ArrayList<Card>) params[3];
    ArrayList<Card> visibleCards = (ArrayList<Card>) params[4];

    //initial load
    if(cardList.size() == 0) {
      try {
        String response = NetworkUtil.get(url, "X-Mashape-Key", key);
        JsonUtil.parse(response, cardList);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    //TEMP VARIABLES. WILL COME FROM DRAWER IN THE FUTURE.
    String classFilter = "Neutral";
    String manaCostFilter = "CLEAR";
    String typeFilter = "CLEAR";
    String rarityFilter = "CLEAR";
    String cardSetFilter = "CLEAR";
    //END

    visibleCards.clear();
    visibleCards.addAll(filterCards(cardList, classFilter, manaCostFilter, typeFilter, rarityFilter, cardSetFilter));
    return null;
  }

  ArrayList<Card> filterCards(ArrayList<Card> cards, String classFilter, String manaCostFilter, String typeFilter,
                              String rarityFilter, String cardSetFilter){

    //structure to sort the cards by mana cost.
    ArrayList<Card> sortedCards = new ArrayList<>();
    ArrayList<ArrayList<Card>> filteredCards = new ArrayList<>();

    for (int i = 0; i < cards.size(); i++){
      Card currentCard = cards.get(i);
      //does the card meet all filtering set on it?
      if(classFilterCard(classFilter, currentCard) && manaCostFilterCard(manaCostFilter, currentCard)
              && typeFilterCard(typeFilter,currentCard)&& rarityFilterCard(rarityFilter,currentCard)
              && cardSetFilterCard(cardSetFilter,currentCard)) {

        //index in filteredCards to add the currentCard
        int costIndex = currentCard.getCost();

        //if this cost has not been initialized then initialize it
        if (filteredCards.size() <= costIndex){
          for (int c = filteredCards.size(); c <= costIndex; c++){
            //add an empty arrayList to the index
            filteredCards.add(new ArrayList<Card>());
          }
        }

        //get the arrayList at the mana cost index and add the current card
        filteredCards.get(costIndex).add(currentCard);
      }
    }
    //after putting the cards into their index, go through and all to the final list
    for (int k = 0; k < filteredCards.size(); k++) {
      sortedCards.addAll(filteredCards.get(k));
    }
    return sortedCards;
  }

  boolean classFilterCard (String classFilter, Card card){
    if (classFilter.equals("Neutral") && card.getPlayerClass().isEmpty()){
      return true;
    } else if (classFilter.equals(card.getPlayerClass())){
      return true;
    }
    return false;
  }

  boolean manaCostFilterCard (String manaCostFilter, Card card){
    if (manaCostFilter.equals("CLEAR")) {
      return true;
    } else if (Integer.parseInt(manaCostFilter) == card.getCost()){
      return true;
    }
    return false;
  }

  boolean typeFilterCard (String typeFilter, Card card){
    if (typeFilter.equals("CLEAR")) {
      return true;
    } else if (typeFilter.equals(card.getType())){
      return true;
    }
    return false;
  }

  boolean rarityFilterCard (String rarityFilter, Card card){
    if (rarityFilter.equals("CLEAR")){
      return true;
    } else if (rarityFilter.equals(card.getRarity())){
      return true;
    }
    return false;
  }

  boolean cardSetFilterCard (String cardSetFilter, Card card){
    if (cardSetFilter.equals("CLEAR")){
      return true;
    } else if (cardSetFilter.equals(card.getCardSet())){
      return true;
    }
    return false;
  }
}
