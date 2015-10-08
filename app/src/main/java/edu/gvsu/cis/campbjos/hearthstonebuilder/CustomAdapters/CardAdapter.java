package edu.gvsu.cis.campbjos.hearthstonebuilder.CustomAdapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;
import edu.gvsu.cis.campbjos.hearthstonebuilder.R;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

  private ArrayList<Card> cardData;

  public CardAdapter(ArrayList<Card> myDataset) {
    cardData = myDataset;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater
        .from(parent.getContext())
        .inflate(R.layout.item_two_line, parent, false), parent.getContext());
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
    TextView itemTitle;
    TextView itemSubtitle;
    TextView itemManaCost;
    TextView itemRarity;
    TextView itemAttack;
    TextView itemHealthDurability;
    View cardView;
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
      itemTitle = (TextView) cardView.findViewById(R.id.item_title);
      itemSubtitle = (TextView) cardView.findViewById(R.id.item_subtitle);
      itemManaCost = (TextView) cardView.findViewById(R.id.mana_cost);
      itemRarity = (TextView) cardView.findViewById(R.id.rarity);
      itemAttack = (TextView) cardView.findViewById(R.id.attack);
      itemHealthDurability = (TextView) view.findViewById(R.id.health);
      context = parent;
    }

    private void setEntry(Card card) {
      //sets text values. These are used by all cards
      currentCard = card;
      itemTitle.setText(currentCard.getCardName());
      itemSubtitle.setText(currentCard.getTextDescription());
      itemManaCost.setText(Integer.toString(card.getCost()));

      //switch rarity
      switch (currentCard.getRarity()) {
        case "Common":
          itemRarity.setBackground(ContextCompat.getDrawable(context, R.drawable.rarity_common));
          break;
        case "Free":
          itemRarity.setBackground(ContextCompat.getDrawable(context, R.drawable.rarity_common));
          break;
        case "Rare":
          itemRarity.setBackground(ContextCompat.getDrawable(context, R.drawable.rarity_rare));
          break;
        case "Epic":
          itemRarity.setBackground(ContextCompat.getDrawable(context, R.drawable.rarity_epic));
          break;
        case "Legendary":
          itemRarity.setBackground(ContextCompat.getDrawable(context, R.drawable.rarity_legendary));
          break;
        default:
          break;
      }

      //hides and sets based on types
      switch (currentCard.getType()) {
        case "Minion":
          itemHealthDurability.setText(Integer.toString(card.getHealth()));
          itemAttack.setText(Integer.toString(card.getAttack()));
          itemHealthDurability.setBackground(
              ContextCompat.getDrawable(context, R.drawable.health_minion));
          itemAttack.setBackground(ContextCompat.getDrawable(context, R.drawable.attack_minion));
          itemHealthDurability.setVisibility(View.VISIBLE);
          itemAttack.setVisibility(View.VISIBLE);
          break;
        case "Weapon":
          itemHealthDurability.setText(Integer.toString(card.getDurability()));
          itemHealthDurability.setBackground(
              ContextCompat.getDrawable(context, R.drawable.durability_weapon));
          itemAttack.setBackground(ContextCompat.getDrawable(context, R.drawable.attack_weapon));
          itemAttack.setText(Integer.toString(card.getAttack()));
          itemHealthDurability.setVisibility(View.VISIBLE);
          itemAttack.setVisibility(View.VISIBLE);
          break;
        case "Spell":
          itemHealthDurability.setVisibility(View.INVISIBLE);
          itemAttack.setVisibility(View.INVISIBLE);
          break;
        default:
          break;
      }

      switch (currentCard.getPlayerClass()) {
        case "Neutral":
          cardView.setBackgroundColor(Color.parseColor("#E0E0E0"));
          break;
        case "Druid":
          cardView.setBackgroundColor(Color.parseColor("#44FF7D0A"));
          break;
        case "Hunter":
          cardView.setBackgroundColor(Color.parseColor("#44ABD473"));
          break;
        case "Mage":
          cardView.setBackgroundColor(Color.parseColor("#4469CCF0"));
          break;
        case "Paladin":
          cardView.setBackgroundColor(Color.parseColor("#44F58CBA"));
          break;
        case "Priest":
          cardView.setBackgroundColor(Color.parseColor("#FFFFFF"));
          break;
        case "Rogue":
          cardView.setBackgroundColor(Color.parseColor("#44FFF569"));
          break;
        case "Shaman":
          cardView.setBackgroundColor(Color.parseColor("#440070DE"));
          break;
        case "Warlock":
          cardView.setBackgroundColor(Color.parseColor("#449482C9"));
          break;
        case "Warrior":
          cardView.setBackgroundColor(Color.parseColor("#44C79C6E"));
          break;
        default:
          break;
      }
    }
  }
}
