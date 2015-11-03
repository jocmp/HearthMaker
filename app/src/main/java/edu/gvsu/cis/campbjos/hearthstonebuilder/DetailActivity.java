package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DetailActivity extends AppCompatActivity {

  @InjectView(R.id.toolbar)
  Toolbar toolbar;
  @InjectView(R.id.cardView)
  ImageView cardImage;
  @InjectView(R.id.toolbar_layout)
  CollapsingToolbarLayout cToolLayout;
  @InjectView(R.id.cardDetails)
  TextView cardData;
  @InjectView(R.id.classDetail)
  TextView classDetail;
  @InjectView(R.id.classGridImage)
  ImageView classImage;
  @InjectView(R.id.rarityDetail)
  TextView rarityDetail;
  @InjectView(R.id.rarityGridImage)
  ImageView rarityImage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    ButterKnife.inject(this);
    setSupportActionBar(toolbar);

    Intent intent = getIntent();
    String imageURL = intent.getStringExtra("card");
    String cardName = intent.getStringExtra("name");
    String cardFlavor = intent.getStringExtra("flavor");
    String cardClass = intent.getStringExtra("class");
    String cardRarity = intent.getStringExtra("rarity");

    if(cardRarity.equals("Free")) {
      cardRarity = "Common";
    }

    rarityDetail.setText(cardRarity);
    cardData.setText("\""+cardFlavor+"\"");
    classDetail.setText(cardClass);
    cToolLayout.setTitle(cardName);
    Picasso.with(this).load(imageURL).into(cardImage);

    switch (cardClass) {
      case "Warrior":
        classImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.warrior_icon));
        break;
      case "Neutral":
        classImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.placeholder));
        break;
      case "Druid":
        classImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.druid_icon));
        break;
      case "Hunter":
        classImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.hunter_icon));
        break;
      case "Mage":
        classImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.mage_icon));
        break;
      case "Paladin":
        classImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.paladin_icon));
        break;
      case "Priest":
        classImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.priest_icon));
        break;
      case "Rogue":
        classImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rogue_icon));
        break;
      case "Shaman":
        classImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.shaman_icon));
        break;
      case "Warlock":
        classImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.warlock_icon));
        break;
      default:
        break;
    }

    switch (cardRarity) {
      case "Common":
        rarityImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rarity_common));
        break;
      case "Free":
        rarityImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rarity_common));
        break;
      case "Rare":
        rarityImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rarity_rare));
        break;
      case "Epic":
        rarityImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rarity_epic));
        break;
      case "Legendary":
        rarityImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rarity_legendary));
        break;
      default:
        break;
    }
  }
  

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_scrolling, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
