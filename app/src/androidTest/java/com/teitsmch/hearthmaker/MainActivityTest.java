package com.teitsmch.hearthmaker;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

import org.junit.Before;

/**
 * @author Josiah
 * @version Fall 2015
 */
public class MainActivityTest
    extends ActivityInstrumentationTestCase2<MainActivity> {

  private MainActivity mTestMainActivity;

  public MainActivityTest() {
    super(MainActivity.class);
  }

  @Override @Before
  protected void setUp() throws Exception {
    super.setUp();
    mTestMainActivity = getActivity();
  }

  @UiThreadTest @Before
  public void testPreconditions() {
    assertNotNull("mTestMainActivity is null", mTestMainActivity);
    assertNotNull("Activity Fragment is null", mTestMainActivity.getActivityFragment());
    assertNotNull("Activity Presenter is null", mTestMainActivity.getPresenter());
  }

  @UiThreadTest
  public void testLoadDecks() {
    mTestMainActivity.getNavigationView().getMenu().clear();
    String[] testFileList = {"-100000", "100000", "-10.001", "rJunk"};
    mTestMainActivity.getPresenter().loadDecks(testFileList);
  }
}