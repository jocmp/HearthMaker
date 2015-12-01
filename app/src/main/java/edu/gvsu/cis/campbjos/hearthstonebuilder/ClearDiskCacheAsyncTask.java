package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.content.Context;
import android.os.AsyncTask;

import com.bumptech.glide.Glide;

/**
 * Created by chadt on 11/30/2015.
 */
public class ClearDiskCacheAsyncTask extends AsyncTask {

    @Override
    protected Object doInBackground(Object[] params) {
        Context context = (Context) params[0];
        Glide.get(context).clearDiskCache();
        return null;
    }
}
