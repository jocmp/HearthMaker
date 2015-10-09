package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by HearthMaker Team on 10/9/2015.
 */
public class FontCache {
    private static Hashtable<String, Typeface> fontCache = new Hashtable<>();

    public static Typeface getTypeface(String fontname, Context context) {
        Typeface typeface = fontCache.get(fontname);

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontname);
            } catch (Exception e) {
                return null;
            }

            fontCache.put(fontname, typeface);
        }

        return typeface;
    }
}
