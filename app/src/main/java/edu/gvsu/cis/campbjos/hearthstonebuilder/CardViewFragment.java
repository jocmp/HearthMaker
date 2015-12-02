package edu.gvsu.cis.campbjos.hearthstonebuilder;

import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
  public void onResume() {
    super.onResume();
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
                configureDetailIntent(adapter.getPositionInfo(position));
              }

              @Override
              public void onItemLongClick(View view, int position) {
                configureDetailIntent(adapter.getPositionInfo(position));
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
    if (isLoading) {
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

  public void configureDetailIntent(Card card) {
    Intent intent = new Intent(getActivity(), DetailActivity.class);
    Gson gson = new Gson();
    String cardJson = gson.toJson(card);
    intent.putExtra("card", cardJson);
    startActivity(intent);
  }
}