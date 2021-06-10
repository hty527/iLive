package com.amazon.kindle.model;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * TinyHung@Outlook.com
 * 2018/10/21
 */

public class IndexLinLayoutManager extends LinearLayoutManager {

    private RecyclerView mRecyclerView;

    public IndexLinLayoutManager(Context context) {
        this(context, LinearLayoutManager.VERTICAL, false);
    }

    public IndexLinLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            return super.scrollHorizontallyBy(dx, recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        RecyclerView.SmoothScroller smoothScroller = null;
        if(null!=mRecyclerView){
            smoothScroller = new CenterSmoothScroller(mRecyclerView.getContext());
        }else if(null!=recyclerView){
            smoothScroller = new CenterSmoothScroller(recyclerView.getContext());
        }
        if(null!=smoothScroller){
            smoothScroller.setTargetPosition(position);
            startSmoothScroll(smoothScroller);
        }
    }

    public void setMsgRecyclerView(RecyclerView recyclerView) {
        this.mRecyclerView=recyclerView;
    }

    private class CenterSmoothScroller extends LinearSmoothScroller {

        CenterSmoothScroller(Context context) {
            super(context);
        }

        @Nullable
        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            return IndexLinLayoutManager.this.computeScrollVectorForPosition(targetPosition);
        }

        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
            return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2);
        }

        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            return 0.2f;
        }

        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_START;
        }
    }
}
