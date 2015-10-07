package edu.gvsu.cis.campbjos.hearthstonebuilder.CustomAdapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;
import edu.gvsu.cis.campbjos.hearthstonebuilder.R;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private ArrayList<Card> cardData;
    private static Context context;

    public CardAdapter(ArrayList<Card> myDataset) {
        cardData = myDataset;
    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
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
        RelativeLayout view;
        private Context context;
        private Card currentCard;

        public ViewHolder(View v, Context parent) {
            super(v);
            itemTitle = (TextView) v.findViewById(R.id.item_title);
            itemSubtitle = (TextView) v.findViewById(R.id.item_subtitle);
            itemManaCost = (TextView) v.findViewById(R.id.mana_cost);
            itemRarity = (TextView) v.findViewById(R.id.rarity);
            itemAttack = (TextView) v.findViewById(R.id.attack);
            itemHealthDurability = (TextView) v.findViewById(R.id.health);
            view = (RelativeLayout) v.findViewById(R.id.list_item);
            context = parent;
        }

        public void setEntry(Card card) {
            //sets text values. These are used by all cards
            currentCard = card;
            itemTitle.setText(currentCard.getCardName());
            itemSubtitle.setText(currentCard.getTextDescription());
            itemManaCost.setText(Integer.toString(card.getCost()));

            //switch rarity
            switch (currentCard.getRarity()) {
                case "Free":
                    itemRarity.setVisibility(itemView.INVISIBLE);
                    break;
                case "Common":
                    itemRarity.setBackground(context.getResources().getDrawable(R.drawable.rarity_common));
                    itemRarity.setVisibility(itemView.VISIBLE);
                    break;
                case "Rare":
                    itemRarity.setBackground(context.getResources().getDrawable(R.drawable.rarity_rare));
                    itemRarity.setVisibility(itemView.VISIBLE);
                    break;
                case "Epic":
                    itemRarity.setBackground(context.getResources().getDrawable(R.drawable.rarity_epic));
                    itemRarity.setVisibility(itemView.VISIBLE);
                    break;
                case "Legendary":
                    itemRarity.setBackground(context.getResources().getDrawable(R.drawable.rarity_legendary));
                    itemRarity.setVisibility(itemView.VISIBLE);
                    break;
                default:
                    break;
            }

            //hides and sets based on types
            switch (currentCard.getType()) {
                case "Minion":
                    itemHealthDurability.setText(Integer.toString(card.getHealth()));
                    itemAttack.setText(Integer.toString(card.getAttack()));
                    itemHealthDurability.setBackground(context.getResources().getDrawable(R.drawable.health_minion));
                    itemAttack.setBackground(context.getResources().getDrawable(R.drawable.attack_minion));
                    itemHealthDurability.setVisibility(itemView.VISIBLE);
                    itemAttack.setVisibility(itemView.VISIBLE);
                    break;
                case "Weapon":
                    itemHealthDurability.setText(Integer.toString(card.getDurability()));
                    itemHealthDurability.setBackground(context.getResources().getDrawable(R.drawable.durability_weapon));
                    itemAttack.setBackground(context.getResources().getDrawable(R.drawable.attack_weapon));
                    itemAttack.setText(Integer.toString(card.getAttack()));
                    itemHealthDurability.setVisibility(itemView.VISIBLE);
                    itemAttack.setVisibility(itemView.VISIBLE);
                    break;
                case "Spell":
                    itemHealthDurability.setVisibility(itemView.INVISIBLE);
                    itemAttack.setVisibility(itemView.INVISIBLE);
                    break;
                default:
                    break;
            }

            switch (currentCard.getPlayerClass()){
                case "Neutral":
                    view.setBackgroundColor(Color.parseColor("#E0E0E0"));
                    break;
                case "Druid":
                    view.setBackgroundColor(Color.parseColor("#44FF7D0A"));
                    break;
                case "Hunter":
                    view.setBackgroundColor(Color.parseColor("#44ABD473"));
                    break;
                case "Mage":
                    view.setBackgroundColor(Color.parseColor("#4469CCF0"));
                    break;
                case "Paladin":
                    view.setBackgroundColor(Color.parseColor("#44F58CBA"));
                    break;
                case "Priest":
                    view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    break;
                case "Rogue":
                    view.setBackgroundColor(Color.parseColor("#44FFF569"));
                    break;
                case "Shaman":
                    view.setBackgroundColor(Color.parseColor("#440070DE"));
                    break;
                case "Warlock":
                    view.setBackgroundColor(Color.parseColor("#449482C9"));
                    break;
                case "Warrior":
                    view.setBackgroundColor(Color.parseColor("#44C79C6E"));
                    break;
                default:
                    break;
            }
        }
    }
}
