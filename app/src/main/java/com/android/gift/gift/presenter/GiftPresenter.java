package com.android.gift.gift.presenter;

import android.content.Context;
import com.android.gift.base.BasePresenter;
import com.android.gift.bean.GiftItemInfo;
import com.android.gift.bean.GiftType;
import com.android.gift.bean.ResultData;
import com.android.gift.bean.ResultList;
import com.android.gift.gift.contract.GiftContact;
import com.android.gift.gift.manager.GiftBoardManager;
import com.android.gift.model.GiftEngin;
import com.android.gift.net.OkHttpUtils;
import com.android.gift.net.OnResultCallBack;
import java.util.ArrayList;
import java.util.List;

/**
 * TinyHung@outlook.com
 * 2018/5/15
 * 礼物Presenter
 */

public class GiftPresenter extends BasePresenter<GiftContact.View,GiftEngin> implements GiftContact.Presenter<GiftContact.View> {

    @Override
    protected GiftEngin createEngin() {
        return new GiftEngin();
    }

    /**
     * 获取礼物分类
     * @param context 全局上下文
     */
    @Override
    public void getGiftsType(Context context) {
        if(null!=mViewRef&&null!=mViewRef.get()){
            List<GiftType> giftTypes = GiftBoardManager.getInstance().getGiftTypes();
            if(null!=giftTypes&&giftTypes.size()>0){
                mViewRef.get().showGiftTypes(giftTypes);
                return;
            }
            mViewRef.get().showLoading();
            getNetEngin().get().getGiftType(context, new OnResultCallBack<ResultData<ResultList<GiftType>>>() {

                @Override
                public void onResponse(ResultData<ResultList<GiftType>> data) {
                    if(null!=mViewRef&&null!=mViewRef.get()){
                        if(null!=data.getData()&&null!=data.getData().getList()&&data.getData().getList().size()>0){
                            GiftBoardManager.getInstance().setGiftTypes(data.getData().getList());
                            mViewRef.get().showGiftTypes(data.getData().getList());
                        }else{
                            mViewRef.get().showGiftTypesError(OkHttpUtils.ERROR_EMPTY,"礼物数据为空");
                        }
                    }
                }

                @Override
                public void onError(int code, String errorMsg) {
                    if(null!=mViewRef&&null!=mViewRef.get()){
                        mViewRef.get().showGiftTypesError(code,errorMsg);
                    }
                }
            });
        }
    }

    /**
     * 根据礼物Typeh获取礼物分类
     * @param context 全局上下文
     * @param type 礼物分类
     */
    @Override
    public void getGiftsByType(Context context, final String type) {
        if(null!=mViewRef&&null!=mViewRef.get()){
            List<GiftItemInfo> giftItemInfos = GiftBoardManager.getInstance().getGiftItemInfos(type);
            if(null!=giftItemInfos&&giftItemInfos.size()>0){
                mViewRef.get().showGifts(giftItemInfos,type);
                return;
            }

            mViewRef.get().showLoading();
            getNetEngin().get().getGiftByType(context, type, new OnResultCallBack<ResultData<ResultList<GiftItemInfo>>>() {

                @Override
                public void onResponse(ResultData<ResultList<GiftItemInfo>> data) {
                    if(null!=mViewRef&&null!=mViewRef.get()){
                        if(null!=data.getData()&&null!=data.getData().getList()&&data.getData().getList().size()>0){
                            GiftBoardManager.getInstance().setGiftItemInfos((ArrayList<GiftItemInfo>) data.getData().getList(),type);
                            mViewRef.get().showGifts(data.getData().getList(),type);
                        }else{
                            mViewRef.get().showGiftError(OkHttpUtils.ERROR_EMPTY,type,"礼物为空");
                        }
                    }
                }

                @Override
                public void onError(int code, String errorMsg) {
                    if(null!=mViewRef&&null!=mViewRef.get()){
                        mViewRef.get().showGiftError(code,type,errorMsg);
                    }
                }
            });
        }
    }
}