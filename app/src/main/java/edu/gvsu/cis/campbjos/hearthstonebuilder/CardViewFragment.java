package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

public class CardViewFragment extends Fragment {

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
    mListener.getAllCards();
    mLoadingView.setVisibility(View.VISIBLE);
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
        new RecyclerItemClickListener(getActivity(),
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity() ,DetailActivity.class);
                intent.putExtra("card", adapter.getPositionInfo(position).getImageUrl());
                intent.putExtra("name", adapter.getPositionInfo(position).getCardName());
                intent.putExtra("flavor", adapter.getPositionInfo(position).getFlavor());
                intent.putExtra("class", adapter.getPositionInfo(position).getPlayerClass());
                intent.putExtra("rarity", adapter.getPositionInfo(position).getRarity());
                startActivity(intent);
              }
            })
    );
    mCategoryRecycler.addItemDecoration
        (new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
    return mCardFragmentView;
  }

  public void onMessage(String s) {
    final CardViewFragment frag = this;
    Snackbar.make(mCardFragmentView, s, Snackbar.LENGTH_INDEFINITE).setAction("Retry",
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            mListener.getAllCards();
            mEmptyTextView.setVisibility(View.GONE);
            mLoadingView.setVisibility(View.VISIBLE);
          }
        }).show();
  }

  public View getProgressView() {
    return mLoadingView;
  }

  public void setCardList(List<Card> list) {
    cards.clear();
    cards.addAll(list);
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
  public interface OnFragmentInteractionListener {
    void getAllCards();
  }
}