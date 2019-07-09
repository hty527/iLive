package com.android.gift.gift.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.gift.R;
import com.android.gift.gift.manager.GiftBoardManager;
import com.android.gift.util.SpannableStringUtils;
import com.android.gift.room.view.GoldWireLayout;
import com.android.gift.util.AnimationUtil;
import com.android.gift.util.AppUtils;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TinyHung@Outlook.com
 * 2018/7/18
 * 大倍率的中奖动画
 */

public class GiftDrawBigMulitAnimationView extends RelativeLayout{

    private static final long CLEAN_MILLIS = 2160;//定时清除自己
    private ImageView mSurfaceView;
    private TextView mSurfaceText;
    private boolean isRunning=false;
    private AnimationDrawable mAnimationDrawable;
    private boolean isRuning;//金币掉落动画是否正在进行
    private int[] mStartPosition =null;
    private TimerTask mTask;
    private Timer timer;
    public static int COUNT=60;
    private int count;

    public GiftDrawBigMulitAnimationView(Context context) {
        super(context);
        init(context);
    }

    public GiftDrawBigMulitAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.view_gift_big_draw_layout,this);
        mSurfaceText = (TextView) findViewById(R.id.view_draw_num);
        mSurfaceView = (ImageView) findViewById(R.id.view_bg_view);
    }

    /**
     * 设置起始位置
     * @param position
     */
    public void setLocationStartPosition(int[] position) {
        this.mStartPosition =position;
    }

    /**
     * 设置金币的最大个数
     * @param count
     */
    public void setCount(int count){
        COUNT=count;
    }

    /**
     * 开始动画
     * @param powerNum 倍率
     */
    public void start(int powerNum){
        if(null==mSurfaceView||null==mSurfaceText) return;
        if(isRunning){
            this.removeCallbacks(stopRunnable);
            //480毫秒过后绘制中奖倍数
            SpannableStringBuilder stringBuilder = SpannableStringUtils.getInstance().drawNumFromat(String.valueOf(powerNum));
            //拼接自定义倍数Image
            stringBuilder.append(SpannableStringUtils.getInstance().drawPowerFromat("倍"));
            mSurfaceText.setText(stringBuilder);
            this.postDelayed(stopRunnable,CLEAN_MILLIS);
            return;
        }
        this.setVisibility(VISIBLE);
        isRunning=true;
        //动画播放
        mSurfaceView.setImageResource(R.drawable.gift_draw_big_anim);
        mAnimationDrawable = (AnimationDrawable) mSurfaceView.getDrawable();
        mAnimationDrawable.start();
        //绘制中奖倍数
        SpannableStringBuilder stringBuilder = SpannableStringUtils.getInstance().drawNumFromat(String.valueOf(powerNum));
        //拼接自定义倍数Image
        stringBuilder.append(SpannableStringUtils.getInstance().drawPowerFromat("倍"));
        mSurfaceText.setText(stringBuilder);
        //5秒后自动结束
        this.postDelayed(stopRunnable,CLEAN_MILLIS);
    }

    public void stop(){
        if(null==mSurfaceView||null==mSurfaceText) return;
        if(null!=mAnimationDrawable&&mAnimationDrawable.isRunning()){
            mAnimationDrawable.stop();
        }
        mAnimationDrawable=null;
        mSurfaceView.setImageResource(0);
        if(null!=mSurfaceText) mSurfaceText.setText("");
        AnimationUtil.invisibleTransparentView(this);
        isRunning=false;
    }

    private Runnable stopRunnable =new Runnable() {
        @Override
        public void run() {
            stop();
        }
    };

    /**
     * 开启金币掉落动画
     */
    public void startGoldAnimation() {
        startTask();
    }

    /**
     * 结束金币掉落动画
     */
    public void stopGoldAnimation(){
        stopTask();
    }

    /**
     * 开始执行
     */
    private void startTask(){
        /**
         * 中奖金币掉落场景位置
         * 起始位置X：在礼物Item X轴中间位置向右偏移50DP之间
         * 起始位置Y：在礼物Item高度中心位置
         * 中间位置X：向起始位置X轴正数偏移200像素
         * 中间位置Y：向起始位置Y轴负数偏移300像素
         * 终点位置X：礼物面板按钮中心-->顶部边缘之间
         * 终点位置Y：礼物面板按钮中心位置
         */
        if(null== mStartPosition) return;
        if(isRuning) return;
        count=0;
        isRuning=true;
        mTask = new TimerTask() {
            @Override
            public void run() {
                GiftDrawBigMulitAnimationView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        Context context = getContext();
                        if(null!=context){
                            GoldWireLayout goldWireLayout = new GoldWireLayout(getContext());
                            int startX = AppUtils.getInstance().getRandomNum(AppUtils.getInstance().dpToPxInt(getContext(),72f), AppUtils.getInstance().dpToPxInt(getContext(),92f));
                            int startY=(mStartPosition[1]+AppUtils.getInstance().dpToPxInt(getContext(),30f));
                            goldWireLayout.setStartPosition(new Point(startX,startY));
                            ViewGroup rootView = (ViewGroup) ((Activity) context).getWindow().getDecorView();
                            rootView.addView(goldWireLayout);
                            int[] awardEndLocation = GiftBoardManager.getInstance().getAwardEndLocation();
                            goldWireLayout.setEndPosition(new Point(awardEndLocation[0]+AppUtils.getInstance().dpToPxInt(getContext(),23f), awardEndLocation[1]+AppUtils.getInstance().dpToPxInt(getContext(),15f)));
                            goldWireLayout.startBeizerAnimation();
                            count++;
                            if(count>=COUNT){
                                stopTask();
                            }
                        }
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(mTask, 0, 30);
    }

    private void stopTask() {
        if(null!=timer) timer.cancel();
        timer=null;mTask=null;
        isRuning=false;count=0;
    }

    public void onDestroy(){
        if(null!=mAnimationDrawable&&mAnimationDrawable.isRunning()) mAnimationDrawable.stop();
        stopTask();
        if(null!=mSurfaceView) mSurfaceView.setImageResource(0);
        isRunning=false;mAnimationDrawable=null;
    }
}
