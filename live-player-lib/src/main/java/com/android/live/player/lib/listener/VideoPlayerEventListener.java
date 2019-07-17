package com.android.live.player.lib.listener;

import com.android.live.player.lib.model.VideoPlayerState;

/**
 * TinyHung@Outlook.com
 * 2019/4/20
 * Video\Live Player Event Listener
 */

public interface VideoPlayerEventListener {

    /**
     * 播放器所有状态回调
     * @param playerState 播放器内部状态
     */
    void onVideoPlayerState(VideoPlayerState playerState, String message);

    /**
     * 播放器准备好了
     * @param totalDurtion 总时长
     */
    void onPrepared(long totalDurtion);

    /**
     * 缓冲百分比
     * @param percent 百分比
     */
    void onBufferingUpdate(int percent);

    /**
     * 播放器反馈信息
     * @param event 事件
     * @param extra
     */
    void onInfo(int event, int extra);

    /**
     * 音频地址无效,组件可处理付费购买等逻辑
     */
    void onVideoPathInvalid();

    /**
     * @param totalDurtion 音频总时间
     * @param currentDurtion 当前播放的位置
     * @param bufferPercent 缓冲进度，从常规默认切换至全屏、小窗时，应该关心此进度
     * @param isPlaying 部分视频格式的视频播放当缓冲结束后的状态无法准确获取，此标记标识是否是正在播放
     */
    void onTaskRuntime(long totalDurtion, long currentDurtion, int bufferPercent,boolean isPlaying);

    /**
     * 销毁
     */
    void destroy();
}