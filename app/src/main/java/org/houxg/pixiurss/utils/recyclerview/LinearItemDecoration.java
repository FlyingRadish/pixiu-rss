package org.houxg.pixiurss.utils.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * linear间距器
 */
public class LinearItemDecoration extends RecyclerView.ItemDecoration {

    int dividerSize = 0;
    Rect paddingRect;

    boolean hasHeader = false;

    public LinearItemDecoration() {
    }

    public LinearItemDecoration(int left, int top, int right, int bottom) {
        paddingRect = new Rect(left, top, right, bottom);
    }

    public void setDividerSize(int size) {
        dividerSize = size;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int adapterPos = parent.getChildAdapterPosition(view);
        int itemCnt = parent.getAdapter().getItemCount();
        if (hasHeader) {
            adapterPos--;
            itemCnt--;
        }
        if (adapterPos == -1) {
            return;
        }

        outRect.left = paddingRect.left;
        outRect.bottom = 0;
        outRect.right = paddingRect.right;
        outRect.top = dividerSize;

        if (adapterPos == 0) {
            outRect.top = paddingRect.top;
        }

        if (adapterPos == (itemCnt - 1)) {
            outRect.bottom = paddingRect.bottom;
        }
    }
}
