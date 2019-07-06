package com.android.gift.gift.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import com.android.gift.listener.OnAnimationListener;
import com.android.gift.util.AnimationUtil;

/**
 * TinyHung@Outlook.com
 * 2018/6/15
 * 小倍率的中奖动画
 */

public class GiftDrawSmallMulitAnimationView extends android.support.v7.widget.AppCompatTextView {

    private static final long CLEAN_MILLIS = 2500;//定时清除自己
    private static final String TAG = "DrawSmallMulitAnimationView";
    private int strokeColor=Color.BLACK;//描边颜色


    public GiftDrawSmallMulitAnimationView(Context context) {
        super(context);
    }

    public GiftDrawSmallMulitAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setStrokeColor(int strokeColor){
        this.strokeColor=strokeColor;
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    /**
     * 开始
     * @param content
     */
    public void startText(String content) {
        this.clearAnimation();
        if(null!=cleanRunnable) this.removeCallbacks(cleanRunnable);
        this.setText(content);
        AnimationUtil.startSmallAwardAnim(this,1000,null);
        this.postDelayed(cleanRunnable,CLEAN_MILLIS);
    }

    /**
     * 结束
     */
    private void stop() {
        AnimationUtil.startSmallAwardAnimOut(this, 300, new OnAnimationListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onEnd() {
                GiftDrawSmallMulitAnimationView.this.setBackgroundResource(0);
                GiftDrawSmallMulitAnimationView.this.setText("");
            }
        });
    }

    private  Runnable cleanRunnable=new Runnable() {
        @Override
        public void run() {
            stop();
        }
    };

    public void onDestroy(){
        if(null!=cleanRunnable) this.removeCallbacks(cleanRunnable);
        GiftDrawSmallMulitAnimationView.this.setBackgroundResource(0);
    }
}


