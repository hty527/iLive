package com.android.live.player.lib.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.live.player.lib.R;
import com.android.live.player.lib.base.BaseVideoController;
import com.android.live.player.lib.manager.VideoPlayerManager;
import com.android.live.player.lib.utils.Logger;

/**
 * Created by TinyHung@outlook.com
 * 2019/4/20
 * Default Video\Live Controller
 */

public class PlayerVideoController extends BaseVideoController {

    private View mBtnStart,mMobileLayout;
    private ProgressBar mLoadProgressBar;
    private TextView mErrorText;

    public PlayerVideoController(@NonNull Context context) {
        this(context,null);
    }

    public PlayerVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PlayerVideoController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.video_controller_layout,this);
        mLoadProgressBar = (ProgressBar) findViewById(R.id.video_loading);
        mBtnStart = findViewById(R.id.video_start);
        mErrorText = findViewById(R.id.video_error);
        mMobileLayout = findViewById(R.id.mobile_layout);
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = v.getId();
                if (i == R.id.video_start || i == R.id.video_error) {
                    if (null != mOnStartListener) {
                        mOnStartListener.onStartPlay();
                    }

                } else if (i == R.id.video_btn_reset_play) {
                    if (null != mOnStartListener) {
                        //允许4G工作
                        VideoPlayerManager.getInstance().setMobileWorkEnable(true);
                        mOnStartListener.onStartPlay();
                    }

                }
            }
        };
        mBtnStart.setOnClickListener(onClickListener);
        mErrorText.setOnClickListener(onClickListener);
        findViewById(R.id.video_btn_reset_play).setOnClickListener(onClickListener);
        //默认停止状态
        changedUIState(View.INVISIBLE,View.VISIBLE,View.INVISIBLE,View.INVISIBLE);
    }

    /**
     * 改变控制器UI状态
     * @param loading 加载中
     * @param start 开始按钮
     * @param error 播放失败了
     * @param mobileLayout 移动网络提示
     */
    private void changedUIState(int loading, int start,int error,int mobileLayout) {
        if(null!=mLoadProgressBar){
            mLoadProgressBar.setVisibility(loading);
        }
        if(null!=mBtnStart){
            mBtnStart.setVisibility(start);
        }
        if(null!=mErrorText){
            mErrorText.setVisibility(error);
        }
        if(null!=mMobileLayout){
            mMobileLayout.setVisibility(mobileLayout);
        }
    }

    @Override
    public void readyPlaying() {
        changedUIState(View.VISIBLE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE);
    }

    @Override
    public void startBuffer() {
        Logger.d(TAG,"startBuffer");
        changedUIState(View.VISIBLE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE);
    }

    @Override
    public void endBuffer() {
        Logger.d(TAG,"endBuffer");
        //此方法会频繁调用，只在缓冲状态无法获取的情况下，并且当前缓冲按钮处于可见，才会刷新UI
        if(null!=mLoadProgressBar&&mLoadProgressBar.getVisibility()==View.VISIBLE){
            changedUIState(View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE);
        }
    }

    @Override
    public void play() {
        Logger.d(TAG,"play");
        changedUIState(View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE);
    }

    @Override
    public void pause() {
        Logger.d(TAG,"pause");
        changedUIState(View.INVISIBLE,View.VISIBLE,View.INVISIBLE,View.INVISIBLE);
    }

    @Override
    public void repeatPlay() {
        Logger.d(TAG,"repeatPlay");
        changedUIState(View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE);
    }

    @Override
    public void mobileWorkTips() {
        Logger.d(TAG,"mobileWorkTips");
        changedUIState(View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,View.VISIBLE);
    }

    @Override
    public void error(int errorCode, String errorMessage) {
        Logger.d(TAG,"error,errorCode:"+errorCode+",errorMessage:"+errorMessage);
        if(null!=mErrorText){
            mErrorText.setText("加载失败，点击重试");
        }
        changedUIState(View.INVISIBLE,View.VISIBLE,View.VISIBLE,View.INVISIBLE);
    }

    @Override
    public void reset() {
        Logger.d(TAG,"reset");
        changedUIState(View.INVISIBLE,View.VISIBLE,View.INVISIBLE,View.INVISIBLE);
    }

    @Override
    public void pathInvalid() {
        Logger.d(TAG,"pathInvalid");
    }

    @Override
    public void onDestroy() {
        changedUIState(View.INVISIBLE,View.VISIBLE,View.INVISIBLE,View.INVISIBLE);
        mBtnStart=null;mLoadProgressBar=null;
    }
}