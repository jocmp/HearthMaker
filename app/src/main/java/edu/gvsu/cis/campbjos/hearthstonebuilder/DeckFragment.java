package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.gvsu.cis.campbjos.hearthstonebuilder.CustomAdapters.CardDeckAdapter;
import edu.gvsu.cis.campbjos.hearthstonebuilder.CustomAdapters.DeckCatalogAdapter;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Deck;
import edu.gvsu.cis.campbjos.hearthstonebuilder.UI.DividerItemDecoration;
import edu.gvsu.cis.campbjos.hearthstonebuilder.presenters.DeckFragmentPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * {@link DeckFragment.DeckFragmentListener} interface to handle interaction events. Use the {@link
 * DeckFragment#newInstance} factory method to create an instance of this fragment.
 */
public class DeckFragment extends Fragment implements FragmentView {

  @InjectView(R.id.catalog_recyclerview)
  RecyclerView mCatalogRecyclerView;
  @InjectView(R.id.deck_recyclerview)
  RecyclerView mDeckRecyclerView;
  @InjectView(R.id.loading_spinner)
  View mLoadingView;
  @InjectView(R.id.empty_event_text)
  TextView mEmptyTextView;

  private View mDeckFragmentView;

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_TYPE = "param1";

  private int mType;
  private DeckCatalogAdapter catalogAdapter;
  private CardDeckAdapter deckAdapter;

  private DeckFragmentListener hostActivity;
  private DeckFragmentPresenter mDeckFragmentPresenter;
  private List<Card> catalogCards;
  private Deck deck;
  private String deckName;
  private static boolean setDelete;

  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";
  private static final String ARG_PARAM3 = "param3";

  private String mDeckClassParam;
  private String mNameParam;
  private int mDeckIdParam;
  private int deckCardCount;

  /**
   * Use this factory method to create a new instance of this fragment using the provided
   * parameters.
   *
   * @param type Parameter 1.
   * @return A new instance of fragment DeckFragment.
   */
  public static DeckFragment newInstance(String type, int deckId, String deckName) {
    DeckFragment fragment = new DeckFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, type);
    args.putInt(ARG_PARAM2, deckId);
    args.putString(ARG_PARAM3, deckName);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mDeckFragmentPresenter = new DeckFragmentPresenter(this);
    if (getArguments() != null) {
      mDeckClassParam = getArguments().getString(ARG_PARAM1);
      mDeckIdParam = getArguments().getInt(ARG_PARAM2);
      mNameParam = getArguments().getString(ARG_PARAM3);
    }
    catalogCards = new ArrayList<>();
    deck = new Deck(mDeckClassParam, mNameParam, mDeckIdParam);
  }

  @Override
  public void onResume() {
    super.onResume();
    mDeckFragmentPresenter.loadDeck(String.valueOf(mDeckIdParam));
    hostActivity.updateSpinner(deck.getDeckClass());
    hostActivity.updateClassIcon(getClassIcon(deck.getDeckClass()));
    updateCount(deck.getCardList());
    setDelete = false;
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    mDeckFragmentView = inflater.inflate(R.layout.fragment_deck, container, false);
    ButterKnife.inject(this, mDeckFragmentView);
    String[] classes = getResources().getStringArray(R.array.card_class_dialog);

    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),
        LinearLayoutManager.VERTICAL, false);
    mCatalogRecyclerView.setHasFixedSize(true);
    mCatalogRecyclerView.isVerticalScrollBarEnabled();
    mCatalogRecyclerView.setLayoutManager(mLayoutManager);
    catalogAdapter = new DeckCatalogAdapter(catalogCards);
    mCatalogRecyclerView.setAdapter(catalogAdapter);
    mCatalogRecyclerView.addOnItemTouchListener(
            new RecyclerItemClickListener(getActivity(), mCatalogRecyclerView,
                    new RecyclerItemClickListener.OnItemClickListener() {
                      @Override
                      public void onItemClick(View view, int position) {
                        addDeckCard(catalogAdapter.getPositionInfo(position));
                      }

                      @Override
                      public void onItemLongClick(View view, int position) {
                        mDeckFragmentPresenter.startDetailIntent(catalogAdapter.getPositionInfo(position));
                      }
                    }));
    mCatalogRecyclerView.addItemDecoration
            (new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
    RecyclerView.LayoutManager mDeckLayoutManager = new LinearLayoutManager(getActivity(),
        LinearLayoutManager.VERTICAL, false);
    mDeckRecyclerView.setHasFixedSize(true);
    mDeckRecyclerView.isVerticalScrollBarEnabled();
    mDeckRecyclerView.setLayoutManager(mDeckLayoutManager);
    deckAdapter = new CardDeckAdapter(deck.getCardList());
    mDeckRecyclerView.setAdapter(deckAdapter);
    mDeckRecyclerView.addOnItemTouchListener(
            new RecyclerItemClickListener(getActivity(), mCatalogRecyclerView,
                    new RecyclerItemClickListener.OnItemClickListener() {
                      @Override
                      public void onItemClick(View view, int position) {
                        Card current = deck.getCardList().get(position);
                        int count = current.getCardCount();
                        if (count > 1) {
                          count--;
                          current.setCardCount(count);
                          deckAdapter.notifyItemChanged(position);
                        } else {
                          deck.getCardList().remove(position);
                          deckAdapter.notifyItemRemoved(position);
                        }
                        updateCount(deck.getCardList());
                      }

                      @Override
                      public void onItemLongClick(View view, int position) {
                        // Do nothing
                      }
                    }));
    hostActivity.getAllCards();
    return mDeckFragmentView;
  }

  @Override
  public void onAttach(Context activity) {
    super.onAttach(activity);
    hostActivity = (DeckFragmentListener) activity;
  }

  @Override
  public void onPause() {
    super.onPause();
    mDeckFragmentPresenter.saveDeck(deck, setDelete);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    hostActivity = null;
  }

  public void setCardList(List<Card> list) {
    catalogCards.clear();
    catalogCards.addAll(list);
    if (catalogCards.isEmpty())
      mEmptyTextView.setVisibility(View.VISIBLE);
    else
      mEmptyTextView.setVisibility(View.GONE);
    mLoadingView.setVisibility(View.GONE);
    catalogAdapter.notifyDataSetChanged();
  }

  public void setListEmpty() {
    catalogCards.clear();
    mEmptyTextView.setVisibility(View.VISIBLE);
    mLoadingView.setVisibility(View.GONE);
    catalogAdapter.notifyDataSetChanged();
  }

  @Override
  public void setProgress(boolean isLoading) {
    if (isLoading) {
      mLoadingView.setVisibility(View.VISIBLE);
      mEmptyTextView.setVisibility(View.GONE);
    } else {
      mLoadingView.setVisibility(View.GONE);
      mEmptyTextView.setVisibility(View.VISIBLE);
    }
  }

  public void addDeckCard(Card card) {
    //is the deck full?
    if(deckCardCount < 30) {
      int indexFound = -1;
      int size = deck.getCardList().size();
      for (int k = 0; k < size; k++) {
        if (deck.getCardList().get(k).getCardId().equals(card.getCardId())) {
          indexFound = k;
        }
      }
      //card has been found in the list
      if (indexFound != -1) {
        int count = deck.getCardList().get(indexFound).getCardCount();
        //card has been found and there is only one
        if (count < 2 && !card.getRarity().equals("Legendary")) {
          count++;
          deck.getCardList().get(indexFound).setCardCount(count);
        } else if (card.getRarity().equals("Legendary")) {
          Snackbar.make(mDeckFragmentView,
              "You can only have 1 " + card.getCardName(), Snackbar.LENGTH_SHORT).show();
        } else {
          String message = "You can only have 2 " + card.getCardName();
          if (!message.substring(message.length()).equals("s"))
            message += "s";
          Snackbar.make(mDeckFragmentView, message, Snackbar.LENGTH_SHORT).show();
        }
        //card has not been found at all
      } else {
        card.setCardCount(1);
        deck.getCardList().add(card);
      }
      Collections.sort(deck.getCardList(), HEARTHSTONE_ORDER);
      deckAdapter.notifyDataSetChanged();
      updateCount(deck.getCardList());
    } else {
      Snackbar.make(mDeckFragmentView, "Deck is full", Snackbar.LENGTH_SHORT).show();
    }
  }

  private int getClassIcon(String className){
    switch (className){
      case "Warrior":
        return R.drawable.warrior_icon;
      case "Druid":
        return R.drawable.druid_icon;
      case "Hunter":
        return R.drawable.hunter_icon;
      case "Mage":
        return R.drawable.mage_icon;
      case "Paladin":
        return R.drawable.paladin_icon;
      case "Priest":
        return R.drawable.priest_icon;
      case "Rogue":
        return R.drawable.rogue_icon;
      case "Shaman":
        return R.drawable.shaman_icon;
      case "Warlock":
        return R.drawable.warlock_icon;
      default:
        break;
    }
    return R.drawable.placeholder;
  }

  private static Comparator<Card> HEARTHSTONE_ORDER = new Comparator<Card>() {
    public int compare(Card card1, Card card2) {
      if (card1.getCost() > card2.getCost())
        return 1;
      else if (card2.getCost() > card1.getCost()) {
        return -1;
      }
      //Alphabetical order
      int res = String.CASE_INSENSITIVE_ORDER.compare(card1.getCardName(), card2.getCardName());
      if (res == 0) {
        res = card1.getCardName().compareTo(card2.getCardName());
      }
      return res;
    }
  };

  private void updateCount(List<Card> deck){
    int count = 0;
    for (Card current : deck) {
      count += current.getCardCount();
    }
    deckCardCount = count;
    hostActivity.updateSubtitle(String.valueOf(deckCardCount)  + " / 30");
  }

  public List<Card> getAdapterCards() {
    return catalogCards;
  }

  public Deck getFragmentDeck() {
    return deck;
  }

  public RecyclerView.Adapter getDeckAdapter() {
    return deckAdapter;
  }

  public void setFragmentDeckName(String name) {
    deck.setDeckName(name);
  }

  public void clearDeck() {
    deck.getCardList().clear();
    updateCount(deck.getCardList());
    deckAdapter.notifyDataSetChanged();
  }

  public interface DeckFragmentListener {
    public void getAllCards();
    public void updateSubtitle(String amount);
    public void updateSpinner(String className);
    public void updateClassIcon(int iconId);
    public void deleteDeck();
  }

  public void setForDeletion(boolean delete) {
    this.setDelete = delete;
  }
}
