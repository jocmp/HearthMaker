package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.gvsu.cis.campbjos.hearthstonebuilder.CustomAdapters.CardAdapter;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;
import edu.gvsu.cis.campbjos.hearthstonebuilder.UI.DividerItemDecoration;

public class CardViewFragment extends Fragment implements FragmentView {

  @InjectView(R.id.card_recyclerview)
  RecyclerView mCategoryRecycler;
  @InjectView(R.id.loading_spinner)
  View mLoadingView;
  @InjectView(R.id.empty_event_text)
  TextView mEmptyTextView;

  private OnFragmentInteractionListener mListener;
  private ArrayList<Card> cards;
  private CardAdapter adapter;
  private View mCardFragmentView;

  public static CardViewFragment newInstance() {
    return new CardViewFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mListener = (OnFragmentInteractionListener) getActivity();
    cards = new ArrayList<>();
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //mLoadingView.setVisibility(View.VISIBLE);
    mListener.getAllCards();

  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mCardFragmentView = inflater.inflate(R.layout.fragment_card_view, container, false);

    ButterKnife.inject(this, mCardFragmentView);

    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),
        LinearLayoutManager.VERTICAL, false);
    mCategoryRecycler.setHasFixedSize(true);
    mCategoryRecycler.isVerticalScrollBarEnabled();
    mCategoryRecycler.setLayoutManager(mLayoutManager);
    adapter = new CardAdapter(cards);
    mCategoryRecycler.setAdapter(adapter);
    mCategoryRecycler.addOnItemTouchListener(
        new RecyclerItemClickListener(getActivity(), mCategoryRecycler,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                // Reduce the number of calls to the adapter
                Card intentCard =  adapter.getPositionInfo(position);
                //Important Intents
                intent.putExtra("card",intentCard.getImageUrl());
                intent.putExtra("name",intentCard.getCardName());
                intent.putExtra("flavor",intentCard.getFlavor());
                intent.putExtra("class",intentCard.getPlayerClass());
                intent.putExtra("rarity",intentCard.getRarity());
                intent.putExtra("set",intentCard.getCardSet());
                intent.putExtra("type",intentCard.getType());

                //Alt. Intents
                intent.putExtra("health",intentCard.getHealth());
                intent.putExtra("attack",intentCard.getAttack());
                intent.putExtra("artist",intentCard.getArtist());
                intent.putExtra("cost",intentCard.getCost());
                intent.putExtra("text",intentCard.getText());
                intent.putExtra("durability",intentCard.getDurability());
                intent.putExtra("gold",intentCard.getGoldImageUrl());
                startActivity(intent);
              }

              @Override
              public void onItemLongClick(View view, int position) {
                // Reduce the number of calls to the adapter
                Card intentCard =  adapter.getPositionInfo(position);

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("card",intentCard.getImageUrl());
                intent.putExtra("name",intentCard.getCardName());
                intent.putExtra("flavor",intentCard.getFlavor());
                intent.putExtra("class",intentCard.getPlayerClass());
                intent.putExtra("rarity",intentCard.getRarity());
                intent.putExtra("set",intentCard.getCardSet());
                intent.putExtra("type",intentCard.getType());

                //Alt. Intents
                intent.putExtra("health",intentCard.getHealth());
                intent.putExtra("attack",intentCard.getAttack());
                intent.putExtra("artist",intentCard.getArtist());
                intent.putExtra("mana",intentCard.getCost());
                intent.putExtra("text",intentCard.getText());
                intent.putExtra("gold",intentCard.getGoldImageUrl());
                startActivity(intent);
              }

            }));
    mCategoryRecycler.addItemDecoration
            (new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
    return mCardFragmentView;
  }

  public View getProgressView() {
    return mLoadingView;
  }

  public void setCardList(List<Card> list) {
    cards.clear();
    cards.addAll(list);
    if (cards.isEmpty())
      mEmptyTextView.setVisibility(View.VISIBLE);
    else
      mEmptyTextView.setVisibility(View.GONE);
    mLoadingView.setVisibility(View.GONE);

    adapter.notifyDataSetChanged();
  }

  public void setListEmpty() {
    cards.clear();
    mEmptyTextView.setVisibility(View.VISIBLE);
    mLoadingView.setVisibility(View.GONE);
    adapter.notifyDataSetChanged();
  }

  @Override
  public void setProgress(boolean isLoading) {
    if (isLoading){
      mLoadingView.setVisibility(View.VISIBLE);
      mEmptyTextView.setVisibility(View.GONE);
    } else {
      mLoadingView.setVisibility(View.GONE);
      mEmptyTextView.setVisibility(View.VISIBLE);
    }
  }

  public interface OnFragmentInteractionListener {
    void getAllCards();
  }

  public List<Card> getCards() {
    return cards;
  }
}