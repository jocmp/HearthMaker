package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

/**
 * Created by chadt on 11/1/2015.
 */
public class SlideAnimationListener implements Animation.AnimationListener {

    View viewToAnimate;
    ActionBar bar;
    View spinners;
    RelativeLayout.LayoutParams rlParams;

    public SlideAnimationListener(View viewToAnimate, ActionBar bar, View spinners){
        this.viewToAnimate = viewToAnimate;
        rlParams = (RelativeLayout.LayoutParams) viewToAnimate.getLayoutParams();
        this.bar = bar;
        this.spinners = spinners;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        //set margins back so the view can be scrolled to the bottom
        rlParams.setMargins(0, 0, 0, 0);
        bar.setElevation(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            spinners.setElevation(10);
        }
        viewToAnimate.requestLayout();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
