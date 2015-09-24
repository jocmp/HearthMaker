package edu.gvsu.cis.campbjos.hearthstonebuilder;

import com.google.api.client.json.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by josiah on 9/24/15.
 */
public class JsonUtil {
  private static JsonParser parser;

  public static void parseCardJson(ArrayList<?> cardData, InputStream in) throws JSONException {
//    JSONArray tempArr = new JSONArray();
//    for(int idx = 0; idx < responseObject.length(); idx++) {
//      tempArr = responseObject[idx];
//    }
    /*
    Object -> object:array -> object (cards)
     */
  }
}