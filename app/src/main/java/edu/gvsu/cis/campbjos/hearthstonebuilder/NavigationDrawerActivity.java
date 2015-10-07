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
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * This example illustrates a common usage of the DrawerLayout widget in the Android support
 * library.
 */
public class NavigationDrawerActivity extends AppCompatActivity implements DrawerAdapter.OnItemClickListener {
  private DrawerLayout mDrawerLayout;
  private RecyclerView mDrawerList;
  private ActionBarDrawerToggle mDrawerToggle;

  private CharSequence mDrawerTitle;
  private CharSequence mTitle;

  //just to make it build. CT 9/25/2015
  String[]mPlanetTitles = {"HearthMaker"};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_navigation_drawer);

    mTitle = mDrawerTitle = getTitle();
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (RecyclerView) findViewById(R.id.left_drawer);
    Toolbar mToolbar = (Toolbar) findViewById(R.id.activity_toolbar);
    setSupportActionBar(mToolbar);
    // set a custom shadow that overlays the main content when the drawer opens
    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
    // improve performance by indicating the list if fixed size.
    mDrawerList.setHasFixedSize(true);

    // set up the drawer's list view with items and click listener
    mDrawerList.setAdapter(new DrawerAdapter(mPlanetTitles, this));
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

    if (savedInstanceState == null) {
      selectItem(0);
    }
  }

  /* The click listener for RecyclerView in the navigation drawer */
  @Override
  public void onClick(View view, int position) {
    selectItem(position);
  }

  private void selectItem(int position) {
    // update the main content by replacing fragments
    Fragment fragment = CardViewFragment.newInstance();

    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction ft = fragmentManager.beginTransaction();
    ft.replace(R.id.content_frame, fragment);
    ft.commit();

    // update selected item title, then close the drawer
    setTitle(mPlanetTitles[position]);
    mDrawerLayout.closeDrawer(mDrawerList);
  }

  @Override
  public void setTitle(CharSequence title) {
    mTitle = title;
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(mTitle);
    }
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

}
