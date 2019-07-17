package com.android.live.player.lib.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by TinyHung@outlook.com
 * 2019/4/20
 * Base Video\Live Controller
 */

public abstract class BaseVideoController extends FrameLayout {

    protected static final String TAG = "VideoBaseController";

    public BaseVideoController(@NonNull Context context) {
        this(context,null);
    }

    public BaseVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BaseVideoController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //准备播放中
    public abstract void readyPlaying();
    //开始缓冲中
    public abstract void startBuffer();
    //缓冲结束
    public abstract void endBuffer();
    //开始播放中
    public abstract void play();
    //已暂停播放
    public abstract void pause();
    //已回复播放
    public abstract void repeatPlay();
    //移动网络状态下工作
    public abstract void mobileWorkTips();
    //播放失败
    public abstract void error(int errorCode,String errorMessage);
    //播放器被重置
    public abstract void reset();
    //实时播放状态
    protected void onTaskRuntime(long totalDurtion, long currentDurtion,int bufferPercent,boolean isPlaying){}
    //缓冲百分比
    protected void onBufferingUpdate(int percent){}
    //播放资源地址为空
    public abstract void pathInvalid();
    //播放器被销毁
    public abstract void onDestroy();

    /**
     * 非必须的，根据自身业务逻辑实现
     */
    //设置视频标题内容
    protected void setTitle(String videoTitle){}

    //播放器空白位置单击事件
    public void changeControllerState(){}

    public interface OnStartListener{
        void onStartPlay();
    }
    protected OnStartListener mOnStartListener;

    public void setOnStartListener(OnStartListener onStartListener) {
        mOnStartListener = onStartListener;
    }
}
