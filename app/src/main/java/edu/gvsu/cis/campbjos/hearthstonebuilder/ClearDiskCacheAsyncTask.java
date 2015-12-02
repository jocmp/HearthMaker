package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.content.Context;
import android.os.AsyncTask;

import com.bumptech.glide.Glide;

/**
 * @author Chad
 * @version November 2015
 */
public class ClearDiskCacheAsyncTask extends AsyncTask<Context, Void, Void> {

  @Override
  protected Void doInBackground(Context... params) {
    Context context = params[0];
    Glide.get(context).clearDiskCache();
    return null;
  }
}