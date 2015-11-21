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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemSelected;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;
import edu.gvsu.cis.campbjos.hearthstonebuilder.presenters.MainActivityPresenter;
import edu.gvsu.cis.campbjos.hearthstonebuilder.services.HearthService;


public class MainActivity extends AppCompatActivity implements
    CardViewFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener,
    DeckFragment.DeckFragmentListener, NewDeckDialog.DialogListener {

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
  @InjectView(R.id.content_frame)
  View mContentFrame;

  private DrawerLayout mDrawerLayout;
  private ActionBarDrawerToggle mDrawerToggle;
  private CharSequence mDrawerTitle;
  private CharSequence mTitle;
  private String mCollectibleOption;
  private String mManifestHearthApiKey;
  private MainActivityPresenter mMainActivityPresenter;
  private HearthService mHearthService;
  private Fragment mFragment;

  private ImageView setExpand;
  private ImageView rarityExpand;
  private ImageView typeExpand;
  private ImageView costExpand;
  private ImageView classExpand;
  private ImageView clearAll;
  private SearchView searchView;

  private MenuItem mPreviousMenuItem;

  private static ArrayList<Spinner> spinners;
  private static int[] idArray;
  private String[] classes;

  static {
    spinners = new ArrayList<>();
    idArray = new int[]{R.array.card_class,
        R.array.cost, R.array.card_type, R.array.rarity, R.array.card_set};
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    mTitle = mDrawerTitle = getTitle();
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    Toolbar mToolbar = (Toolbar) findViewById(R.id.activity_toolbar);
    setSupportActionBar(mToolbar);
    mHearthService = new HearthService();
    mMainActivityPresenter = new MainActivityPresenter(this, mHearthService);
    // set a custom shadow that overlays the main content when the drawer opens
    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
    mDrawerList.setNavigationItemSelectedListener(this);
    mMainActivityPresenter.loadDecks(this.fileList());

    setExpand = (ImageView) findViewById(R.id.expand_set);
    setExpand.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mSetSpinner.performClick();
      }
    });

    rarityExpand = (ImageView) findViewById(R.id.expand_rarity);
    rarityExpand.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mRaritySpinner.performClick();
      }
    });

    typeExpand = (ImageView) findViewById(R.id.expand_type);
    typeExpand.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mTypeSpinner.performClick();
      }
    });

    costExpand = (ImageView) findViewById(R.id.expand_cost);
    costExpand.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mCostSpinner.performClick();
      }
    });

    classExpand = (ImageView) findViewById(R.id.expand_class);
    classExpand.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mClassSpinner.performClick();
      }
    });

    searchView = (SearchView) findViewById(R.id.searchView);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        mMainActivityPresenter.getCardFilter(
            mClassSpinner.getSelectedItem().toString(),
            mCostSpinner.getSelectedItem().toString(),
            mTypeSpinner.getSelectedItem().toString(),
            mRaritySpinner.getSelectedItem().toString(),
            mSetSpinner.getSelectedItem().toString(), query);
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        mMainActivityPresenter.getCardFilter(
            mClassSpinner.getSelectedItem().toString(),
            mCostSpinner.getSelectedItem().toString(),
            mTypeSpinner.getSelectedItem().toString(),
            mRaritySpinner.getSelectedItem().toString(),
            mSetSpinner.getSelectedItem().toString(), newText);
        return false;
      }
    });

    clearAll = (ImageView) findViewById(R.id.clear_all);
    clearAll.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mClassSpinner.setVisibility(View.INVISIBLE);
        mClassSpinner.setSelection(0);
        mRaritySpinner.setVisibility(View.INVISIBLE);
        mRaritySpinner.setSelection(0);
        mCostSpinner.setVisibility(View.INVISIBLE);
        mCostSpinner.setSelection(0);
        mTypeSpinner.setVisibility(View.INVISIBLE);
        mTypeSpinner.setSelection(0);
        mSetSpinner.setVisibility(View.INVISIBLE);
        mSetSpinner.setSelection(0);
        searchView.setQuery("", true);
        searchView.onActionViewCollapsed();
      }
    });

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
    classes = getResources().getStringArray(R.array.card_class_dialog);

    mSpinnerView.setVisibility(View.GONE);
    onNavigationItemSelected(mDrawerList.getMenu().findItem(R.id.nav_catalog));
  }

  private void setSpinnerAdapter(Spinner currentSpinner, int arrayId) {
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
        this, arrayId, R.layout.spinner_item);
    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
    currentSpinner.setAdapter(adapter);
  }

  public void selectItem(MenuItem currentItem) {
    // update the main content by replacing fragments
    switch (currentItem.getItemId()) {
      case R.id.nav_catalog:
        mMainActivityPresenter.resetFilter();
        mFragment = CardViewFragment.newInstance();
        if (mSpinnerView.getVisibility() == View.VISIBLE) {
          mSpinnerView.setVisibility(View.GONE);
        }
        break;
      case R.id.nav_new_deck:
        createNewDeckDialog();
        return;
      default:
        if (currentItem.getGroupId() == R.id.navigation_deck_group) {
          mFragment =
              DeckFragment.newInstance(
                  // TODO add class type as first parameter
                  -1, currentItem.getItemId(), (String) currentItem.getTitle());

          if (mSpinnerView.getVisibility() == View.VISIBLE) {
            mSpinnerView.setVisibility(View.GONE);
            getSupportActionBar().setSubtitle(null);
          }
        }
        break;
    }

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction ft = fragmentManager.beginTransaction();
    ft.replace(R.id.content_frame, mFragment);
    ft.commit();
    // Close the drawer
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

    final MenuItem item = menu.findItem(R.id.action_search);
    //item.setActionView(R.layout.iv_rotate);
    //ImageView refresh = (ImageView) item.getActionView();
    final ImageView image = new ImageView(this);
    ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
        ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
    image.setLayoutParams(layoutParams);
    image.setImageResource(R.drawable.ic_filter_list_24dp);
    image.setPadding(50, 0, 50, 0);

    image.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                 if (mSpinnerView.getVisibility() == View.VISIBLE)
                                   image.setImageResource(R.drawable.rev_ic_filter_list_24dp);
                                 else
                                   image.setImageResource(R.drawable.ic_filter_list_24dp);

                                 onOptionsItemSelected(item);
                               }
                             }
    );
    item.setActionView(image);

    MenuItem rename = menu.findItem(R.id.rename_deck);
    MenuItem clear = menu.findItem(R.id.clear_all);
    MenuItem delete = menu.findItem(R.id.delete_deck);
    if (mFragment.getClass() == CardViewFragment.class) {
      rename.setVisible(false);
      clear.setVisible(false);
      delete.setVisible(false);
    } else {
      rename.setVisible(true);
      clear.setVisible(true);
      delete.setVisible(true);
    }

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
        rotate(item);
        if (mSpinnerView.getVisibility() == View.VISIBLE) {
          //going up
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSpinnerView.setElevation(0);
            getSupportActionBar().setElevation(1);
          }
          setBarUpAnimation(mSpinnerView);
          setListUpAnimation(mContentFrame);
          //mSpinnerView.setVisibility(View.INVISIBLE);
          mSpinnerView.setVisibility(View.GONE);
          getSupportActionBar().setSubtitle(null);
        } else {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSpinnerView.setElevation(0);
            getSupportActionBar().setElevation(1);
          }
          mSpinnerView.setVisibility(View.VISIBLE);
          setListDownAnimation(mContentFrame);
          setBarDownAnimation(mSpinnerView);
          getSupportActionBar().setSubtitle("Filter Cards");
        }
        return true;
      default:
        return true;
    }
  }

  private void rotate(MenuItem item) {
    Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
    rotation.setFillAfter(true);
    item.getActionView().startAnimation(rotation);
  }

  private void setBarUpAnimation(View viewToAnimate) {
    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_up);
    animation.setAnimationListener(new SlideUpAnimationListener(getSupportActionBar()));
    viewToAnimate.startAnimation(animation);
  }

  private void setBarDownAnimation(View viewToAnimate) {
    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_down);
    viewToAnimate.startAnimation(animation);
  }

  private void setListUpAnimation(View viewToAnimate) {
    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_up_list);
    viewToAnimate.startAnimation(animation);
  }

  private void setListDownAnimation(View viewToAnimate) {
    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_down_list);

    RelativeLayout.LayoutParams rlParams = (RelativeLayout.LayoutParams) viewToAnimate.getLayoutParams();
    //set the margin to negative so the animation does not truncate the bottom
    rlParams.setMargins(0, 0, 0, viewToAnimate.getHeight() * -1);
    //Custom SlideAnimationListener to set the margin back once the animation is complete
    animation.setAnimationListener(new SlideAnimationListener(viewToAnimate, getSupportActionBar(), mSpinnerView));
    viewToAnimate.startAnimation(animation);
  }

  @OnItemSelected({R.id.spinner_class, R.id.spinner_cost, R.id.spinner_type, R.id.spinner_rarity,
      R.id.spinner_set})
  public void onSpinnerItemSelected() {
    mMainActivityPresenter.getCardFilter(

        mClassSpinner.getSelectedItem().toString(),
        mCostSpinner.getSelectedItem().toString(),
        mTypeSpinner.getSelectedItem().toString(),
        mRaritySpinner.getSelectedItem().toString(),
        mSetSpinner.getSelectedItem().toString(),
        searchView.getQuery().toString()
    );
    if (mSetSpinner.getSelectedItem().toString().equals("CLEAR")) {
      mSetSpinner.setVisibility(View.INVISIBLE);
      setExpand.setVisibility(View.VISIBLE);
    } else {
      mSetSpinner.setVisibility(View.VISIBLE);
      setExpand.setVisibility(View.INVISIBLE);
    }

    if (mRaritySpinner.getSelectedItem().toString().equals("CLEAR")) {
      mRaritySpinner.setVisibility(View.INVISIBLE);
      rarityExpand.setVisibility(View.VISIBLE);
    } else {
      mRaritySpinner.setVisibility(View.VISIBLE);
      rarityExpand.setVisibility(View.INVISIBLE);
    }

    if (mTypeSpinner.getSelectedItem().toString().equals("CLEAR")) {
      mTypeSpinner.setVisibility(View.INVISIBLE);
      typeExpand.setVisibility(View.VISIBLE);
    } else {
      mTypeSpinner.setVisibility(View.VISIBLE);
      typeExpand.setVisibility(View.INVISIBLE);
    }

    if (mCostSpinner.getSelectedItem().toString().equals("CLEAR")) {
      mCostSpinner.setVisibility(View.INVISIBLE);
      costExpand.setVisibility(View.VISIBLE);
    } else {
      mCostSpinner.setVisibility(View.VISIBLE);
      costExpand.setVisibility(View.INVISIBLE);
    }

    if (mClassSpinner.getSelectedItem().toString().equals("CLEAR")) {
      mClassSpinner.setVisibility(View.INVISIBLE);
      classExpand.setVisibility(View.VISIBLE);
    } else {
      mClassSpinner.setVisibility(View.VISIBLE);
      classExpand.setVisibility(View.INVISIBLE);
    }
  }

  public void setSubscriberResult(List<Card> list) {
    if (mFragment.getClass() == CardViewFragment.class) {
      CardViewFragment cardViewFragment = (CardViewFragment) mFragment;
      cardViewFragment.setCardList(list);
    } else {
      DeckFragment deckFragment = (DeckFragment) mFragment;
      deckFragment.setCardList(list);
    }
  }

  public String getCollectibleOption() {
    return this.mCollectibleOption;
  }

  public String getManifestHearthApiKey() {
    return mManifestHearthApiKey;
  }

  @Override
  public void updateSubtitle(String amount) {
    getSupportActionBar().setSubtitle(amount);
  }

  @Override
  public void getAllCards() {
    mMainActivityPresenter.loadCards();
  }

  public void setNotifyListEmpty() {
    if (mFragment.getClass() == CardViewFragment.class) {
      CardViewFragment cardViewFragment = (CardViewFragment) mFragment;
      cardViewFragment.setListEmpty();
    } else {
      DeckFragment deckFragment = (DeckFragment) mFragment;
      deckFragment.setListEmpty();
    }
  }

  @Override
  public boolean onNavigationItemSelected(MenuItem menuItem) {
    menuItem.setChecked(true);
    if (mPreviousMenuItem != null) {
      mPreviousMenuItem.setChecked(false);
    }
    mPreviousMenuItem = menuItem;
    selectItem(menuItem);
    invalidateOptionsMenu();
    return true;
  }

  public void setNavigationMenuItem(int menuItemId, String menuItemName) {
    mDrawerList.getMenu().findItem(R.id.navigation_deck_item).getSubMenu().add(
        R.id.navigation_deck_group, menuItemId, Menu.NONE, menuItemName);
  }

  public Fragment getActivityFragment() {
    return mFragment;
  }

  @Override
  public void onDialogComplete(int type, int deckId, String deckName) {
    if (deckName.trim().isEmpty()) {
      SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
      SharedPreferences.Editor editor = preferences.edit();
      String className = classes[type];

      deckName = "";
      int count = preferences.getInt(className, 0);
      count++;
      deckName = String.format("%s %d", className, count);
      editor.putInt(className, count);
      editor.apply();
    }
    setNavigationMenuItem(deckId, deckName);
    onNavigationItemSelected(mDrawerList.getMenu().findItem(deckId));
  }

  public MainActivityPresenter getPresenter() {
    return this.mMainActivityPresenter;
  }

  public NavigationView getNavigationView() {
    return mDrawerList;
  }
}