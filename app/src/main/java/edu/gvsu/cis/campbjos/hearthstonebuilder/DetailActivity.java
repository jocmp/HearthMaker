package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;
import edu.gvsu.cis.campbjos.hearthstonebuilder.UI.CardIconCrop;


public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

  private String cardRarity;
  private String dustCost;
  private String cardSet;
  private String cardJson;
  private Card mCard;
  private String mFileName;

  private final static int GOLD_ICON = R.drawable.ic_radio_button_on_24dp;
  private final static int NON_GOLD_ICON = R.drawable.ic_radio_button_off_24dp;
  private static final String TAG = "DetailActivity";

  @InjectView(R.id.toolbar)
  Toolbar toolbar;
  @InjectView(R.id.cardView)
  ImageView cardImage;
  @InjectView(R.id.toolbar_layout)
  CollapsingToolbarLayout mToolbarLayout;
  @InjectView(R.id.detailCoordinatorView)
  CoordinatorLayout detailCoordinatorView;
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
  @InjectView(R.id.add_fab)
  FloatingActionButton addFab;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    ButterKnife.inject(this);
    setSupportActionBar(toolbar);

    Glide.get(this).clearMemory();

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    addFab.setOnClickListener(this);
    cardJson = getIntent().getStringExtra("card");
    mFileName = getIntent().getStringExtra("fileName");
    mCard = JsonUtil.parseJsonToCard(cardJson);
    String imageURL = mCard.getImageUrl();
    String cardName = mCard.getCardName();
    String cardFlavor = mCard.getFlavor();
    String cardClass = mCard.getPlayerClass();
    cardRarity = mCard.getRarity();
    cardSet = mCard.getCardSet();
    String cardType = mCard.getType();
    int cardHealth = mCard.getHealth();
    int cardAttack = mCard.getAttack();
    String cardArtist = mCard.getArtist();
    int cardMana = mCard.getCost();
    int cardDurability = mCard.getDurability();
    String cardText = mCard.getText();
    String goldURL = mCard.getGoldImageUrl();
    dustCost = "N/A";

    if (mFileName == null) {
      addFab.setVisibility(View.INVISIBLE);
    }
    switch (cardRarity) {
      case "Free":
        dustCost = "Uncraftable";
        cardRarity = "Basic";
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
      default:
        if (cardSet.equals("Basic") || cardSet.equals("Reward")) {
          dustCost = "Uncraftable";
        }
    }

    cardText = cardText.replaceAll("[$#]", "");

    if (cardText.equals("")) {
      cardText = "No Card Text";
    }

    //set expanded color to transparent
    mToolbarLayout.setExpandedTitleColor(Color.parseColor("#00FFFFFF"));

    rarityDetail.setText(cardRarity);
    //get rid of html tags
    cardFlavor = Html.fromHtml(cardFlavor).toString();
    flavorText.setText(String.format("-\"%s\"", cardFlavor));
    className.setText(cardClass);
    mToolbarLayout.setTitle(cardName);
    typeDetail.setText(cardType);
    textDetail.setText(cardText);
    dustDetail.setText(dustCost);
    artistDetail.setText(String.format("Artist: %s", cardArtist));
    setDetail.setText(cardSet);
    manaDetail.setText(String.valueOf(cardMana));

    //load gif into the gold image view
    switch (cardType) {
      case "Spell":
      Glide.with(this)
              .load(goldURL)
              .placeholder(R.drawable.placeholder_gold_spell)
              .diskCacheStrategy(DiskCacheStrategy.SOURCE)
              .into(gifImage);
        break;
      case "Minion":
        Glide.with(this)
                .load(goldURL)
                .placeholder(R.drawable.placeholder_gold_minion)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(gifImage);
        break;
      case "Weapon":
        Glide.with(this)
                .load(goldURL)
                .placeholder(R.drawable.placeholder_gold_weapon)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(gifImage);
        break;
    }

    //load into normal card image view
    Picasso.with(this).load(imageURL).into(cardImage);
    //load into the icon
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

    switch (cardType) {
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
        healthDetail.setText(String.valueOf(cardDurability));
        healthIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.durability_weapon_500));
        break;
    }
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.detail_toolbar, menu);
    if(cardImage.getVisibility() == View.VISIBLE) {
      menu.findItem(R.id.gold_card_button).setIcon(NON_GOLD_ICON);
    } else {
      menu.findItem(R.id.gold_card_button).setIcon(GOLD_ICON);
    }
    return true;
  }

  @Override
  protected void onDestroy() {
    Glide.get(this).clearMemory();
    new ClearDiskCacheAsyncTask().execute(this);
    super.onDestroy();
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

          if (cardSet.equals("Basic") || cardSet.equals("Reward")) {
            dustCost = "Uncraftable";
          } else {
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
        } else {
          cardImage.setVisibility(View.VISIBLE);
          gifImage.setVisibility(View.INVISIBLE);

          if (cardSet.equals("Basic") || cardSet.equals("Reward")) {
            dustCost = "Uncraftable";
          } else {
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

  private String addCardToDeck(String filename) {
    JsonParser jsonParser = new JsonParser();
    JsonObject jsonDeck = readFileStreamToJson(filename, jsonParser);

    Gson gson = new Gson();
    if (jsonDeck == null) {
      return "Invalid add";
    }
    JsonArray array = jsonDeck.get("cards").getAsJsonArray();
    JsonObject currentObj;
    JsonObject foundObject = null;
    String message = "";
    int deckSize = 0;
    int foundIndex = -1;
    int foundCount = -1;
    for (int k = 0; k < array.size(); k++) {
      currentObj = gson.fromJson(array.get(k).getAsString(), JsonObject.class);
      deckSize += currentObj.get("cardCount").getAsInt();
      if (currentObj.get("name").getAsString().equals(mCard.getCardName())) {
        foundObject = currentObj;
        foundIndex = k;
      }
    }
    if (foundObject != null) {
      foundCount = foundObject.get("cardCount").getAsInt();
      if (foundObject.get("rarity").getAsString().equals("Legendary") && foundCount == 1) {
        return String.format("You can only have 1 %s", mCard.getCardName());
      }
      if (foundCount == 2) {
        return String.format("You can only have 2 %ss", mCard.getCardName());
      }
      if (foundCount < 2 && deckSize < 30) {
        foundCount++;
        mCard.setCardCount(foundCount);
        jsonDeck.get("cards").getAsJsonArray().remove(foundIndex);
        jsonDeck.get("cards").getAsJsonArray().add(new JsonPrimitive(gson.toJson(mCard)));
        message = String.format("Added %s to deck", mCard.getCardName());
      }
    } else if (deckSize < 30) {
      mCard.setCardCount(1);
      jsonDeck.get("cards").getAsJsonArray().add(new JsonPrimitive(gson.toJson(mCard)));
      message = String.format("Added %s to deck", mCard.getCardName());
    } else if (deckSize == 30) {
      return "Deck is full";
    }
    try {
      OutputStreamWriter outputStreamWriter
          = new OutputStreamWriter(openFileOutput(filename, Context.MODE_PRIVATE));
      outputStreamWriter.write(jsonDeck.toString());
      outputStreamWriter.close();
    } catch (IOException e) {
      return "Failed to add card to deck";
    }
    return message;
  }

  private JsonObject readFileStreamToJson(String fileName, JsonParser parser) {
    FileInputStream fis = null;
    JsonObject fileObject = new JsonObject();
    try {
      fis = this.openFileInput(fileName);
      fileObject = parser.parse(getString(fis)).getAsJsonObject();
      fis.close();
    } catch (FileNotFoundException e) {
      return null;
    } catch (IOException e) {
      return null;
    }
    return fileObject;
  }

  private static String getString(FileInputStream stream) throws IOException {

    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
    StringBuilder sb = new StringBuilder();

    String line = null;
    try {
      while ((line = reader.readLine()) != null) {
        sb.append(line).append("\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return sb.toString();
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.add_fab) {
      if (mFileName != null) {
        String message = addCardToDeck(mFileName);
        Snackbar.make(detailCoordinatorView, message, Snackbar.LENGTH_SHORT).show();
      } else {
        Snackbar.make(detailCoordinatorView, "Invalid add", Snackbar.LENGTH_SHORT).show();
      }
    }
  }
}