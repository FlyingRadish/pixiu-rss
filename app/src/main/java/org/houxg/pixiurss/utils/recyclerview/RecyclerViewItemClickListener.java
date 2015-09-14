package org.houxg.pixiurss.utils.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Recycler Item-click listener
 */
public class RecyclerViewItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    GestureDetector mGestureDetector;

    public RecyclerViewItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
//            if(childView instanceof ViewGroup){
//                ViewGroup vg = (ViewGroup) childView;
//                vg.onTouchEvent()
//            }
            if(!childView.onTouchEvent(e)){
                mListener.onItemClick(childView, view.getChildPosition(childView));
            }
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }


    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}