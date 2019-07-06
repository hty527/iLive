package com.android.gift.gift.view;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Shader;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import com.android.gift.gift.manager.SpannableStringUtils;

/**
 * TinyHung@Outlook.com
 * 2018/6/15
 * 一个自定义描边、文字渐变颜色的TextView
 */

public class GiftGradualTextView extends android.support.v7.widget.AppCompatTextView {

    private static final String TAG = "GradualTextView";
    private TextPaint strokePaint;
    private Shader mShaderVertical;
    private long mDuration = 500; // 动画持续时间 ms，默认1s
    private ValueAnimator animator;
    private TimeInterpolator mTimeInterpolator = new LinearInterpolator(); // 动画速率
    private AnimEndListener mEndListener; // 动画正常结束监听事件
    private int olderNum;


    public GiftGradualTextView(Context context) {
        super(context);
    }

    public GiftGradualTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    /**
     * 字体描边
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
//        if (strokePaint == null) strokePaint = new TextPaint();
//        /**
//         x0 渐变起点坐标x位置
//         y0 渐变起点坐标y位置
//         x1 渐变起终点坐标x位置
//         y1 渐变起终点坐标y位置
//         color0 渐变颜色起始色
//         color1 渐变颜色终止色
//         Shader.TileMode tile 平铺方式
//         */
//        if(null==mShaderVertical) mShaderVertical = new LinearGradient(0, getHeight()/4, 0, getHeight(), Color.parseColor("#F79970"),  Color.parseColor("#DE6682"), Shader.TileMode.CLAMP);
//        // 复制原来TextViewg画笔中的一些参数
//        TextPaint paint = getPaint();
//        strokePaint.setTextSize(paint.getTextSize());
//        strokePaint.setTypeface(paint.getTypeface());
//        strokePaint.setFlags(paint.getFlags());
//        strokePaint.setAlpha(paint.getAlpha());
//        //设置垂直的渐变颜色
//        paint.setShader(mShaderVertical);
//        paint.setFakeBoldText(true);//开启粗体
//        // 自定义描边效果
//        strokePaint.setStyle(Paint.Style.STROKE);
//        strokePaint.setColor(Color.WHITE);
//        strokePaint.setStrokeWidth(7);
//        String text = getText().toString();
//        //设置字体的水平间距
////        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
////            strokePaint.setLetterSpacing(0.1f);
////        }
//        //在文本底层画出带描边的文本
//        canvas.drawText(text, 0, getBaseline(), strokePaint);
        super.onDraw(canvas);
    }

    /**
     * 设置动画持续时间，默认为1s。需要在setNumberWithAnim之前设置才有效
     *
     * @param duration
     */
    public void setDuration(long duration) {
        if (duration > 0) {
            mDuration = duration;
        }
    }

    /**
     * 设置动画速率，默认为LinearInterpolator。需要在setNumberWithAnim之前设置才有效
     *
     * @param timeInterpolator
     */
    public void setAnimInterpolator(TimeInterpolator timeInterpolator) {
        mTimeInterpolator = timeInterpolator;
    }

    /**
     * 设置要显示的float数字，带动画显示
     *
     * @param number
     */
    public void setNumberWithAnim(float number) {
        clearAnimator();
        // 设置动画，float值的起始值
        animator = ValueAnimator.ofFloat(0.0f, number);
        startAnimator();
    }

    /**
     * 设置要显示的int数字，带动画显示。
     * @param number
     */
    public void setNumberWithAnim(int number) {
        this.olderNum=number;
        clearAnimator();
        // 设置动画，int值的起始值
        animator = ValueAnimator.ofInt(0, number);
        startAnimator();
    }

    // 清除动画
    public void clearAnimator() {
        if (null != animator) {
            if (animator.isRunning()) {
                animator.removeAllListeners();
                animator.removeAllUpdateListeners();
                animator.cancel();
            }
            animator = null;
        }
    }

    // 暂停动画
    public void onPause() {
        if (null != animator && animator.isRunning()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                animator.pause(); // API 不低于19
            }
        }
    }

    // 继续执行动画
    public void onResume() {
        if (null != animator && animator.isRunning()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                animator.resume();
            }
        }
    }

    // 设置时常与过程处理，启动动画
    private void startAnimator() {
        if (null != animator) {
            animator.setDuration(mDuration);
            animator.setInterpolator(mTimeInterpolator);
            // 动画过程中获取当前值，显示
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    SpannableStringBuilder stringBuilder = SpannableStringUtils.giftNumFromat(valueAnimator.getAnimatedValue().toString());
                    setText(stringBuilder);
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (null != mEndListener) { // 动画不是中途取消，而是正常结束
                        mEndListener.onAnimFinish();
                    }
                    String trim = getText().toString().trim();
                    if(!TextUtils.isEmpty(trim)){
                        try {
                            Integer integer = Integer.valueOf(trim);
                            if(integer!=olderNum){
                                SpannableStringBuilder stringBuilder = SpannableStringUtils.giftNumFromat(String.valueOf(olderNum));
                                setText(stringBuilder);
                            }
                        }catch (Exception e){

                        }
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
            animator.start();
        }
    }

    public void setAnimEndListener(AnimEndListener listener) {
        mEndListener = listener;
    }

    // 动画显示数字的结束监听，当动画结束显示正确的数字时，可能需要做些处理
    public interface AnimEndListener {
        void onAnimFinish();
    }
}
