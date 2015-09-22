package org.houxg.pixiurss.utils.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Recycler Item-click listener
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;

    int position;
    View childView;
    RecyclerView recyclerView;

    public interface OnItemClickListener {
        void onItemClick(RecyclerView recyclerView, View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(RecyclerView recyclerView, View view, int position);
    }

    GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context) {
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(recyclerView, childView, position);
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if (itemLongClickListener != null) {
                    itemLongClickListener.onItemLongClick(recyclerView, childView, position);
                }
            }
        });
    }

    public RecyclerItemClickListener setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }

    public RecyclerItemClickListener setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
        return this;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null) {
            position = view.getChildAdapterPosition(childView);
            recyclerView = view;
            return mGestureDetector.onTouchEvent(e);
        } else {
            position = 0;
            recyclerView = null;
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