package com.android.gift.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.android.gift.room.bean.CustomMsgInfo;
import com.android.gift.room.bean.NumberChangedInfo;

/**
 * TinyHung@Outlook.com
 * 2019/2/22
 * 直播间控制器父类
 * 此控制器提供了IM消息入口，分别为newSystemCustomMessage,onNewTextMessage,onNewMinMessage三个方法，
 * 你需要在你持有此组件的Acticity中收到IM消息后，调用此组件进行通信，关于消息的CMD类型，Constants内置了基本的消息CMD
 */

public abstract class RoomBaseController extends FrameLayout{

    public RoomBaseController(@NonNull Context context) {
        this(context,null);
    }

    public RoomBaseController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 需要实现的
     * @param flag 软键盘是否弹起
     * @param keyBordHeight 软键盘的高度
     */
    public abstract void showInputKeyBord(boolean flag, int keyBordHeight);

    /**
     * 来自本地和远端的IM新的推送消息
     * @param customMsgInfo 消息实体
     * @param isSystemPro 是否来自远程推送
     */
    public abstract void newSystemCustomMessage(CustomMsgInfo customMsgInfo, boolean isSystemPro);

    /**
     * 来自本地和远端的IM新的文本(群聊天)消息
     * @param customMsgInfo 消息实体
     * @param isSystemPro 是否来自远程推送
     */
    public abstract void onNewTextMessage(CustomMsgInfo customMsgInfo, boolean isSystemPro);

    /**
     * 来自本地和远端的IM新的轻量级消息
     * @param groupId 接收群ID
     * @param sender  发送者(群ID)
     * @param changedInfo 消息实体
     */
    public abstract void onNewMinMessage(String groupId, String sender, NumberChangedInfo changedInfo);

    /**
     * 返回当前主播本次直播的时长
     * @return
     */
    public abstract long getSecond();

    /**
     * 开始直播时长时间录制
     */
    public abstract void startReckonTime();

    /**
     * 结束直播时长时间录制
     */
    public abstract void stopReckonTime();

    /**
     * Activity中礼物面板被打开了
     */
    public abstract void closeGiftBoard();

    /**
     * Activity中礼物面板被关闭了
     */
    public abstract void showGiftBoard();

    /**
     * 组件生命周期可见
     */
    public abstract void onResume();

    /**
     * 组件生命周期不可见
     */
    public abstract void onPause();

    /**
     * 组件生命周期销毁
     */
    public abstract void onDestroy();
}