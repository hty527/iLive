package com.android.live.player.lib.manager;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.TextUtils;
import com.android.live.player.lib.base.BaseVideoPlayer;
import com.android.live.player.lib.listener.VideoPlayerEventListener;
import com.android.live.player.lib.model.VideoPlayerState;
import com.android.live.player.lib.utils.Logger;
import com.android.live.player.lib.utils.VideoUtils;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * hty_Yuye@Outlook.com
 * 2019/4/20
 * Video Player Manager
 */

public class VideoPlayerManager {

    private static final String TAG = "VideoPlayerManager";
    private static VideoPlayerManager mInstance;
    private Context mContext;
    //播放器相关
    private TXVodPlayer mVideoPlayer;
    private TXCloudVideoView mVideoView;
    //音频焦点
    private static VideoAudioFocusManager mAudioFocusManager;
    //是否循环播放
    private boolean mLoop;
    //移动网络下是否允许工作
    private boolean mMobileWorkenableEnable;
    //内部播放状态
    private static VideoPlayerState mVideoPlayerState = VideoPlayerState.MUSIC_PLAYER_STOP;
    //监听器
    private VideoPlayerEventListener mListener;
    //播放地址
    private String mDataSource;
    //缓冲进度
    private int mDurationMS,mProgressMS,mBufferPercent;
    //缩放模式
    private int mScreenMode = BaseVideoPlayer.VIDEO_SCALING_MODE_FULL_SCREEN;
    //屏幕方向角度，默认是竖屏的
    private int mOrientationAngle = TXLiveConstants.RENDER_ROTATION_PORTRAIT;
    private boolean mMute;
    //EVENT事件运行时标记
    private long RUN_EVENT =0;

    public static synchronized VideoPlayerManager getInstance() {
        synchronized (VideoPlayerManager.class) {
            if (null == mInstance) {
                mInstance = new VideoPlayerManager();
            }
        }
        return mInstance;
    }

