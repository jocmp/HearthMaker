package edu.gvsu.cis.campbjos.hearthstonebuilder.UI;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import com.squareup.picasso.Transformation;

/**
 * Created by chadt on 10/9/2015.
 */
public class CardIconCrop implements Transformation {

    public CardIconCrop () {
        //empty construction
    }

    @Override public Bitmap transform(Bitmap source) {

        int Yoffset = (source.getHeight()/source.getWidth())*95;
        int Xoffset = (source.getHeight()/source.getWidth())*82;
        double temp = Yoffset*1.45;
        int size = (int) temp;

        //create square image
        Bitmap scaledSquare = Bitmap.createBitmap(source, Xoffset, Yoffset, size, size);

        //create transparent edges
        Bitmap output = Bitmap.createBitmap(scaledSquare.getWidth(),
                scaledSquare.getHeight(), Bitmap.Config.ARGB_8888);

        //draw to canvas and put circle over top of the edges
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, size, size);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(size/ 2 + 0.7f,
               size / 2 + 0.7f, size / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaledSquare, rect, rect, paint);

        //recycle old bitmaps
        if (output != source){
            scaledSquare.recycle();
            source.recycle();
        }

        return output;
    }


    @Override public String key() { return "icon()"; }

}
