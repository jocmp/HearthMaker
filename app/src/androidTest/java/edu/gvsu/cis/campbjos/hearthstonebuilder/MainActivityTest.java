package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

import org.junit.Test;

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

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    mTestMainActivity = getActivity();
  }

  public void testPreconditions() {
    assertNotNull("mTestMainActivity is null", mTestMainActivity);
  }

  @UiThreadTest
  public void testLoadCards() {
    mTestMainActivity.getPresenter().loadCards();
    assertEquals(false, mTestMainActivity.getPresenter().getCardList().isEmpty());
  }
}