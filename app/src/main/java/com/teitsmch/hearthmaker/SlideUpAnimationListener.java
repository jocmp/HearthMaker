package com.teitsmch.hearthmaker;

import android.support.v7.app.ActionBar;
import android.view.animation.Animation;

/**
 * Created by chadt on 11/3/2015.
 */
public class SlideUpAnimationListener implements Animation.AnimationListener {

    ActionBar bar;

    public SlideUpAnimationListener(ActionBar bar){
        this.bar = bar;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        bar.setElevation(10);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
