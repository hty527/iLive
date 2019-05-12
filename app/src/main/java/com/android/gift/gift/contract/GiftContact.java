package com.android.gift.gift.contract;

import android.content.Context;
import com.android.gift.base.BaseContract;
import com.android.gift.bean.GiftItemInfo;
import com.android.gift.bean.GiftType;
import java.util.List;

/**
 * TinyHung@outlook.com
 * 2018/5/15
 * 礼物Contact
 */

public interface GiftContact {

    interface View extends BaseContract.BaseView {

        /**
         * 显示礼物分类列表
         * @param data 分类列表
         */
        void showGiftTypes(List<GiftType> data);

        /**
         * 获取礼物分类失败
         * @param code 错误码
         * @param errMsg 描述信息
         */
        void showGiftTypesError(int code,String errMsg);

        /**
         * 显示所有礼物信息
         * @param data 礼物列表
         * @param type 礼物类别
         */
        void showGifts(List<GiftItemInfo> data, String type);

        /**
         * 礼物加载失败或者为空
         * @param code 错误码，3002 为空，其他为失败
         * @param type 礼物类别
         * @param errMsg 描述信息
         */
        void showGiftError(int code,String type,String errMsg);

        /**
         * 礼物交易成功
         * @param giftItemInfo 礼物信息
         * @param giftCount 礼物数量
         * @param isDoubleClick 是否连击
         */
        void showGivePresentSuccess(GiftItemInfo giftItemInfo, int giftCount, boolean isDoubleClick);

        /**
         * 礼物交易失败
         * @param code 错误码 -1：金币不足，需充值
         * @param errMsg 描述信息
         */
        void showGivePresentError(int code, String errMsg);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {

        /**
         * 获取礼物分类
         * @param context 全局上下文
         */
        void getGiftsType(Context context);

        /**
         * 根据礼物分类获取礼物列表
         * @param context 全局上下文
         * @param type 礼物分类
         */
        void getGiftsByType(Context context, String type);

        /**
         * 发送人，接收人，礼物ID，房号,是否连击模式,应用场景
         * @param giftItemInfo 包含礼物ID的礼物信息
         * @param acceptUserID 接收人
         * @param giftCount 礼物数量
         * @param roomID 房间ID
         * @param isDoubleClick 是否连击
         * @param sceneType 场景
         */
        void givePresentGift(GiftItemInfo giftItemInfo, String acceptUserID, int giftCount, String roomID, boolean isDoubleClick, int sceneType);
    }
}