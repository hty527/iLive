package com.android.live.player.lib.listener;

import android.view.View;

/**
 * hty_Yuye@Outlook.com
 * 2019/4/21
 */

public abstract class OnVideoTouchListener {

    /**
     * 单机事件
     * @param view
     */
    public void onClick(View view){}

    /**
     * 双单机事件
     * @param view
     */
    public void onDouble(View view){}

    /**
     * 向左滑动
     */
    public void onScrollLeft(){}

    /**
     * 向右滑动
     */
    public void onScrollRight(){}
}
