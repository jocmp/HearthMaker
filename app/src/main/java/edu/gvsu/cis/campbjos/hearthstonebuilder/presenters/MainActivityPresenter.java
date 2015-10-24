package edu.gvsu.cis.campbjos.hearthstonebuilder.presenters;

import android.support.v4.app.Fragment;

import edu.gvsu.cis.campbjos.hearthstonebuilder.CardFilter;
import edu.gvsu.cis.campbjos.hearthstonebuilder.CardViewFragment;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;
import edu.gvsu.cis.campbjos.hearthstonebuilder.MainActivity;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Josiah Campbell
 * @version Fall 2015
 */
public class MainActivityPresenter {
  MainActivity mParentView;
  Fragment mView;

  /**
   * Presenter class for {@link CardViewFragment}.
   * @param activity The fragment's containing class
   */
  public MainActivityPresenter(MainActivity activity) {

    mParentView = activity;
  }

  public ArrayList<Card> getCardFilter(String search) {
    ArrayList<Card> removeThis = new ArrayList<Card>();
    return CardFilter.filterCards(removeThis, search, search, search, search, search, search);
  }
}