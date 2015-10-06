package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import edu.gvsu.cis.campbjos.hearthstonebuilder.CustomAdapters.CardAdapter;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * {@link CardViewFragment.OnFragmentInteractionListener} interface to handle interaction events.
 * Use the {@link CardViewFragment#newInstance} factory method to create an instance of this
 * fragment.
 */
public class CardViewFragment extends Fragment implements LoadCardJsonTask.JsonTaskListener {
  private static final String URL = "https://omgvamp-hearthstone-v1.p.mashape.com/cards?";
  private static final String COLLECT_PARAM = "collectible=1";
  private ArrayList<Card> cards;
  private ArrayList<Card> visibleCards;
  private CardAdapter adapter;
  private LoadCardJsonTask jsonTask;
  private View cardFragmentView;

  //TEMP VARIABLES. WILL COME FROM DRAWER IN THE FUTURE.
  private String classFilter = "Neutral";
  private String manaCostFilter = "CLEAR";
  private String typeFilter = "CLEAR";
  private String rarityFilter = "CLEAR";
  private String cardSetFilter = "CLEAR";
  //END

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

  private OnFragmentInteractionListener mListener;

  // TODO: Rename and change types and number of parameters
  public static CardViewFragment newInstance() {
    CardViewFragment fragment = new CardViewFragment();
    Bundle args = new Bundle();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    cards = new ArrayList<>();
    visibleCards = new ArrayList<>();
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    jsonTask = new LoadCardJsonTask(this);
    jsonTask.execute(URL + COLLECT_PARAM,
        getStringFromManifest("hearthstone_api_key"), cards);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    cardFragmentView = inflater.inflate(R.layout.fragment_card_view, container, false);
    RecyclerView categoryRecycler = (RecyclerView) cardFragmentView.findViewById(R.id.card_recyclerview);

    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),
        LinearLayoutManager.VERTICAL, false);
    categoryRecycler.setHasFixedSize(true);
    categoryRecycler.isVerticalScrollBarEnabled();
    categoryRecycler.setLayoutManager(mLayoutManager);
    adapter = new CardAdapter(visibleCards);
    categoryRecycler.setAdapter(adapter);
    categoryRecycler.addOnItemTouchListener(
        new RecyclerItemClickListener(getActivity(),
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {

              }
            })
    );
    setHasOptionsMenu(true);
    return cardFragmentView;
  }

  // TODO: Rename method, update argument and hook method into UI event
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.navigation_drawer, menu);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @Override
  public void onTaskComplete() {
    visibleCards.clear();
    visibleCards.addAll(cards);
    adapter.notifyDataSetChanged();
  }

  /**
   * This interface must be implemented by activities that contain this fragment to allow an
   * interaction in this fragment to be communicated to the activity and potentially other fragments
   * contained in that activity. <p> See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html" >Communicating with
   * Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }

  private String getStringFromManifest(String key) {
    String results = "";
    try {
      Context context = getActivity().getBaseContext();
      ApplicationInfo ai = context.getPackageManager()
          .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
      results = (String) ai.metaData.get(key);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return results;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // The action bar home/up action should open or close the drawer.
    // ActionBarDrawerToggle will take care of this.

    // Handle action buttons
    switch (item.getItemId()) {
      case R.id.action_search:
        visibleCards.clear();
        visibleCards.addAll(filterCards(cards,
            classFilter, manaCostFilter, typeFilter, rarityFilter, cardSetFilter));
        adapter.notifyDataSetChanged();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  ArrayList<Card> filterCards(ArrayList<Card> cards, String classFilter,
                              String manaCostFilter, String typeFilter,
                              String rarityFilter, String cardSetFilter){

    //structure to sort the cards by mana cost.
    ArrayList<Card> sortedCards = new ArrayList<>();
    ArrayList<ArrayList<Card>> filteredCards = new ArrayList<>();

    for (int i = 0; i < cards.size(); i++){
      Card currentCard = cards.get(i);
      //does the card meet all filtering set on it?
      if(classFilterCard(classFilter, currentCard)
          && manaCostFilterCard(manaCostFilter, currentCard)
          && typeFilterCard(typeFilter,currentCard)
          && rarityFilterCard(rarityFilter,currentCard)
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
