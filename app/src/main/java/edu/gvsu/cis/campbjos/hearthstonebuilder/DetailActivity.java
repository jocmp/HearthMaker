package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.gvsu.cis.campbjos.hearthstonebuilder.UI.CardIconCrop;

public class DetailActivity extends AppCompatActivity {

  String cardRarity;
  String dustCost;
  String cardSet;
  private final static int STAR_EMPTY = R.drawable.star_icon_empty;
  private final static int STAR_FILLED = R.drawable.star_icon_filled;

  @InjectView(R.id.toolbar)
  Toolbar toolbar;
  @InjectView(R.id.cardView)
  ImageView cardImage;
  @InjectView(R.id.toolbar_layout)
  CollapsingToolbarLayout cToolLayout;
  @InjectView(R.id.flavor_text)
  TextView flavorText;
  @InjectView(R.id.class_name)
  TextView className;
  @InjectView(R.id.class_icon)
  ImageView classIcon;
  @InjectView(R.id.rarity_detail)
  TextView rarityDetail;
  @InjectView(R.id.rarity_image_big)
  ImageView rarityImage;
  @InjectView(R.id.card_type)
  TextView typeDetail;
  @InjectView(R.id.type_icon)
  ImageView typeIcon;
  @InjectView(R.id.dust_cost)
  TextView dustDetail;
  @InjectView(R.id.card_set)
  TextView setDetail;
  @InjectView(R.id.mana_cost)
  TextView manaDetail;
  @InjectView(R.id.attack_icon)
  ImageView attackIcon;
  @InjectView(R.id.attack_value)
  TextView attackDetail;
  @InjectView(R.id.health_icon)
  ImageView healthIcon;
  @InjectView(R.id.health_value)
  TextView healthDetail;
  @InjectView(R.id.card_text)
  TextView textDetail;
  @InjectView(R.id.artist_name)
  TextView artistDetail;
  @InjectView(R.id.health_layout)
  RelativeLayout healthLayout;
  @InjectView(R.id.attack_layout)
  RelativeLayout attackLayout;
  @InjectView(R.id.card_icon)
  ImageView cardIcon;
  @InjectView(R.id.gold_card)
  ImageView gifImage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    ButterKnife.inject(this);
    setSupportActionBar(toolbar);

    Glide.get(this).clearMemory();

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    Intent intent = getIntent();
    String imageURL = intent.getStringExtra("card");
    String cardName = intent.getStringExtra("name");
    String cardFlavor = intent.getStringExtra("flavor");
    String cardClass = intent.getStringExtra("class");
    cardRarity = intent.getStringExtra("rarity");
    cardSet = intent.getStringExtra("set");
    String cardType = intent.getStringExtra("type");
    int cardHealth = intent.getIntExtra("health", 0);
    int cardAttack = intent.getIntExtra("attack", 0);
    String cardArtist = intent.getStringExtra("artist");
    int cardMana = intent.getIntExtra("cost", 0);
    int cardDura = intent.getIntExtra("durability", 0);
    String cardText = intent.getStringExtra("text");
    String goldURL = intent.getStringExtra("gold");
    dustCost = "N/A";

    if(cardSet.equals("Basic") || cardSet.equals("Reward")) {
      dustCost = "Uncraftable";
    }
    else {
      switch (cardRarity) {
        case "Free":
          dustCost = "Uncraftable";
          break;
        case "Common":
          dustCost = "40";
          break;
        case "Rare":
          dustCost = "100";
          break;
        case "Epic":
          dustCost = "400";
          break;
        case "Legendary":
          dustCost = "1600";
          break;
      }
    }

    if (cardRarity != null && cardRarity.equals("Free")) {
      cardRarity = "Basic";
    }

    cardText = cardText.replaceAll("[$#]", "");

    if (cardText.equals("")) {
      cardText = "No Card Text";
    }

    cToolLayout.setExpandedTitleColor(Color.parseColor("#00FFFFFF"));

