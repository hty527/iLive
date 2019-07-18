package com.android.gift.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import com.android.gift.R;
import com.android.gift.util.AppUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TinyHung@Outlook.com
 * 2018/8/24
 * 自定义View+属性动画实现咻一咻雷达效果
 */

public class RadarLayout extends RelativeLayout{

    private static final String TAG = "IndexZhuanRadarLayout";
    private Paint mPaint;
    private int mColor=getResources().getColor(R.color.colorAccent);//默认是APP主题色
    private float mRadius;
    private float mCenterX;
    private float mCenterY;
    private int mStrokeWidth;
    private boolean isRuning;//是否正在运行
    private int mPlayDurtion=2000;//播放一个圆圈需要多久 毫秒
    private Timer timer;
    private boolean mIsRing;//是否绘制环形
    private static int DEFAULT_STROKE_WIDTH = 2;//圆环的宽度 仅在mIsRing为true有效 单位dp
    private boolean mIsAutoRuning;
    private float mMinAlpha =0f;//圆圈最小的透明度
    //总运行计时
    private long DURRENT_NUM=0;


    public RadarLayout(Context context) {
        super(context);
        init(context,null);
    }

    public RadarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }
    public RadarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if(null!=attrs){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RadarLayout );
            mColor = typedArray.getInt(R.styleable.RadarLayout_radarColor, getResources().getColor(R.color.colorAccent));//圆圈、圆环颜色
            mPlayDurtion = typedArray.getInt(R.styleable.RadarLayout_radarPlayDurtion, 2000);//一圈动画需要多久播放完成
            DEFAULT_STROKE_WIDTH = typedArray.getInt(R.styleable.RadarLayout_radarStrokeWidth, 2);//圆环的宽度
            mIsRing = typedArray.getBoolean(R.styleable.RadarLayout_radarIsRing, false);//是否是圆环
            mIsAutoRuning = typedArray.getBoolean(R.styleable.RadarLayout_radarIsAutoRun, false);//是否自动开始
            mMinAlpha = typedArray.getFloat(R.styleable.RadarLayout_radarMinAlpha, 0f);//圆圈的最小透明度
            typedArray.recycle();
        }
        mStrokeWidth = AppUtils.getInstance().dpToPxInt(DEFAULT_STROKE_WIDTH);
        if(mIsAutoRuning) onStart();
    }

    /**
     * 设置圆圈、圆环颜色
     * @param color
     */
    public void setStyleColor(int color){
        this.mColor=color;
        invalidate();
    }

    /**
     * 设置一圈需要多久播放完成
     * @param playMillis
     * 将在下次绘制生效
     */
    public void setPlayDurtion(int playMillis){
        this.mPlayDurtion=playMillis;
    }

    /**
     * 是否绘制环形
     * @param isRing
     */
    public void setDrawCircleRing(boolean isRing){
        this.mIsRing=isRing;
        invalidate();
    }

    /**
     * 设置环形的宽度
     * @param widthDP 单位DP
     * 将在下次绘制生效
     */
    public void setRingStrokeWidthDP(int widthDP){
        this.DEFAULT_STROKE_WIDTH=widthDP;
    }

    /**
     * 设置环形的宽度
     * @param widthPX 单位PX
     * 将在下次绘制生效
     */
    public void setRingStrokeWidthPX(int widthPX){
        this.mStrokeWidth=widthPX;
    }

    /**
     * 设置最小透明度
     * @param minAlpha
     * 将在下次绘制生效
     */
    public void setMinAlpha(float minAlpha){
        this.mMinAlpha =minAlpha;
    }

    /**
     * 开始执行
     */
    public void onStart(){
        startPlayer();
    }

    /**
     * 间歇性的雷达
     */
