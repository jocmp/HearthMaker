package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    ButterKnife.inject(this);
    setSupportActionBar(toolbar);

    Intent intent = getIntent();
    String imageURL = intent.getStringExtra("card");
    String cardName = intent.getStringExtra("name");
    cToolLayout.setTitle(cardName);
    Picasso.with(this).load(imageURL).into(cardImage);
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
