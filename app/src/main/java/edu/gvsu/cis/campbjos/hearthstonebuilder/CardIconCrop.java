package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

/**
 * Created by chadt on 10/9/2015.
 */
public class CardIconCrop implements Transformation {

    public CardIconCrop () {
        //empty construction
    }

    @Override public Bitmap transform(Bitmap source) {
        
        //do logic to transform into a rounded bitmap
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
        if (result != source) {
            source.recycle();
        }
        return result;
    }


    @Override public String key() { return "icon()"; }

}
