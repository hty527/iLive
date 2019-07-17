package com.android.live.player.lib.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.live.player.lib.R;
import com.android.live.player.lib.controller.PlayerVideoController;
import com.android.live.player.lib.listener.OnVideoTouchListener;
import com.android.live.player.lib.listener.VideoPlayerListener;
import com.android.live.player.lib.listener.VideoPlayerEventListener;
import com.android.live.player.lib.manager.VideoPlayerManager;
import com.android.live.player.lib.model.VideoPlayerState;
import com.android.live.player.lib.utils.Logger;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.ui.TXCloudVideoView;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * TinyHung@Outlook.com
 * 2019/4/20
 * Base from Tencent SDK to All Video\Live Player as Base
 * https://cloud.tencent.com/document/product/454/7877
 * 动态加载SO 库 TXLiveBase 的 setLibraryPath 将下载的目标 path 设置给 SDK
 */

public abstract class BaseVideoPlayer<V extends BaseVideoController> extends FrameLayout implements VideoPlayerEventListener {

    private static final String TAG = "BaseVideoPlayer";
    private OnVideoTouchListener mTouchLostener;
    private V mVideoController;
    private String mDataSource;
    private TXCloudVideoView mTextureView;
    private VideoPlayerListener mListener;
    public static final int VIDEO_SCALING_MODE_FULL_SCREEN  = 0; //裁剪至铺满全屏
    public static final int VIDEO_SCALING_MODE_ADJUST     = 1; //等比例最长边适配屏幕，居中显示
    //默认缩放模式
    private int SCREEN_MODE=VIDEO_SCALING_MODE_FULL_SCREEN;
    //屏幕方向角度，默认是竖屏的
    private int mOrientationAngle = TXLiveConstants.RENDER_ROTATION_PORTRAIT;
    public ImageView mVideoCover;
    //是否循环
    private boolean mLoop;
    //是否静音
    private boolean mSoundMute=false;


    public BaseVideoPlayer(@NonNull Context context) {
        this(context,null);
    }

    public BaseVideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    protected abstract int getLayoutID();

