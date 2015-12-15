package com.teitsmch.hearthmaker;

/**
 * Created by Josiah Campbell on 8/14/15. <p> Adapted with love from
 *
 * @see <a href=http://stackoverflow.com/questions/24471109/recyclerview-onclick/26196831#26196831>
 * StackOverflow</a>
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
  GestureDetector mGestureDetector;
  private OnItemClickListener mListener;

  public RecyclerItemClickListener(
      Context context, final RecyclerView view, OnItemClickListener listener) {
    mListener = listener;
    mGestureDetector = new GestureDetector(
        context, new GestureDetector.SimpleOnGestureListener() {
          @Override
          public boolean onSingleTapUp(MotionEvent e) {
            return true;
          }
          @Override
          public void onLongPress(MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if(childView != null && mListener != null) {
              mListener.onItemLongClick(childView, view.getChildAdapterPosition(childView));
            }
          }
        });
  }

  @Override
  public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
    View childView = view.findChildViewUnder(e.getX(), e.getY());
    if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
      mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
      return true;
    }
    return false;
  }

  @Override
  public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
  }

  @Override
  public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

  }

  public interface OnItemClickListener {
    void onItemClick(View view, int position);
    public void onItemLongClick(View view, int position);
  }
}