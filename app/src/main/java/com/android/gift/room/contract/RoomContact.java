package com.android.gift.room.contract;

import com.android.gift.base.BaseContract;
import com.android.gift.room.bean.InkeRoomData;
import com.android.gift.room.bean.RoomItem;
import java.util.List;

/**
 * TinyHung@outlook.com
 * 2019/7/10
 * 直播间
 */

public interface RoomContact {

    interface View extends BaseContract.BaseView {

        /**
         * 显示在线直播间列表
         * @param data 直播间列表
         */
        void showRooms(InkeRoomData data);

        /**
         * 显示1V1直播间列表
         * @param data 直播间列表
         */
        void showPrivateRooms(List<RoomItem> data);

        /**
         * 获取直播间列表失败
         * @param code 错误码
         * @param errMsg 描述信息
         */
        void showRoomsError(int code, String errMsg);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {

        /**
         * 获取在线直播间列表
         * @param offset 数据集起始偏移位置
         */
        void getRooms(int offset);

        /**
         * 获取在线1V1列表
         */
        void getPrivateRooms();
    }
}