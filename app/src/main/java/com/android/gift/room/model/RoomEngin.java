package com.android.gift.room.model;

import android.content.Context;
import com.android.gift.base.BaseEngin;
import com.android.gift.net.OkHttpUtils;
import com.android.gift.net.OnResultCallBack;

/**
 * hty_Yuye@Outlook.com
 * 2019/7/10
 * 直播间
 */

public class RoomEngin extends BaseEngin {

    /**
     * 获取在线直播间列表
     * @param callBack 回调监听器
     */
    public void getGiftRooms(final Context context,final OnResultCallBack callBack){
        if(null==callBack){
            return;
        }
        OkHttpUtils.get("https://raw.githubusercontent.com/Yuye584312311/ConfigFile/master/index/rooms.json",callBack);
    }
}