//    public void startPlayer(){
//        if(isRuning) return;
//        isRuning=true;
//        DURRENT_NUM=0;
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                if(DURRENT_NUM>=2000){
//                    DURRENT_NUM=0;
//                }
//                if(DURRENT_NUM==0){
//                    RadarLayout.this.post(new TaskRunnable(0));
//                }
//                if(DURRENT_NUM==400){
//                    RadarLayout.this.post(new TaskRunnable(500));
//                }
//                if(DURRENT_NUM==800){
//                    RadarLayout.this.post(new TaskRunnable(1000));
//                }
//                DURRENT_NUM+=100;
//            }
//        };
//        if(null==timer) timer = new Timer();
//        //100高速运行
//        timer.schedule(task, 0, 100);
//    }

    /**
     * 无限循环的雷达
     */
    public void startPlayer(){
        if(isRuning) return;
        isRuning=true;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                RadarLayout.this.post(new TaskRunnable(0));
            }
        };
        if(null==timer) timer = new Timer();
        //100高速运行
        timer.schedule(task, 0, 600);
    }

    /**
     * 主线程
     */
    private class TaskRunnable implements Runnable {

        private final int mMillis;

        public TaskRunnable(int millis) {
            this.mMillis=millis;
        }

        @Override
        public void run() {
            addACircle();
        }
    }

    /**
     * 结束执行
     */
    public void onStop(){
        isRuning=false;
        if(null!=timer) timer.cancel(); timer=null;
        DURRENT_NUM=0;
    }

    /**
     * 对应生命周期调用
     */
    public void onDestroy(){
        onStop();
        mPaint=null; mIsRing=false;
    }


    /**
     * 绘制一个圆
     * 2.0.0版本需求：
     * 中间金币图标大小为80DP,圆形直径为140DP,光晕效果从76DP处开始向外扩散,透明度从80DP-140DP开始不透明到完全透明
     * 计算公式：55/79*100*0.01,即为起始X、Y，重点X、Y为1.0
     */
    private void addACircle() {
        //绘制一个个自己一样大小的圆、环
        LayoutParams params = new LayoutParams(-1,-1);
        List<Animator> animators = new ArrayList<Animator>();
        final RadarView radarView = new RadarView(getContext());
        radarView.setScaleX(0.54f);
        radarView.setScaleY(0.54f);
        radarView.setAlpha(1);
        //添加在布局中最底层
        addView(radarView, 0, params);
        // 属性动画
        animators.add(create(radarView, "scaleX", 0.696f, 1.0f));
        animators.add(create(radarView, "scaleY", 0.696f, 1.0f));
        animators.add(create(radarView, "alpha", 1, mMinAlpha));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animators);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(mMinAlpha>0) radarView.setAlpha(0);//用户设置的透明通道大于0，强制隐藏
                RadarLayout.this.removeView(radarView);//移除刚才添加的
            }
        });
        animatorSet.setDuration(mPlayDurtion);
        //大家一起玩
        animatorSet.start();
    }

    private ObjectAnimator create(View target, String propertyName, float from, float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, propertyName, from, to);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        return animator;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        // 确定圆的圆点坐标及半径
        mCenterX = width * 0.5f;
        mCenterY = height * 0.5f;
        mRadius = Math.min(width, height) * 0.5f;
    }

    private class RadarView extends View {

        public RadarView(Context context) {
            super(context);
        }
        @Override
        protected void onDraw(Canvas canvas) {
            if (null == mPaint) {
                mPaint = new Paint();
                mPaint.setColor(mColor);
                mPaint.setAntiAlias(true);
                // 注意Style的用法，【STROKE：画环】【FILL：画圆】
                mPaint.setStyle(mIsRing ? Paint.Style.STROKE : Paint.Style.FILL);
                mPaint.setStrokeWidth(mIsRing ? mStrokeWidth : 0);
            }
            // 画圆或环
            canvas.drawCircle(mCenterX, mCenterY, mIsRing ? mRadius - mStrokeWidth : mRadius, mPaint);
        }
    }

    /**
     * 在两个View之间翻转
     * @param view1
     * @param view2
     */
    private void reversalView(final View view1, final View view2) {
        final int duration = 200;
        final int degree = 90;
        final int degree2 = -degree;
        final ObjectAnimator a, b;
        a = ObjectAnimator.ofFloat(view1, "rotationY", 0, degree);
        b = ObjectAnimator.ofFloat(view2, "rotationY", degree2, 0);
        a.setDuration(duration);
        b.setDuration(duration);
        a.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.GONE);
        AnimatorSet set = new AnimatorSet();
        set.play(a).before(b);
        set.start();
    }
}