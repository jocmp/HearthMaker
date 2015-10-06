package edu.gvsu.cis.campbjos.hearthstonebuilder.CustomAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;
import edu.gvsu.cis.campbjos.hearthstonebuilder.R;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

  private ArrayList<Card> cardData;

  public CardAdapter(ArrayList<Card> myDataset) {
    cardData = myDataset;
  }
    private static Context context;
  @Override
  public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
      context = parent.getContext();
      return new ViewHolder(LayoutInflater
        .from(parent.getContext())
        .inflate(R.layout.item_two_line, parent, false));

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

    public ViewHolder(View v) {
      super(v);
      itemTitle = (TextView) v.findViewById(R.id.item_title);
      itemSubtitle = (TextView) v.findViewById(R.id.item_subtitle);
      itemManaCost = (TextView) v.findViewById(R.id.mana_cost);
      itemRarity = (TextView) v.findViewById(R.id.rarity);
      itemAttack = (TextView) v.findViewById(R.id.attack);
      itemHealthDurability = (TextView) v.findViewById(R.id.health);
    }

    public void setEntry(Card card) {
        //sets text values. These are used by all cards
        itemTitle.setText(card.getCardName());
        itemSubtitle.setText(card.getTextDescription());
        itemManaCost.setText(Integer.toString(card.getCost()));

        //switch rarity
        switch(card.getRarity()) {
            case "Common":
                itemRarity.setBackground(context.getResources().getDrawable(R.drawable.rarity_common));
                break;
            case "Rare":
                itemRarity.setBackground(context.getResources().getDrawable(R.drawable.rarity_rare));
                break;
            case "Epic":
                itemRarity.setBackground(context.getResources().getDrawable(R.drawable.rarity_epic));
                break;
            case "Legendary":
                itemRarity.setBackground(context.getResources().getDrawable(R.drawable.rarity_legendary));
                break;
         }

        //hides and sets based on types
        switch (card.getType()){
            case "Minion":
                itemHealthDurability.setText(Integer.toString(card.getHealth()));
                itemAttack.setText(Integer.toString(card.getAttack()));
                break;
            case "Spell":
                itemHealthDurability.setVisibility(itemView.INVISIBLE);
                itemAttack.setVisibility(itemView.INVISIBLE);
                break;
            case "Weapon":
                itemHealthDurability.setText(Integer.toString(card.getDurability()));
                itemHealthDurability.setBackground(context.getResources().getDrawable(R.drawable.durability_weapon));
                itemAttack.setBackground(context.getResources().getDrawable(R.drawable.attack_weapon));
                itemAttack.setText(Integer.toString(card.getAttack()));
                break;
        }
    }
  }
}
