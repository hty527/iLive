package com.android.live.player.lib.listener;

/**
 * TinyHung@Outlook.com
 * 2019/4/20
 * Video\Live Player Listener
 */

public abstract class VideoPlayerListener {
    /**
     * 播放开始
     */
    public void onStart(){}

    /**
     * 缓冲进度
     * @param progress 已经缓冲的数据量占整个视频时长的百分比
     */
    public void onBufferingUpdate(int progress){}

    /**
     * 播放进度
     * @param currentDurtion 实时播放位置
     * @param totalDurtion 总长度
     */
    public void onPlayingProgress(long currentDurtion,long totalDurtion){}

    /**
     * 播放中各种状态
     */
    public void onStatus(int event){}

    /**
     * 播放结束
     */
    public void onCompletion(){}

    /**
     * 失败
     * @param errorCode
     */
    public void onError(int errorCode){}
}
