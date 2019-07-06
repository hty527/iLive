package com.android.gift.gift.listener;

import com.android.gift.bean.GiftItemInfo;
import com.android.gift.bean.UserInfo;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/5
 * 礼物赠送接口监听
 */

public interface OnGiveGiftListener {

    /**
     * 礼物赠送意图
     * @param giftItemInfo 礼物信息
     * @param userInfo 接收人信息
     * @param roomid 房间ID
     * @param count 赠送数量
     */
    void onReceiveGift(GiftItemInfo giftItemInfo,UserInfo userInfo,String roomid,int count);

    /**
     * 用户选中了新的礼物对象
     * @param giftItemInfo 礼物对象
     */
    void onReceiveNewGift(GiftItemInfo giftItemInfo);

    /**
     * 选中的礼物个数发生了变化，这个方法和onReceiveGift不会同时回调
     * @param count 礼物个数
     */
    void onReceiveGiftCount(int count);
}