    /**
     * 获取播放器对象
     * @param context
     * @return
     */
    public TXVodPlayer getMediaPlayer(Context context){
        if(null==mVideoPlayer){
            mVideoPlayer = new TXVodPlayer(context);
            mVideoPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);//保证全屏缩放显示
            mVideoPlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
            TXVodPlayConfig playConfig = new TXVodPlayConfig();
            playConfig.setMaxBufferSize(1000);
            playConfig.setTimeout(20);
            mVideoPlayer.setConfig(playConfig);
            mVideoPlayer.setVodListener(new ITXVodPlayListener() {
                @Override
                public void onPlayEvent(TXVodPlayer txVodPlayer, int event, Bundle param) {
                    Logger.d(TAG,"PLAY_EVENT:"+event);
                    switch (event) {
                        //缓冲开始
                        case TXLiveConstants.PLAY_EVT_PLAY_LOADING:
                            Logger.d(TAG,"点播缓冲开始");
                            VideoPlayerManager.this.mVideoPlayerState=VideoPlayerState.MUSIC_PLAYER_BUFFER;
                            if(null!=mListener){
                                mListener.onVideoPlayerState(mVideoPlayerState,null);
                            }
                            return;
                        //缓冲结束
                        case TXLiveConstants.PLAY_EVT_VOD_LOADING_END:
                            Logger.d(TAG,"LoadingEnd");
                            break;
                        //准备完成
                        case 3000:
                        case TXLiveConstants.PLAY_EVT_VOD_PLAY_PREPARED:
                            Logger.d(TAG,"准备完成");
                            VideoPlayerManager.this.mVideoPlayerState=VideoPlayerState.MUSIC_PLAYER_START;
                            if(null!=mListener){
                                mListener.onVideoPlayerState(mVideoPlayerState,null);
                            }
                            return;
                        //渲染首帧
                        case TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME:
                            Logger.d(TAG,"点播渲染首帧");
//                            VideoPlayerManager.this.mVideoPlayerState=VideoPlayerState.MUSIC_PLAYER_START;
//                            if(null!=mListener){
//                                mListener.onVideoPlayerState(mVideoPlayerState,null);
//                            }
                        return;
                        //播放开始、结束缓冲
                        case TXLiveConstants.PLAY_EVT_PLAY_BEGIN:
                            Logger.d(TAG,"开始播放");
                            VideoPlayerManager.this.mVideoPlayerState=VideoPlayerState.MUSIC_PLAYER_PLAY;
                            if(null!=mListener){
                                mListener.onVideoPlayerState(mVideoPlayerState,null);
                            }
                            mDurationMS=0;mProgressMS=0;
                            return;
                        //播放结束
                        case TXLiveConstants.PLAY_EVT_PLAY_END:
                            Logger.d(TAG,"点播播放结束："+mLoop);
                            if(null!=mVideoPlayer){
                                mVideoPlayer.setRenderMode(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
                            }
                            if(mLoop){
                                startPlayer();
                            }else{
                                VideoPlayerManager.this.mVideoPlayerState=VideoPlayerState.MUSIC_PLAYER_COMPLETION;
                                if(null!=mListener){
                                    mListener.onVideoPlayerState(mVideoPlayerState,null);
                                }
                            }
                            return;
                        //播放失败
                        case TXLiveConstants.PLAY_ERR_NET_DISCONNECT:
                        case TXLiveConstants.PLAY_WARNING_RECONNECT:
                            Logger.d(TAG,"点播播放播放失败");
                            if(null!=mVideoPlayer){
                                mVideoPlayer.setRenderMode(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
                            }
                            VideoPlayerManager.this.mVideoPlayerState=VideoPlayerState.MUSIC_PLAYER_ERROR;
                            if(null!=mListener){
                                mListener.onVideoPlayerState(mVideoPlayerState,null);
                            }
                            mDurationMS=0;mProgressMS=0;
                            return;
                        //播放进度
                        case TXLiveConstants.PLAY_EVT_PLAY_PROGRESS:
                            //已播放时长
                            mProgressMS = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS_MS);
                            //总时长
                            mDurationMS = param.getInt(TXLiveConstants.EVT_PLAY_DURATION_MS);
                            //已缓冲时长
                            int bufferMs = param.getInt(TXLiveConstants.EVT_PLAYABLE_DURATION_MS);
                            int progress=bufferMs*100/ mDurationMS;
                            mBufferPercent=progress;
//                            Logger.d(TAG,"onPlayEvent-->:\n"+
//                                    "durationMS:"+mDurationMS+"\n"+
//                                    "progressMS:"+mProgressMS+"\n"+
//                                    "mBufferPercent:"+mBufferPercent+"\n"+
//                                    "PARAMS:"+param.toString());
                            if(null!=mListener){
                                if(null!=mVideoPlayer){
                                    //当缓冲结束后的状态获取不到，走这里
                                    if(0 == RUN_EVENT % 8){
                                        //没调用四次（即2秒）后置缓冲状态为完成
                                        mListener.onTaskRuntime(mDurationMS, mProgressMS,mBufferPercent,mVideoPlayer.isPlaying());
                                    }else{
                                        mListener.onTaskRuntime(mDurationMS, mProgressMS,mBufferPercent,false);
                                    }
                                }else{
                                    mListener.onTaskRuntime(mDurationMS, mProgressMS,mBufferPercent,false);
                                }
                            }
                            RUN_EVENT++;
                            return;
                    }
                }

                @Override
                public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {
//                    if(null!=mVideoPlayer){
//                        if(bundle.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) > bundle.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT)) {
//                            mVideoPlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_LANDSCAPE);
//                        } else  {
//                            mVideoPlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
//                        }
//                    }
                }
            });
        }
        return mVideoPlayer;
    }

    /**
     * 设置循环模式
     * @param loop
     */
    public void setLoop(boolean loop){
        this.mLoop=loop;
    }

    /**
     * 移动网络工作开关
     * @param enable true：允许移动网络工作 false：不允许
     */
    public void setMobileWorkEnable(boolean enable){
        this.mMobileWorkenableEnable=enable;
    }

    /**
     * 是否允许移动网络环境下工作
     * @return
     */
    public boolean isMobileWorkEnable() {
        return mMobileWorkenableEnable;
    }

    /**
     * 设置播放地址
     * @param dataSource
     */
    public void setDataSource(String dataSource){
        this.mDataSource=dataSource;
    }

    /**
     * 设置静音,播放过程中声音状态时调用此方法，视频源切换会被BaseVideoPlayer组件单独设置的属性覆盖
     * @param mute
     */
    public void setMute(boolean mute) {
        mMute = mute;
        if(null!=mVideoPlayer){
            mVideoPlayer.setMute(mMute);
        }
    }

