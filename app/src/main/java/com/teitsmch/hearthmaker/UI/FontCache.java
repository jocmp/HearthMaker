package com.teitsmch.hearthmaker.UI;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * @author HearthMaker Team
 * @version Fall 2015
 */
public class FontCache {
  private static Hashtable<String, Typeface> fontCache = new Hashtable<>();

  /**
   *
   * @param fontName Name of incoming font
   * @param context Parent context for view
   * @return Formatted text
   */
  public static Typeface getTypeface(String fontName, Context context) {
    Typeface typeface = fontCache.get(fontName);

    if (typeface == null) {
      try {
        typeface = Typeface.createFromAsset(context.getAssets(), fontName);
      } catch (Exception e) {
        return null;
      }

      fontCache.put(fontName, typeface);
    }

    return typeface;
  }
}
