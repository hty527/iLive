package com.android.gift.gift.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.gift.R;
import com.android.gift.bean.GiftItemInfo;
import com.android.gift.gift.manager.GiftBoardManager;
import com.android.gift.gift.manager.SpannableStringUtils;
import com.android.gift.model.GlideCircleTransform;
import com.android.gift.util.AppUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * TinyHung@Outlook.com
 * 2018/7/26
 * 直播间倒计时赠送礼物按钮
 */

public class CountdownGiftView extends LinearLayout {

    private static final String TAG = "CountdownGiftView";
    private CircleTextProgressbar mProgressbar;
    //礼物倒计时一圈耗时时长，单位：秒
    private int PROGRESS_DURTION=60;
    private ImageView mGiftImageView;
    private TextView mViewCount;
    private FrameLayout mProgressView;
    private TextView mViewTvMonery;

    public CountdownGiftView(@NonNull Context context) {
        this(context,null);
    }

    public CountdownGiftView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CountdownGiftView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    /**
     * 初始化
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.view_countdown_gift_layout,this);
        if(null!=attrs){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountdownGiftView);
            PROGRESS_DURTION = typedArray.getInteger(R.styleable.CountdownGiftView_count_durtion, 60);
            typedArray.recycle();
        }
        mProgressbar = findViewById(R.id.view_circle_progress);
        mProgressView = findViewById(R.id.view_progress_group);
        mGiftImageView = findViewById(R.id.view_circle_gift_icon);
        mViewCount = findViewById(R.id.view_circle_count);
        mViewTvMonery = (TextView) findViewById(R.id.view_tv_monery);
        int screenDensity = AppUtils.getInstance().getScreenDensity();
        if(screenDensity<=300){
            mProgressbar.setProgressLineWidth(6);//进度条宽度
        }else{
            mProgressbar.setProgressLineWidth(12);//进度条宽度
        }
        mProgressbar.setTimeMillis(1000*PROGRESS_DURTION);//一分钟的连击
        mProgressbar.setProgressColor(Color.parseColor("#FB6665"));//进度条颜色
        //监听进度
        mProgressbar.setCountdownProgressListener(0, new CircleTextProgressbar.OnCountdownProgressListener() {
            @Override
            public void onProgress(int what, int progress) {
                if(0==progress){
                    onReset();
                }
            }
        });
        //连击监听
        mGiftImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=mProgressbar){
                    mProgressbar.setTimeMillis(1000*PROGRESS_DURTION);//一分钟的连击
                    mProgressbar.reStart();
                }
                GiftBoardManager.getInstance().sendGift();
            }
        });
        setMoney(10000000);
    }

    /**
     * 更新余额显示
     * @param diamonds
     */
    private void setMoney(long diamonds) {
        if(null!=mViewTvMonery) mViewTvMonery.setText(AppUtils.getInstance().formatWan(diamonds,true));
    }

    /**
     * 更新礼物对象
     * @param giftInfo 礼物信息
     */
    public void updataView(GiftItemInfo giftInfo){
        if(null==giftInfo) return;
        if(null!=getTag()&& ((int) getTag())==giftInfo.getId()){
            if(null!=mProgressView) mProgressView.setVisibility(VISIBLE);
            if(null!=mViewTvMonery) mViewTvMonery.setVisibility(VISIBLE);
            return;
        }
        setTag(giftInfo.getId());
        if(null!=mProgressView) mProgressView.setVisibility(VISIBLE);
        if(null!=mViewTvMonery) mViewTvMonery.setVisibility(VISIBLE);
        setCount(giftInfo.getCount());
        if(null!=mGiftImageView){
            Glide.with(getContext())
                    .load(giftInfo.getSrc())
                    .placeholder(R.drawable.ic_default_gift_icon)
                    .error(R.drawable.ic_default_gift_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()//中心点缩放
                    .dontAnimate()
                    .transform(new GlideCircleTransform(getContext()))
                    .into(mGiftImageView);
        }
    }

    /**
     * 更新选中数量
     * @param count
     */
    public void setCount(int count){
        SpannableStringBuilder stringBuilder = SpannableStringUtils.getInstance().giftSendNumFromat(String.valueOf(count));
        mViewCount.setText(stringBuilder);
        recoverTime();
    }

    /**
     * 还原到重新开始倒计时
     */
    public void recoverTime(){
        if(null!=mProgressbar){
            mProgressbar.setTimeMillis(1000*PROGRESS_DURTION);
            mProgressbar.reStart();//重新开始
        }
    }

    /**
     * 还原
     */
    public void onReset() {
        setTag(0);
        if(null!=mProgressView) mProgressView.setVisibility(INVISIBLE);
        if(null!=mViewTvMonery) mViewTvMonery.setVisibility(INVISIBLE);
        if(null!=mProgressbar) mProgressbar.stop();
        if(null!=mViewCount) mViewCount.setText("");
    }

    /**
     * 对应方法调用
     */
    public void onDestroy(){
        onReset();
    }
}