    rarityDetail.setText(cardRarity);
    //get rid of html tags
    cardFlavor = Html.fromHtml(cardFlavor).toString();
    flavorText.setText("-\"" + cardFlavor + "\"");
    className.setText(cardClass);
    cToolLayout.setTitle(cardName);
    typeDetail.setText(cardType);
    textDetail.setText(cardText);
    dustDetail.setText(dustCost);
    artistDetail.setText("Artist: " + cardArtist);
    setDetail.setText(cardSet);
    manaDetail.setText(String.valueOf(cardMana));

    Glide.with(DetailActivity.this)
            .load(goldURL)
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .into(gifImage);

    Picasso.with(this).load(imageURL).into(cardImage);
    Picasso.with(this).load(imageURL).transform(CardIconCrop.getCardIconCrop()).into(cardIcon);

    switch (cardClass) {
      case "Warrior":
        classIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.warrior_icon));
        break;
      case "Neutral":
        classIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.placeholder));
        break;
      case "Druid":
        classIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.druid_icon));
        break;
      case "Hunter":
        classIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.hunter_icon));
        break;
      case "Mage":
        classIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.mage_icon));
        break;
      case "Paladin":
        classIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.paladin_icon));
        break;
      case "Priest":
        classIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.priest_icon));
        break;
      case "Rogue":
        classIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rogue_icon));
        break;
      case "Shaman":
        classIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.shaman_icon));
        break;
      case "Warlock":
        classIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.warlock_icon));
        break;
      default:
        break;
    }

    switch (cardRarity) {
      case "Basic":
        rarityImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rarity_common_500));
        break;
      case "Common":
        rarityImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rarity_common_500));
        break;
      case "Rare":
        rarityImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rarity_rare_500));
        break;
      case "Epic":
        rarityImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rarity_epic_500));
        break;
      case "Legendary":
        rarityImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rarity_legendary_500));
        break;
      default:
        break;
    }

    switch(cardType) {
      case ("Minion"):
        typeIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.minion_icon2));
        attackDetail.setText(String.valueOf(cardAttack));
        attackIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.attack_minion_500));
        healthDetail.setText(String.valueOf(cardHealth));
        healthIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.health_minion_500));
        break;
      case ("Spell"):
        typeIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.spell_icon2));
        healthLayout.setVisibility(View.GONE);
        attackLayout.setVisibility(View.GONE);
        break;
      case ("Weapon"):
        attackDetail.setText(String.valueOf(cardAttack));
        attackIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.attack_weapon_500));
        typeIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.weapon_icon2));
        healthDetail.setText(String.valueOf(cardDura));
        healthIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.durability_weapon_500));
        break;
    }
  }
  

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.detail_toolbar, menu);
    if(cardImage.getVisibility() == View.VISIBLE) {
      menu.findItem(R.id.gold_card_button).setIcon(STAR_EMPTY);
    }
    else {
      menu.findItem(R.id.gold_card_button).setIcon(STAR_FILLED);
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.

    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
      case R.id.gold_card_button:
        if (cardImage.getVisibility() == View.VISIBLE) {
          cardImage.setVisibility(View.INVISIBLE);
          gifImage.setVisibility(View.VISIBLE);

          if(cardSet.equals("Basic") || cardSet.equals("Reward")) {
            dustCost = "Uncraftable";
          }
          else {
            switch (cardRarity) {
              case "Free":
                dustCost = "Uncraftable";
                break;
              case "Common":
                dustCost = "400";
                break;
              case "Rare":
                dustCost = "800";
                break;
              case "Epic":
                dustCost = "1600";
                break;
              case "Legendary":
                dustCost = "3200";
                break;
            }
          }
        }
        else {
          cardImage.setVisibility(View.VISIBLE);
          gifImage.setVisibility(View.INVISIBLE);

          if(cardSet.equals("Basic") || cardSet.equals("Reward")) {
            dustCost = "Uncraftable";
          }
          else {
            switch (cardRarity) {
              case "Free":
                dustCost = "Uncraftable";
                break;
              case "Common":
                dustCost = "40";
                break;
              case "Rare":
                dustCost = "100";
                break;
              case "Epic":
                dustCost = "400";
                break;
              case "Legendary":
                dustCost = "1600";
                break;
            }
          }
        }

        dustDetail.setText(dustCost);
        invalidateOptionsMenu();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