    /**
     * 设置缩放模式,播放过程中改变缩放模式时调用此方法，视频源切换会被BaseVideoPlayer组件单独设置的属性覆盖
     * 适合非BaseVideoPlayer或继承自BaseVideoPlayer的类调用
     * @param screenMode
     */
    public void setScreenMode(int screenMode) {
        mScreenMode = screenMode;
        if(null!=mVideoPlayer){
            mVideoPlayer.setRenderMode(getRendMode());
        }
    }

    /**
     * 设置播放器方向角度,播放过程中改变播放器方向角度时调用此方法，视频源切换会被BaseVideoPlayer组件单独设置的属性覆盖
     * 适合非BaseVideoPlayer或继承自BaseVideoPlayer的类调用
     * @param orientationAngle TXLiveConstants.RENDER_ROTATION_PORTRAI\TXLiveConstants.RENDER_ROTATION_LANDSCAPE
     */
    public void setOrientationAngle(int orientationAngle){
        this.mOrientationAngle=orientationAngle;
        if(null!=mVideoPlayer){
            mVideoPlayer.setRenderRotation(mOrientationAngle);
        }
    }

    /**
     * 设置循环模式，交由BaseVideoPlayer属性覆盖此设置
     * @param loop
     */
    public void setVideoLoop(boolean loop){
        this.mLoop=loop;
    }

   /**
     * 设置静音，交由BaseVideoPlayer属性覆盖此设置
     * @param mute
     */
    public void setVideoMute(boolean mute) {
        mMute = mute;
    }



    /**
     * 设置缩放模式，交由BaseVideoPlayer属性覆盖此设置
     * @param screenMode
     */
    public void setVideoScreenMode(int screenMode) {
        mScreenMode = screenMode;
    }

    /**
     * 设置播放器方向角度,初始化设置时调用此方法，会覆盖setOrientationAngle,
     * @param orientationAngle TXLiveConstants.RENDER_ROTATION_PORTRAI\TXLiveConstants.RENDER_ROTATION_LANDSCAPE
     */
    public void setVideoOrientationAngle(int orientationAngle){
        this.mOrientationAngle=orientationAngle;
    }

    /**
     * 内部的播放方法，这里是重复播放
     */
    private void startPlayer() {
        startPlayer(null,null,null);
    }
    /**
     * 开始播放
     * @param txCloudVideoView 渲染组建
     * @param dataSource 播放源
     */
    public void startPlayer(TXCloudVideoView txCloudVideoView, String dataSource) {
        startPlayer(mContext,txCloudVideoView,dataSource);
    }

    /**
     * 开始播放
     * @param context 全局上下文
     * @param txCloudVideoView 渲染组建
     * @param dataSource 播放源
     */
    public void startPlayer(Context context, TXCloudVideoView txCloudVideoView, String dataSource) {
        //重复播放
        if(null==context&&null==txCloudVideoView&&null==dataSource&&null!=mVideoPlayer){
            VideoPlayerManager.this.mVideoPlayerState = VideoPlayerState.MUSIC_PLAYER_PREPARE;
            if (null != mListener) {
                mListener.onVideoPlayerState(mVideoPlayerState,"播放准备中");
            }
            mVideoPlayer.startPlay(mDataSource);
            return;
        }
        //全新播放任务先还原一把
        reset();
        //全新播放
        if(null!=context&&null!=txCloudVideoView&&!TextUtils.isEmpty(dataSource)){
            this.mDataSource=dataSource;
            this.mContext=context;
            this.mVideoView=txCloudVideoView;
            if(null==mAudioFocusManager){
                mAudioFocusManager= new VideoAudioFocusManager(context);
            }
            //如果联网了
            if(VideoUtils.getInstance().isCheckNetwork(mContext)){
                //如果不允许移动网络环境下工作
                if(!VideoUtils.getInstance().isWifiConnected(mContext)&&!isMobileWorkEnable()){
                    VideoPlayerManager.this.mVideoPlayerState=VideoPlayerState.MUSIC_PLAYER_MOBILE;
                    if(null!=mListener){
                        mListener.onVideoPlayerState(mVideoPlayerState,"正在使用移动网络");
                    }
                    return;
                }
                int requestAudioFocus = mAudioFocusManager.requestAudioFocus(new VideoAudioFocusManager.OnAudioFocusListener() {
                    @Override
                    public void onStart() {
                        play();
                    }

                    @Override
                    public void onStop() {
//                    VideoPlayerManager.this.stop();
                    }

                    @Override
                    public boolean isPlaying() {
                        return VideoPlayerManager.this.isPlaying();
                    }
                });

                if(requestAudioFocus== AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    VideoPlayerManager.this.mVideoPlayerState = VideoPlayerState.MUSIC_PLAYER_PREPARE;
                    this.mDataSource = dataSource;
                    getMediaPlayer(context);
                    mVideoPlayer.setPlayerView(mVideoView);
                    mVideoPlayer.setRenderMode(getRendMode());
                    mVideoPlayer.setRenderRotation(mOrientationAngle);
                    if (null != mListener) {
                        mListener.onVideoPlayerState(mVideoPlayerState,"播放准备中");
                    }
                    mVideoPlayer.startPlay(mDataSource);
                }else{
                    VideoPlayerManager.this.mVideoPlayerState = VideoPlayerState.MUSIC_PLAYER_ERROR;
                    if (null != mListener) {
                        mListener.onVideoPlayerState(mVideoPlayerState,"未成功获取音频输出焦点");
                    }
                }
            }else{
                VideoPlayerManager.this.mVideoPlayerState=VideoPlayerState.MUSIC_PLAYER_STOP;
                if(null!=mListener){
                    mListener.onVideoPlayerState(mVideoPlayerState,"网络未连接");
                }
            }
        }else{
            if(null!=mListener){
                VideoPlayerManager.this.mVideoPlayerState=VideoPlayerState.MUSIC_PLAYER_ERROR;
                mListener.onVideoPlayerState(mVideoPlayerState,"播放失败，必要组件未初始化");
            }
        }
    }

