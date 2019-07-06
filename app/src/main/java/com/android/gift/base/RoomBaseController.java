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
     * 新的推送消息
     * @param customMsgInfo 消息实体
     * @param isSystemPro 是否来自远程推送
     */
    public abstract void newSystemCustomMessage(CustomMsgInfo customMsgInfo, boolean isSystemPro);

    /**
     * 新的文本(群聊天)消息
     * @param customMsgInfo 消息实体
     * @param isSystemPro 是否来自远程推送
     */
    public abstract void onNewTextMessage(CustomMsgInfo customMsgInfo, boolean isSystemPro);

    /**
     * 新的轻量级消息
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

    public abstract void startReckonTime();

    public abstract void stopReckonTime();

    public abstract void onResume();

    public abstract void onPause();

    public abstract void onDestroy();

}
