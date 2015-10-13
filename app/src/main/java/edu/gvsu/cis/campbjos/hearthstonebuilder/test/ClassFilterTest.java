package edu.gvsu.cis.campbjos.hearthstonebuilder.test;

import android.test.InstrumentationTestCase;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import edu.gvsu.cis.campbjos.hearthstonebuilder.CardFilter;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;
import edu.gvsu.cis.campbjos.hearthstonebuilder.JsonUtil;
import edu.gvsu.cis.campbjos.hearthstonebuilder.NetworkUtil;

/**
 * Created by chadt on 10/12/2015.
 */
public class ClassFilterTest extends InstrumentationTestCase {


    public void testClassFilter() {
        ArrayList<Card> fakeCards = new ArrayList<Card>();
        String response = null;
        try {
            response = NetworkUtil.get("https://omgvamp-hearthstone-v1.p.mashape.com/cards?collectible=1", "X-Mashape-Key", "bexJwbUuYPmshc9PHODsSl9ToBo6p1qjKxojsn2mTw1utrOlqo");
            JsonUtil.parse(response, fakeCards);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<Card> actual = CardFilter.filterCards(fakeCards, "Hunter", "CLEAR", "CLEAR", "CLEAR", "CLEAR");
        for (int i = 0; i < actual.size(); i++){
            assertEquals(actual.get(i).getPlayerClass(), "Hunter");
        }
    }


}