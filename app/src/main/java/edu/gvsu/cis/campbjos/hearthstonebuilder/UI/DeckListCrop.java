package edu.gvsu.cis.campbjos.hearthstonebuilder.UI;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

/**
 * Created by chadt on 10/9/2015.
 */
public class DeckListCrop implements Transformation {

    private static DeckListCrop cardCrop;

    private DeckListCrop () {

    }

    public static DeckListCrop getDeckListCrop(){
        if (cardCrop == null){
            cardCrop = new DeckListCrop();
        }
        return cardCrop;
    }

    @Override public Bitmap transform(Bitmap source) {

        int Yoffset = (source.getHeight()/source.getWidth())*80;
        int Xoffset = (source.getHeight()/source.getWidth())*82;
        double tempX = Yoffset*1.6;
        double tempY = Yoffset*1.45;
        int sizeX = (int) tempX;
        int sizeY = (int) tempY/3;

        //create square image
        Bitmap scaledSquare = Bitmap.createBitmap(source, Xoffset, Yoffset*2, sizeX, sizeY);

        source.recycle();

        return scaledSquare;
    }


    @Override public String key() { return "list()"; }

}
