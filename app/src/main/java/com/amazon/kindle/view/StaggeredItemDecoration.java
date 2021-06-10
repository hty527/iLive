package com.amazon.kindle.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * TinyHung@outlook.com
 * 2017/4/13 13:56
 * 平均的间距
 */
public class StaggeredItemDecoration extends RecyclerView.ItemDecoration{

    private final int space;

    public StaggeredItemDecoration(int space) {
        this.space=space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.bottom = space;
        outRect.right = space;
        outRect.top= space;
    }
}
