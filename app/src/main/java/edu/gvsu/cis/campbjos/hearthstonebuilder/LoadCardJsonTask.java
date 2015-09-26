package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.os.AsyncTask;

import edu.gvsu.cis.campbjos.hearthstonebuilder.CustomAdapters.CardAdapter;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * AsyncTask responsible for gathering the HS card information from the
 * MashApe API.
 *
 * @author HearthBuilder Team
 * @version Fall 2015
 */
public class LoadCardJsonTask extends AsyncTask<Object, Void, Void> {
  private CardAdapter adapter;

  @Override
  protected void onPostExecute(Void vd) {
    super.onPostExecute(vd);
    adapter.notifyDataSetChanged();
  }

  /**
   * doInBackground calls on the MashApe HS API, which we process using the
   * {@link JsonUtil} class, and notify the calling fragment onPostExecute.
   *
   * @param params Context, String url, String key, ArrayList deck
   * @return null
   */
  @Override
  @SuppressWarnings(value = "unchecked")
  protected Void doInBackground(Object... params) {
    adapter = (CardAdapter) params[0];
    String url = (String) params[1];
    String key = (String) params[2];
    ArrayList<Card> cardList = (ArrayList<Card>) params[3];
    try {
      String response = NetworkUtil.get(url, "X-Mashape-Key", key);
      JsonUtil.parse(response, cardList);
    }  catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
