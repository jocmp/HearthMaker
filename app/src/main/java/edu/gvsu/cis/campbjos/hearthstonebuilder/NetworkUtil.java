package edu.gvsu.cis.campbjos.hearthstonebuilder;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import edu.gvsu.cis.campbjos.hearthstonebuilder.services.HearthService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtil {
  /**
   * Public method returns whether or not Android device is online.
   */
  public static boolean isOnline(Context context) {
    ConnectivityManager connMgr = (ConnectivityManager)
        context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    return (networkInfo != null && networkInfo.isConnected());
  }
}