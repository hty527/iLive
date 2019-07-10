package com.android.gift.room.presenter;

import android.content.Context;
import com.android.gift.base.BasePresenter;
import com.android.gift.bean.ResultData;
import com.android.gift.bean.ResultList;
import com.android.gift.net.OkHttpUtils;
import com.android.gift.net.OnResultCallBack;
import com.android.gift.room.bean.RoomItem;
import com.android.gift.room.contract.RoomContact;
import com.android.gift.room.model.RoomEngin;

/**
 * TinyHung@outlook.com
 * 2019/7/10
 * 直播间
 */

public class RoomPresenter extends BasePresenter<RoomContact.View,RoomEngin> implements RoomContact.Presenter<RoomContact.View> {

    @Override
    protected RoomEngin createEngin() {
        return new RoomEngin();
    }

    /**
     * 获取直播间
     * @param context 全局上下文
     */
    @Override
    public void getRooms(Context context) {
        if(null!=mViewRef&&null!=mViewRef.get()){
            mViewRef.get().showLoading();
            getNetEngin().get().getGiftRooms(context, new OnResultCallBack<ResultData<ResultList<RoomItem>>>() {

                @Override
                public void onResponse(ResultData<ResultList<RoomItem>> data) {
                    if(null!=mViewRef&&null!=mViewRef.get()){
                        if(null!=data.getData()&&null!=data.getData().getList()&&data.getData().getList().size()>0){
                            mViewRef.get().showRooms(data.getData().getList());
                        }else{
                            mViewRef.get().showRoomsError(OkHttpUtils.ERROR_EMPTY,"暂无直播间列表");
                        }
                    }
                }

                @Override
                public void onError(int code, String errorMsg) {
                    if(null!=mViewRef&&null!=mViewRef.get()){
                        mViewRef.get().showRoomsError(code,errorMsg);
                    }
                }
            });
        }
    }
}