    public BaseVideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, getLayoutID(),this);
        mVideoCover = (ImageView) findViewById(R.id.view_video_cover);
        if(null!=attrs){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseVideoPlayer);
            if(null!=mVideoCover){
                Drawable backDrawable = typedArray.getDrawable(R.styleable.BaseVideoPlayer_videoPlayerBackgroundDrawable);
                if(null!=backDrawable){
                    mVideoCover.setBackground(backDrawable);
                }else{
                    int backColor = typedArray.getColor(R.styleable.BaseVideoPlayer_videoPlayerBackgroundColor, Color.parseColor("#000000"));
                    mVideoCover.setBackgroundColor(backColor);
                }
            }
            //是否创建默认的控制器
            boolean createController = typedArray.getBoolean(R.styleable.BaseVideoPlayer_videoPlayerCreateDefaultController, false);
            if(createController){
                setVideoController(null,true);
            }
            typedArray.recycle();
        }
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=mVideoController){
                    mVideoController.changeControllerState();
                }
            }
        });
        mTextureView = (TXCloudVideoView) findViewById(R.id.view_textureview);
    }

    /**
     * 设置视频封面
     * @param frontCover 视频封面地址
     * @param transformat 是否毛玻璃显示
     */
    public void setVideoCover(String frontCover,boolean transformat) {
        Logger.d(TAG,"setVideoCover-->frontCover:"+frontCover);
        if(TextUtils.isEmpty(frontCover)){
            if(null!=mVideoCover) mVideoCover.setImageResource(0);
            return;
        }
        if(null!=mVideoCover){
            if(transformat){
                Glide.with(getContext())
                        .load(frontCover)
                        .dontAnimate()
                        .placeholder(R.drawable.bg_live_transit)
                        .error(R.drawable.bg_live_transit)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .bitmapTransform(new BlurTransformation(getContext(), 15))
                        .into(mVideoCover);
            }else{
                Glide.with(getContext())
                        .load(frontCover)
                        .dontAnimate()
                        .placeholder(R.drawable.bg_live_transit)
                        .error(R.drawable.bg_live_transit)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(mVideoCover);
            }
        }
    }

    /**
     * 设置播放器控制器
     * @param videoController 继承VideoBaseController的控制器
     * @param isCreateDefaultController 当 videoController 为空，是否自动创建默认的控制器
     */
    public void setVideoController(V videoController,boolean isCreateDefaultController) {
        FrameLayout controllerView = (FrameLayout) findViewById(R.id.view_controller);
        if(null!=controllerView){
            removeGroupView(mVideoController);
            if(controllerView.getChildCount()>0){
                controllerView.removeAllViews();
            }
            if(null!= mVideoController){
                mVideoController.onDestroy();
                mVideoController =null;
            }
            if(null!=videoController){
                BaseVideoPlayer.this.mVideoController =videoController;
            }else{
                if(isCreateDefaultController){
                    mVideoController = (V) new PlayerVideoController(getContext());
                }
            }
            if(null!= mVideoController){
                mVideoController.setOnStartListener(new BaseVideoController.OnStartListener() {
                    @Override
                    public void onStartPlay() {
                        startPlayer();
                    }
                });
                controllerView.addView(mVideoController,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
            }
        }
    }

    /**
     * 返回播放器控制器
     * @return
     */
    public V getVideoController() {
        return mVideoController;
    }

    /**
     * 将自己从父Parent中移除
     * @param viewGroup
     */
    private void removeGroupView(ViewGroup viewGroup) {
        if(null!=viewGroup&&null!=viewGroup.getParent()){
            ViewGroup parent = (ViewGroup) viewGroup.getParent();
            parent.removeView(viewGroup);
        }
    }

    public void setLoop(boolean loop) {
        this.mLoop=loop;
    }

    /**
     * 设置缩放模式
     * @param screenMode 见顶部常量定义
     */
    public void setScreenMode(int screenMode){
        this.SCREEN_MODE=screenMode;
    }

    /**
     * 设置播放器方向角度
     * @param orientationAngle TXLiveConstants.RENDER_ROTATION_PORTRAI\TXLiveConstants.RENDER_ROTATION_LANDSCAPE
     */
    public void setOrientationAngle(int orientationAngle){
        this.mOrientationAngle=orientationAngle;
    }

    /**
     * 静音设置
     * @param mute
     */
    public void setMuteMode(boolean mute) {
        this.mSoundMute=mute;
    }

    /**
     * 设置播放状态监听
     * @param listener
     */
    public void setMediaPlayerListener(VideoPlayerListener listener) {
        mListener = listener;
    }

    /**
     * 设置播放地址
     * @param dataSource
     */
    public void setDataSource(String dataSource){
        this.mDataSource=dataSource;
    }

    /**
     * 业务需要的，假装正在缓冲中状态
     */
    public void startPrepareState(){
        if(null!= mVideoController){
            mVideoController.startBuffer();
        }
    }

    /**
     * 开始播放，此方法是用户主动触发播放事件的，前提是播放器已经准备好了
     */
    public void startPlayer() {
        startPlayer(mDataSource);
    }

    /**
     * 开始播放
     * @param dataSource
     */
    public void startPlayer(String dataSource) {
        //还原上个播放任务，如果存在的话
        VideoPlayerManager.getInstance().reset();
        this.mDataSource=dataSource;
        if(null!=mTextureView&&!TextUtils.isEmpty(mDataSource)){
            //设置缩放类型
            VideoPlayerManager.getInstance().setVideoScreenMode(SCREEN_MODE);
            //设置旋转角度
            VideoPlayerManager.getInstance().setVideoOrientationAngle(mOrientationAngle);
            //是否静音
            VideoPlayerManager.getInstance().setVideoMute(mSoundMute);
            //是否循环
            VideoPlayerManager.getInstance().setVideoLoop(mLoop);
            //添加监听器
            VideoPlayerManager.getInstance().addOnPlayerEventListener(this);
            //开始播放
            VideoPlayerManager.getInstance().startPlayer(getContext().getApplicationContext(),mTextureView,mDataSource);
        }
    }

    /**
     * 开始重新播放
     */
    public void onStart(){
        startPlayer();
    }

    /**
     * 结束播放
     */
    public void onStop(){
        //结束播放
        VideoPlayerManager.getInstance().stop();
    }

    /**
     * 尝试恢复播放
     */
    public void onResume(){
        VideoPlayerManager.getInstance().onResume();
    }

    /**
     * 尝试暂停播放
     */
    public void onPause(){
        VideoPlayerManager.getInstance().onPause();
    }

    /**
     * 还原播放器及controller内部所有状态
     */
    public void onReset(){
        VideoPlayerManager.getInstance().reset();
    }

    /**
     * 销毁播放器及controller
     */
    public void onDestroy(){
        if(null!=mTextureView){
            mTextureView.removeVideoView();
            mTextureView.onDestroy();
            mTextureView=null;
        }
        mDataSource=null;
        if(null!= mVideoController){
            mVideoController.onDestroy();
            mVideoController =null;
        }
    }

    /**
     * 播放器播放状态
     * @param playerState 播放器内部状态
     * @param message
     */
    @Override
    public void onVideoPlayerState(final VideoPlayerState playerState, final String message) {
        Logger.d(TAG,"onVideoPlayerState-->playerState:"+playerState);
        if(playerState.equals(VideoPlayerState.MUSIC_PLAYER_ERROR)&&!TextUtils.isEmpty(message)){
            Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
        }
        if(null!=mVideoController){
            BaseVideoPlayer.this.post(new Runnable() {
                @Override
                public void run() {
                    switch (playerState) {
                        //播放器准备中
                        case MUSIC_PLAYER_PREPARE:
                            if(null!=mVideoController){
                                mVideoController.readyPlaying();
                            }
                            break;
                        //播放过程缓冲中
                        case MUSIC_PLAYER_BUFFER:
                            if(null!=mVideoController){
                                mVideoController.startBuffer();
                            }
                            break;
                        //缓冲结束、准备结束 后的开始播放
                        case MUSIC_PLAYER_START:
                            if(null!=mVideoController){
                                mVideoController.play();
                            }
                            if(null!=mListener){
                                mListener.onStart();
                            }
                            break;
                        //恢复播放
                        case MUSIC_PLAYER_PLAY:
                            if(null!=mVideoController){
                                mVideoController.repeatPlay();
                            }
                            if(null!=mListener){
                                mListener.onStart();
                            }
                            break;
                        //移动网络环境下播放
                        case MUSIC_PLAYER_MOBILE:
                            if(null!=mVideoController){
                                mVideoController.mobileWorkTips();
                            }
                            break;
                        //暂停
                        case MUSIC_PLAYER_PAUSE:
                            if(null!=mVideoController){
                                mVideoController.pause();
                            }
                            break;
                        //停止
                        case MUSIC_PLAYER_STOP:
                            if(null!=mVideoController){
                                mVideoController.reset();
                            }
                            break;
                        //正常的播放器结束
                        case MUSIC_PLAYER_COMPLETION:
                            if(null!=mVideoController){
                                mVideoController.reset();
                            }
                            if(null!=mListener){
                                mListener.onCompletion();
                            }
                            break;
                        //失败
                        case MUSIC_PLAYER_ERROR:
                            if(null!=mVideoController){
                                mVideoController.error(0,message);
                            }
                            if(null!=mListener){
                                mListener.onError(1000);
                            }
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onPrepared(long totalDurtion) {}

    /**
     * 百分比
     * @param percent 百分比
     */
    @Override
    public void onBufferingUpdate(final int percent) {
        if(null!=mVideoController){
            mVideoController.post(new Runnable() {
                @Override
                public void run() {
                    if(null!=mVideoController){
                        mVideoController.onBufferingUpdate(percent);
                    }
                }
            });
        }
        if(null!=mListener){
            mListener.onBufferingUpdate(percent);
        }
    }

    @Override
    public void onInfo(int event, int extra) {}

    /**
     * 播放地址无效
     */
    @Override
    public void onVideoPathInvalid() {
        if(null!=mVideoController){
            mVideoController.pathInvalid();
        }
    }

    /**
     * 播放实时状态
     * @param totalDurtion 音频总时间
     * @param currentDurtion 当前播放的位置
     * @param bufferPercent 缓冲进度，从常规默认切换至全屏、小窗时，应该关心此进度
     * @param isPlaying 部分视频格式的视频播放当缓冲结束后的状态无法准确获取，此标记标识是否是正在播放
     */
    @Override
    public void onTaskRuntime(final long totalDurtion, final long currentDurtion, final int bufferPercent, final boolean isPlaying) {
        if(null!=mVideoController){
            mVideoController.post(new Runnable() {
                @Override
                public void run() {
                    if(null!=mVideoController){
                        mVideoController.onTaskRuntime(totalDurtion,currentDurtion,bufferPercent,isPlaying);
                        if(isPlaying){
                            mVideoController.endBuffer();
                        }
                    }
                }
            });
        }
        if(null!=mListener){
            mListener.onPlayingProgress(currentDurtion,totalDurtion);
            mListener.onBufferingUpdate(bufferPercent);
        }
    }

    @Override
    public void destroy() {

    }
}