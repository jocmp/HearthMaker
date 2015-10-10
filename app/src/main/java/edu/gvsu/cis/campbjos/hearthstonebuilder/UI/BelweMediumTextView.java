package edu.gvsu.cis.campbjos.hearthstonebuilder.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import edu.gvsu.cis.campbjos.hearthstonebuilder.UI.FontCache;

/**
 * Created by chadt on 10/9/2015.
 */
public class BelweMediumTextView extends TextView {
    public BelweMediumTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public BelweMediumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public BelweMediumTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("Belwe-Medium.otf", context);
        setTypeface(customFont);
    }
}
