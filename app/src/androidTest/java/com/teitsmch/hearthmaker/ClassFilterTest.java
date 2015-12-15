package com.teitsmch.hearthmaker;

import com.google.gson.JsonObject;

import android.test.InstrumentationTestCase;

import com.teitsmch.hearthmaker.Entity.Card;
import com.teitsmch.hearthmaker.services.HearthService;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Chad
 * @version Fall 2015
 */
public class ClassFilterTest extends InstrumentationTestCase {

  HearthService mHearthService;
  ArrayList<Card> fakeCards;

  public ClassFilterTest() {
    mHearthService = new HearthService();
    fakeCards = new ArrayList<>();
  }

  public void testClassFilter() {
    loadCards();

    ArrayList<Card> actual = CardFilter
        .filterCards(fakeCards, "Hunter", "CLEAR", "CLEAR", "CLEAR", "CLEAR", "");
    for (int i = 0; i < actual.size(); i++) {
      assertEquals(actual.get(i).getPlayerClass(), "Hunter");
    }
  }

  public void testCostFilter1() {
    ArrayList<Card> fakeCards = new ArrayList<Card>();
    loadCards();

    ArrayList<Card> actual =
        CardFilter.filterCards(fakeCards, "CLEAR", "6", "CLEAR", "CLEAR", "CLEAR", "");
    for (int i = 0; i < actual.size(); i++) {
      assertEquals(actual.get(i).getCost(), 6);
    }
  }

  public void testCostFilter2() {
    ArrayList<Card> fakeCards = new ArrayList<Card>();
    loadCards();

    ArrayList<Card> actual = CardFilter.filterCards(fakeCards, "CLEAR", "7+", "CLEAR", "CLEAR", "CLEAR", "");
    for (int i = 0; i < actual.size(); i++) {
      assertFalse(actual.get(i).getCost() < 7);
    }
  }

  public void testTypeFilter() {
    ArrayList<Card> fakeCards = new ArrayList<Card>();
    loadCards();

    ArrayList<Card> actual = CardFilter.filterCards(fakeCards, "CLEAR", "CLEAR", "Minion", "CLEAR", "CLEAR", "");
    for (int i = 0; i < actual.size(); i++) {
      assertEquals(actual.get(i).getType(), "Minion");
    }
  }

  public void testRarityFilter() {
    ArrayList<Card> fakeCards = new ArrayList<Card>();
    loadCards();

    ArrayList<Card> actual = CardFilter.filterCards(fakeCards, "CLEAR", "CLEAR", "CLEAR", "Rare", "CLEAR", "");
    for (int i = 0; i < actual.size(); i++) {
      assertEquals(actual.get(i).getRarity(), "Rare");
    }
  }

  public void testSetFilter() {
    ArrayList<Card> fakeCards = new ArrayList<Card>();
    loadCards();

    ArrayList<Card> actual = CardFilter.filterCards(fakeCards, "CLEAR", "CLEAR", "CLEAR", "Clear", "Classic", "");
    for (int i = 0; i < actual.size(); i++) {
      assertFalse(!(actual.get(i).getCardSet().equals("Basic") || actual.get(i).getCardSet().equals("Classic")));
    }
  }

  public void loadCards() {

    mHearthService.getApi()
        .getCardResponse("bexJwbUuYPmshc9PHODsSl9ToBo6p1qjKxojsn2mTw1utrOlqo", "1")
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<JsonObject>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onNext(JsonObject jsonObject) {
            JsonUtil.parse(jsonObject, fakeCards);
          }
        });
  }
}