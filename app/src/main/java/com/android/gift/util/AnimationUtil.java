package com.android.gift.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.android.gift.listener.OnAnimationListener;

/**
 * TinyHung@outlook.com
 * 2017/7/28
 * 动画生产和播放
 */
public class AnimationUtil {

    private static final String TAG = AnimationUtil.class.getSimpleName();
    /**
     * 从控件所在位置移动到控件的底部
     *  从上往下出场
     * @return
     */
    public static TranslateAnimation moveToViewBottom() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mHiddenAction.setDuration(350);
        return mHiddenAction;
    }

    /**
     * 从控件所在位置移动到控件的底部
     *  从上往下出场
     * @return
     */

    public static TranslateAnimation moveToViewBottom2() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 2.0f);
        mHiddenAction.setDuration(350);
        mHiddenAction.setInterpolator(new LinearInterpolator());
        return mHiddenAction;
    }


    /**
     * 从控件的顶部移动到控件所在位置
     * 从上往下进场
     * @return
     */
    public static TranslateAnimation moveToViewTopLocation() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(350);
        mHiddenAction.setInterpolator(new LinearInterpolator());
        return mHiddenAction;
    }

    /**
     * 从控件的底部移动到控件所在位置
     * 从下往上进场
     * @return
     */
    public static TranslateAnimation moveToViewLocation() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(350);
        mHiddenAction.setInterpolator(new LinearInterpolator());
        return mHiddenAction;
    }


    /**
     * 从控件的顶部移动到控件所在位置
     * 从上往下进场
     * @return
     */
    public static TranslateAnimation moveToViewTopLocation5() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(450);
        return mHiddenAction;
    }

    /**
     * 从控件的底部移动到控件所在位置
     * 从下往上进场
     * @return
     */
    public static TranslateAnimation moveToViewLocation5() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(450);
        return mHiddenAction;
    }


    /**
     * 从控件的底部移动到控件所在位置
     * 从下往上进场
     * @return
     */

    public static TranslateAnimation moveToViewLocation2() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                2.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(350);
        return mHiddenAction;
    }



    /**
     * 从控件所在的位置移动到控件的右边
     * @return
     */
    public static TranslateAnimation moveToViewRight() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(350);
        return mHiddenAction;
    }

    /**
     * 从控件右边移动到控件的所在位置
     * @return
     */
    public static TranslateAnimation moveLeftToViewLocation() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }


    /**
     * 从控件所在位置移动到控件的顶部
     *  从下往下上出场
     * @return
     */
    public static TranslateAnimation moveToViewTop() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f);
        mHiddenAction.setDuration(350);
        mHiddenAction.setInterpolator(new LinearInterpolator());
        return mHiddenAction;
    }


    /**
     * 相对自身大小缩放至不见
     * @return
     */
    public static ScaleAnimation moveThisScaleViewToDissmes(){
        ScaleAnimation animation = new ScaleAnimation(1.0f,0.0f,1.0f,0.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(100);
        return animation;
    }

    /**
     * 相对自身一般缩放到自身大小
     * @return
     */
    public static ScaleAnimation moveThisScaleViewToBig(){
        ScaleAnimation animation =  new ScaleAnimation(0.1f, 1.0f, 0.0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setDuration(200);
        return animation;
    }

    public static ScaleAnimation moveThisScaleViewToBigMenu(){
        ScaleAnimation animation =  new ScaleAnimation(0.1f, 1.0f, 0.0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setDuration(100);
        return animation;
    }

    /**
     * 点赞的动画小
     * @return
     */
    public static ScaleAnimation followAnimation(){
        ScaleAnimation followScaleAnimation = new ScaleAnimation(1.0f, 1.6f, 1.0f, 1.6f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        followScaleAnimation.setDuration(500);
        return followScaleAnimation;
    }

    /**
     * 无限期旋转动画
     * @return
     */
    public static RotateAnimation createRotateAnimation(){
        RotateAnimation rotate  = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(2000);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setRepeatMode(Animation.REVERSE);

        return rotate;
    }

    public static void displayAnim(View view, Context context, int animId, int targetVisibility){
        view.clearAnimation();
        Animation anim =
                android.view.animation.AnimationUtils.loadAnimation(context, animId);
        view.setVisibility(targetVisibility);
        view.startAnimation(anim);
    }

    /**
     * 透明度渐变显示图层
     * @param view
     */
    public static void visibTransparentView(View view){
        if(null==view)return;
        if(view.getVisibility()==View.VISIBLE) return;
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(200);
        view.setVisibility(View.VISIBLE);
        view.startAnimation(animation);
    }

    public static void visibTransparentView(View view,long durtion){
        if(null==view)return;
        if(view.getVisibility()==View.VISIBLE) return;
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(durtion);
        view.setVisibility(View.VISIBLE);
        view.startAnimation(animation);
    }


    /**
     * 透明度渐变隐藏图层
     * @param view
     */
    public static void goneTransparentView(final View view){
        if(null==view)return;
        if(view.getVisibility()==View.GONE) return;
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(200);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    /**
     * 透明度渐变隐藏图层
     * @param view
     * @param durtion 动画耗时
     */
    public static void goneTransparentView(final View view,long durtion){
        if(null==view)return;
        if(view.getVisibility()==View.GONE) return;
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(durtion);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    public static void invisibleTransparentView(final View view){
        if(null==view)return;
        if(view.getVisibility()==View.INVISIBLE) return;
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(200);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    public static void goneTransparentView(final View view,int durtion){
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(durtion);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }


    /**
     * 平移动画
     * 从view所在的位置的底部移动到所在位置
     */
    public static void visibViewBottomToPoistionForTranslation(View view){
        if(null==view)return;
        view.setVisibility(View.VISIBLE);
        view.startAnimation(moveToViewLocation2());
    }

    /**
     * 平移动画
     * 从view所在的位置移动到底部
     */
    public static  void goneViewPoistionToBottomForTranslation(final View view){
        if(null==view)return;
        TranslateAnimation translateAnimation = moveToViewBottom();
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(moveToViewLocation());
    }
    //        AccelerateDecelerateInterpolator============动画开始与结束的地方速率改变比较慢，在中间的时候加速。
//        AccelerateInterpolator===================动画开始的地方速率改变比较慢，然后开始加速。
//        AnticipateInterpolator ==================开始的时候向后然后向前甩。
//        AnticipateOvershootInterpolator=============开始的时候向后然后向前甩一定值后返回最后的值。
//        BounceInterpolator=====================动画结束的时候弹起。
//        CycleInterpolator======================动画循环播放特定的次数，速率改变沿着正弦曲线。
//        DecelerateInterpolator===================在动画开始的地方快然后慢。
//        LinearInterpolator======================以常量速率改变。
//        OvershootInterpolator====================向前甩一定值后再回到原来位置。
//        PathInterpolator========================新增的，就是可以定义路径坐标，然后可以按照路径坐标来跑动；注意其坐标并不

    /**
     * 连击动画
     * @param view
     * @return
     */
    public static void doubleCountAnimation(View view){
        if(null==view) return;
        view.clearAnimation();
        AnimatorSet animatorSet1=new AnimatorSet();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1.1f).setDuration(100);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1.1f).setDuration(100);
        animatorSet1.playTogether(animator1,animator2);//一起执行
        final AnimatorSet animatorSet2=new AnimatorSet();
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(view, "scaleX", 1.1f, 1.0f).setDuration(90);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(view, "scaleY", 1.1f, 1.0f).setDuration(90);
        animatorSet2.setInterpolator(new OvershootInterpolator());
        animatorSet2.playTogether(animator3,animator4);
        animatorSet1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animatorSet2.start();//第一个完成后接着播放第二个动画
            }
        });
        animatorSet1.start();//立马执行
    }


    /**
     * 播放点击礼物后的动画,数字变化动画
     * @param view
     * @return
     */
    public static void playTextCountAnimation(View view){
        if(null==view) return;
        view.clearAnimation();
        AnimatorSet animatorSet1=new AnimatorSet();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1.2f).setDuration(150);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1.2f).setDuration(150);
        animatorSet1.playTogether(animator1,animator2);//一起执行
        final AnimatorSet animatorSet2=new AnimatorSet();
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(view, "scaleX", 1.2f, 1.0f).setDuration(100);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(view, "scaleY", 1.2f, 1.0f).setDuration(100);
        animatorSet2.setInterpolator(new OvershootInterpolator());
        animatorSet2.playTogether(animator3,animator4);
        animatorSet1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animatorSet2.start();//第一个完成后接着播放第二个动画
            }
        });
        animatorSet1.start();//立马执行
    }

    /**
     * 播放点击礼物后的动画,数字变化动画
     * @param view
     * @return
     */
    public static void playTextCountAnimation2(View view){
        if(null==view) return;
        view.clearAnimation();
        AnimatorSet animatorSet1=new AnimatorSet();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1.3f).setDuration(150);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1.3f).setDuration(150);
        animatorSet1.playTogether(animator1,animator2);//一起执行
        final AnimatorSet animatorSet2=new AnimatorSet();
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(view, "scaleX", 1.3f, 1.0f).setDuration(100);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(view, "scaleY", 1.3f, 1.0f).setDuration(100);
        animatorSet2.setInterpolator(new OvershootInterpolator());
        animatorSet2.playTogether(animator3,animator4);
        animatorSet1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animatorSet2.start();//第一个完成后接着播放第二个动画
            }
        });
        animatorSet1.start();//立马执行
    }

    /**
     * 播放点击礼物后的动画 item选中后的动画
     * @param view
     * @return
     */
    public static void playAnimation(View view, final OnAnimationListener listener){
        if(null==view) return;
        view.clearAnimation();
        AnimatorSet animatorSet1=new AnimatorSet();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.7f).setDuration(90);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.7f).setDuration(90);
        animatorSet1.playTogether(animator1,animator2);//一起执行

        final AnimatorSet animatorSet2=new AnimatorSet();
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(view, "scaleX", 0.7f, 1.0f).setDuration(130);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(view, "scaleY", 0.7f, 1.0f).setDuration(130);
        animatorSet2.setInterpolator(new OvershootInterpolator());
        animatorSet2.playTogether(animator3,animator4);
        animatorSet2.setInterpolator(new BounceInterpolator());//BounceInterpolator动画结束的时候弹起
        animatorSet1.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if(null!=listener){
                    listener.onStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animatorSet2.start();//第一个完成后接着播放第二个动画
            }
        });
        animatorSet2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(null!=listener){
                    listener.onEnd();
                }
            }
        });
        animatorSet1.start();//立马执行
    }

    /**
     * 组件平移上升，渐变消失
     * @param view
     */
    public static void translationToveToTop(final View view) {
        if(null==view) return;
        view.clearAnimation();
        AnimatorSet animatorSet1=new AnimatorSet();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "translationY", 1.0f, -1.0f).setDuration(350);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f).setDuration(150);
        animatorSet1.playTogether(animator1,animator2);//一起执行
        animatorSet1.setInterpolator(new LinearInterpolator());//匀速动画
        animatorSet1.start();//立马执行
    }

    /**
     * 组件平移下降，渐变消失
     * @param view
     */
    public static void translationToveToBottom(final View view) {
        if(null==view) return;
        view.clearAnimation();
        AnimatorSet animatorSet1=new AnimatorSet();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "translationY", 1.0f, 2.0f).setDuration(350);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f).setDuration(150);
        animatorSet1.playTogether(animator1,animator2);//一起执行
        animatorSet1.setInterpolator(new LinearInterpolator());//匀速动画
        animatorSet1.start();//立马执行
    }

    /**
     * 从0放大某个组件
     */
    public static ScaleAnimation scalViewAnimationToBig(){
        ScaleAnimation scal = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scal.setDuration(300);
        scal.setFillAfter(true);//放大后保持不变
        return scal;
    }

    public static ScaleAnimation scalViewAnimationToHid(){
        ScaleAnimation scal = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scal.setDuration(300);
        scal.setFillAfter(true);//放大后保持不变
        return scal;
    }

    /**
     * 放大某个组件
     * @param view
     */
    public static void scalViewAnimationAdd(View view){
        if(null==view) return;
        ScaleAnimation scal = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scal.setDuration(0);
        scal.setFillAfter(true);//放大后保持不变
        view.startAnimation(scal);
    }

    public static void scalViewAnimationReturn(View view){
        if(null==view) return;
        ScaleAnimation scal = new ScaleAnimation(1.1f, 1.0f, 1.1f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scal.setDuration(0);
        scal.setFillAfter(true);//放大后保持不变
        view.startAnimation(scal);
    }

    /**
     * 从大到小-到连续抖动两次
     * @param view
     * @param scaleSmall
     * @param scaleLarge
     * @param shakeDegrees
     * @param duration
     */
    public static void startShakeByPropertyAnim(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }
        //先变小后变大
        PropertyValuesHolder scaleXValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );
        PropertyValuesHolder scaleYValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );

        //先往左再往右
        PropertyValuesHolder rotateValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, -shakeDegrees),
                Keyframe.ofFloat(0.2f, shakeDegrees),
                Keyframe.ofFloat(0.3f, -shakeDegrees),
                Keyframe.ofFloat(0.4f, shakeDegrees),
                Keyframe.ofFloat(0.5f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, shakeDegrees),
                Keyframe.ofFloat(0.7f, -shakeDegrees),
                Keyframe.ofFloat(0.8f, shakeDegrees),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f)
        );
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleXValuesHolder, scaleYValuesHolder,rotateValuesHolder);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    /**
     * 从空间所在的位置平移到空间的下方
     * @param view
     */
    public static void gonePoistionToBottomForTranslationAnimation(final View view) {
        TranslateAnimation translateAnimation = moveToViewBottom();
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(translateAnimation);
    }

    /**
     * 从控件的底部移动到空间所在的位置
     * @param view
     */
    public static void viewBottomToPoistionForTranslationAnimation(final View view) {
        view.setVisibility(View.VISIBLE);
        view.startAnimation(moveToViewLocation());
    }

    public static void start(View view) {
        if(null==view) return;
        view.clearAnimation();
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX",1.7f, 1.0f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleY",1.7f, 1.0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(90);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(anim1, anim2);
        animSet.start();
    }

    /**
     * 播放器暂停播放 播放按钮动画
     * @param view
     */
    public static void playPlayPauseAnimation(View view) {
        if(null==view) return;
        if(view.getVisibility()!=View.VISIBLE) view.setVisibility(View.VISIBLE);
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX",3.0f, 1.0f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleY",3.0f, 1.0f);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(view, "alpha",0.0f, 1.0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(300);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(anim1, anim2,anim3);
        animSet.start();
    }

    /**
     * 播放器开始播放 播放按钮动画
     * @param view
     */
    public static void playPlayPlayAnimation(final View view) {
        if(null==view) return;
        if(view.getVisibility()!=View.VISIBLE) view.setVisibility(View.VISIBLE);
//        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX",1.0f, 2.0f);
//        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleY",1.0f, 2.0f);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(view, "alpha",1.0f, 0.0f);
        anim3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        });
        anim3.setDuration(200);
        anim3.setInterpolator(new LinearInterpolator());
        anim3.start();
    }


    /**
     * View 小倍率中奖动画出场
     * @param view
     * @param duration
     */
    public static void startSmallAwardAnimOut(View view, long duration, final OnAnimationListener listener) {
        if (view == null) {
            return;
        }
        view.clearAnimation();
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.0f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.0f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(null!=listener) listener.onEnd();
            }
        });
        objectAnimator.start();
    }

    /**
     * View 小倍率中奖动画进场
     * @param view
     * @param duration
     */
    public static void startSmallAwardAnim(View view, long duration, final OnAnimationListener listener) {
        if (view == null) {
            return;
        }
        view.clearAnimation();
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 1.5f, 0.6f, 1.0f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 1.5f, 0.6f, 1.0f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(null!=listener) listener.onEnd();
            }
        });
        objectAnimator.start();
    }

    /**
     * View 中超级大奖动画消息
     * 从大到小-到连续抖动两次
     * @param view
     * @param duration
     */
    public static void startSuperAwardUserAnim(View view, long duration, final OnAnimationListener listener) {
        if (view == null) {
            return;
        }
        view.clearAnimation();
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 2.6f, 0.1f, 1.5f,0.5f,1.2f,0.8f,1.0f,0.9f,1.0f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 2.6f, 0.1f, 1.4f,0.5f,1.2f,0.8f,1.0f,0.9f,1.0f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());//一开始速度很快，后来越来越慢
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(null!=listener) listener.onEnd();
            }
        });
        objectAnimator.start();
    }

}