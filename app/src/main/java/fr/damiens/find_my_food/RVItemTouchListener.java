package fr.damiens.find_my_food;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RVItemTouchListener implements RecyclerView.OnItemTouchListener { // intercept touch events. call onItemTouch method when a touch happens
    private ItemTouchListener itemTouchListener;

    public interface ItemTouchListener {
        public void onItemTouch(View view, int position);
    }

    GestureDetector mGestureDetector;

    public RVItemTouchListener(Context context, ItemTouchListener listener, boolean onSingleTap, boolean onDoubleTap) {
        itemTouchListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) { // this SimpleOnGestureListener returns true
                return onSingleTap;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) { // this SimpleOnGestureListener returns true
                return onSingleTap;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) { // this SimpleOnGestureListener returns true
                return onDoubleTap;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && itemTouchListener != null && mGestureDetector.onTouchEvent(e)) { // GestureDetector checksGestureListener callback results
            itemTouchListener.onItemTouch(childView, view.getChildAdapterPosition(childView));
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
}