package edu.gvsu.cis.campbjos.hearthstonebuilder.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by chadt on 10/9/2015.
 */
public class BelweBoldTextView extends TextView{
    public BelweBoldTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public BelweBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public BelweBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("Belwe-bd-bt-bold.ttf", context);
        setTypeface(customFont);
    }
}