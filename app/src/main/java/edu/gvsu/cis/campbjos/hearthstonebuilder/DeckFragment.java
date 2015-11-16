package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import edu.gvsu.cis.campbjos.hearthstonebuilder.CustomAdapters.CardAdapter;
import edu.gvsu.cis.campbjos.hearthstonebuilder.CustomAdapters.CardDeckAdapter;
import edu.gvsu.cis.campbjos.hearthstonebuilder.CustomAdapters.DeckCatalogAdapter;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Deck;
import edu.gvsu.cis.campbjos.hearthstonebuilder.UI.DividerItemDecoration;
import edu.gvsu.cis.campbjos.hearthstonebuilder.presenters.DeckFragmentPresenter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * {@link DeckFragment.DeckFragmentListener} interface to handle interaction events. Use
 * the {@link DeckFragment#newInstance} factory method to create an instance of this fragment.
 */
public class DeckFragment extends Fragment {

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
  
  private DeckFragmentListener mListener;
  private DeckFragmentPresenter mDeckFragmentPresenter;
  private List<Card> cards;
  private Deck deck;
  private String deckName;

  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";
  private static final String ARG_PARAM3 = "param3";

  private int mParam1;
  private String mNameParam;
  private int mDeckIdParam;


  /**
   * Use this factory method to create a new instance of this fragment using the provided
   * parameters.
   *
   * @param type Parameter 1.
   * @return A new instance of fragment DeckFragment.
   */
  public static DeckFragment newInstance(int type, int deckId, String deckName) {
    DeckFragment fragment = new DeckFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_PARAM1, type);
    args.putInt(ARG_PARAM2, deckId);
    args.putString(ARG_PARAM3, deckName);
    fragment.setArguments(args);
    return fragment;
  }

  public static DeckFragment newInstance(String deckName) {
    DeckFragment fragment = new DeckFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_PARAM1, -1);
    args.putInt(ARG_PARAM2, -1);
    args.putString(ARG_PARAM3, deckName);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mDeckFragmentPresenter = new DeckFragmentPresenter(this);
    if (getArguments() != null) {
      mParam1 = getArguments().getInt(ARG_PARAM1);
      mDeckIdParam = getArguments().getInt(ARG_PARAM2);
      mNameParam = getArguments().getString(ARG_PARAM3);
    }
    cards = new ArrayList<>();
    deck = new Deck(mNameParam, mDeckIdParam);
  }

  @Override
  public void onResume() {
    super.onResume();
    mDeckFragmentPresenter.loadDeck(String.valueOf(mDeckIdParam));
    catalogAdapter.notifyDataSetChanged();
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
    catalogAdapter = new DeckCatalogAdapter(cards);
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
              }
              @Override
              public void onItemLongClick(View view, int position) {
                // Do nothing
              }
            }));
    //mDeckRecyclerView.addItemDecoration
    //    (new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
    mListener.getAllCards();
    return mDeckFragmentView;
  }

  @Override
  public void onAttach(Context activity) {
    super.onAttach(activity);
    mListener = (DeckFragmentListener) activity;
  }

  @Override
  public void onPause() {
    super.onPause();
    mDeckFragmentPresenter.saveDeck(deck);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  public void setCardList(List<Card> list) {
    cards.clear();
    cards.addAll(list);
    mEmptyTextView.setVisibility(View.GONE);
    mLoadingView.setVisibility(View.GONE);
    catalogAdapter.notifyDataSetChanged();
  }

  public void setListEmpty() {
    cards.clear();
    mEmptyTextView.setVisibility(View.VISIBLE);
    mLoadingView.setVisibility(View.GONE);
    catalogAdapter.notifyDataSetChanged();
  }

  public interface DeckFragmentListener {
    void getAllCards();
  }

  public void addDeckCard(Card card) {
    int indexFound = -1;
    int size = deck.getCardList().size();
    for (int k = 0; k < size; k++) {
      if (deck.getCardList().get(k).getCardId().equals(card.getCardId())) {
        indexFound = k;
      }
    }
    if (indexFound != -1) {
      int count = deck.getCardList().get(indexFound).getCardCount();
      if (count < 2) {
        count++;
        deck.getCardList().get(indexFound).setCardCount(count);
      }
    } else {
      card.setCardCount(1);
      deck.getCardList().add(card);
    }
    deckAdapter.notifyDataSetChanged();
  }

  public List<Card> getAdapterCards() {
    return cards;
  }
  public Deck getFragmentDeck() {
    return deck;
  }

  public RecyclerView.Adapter getDeckAdapter() {
    return deckAdapter;
  }
}
