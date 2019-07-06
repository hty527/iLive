package com.android.gift.room.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.android.gift.R;
import com.android.gift.model.BezierEvaluator;

/**
 * TinyHung@Outlook.com
 * 2018/8/25
 * 金币线条
 * 也适用于购物车动画
 */

public class GoldWireLayout extends android.support.v7.widget.AppCompatImageView implements ValueAnimator.AnimatorUpdateListener {

    public static final int VIEW_SIZE = 20;
    private static final String TAG = "GoldWireLayout";
    protected Context mContext;
    protected Paint mPaint4Circle;
    protected int radius;
    protected Point startPosition;
    protected Point endPosition;

    /**
     *
     金币奖励调用
     GoldWireLayout goldWireLayout = new GoldWireLayout(getContext());
     int position[] = new int[2];
     view.getLocationInWindow(position);
     goldWireLayout.setStartPosition(new Point(position[0], position[1]));
     ViewGroup rootView = (ViewGroup) mActivity.getWindow().getDecorView();
     rootView.addView(goldWireLayout);
     int endPosition[] = new int[2];
     endPosition[0]=540;
     endPosition[1]=1920;
     goldWireLayout.setEndPosition(new Point(endPosition[0], endPosition[1]));
     goldWireLayout.startBeizerAnimation();
     * @param context
     */
    public GoldWireLayout(Context context) {
        this(context, null);
    }

    public GoldWireLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoldWireLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mPaint4Circle = new Paint();
        mPaint4Circle.setColor(Color.YELLOW);
        mPaint4Circle.setAntiAlias(true);
        setImageResource(R.drawable.ic_gift_money);//默认是金币
    }

    /**
     * 确定起始点
     * @param startPosition
     */
    public void setStartPosition(Point startPosition) {
        startPosition.y -= 10;
        this.startPosition = startPosition;
    }

    /**
     * 确定中间点
     * @param centerPosition
     */
    public void setCenterPosition(Point centerPosition){

    }

    /**
     * 确定结束点位置
     * @param endPosition
     */
    public void setEndPosition(Point endPosition) {
        this.endPosition = endPosition;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int PX4SIZE = (int) convertDpToPixel(VIEW_SIZE, mContext);
        setMeasuredDimension(PX4SIZE, PX4SIZE);
        radius = PX4SIZE / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, radius, mPaint4Circle);
        super.onDraw(canvas);
    }

    /**
     * 开始从起始点沿着中间点到达结束点
     * @param randomIndex 随机角标 介于 23-430之间 偏左 向左偏移-200像素 偏右：向右偏移200像素
     *                    180-220之间 不做偏移
     */
    public void startBeizerAnimation(int randomIndex) {
        if (startPosition == null || endPosition == null) return;
        int pointX =startPosition.x;//偏移像素
        if(randomIndex<180){
            pointX=(startPosition.x-500);
        }else if(randomIndex>220){
            pointX=(startPosition.x+500);
        }
        int pointY = (int) (startPosition.y - convertDpToPixel(300, mContext));
        Point controllPoint = new Point(pointX, pointY);
        BezierEvaluator bezierEvaluator = new BezierEvaluator(controllPoint);
        ValueAnimator anim = ValueAnimator.ofObject(bezierEvaluator, startPosition, endPosition);
        anim.addUpdateListener(this);
        anim.setDuration(1000);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ViewGroup viewGroup = (ViewGroup) getParent();
                viewGroup.removeView(GoldWireLayout.this);
                if(null!=mOnAnimationListener) mOnAnimationListener.onAnimationEnd();
            }
        });
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }


    /**
     * 开始从起始点沿着中间点到达结束点
     */
    public void startBeizerAnimation() {
        if (startPosition == null || endPosition == null) return;
        int pointX = (startPosition.x+200);
        int pointY = (int) (startPosition.y - convertDpToPixel(150, mContext));
        Point controllPoint = new Point(pointX, pointY);
        BezierEvaluator bezierEvaluator = new BezierEvaluator(controllPoint);
        ValueAnimator anim = ValueAnimator.ofObject(bezierEvaluator, startPosition, endPosition);
        anim.addUpdateListener(this);
        anim.setDuration(1000);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ViewGroup viewGroup = (ViewGroup) getParent();
                viewGroup.removeView(GoldWireLayout.this);
            }
        });
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

    /**
     * 绘制View所在位置
     * @param animation
     */
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        Point point = (Point) animation.getAnimatedValue();
        setX(point.x);
        setY(point.y);
        invalidate();
    }


    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public interface OnAnimationListener{
        void onAnimationEnd();
    }

    private OnAnimationListener mOnAnimationListener;

    public void setOnAnimationListener(OnAnimationListener onAnimationListener) {
        mOnAnimationListener = onAnimationListener;
    }
}
