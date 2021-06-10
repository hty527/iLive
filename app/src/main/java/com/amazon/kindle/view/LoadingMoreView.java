package com.amazon.kindle.view;

import com.amazon.kindle.R;
import com.chad.library.adapter.base.loadmore.LoadMoreView;

/**
 * hty_Yuye@Outlook.com
 * 2019/7/19
 */

public class LoadingMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.view_load_more_layout;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_fail;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_end;
    }
}
