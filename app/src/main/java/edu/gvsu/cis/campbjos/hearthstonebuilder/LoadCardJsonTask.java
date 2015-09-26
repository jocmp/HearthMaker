package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.os.AsyncTask;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


import edu.gvsu.cis.campbjos.hearthstonebuilder.CustomAdapters.CardAdapter;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;

import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;


/**
 * @author HearthBuilder Team
 * @version Fall 2015
 */
public class LoadCardJsonTask extends AsyncTask<Object, Void, Void> {
  CardAdapter adapter;

  @Override
  protected void onPostExecute(Void v) {
    super.onPostExecute(v);
    adapter.notifyDataSetChanged();
  }

  @Override
  protected Void doInBackground(Object... params) {
    HttpResponse<JsonNode> response = null;
    String key = (String) params[0];
    ArrayList<Card> cardList = (ArrayList<Card>) params[2];

    try {
      response = Unirest
          .get("https://omgvamp-hearthstone-v1.p.mashape.com/cards?collectible=1")
          .header("X-Mashape-Key", key)
          .asJson();
      JsonNode body = response.getBody();
      JsonUtil.parseJsonResponse(cardList, body.toString());
    } catch (UnirestException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return null;
  }

  interface CardResponseListenener {
    void onTaskComplete();
  }
}