    /**
     * 转换缩放模式
     * @return
     */
    private int getRendMode() {
        switch (mScreenMode) {
            case BaseVideoPlayer.VIDEO_SCALING_MODE_FULL_SCREEN:
                return TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN;
            case BaseVideoPlayer.VIDEO_SCALING_MODE_ADJUST:
                return TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;
        }
        return TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN;
    }

    /**
     * 添加监听器
     * @param listener
     */
    public void addOnPlayerEventListener(VideoPlayerEventListener listener){
        this.mListener=listener;
    }

    /**
     * 移除监听器
     */
    public void removePlayerListener(){
        mListener=null;
    }

    /**
     * 返回播放器内部播放状态
     * @return true：正在播放，fasle：未播放
     */
    public boolean isPlaying(){
        try {
            return null!=mVideoPlayer&&(mVideoPlayerState.equals(VideoPlayerState.MUSIC_PLAYER_PREPARE)
                    || mVideoPlayerState.equals(VideoPlayerState.MUSIC_PLAYER_START)
                    || mVideoPlayerState.equals(VideoPlayerState.MUSIC_PLAYER_PLAY)
                    || mVideoPlayerState.equals(VideoPlayerState.MUSIC_PLAYER_BUFFER));
        }catch (RuntimeException e){

        }
        return false;
    }

    /**
     * 开始、暂停播放
     */
    public void playOrPause(){
        if(null!=mContext&&!TextUtils.isEmpty(mDataSource)&&null!=mVideoView){
            switch (getVideoPlayerState()) {
                case MUSIC_PLAYER_STOP:
                    startPlayer(mContext,mVideoView,mDataSource);
                    break;
                case MUSIC_PLAYER_PREPARE:
                    pause();
                    break;
                case MUSIC_PLAYER_BUFFER:
                    pause();
                    break;
                case MUSIC_PLAYER_START:
                    pause();
                    break;
                case MUSIC_PLAYER_PLAY:
                    pause();
                    break;
                case MUSIC_PLAYER_PAUSE:
                    if(null!=mAudioFocusManager){
                        mAudioFocusManager.requestAudioFocus(null);
                    }
                    play();
                    break;
            }
        }
    }

    /**
     * 恢复播放
     */
    public void play(){
        if(null!=mVideoPlayer){
            mVideoPlayer.resume();
            VideoPlayerManager.mVideoPlayerState=VideoPlayerState.MUSIC_PLAYER_PLAY;
            if(null!=mListener){
                mListener.onVideoPlayerState(mVideoPlayerState,null);
            }
        }
    }

