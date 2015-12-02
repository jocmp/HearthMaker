package edu.gvsu.cis.campbjos.hearthstonebuilder;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Deck;
import edu.gvsu.cis.campbjos.hearthstonebuilder.UI.CardIconCrop;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

  private String cardRarity;
  private String dustCost;
  private String cardSet;
  private static final int STAR_EMPTY = R.drawable.star_icon_empty;
  private static final int STAR_FILLED = R.drawable.star_icon_filled;
  private String cardJson;
  private Card mCard;
  private boolean validAdd;
  private String mFileName;
  private String validReason;

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
    validAdd = getIntent().getBooleanExtra("isValid", false);
    validReason = getIntent().getStringExtra("validReason");
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

    Glide.with(this)
        .load(goldURL)
        .placeholder(R.drawable.placeholder_gold)
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
    if (cardImage.getVisibility() == View.VISIBLE) {
      menu.findItem(R.id.gold_card_button).setIcon(STAR_EMPTY);
    } else {
      menu.findItem(R.id.gold_card_button).setIcon(STAR_FILLED);
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
    JsonObject currentDeckObject = readFileStreamToJson(filename, jsonParser);

    Gson gson = new Gson();
    if (currentDeckObject == null) {
      return "Invalid add";
    }
    JsonArray array = currentDeckObject.get("cards").getAsJsonArray();
    boolean cardFound = false;
    String message = "";
    JsonObject currentObj;
    for (int k = 0; k < array.size(); k++) {
      currentObj = gson.fromJson(array.get(k).getAsString(), JsonObject.class);
      if (currentObj.get("name").getAsString().equals(mCard.getCardName())) {
        cardFound = true;
        int count = currentObj.get("cardCount").getAsInt();
        if (count < 2) {
          count++;
          currentDeckObject.get("cards")
              .getAsJsonArray().remove(k);
          mCard.setCardCount(count);
          currentDeckObject
              .get("cards").getAsJsonArray().add(new JsonPrimitive(gson.toJson(mCard)));
          message = String.format("Added %s to deck", mCard.getCardName());
        } else {
          message = String.format("You can only have 2 %s", mCard.getCardName());
        }
      }
    }
    if (!cardFound) {
      int cardCount = mCard.getCardCount();
      mCard.setCardCount(cardCount += 1);
      currentDeckObject.get("cards").getAsJsonArray().add(new JsonPrimitive(gson.toJson(mCard)));
      message = String.format("Added %s to deck", mCard.getCardName());
    }
    try {
      OutputStreamWriter outputStreamWriter
          = new OutputStreamWriter(openFileOutput(filename, Context.MODE_PRIVATE));
      outputStreamWriter.write(currentDeckObject.toString());
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
      if (mFileName != null && validAdd) {
        String message = addCardToDeck(mFileName);
        Snackbar.make(detailCoordinatorView, message, Snackbar.LENGTH_SHORT).show();
      } else if (mFileName != null) {
        Snackbar.make(detailCoordinatorView, validReason, Snackbar.LENGTH_SHORT).show();
      } else {
        Snackbar.make(detailCoordinatorView, "Invalid add", Snackbar.LENGTH_SHORT).show();
      }
    }
  }
}