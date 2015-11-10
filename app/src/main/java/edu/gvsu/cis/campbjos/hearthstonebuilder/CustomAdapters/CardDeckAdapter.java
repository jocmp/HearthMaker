package edu.gvsu.cis.campbjos.hearthstonebuilder.CustomAdapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;
import edu.gvsu.cis.campbjos.hearthstonebuilder.R;

import butterknife.InjectView;

public class CardDeckAdapter extends RecyclerView.Adapter<CardDeckAdapter.ViewHolder> {

  private List<Card> cardData;

  public CardDeckAdapter(List<Card> myDataset) {
    cardData = myDataset;
  }

  public Card getPositionInfo(int position) {
    return cardData.get(position);
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater
        .from(parent.getContext())
        .inflate(R.layout.item_small_one_line, parent, false), parent.getContext());
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.setEntry(cardData.get(position));
  }

  @Override
  public int getItemCount() {
    return cardData.size();
  }



  public static class ViewHolder extends RecyclerView.ViewHolder {
    private TextView itemTitle;
    private TextView manaCost;
    private TextView amountTextView;
    private View cardView;
    private Context context;
    private Card currentCard;

    /**
     * Viewholder class for each RecyclerView item.
     *
     * @param view   view
     * @param parent Parent context
     */
    public ViewHolder(View view, Context parent) {
      super(view);
        cardView = view;
        amountTextView = (TextView) cardView.findViewById(R.id.amountTextView);
        itemTitle = (TextView) cardView.findViewById(R.id.item_title);
        manaCost = (TextView) cardView.findViewById(R.id.mana_cost_deck);
        context = parent;
    }

    private void setEntry(Card card) {
      //sets text values. These are used by all cards
      currentCard = card;
      itemTitle.setText(currentCard.getCardName());
      manaCost.setText(String.valueOf(card.getCost()));
      if (card.getCardCount() < 2) {
        amountTextView.setVisibility(View.GONE);
      } else {
        amountTextView.setText(String.valueOf(card.getCardCount()));
        amountTextView.setVisibility(View.VISIBLE);
      }
      switch (currentCard.getPlayerClass()) {
        case "Warrior":
          cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.warrior));
          break;
        case "Neutral":
          cardView.setBackgroundColor(ContextCompat.getColor(context,R.color.neutral));
          break;
        case "Druid":
          cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.druid));
          break;
        case "Hunter":
          cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.hunter));
          break;
        case "Mage":
          cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.mage));
          break;
        case "Paladin":
          cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.paladin));
          break;
        case "Priest":
          cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.priest));
          break;
        case "Rogue":
          cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.rogue));
          break;
        case "Shaman":
          cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.shaman));
          break;
        case "Warlock":
          cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.warlock));
          break;
        default:
          break;
      }
    }
  }
}