    /**
     * 暂停播放
     */
    public void pause(){
        if(null!=mVideoPlayer){
            mVideoPlayer.pause();
            VideoPlayerManager.mVideoPlayerState=VideoPlayerState.MUSIC_PLAYER_PAUSE;
            if(null!=mListener){
                mListener.onVideoPlayerState(mVideoPlayerState,null);
            }
        }
    }

    /**
     * 释放、还原播放、监听、渲染等状态
     */
    public void reset(){
        if(null!=mVideoPlayer){
            mVideoPlayer.stopPlay(true);
        }
        RUN_EVENT=0;
        if(null!=mVideoView){
            mVideoView.removeVideoView();
        }
        VideoPlayerManager.mVideoPlayerState=VideoPlayerState.MUSIC_PLAYER_STOP;
        if(null!=mListener){
            mListener.onVideoPlayerState(mVideoPlayerState,null);
        }
        mBufferPercent=0;
        if(null!=mAudioFocusManager){
            mAudioFocusManager.releaseAudioFocus();
        }
    }

    /**
     * 停止播放
     */
    public void stop(){
        reset();
        mListener=null;
    }

    /**
     * 跳转至指定位置播放
     * @param currentTime 事件位置，单位毫秒
     */
    public void seekTo(long currentTime){
        if(null!=mVideoPlayer){
            mVideoPlayer.seek(currentTime);
        }
    }

    /**
     * 返回当前播放对象的总时长
     * @return
     */
    public long getDurtion(){
        return mDurationMS;
    }

    /**
     * 返回当前已播放的时长
     * @return
     */
    public long getCurrentDurtion(){
        return mProgressMS;
    }

    /**
     * 返回内部播放器播放状态
     * @return
     */
    public VideoPlayerState getVideoPlayerState(){
        return mVideoPlayerState;
    }

    /**
     * 检查播放器内部状态
     */
    public void checkedVidepPlayerState(){
        if(null!=mListener){
            mListener.onVideoPlayerState(mVideoPlayerState,null);
        }
    }

    /**
     * 组件对应生命周期调用
     */
    public void onResume(){
        if(null!=mVideoPlayer&&mVideoPlayerState.equals(VideoPlayerState.MUSIC_PLAYER_PAUSE)){
            mVideoPlayer.resume();
            VideoPlayerManager.this.mVideoPlayerState=VideoPlayerState.MUSIC_PLAYER_PLAY;
            if(null!=mListener){
                mListener.onVideoPlayerState(mVideoPlayerState,null);
            }
        }
    }

    /**
     * 组件对应生命周期调用
     */
    public void onPause(){
        Logger.d(TAG,"onPause:"+mVideoPlayerState);
        if(null!=mVideoPlayer&&!TextUtils.isEmpty(mDataSource)){
            if(mVideoPlayerState.equals(VideoPlayerState.MUSIC_PLAYER_START)||mVideoPlayerState.equals(VideoPlayerState.MUSIC_PLAYER_PLAY)){
                Logger.d(TAG,"onPause-->onPause");
                mVideoPlayer.pause();
                VideoPlayerManager.this.mVideoPlayerState=VideoPlayerState.MUSIC_PLAYER_PAUSE;
            }else if(mVideoPlayerState.equals(VideoPlayerState.MUSIC_PLAYER_COMPLETION)
                    ||mVideoPlayerState.equals(VideoPlayerState.MUSIC_PLAYER_STOP)
                    ||mVideoPlayerState.equals(VideoPlayerState.MUSIC_PLAYER_ERROR)){
                Logger.d(TAG,"onPause-->onStop");
                RUN_EVENT=0;
                mVideoPlayer.stopPlay(false);
                VideoPlayerManager.this.mVideoPlayerState=VideoPlayerState.MUSIC_PLAYER_STOP;
            }
            if(null!=mListener){
                mListener.onVideoPlayerState(mVideoPlayerState,null);
            }
        }
    }

    /**
     * 组件对应生命周期调用
     */
    public void onDestroy(){
        stop();
        mContext=null;
        if(null!=mAudioFocusManager){
            mAudioFocusManager.releaseAudioFocus();
            mAudioFocusManager.onDestroy();
            mAudioFocusManager=null;
        }
        RUN_EVENT=0;
        if(null!=mVideoPlayer){
            mVideoPlayer.stopPlay(true);
            mVideoPlayer=null;
        }
        if(null!=mVideoView){
            mVideoView.removeVideoView();
            mVideoView.onDestroy();
            mVideoView=null;
        }
    }
}