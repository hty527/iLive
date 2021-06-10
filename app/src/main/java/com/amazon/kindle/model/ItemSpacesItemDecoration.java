package com.amazon.kindle.model;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * TinyHung@outlook.com
 * 2017/5/24 8:58
 * GridLayout 两列列表间距
 * 只有左侧有边距，第一个除外
 */
public class ItemSpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public ItemSpacesItemDecoration(int space) {
        this.space=space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(0==parent.getChildLayoutPosition(view)){
            outRect.left=0;
        }else{
            outRect.left=space;
        }
        outRect.bottom=0;
        outRect.right=0;
        outRect.top=0;
    }
}
