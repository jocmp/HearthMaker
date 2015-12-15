package com.teitsmch.hearthmaker;

import android.app.Application;
import android.content.Context;

/**
 * @author Josiah Campbell
 * @version December 2015
 */
public class HearthApplication extends Application {

  private static Context context;

  public void onCreate() {
    super.onCreate();
    HearthApplication.context = getApplicationContext();
  }

  public static Context getAppContext() {
    return HearthApplication.context;
  }

}
