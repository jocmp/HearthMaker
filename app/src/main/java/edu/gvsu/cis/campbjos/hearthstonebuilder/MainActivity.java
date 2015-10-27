/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import edu.gvsu.cis.campbjos.hearthstonebuilder.CustomAdapters.DrawerAdapter;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;
import edu.gvsu.cis.campbjos.hearthstonebuilder.presenters.MainActivityPresenter;
import edu.gvsu.cis.campbjos.hearthstonebuilder.services.HearthService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemSelected;

/**
 * This example illustrates a common usage of the DrawerLayout widget in the Android support
 * library.
 */
public class MainActivity extends AppCompatActivity implements
    CardViewFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener,
      DeckFragment.DeckFragmentListener, NewDeckDialog.DialogListener{

  @InjectView(R.id.spinner_class)
  Spinner mClassSpinner;
  @InjectView(R.id.spinner_cost)
  Spinner mCostSpinner;
  @InjectView(R.id.spinner_type)
  Spinner mTypeSpinner;
  @InjectView(R.id.spinner_rarity)
  Spinner mRaritySpinner;
  @InjectView(R.id.spinner_set)
  Spinner mSetSpinner;
  @InjectView(R.id.nav_view)
  NavigationView mDrawerList;
  @InjectView(R.id.spinner_layout)
  View mSpinnerView;
  private DrawerLayout mDrawerLayout;
  private ActionBarDrawerToggle mDrawerToggle;
  private CharSequence mDrawerTitle;
  private CharSequence mTitle;
  private String mCollectibleOption;
  private String mManifestHearthApiKey;
  private MainActivityPresenter mMainActivityPresenter;
  private HearthService mHearthService;
  private Fragment mFragment;
  private int count;

  private static ArrayList<Spinner> spinners;
  private static List<List<Card>> UserDecks;
  private static int[] idArray;
  static {
    spinners = new ArrayList<>();
    idArray = new int[]{R.array.card_class,
      R.array.cost, R.array.card_type, R.array.rarity, R.array.card_set};
    UserDecks = new ArrayList<>();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);
    setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    mTitle = mDrawerTitle = getTitle();
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    Toolbar mToolbar = (Toolbar) findViewById(R.id.activity_toolbar);
    setSupportActionBar(mToolbar);
    mHearthService = new HearthService();
    mMainActivityPresenter = new MainActivityPresenter(this, mHearthService);
    // set a custom shadow that overlays the main content when the drawer opens
    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
    mDrawerList.setNavigationItemSelectedListener(this);
    // enable ActionBar app icon to behave as action to toggle nav drawer
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setHomeButtonEnabled(true);
    }

    // ActionBarDrawerToggle ties together the the proper interactions
    // between the sliding drawer and the action bar app icon
    mDrawerToggle = new ActionBarDrawerToggle(
        this,                  /* host Activity */
        mDrawerLayout,          /* DrawerLayout object */
        mToolbar,  /* nav drawer image to replace 'Up' caret */
        R.string.drawer_open,  /* "open drawer" description for accessibility */
        R.string.drawer_close  /* "close drawer" description for accessibility */
    ) {
      public void onDrawerClosed(View view) {
        getSupportActionBar().setTitle(mTitle);
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }

      public void onDrawerOpened(View drawerView) {
        getSupportActionBar().setTitle(mDrawerTitle);
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }
    };
    mDrawerLayout.setDrawerListener(mDrawerToggle);

    spinners.add(mClassSpinner);
    spinners.add(mCostSpinner);
    spinners.add(mTypeSpinner);
    spinners.add(mRaritySpinner);
    spinners.add(mSetSpinner);

    for (int k = 0; k < 5; k++) {
      setSpinnerAdapter(spinners.get(k), idArray[k]);
    }

    mCollectibleOption = "1";
    mManifestHearthApiKey = "hearthstone_api_key";

    mSpinnerView.setVisibility(View.GONE);
    selectItem(R.id.nav_catalog);
  }

  private void setSpinnerAdapter(Spinner currentSpinner, int arrayId) {
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
        this, arrayId, R.layout.spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    currentSpinner.setAdapter(adapter);
  }

  private void selectItem(int position) {
    // update the main content by replacing fragments
    switch (position) {
      case R.id.nav_catalog:
        mFragment = CardViewFragment.newInstance();
        break;
      case R.id.nav_new_deck:
        createNewDeckDialog();
        return;
      default:
        mFragment = CardViewFragment.newInstance();
        break;
    }
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction ft = fragmentManager.beginTransaction();
    ft.replace(R.id.content_frame, mFragment);
    ft.commit();

    // Close the drawer
    MenuItem currentItem = mDrawerList.getMenu().findItem(position);
    currentItem.setChecked(true);
    setTitle(currentItem.getTitle());
    mDrawerLayout.closeDrawer(mDrawerList);
  }

  public void createNewDeckDialog() {
    NewDeckDialog dialog = new NewDeckDialog();
    dialog.show(getSupportFragmentManager(), "newdeck");
  }

  @Override
  public void setTitle(CharSequence title) {
    mTitle = title;
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(mTitle);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.navigation_drawer, menu);
    return super.onCreateOptionsMenu(menu);
  }

  /**
   * When using the ActionBarDrawerToggle, you must call it during onPostCreate() and
   * onConfigurationChanged()...
   */
  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    // Sync the toggle state after onRestoreInstanceState has occurred.
    mDrawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    // Pass any configuration change to the drawer toggls
    mDrawerToggle.onConfigurationChanged(newConfig);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case R.id.action_search:
        if (mSpinnerView.getVisibility() == View.VISIBLE) {
          mSpinnerView.setVisibility(View.GONE);
        } else {
          mSpinnerView.setVisibility(View.VISIBLE);
        }
        return true;
      default:
        mSpinnerView.setVisibility(View.VISIBLE);
        return true;
    }
  }

  @OnItemSelected({R.id.spinner_class, R.id.spinner_cost, R.id.spinner_type, R.id.spinner_rarity,
                    R.id.spinner_set})
  public void onSpinnerItemSelected() {
    mMainActivityPresenter.getCardFilter(
        mClassSpinner.getSelectedItem().toString(),
        mCostSpinner.getSelectedItem().toString(),
        mTypeSpinner.getSelectedItem().toString(),
        mRaritySpinner.getSelectedItem().toString(),
        mSetSpinner.getSelectedItem().toString()
    );
  }

  public void setSubscriberResult(List<Card> list) {
    if (mFragment.getClass() == CardViewFragment.class && mFragment != null) {
      CardViewFragment cardViewFragment = (CardViewFragment) mFragment;
      cardViewFragment.setCardList(list);
    }
  }

  public String getCollectibleOption() {
    return this.mCollectibleOption;
  }

  public String getManifestHearthApiKey() {
    return mManifestHearthApiKey;
  }

  @Override
  public void getAllCards() {
    mMainActivityPresenter.loadCards();
  }

  public void setNotifyListEmpty() {
    if (mFragment != null && mFragment.getClass() == CardViewFragment.class) {
      CardViewFragment cardViewFragment = (CardViewFragment) mFragment;
      cardViewFragment.setListEmpty();
    }
  }

  @Override
  public boolean onNavigationItemSelected(MenuItem menuItem) {
    selectItem(menuItem.getItemId());
    return true;
  }

  @Override
  public void onFragmentInteraction(Uri uri) {

  }

  private void createNewDeck(int className) {
    String[] classes = getResources().getStringArray(R.array.card_class_dialog);
    count++;
    mDrawerList.getMenu().add(
        R.id.group_user_cards, 1000*count, Menu.NONE, classes[className]+count);
  }

  @Override
  public void onDialogComplete(int type) {
    createNewDeck(type);
    mFragment = DeckFragment.newInstance(type);
    getFragmentManager().beginTransaction()
        .replace(R.id.content_frame, mFragment)
        .commit();
    mDrawerLayout.closeDrawer(mDrawerList);
    if (mSpinnerView.getVisibility() == View.VISIBLE) {
      mSpinnerView.setVisibility(View.GONE);
    }
  }
}