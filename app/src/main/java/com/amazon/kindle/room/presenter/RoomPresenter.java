package com.amazon.kindle.room.presenter;

import com.amazon.kindle.base.BasePresenter;
import com.amazon.kindle.bean.ResultData;
import com.amazon.kindle.bean.ResultList;
import com.amazon.kindle.net.OkHttpUtils;
import com.amazon.kindle.net.OnResultCallBack;
import com.amazon.kindle.room.bean.InkeRoomData;
import com.amazon.kindle.room.bean.RoomItem;
import com.amazon.kindle.room.contract.RoomContact;
import com.amazon.kindle.room.model.RoomEngin;

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
     * 获取在线直播间
     * @param page 页眉，头部推荐数据为0,常规推荐为>0
     * @param offset 数据集起始偏移位置
     */
    @Override
    public void getRooms(int page,int offset) {
        if(null!=mViewRef&&null!=mViewRef.get()){
            mViewRef.get().showLoading(offset);
            getNetEngin().get().getGiftRooms(page,offset,new OnResultCallBack<InkeRoomData>() {

                @Override
                public void onResponse(InkeRoomData data) {
                    if(null!=mViewRef&&null!=mViewRef.get()){
                        if(null!=data.getCards()&&data.getCards().size()>0){
                            mViewRef.get().showRooms(data);
                        }else{
                            mViewRef.get().showRoomsError(OkHttpUtils.ERROR_EMPTY,"主播正在赶来~请稍后...");
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

    /**
     * 获取在线1V1主播列表
     */
    @Override
    public void getPrivateRooms() {
        if(null!=mViewRef&&null!=mViewRef.get()){
            mViewRef.get().showLoading(0);
            getNetEngin().get().getPrivateGiftRooms(new OnResultCallBack<ResultData<ResultList<RoomItem>>>() {

                @Override
                public void onResponse(ResultData<ResultList<RoomItem>> data) {
                    if(null!=mViewRef&&null!=mViewRef.get()){
                        if(null!=data.getData()&&null!=data.getData().getList()&&data.getData().getList().size()>0){
                            mViewRef.get().showPrivateRooms(data.getData().getList());
                        }else{
                            mViewRef.get().showRoomsError(OkHttpUtils.ERROR_EMPTY,"主播正在赶来~请稍后...");
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