package edu.gvsu.cis.campbjos.hearthstonebuilder.CustomAdapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.gvsu.cis.campbjos.hearthstonebuilder.UI.CardIconCrop;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;
import edu.gvsu.cis.campbjos.hearthstonebuilder.R;

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
    ImageView itemRarity;
    TextView itemAttack;
    TextView itemHealthDurability;
    ImageView itemHealthDurabilityImage;
    ImageView itemIcon;
    RelativeLayout itemHealthDurabilityGrid;
    View cardView;
    private Context context;
    private Card currentCard;
    private CardIconCrop crop;

    /**
     * Viewholder class for each RecyclerView item.
     *
     * @param view   view
     * @param parent Parent context
     */
    public ViewHolder(View view, Context parent) {
      super(view);
      cardView = view;
      itemIcon = (ImageView) cardView.findViewById(R.id.card_icon);
      itemTitle = (TextView) cardView.findViewById(R.id.item_title);
      itemSubtitle = (TextView) cardView.findViewById(R.id.item_subtitle);
      itemManaCost = (TextView) cardView.findViewById(R.id.mana_cost);
      itemRarity = (ImageView) cardView.findViewById(R.id.rarity);
      itemAttack = (TextView) cardView.findViewById(R.id.attack);
      itemHealthDurability = (TextView) view.findViewById(R.id.health);
      itemHealthDurabilityImage = (ImageView) cardView.findViewById(R.id.health_image);
      itemHealthDurabilityGrid = (RelativeLayout) cardView.findViewById(R.id.health_grid);
      context = parent;
    }

    private void setEntry(Card card) {
      //sets text values. These are used by all cards
      currentCard = card;
      itemTitle.setText(currentCard.getCardName());
      itemSubtitle.setText(currentCard.getTextDescription());
      itemManaCost.setText(Integer.toString(card.getCost()));

      Picasso.with(context).load(card.getImageUrl())
          .transform(new CardIconCrop())
          .into(itemIcon);


      //switch rarity
      switch (currentCard.getRarity()) {
        case "Common":
          itemRarity.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rarity_common));
          break;
        case "Free":
          itemRarity.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rarity_common));
          break;
        case "Rare":
          itemRarity.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rarity_rare));
          break;
        case "Epic":
          itemRarity.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rarity_epic));
          break;
        case "Legendary":
          itemRarity.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rarity_legendary));
          break;
        default:
          break;
      }

      //hides and sets based on types
      switch (currentCard.getType()) {
        case "Minion":
          itemHealthDurability.setText(Integer.toString(card.getHealth()));
          itemAttack.setText(Integer.toString(card.getAttack()));
          itemHealthDurabilityImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.health_minion));
          itemAttack.setBackground(ContextCompat.getDrawable(context, R.drawable.attack_minion));
          itemHealthDurabilityGrid.setVisibility(View.VISIBLE);
          itemAttack.setVisibility(View.VISIBLE);
          break;
        case "Weapon":
          itemHealthDurability.setText(Integer.toString(card.getDurability()));
          itemHealthDurabilityImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.durability_weapon));
          itemAttack.setBackground(ContextCompat.getDrawable(context, R.drawable.attack_weapon));
          itemAttack.setText(Integer.toString(card.getAttack()));
          //itemAttack.setPaddingRelative(0,0,0,0);
          itemHealthDurabilityGrid.setVisibility(View.VISIBLE);
          itemAttack.setVisibility(View.VISIBLE);
          break;
        case "Spell":
          itemHealthDurabilityGrid.setVisibility(View.INVISIBLE);
          itemAttack.setVisibility(View.INVISIBLE);
          break;
        default:
          break;
      }

      switch (currentCard.getPlayerClass()) {
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
        case "Warrior":
          cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.warrior));
          break;
        default:
          break;
      }
    }
  }
}
