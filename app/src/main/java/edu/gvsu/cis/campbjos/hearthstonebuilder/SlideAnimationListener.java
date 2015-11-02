package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

/**
 * Created by chadt on 11/1/2015.
 */
public class SlideAnimationListener implements Animation.AnimationListener {

    View viewToAnimate;
    RelativeLayout.LayoutParams rlParams;

    public SlideAnimationListener(View viewToAnimate){
        this.viewToAnimate = viewToAnimate;
        rlParams = (RelativeLayout.LayoutParams) viewToAnimate.getLayoutParams();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        //set margins back so the view can be scrolled to the bottom
        rlParams.setMargins(0, 0, 0, 0);
        viewToAnimate.requestLayout();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
