package com.android.gift.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.android.gift.R;
import com.android.gift.util.AppUtils;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TinyHung@outlook.com
 * 2017/6/23 16:32
 * 多用途圆形进度条
 */

public class CircleProgressView extends View {

    private static final String TAG = "CircleProgressView";
    private final int mTxtStrokeWidth = 2;
    // 画圆所在的距形区域
    private final RectF mRectF;
    private final Paint mPaint;
    private Context mContext;
    private String mTxtHint1;
    private String mTxtHint2;
    private int mProgress = 0;
    private int mMaxProgress = 3000;
    private float mStrokeWidth;
    private boolean mShowtext;
    private float mTextSize;
    private int mProCirBgColor;
    //背景颜色默认是透明的
    private int mProBgColor;
    private int mProColor;
    private int mProTextColor;
    private Timer mTimer;
    //每100毫秒刻度进度
    private long mScaleProgress=0;
    //进度一圈总耗时，默认1分钟
    private long mTotalMillis=60000;

    public CircleProgressView(Context context){
        this(context,null);
    }
    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(null!=attrs){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView);
            mProCirBgColor = typedArray.getColor(R.styleable.CircleProgressView_circle_progress_cir_bg_color, Color.rgb(0xE6, 0xE6, 0xE6));
            mProBgColor = typedArray.getColor(R.styleable.CircleProgressView_circle_progress_bg_color, Color.argb(0,0,0,0));
            mProColor = typedArray.getColor(R.styleable.CircleProgressView_circle_progress_color, Color.rgb(0xFF, 0x32, 0x00));
            mProTextColor = typedArray.getColor(R.styleable.CircleProgressView_circle_progress_text_color, Color.rgb(0xFF, 0xFF, 0xFF));
            mTextSize = typedArray.getDimensionPixelSize(R.styleable.CircleProgressView_circle_progress_text_size, 15);
            mStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.CircleProgressView_circle_progress_stroke_width, 4);
            mShowtext = typedArray.getBoolean(R.styleable.CircleProgressView_circle_progress_show_text, false);
            mMaxProgress = typedArray.getInt(R.styleable.CircleProgressView_circle_progress_max, 3000);
            typedArray.recycle();
        }else {
            mTextSize= AppUtils.getInstance().dpToPxInt(15f);
            mStrokeWidth= AppUtils.getInstance().dpToPxInt(4f);
            mProBgColor = Color.argb(0,0,0,0);
            mProCirBgColor =Color.rgb(0xE6, 0xE6, 0xE6);
            mProColor=Color.rgb(0xFF, 0x32, 0x00);
            mProTextColor=Color.rgb(0xFF, 0xFF, 0xFF);
        }
        mContext = context;
        mRectF = new RectF();
        //通用画笔
        mPaint = new Paint();
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=mOnActionListener){
                    mOnActionListener.onClick(CircleProgressView.this);
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();
        if (width != height) {
            int min = Math.min(width, height);
            width = min;
            height = min;
        }
        //设置画笔相关属性
        mPaint.setAntiAlias(true);
        //环形背景
        mPaint.setColor(mProCirBgColor);
        //中间透明
        canvas.drawColor(Color.TRANSPARENT);
        mPaint.setStrokeWidth(mStrokeWidth);
        //描边样式
        mPaint.setStyle(Paint.Style.STROKE);
        //描边圆角
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //圆环位置
        mRectF.left = mStrokeWidth / 2; // 左上角x
        mRectF.top = mStrokeWidth / 2; // 左上角y
        mRectF.right = width - mStrokeWidth / 2; // 左下角x
        mRectF.bottom = height - mStrokeWidth / 2; // 右下角y
        //进度条背景
        canvas.drawArc(mRectF, -90, 360, false, mPaint);
        //圆环进度颜色
        mPaint.setColor(mProColor);//圆边进度颜色
        canvas.drawArc(mRectF, -90, ((float) mProgress / mMaxProgress) * 360, false, mPaint);
        //绘制背景
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mProBgColor);
        mPaint.setAntiAlias(true);
        //进度条控件背景，这里很奇怪，按照宽度标准绘制圆形，有间隙
        mRectF.left = mStrokeWidth; // 左上角x
        mRectF.top = mStrokeWidth; // 左上角y
        mRectF.right = width - mStrokeWidth; // 左下角x
        mRectF.bottom = height - mStrokeWidth; // 右下角y
        canvas.drawArc(mRectF, -90, 360, true, mPaint);
        if(mShowtext){
            // 绘制进度文案显示
            mPaint.setStrokeWidth(mTxtStrokeWidth);
            String text = mProgress + "%";
            int textHeight = height / 4;
            mPaint.setTextSize(textHeight);
            int textWidth = (int) mPaint.measureText(text, 0, text.length());
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(text, width / 2 - textWidth / 2, height / 2 + textHeight / 2, mPaint);

            if (!TextUtils.isEmpty(mTxtHint1)) {
                mPaint.setStrokeWidth(mTxtStrokeWidth);
                text = mTxtHint1;
                textHeight = height / 8;
                mPaint.setTextSize(textHeight);
                mPaint.setColor(mProTextColor);
                textWidth = (int) mPaint.measureText(text, 0, text.length());
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawText(text, width / 2 - textWidth / 2, height / 4 + textHeight / 2, mPaint);
            }

            if (!TextUtils.isEmpty(mTxtHint2)) {
                mPaint.setStrokeWidth(mTxtStrokeWidth);
                text = mTxtHint2;
                textHeight = height / 8;
                mPaint.setTextSize(textHeight);
                textWidth = (int) mPaint.measureText(text, 0, text.length());
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawText(text, width / 2 - textWidth / 2, 3 * height / 4 + textHeight / 2, mPaint);
            }
        }
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
    }

    public void setProgressNotInUiThread(int progress) {
        this.mProgress = progress;
        this.postInvalidate();
    }

    public String getmTxtHint1() {
        return mTxtHint1;
    }

    public void setmTxtHint1(String mTxtHint1) {
        this.mTxtHint1 = mTxtHint1;
    }

    public String getmTxtHint2() {
        return mTxtHint2;
    }

    public void setmTxtHint2(String mTxtHint2) {
        this.mTxtHint2 = mTxtHint2;
    }

    /**
     * 设置圆环一圈耗时时长
     * @param totalMillis 毫秒
     */
    public void setTotalMillis(long totalMillis){
        this.mTotalMillis=totalMillis;
    }

    /**
     * 进度条递增增长，这里引入“环”的概念
     */
    public synchronized void startDegress() {
        stopDegress();
        mScaleProgress = mMaxProgress / (mTotalMillis / 100);
        mScaleProgress=mMaxProgress/mTotalMillis;
        if(null==mTimer){
            mTimer = new Timer();
            mTimer.schedule(new DegressTask(),0,100);
        }
    }

    /**
     * 结束自动环比增长
     */
    private void stopDegress() {
        if(null!=mTimer){
            mTimer.cancel();
            mTimer=null;
        }
    }

    /**
     * 设置指定进度
     * @param progress 已完成进度
     */
    public void setProgress(int progress) {
        //根据已完成进度和总进度求出已完成环数
        this.mProgress = progress;
        this.invalidate();
    }

    /**
     * 环比进度是否已经走完了
     * @return true:到达终点
     */
    public boolean isSuccess() {
        if(mProgress>=mMaxProgress){
            return true;
        }
        return false;
    }

    //一秒转10
    private class DegressTask extends TimerTask{

        @Override
        public void run() {
            mProgress+=10;
            setProgressNotInUiThread(mProgress);
            if(mProgress>=mMaxProgress){
                stopDegress();
                if(null!=mOnActionListener){
                    CircleProgressView.this.post(new Runnable() {
                        @Override
                        public void run() {
                            mOnActionListener.onSuccess(mMaxProgress);
                        }
                    });
                }
            }
        }
    }

    /**
     * 还原
     */
    public void onReset() {
        stopDegress();
        mProgress=0;
        invalidate();
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        mOnActionListener = onActionListener;
    }

    public void onDestroy(){
        mOnActionListener=null;
        stopDegress();
        mContext=null;
    }

    private OnActionListener mOnActionListener;

    public interface OnActionListener{
        /**
         * 单击事件
         * @param view 进度条
         */
        void onClick(View view);

        /**
         * 已完成
         * @param maxProgress 总进度
         */
        void onSuccess(int maxProgress);
    }
}