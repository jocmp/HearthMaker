package edu.gvsu.cis.campbjos.hearthstonebuilder;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtil {

  /**
   * Public method that takes a valid url as input and returns a string response, e.g. JSON or XML
   * feed
   */
  public static String get(String myUrl, String accept, String key)
      throws IOException {

    InputStream is = null;

    try {
      URL url = new URL(myUrl);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setReadTimeout(10000 /* milliseconds */);
      conn.setConnectTimeout(15000 /* milliseconds */);
      conn.setRequestMethod("GET");
      conn.setRequestProperty(accept, key);
      conn.setDoInput(true);
      // Start the query
      conn.connect();
      is = conn.getInputStream();
      // Convert the InputStream into a string
      return getString(is);
            /* Makes sure that the InputStream is closed after the app is
             * finished using it. */
    } finally {
      if (is != null) {
        is.close();
      }
    }
  }

  private static String getString(InputStream stream) throws IOException {

    BufferedReader reader =
        new BufferedReader(new InputStreamReader(stream));
    StringBuilder sb = new StringBuilder();

    String line = null;
    try {
      while ((line = reader.readLine()) != null) {
        sb.append(line).append("\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return sb.toString();
  }

  /**
   * Public method returns whether or not Android device is online
   */
  public static boolean isOnline(Context context) {
    ConnectivityManager connMgr = (ConnectivityManager)
        context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    return (networkInfo != null && networkInfo.isConnected());
  }
}