package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
  public void testLoadCards() {
    mTestMainActivity.getPresenter().loadCards();
    assertEquals(false, mTestMainActivity.getPresenter().getCardList().isEmpty());
  }

  @UiThreadTest
  public void testSetTitle() throws Exception {
    mTestMainActivity.setTitle("Test");
    assertEquals("Test", mTestMainActivity.getTitle());
  }

  @UiThreadTest
  public void testSetTitleNull() throws Exception {
    mTestMainActivity.setTitle("Test");
    assertEquals("Test", mTestMainActivity.getTitle());
    mTestMainActivity.setSupportActionBar(null);
    mTestMainActivity.setTitle("This shouldn't pass");
    assertEquals("Test", mTestMainActivity.getTitle());
  }

  @UiThreadTest
  public void testSetSubscriberResultCardView() throws Exception {
    List<Card> testList = new ArrayList<>(4);
    CardViewFragment cardViewFragment =
        (CardViewFragment) mTestMainActivity.getActivityFragment();
    mTestMainActivity.selectItem(R.id.nav_catalog);
    for (int i = 0; i < 4; i++) {
      testList.add(new Card());
    }
    assertNotNull(cardViewFragment);
    mTestMainActivity.setSubscriberResult(testList);
    assertEquals(4, cardViewFragment.getCards().size());
  }
}
